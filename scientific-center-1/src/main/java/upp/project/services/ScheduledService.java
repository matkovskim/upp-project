package upp.project.services;

import java.util.Date;
import java.util.List;

import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import upp.project.model.Publication;
import upp.project.repository.PublicationRepository;

@Service
public class ScheduledService {
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private HistoryService historyService;
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	/**
	 * Unistavanje zapocetih procesa starijih od jednog dana
	 */
	@Scheduled(initialDelay = 1800000, fixedRate = 1800000)
	//@Scheduled(initialDelay = 30000, fixedRate = 30000)
	public void checkOldProcessInstances() {
		//uzimam sve aktivne procese
		List<HistoricProcessInstance>hpi=historyService.createHistoricProcessInstanceQuery().active().unfinished().list();
		Date yesterday=new Date(System.currentTimeMillis()-24*60*60*1000);
		//Date yesterday=new Date(System.currentTimeMillis()-60*1000);
		System.out.println(yesterday);
		System.out.println(new Date());
		for(HistoricProcessInstance h:hpi) {
			Date startDate=h.getStartTime();
			if(startDate.before(yesterday)) {
				runtimeService.suspendProcessInstanceById(h.getId());
			}
		}
	}

	/**
	 * Kreiranje novih izdanja i zatvaranje starih, poziva se jednom dnevno
	 */
	@Scheduled(initialDelay = 86000000, fixedRate = 86000000)
	public void checkPublications() {
		//uzimam sve aktivne procese
		List<Publication>publications=publicationRepository.findByPublished(false);
		for(Publication p:publications) {
			if(p.getPublishingDate().before(new Date())) {
				p.setPublished(true);
				publicationRepository.save(p);
				Publication newPublication= new Publication();
				newPublication.setMagazine(p.getMagazine());
				newPublication.setPublished(false);
				newPublication.setPublishingDate(new Date(System.currentTimeMillis()+7*24*60*60*1000));
				publicationRepository.save(newPublication);
			}
		}

	}

}
