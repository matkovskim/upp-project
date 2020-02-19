package upp.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.Magazine;
import upp.project.model.MagazineOrder;
import upp.project.model.OrderStatus;
import upp.project.model.RegistredUser;

@Repository
public interface MagazineOrderRepository extends JpaRepository<MagazineOrder, Long> {

	@Query("SELECT distinct o from MagazineOrder as o WHERE o.orderStatus = ?1 or o.orderStatus = ?2 ")
	List<MagazineOrder> findOrdersBasedOnOrderStatused(OrderStatus orderStatus1, OrderStatus orderStatus2);

	@Query("SELECT distinct mag from MagazineOrder as o inner join o.buyer as buyer inner join o.magazine as mag  WHERE buyer = ?1")
	List<Magazine> findAllMagazinesFromOrders(RegistredUser user);
}
