package upp.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.Magazine;
import upp.project.model.RegistredUser;

@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long> {

	Magazine findByName(String name);

	@Query("select distinct mag from Magazine as mag inner join mag.editors as editors where ?1 in editors")
	List<Magazine> findByEditor(RegistredUser user);
	List<Magazine> findByActivated(boolean activated);
}
