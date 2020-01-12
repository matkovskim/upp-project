package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.Authority;
import upp.project.model.Role;

@Repository
public interface AuthorityRespository extends JpaRepository<Authority, Long> {
	
	Authority findByName(Role name);
	
}
