package upp.project.services;

import java.util.HashSet;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Article;
import upp.project.model.CoAuthor;
import upp.project.repository.ArticleRepository;
import upp.project.repository.CoAuthorRepository;
import upp.project.repository.RegistredUserRepository;

@Service
public class SaveCoAuthorForArticleHandler  implements JavaDelegate {

	@Autowired
	ArticleRepository articleRepository;
	
	@Autowired
	RegistredUserRepository registredUserRepository;
	
	@Autowired
	CoAuthorRepository coAuthorRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String name=(String)execution.getVariable("ime");
		String lastName=(String)execution.getVariable("prezime");
		String email=(String)execution.getVariable("email");
		String city=(String)execution.getVariable("grad");
		String state=(String)execution.getVariable("drzava");
		CoAuthor coautor=new CoAuthor();
		coautor.setCity(city);
		coautor.setEmail(email);
		coautor.setLastName(lastName);
		coautor.setName(name);
		coautor.setState(state);
		coAuthorRepository.save(coautor);
		
		Long articleId=(Long)execution.getVariable("articleId");
		Article article=articleRepository.findById(articleId).get();
		
		Set<CoAuthor>coauthors=article.getCoauthors();
		if(coauthors==null) {
			coauthors=new HashSet<CoAuthor>();
		}
		coauthors.add(coautor);
		article.setCoauthors(coauthors);
		articleRepository.save(article);
		
	}

}