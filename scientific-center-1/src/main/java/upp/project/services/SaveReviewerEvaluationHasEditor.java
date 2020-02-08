package upp.project.services;

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
public class SaveReviewerEvaluationHasEditor implements JavaDelegate {

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

		System.out.println("recomendationForPublishingString: " + recomendationForPublishingString);

		Comment commentForEditor=new Comment(commentForEditorString);
		Comment commentAboutArticle=new Comment(commentAboutArticleString);
		Comment recomendationForPublishing=new Comment(recomendationForPublishingString);
		commentRepository.save(commentForEditor);
		commentRepository.save(commentAboutArticle);
		commentRepository.save(recomendationForPublishing);
		
		Article article=articleRepository.findById(articleId).get();
		
		Set<Comment>commentsForEditors=article.getComentsForEditors();
		commentsForEditors.add(commentForEditor);
		article.setComentsForEditors(commentsForEditors);
		
		Set<Comment>commentsAboutArticle=article.getCoomentsAboutArticle();
		commentsAboutArticle.add(commentAboutArticle);
		article.setCoomentsAboutArticle(commentsAboutArticle);
		
		Set<Comment>recomendationForPublishingArticle=article.getRecomendationsList();
		recomendationForPublishingArticle.add(recomendationForPublishing);
		article.setRecomendationsList(recomendationForPublishingArticle);
		
		articleRepository.save(article);

		//ocisti polja za unos komentara za sledece recenzente
		execution.setVariable("komentarORadu", "");
		execution.setVariable("komentarZaUrednike", "");

	}

}