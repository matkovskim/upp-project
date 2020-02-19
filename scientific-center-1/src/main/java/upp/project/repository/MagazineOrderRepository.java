package upp.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.MagazineOrder;
import upp.project.model.OrderStatus;

@Repository
public interface MagazineOrderRepository extends JpaRepository<MagazineOrder, Long> {

	@Query("SELECT distinct o from MagazineOrder as o WHERE o.orderStatus = ?1 or o.orderStatus = ?2 ")
	List<MagazineOrder> findOrdersBasedOnOrderStatused(OrderStatus orderStatus1, OrderStatus orderStatus2);

}
