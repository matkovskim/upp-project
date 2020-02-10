package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.RegistredUser;
import upp.project.repository.MagazineRepository;
import upp.project.repository.RegistredUserRepository;

@Service
public class SendMailToAuthorAndMainEditor implements JavaDelegate {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private RegistredUserRepository registredUserRepository;
	
	@Autowired
	private MagazineRepository magazineRepository;

	@Autowired
	private Environment env;

	@Async
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		SimpleMailMessage mail = new SimpleMailMessage();
		String username = (String) execution.getVariable("starter");
		RegistredUser regUser = registredUserRepository.findByUsername(username);

		mail.setTo(regUser.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Naučna centrala 1");
		mail.setText("Poštovani korisniče, Vaš zahetv za obradu rada je pristigao u naš uaučnu centralu, bićete obavešteni o ishodu recenziranja rada.");
		javaMailSender.send(mail);
		
		String selectedMagazineName=(String)execution.getVariable("izborCasopisa");
		Magazine magazine=magazineRepository.findByName(selectedMagazineName);
		RegistredUser mainEditor=magazine.getMainEditor();
		
		execution.setVariable("glavniUrednik", mainEditor.getUsername());
		
		mail.setTo(mainEditor.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Naučna centrala 1");
		mail.setText("Poštovani uredniče, podnet je zahtev za objavljivanje rada u časopisu u kojem ste Vi glavni urenik.");
		javaMailSender.send(mail);
	}

}
