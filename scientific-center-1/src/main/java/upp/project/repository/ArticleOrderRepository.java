package upp.project.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.ArticleOrder;
import upp.project.model.OrderStatus;

@Repository
public interface ArticleOrderRepository  extends JpaRepository<ArticleOrder, Long> {

	@Query("SELECT distinct o from ArticleOrder as o WHERE o.orderStatus = ?1 or o.orderStatus = ?2 ")
	List<ArticleOrder> findOrdersBasedOnOrderStatused(OrderStatus orderStatus1, OrderStatus orderStatus2);

}
