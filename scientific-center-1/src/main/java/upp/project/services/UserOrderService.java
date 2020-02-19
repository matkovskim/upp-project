package upp.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.ArticleOrder;
import upp.project.model.MagazineOrder;
import upp.project.model.PublicationOrder;
import upp.project.model.UserOrder;
import upp.project.repository.ArticleOrderRepository;
import upp.project.repository.MagazineOrderRepository;
import upp.project.repository.PublicationOrderRepository;
import upp.project.repository.UserOrderRepository;

@Service
public class UserOrderService {

	@Autowired
	private MagazineOrderRepository magazineRepository;

	@Autowired
	private PublicationOrderRepository publicationOrderRepository;

	@Autowired
	private ArticleOrderRepository articleOrderRepository;

	@Autowired
	private UserOrderRepository userOrderRepository;
	
	public UserOrder save(UserOrder order) {
		
		UserOrder saved = null;

		if (order instanceof MagazineOrder) {
			try {
				saved = this.magazineRepository.save((MagazineOrder) order);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (order instanceof PublicationOrder) {
			try {
				saved = this.publicationOrderRepository.save((PublicationOrder) order);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				saved = articleOrderRepository.save((ArticleOrder) order);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return saved;
	}

	public UserOrder getMagazineOrder(Long id) {
		return this.magazineRepository.getOne(id);
	}

	public UserOrder getPublicationOrder(Long id) {
		return this.publicationOrderRepository.getOne(id);
	}

	public UserOrder getArticleOrder(Long id) {
		return this.articleOrderRepository.getOne(id);
	}

	public UserOrder getUserOrder(String uuid) {
		return this.userOrderRepository.findByUuid(uuid);
	}

	public UserOrder getUserOrder(Long id) {
		return this.userOrderRepository.getOne(id);
	}
}
