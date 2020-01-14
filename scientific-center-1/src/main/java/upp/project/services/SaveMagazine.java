package upp.project.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.FormSubmissionDto;
import upp.project.model.Magazine;
import upp.project.repository.MagazineRepository;
import upp.project.repository.ScientificAreaRepository;

@Service
public class SaveMagazine implements JavaDelegate {

	@Autowired
	MagazineRepository magazineRepository;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	@Autowired
	MagazineService magazineService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmissionDto> dto = (List<FormSubmissionDto>) execution.getVariable("magazine");
		Magazine magazine = magazineService.makeMagazine(dto);
		execution.setVariable("magazineName", magazine.getName());
		System.out.println("SACUVAO PROMENLJIVU "+ magazine.getName());
		magazineRepository.save(magazine);
//PSTAVI GLAVNOG UREDNIKA
	}

}
