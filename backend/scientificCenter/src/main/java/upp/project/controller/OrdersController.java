package upp.project.controller;

import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.Optional;

import org.camunda.bpm.engine.RuntimeService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import upp.project.dto.OrderDTO;
import upp.project.dto.OrderInformationDTO;
import upp.project.dto.OrderResponseDTO;
import upp.project.dto.RedirectDTO;
import upp.project.model.Magazine;
import upp.project.model.Membership;
import upp.project.model.OrderStatus;
import upp.project.model.RegisteredUser;
import upp.project.model.UserOrder;
import upp.project.services.MagazineService;
import upp.project.services.MembershipService;
import upp.project.services.UserCustomService;
import upp.project.services.UserOrderService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*", maxAge = 3600)
@RequestMapping(value = "/orders", produces = MediaType.APPLICATION_JSON_VALUE)
public class OrdersController {

	@Autowired
	private UserOrderService userOrderService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private MembershipService membershipService;
	
	@Autowired
	private UserCustomService userCustomService;
	
	@Autowired
	private MagazineService magazineServicce;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@PreAuthorize("hasRole('ROLE_REG_USER')")
	public ResponseEntity<?> createOrder(Principal principal, @RequestBody OrderDTO orderDTO) {
		UserOrder userOrder = new UserOrder();
		Magazine magazine = this.magazineServicce.findById(orderDTO.getMagazineId());
		if(magazine == null) {
			return ResponseEntity.badRequest().build();
		}
		
		RegisteredUser user = this.userCustomService.findUser(principal.getName());
		
		if(user == null) {
			return ResponseEntity.badRequest().build();
		}
		userOrder.setBuyer(user);
		userOrder.setMagazine(magazine);
		userOrder.setOrderStatus(OrderStatus.CREATED);
		userOrder.setPaymentCurrency("USD");

		userOrder = this.userOrderService.save(userOrder);
		if (userOrder == null) {
			return ResponseEntity.badRequest().build();
		}

		OrderInformationDTO orderInformationDTO = new OrderInformationDTO();
		orderInformationDTO.setPaymentCurrency("USD");
		orderInformationDTO.setPaymentAmount(magazine.getPrice().doubleValue());
		orderInformationDTO.setEmail(magazine.getEmail());
		orderInformationDTO.setSuccessUrl("https://localhost:8080/orders/successMembership"+ "?id=" + userOrder.getId());
		orderInformationDTO.setFailedUrl("https://localhost:8080/orders/failed"+ "?id=" + userOrder.getId());
		orderInformationDTO.setErrorUrl("https://localhost:8080/orders/error"+ "?id=" + userOrder.getId());
		orderInformationDTO.setOrderId(userOrder.getId());

		HttpEntity<OrderInformationDTO> request = new HttpEntity<>(orderInformationDTO);

		ResponseEntity<OrderResponseDTO> response = null;
		try {
			response = restTemplate.exchange("https://localhost:8762/api/client/orders/create", HttpMethod.POST, request, OrderResponseDTO.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			userOrder.setOrderStatus(OrderStatus.ERROR);
			return ResponseEntity.badRequest().build();
		}
		
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl(response.getBody().getRedirectUrl());
		return ResponseEntity.ok(redirectDTO);
	}
	
	@GetMapping("/successMembership")
	public ResponseEntity<?> successfulMembership(@RequestParam("id") Long id, @RequestParam("processId") String processId ) {
		UserOrder userOrder = this.userOrderService.getUserOrder(id);
		RegisteredUser user = userOrder.getBuyer();
		Magazine magazine = userOrder.getMagazine();
		Membership membership = this.membershipService.findMembership(magazine.getId(), user.getUsername());
		if(membership == null)
		{
			membership = new Membership();
			membership.setMagazine(magazine);
			
			if(user.getMemberships() == null) {
				user.setMemberships(new HashSet<Membership>());
			}
			user.getMemberships().add(membership);
			
		}
		Date dt = new Date();
		DateTime dtOrg = new DateTime(dt);
		DateTime dtPlusOne = dtOrg.plusMonths(1);
		membership.setPayedUntil(dtPlusOne.toDate());
		membership = this.membershipService.saveMembership(membership);
		if(membership == null) {
			return ResponseEntity.badRequest().build();
		}
		
		user = this.userCustomService.save(user);
		if(user == null) {
			return ResponseEntity.badRequest().build();
		}
		
		userOrder.setOrderStatus(OrderStatus.SUCCEEDED);
		userOrder = this.userOrderService.save(userOrder);
		if(userOrder == null) {
			System.out.println("orders FAILD");
			return ResponseEntity.badRequest().build();
		}
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4205/success");
		this.runtimeService.setVariable(processId, "paymentSuccessful" , true);
		return ResponseEntity.ok(redirectDTO);
		
	}
	
	@GetMapping("/success")
	public ResponseEntity<?> successfulOrder(@RequestParam("id") Long id, @RequestParam("processId") Optional<String> processId ) {
		
		if(processId.isPresent()) {
			this.runtimeService.setVariable(processId.get(), "paymentSuccessful" , true);
		}
		
		UserOrder userOrder = this.userOrderService.getUserOrder(id);
		userOrder.setOrderStatus(OrderStatus.SUCCEEDED);
		this.userOrderService.save(userOrder);
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4205/success");
		return ResponseEntity.ok(redirectDTO);
		
	}
	
	@GetMapping("/failed")
	public ResponseEntity<?> failedOrder(@RequestParam("id") Long id, @RequestParam("processId") Optional<String> processId) {
		
		if(processId.isPresent()) {
			this.runtimeService.setVariable(processId.get(), "paymentSuccessful" , false);
		}
		
		UserOrder userOrder = this.userOrderService.getUserOrder(id);
		userOrder.setOrderStatus(OrderStatus.FAILED);
		this.userOrderService.save(userOrder);
		
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4205/cancel");
		return ResponseEntity.ok(redirectDTO);
		
	}
	
	@GetMapping("/error")
	public ResponseEntity<?> errorOrder(@RequestParam("id") Long id, @RequestParam("processId") Optional<String> processId) {
		
		if(processId.isPresent()) {
			this.runtimeService.setVariable(processId.get(), "paymentSuccessful" , false);
		}
		
		
		UserOrder userOrder = this.userOrderService.getUserOrder(id);
		userOrder.setOrderStatus(OrderStatus.ERROR);
		this.userOrderService.save(userOrder);
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4205/error");
		return ResponseEntity.ok(redirectDTO);
		
	}
}
