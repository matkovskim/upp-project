package upp.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import upp.project.model.ScientificArea;
import upp.project.services.ScientificAreaService;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/scientificArea", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScientificAreaController {
	
	@Autowired 
	private ScientificAreaService scientificAreaService;

	@GetMapping(path = "", produces = "application/json")
	public @ResponseBody ResponseEntity<?> getScientificAreas() {

		List<ScientificArea>scientificAreas=scientificAreaService.getAllScientificServices();
		return ResponseEntity.ok(scientificAreas);

	}
	
}
