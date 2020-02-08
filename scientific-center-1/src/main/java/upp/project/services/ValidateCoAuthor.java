package upp.project.services;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ValidateCoAuthor implements JavaDelegate {

	@Autowired
	MagazineService magazineService;

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String name = (String) execution.getVariable("ime");
		String lastName = (String) execution.getVariable("prezime");
		String email = (String) execution.getVariable("email");
		String city = (String) execution.getVariable("grad");
		String state = (String) execution.getVariable("drzava");

		if (name == null || lastName == null || email == null || city == null || state == null) {
			execution.setVariable("isValidCoAuthor", "false");
		}
		else if(!validate(email)) {
			execution.setVariable("isValidCoAuthor", "false");
		}
		else {
			execution.setVariable("isValidCoAuthor", "true");
		}

	}

}