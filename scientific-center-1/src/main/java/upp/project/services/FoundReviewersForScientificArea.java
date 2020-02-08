package upp.project.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Authority;
import upp.project.model.RegistredUser;
import upp.project.model.Role;
import upp.project.model.ScientificArea;
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
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String scientificAreaName = (String) execution.getVariable("NaucneOblasti");
		ScientificArea scientificArea=scientificAreaRepository.findByName(scientificAreaName);
		
		Authority reviewerAuthority = this.authorityService.findByName(Role.ROLE_REWIEWER);
		
		List<RegistredUser>reviewers=registredUserRepository.userWithSpecificAuthorityForArea(scientificArea, reviewerAuthority);
		
		System.out.println("recenzenata ima: "+reviewers.size());
		//TODO: proveriti: urednik bira najmanje dva recenzenta, problem sta ako je jedan, radim kao da nema ni njega...
		if(reviewers.size()<2) {
			execution.setVariable("postojeRecenzenti", false);
		}
		else{
			execution.setVariable("postojeRecenzenti", true);
		}
		
	}

}