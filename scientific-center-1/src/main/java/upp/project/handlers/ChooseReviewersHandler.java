package upp.project.handlers;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Authority;
import upp.project.model.RegistredUser;
import upp.project.model.Role;
import upp.project.model.ScientificArea;
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

	public void notify(DelegateTask delegateTask) {
		TaskFormData taskFormFields = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId());

		String scientificAreaName = (String) delegateTask.getExecution().getVariable("NaucneOblasti");
		ScientificArea scientificArea = scientificAreaRepository.findByName(scientificAreaName);
		Authority reviewerAuthority = this.authorityService.findByName(Role.ROLE_REWIEWER);
		List<RegistredUser> reviewers = registredUserRepository.userWithSpecificAuthorityForArea(scientificArea,
				reviewerAuthority);

		for (FormField f : taskFormFields.getFormFields()) {
			if (f.getId().equals("izaborRecenzenata") || f.getId().equals("izabraniNoviRecenzent")) {
				HashMap<String, String> mapa = (HashMap<String, String>) f.getType().getInformation("values");
				mapa.clear();
				for (RegistredUser ru : reviewers) {
					mapa.put(ru.getUsername(), ru.getUsername());
				}
			}
		}
	}

}
