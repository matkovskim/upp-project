package upp.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import upp.project.model.FormSubmissionDto;
import upp.project.services.MagazineService;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/admin", produces = MediaType.APPLICATION_JSON_VALUE)
public class AdminController {

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

	@Autowired
	MagazineService magazineService;
	
	@Autowired
	private RuntimeService runtimeService;

	/**
	 * Potvrda recenzenta, potvrda casopisa DUPLIRANO IZMENITI
	 */
	@PostMapping(path = "/accept/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity rewieverAcceptance(@RequestBody List<FormSubmissionDto> dto,
			@PathVariable String taskId) {

		HashMap<String, Object> map = mapListToDto(dto);
		for (Map.Entry mapElement : map.entrySet()) {
			String key = (String) mapElement.getKey();
			if (key.equals("NaucneOblasti") || key.equals("Recenzenti") || key.equals("Urednici")) {
					mapElement.setValue(null);
				
			}
		}

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();

		runtimeService.setVariable(processInstanceId, "magazine", dto);
		
		try {
			formService.submitTaskForm(taskId, map);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uneti podaci nisu validni!");
		}

		return new ResponseEntity<>(HttpStatus.OK);
	}

	public HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmissionDto temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		return map;
	}
}
