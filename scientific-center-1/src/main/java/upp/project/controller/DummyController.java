package upp.project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import upp.project.aasecurity.JwtProvider;
import upp.project.model.FormFieldsDto;
import upp.project.model.FormSubmissionDto;
import upp.project.model.RegistredUser;
import upp.project.model.TaskDto;
import upp.project.repository.RegistredUserRepository;
import upp.project.repository.ScientificAreaRepository;
import upp.project.services.AuthorityService;

@Controller
@RequestMapping("/welcome")
public class DummyController {
	@Autowired
	IdentityService identityService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	@Autowired
	RegistredUserRepository registredUserRepository;

	@Autowired
	JwtProvider tokenProvider;

	@Autowired
	AuthorityService authorityService;
	
	/**
	 * Pokretanje procesa, vraca prvu formu
	 */
	@GetMapping(path = "/get", produces = "application/json")
	public @ResponseBody FormFieldsDto get() {

		// pokretanje procesa registracije
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("Registracija");

		// uzmi prvi task
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);

		// uzmi polja forme
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();

		// vrati informacije o tasku, procesu i polja
		return new FormFieldsDto(task.getId(), pi.getId(), properties);
	}

	/**
	 * Vraca sledeci task ili vise njih zajedno sa poljima forme
	 */
	@GetMapping(path = "/get/tasks/{processInstanceId}", produces = "application/json")
	public @ResponseBody ResponseEntity<List<TaskDto>> get(@PathVariable String processInstanceId) {

		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
		List<TaskDto> dtos = new ArrayList<TaskDto>();
		for (Task task : tasks) {
			TaskFormData tfd = formService.getTaskFormData(task.getId());
			List<FormField> properties = tfd.getFormFields();
			TaskDto t = new TaskDto(task.getId(), task.getName(), task.getAssignee(), properties);
			dtos.add(t);
		}

		return new ResponseEntity(dtos, HttpStatus.OK);
	}

	/**
	 * Potvrda na mailu
	 */
	@GetMapping(path = "/confirmation/{processInstanceId}/{confirmationString}", produces = "application/json")
	public @ResponseBody ResponseEntity getNextTask(@PathVariable String processInstanceId, @PathVariable String confirmationString) {

		String newUserUsername = (String) runtimeService.getVariable(processInstanceId, "KorisnickoIme");
		RegistredUser regUser = registredUserRepository.findByUsername(newUserUsername);
		if(regUser.getRegistrationCode().equals(confirmationString)) {
			MessageCorrelationResult results = runtimeService.createMessageCorrelation("PotvrdaMaila")
					.processInstanceId(processInstanceId).correlateWithResult();
			if (regUser.isReviewer()) {
				return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
						.header("Location", "http://localhost:4200/waitAdminConfirmation").build();
			} else {
				return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
						.header("Location", "http://localhost:4200/success").build();
			}
		}
		else {
			return ResponseEntity.badRequest().body("Pogrešan verifikacioni kod!");
		}
		
	}

	/**
	 * Vraca sledeci task ili vise njih zajedno sa poljima forme za trenutno ulogovanog korisnika i to smao ako ima rolu admin
	 */
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path = "/get/allMyTasks", produces = "application/json")
	public @ResponseBody ResponseEntity<List<TaskDto>> allMyTasks(HttpServletRequest request) {

		String token = tokenProvider.getToken(request);
		String username = tokenProvider.getUsernameFromToken(token);

		List<Task> tasks = taskService.createTaskQuery().list();
		List<Task> myTasks = new ArrayList<>();
		for (Task t : tasks) {
			if (t.getAssignee() != null) {
				if (t.getAssignee().equals(username)) {
					System.out.println(t.getId());
					myTasks.add(t);
				}
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
	 * Vraca task sa zadatim id
	 */
	@GetMapping(path = "/getTask/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity<TaskDto> getTask(@PathVariable String taskId) {

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData tfd = formService.getTaskFormData(taskId);
		List<FormField> properties = tfd.getFormFields();
		TaskDto t = new TaskDto(taskId, task.getName(), task.getAssignee(), properties);

		return new ResponseEntity(t, HttpStatus.OK);
	}

	/**
	 * Potvrda recenzenta
	 */
	@PostMapping(path = "/rewieverAcceptance/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity rewieverAcceptance(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = mapListToDto(dto);
		formService.submitTaskForm(taskId, map);
		return ResponseEntity.status(HttpStatus.OK).build();
	}
	
	public HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmissionDto temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}

		return map;
	}
}
