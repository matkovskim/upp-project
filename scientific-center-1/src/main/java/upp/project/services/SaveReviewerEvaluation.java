package upp.project.services;

import java.util.HashSet;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Article;
import upp.project.model.Comment;
import upp.project.repository.ArticleRepository;
import upp.project.repository.CommentRepository;

@Service
public class SaveReviewerEvaluation implements JavaDelegate {

	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String commentForEditorString = (String) execution.getVariable("komentarZaUrednike");
		String commentAboutArticleString = (String) execution.getVariable("komentarORadu");
		String recomendationForPublishingString = (String) execution.getVariable("preporukaZaObjavljivanje");

		Long articleId = (Long) execution.getVariable("articleId");

		Comment commentForEditor=new Comment(commentForEditorString);
		Comment commentAboutArticle=new Comment(commentAboutArticleString);
		
		if(recomendationForPublishingString.equals("prihvatitiUzManjeIspravke")) {
			recomendationForPublishingString="prihvatiti uz manje ispravke";
		}
		if(recomendationForPublishingString.equals("uslovnoPrihvatiUzVeceIspravke")) {
			recomendationForPublishingString="uslovno prihvati uz vece ispravke";
		}
		Comment recomendationForPublishing=new Comment(recomendationForPublishingString);
		commentRepository.save(commentForEditor);
		commentRepository.save(commentAboutArticle);
		commentRepository.save(recomendationForPublishing);
		Article article=articleRepository.findById(articleId).get();
		
		Set<Comment>newCommentsForEditors=new HashSet<Comment>();
		newCommentsForEditors.add(commentForEditor);
		article.setComentsForEditors(newCommentsForEditors);
		
		Set<Comment>newCommentsAboutArticle=new HashSet<Comment>();
		newCommentsAboutArticle.add(commentAboutArticle);
		article.setCoomentsAboutArticle(newCommentsAboutArticle);
		
		Set<Comment>newPublishingRecomendation=new HashSet<Comment>();
		newPublishingRecomendation.add(recomendationForPublishing);
		article.setRecomendationsList(newPublishingRecomendation);
		
		articleRepository.save(article);

	}

}