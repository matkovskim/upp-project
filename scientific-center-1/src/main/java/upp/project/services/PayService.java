package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import upp.project.model.Magazine;
import upp.project.model.OrderInformationDTO;
import upp.project.model.OrderResponseDTO;
import upp.project.model.OrderStatus;
import upp.project.model.RegistredUser;
import upp.project.model.UserOrder;
import upp.project.repository.MagazineRepository;
import upp.project.repository.RegistredUserRepository;
import upp.project.repository.UserOrderRepository;

@Service
public class PayService implements JavaDelegate {

	@Autowired
	private MagazineRepository magazineRepository;

	@Autowired
	private UserOrderRepository userOrderRepository;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RegistredUserRepository registredUserRepository;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		String magazineName = (String) execution.getVariable("izborCasopisa");
		Magazine magazine = magazineRepository.findByName(magazineName);

		String username = (String) execution.getVariable("starter");
		RegistredUser user=registredUserRepository.findByUsername(username);
		
		UserOrder userOrder = new UserOrder();
		userOrder.setMagazine(magazine);
		userOrder.setOrderStatus(OrderStatus.CREATED);
		userOrder.setPaymentAmount(50);
		userOrder.setPaymentCurrency("USD");
		userOrder.setBuyer(user);
		userOrder = this.userOrderRepository.save(userOrder);

		OrderInformationDTO orderInformationDTO = new OrderInformationDTO();
		orderInformationDTO.setPaymentCurrency("USD");
		orderInformationDTO.setPaymentAmount(50.0);
	    orderInformationDTO.setErrorUrl("https://localhost:8080/orders/error"+ "?id=" + userOrder.getId() + "&procesId="+execution.getProcessInstanceId());
	    orderInformationDTO.setFailedUrl("https://localhost:8080/orders/failed"+ "?id=" + userOrder.getId() + "&procesId="+execution.getProcessInstanceId());
		orderInformationDTO.setSuccessUrl("https://localhost:8080/orders/successMembership"+ "?id=" + userOrder.getId() + "&procesId="+execution.getProcessInstanceId());
		
		orderInformationDTO.setEmail(magazine.getEmail());
		orderInformationDTO.setOrderId(userOrder.getId());

		HttpEntity<OrderInformationDTO> request = new HttpEntity<>(orderInformationDTO);

		ResponseEntity<OrderResponseDTO> response = null;
		try {
			response = restTemplate.exchange("https://localhost:8762/api/client/orders/create", HttpMethod.POST,
					request, OrderResponseDTO.class);
		} catch (RestClientException e) {
			userOrder.setOrderStatus(OrderStatus.ERROR);
			return;
		}
		execution.setVariable("redirectUrl", response.getBody().getRedirectUrl());
		return;
	}

}
