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
import upp.project.repository.ArticleRepository;
import upp.project.repository.RegistredUserRepository;

@Service
public class NotifyAuthorTimeOut implements JavaDelegate {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	RegistredUserRepository registredUserRepository;
	
	@Autowired
	private Environment env;
	
	@Autowired
	MagazineService magazineService;
	
	@Async
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		SimpleMailMessage mail = new SimpleMailMessage();
		String username=(String) execution.getVariable("starter");
		RegistredUser regUser=registredUserRepository.findByUsername(username);
		
		mail.setTo(regUser.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Odbijanje rada");
		mail.setText(
				"Poštovani korisniče, Vaš rad je odbijen jer ga niste ispravili u zadatom vremenskom roku.");
		javaMailSender.send(mail);
	}

}
