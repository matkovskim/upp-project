package upp.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.Magazine;
import upp.project.model.Publication;

@Repository
public interface PublicationRepository extends JpaRepository<Publication, Long> {

	Publication findByMagazineAndPublished(Magazine magazine, boolean published);
	List<Publication>findByPublished(boolean published);
	List<Publication> findByMagazine(Magazine magazine);
	
}
