package upp.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import upp.project.aasecurity.JwtProvider;
import upp.project.model.FormFieldsDto;
import upp.project.model.FormSubmissionDto;
import upp.project.model.TaskDto;
import upp.project.services.MagazineService;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/magazine", produces = MediaType.APPLICATION_JSON_VALUE)
public class MagazineController {

	@Autowired
	IdentityService identityService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

	@Autowired
	MagazineService magazineService;
	
	@Autowired
	JwtProvider tokenProvider;

	/**
	 * Pokretanje procesa, vraca prvu formu
	 */
	@GetMapping(path = "/getMagazineForm", produces = "application/json")
	public @ResponseBody FormFieldsDto get(HttpServletRequest request) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("KreiranjeCasopisa");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();

		String token = tokenProvider.getToken(request);
		String username = tokenProvider.getUsernameFromToken(token);
	
		runtimeService.setVariable(pi.getId(), "starterUser", username);

		return new FormFieldsDto(task.getId(), pi.getId(), properties);
	}

	/**
	 * Submit casopisa
	 */
	@PostMapping(path = "/post/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
	
		HashMap<String, Object> map = magazineService.mapListToDto(dto);
		for (Map.Entry mapElement : map.entrySet()) {
			String key = (String) mapElement.getKey();
			if (key.equals("NaucneOblasti") || key.equals("Recenzenti") || key.equals("Urednici")) {
				String value = (String) mapElement.getValue();
				String[] parts = value.split(",");
				if (parts.length != 1) {
					mapElement.setValue(parts[0]);
				}
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
	
	/**
	 * Vraca moj sledeci task ili vise njih zajedno sa poljima forme
	 */
	@GetMapping(path = "/getTasks/{processInstanceId}", produces = "application/json")
    public @ResponseBody FormFieldsDto get(@PathVariable String processInstanceId, HttpServletRequest request) {
		
		String token = tokenProvider.getToken(request);
		String username = tokenProvider.getUsernameFromToken(token);
		
		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

		if(task!=null && (task.getAssignee()==null || task.getAssignee().equals(username))) {
			TaskFormData tfd = formService.getTaskFormData(task.getId());
			List<FormField> properties = tfd.getFormFields();
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee(), properties);
			return new FormFieldsDto(task.getId(), task.getProcessInstanceId(), properties);
		}
		
		return null;

    }
	

}