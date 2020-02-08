package upp.project.repository;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.Authority;
import upp.project.model.RegistredUser;
import upp.project.model.Role;
import upp.project.model.ScientificArea;

@Repository
public interface RegistredUserRepository extends JpaRepository<RegistredUser, Long> {

	RegistredUser findByEmail(String email);
	RegistredUser findByUsername(String username);
	RegistredUser findByNameAndPassword(String name, String password);
	ArrayList<RegistredUser> findByAuthoritiesName(Role role);
	RegistredUser findByNameAndLastName(String name, String lastName);
	
	@Query("select distinct us from RegistredUser as us inner join us.scientificArea as scia inner join us.authorities as auth WHERE ?1 in scia and ?2 in auth")
	List<RegistredUser> userWithSpecificAuthorityForArea(ScientificArea area, Authority authority);
	
}
