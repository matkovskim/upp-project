package upp.project.controller;

import java.util.ArrayList;
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
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
import upp.project.services.AuthentificationService;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/welcome", produces = MediaType.APPLICATION_JSON_VALUE)
public class StartController {
	
	@Autowired
	IdentityService identityService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

	@Autowired
	JwtProvider tokenProvider;

	@Autowired
	AuthentificationService authentificationService;

	/**
	 * Pokretanje procesa registracije, vraca prvu formu
	 */
	@GetMapping(path = "/startRegistration", produces = "application/json")
	public @ResponseBody FormFieldsDto startRegistration(HttpServletRequest request) {
		String token = tokenProvider.getToken(request);
		if(token!=null) {
			return null;
		}
		identityService.setAuthenticatedUserId("gost");
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("Registracija");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		task.setAssignee("gost");
		taskService.saveTask(task);
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDto(task.getId(), pi.getId(), properties);
	}

	/**
	 * Pokretanje procesa kreiranja magazina, vraca prvu formu
	 */
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	@GetMapping(path = "/startCreatingMagazine", produces = "application/json")
	public @ResponseBody FormFieldsDto get(HttpServletRequest request) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("KreiranjeCasopisa");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		String token = tokenProvider.getToken(request);
		String username = tokenProvider.getUsernameFromToken(token);
		task.setAssignee(username);
		taskService.saveTask(task);
		runtimeService.setVariable(pi.getId(), "starterUser", username);
		return new FormFieldsDto(task.getId(), pi.getId(), properties);
	}
	
	/**
	 * Pokretanje procesa obrade podnetog teksta, vraca prvu formu
	 */
	@PreAuthorize("hasRole('ROLE_REG_USER')")
	@GetMapping(path = "/startProcessingText", produces = "application/json")
	public @ResponseBody FormFieldsDto startProcessing(HttpServletRequest request) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("ObradaTeksta");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).singleResult();
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();

		String token = tokenProvider.getToken(request);
		String username = tokenProvider.getUsernameFromToken(token);
		task.setAssignee(username);
		taskService.saveTask(task);
		runtimeService.setVariable(pi.getId(), "starter", username);
		return new FormFieldsDto(task.getId(), pi.getId(), properties);
	}

	/**
	 * Primanje sadrzaja forme i kacenje dto objekta na runtime service
	 */
	@PostMapping(path = "/post/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {

		HashMap<String, Object> map = authentificationService.mapListToDto(dto);
		for (Map.Entry mapElement : map.entrySet()) {
			String key = (String) mapElement.getKey();
			if (key.equals("NaucneOblasti") || key.equals("Recenzenti") || key.equals("Urednici") || key.equals("izaborRecenzenata")) {
				mapElement.setValue(null);
			}
		}

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();

		runtimeService.setVariable(processInstanceId, "dto", dto);

		try {
			formService.submitTaskForm(taskId, map);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uneti podaci nisu validni!");
		}

		return new ResponseEntity<>(HttpStatus.OK);

	}

	/**
	 * Vraca sve moje taskove zajedno sa poljima forme za trenutno
	 * ulogovanog korisnika
	 */
	@PreAuthorize("hasRole('ROLE_EDITOR') or hasRole('ROLE_REG_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_REWIEWER')")
	@GetMapping(path = "/getAllMyTasks", produces = "application/json")
	public @ResponseBody ResponseEntity<List<TaskDto>> allMyTasks(HttpServletRequest request) {

		String token = tokenProvider.getToken(request);
		String username = tokenProvider.getUsernameFromToken(token);
		
		List<Task> myTasks = taskService.createTaskQuery().taskAssignee(username).list();
		List<Group>userGroups=identityService.createGroupQuery().groupMember(username).list();
		for(Group g: userGroups) {
			List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(g.getId()).list();
			for(Task t:tasks) {
				myTasks.add(t);
			}
		}

		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : myTasks) {
			TaskFormData tfd = formService.getTaskFormData(task.getId());
			List<FormField> properties = tfd.getFormFields();
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee(), properties);
			dtos.add(t);
		}

		return new ResponseEntity(dtos, HttpStatus.OK);
	}
	
	/**
	 * Vraca jedan moj sledeci task zajedno sa poljima forme
	 */
	@GetMapping(path = "/getTasks/{processInstanceId}", produces = "application/json")
	public @ResponseBody FormFieldsDto get(@PathVariable String processInstanceId, HttpServletRequest request) {

		String token = tokenProvider.getToken(request);
		String username = tokenProvider.getUsernameFromToken(token);

		Task task = taskService.createTaskQuery().processInstanceId(processInstanceId).singleResult();

		if (task != null && (task.getAssignee() == null || task.getAssignee().equals(username))) {
			TaskFormData tfd = formService.getTaskFormData(task.getId());
			List<FormField> properties = tfd.getFormFields();
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee(), properties);
			return new FormFieldsDto(task.getId(), task.getProcessInstanceId(), properties);
		}

		return null;

	}

	/**
	 * Vraca informacije o tasku sa zadatim id
	 */
	@PreAuthorize("hasRole('ROLE_EDITOR') or hasRole('ROLE_REG_USER') or hasRole('ROLE_ADMIN') or hasRole('ROLE_REWIEWER')")
	@GetMapping(path = "/getTask/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity<TaskDto> getTask(@PathVariable String taskId) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData tfd = formService.getTaskFormData(taskId);
		
		List<FormField> properties = tfd.getFormFields();
		TaskDto t = new TaskDto(taskId, task.getName(), task.getAssignee(), properties);

		return new ResponseEntity(t, HttpStatus.OK);
	}

}
