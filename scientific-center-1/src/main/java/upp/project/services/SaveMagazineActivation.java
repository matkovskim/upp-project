package upp.project.services;

import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.Publication;
import upp.project.repository.MagazineRepository;
import upp.project.repository.PublicationRepository;

@Service
public class SaveMagazineActivation implements JavaDelegate {

	@Autowired
	MagazineRepository magazineRepository;
	
	@Autowired
	PublicationRepository publicationRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String magazineName = (String) execution.getVariable("NazivCasopisa");
		Magazine magazine = magazineRepository.findByName(magazineName);

		boolean actevated = (boolean) execution.getVariable("Aktiviraj");

		magazine.setActivated(actevated);
		magazineRepository.save(magazine);
		
		Publication publication=new Publication();
		publication.setMagazine(magazine);
		publication.setPublished(false);
		Date publishedDate = DateUtils.addMonths(new Date(), 1);
		publication.setPublishingDate(publishedDate);
		publicationRepository.save(publication);
		
	}

}