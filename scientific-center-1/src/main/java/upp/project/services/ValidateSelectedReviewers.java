package upp.project.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import upp.project.model.FormSubmissionDto;

@Service
public class ValidateSelectedReviewers implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("dto");
		String selectedReviewers=null;
		for (FormSubmissionDto fsDTO : dto) {
			if (fsDTO.getFieldId().equals("izaborRecenzenata")) {
				selectedReviewers=fsDTO.getFieldValue();
			}
		}
		
		if(selectedReviewers==null || selectedReviewers.equals("")) {
			execution.setVariable("validniRecenzenti", false);
		}
		else {
			String[] parts=selectedReviewers.split(",");
			if(parts.length<2) {
				execution.setVariable("validniRecenzenti", false);
			}
			else {
				execution.setVariable("validniRecenzenti", true);
			}
		}

	}

}