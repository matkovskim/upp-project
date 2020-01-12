package upp.project.services;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.RegistredUser;

@Service
public class ValidationService implements JavaDelegate {

	@Autowired
	IdentityService identityService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		RegistredUser newUser = (RegistredUser) execution.getVariable("newUser");
		if (newUser != null) {
			if (newUser.getCity() == null || newUser.getCity() == "") {
				execution.setVariable("isValid", false);
			}
			else if (newUser.getName() == null || newUser.getName() == "") {
				execution.setVariable("isValid", false);
			}
			else if (newUser.getLastName() == null || newUser.getLastName() == "") {
				execution.setVariable("isValid", false);
			}
			else if (newUser.getState() == null || newUser.getState() == "") {
				execution.setVariable("isValid", false);
			}
			else if (newUser.getPassword() == null || newUser.getPassword() == "") {
				execution.setVariable("isValid", false);
			}
			else if (newUser.getEmail() == null || newUser.getEmail() == "") {
				execution.setVariable("isValid", false);
			}
			else if (newUser.getScientificArea().size() == 0) {
				execution.setVariable("isValid", false);
			}
			else if(!isValidEmailAddress(newUser.getEmail())) {
				execution.setVariable("isValid", false);
			}
			else {
				execution.setVariable("isValid", true);
			}
		}
		else {
			execution.setVariable("isValid", false);
		}
		System.out.println(execution.getVariable("isValid"));
	}

	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

}
