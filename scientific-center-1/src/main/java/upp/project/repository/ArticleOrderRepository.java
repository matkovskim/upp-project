package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.ArticleOrder;

@Repository
public interface ArticleOrderRepository  extends JpaRepository<ArticleOrder, Long> {

}
