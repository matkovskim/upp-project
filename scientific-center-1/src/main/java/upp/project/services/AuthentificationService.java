package upp.project.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import upp.project.model.Authority;
import upp.project.model.FormSubmissionDto;
import upp.project.model.RegisterAdminDTO;
import upp.project.model.RegisterEditorDTO;
import upp.project.model.RegistredUser;
import upp.project.model.Role;
import upp.project.model.ScientificArea;
import upp.project.repository.RegistredUserRepository;
import upp.project.repository.ScientificAreaRepository;

@Service
public class AuthentificationService {

	@Autowired
	private RegistredUserRepository registredUserRepository;

	@Autowired
	private ScientificAreaRepository scientificAreaRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private IdentityService identityService;

	public HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmissionDto temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}

		return map;
	}

	/**
	 * Pravljenje novog korisnika
	 */
	public RegistredUser registerNewUser(List<FormSubmissionDto> dto) {
		RegistredUser newUser = new RegistredUser();

		for (FormSubmissionDto fsDTO : dto) {
			if (fsDTO.getFieldId().equals("Ime")) {
				newUser.setName(fsDTO.getFieldValue());
			} else if (fsDTO.getFieldId().equals("Prezime")) {
				newUser.setLastName(fsDTO.getFieldValue());
			} else if (fsDTO.getFieldId().equals("Grad")) {
				newUser.setCity(fsDTO.getFieldValue());
			} else if (fsDTO.getFieldId().equals("Drzava")) {
				newUser.setState(fsDTO.getFieldValue());
			} else if (fsDTO.getFieldId().equals("Titula")) {
				newUser.setTitle(fsDTO.getFieldValue());
			} else if (fsDTO.getFieldId().equals("Email")) {
				newUser.setEmail(fsDTO.getFieldValue());
			} else if (fsDTO.getFieldId().equals("KorisnickoIme")) {
				newUser.setUsername(fsDTO.getFieldValue());
			} else if (fsDTO.getFieldId().equals("Recenzent")) {
				if (fsDTO.getFieldValue().equals("true")) {
					newUser.setReviewer(true);
				} else {
					newUser.setReviewer(false);
				}
			} else if (fsDTO.getFieldId().equals("Lozinka")) {
				newUser.setPassword(passwordEncoder.encode(fsDTO.getFieldValue()));
			} else {
				if (fsDTO.getFieldValue() != null && fsDTO.getFieldValue() != "") {
					String[] parts = fsDTO.getFieldValue().split(",");
					Set<ScientificArea> areas = new HashSet<ScientificArea>();
					for (String part : parts) {
						ScientificArea area = scientificAreaRepository.findByName(part);
						areas.add(area);
					}
					newUser.setScientificArea(areas);
				} else {
					RegistredUser retUser = new RegistredUser();
					retUser.setId(-1);
					return retUser;
				}
			}

			// provera da li postoji vec korisnik sa tim emailom
			RegistredUser existingUser = registredUserRepository.findByUsername(newUser.getUsername());
			if (existingUser != null) {
				return null;
			}
		}
		return newUser;
	}

	public static boolean isValidEmailAddress(String email) {
		boolean result = true;
		try {
			InternetAddress emailAddr = new InternetAddress(email);
			emailAddr.validate();
		} catch (AddressException ex) {
			result = false;
		}
		return result;
	}

	public List<RegistredUser> getAll() {
		return registredUserRepository.findAll();
	}

	public boolean registerAdmin(RegisterAdminDTO dto) {

		RegistredUser newAdmin = new RegistredUser();
		newAdmin.setName(dto.getName());
		newAdmin.setEmail(dto.getEmail());
		newAdmin.setLastName(dto.getLastName());
		newAdmin.setUsername(dto.getUsername());
		newAdmin.setCity(dto.getCity());
		newAdmin.setPassword(passwordEncoder.encode(dto.getPassword()));
		newAdmin.setState(dto.getState());
		newAdmin.setConfirmed(true);
		newAdmin.setReviewer(false);

		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(authorityService.findByName(Role.ROLE_ADMIN));
		newAdmin.setAuthorities(authorities);

		registerInCamunda(newAdmin);

		identityService.createMembership(newAdmin.getUsername(), "administratori");
		registredUserRepository.save(newAdmin);

		return true;

	}

	public boolean registerEditor(RegisterEditorDTO dto) {

		RegistredUser newEditor = new RegistredUser();
		newEditor.setName(dto.getName());
		newEditor.setEmail(dto.getEmail());
		newEditor.setLastName(dto.getLastName());
		newEditor.setUsername(dto.getUsername());
		newEditor.setCity(dto.getCity());
		newEditor.setPassword(passwordEncoder.encode(dto.getPassword()));
		newEditor.setState(dto.getState());
		newEditor.setTitle(dto.getTitle());
		newEditor.setConfirmed(true);
		newEditor.setReviewer(false);

		Set<ScientificArea>areas=new HashSet<>();
		if(!dto.getScientificAreas().equals("")) {
			String[]areaNames=dto.getScientificAreas().split(",");
			for(String area:areaNames) {
				ScientificArea foundArea=scientificAreaRepository.findByName(area);
				if(foundArea!=null) {
					areas.add(foundArea);
				}
			}
			newEditor.setScientificArea(areas);
		}
		
		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(authorityService.findByName(Role.ROLE_EDITOR));
		newEditor.setAuthorities(authorities);
		registerInCamunda(newEditor);

		identityService.createMembership(newEditor.getUsername(), "urednici");
		registredUserRepository.save(newEditor);

		return true;

	}

	private void registerInCamunda(RegistredUser newUser) {
		try {
			User camundaUser = identityService.newUser(newUser.getUsername());
			camundaUser.setPassword(newUser.getPassword());
			camundaUser.setFirstName(newUser.getName());
			camundaUser.setLastName(newUser.getLastName());
			camundaUser.setEmail(newUser.getEmail());
			identityService.saveUser(camundaUser);
		} catch (Exception e) {
			System.out.println("Korisnik vec postoji");
		}
	}
}
