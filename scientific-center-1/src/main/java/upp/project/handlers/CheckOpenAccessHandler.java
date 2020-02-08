package upp.project.handlers;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.repository.MagazineRepository;

@Service
public class CheckOpenAccessHandler implements TaskListener {

	@Autowired
	private MagazineRepository magazineRepository;
	
	public void notify(DelegateTask delegateTask) {

		String magazineName=(String) delegateTask.getExecution().getVariable("izborCasopisa");
		Magazine magazine = magazineRepository.findByName(magazineName);

		String whoPays=magazine.getWhoPays();
		boolean openAccess=true;
		System.out.println(whoPays);
		if(whoPays.equals("Citaoci")) {
			openAccess=false;
		}

		delegateTask.getExecution().setVariable("openAccess", openAccess);

	}

}