package upp.project.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.FormSubmissionDto;
import upp.project.model.Magazine;
import upp.project.model.ScientificArea;
import upp.project.repository.MagazineRepository;
import upp.project.repository.ScientificAreaRepository;

@Service
public class MagazineService {

	public static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
			Pattern.CASE_INSENSITIVE);

	public static boolean validate(String emailStr) {
		Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
		return matcher.find();
	}
	
	@Autowired
	private MagazineRepository magazineRepository;

	@Autowired
	private ScientificAreaRepository scientificAreaRepository;

	public Magazine makeMagazine(List<FormSubmissionDto> dto) {

		Magazine magazine = new Magazine();

		for (FormSubmissionDto fsDTO : dto) {
			if (fsDTO.getFieldId().equals("NazivCasopisa")) {
				magazine.setName(fsDTO.getFieldValue());
			} else if (fsDTO.getFieldId().equals("ISBN")) {
				magazine.setISBN(fsDTO.getFieldValue());
			} else if (fsDTO.getFieldId().equals("NacinNaplate")) {
				magazine.setWhoPays(fsDTO.getFieldValue());
			} else if (fsDTO.getFieldId().equals("email")) {
				System.out.println("IMAM EMAIL");
				magazine.setEmail(fsDTO.getFieldValue());
			} else {
				if (fsDTO.getFieldValue() != null && fsDTO.getFieldValue() != "") {
					String[] parts = fsDTO.getFieldValue().split(",");
					Set<ScientificArea> areas = new HashSet<ScientificArea>();
					for (String part : parts) {

						ScientificArea area = scientificAreaRepository.findByName(part);
						if (area != null) {
							areas.add(area);
						}
					}
					magazine.setScientificArea(areas);
				} else {
					Magazine retMag = new Magazine();
					retMag.setId(-1);
					return retMag;
				}
			}

			// provera da li postoji vec magazin sa tim imenom
			Magazine existingMagazine = magazineRepository.findByName(magazine.getName());
			if (existingMagazine != null) {
				return null;
			}
			
		}
		//provera email
		if(!validate(magazine.getEmail())){
			Magazine retMag = new Magazine();
			retMag.setId(-1);
			return retMag;
		}
		return magazine;

	}

	public HashMap<String, Object> mapListToDto(List<FormSubmissionDto> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmissionDto temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}
		return map;
	}
	
	public List<Magazine>getAllMagazines(){
		return magazineRepository.findAll();
	}

	public List<Magazine>getAllActivatedMagazines(){
		return magazineRepository.findByActivated(true);
	}
}
