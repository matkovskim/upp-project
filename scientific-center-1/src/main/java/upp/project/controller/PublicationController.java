package upp.project.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import upp.project.model.Magazine;
import upp.project.model.Publication;
import upp.project.repository.MagazineRepository;
import upp.project.repository.PublicationRepository;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/publication", produces = MediaType.APPLICATION_JSON_VALUE)
public class PublicationController {

	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private MagazineRepository magazineRepository;
	
	@GetMapping(path = "/get/{magazineId}", produces = "application/json")
	public @ResponseBody ResponseEntity<?> post(@PathVariable Long magazineId) {
		
		Magazine magazine=magazineRepository.findById(magazineId).get();
		List<Publication>publications=publicationRepository.findByMagazine(magazine);
		return ResponseEntity.ok(publications);

	}
}
