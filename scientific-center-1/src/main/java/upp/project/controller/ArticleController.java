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

import upp.project.model.Article;
import upp.project.model.Publication;
import upp.project.repository.ArticleRepository;
import upp.project.repository.PublicationRepository;

@Controller
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/article", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArticleController {
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private ArticleRepository aericleRepository;
	
	@GetMapping(path = "/get/{publicationId}", produces = "application/json")
	public @ResponseBody ResponseEntity<?> post(@PathVariable Long publicationId) {
		
		Publication publication=publicationRepository.findById(publicationId).get();
		List<Article>articles=aericleRepository.findByPublication(publication);
		return ResponseEntity.ok(articles);

	}

}
