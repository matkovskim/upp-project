package upp.project.aasecurity.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import upp.project.aasecurity.JwtProvider;

public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

	private JwtProvider jwtProvider;

	private UserDetailsService userDetailsService;

	public JwtAuthenticationTokenFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
		super();
		this.jwtProvider = jwtProvider;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		IdentityService identityService = SpringContext.getBean(IdentityService.class);

		String username;
		String authenticationToken = jwtProvider.getToken(request);
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, x-requested-with, authorization");

		if (authenticationToken != null) {
			username = jwtProvider.getUsernameFromToken(authenticationToken);
			if (username != null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (jwtProvider.validateToken(authenticationToken, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

					List<Group> groups = identityService.createGroupQuery().groupMember(username).list();
					List<String> groupsIds = new ArrayList<String>();
					for (Group g : groups) {
						groupsIds.add(g.getId());
					}
					Authentication a = new Authentication(username, groupsIds);
					identityService.setAuthentication(a);
					identityService.setAuthenticatedUserId(username);
					System.out.println("FILTER: ulogovan je: " + username);
					System.out.println("authentication: " + a);
				}
			}
		}

		if (identityService.getCurrentAuthentication() == null) {
			authentificateGuest();
		} else {
			if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				try {
					filterChain.doFilter(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		}

	}

	private void authentificateGuest() {
		System.out.println("logujem gost");
		IdentityService identityService = SpringContext.getBean(IdentityService.class);
		List<String> gosti = new ArrayList<>();
		gosti.add("gosti");
		Authentication a = new Authentication("gost", gosti);
		identityService.setAuthentication(a);
		identityService.setAuthenticatedUserId("gost");
		System.out.println("POSTAVIO " + identityService.getCurrentAuthentication().getUserId());

	}

}
