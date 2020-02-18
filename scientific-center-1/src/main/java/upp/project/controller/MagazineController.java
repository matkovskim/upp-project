package upp.project.controller;

import java.util.List;

import org.camunda.bpm.engine.RuntimeService;
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

import upp.project.model.Magazine;
import upp.project.services.MagazineService;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/magazine", produces = MediaType.APPLICATION_JSON_VALUE)
public class MagazineController {
	
	@Autowired
	private MagazineService magazineService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody ResponseEntity<?> getScientificAreas() {

		List<Magazine>magazines=magazineService.getAllActivatedMagazines();
		return ResponseEntity.ok(magazines);

	}
	
	@GetMapping(value = "/registration/{processInstanceId}/{magazineId}")
	public ResponseEntity<?> successRegistration(@PathVariable String processInstanceId, @PathVariable Long magazineId) {
		
		System.out.println("MAG | registered on PaymentHub");
		
		Magazine magazine = magazineService.findById(magazineId);
		
		if(magazine != null ) {
			//set registration flag
			magazine.setRegisteredOnPaymentHub(true);
			magazineService.save(magazine);
			
			runtimeService.createMessageCorrelation("PaymentHubRegistration").processInstanceId(processInstanceId).correlateWithResult();
			
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
	}
	
	
}
