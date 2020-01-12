package upp.project.aasecurity;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import upp.project.model.RegistredUser;

@Component
public class JwtProvider {

	@Value("ScientificCenter1 ")
	private String APPLICATION_NAME;

	
	private int jwtExpiration = 36000;

	@Value("jwtSecretKeyVM2")
	private String jwtSecret;
	
	@Value("Authorization")
	private String AUTH_HEADER;

	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

	public String generateJwtToken(String username, int hours) {
		return Jwts.builder().setIssuer(APPLICATION_NAME).setSubject(username).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpiration * hours * 100))
				.signWith(SIGNATURE_ALGORITHM, jwtSecret).compact();
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		RegistredUser user = (RegistredUser) userDetails;
		final String username = getUsernameFromToken(token);
		final Date created = getIssuedAtDateFromToken(token);

		return (username != null && username.equals(userDetails.getUsername()));
	}

	public Boolean canTokenBeRefreshed(String token) {
		final Date created = this.getIssuedAtDateFromToken(token);
		return (!(this.isTokenExpired(token)));
	}
	
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			claims.setIssuedAt(new Date());
			refreshedToken = Jwts.builder()
					.setClaims(claims)
					.setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000))
					.signWith(SIGNATURE_ALGORITHM, jwtSecret).compact();
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}


	private Boolean isTokenExpired(String token) {
		final Date expiration = this.getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public String getUsernameFromToken(String token) {
		String username;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			username = claims.getSubject();
		} catch (Exception e) {
			username = null;
		}
		return username;
	}
	
	
	public Date getIssuedAtDateFromToken(String token) {
		Date issueAt;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		} catch (Exception e) {
			issueAt = null;
		}
		return issueAt;
	}

	
	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	
	public String getToken(HttpServletRequest request) {
		String authHeader = (String)getAuthHeaderFromHeader(request);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		return null;
	}

	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}

	public int getJwtExpiration() {
		return jwtExpiration;
	}
	
}
