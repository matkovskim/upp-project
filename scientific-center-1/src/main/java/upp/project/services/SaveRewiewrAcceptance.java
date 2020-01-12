package upp.project.services;

import java.util.HashSet;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
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

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String username = (String) execution.getVariable("KorisnickoIme");
		boolean reviewer = (boolean) execution.getVariable("Recenzent");
				
		if (reviewer==true) {
			RegistredUser savedUser = registredUserRepository.findByUsername(username);

			Set<Authority> authorities = (Set<Authority>) savedUser.getAuthorities();
			authorities.add(authorityService.findByName(Role.ROLE_REWIEWER));
			savedUser.setAuthorities(authorities);

			registredUserRepository.save(savedUser);
		}

	}

}