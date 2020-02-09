package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import upp.project.model.Article;
import upp.project.model.RegistredUser;
import upp.project.repository.ArticleRepository;
import upp.project.repository.RegistredUserRepository;

@Service
public class NotifyAuthorAboutAcceptance implements JavaDelegate {

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private RegistredUserRepository registredUserRepository;
	
	@Autowired
	private Environment env;
	
	@Autowired
	private ArticleRepository articleRepository;

	@Async
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		Long articleId=(Long)execution.getVariable("articleId");
		Article article=articleRepository.findById(articleId).get();
		article.setActivated(true);
		articleRepository.save(article);
		
		SimpleMailMessage mail = new SimpleMailMessage();
		String username=(String) execution.getVariable("starter");
		RegistredUser regUser=registredUserRepository.findByUsername(username);
		
		mail.setTo(regUser.getEmail());
		mail.setFrom(env.getProperty("spring.mail.username"));
		mail.setSubject("Odbijanje rada");
		mail.setText(
				"Poštovani korisniče, Vaš rad u časopisu je prihvaćen.");
		javaMailSender.send(mail);
	}

}
