package upp.project.aasecurity.authentication;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

public class JwtBasedAuthentication extends AbstractAuthenticationToken {

	private String userToken;
	private final UserDetails userDetails;


	public JwtBasedAuthentication(UserDetails userDetails) {
		super(userDetails.getAuthorities());
		this.userDetails = userDetails;
		// TODO Auto-generated constructor stub
	}

	
	@Override
	public Object getCredentials() {
		// TODO Auto-generated method stub
		return userToken;
	}

	@Override
	public Object getPrincipal() {
		// TODO Auto-generated method stub
		return userDetails;
	}

	@Override
	public boolean isAuthenticated() {
		return true;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

}
