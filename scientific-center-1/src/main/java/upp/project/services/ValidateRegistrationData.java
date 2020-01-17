package upp.project.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.FormSubmissionDto;
import upp.project.model.RegistredUser;

@Service
public class ValidateRegistrationData implements JavaDelegate {

	@Autowired
	AuthentificationService authentificationService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("newUser");
		RegistredUser user = authentificationService.registerNewUser(dto);

		// Validacije
		if (user == null) {
			execution.setVariable("isValid", false);
		}

		else if (user.getId() == -1) {
			execution.setVariable("isValid", false);
		}

		else if (!authentificationService.isValidEmailAddress(user.getEmail())) {
			execution.setVariable("isValid", false);
		}

		else {
			execution.setVariable("isValid", true);
		}

	}

}