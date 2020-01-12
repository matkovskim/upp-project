package upp.project.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Authority;
import upp.project.model.Role;
import upp.project.repository.AuthorityRespository;

@Service
public class AuthorityService {

	@Autowired
	private AuthorityRespository authorityRespository;
	
	public Authority findOne(Long id) {
		Optional<Authority> authority = this.authorityRespository.findById(id);
		if (authority.isPresent()) {
			return authority.get();
		} else {
			return null;
		}
	}

	public List<Authority> findAll() {
		return authorityRespository.findAll();
	}

	public Authority save(Authority authority) {
		return authorityRespository.save(authority); 
	}

	public void remove(Long id) {
		authorityRespository.deleteById(id);
	}
	
	public Authority findByName(Role name)
	{
		return authorityRespository.findByName(name);
	}

}
