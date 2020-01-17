package upp.project.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.IdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import upp.project.aasecurity.JwtProvider;
import upp.project.model.FormSubmissionDto;
import upp.project.model.RegistredUser;
import upp.project.model.UserTokenState;
import upp.project.services.AuthentificationService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthentificationController {

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	AuthentificationService authentificationService;

	@Autowired
	IdentityService identityService;

	@Lazy
	@Autowired
	private AuthenticationManager authenticationManger;

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
			identityService.setAuthenticatedUserId(username);
			return ResponseEntity.ok(new UserTokenState(token, user.getEmail(), user.getAuthorities(),
					jwtProvider.getExpirationDateFromToken(token)));
		}
		return new ResponseEntity<>("Nalog nije aktiviran", HttpStatus.BAD_REQUEST);

	}

}
