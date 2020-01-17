package upp.project.handlers;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.ScientificArea;
import upp.project.repository.ScientificAreaRepository;

@Service
public class SomeHandler implements TaskListener {

	@Autowired
	IdentityService identityService;
	
	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	public void notify(DelegateTask delegateTask) {
		 TaskFormData taskFormFields=delegateTask.getExecution().getProcessEngineServices().getFormService().getTaskFormData(delegateTask.getId());
		 List<ScientificArea>areas=scientificAreaRepository.findAll();
		 for(FormField f : taskFormFields.getFormFields()){
		       if( f.getId().equals("NaucneOblasti")){
		           for(ScientificArea scArea:areas){
		        	   HashMap<String, String>mapa=(HashMap<String, String>)f.getType().getInformation("values");
		        	   mapa.put(scArea.getName(), scArea.getName());
		           }
		       }
		   }
	  }
}