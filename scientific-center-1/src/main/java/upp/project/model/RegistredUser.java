package upp.project.model;

import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class RegistredUser implements UserDetails{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	String username;
	
	@Column
	String name;

	@Column
	String lastName;

	@Column
	String city;

	@Column
	String state;

	@Column
	String email;

	@Column
	String password;

	@Column
	String title;
	
	@Column
	String registrationCode;

	@Column
	boolean reviewer;

	@Column
	boolean confirmed;

	@ManyToMany
	Set<ScientificArea> scientificArea;
	
	@ManyToMany(fetch = FetchType.EAGER)
	protected Set<Authority> authorities;

	public RegistredUser() {
		confirmed = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isReviewer() {
		return reviewer;
	}

	public void setReviewer(boolean reviewer) {
		this.reviewer = reviewer;
	}

	public Set<ScientificArea> getScientificArea() {
		return scientificArea;
	}

	public void setScientificArea(Set<ScientificArea> scientificArea) {
		this.scientificArea = scientificArea;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isConfirmed() {
		return confirmed;
	}

	public void setConfirmed(boolean confirmed) {
		this.confirmed = confirmed;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	  public Collection<? extends GrantedAuthority> getAuthorities() {
	    return this.authorities;
	  }

	  @Override
	  public boolean isEnabled() {
	    return true;
	  }

	  @Override
	  public String getUsername() {
	        return username;
	    }
	  
	  @JsonIgnore
	  @Override
	  public boolean isAccountNonExpired() {
	    return true;
	  }

	  @JsonIgnore
	  @Override
	  public boolean isAccountNonLocked() {
	    return true;
	  }

	  @JsonIgnore
	  @Override
	  public boolean isCredentialsNonExpired() {
	    return true;
	  }

	public String getRegistrationCode() {
		return registrationCode;
	}

	public void setRegistrationCode(String registrationCode) {
		this.registrationCode = registrationCode;
	}
}
