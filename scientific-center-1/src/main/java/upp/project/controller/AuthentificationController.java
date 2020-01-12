package upp.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import upp.project.aasecurity.JwtProvider;
import upp.project.model.FormSubmissionDto;
import upp.project.model.RegistredUser;
import upp.project.model.UserTokenState;
import upp.project.repository.RegistredUserRepository;
import upp.project.services.AuthentificationService;
import upp.project.services.AuthorityService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthentificationController {

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	AuthentificationService authentificationService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	TaskService taskService;

	@Autowired
	FormService formService;

	@Autowired
	RegistredUserRepository registredUserRepository;

	@Autowired
	AuthorityService authorityService;

	@Lazy
	@Autowired
	private AuthenticationManager authenticationManger;

	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * Registracija korisnika
	 */
	@PostMapping(path = "/post/{taskId}", produces = "application/json")
	public @ResponseBody ResponseEntity post(@RequestBody List<FormSubmissionDto> dto, @PathVariable String taskId) {

		HashMap<String, Object> map = authentificationService.mapListToDto(dto);
		for (Map.Entry mapElement : map.entrySet()) {
			String key = (String) mapElement.getKey();
			if (key.equals("NaucneOblasti")) {
				String value = (String) mapElement.getValue();
				String[] parts = value.split(",");
				if (parts.length != 1) {
					mapElement.setValue(parts[0]);
				}
			}
		}

		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();

		runtimeService.setVariable(processInstanceId, "newUser", dto);

		//Validacije
		RegistredUser newUser = authentificationService.registerNewUser(dto);
		if (newUser == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Već postoji registrovan korisnik sa tim korisničkim imenom!");
		}

		if (newUser.getId() == -1) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Morate izabrati barem jednu naučnu oblast!");
		}

		if (!authentificationService.isValidEmailAddress(newUser.getEmail())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Email adresa nije dobrog formata!");
		}

		try {
			formService.submitTaskForm(taskId, map);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Uneti podaci nisu validni!");
		}

		return new ResponseEntity<>(HttpStatus.OK);

	}

	/**
	 * Logovanje korisnika
	 */
	@PostMapping(path = "/login", produces = "application/json")
	public @ResponseBody ResponseEntity login(@RequestBody List<FormSubmissionDto> dto) {

		String username = "";
		String password = "";

		HashMap<String, Object> map = authentificationService.mapListToDto(dto);
		for (Map.Entry mapElement : map.entrySet()) {
			String key = (String) mapElement.getKey();
			if (key.equals("username")) {
				username = (String) mapElement.getValue();
			}
			if (key.equals("password")) {
				password = (String) mapElement.getValue();
			}
		}

		Authentication authentication = null;

		try {
			authentication = authenticationManger
					.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (AuthenticationException e) {
			e.printStackTrace();
			return new ResponseEntity<>("Kredencijali se ne poklapaju!", HttpStatus.BAD_REQUEST);
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		RegistredUser user = (RegistredUser) authentication.getPrincipal();
		String token = jwtProvider.generateJwtToken(user.getUsername(), 1);
		if (user.isConfirmed()) {
			return ResponseEntity.ok(new UserTokenState(token, user.getEmail(), user.getAuthorities(),
					jwtProvider.getExpirationDateFromToken(token)));
		}
		return new ResponseEntity<>("Nalog nije aktiviran", HttpStatus.BAD_REQUEST);

	}

}
