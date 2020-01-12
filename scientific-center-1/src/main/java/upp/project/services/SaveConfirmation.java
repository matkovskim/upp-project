package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.RegistredUser;
import upp.project.repository.RegistredUserRepository;

@Service
public class SaveConfirmation implements JavaDelegate {

	@Autowired
	RegistredUserRepository registredUserRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String username = (String) execution.getVariable("KorisnickoIme");
		RegistredUser savedUser = registredUserRepository.findByUsername(username);
		savedUser.setConfirmed(true);
		registredUserRepository.save(savedUser);
	}

}
