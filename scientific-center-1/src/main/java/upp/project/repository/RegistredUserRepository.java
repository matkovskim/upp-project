package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import upp.project.model.RegistredUser;

public interface RegistredUserRepository extends JpaRepository<RegistredUser, Long> {

	RegistredUser findByEmail(String email);
	RegistredUser findByUsername(String username);
	RegistredUser findByNameAndPassword(String name, String password);

}
