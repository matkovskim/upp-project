package upp.project.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Authority;
import upp.project.model.RegistredUser;
import upp.project.model.Role;
import upp.project.repository.RegistredUserRepository;

@Service
public class SaveRewiewrAcceptance implements JavaDelegate {

	@Autowired
	RegistredUserRepository registredUserRepository;

	@Autowired
	AuthorityService authorityService;
	
	@Autowired
	IdentityService identityService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String username = (String) execution.getVariable("KorisnickoIme");
		boolean reviewer = (boolean) execution.getVariable("Recenzent");
				
		if (reviewer==true) {
			
			RegistredUser savedUser = registredUserRepository.findByUsername(username);
			
			identityService.createMembership(savedUser.getUsername(), "recenzenti");

			Set<Authority> authorities = new HashSet<Authority>();
			authorities.add(authorityService.findByName(Role.ROLE_REWIEWER));
			savedUser.setAuthorities(authorities);

			registredUserRepository.save(savedUser);
			
		}

	}

}