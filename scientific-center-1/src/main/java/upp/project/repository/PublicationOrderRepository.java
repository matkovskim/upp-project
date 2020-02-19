package upp.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.OrderStatus;
import upp.project.model.Publication;
import upp.project.model.PublicationOrder;
import upp.project.model.RegistredUser;

@Repository
public interface PublicationOrderRepository  extends JpaRepository<PublicationOrder, Long> {

	@Query("SELECT distinct o from PublicationOrder as o WHERE o.orderStatus = ?1 or o.orderStatus = ?2 ")
	List<PublicationOrder> findOrdersBasedOnOrderStatused(OrderStatus orderStatus1, OrderStatus orderStatus2);

	@Query("SELECT distinct mag from PublicationOrder as o inner join o.buyer as buyer inner join o.issue as mag  WHERE buyer = ?1")
	List<Publication> findAllMagazinesFromOrders(RegistredUser user);
}
