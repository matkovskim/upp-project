package upp.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.UserSubscription;
import upp.project.repository.UserSubscriptionRepository;

@Service
public class UserSubscriptionService {
	
	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;
	
	public UserSubscription save(UserSubscription subscription) {
		UserSubscription saved = null;
		
		try {
			saved = this.userSubscriptionRepository.save(subscription);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return saved;
	}
	
	public UserSubscription getUserSubscription(Long id) {
		return userSubscriptionRepository.findById(id).get();
	}
	
	
}
