package upp.project.controller;

import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import upp.project.model.Article;
import upp.project.model.ArticleOrder;
import upp.project.model.Magazine;
import upp.project.model.MagazineOrder;
import upp.project.model.MembershipFeeds;
import upp.project.model.OrderInformationDTO;
import upp.project.model.OrderResponseDTO;
import upp.project.model.OrderStatus;
import upp.project.model.Publication;
import upp.project.model.PublicationOrder;
import upp.project.model.RedirectDTO;
import upp.project.model.RegistredUser;
import upp.project.model.UserOrder;
import upp.project.model.UserOrderDTO;
import upp.project.repository.ArticleRepository;
import upp.project.repository.MembershipFeesRepository;
import upp.project.repository.PublicationRepository;
import upp.project.repository.RegistredUserRepository;
import upp.project.services.MagazineService;
import upp.project.services.UserCustomService;
import upp.project.services.UserOrderService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrderController {

	@Autowired
	private RegistredUserRepository registredUserRepository;

	@Autowired
	private MembershipFeesRepository membershipFeesRepository;

	@Autowired
	private UserOrderService userOrderService;

	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private MagazineService magazineServicce;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PublicationRepository publicationRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	//@Autowired
	//private MembershipService membershipService;
	
	@Value("https://localhost:8762/api/client/orders/create")
	private String kpUrl;

	@PostMapping("/create")
	public ResponseEntity<?> createOrder(Principal principal, @RequestBody UserOrderDTO orderDTO) {

		UserOrder userOrder = null;
		
		String username="";
		if(principal!=null) {
			username=principal.getName();
		}
		else {
			return ResponseEntity.status(400).body("Niko nije ulogovan");
		}
		RegistredUser user = registredUserRepository.findByUsername(username);
		
		if(orderDTO.getIds().isEmpty()) {
			return ResponseEntity.status(400).body("Prazna lista");
		}
		
		if(orderDTO.getType().equals("magazine")) {
			
			Magazine magazine = this.magazineServicce.findById(orderDTO.getIds().get(0));
			if(magazine == null) {
				return ResponseEntity.status(400).body("Nepostojeci magazin");
			}
			
			userOrder = new MagazineOrder(magazine);
			userOrder.setEmail(magazine.getEmail());
			userOrder.setPaymentAmount(magazine.getSubscriptionPrice().doubleValue());
			
		} else if(orderDTO.getType().equals("publication")) {
			Set<Publication> publications = new HashSet<Publication>();
			Magazine magazine = null;
			for(Long id : orderDTO.getIds()) {
				Publication publication = publicationRepository.getOne(id);
				if(publication == null)
					return ResponseEntity.status(400).body("Nepostojeci issue");
				
				if(magazine == null)
					magazine = publication.getMagazine();
				
				publications.add(publication);
			}
			userOrder = new PublicationOrder(publications);
			userOrder.setEmail(magazine.getEmail());
			userOrder.setPaymentAmount(publications.size() * magazine.getPublicationPrice());
			
			
		} else {
			Set<Article> papers = new HashSet<Article>();
			Magazine magazine = null;
			for(Long id : orderDTO.getIds()) {
				Article article = articleRepository.getOne(id);
				if(article == null)
					return ResponseEntity.status(400).body("nepostojeci paper");
				
				if(magazine == null)
					magazine = article.getPublication().getMagazine();
			
				papers.add(article);
			} 
			
			userOrder = new ArticleOrder(papers);
			userOrder.setEmail(magazine.getEmail());
			userOrder.setPaymentAmount(papers.size() * magazine.getArticlePrice());
		}
		
		userOrder.setBuyer(user);
		userOrder.setOrderStatus(OrderStatus.CREATED);
		userOrder.setPaymentCurrency("USD");

		userOrder = this.userOrderService.save(userOrder);
		if (userOrder == null) {
			return ResponseEntity.status(400).body("greska prilikom cuvanja");
		}

		OrderInformationDTO orderInformationDTO = new OrderInformationDTO();
		orderInformationDTO.setPaymentCurrency("USD");
		orderInformationDTO.setSuccessUrl("https://localhost:8080/orders/success"+ "?id=" + userOrder.getId() + "&type=" + orderDTO.getType());
		orderInformationDTO.setFailedUrl("https://localhost:8080/orders/failed"+ "?id=" + userOrder.getId() + "&type=" + orderDTO.getType());
		orderInformationDTO.setErrorUrl("https://localhost:8080/orders/error"+ "?id=" + userOrder.getId() + "&type=" + orderDTO.getType());
		orderInformationDTO.setOrderId(userOrder.getId());
		orderInformationDTO.setEmail(userOrder.getEmail());
		orderInformationDTO.setPaymentAmount(userOrder.getPaymentAmount());

		HttpEntity<OrderInformationDTO> request = new HttpEntity<>(orderInformationDTO);

		ResponseEntity<OrderResponseDTO> response = null;
		try {
			response = restTemplate.exchange("https://localhost:8762/api/client/orders/create", HttpMethod.POST, request, OrderResponseDTO.class);
		} catch (RestClientException e) {
			e.printStackTrace();
			userOrder.setOrderStatus(OrderStatus.ERROR);
			return ResponseEntity.status(400).body("Greska prilikom kontaktiranja kpa");
		}
		
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl(response.getBody().getRedirectUrl());
		return ResponseEntity.ok(redirectDTO);
	}
	
	@GetMapping("/success")
	public ResponseEntity<?> successfulOrder(@RequestParam("id") Long id, @RequestParam("type") String type, @RequestParam("processId") Optional<String> processId ) {
		if(processId.isPresent()) {
			this.runtimeService.setVariable(processId.get(), "paymentSuccessful" , true);
		}
		
		UserOrder userOrder = null;
		if(type.equals("magazine")) {
			userOrder = this.userOrderService.getMagazineOrder(id);
		} else if(type.equals("publication")) {
			userOrder = this.userOrderService.getPublicationOrder(id);
		} else {
			userOrder = this.userOrderService.getArticleOrder(id);
		}
		
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4204/successPayed");
		
		if(userOrder == null) {
			redirectDTO.setUrl("https://localhost:4204/error");
			return ResponseEntity.ok(redirectDTO);
		}
		
		userOrder.setOrderStatus(OrderStatus.SUCCEEDED);
		this.userOrderService.save(userOrder);
		
		return ResponseEntity.ok(redirectDTO);
		
	}
	
	@GetMapping("/failed")
	public ResponseEntity<?> failedOrder(@RequestParam("id") Long id, @RequestParam("type") String type, @RequestParam("processId") Optional<String> processId) {
		
		if(processId.isPresent()) {
			this.runtimeService.setVariable(processId.get(), "paymentSuccessful" , false);
		}
		
		UserOrder userOrder = null;
		if(type.equals("magazine")) {
			userOrder = this.userOrderService.getMagazineOrder(id);
		} else if(type.equals("issue")) {
			userOrder = this.userOrderService.getPublicationOrder(id);
		} else {
			userOrder = this.userOrderService.getArticleOrder(id);
		}
		
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4204/cancel");
		
		if(userOrder == null) {
			redirectDTO.setUrl("https://localhost:4204/error");
			return ResponseEntity.ok(redirectDTO);
		}
		
		userOrder.setOrderStatus(OrderStatus.FAILED);
		this.userOrderService.save(userOrder);
		
		return ResponseEntity.ok(redirectDTO);
		
	}
	
	@GetMapping("/error")
	public ResponseEntity<?> errorOrder(@RequestParam("id") Long id, @RequestParam("type") String type, @RequestParam("processId") Optional<String> processId) {
		
		if(processId.isPresent()) {
			this.runtimeService.setVariable(processId.get(), "paymentSuccessful" , false);
		}
		
		UserOrder userOrder = null;
		if(type.equals("magazine")) {
			userOrder = this.userOrderService.getMagazineOrder(id);
		} else if(type.equals("issue")) {
			userOrder = this.userOrderService.getPublicationOrder(id);
		} else {
			userOrder = this.userOrderService.getArticleOrder(id);
		}
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4204/error");
		
		if(userOrder == null) {
			return ResponseEntity.ok(redirectDTO);
		}
		
		userOrder.setOrderStatus(OrderStatus.ERROR);
		this.userOrderService.save(userOrder);
		
		return ResponseEntity.ok(redirectDTO);
		
	}
	
	@GetMapping("/successMembership")
	private ResponseEntity<?> successfulMembership(@RequestParam("id") Long id,
			@RequestParam("procesId") String procesId) {
		if (procesId != null) {
			MessageCorrelationResult results = runtimeService.createMessageCorrelation("PlacanjeUspesno")
					.processInstanceId(procesId).correlateWithResult();
		}
		UserOrder order = userOrderService.getUserOrder(id);
		RegistredUser user = order.getBuyer();
		Magazine magazine = order.getMagazine();
		
		Date nextMonth = new Date(System.currentTimeMillis() + 31 * 24 * 60 * 60 * 1000);
		MembershipFeeds mf=new MembershipFeeds();
		mf.setExpirationDate(nextMonth);
		mf.setMagazine(magazine);			
		membershipFeesRepository.save(mf);
		Set<MembershipFeeds>memberships=user.getMembershipFees();
		memberships.add(mf);
		user.setMembershipFees(memberships);
		registredUserRepository.save(user);
		
		order.setOrderStatus(OrderStatus.SUCCEEDED);
		userOrderService.save(order);
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4203/successPayed");
		return ResponseEntity.ok(redirectDTO);

	}

}
