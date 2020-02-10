package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import upp.project.model.UserOrder;

public interface UserOrderRepository  extends JpaRepository<UserOrder, Long> {
	
	UserOrder findByUuid(String uuid);

}
