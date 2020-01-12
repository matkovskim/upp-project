package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import upp.project.model.ScientificArea;

public interface ScientificAreaRepository extends JpaRepository<ScientificArea, Long> {

	ScientificArea findByName(String name);	
	
}
