package upp.project.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
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
import upp.project.services.AuthorityService;

@Service
public class ChooseReviewersHandler implements TaskListener {

	@Autowired
	private RegistredUserRepository registredUserRepository;

	@Autowired
	private ScientificAreaRepository scientificAreaRepository;

	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private MagazineRepository magazineRepository;

	public void notify(DelegateTask delegateTask) {
		TaskFormData taskFormFields = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId());

		String scientificAreaName = (String) delegateTask.getExecution().getVariable("NaucneOblasti");
		ScientificArea scientificArea = scientificAreaRepository.findByName(scientificAreaName);
		Authority reviewerAuthority = this.authorityService.findByName(Role.ROLE_REWIEWER);
		List<RegistredUser> reviewers = registredUserRepository.userWithSpecificAuthorityForArea(scientificArea, reviewerAuthority);

		String magazineName=(String)delegateTask.getExecution().getVariable("izborCasopisa");
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
		
		for (FormField f : taskFormFields.getFormFields()) {
			if (f.getId().equals("izaborRecenzenata") || f.getId().equals("izabraniNoviRecenzent")) {
				HashMap<String, String> mapa = (HashMap<String, String>) f.getType().getInformation("values");
				mapa.clear();
				for (RegistredUser ru : finalList) {
					mapa.put(ru.getUsername(), ru.getUsername());
				}
			}
		}
	}

}
