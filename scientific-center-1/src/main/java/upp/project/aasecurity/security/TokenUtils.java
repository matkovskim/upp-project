package upp.project.aasecurity.security;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class TokenUtils {

	@Value("scientific-center-1")
	private String APP_NAME;

	@Value("secret")
	public String SECRET;

	@Value("3000")
	private int EXPIRES_IN;

	@Value("Authorization")
	private String AUTH_HEADER;

	static final String AUDIENCE_UNKNOWN = "unknown";
	static final String AUDIENCE_WEB = "web";

	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

	// Functions for generating new JWT token

	public String generateToken(String username) {
		return Jwts.builder().setIssuer(APP_NAME).setSubject(username).setIssuedAt(new Date())
				.setExpiration(generateExpirationDate()).signWith(SIGNATURE_ALGORITHM, "secret").compact();
	}

	private Date generateExpirationDate() {
		long expiresIn = EXPIRES_IN;
		return new Date(new Date().getTime() + expiresIn * 1000);
	}

	// Functions for refreshing JWT token
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			claims.setIssuedAt(new Date());
			refreshedToken = Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
					.signWith(SIGNATURE_ALGORITHM, SECRET).compact();
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}

	// Functions for validating JWT token data

	public Boolean validateToken(String token) {

		final String username = getUsernameFromToken(token);
		final Date created = getIssuedAtDateFromToken(token);

		return (username != null);

	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = this.getExpirationDateFromToken(token);
		if (expiration == null)
			return true;
		return expiration.before(new Date());
	}

	// Functions for getting data from token

	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(SECRET).parseClaimsJws(token).getBody();
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

	public String getAudienceFromToken(String token) {
		String audience;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			audience = claims.getAudience();
		} catch (Exception e) {
			audience = null;
		}
		return audience;
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

	public int getExpiredIn() {
		return EXPIRES_IN;
	}

	// Functions for getting JWT token out of HTTP request

	public String getToken(HttpServletRequest request) {
		String authHeader = getAuthHeaderFromHeader(request);

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		return null;
	}

	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}

}
