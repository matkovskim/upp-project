package upp.project.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

public class UserTokenState {

	private String token;
	private String type = "Bearer";
	private String email;
	private Collection<? extends GrantedAuthority> authorities;
	private Date expiratonDate;
	
	public UserTokenState() {
		super();
		this.token = null;
		this.email = null;
		this.authorities = null;
	}
	
	public UserTokenState(String token,String email, Collection<? extends GrantedAuthority> collection, Date date) {
		super();
		this.token = token;
		this.email = email;
		this.authorities = collection;
		this.expiratonDate = date;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Collection<Authority> authorities) {
		this.authorities = authorities;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getExpiratonDate() {
		return expiratonDate;
	}

	public void setExpiratonDate(Date expiratonDate) {
		this.expiratonDate = expiratonDate;
	}
	
}
