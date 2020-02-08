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

import upp.project.model.Magazine;
import upp.project.services.MagazineService;

@Service
public class MagazinesHandler implements TaskListener{
	
	@Autowired
	private MagazineService magazineService;

	public void notify(DelegateTask delegateTask) {
		 TaskFormData taskFormFields=delegateTask.getExecution().getProcessEngineServices().getFormService().getTaskFormData(delegateTask.getId());
		 List<Magazine>magazines=magazineService.getAllActivatedMagazines();
		 for(FormField f : taskFormFields.getFormFields()){
		       if( f.getId().equals("izborCasopisa")){
	        	   HashMap<String, String>mapa=(HashMap<String, String>)f.getType().getInformation("values");
	        	   mapa.clear();
		           for(Magazine mag:magazines){
		        	   mapa.put(mag.getName(), mag.getName());
		           }
		       }
		   }
	  }

}
