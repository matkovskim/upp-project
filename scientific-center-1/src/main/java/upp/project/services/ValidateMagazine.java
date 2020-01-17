package upp.project.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.FormSubmissionDto;
import upp.project.model.Magazine;

@Service
public class ValidateMagazine implements JavaDelegate {

	@Autowired
	MagazineService magazineService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("dto");
		Magazine magazine = magazineService.makeMagazine(dto);

		if (magazine == null) {
			execution.setVariable("validan", false);
		} else if (magazine.getId() == -1) {
			execution.setVariable("validan", false);
		} else {
			execution.setVariable("validan", true);
		}

	}

}