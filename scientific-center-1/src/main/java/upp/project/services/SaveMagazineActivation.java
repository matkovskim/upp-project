package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.repository.MagazineRepository;

@Service
public class SaveMagazineActivation implements JavaDelegate {

	@Autowired
	MagazineRepository magazineRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		System.out.println("SaveMagazineActivation");

		String magazineName = (String) execution.getVariable("NazivCasopisa");
		Magazine magazine = magazineRepository.findByName(magazineName);

		boolean actevated = (boolean) execution.getVariable("Aktiviraj");

		magazine.setActevated(actevated);
		magazineRepository.save(magazine);

	}

}