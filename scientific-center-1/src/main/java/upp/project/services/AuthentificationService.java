package upp.project.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import upp.project.model.FormSubmissionDto;
import upp.project.model.RegistredUser;
import upp.project.model.ScientificArea;
import upp.project.repository.RegistredUserRepository;
import upp.project.repository.ScientificAreaRepository;

@Service
public class AuthentificationService {

	@Autowired
	RegistredUserRepository registredUserRepository;

	@Autowired
	ScientificAreaRepository scientificAreaRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	public HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmissionDto temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}

		return map;
	}

	/**
	 * Pravljenje novog korisnika i upis u bazu
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

}
