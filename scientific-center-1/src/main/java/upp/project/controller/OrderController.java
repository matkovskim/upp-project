package upp.project.controller;

import java.util.Date;
import java.util.Optional;
import java.util.Set;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import upp.project.model.Magazine;
import upp.project.model.MembershipFees;
import upp.project.model.OrderStatus;
import upp.project.model.RedirectDTO;
import upp.project.model.RegistredUser;
import upp.project.model.UserOrder;
import upp.project.repository.MembershipFeesRepository;
import upp.project.repository.RegistredUserRepository;
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

	@Value("https://localhost:9897/orders/success")
	private String successUrl;

	@Value("https://localhost:9897/orders/failed")
	private String failedUrl;

	@Value("https://localhost:9897/orders/error")
	private String errorUrl;

	@Value("https://localhost:8762/api/client/orders/create")
	private String kpUrl;

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
		MembershipFees mf=new MembershipFees();
		mf.setExpirationDate(nextMonth);
		mf.setMagazine(magazine);			
		membershipFeesRepository.save(mf);
		Set<MembershipFees>memberships=user.getMembershipFees();
		memberships.add(mf);
		user.setMembershipFees(memberships);
		registredUserRepository.save(user);
		
		order.setOrderStatus(OrderStatus.SUCCEEDED);
		userOrderService.save(order);
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4203/successPayed");
		return ResponseEntity.ok(redirectDTO);

	}

	@GetMapping("/success")
	private ResponseEntity<?> successfulOrder(@RequestParam("id") Long id,
			@RequestParam("procesId") Optional<String> procesId) {
		if (procesId != null) {
			MessageCorrelationResult results = runtimeService.createMessageCorrelation("PlacanjeUspesno")
					.processInstanceId(procesId.get()).correlateWithResult();
		}
		UserOrder userOrder = userOrderService.getUserOrder(id);
		userOrder.setOrderStatus(OrderStatus.SUCCEEDED);
		this.userOrderService.save(userOrder);
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4203/success");
		return ResponseEntity.ok(redirectDTO);
	}

	@GetMapping("/failed")
	private ResponseEntity<?> failedOrder(@RequestParam("id") Long id) {
		UserOrder userOrder = userOrderService.getUserOrder(id);
		userOrder.setOrderStatus(OrderStatus.FAILED);
		this.userOrderService.save(userOrder);

		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4203/cancel");
		return ResponseEntity.ok(redirectDTO);
	}

	@GetMapping("/error")
	private ResponseEntity<?> errorOrder(@RequestParam("id") Long id) {
		UserOrder userOrder = this.userOrderService.getUserOrder(id);
		userOrder.setOrderStatus(OrderStatus.ERROR);
		this.userOrderService.save(userOrder);
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4203/error");
		return ResponseEntity.ok(redirectDTO);
	}

}
