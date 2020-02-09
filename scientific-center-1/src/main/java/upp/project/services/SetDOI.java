package upp.project.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Article;
import upp.project.repository.ArticleRepository;

@Service
public class SetDOI implements JavaDelegate {

	@Autowired
	private ArticleRepository articleRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		Long articleId = (Long) execution.getVariable("articleId");
		Article article = articleRepository.findById(articleId).get();

		String DOI = "http://doi.org/10.";
		String generatedStringFirst = RandomStringUtils.randomNumeric(4);
		String generatedStringSecond = RandomStringUtils.randomNumeric(4);
		DOI = DOI + generatedStringFirst + "/" + generatedStringSecond;
		article.setDOI(DOI);

		articleRepository.save(article);

	}

}
