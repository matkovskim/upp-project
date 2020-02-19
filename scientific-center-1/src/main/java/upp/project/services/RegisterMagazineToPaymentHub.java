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
import upp.project.model.RegistrationDTO;
import upp.project.model.RegistrationResponseDTO;
import upp.project.repository.MagazineRepository;

@Service
public class RegisterMagazineToPaymentHub implements JavaDelegate{
	
	@Autowired
	MagazineService magazineService;
	
	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	MagazineRepository magazineRepository;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String magazineName = (String) execution.getVariable("NazivCasopisa");
		Double subscriptionPrice = (Double) execution.getVariable("cenaMesecnePretplate");
		Long articlePrice = (Long) execution.getVariable("cenaIzdanja");
		Long publicationPrice = (Long) execution.getVariable("cenaRada");

		Magazine magazine = magazineRepository.findByName(magazineName);
	
		if(magazine != null) {
			
			magazine.setPublicationPrice(publicationPrice);
			magazine.setArticlePrice(articlePrice);
			magazine.setSubscriptionPrice(subscriptionPrice);
			magazineRepository.save(magazine);
			
			String successUrl = "https://localhost:9990/magazine/registration/" + execution.getProcessInstanceId() + "/" + magazine.getId();
			RegistrationDTO registrationDTO = new RegistrationDTO(magazine.getEmail(), magazine.getName(), successUrl, "https://localhost:4204/tasks");	
	
			HttpEntity<RegistrationDTO> request = new HttpEntity<>(registrationDTO);
				
			//send a request to PaymentHub with information about the magazine
			ResponseEntity<RegistrationResponseDTO> response = null;
			try {
				response = restTemplate.exchange("https://localhost:8762/api/client/seller", HttpMethod.POST, request, RegistrationResponseDTO.class);
			} 
			catch (RestClientException e) {
				throw e;
			}
			
			//set the link for redirection as a process variable
			if(response != null && response.getBody().getResponseUrl() != null) {
				execution.setVariable("redirectUrl", response.getBody().getResponseUrl());	
			}					
		}		
	}

}
