package upp.project.controller;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import upp.project.model.RegistredUser;
import upp.project.repository.RegistredUserRepository;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/registration", produces = MediaType.APPLICATION_JSON_VALUE)
public class RegistrationController {

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private RegistredUserRepository registredUserRepository;
	
	/**
	 * Potvrda na mailu
	 */
	@GetMapping(path = "/confirmation/{processInstanceId}/{confirmationString}", produces = "application/json")
	public @ResponseBody ResponseEntity getNextTask(@PathVariable String processInstanceId,
			@PathVariable String confirmationString) {

		String newUserUsername = (String) runtimeService.getVariable(processInstanceId, "KorisnickoIme");
		RegistredUser regUser = registredUserRepository.findByUsername(newUserUsername);
		if (regUser.getRegistrationCode().equals(confirmationString)) {
			MessageCorrelationResult results = runtimeService.createMessageCorrelation("PotvrdaMaila")
					.processInstanceId(processInstanceId).correlateWithResult();
			if (regUser.isReviewer()) {
				return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
						.header("Location", "http://localhost:4200/waitAdminConfirmation").build();
			} else {
				return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY)
						.header("Location", "http://localhost:4200/success").build();
			}
		} else {
			return ResponseEntity.badRequest().body("Pogrešan verifikacioni kod!");
		}

	}
	
}
