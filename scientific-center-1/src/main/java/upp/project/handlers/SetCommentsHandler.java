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
public class SetCommentsHandler implements TaskListener {

	@Autowired
	private ArticleRepository articleRepository;

	public void notify(DelegateTask delegateTask) {

		if (delegateTask.getExecution().getVariable("konsultujJos") != null) {
			boolean consult = (boolean) delegateTask.getExecution().getVariable("konsultujJos");
			if (consult == true) {
				String comment = (String) delegateTask.getExecution().getVariable("dodatnikomentarZaUrednike");
				delegateTask.getExecution().setVariable("postavljeniKomentariUredniku", comment);

				String recomendation = (String) delegateTask.getExecution().getVariable("preporukaZaObjavljivanje");
				
				if(recomendation.equals("prihvatitiUzManjeIspravke")) {
					recomendation="prihvatiti uz manje ispravke";
				}
				if(recomendation.equals("uslovnoPrihvatiUzVeceIspravke")) {
					recomendation="uslovno prihvati uz vece ispravke";
				}

				delegateTask.getExecution().setVariable("postavljenePreporukeUredniku", recomendation);
			} else {
				setCommentAndRecomendation(delegateTask);
			}
		} else {
			setCommentAndRecomendation(delegateTask);
		}

	}

	private void setCommentAndRecomendation(DelegateTask delegateTask) {
		Long articleId = (Long) delegateTask.getExecution().getVariable("articleId");
		Article article = articleRepository.findById(articleId).get();
		String comments = "";
		Set<Comment> commentsForEditorsSet = article.getComentsForEditors();
		List<Comment> commentsForEditorsList = Lists.newArrayList(commentsForEditorsSet);
		for (int i = 0; i < commentsForEditorsList.size(); i++) {
			if (i != 0) {
				comments += ", ";
			}
			comments += commentsForEditorsList.get(i).getText();
		}
		delegateTask.getExecution().setVariable("postavljeniKomentariUredniku", comments);

		String recomendations = "";
		Set<Comment> recomendationsForEditorsSet = article.getRecomendationsList();
		List<Comment> recomendationsForEditorsList = Lists.newArrayList(recomendationsForEditorsSet);
		for (int i = 0; i < recomendationsForEditorsList.size(); i++) {
			if (i != 0) {
				recomendations += ", ";
			}
			recomendations += recomendationsForEditorsList.get(i).getText();
		}
		delegateTask.getExecution().setVariable("postavljenePreporukeUredniku", recomendations);

	}

}
