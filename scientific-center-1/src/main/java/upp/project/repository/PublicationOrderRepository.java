package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.PublicationOrder;

@Repository
public interface PublicationOrderRepository  extends JpaRepository<PublicationOrder, Long> {

}
