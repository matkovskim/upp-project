package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.repository.MagazineRepository;

@Service
public class ValidateSelectedMagazine implements JavaDelegate {

	@Autowired
	MagazineRepository magazineRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String selectedMagazineName=(String)execution.getVariable("izborCasopisa");
		if(selectedMagazineName==null) {
			execution.setVariable("isValid", false);
		}
		
		Magazine magazine=magazineRepository.findByName(selectedMagazineName);
		if(magazine==null) {
			execution.setVariable("isValid", false);
		}
		else {
			execution.setVariable("isValid", true);
		}

	}

}