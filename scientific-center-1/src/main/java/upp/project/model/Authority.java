package upp.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Authority implements GrantedAuthority {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name = "name")
	private Role name;

	public Authority() {

	}

	public Authority(Role name) {
		this.name = name;
	}

	@Override
	public String getAuthority() {
		return name.name();
	}

	public void setName(Role name) {
		this.name = name;
	}

	@JsonIgnore
	public Role getName() {
		return name;
	}

	@JsonIgnore
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
