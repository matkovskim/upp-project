package upp.project.services;

import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.RegistredUser;
import upp.project.repository.RegistredUserRepository;

@Service
public class SaveConfirmation implements JavaDelegate {

	@Autowired
	private RegistredUserRepository registredUserRepository;
	
	@Autowired
	private IdentityService identityService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String username = (String) execution.getVariable("KorisnickoIme");
		RegistredUser savedUser = registredUserRepository.findByUsername(username);
		savedUser.setConfirmed(true);
		registredUserRepository.save(savedUser);
		
		registerInCamunda(savedUser);		
		identityService.createMembership(savedUser.getUsername(), "korisnici");
	}


	private void registerInCamunda(RegistredUser newUser) {
		try {
			User camundaUser = identityService.newUser(newUser.getUsername());
			camundaUser.setPassword(newUser.getPassword());
			camundaUser.setFirstName(newUser.getName());
			camundaUser.setLastName(newUser.getLastName());
			camundaUser.setEmail(newUser.getEmail());
			identityService.saveUser(camundaUser);
		}
		catch (Exception e) {
			System.out.println("Korisnik vec postoji");
		}
	}
}
