package upp.project.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.RegistredUser;
import upp.project.model.Role;
import upp.project.model.ScientificArea;
import upp.project.repository.MagazineRepository;
import upp.project.repository.RegistredUserRepository;

@Service
public class EditorsAndReviewersHandler implements TaskListener {

	@Autowired
	IdentityService identityService;

	@Autowired
	RegistredUserRepository registredUserRepository;

	@Autowired
	MagazineRepository magazineRepository;

	public void notify(DelegateTask delegateTask) {
		TaskFormData taskFormFields = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId());

		String name = (String) delegateTask.getExecution().getVariable("NazivCasopisa");
		Magazine magazine = magazineRepository.findByName(name);
		Set<ScientificArea> areas = magazine.getScientificArea();

		List<RegistredUser> reviewers = registredUserRepository.findByAuthoritiesName(Role.ROLE_REWIEWER);
		List<RegistredUser> editors = registredUserRepository.findByAuthoritiesName(Role.ROLE_EDITOR);

		List<RegistredUser> retReviewers = new ArrayList<RegistredUser>();
		List<RegistredUser> retEditors = new ArrayList<RegistredUser>();

		for (ScientificArea sa : areas) {
			for (RegistredUser ru : reviewers) {
				if (ru.getScientificArea().contains(sa)) {
					retReviewers.add(ru);
				}
			}
			for (RegistredUser ru : editors) {
				if (ru.getScientificArea().contains(sa)) {
					retEditors.add(ru);
				}
			}
		}

		for (FormField f : taskFormFields.getFormFields()) {
			if (f.getId().equals("Recenzenti")) {
				for (RegistredUser regUser : retReviewers) {
					HashMap<String, String> mapa = (HashMap<String, String>) f.getType().getInformation("values");
					mapa.put(regUser.getName() +" "+ regUser.getLastName(), regUser.getName() +" "+ regUser.getLastName());
				}
			}
			if (f.getId().equals("Urednici")) {
				for (RegistredUser regUser : retEditors) {
					HashMap<String, String> mapa = (HashMap<String, String>) f.getType().getInformation("values");
					mapa.put(regUser.getName() +" "+ regUser.getLastName(), regUser.getName() +" "+ regUser.getLastName());
				}
			}
		}
		
	}
	
}