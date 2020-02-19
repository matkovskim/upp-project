package upp.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.OrderStatus;
import upp.project.model.PublicationOrder;

@Repository
public interface PublicationOrderRepository  extends JpaRepository<PublicationOrder, Long> {

	@Query("SELECT distinct o from PublicationOrder as o WHERE o.orderStatus = ?1 or o.orderStatus = ?2 ")
	List<PublicationOrder> findOrdersBasedOnOrderStatused(OrderStatus orderStatus1, OrderStatus orderStatus2);

}
