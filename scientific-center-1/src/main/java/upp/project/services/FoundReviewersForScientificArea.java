package upp.project.services;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.glassfish.jersey.internal.guava.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Authority;
import upp.project.model.Magazine;
import upp.project.model.RegistredUser;
import upp.project.model.Role;
import upp.project.model.ScientificArea;
import upp.project.repository.MagazineRepository;
import upp.project.repository.RegistredUserRepository;
import upp.project.repository.ScientificAreaRepository;

@Service
public class FoundReviewersForScientificArea implements JavaDelegate {

	@Autowired
	private RegistredUserRepository registredUserRepository;
	
	@Autowired
	private ScientificAreaRepository scientificAreaRepository;

	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private MagazineRepository magazineRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String scientificAreaName = (String) execution.getVariable("NaucneOblasti");
		ScientificArea scientificArea=scientificAreaRepository.findByName(scientificAreaName);
		Authority reviewerAuthority = this.authorityService.findByName(Role.ROLE_REWIEWER);
		List<RegistredUser>reviewers=registredUserRepository.userWithSpecificAuthorityForArea(scientificArea, reviewerAuthority);

		String magazineName=(String)execution.getVariable("izborCasopisa");
		System.out.println("casopis: "+magazineName);
		Magazine magazine=magazineRepository.findByName(magazineName);
		List<RegistredUser>magazineReviewers=Lists.newArrayList(magazine.getReviewers());

		List<RegistredUser>finalList=new ArrayList<RegistredUser>();
		for(RegistredUser ru:magazineReviewers) {
			for(RegistredUser r:reviewers) {
				if(ru.getUsername().equals(r.getUsername())) {
					finalList.add(ru);
				}
			}
		}
		
		System.out.println("recenzenata ima: "+finalList.size());
		if(finalList.size()<2) {
			execution.setVariable("postojeRecenzenti", false);
		}
		else{
			execution.setVariable("postojeRecenzenti", true);
		}
		
	}

}