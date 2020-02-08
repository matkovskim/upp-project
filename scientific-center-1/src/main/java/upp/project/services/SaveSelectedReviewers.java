package upp.project.services;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.FormSubmissionDto;
import upp.project.repository.RegistredUserRepository;

@Service
public class SaveSelectedReviewers implements JavaDelegate {

	@Autowired
	RegistredUserRepository registredUserRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("dto");
		String selectedReviewers = null;
		for (FormSubmissionDto fsDTO : dto) {
			if (fsDTO.getFieldId().equals("izaborRecenzenata")) {
				selectedReviewers = fsDTO.getFieldValue();
			}
		}

		List<String>reviewers=new ArrayList<String>();
		String[] parts = selectedReviewers.split(",");
		for(String part:parts) {
			reviewers.add(part);
		}

		execution.setVariable("recenzentiIds", reviewers);
	}

}