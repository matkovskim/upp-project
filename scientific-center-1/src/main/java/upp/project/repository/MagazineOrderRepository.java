package upp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.MagazineOrder;

@Repository
public interface MagazineOrderRepository  extends JpaRepository<MagazineOrder, Long> {

}
