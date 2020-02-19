package upp.project.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import upp.project.model.Article;
import upp.project.model.ArticleOrder;
import upp.project.model.Magazine;
import upp.project.model.MagazineOrder;
import upp.project.model.OrderStatus;
import upp.project.model.OrderStatusInformationDTO;
import upp.project.model.Publication;
import upp.project.model.PublicationOrder;
import upp.project.model.RegistredUser;
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
	private MagazineOrderRepository magazineOrderRepository;

	@Autowired
	private UserOrderRepository userOrderRepository;

	@Autowired
	private RestTemplate restTemplate;

	@Value("https://localhost:8762/api/client/orders/status")
	private String kpUrl;

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

	@Scheduled(initialDelay = 10000, fixedRate = 60000)
	public void checkOrdersStatus() {

		List<UserOrder> orders = new ArrayList<UserOrder>();
		orders.addAll(
				this.magazineRepository.findOrdersBasedOnOrderStatused(OrderStatus.INITIATED, OrderStatus.CREATED));
		orders.addAll(this.publicationOrderRepository.findOrdersBasedOnOrderStatused(OrderStatus.INITIATED,
				OrderStatus.CREATED));
		orders.addAll(
				this.articleOrderRepository.findOrdersBasedOnOrderStatused(OrderStatus.INITIATED, OrderStatus.CREATED));
		for (UserOrder order : orders) {
			ResponseEntity<OrderStatusInformationDTO> response = null;
			try {
				System.out.println(this.kpUrl + "?orderId=" + order.getId() + "&email=" + order.getEmail());
				response = restTemplate.getForEntity(
						this.kpUrl + "?orderId=" + order.getId() + "&email=" + order.getEmail(),
						OrderStatusInformationDTO.class);
			} catch (RestClientException e) {
				e.printStackTrace();
				return;
			}

			OrderStatus status = OrderStatus.valueOf(response.getBody().getStatus());

			if (status != null && !status.equals(order.getOrderStatus())) {
				order.setOrderStatus(status);
				this.save(order);
			}
		}
	}
	
	
	public List<Magazine> getAllPurchasedMagazines(RegistredUser user) {
		return magazineOrderRepository.findAllMagazinesFromOrders(user);
	}
	
	public List<Publication> getAllPurchasedIssues(RegistredUser user) {
		return publicationOrderRepository.findAllMagazinesFromOrders(user);
	}
	
	public List<Article> getAllPurchasedPapers(RegistredUser user) {
		return articleOrderRepository.findAllMagazinesFromOrders(user);
	}
}
