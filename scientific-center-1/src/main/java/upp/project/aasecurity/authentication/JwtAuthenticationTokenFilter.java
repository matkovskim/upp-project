package upp.project.aasecurity.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
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

		String username;
		String authenticationToken = jwtProvider.getToken(request);
		HttpServletResponse res = (HttpServletResponse) response;
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, x-requested-with, authorization");

		if (authenticationToken != null) {
			username = jwtProvider.getUsernameFromToken(authenticationToken);

			if (username != null) {
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);

				if (jwtProvider.validateToken(authenticationToken, userDetails)) {
					JwtBasedAuthentication authentication = new JwtBasedAuthentication(userDetails);
					authentication.setUserToken(authenticationToken);
					SecurityContextHolder.getContext().setAuthentication(authentication);
					
				}
			}
		}
		
		   if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
	            response.setStatus(HttpServletResponse.SC_OK);
	        }else {
	        	filterChain.doFilter(request,response);
	        }
		

	}

}
