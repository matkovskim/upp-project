package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import upp.project.model.CoAuthor;

public interface CoAuthorRepository extends JpaRepository<CoAuthor, Long> {
		
}
