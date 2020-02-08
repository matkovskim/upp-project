package upp.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.ScientificArea;
import upp.project.repository.ScientificAreaRepository;

@Service
public class ScientificAreaService {

	@Autowired
	private ScientificAreaRepository scientificAreaRepository;
	
	public List<ScientificArea> getAllScientificServices() {
		return scientificAreaRepository.findAll();
	}
	
}
