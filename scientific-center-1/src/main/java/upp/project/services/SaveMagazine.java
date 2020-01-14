package upp.project.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.FormSubmissionDto;
import upp.project.model.Magazine;
import upp.project.model.RegistredUser;
import upp.project.repository.MagazineRepository;
import upp.project.repository.RegistredUserRepository;
import upp.project.repository.ScientificAreaRepository;

@Service
public class SaveMagazine implements JavaDelegate {

	@Autowired
	MagazineRepository magazineRepository;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	@Autowired
	MagazineService magazineService;

	@Autowired
	RegistredUserRepository registredUserRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("magazine");
		Magazine magazine = magazineService.makeMagazine(dto);
		execution.setVariable("magazineName", magazine.getName());

		String mainEditorUsername = (String) execution.getVariable("starterUser");
		RegistredUser mainEditor = registredUserRepository.findByUsername(mainEditorUsername);

		magazine.setMainReviewer(mainEditor);

		magazineRepository.save(magazine);
	}

}
