package upp.project.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.RegistredUser;
import upp.project.model.Role;

@Repository
public interface RegistredUserRepository extends JpaRepository<RegistredUser, Long> {

	RegistredUser findByEmail(String email);
	RegistredUser findByUsername(String username);
	RegistredUser findByNameAndPassword(String name, String password);
	ArrayList<RegistredUser> findByAuthoritiesName(Role role);
	RegistredUser findByNameAndLastName(String name, String lastName);

}
