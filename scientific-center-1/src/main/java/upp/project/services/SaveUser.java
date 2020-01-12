package upp.project.services;

import java.security.SecureRandom;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Authority;
import upp.project.model.FormSubmissionDto;
import upp.project.model.RegistredUser;
import upp.project.model.Role;
import upp.project.repository.RegistredUserRepository;

@Service
public class SaveUser implements JavaDelegate {

	@Autowired
	AuthorityService authorityService;

	@Autowired
	AuthentificationService authentificationService;

	@Autowired
	RegistredUserRepository registredUserRepository;

	static final String AB = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	static SecureRandom rnd = new SecureRandom();

	private String randomString(int len) {
		StringBuilder sb = new StringBuilder(len);
		for (int i = 0; i < len; i++)
			sb.append(AB.charAt(rnd.nextInt(AB.length())));
		return sb.toString();
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("newUser");
		RegistredUser newUser = authentificationService.registerNewUser(dto);

		newUser.setRegistrationCode(this.randomString(50));
		System.out.println(newUser.getRegistrationCode());
		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(authorityService.findByName(Role.ROLE_REG_USER));
		newUser.setAuthorities(authorities);
		registredUserRepository.save(newUser);

	}

}
