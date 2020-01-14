package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.repository.MagazineRepository;

@Service
public class SaveMagazineChange implements JavaDelegate {

	@Autowired
	MagazineRepository magazineRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String name = (String) execution.getVariable("NazivCasopisa");
		String ISBN = (String) execution.getVariable("ISBN");
		String paymentMethod = (String) execution.getVariable("NacinNaplate");

		String oldName=(String)execution.getVariable("magazineName");
		
		Magazine magazine=magazineRepository.findByName(oldName);
		if(magazine!=null) {
			magazine.setName(name);
			magazine.setISBN(ISBN);
			magazine.setWhoPays(paymentMethod);
			magazineRepository.save(magazine);
		}
	}

}