package upp.project.services;

import java.util.ArrayList;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.FormSubmissionDto;
import upp.project.model.RegistredUser;
import upp.project.repository.RegistredUserRepository;

@Service
public class ValidateReviewersAndEditors implements JavaDelegate {

	@Autowired
	RegistredUserRepository registredUserRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("dto");
		List<RegistredUser> reviewers = getReviewers(dto);
		if (reviewers != null) {
			execution.setVariable("validniUredniciRecenzenti", true);
		} else {
			execution.setVariable("validniUredniciRecenzenti", false);
		}

	}

	private List<RegistredUser> getReviewers(List<FormSubmissionDto> dto) {

		List<RegistredUser> reviewers = new ArrayList<RegistredUser>();

		for (FormSubmissionDto fsDTO : dto) {

			if (fsDTO.getFieldId().equals("Recenzenti")) {
				if (fsDTO.getFieldValue() != null && fsDTO.getFieldValue() != "") {
					String[] parts = fsDTO.getFieldValue().split(",");
					if (parts.length < 2) {
						return null;
					} else {
						for (String part : parts) {
							RegistredUser user = registredUserRepository.findByUsername(part);
							if (user != null) {
								reviewers.add(user);
							}
							else {
								return null;
							}
						}
					}
				} else {
					return null;
				}
			}
		}
		return reviewers;
	}

}