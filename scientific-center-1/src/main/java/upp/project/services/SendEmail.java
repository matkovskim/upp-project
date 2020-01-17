package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import upp.project.model.RegistredUser;
import upp.project.repository.RegistredUserRepository;

@Service
public class SendEmail implements JavaDelegate {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	RegistredUserRepository registredUserRepository;
	
	@Autowired
	private Environment env;

	@Async
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		SimpleMailMessage mail = new SimpleMailMessage();
		String username=(String) execution.getVariable("KorisnickoIme");
		RegistredUser regUser=registredUserRepository.findByUsername(username);
		
		mail.setTo(regUser.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Potvrda registracije korisnika");
		mail.setText(
				"Poštovani korisniče, \nMolimo vas da potvrdite svoju registraciju kako biste mogli da koristite naše usluge: \n"
						+ "Potvrda registracije se vrši klikom da dati link: "
						+ "http://localhost:8080/registration/confirmation/" +execution.getProcessInstanceId()+"/"+regUser.getRegistrationCode()+ "\nS poštovanjem, \nVasa naucna centrala 1.");
		javaMailSender.send(mail);
	}

}
