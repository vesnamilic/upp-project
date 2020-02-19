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

import upp.project.dto.OrderStatusInformationDTO;
import upp.project.model.OrderStatus;
import upp.project.model.RegisteredUser;
import upp.project.model.UserSubscription;
import upp.project.repositories.UserSubscriptionRepository;

@Service
public class UserSubscriptionService {
	
	@Autowired
	private UserSubscriptionRepository userSubscriptionRepository;
	
	@Autowired
	private RestTemplate restTemplate;

	@Value("https://localhost:8762/api/client/subscription/status")
	private String kpUrl;
	
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
		return this.userSubscriptionRepository.getOne(id);
	}
	
	public List<UserSubscription> getAllUserSubscriptions(RegisteredUser user){
		return this.userSubscriptionRepository.findByBuyer(user);
	}
	
	public List<UserSubscription> getSubscriptionForSync() {
		List<OrderStatus> statusList = new ArrayList<OrderStatus>();
		statusList.add(OrderStatus.CREATED);
		statusList.add(OrderStatus.COMPLETED);
		
		return userSubscriptionRepository.findBySubscriptionStatusIn(statusList);
	}
	
	@Scheduled(initialDelay = 10000, fixedRate = 180000)
	public void checkSubscriptionStatus() {

		//find subscriptions
		List<UserSubscription> subscriptions = getSubscriptionForSync();
		
		for (UserSubscription userSubscription : subscriptions) {
			ResponseEntity<OrderStatusInformationDTO> response = null;
			
			try {
				response = restTemplate.getForEntity(this.kpUrl + "?subscriptionId=" + userSubscription.getId() + "&email=" + userSubscription.getMagazine().getEmail(),OrderStatusInformationDTO.class);
			} 
			catch (RestClientException e) {

				return;
			}
			
			OrderStatus status = OrderStatus.valueOf(response.getBody().getStatus());
			
			if(status != null && !status.equals(userSubscription.getSubscriptionStatus())) {
				userSubscription.setSubscriptionStatus(status);
				save(userSubscription);
			}
		}
	}
	
	
}

