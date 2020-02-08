package upp.project.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Article;
import upp.project.model.FormSubmissionDto;
import upp.project.model.Magazine;
import upp.project.model.Publication;
import upp.project.model.RegistredUser;
import upp.project.model.ScientificArea;
import upp.project.repository.ArticleRepository;
import upp.project.repository.MagazineRepository;
import upp.project.repository.PublicationRepository;
import upp.project.repository.RegistredUserRepository;
import upp.project.repository.ScientificAreaRepository;

@Service
public class SaveArticleInformations implements JavaDelegate {

	@Autowired
	MagazineRepository magazineRepository;
	
	@Autowired
	RegistredUserRepository registredUserRepository;
	
	@Autowired
	ArticleRepository articleRepository;
	
	@Autowired
	ScientificAreaRepository scientificAreaRepository;
	
	@Autowired
	PublicationRepository publicationRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("dto");
		String scientificAreaName="";
		for (FormSubmissionDto fsDTO : dto) {
			if (fsDTO.getFieldId().equals("NaucneOblasti")) {
				execution.setVariable("NaucneOblasti", fsDTO.getFieldValue());
				scientificAreaName=fsDTO.getFieldValue();
				System.out.println(fsDTO.getFieldValue());
			}
		}
		
		String magazineName=(String) execution.getVariable("izborCasopisa");
		Magazine magazine = magazineRepository.findByName(magazineName);

		String author=(String) execution.getVariable("starter");
		RegistredUser regUser=registredUserRepository.findByUsername(author);
		
		String title=(String)execution.getVariable("naslov");
		String keyWords=(String)execution.getVariable("klucniPojmovi");
		String apstrakt=(String)execution.getVariable("apstrakt");
		String text=(String)execution.getVariable("tekstRada");
		
		ScientificArea scientificArea=scientificAreaRepository.findByName(scientificAreaName);	
		
		System.out.println("Naucna oblast je: "+scientificAreaName);
		
		Publication publication=publicationRepository.findByMagazineAndPublished(magazine, false);

		Article article=new Article();
		article.setAuthor(regUser);
		article.setPublication(publication);		
		article.setText(text);
		article.setKeyWords(keyWords);
		article.setPaperApstract(apstrakt);
		article.setScientificArea(scientificArea);
		article.setTitle(title);
		
		Article savedArticle=articleRepository.save(article);
		execution.setVariable("articleId", savedArticle.getId());
	}

}