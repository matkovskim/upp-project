package upp.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import upp.project.model.OrderStatus;
import upp.project.model.UserSubscription;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long>{

	List<UserSubscription> findBySubscriptionStatusIn(List<OrderStatus> statuses);

}
