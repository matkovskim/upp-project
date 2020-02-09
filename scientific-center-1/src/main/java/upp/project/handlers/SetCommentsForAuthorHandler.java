package upp.project.handlers;

import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Article;
import upp.project.model.Comment;
import upp.project.repository.ArticleRepository;

@Service
public class SetCommentsForAuthorHandler implements TaskListener{
	
	@Autowired
	private ArticleRepository articleRepository;

	public void notify(DelegateTask delegateTask) {

		 Long articleId = (Long) delegateTask.getExecution().getVariable("articleId");
		 Article article = articleRepository.findById(articleId).get();
		 String comments="";
		 Set<Comment>commentsForAuthorSet=article.getCoomentsAboutArticle();
		 List<Comment>commentsForAuthorList = Lists.newArrayList(commentsForAuthorSet);
		 for(int i=0;i<commentsForAuthorList.size();i++) {
			 if(i!=0) {
				 comments+=", ";
			 }
			 comments+=commentsForAuthorList.get(i).getText();
		 }

		 delegateTask.getExecution().setVariable("postavljeniKomentariAutoru", comments);
		 
	}

}
