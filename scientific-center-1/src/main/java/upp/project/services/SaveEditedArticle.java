package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Article;
import upp.project.repository.ArticleRepository;

@Service
public class SaveEditedArticle implements JavaDelegate {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String title=(String)execution.getVariable("naslov");
		String keyWords=(String)execution.getVariable("klucniPojmovi");
		String apstrakt=(String)execution.getVariable("apstrakt");
		String text=(String)execution.getVariable("tekstRada");
		
		Long articleId=(Long)execution.getVariable("articleId");
		Article article=articleRepository.findById(articleId).get();
		
		article.setText(text);
		article.setKeyWords(keyWords);
		article.setPaperApstract(apstrakt);
		article.setTitle(title);
		
		articleRepository.save(article);
		
	}

}