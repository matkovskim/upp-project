package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Article;
import upp.project.repository.ArticleRepository;

@Service
public class SaveTextChanges implements JavaDelegate {

	@Autowired
	private ArticleRepository articleRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String text = (String) execution.getVariable("tekstRada");

		Long articleId = (Long) execution.getVariable("articleId");
		Article article = articleRepository.findById(articleId).get();

		article.setText(text);

		articleRepository.save(article);

	}

}