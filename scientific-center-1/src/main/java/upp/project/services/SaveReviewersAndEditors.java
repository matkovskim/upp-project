package upp.project.services;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.FormSubmissionDto;
import upp.project.model.Magazine;
import upp.project.model.RegistredUser;
import upp.project.repository.MagazineRepository;
import upp.project.repository.RegistredUserRepository;

@Service
public class SaveReviewersAndEditors implements JavaDelegate {

	@Autowired
	MagazineRepository magazineRepository;
	@Autowired
	RegistredUserRepository registredUserRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("dto");
		List<RegistredUser> reviewers = getReviewers(dto);
		List<RegistredUser> editors = getEditors(dto);

		String magazineName = (String) execution.getVariable("NazivCasopisa");
		Magazine magazine = magazineRepository.findByName(magazineName);
		
		Set<RegistredUser> reviewersSet = new HashSet<RegistredUser>(reviewers);  
		Set<RegistredUser> editorsSet = new HashSet<RegistredUser>(editors);  

		magazine.setReviewers(reviewersSet);
		magazine.setEditors(editorsSet);

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
						}
					}
				} else {
					return null;
				}
			}
		}
		return reviewers;
	}

	private List<RegistredUser> getEditors(List<FormSubmissionDto> dto) {

		List<RegistredUser> editors = new ArrayList<RegistredUser>();

		for (FormSubmissionDto fsDTO : dto) {
			if (fsDTO.getFieldId().equals("Urednici")) {
				if (fsDTO.getFieldValue() != null && fsDTO.getFieldValue() != "") {
					String[] parts = fsDTO.getFieldValue().split(",");
					for (String part : parts) {
						RegistredUser user = registredUserRepository.findByUsername(part);
						if (user != null) {
							editors.add(user);
						}
					}
				}
			}
		}
		return editors;
	}
	
}