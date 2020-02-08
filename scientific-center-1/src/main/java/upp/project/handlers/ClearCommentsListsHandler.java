package upp.project.handlers;

import java.util.HashSet;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Article;
import upp.project.model.Comment;
import upp.project.repository.ArticleRepository;

@Service
public class ClearCommentsListsHandler implements TaskListener {

	@Autowired
	private ArticleRepository articleRepository;

	public void notify(DelegateTask delegateTask) {

		Long articleId = (Long) delegateTask.getExecution().getVariable("articleId");
		Article article = articleRepository.findById(articleId).get();

		article.setComentsForEditors(new HashSet<Comment>());
		article.setCoomentsAboutArticle(new HashSet<Comment>());

		articleRepository.save(article);

	}

}
