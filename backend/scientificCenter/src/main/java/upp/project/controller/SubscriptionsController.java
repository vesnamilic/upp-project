package upp.project.controller;

import java.security.Principal;

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

import upp.project.dto.OrderResponseDTO;
import upp.project.dto.RedirectDTO;
import upp.project.dto.SubscriptionInformationDTO;
import upp.project.model.Magazine;
import upp.project.model.OrderStatus;
import upp.project.model.RegisteredUser;
import upp.project.model.UserSubscription;
import upp.project.services.MagazineService;
import upp.project.services.UserCustomService;
import upp.project.services.UserSubscriptionService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/subscription", produces = MediaType.APPLICATION_JSON_VALUE)
public class SubscriptionsController {
	
	@Autowired
	private MagazineService magazineService;

	@Autowired
	private UserSubscriptionService userSubscriptionService;
	
	@Autowired
	private UserCustomService userService;
	
	@Autowired
	private RestTemplate restTemplate;

	@Value("https://localhost:9991/success")
	private String successUrl;

	@Value("https://localhost:9991/failed")
	private String failedUrl;

	@Value("https://localhost:9991/error")
	private String errorUrl;

	@Value("https://localhost:8762/api/client/subscription/create")
	private String kpUrl;

	@PostMapping("/create")
	public ResponseEntity<?> createSubscription(@RequestBody String magazineEmail, Principal principal) {
		
		String username="";
		if(principal!=null) {
			username=principal.getName();
		}
		else {
			System.out.println("NULL");
		}

		RegisteredUser buyer=userService.findUser(username);

		Magazine magazine = magazineService.findByEmail(magazineEmail);
		if (magazine == null) {
			return ResponseEntity.status(400).build();
		}

		UserSubscription userSubscription = new UserSubscription();
		userSubscription.setMagazine(magazine);
		userSubscription.setSubscriptionStatus(OrderStatus.INITIATED);
		userSubscription.setBuyer(buyer);

		userSubscription = userSubscriptionService.save(userSubscription);
		
		if (userSubscription == null) {
			return ResponseEntity.status(500).build();
		}
		SubscriptionInformationDTO subscriptionInformationDTO = new SubscriptionInformationDTO();
		subscriptionInformationDTO.setEmail(magazineEmail);
		subscriptionInformationDTO.setErrorUrl("https://localhost:9991/subscription/error"+ "?id=" + userSubscription.getId());
		subscriptionInformationDTO.setFailedUrl("https://localhost:9991/subscription/failed"+ "?id=" + userSubscription.getId());
		subscriptionInformationDTO.setSuccessUrl("https://localhost:9991/subscription/success"+ "?id=" + userSubscription.getId());
		subscriptionInformationDTO.setSubscriptionId(userSubscription.getId());
		subscriptionInformationDTO.setPaymentCurrency("USD");
		subscriptionInformationDTO.setPaymentAmount(magazine.getSubscriptionPrice().doubleValue());
		
		HttpEntity<SubscriptionInformationDTO> request = new HttpEntity<>(subscriptionInformationDTO);
		ResponseEntity<OrderResponseDTO> response = null;
		try {
			response = restTemplate.exchange(this.kpUrl, HttpMethod.POST, request, OrderResponseDTO.class);
		} catch (RestClientException e) {
			userSubscription.setSubscriptionStatus(OrderStatus.INVALID);
			return ResponseEntity.status(400)
					.body("An error occurred while trying to contact the payment microservice!");
		}
		userSubscription.setUuid(response.getBody().getUuid());
		userSubscription.setSubscriptionStatus(OrderStatus.CREATED);
		this.userSubscriptionService.save(userSubscription);

		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl(response.getBody().getRedirectUrl());
		return ResponseEntity.ok(redirectDTO);
	}
	
	@GetMapping("/success")
	private ResponseEntity<?> successfulOrder(@RequestParam("id") Long id) {
		UserSubscription userSubscription = userSubscriptionService.getUserSubscription(id);
		userSubscription.setSubscriptionStatus(OrderStatus.COMPLETED);
		userSubscriptionService.save(userSubscription);
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4205/success");
		return ResponseEntity.ok(redirectDTO);
	}

	@GetMapping("/failed")
	private ResponseEntity<?> failedOrder(@RequestParam("id") Long id) {
		UserSubscription userSubscription = userSubscriptionService.getUserSubscription(id);
		userSubscription.setSubscriptionStatus(OrderStatus.CANCELED);
		userSubscriptionService.save(userSubscription);
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4205/cancel");
		return ResponseEntity.ok(redirectDTO);
	}

	@GetMapping("/error")
	private ResponseEntity<?> errorOrder(@RequestParam("id") Long id) {
		UserSubscription userSubscription = userSubscriptionService.getUserSubscription(id);
		userSubscription.setSubscriptionStatus(OrderStatus.INVALID);
		userSubscriptionService.save(userSubscription);
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4205/error");
		return ResponseEntity.ok(redirectDTO);
	}

}
