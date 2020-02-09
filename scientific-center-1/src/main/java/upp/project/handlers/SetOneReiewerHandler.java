package upp.project.handlers;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.RegistredUser;
import upp.project.model.Role;
import upp.project.repository.RegistredUserRepository;

@Service
public class SetOneReiewerHandler implements TaskListener {

	@Autowired
	private RegistredUserRepository registredUserRepository;

	public void notify(DelegateTask delegateTask) {
		TaskFormData taskFormFields = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId());
		
		List<RegistredUser> reviewers = registredUserRepository.findByAuthoritiesName(Role.ROLE_REWIEWER);

		for (FormField f : taskFormFields.getFormFields()) {
			if (f.getId().equals("dodatniRecenzent")) {
				HashMap<String, String> mapa = (HashMap<String, String>) f.getType().getInformation("values");
				mapa.clear();
				for (RegistredUser ru : reviewers) {
					mapa.put(ru.getUsername(), ru.getUsername());
				}
			}
		}
	}

}
