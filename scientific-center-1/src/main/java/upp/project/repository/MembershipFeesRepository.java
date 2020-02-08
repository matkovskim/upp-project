package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.MembershipFees;

@Repository
public interface MembershipFeesRepository extends JpaRepository<MembershipFees, Long> {
	
}

