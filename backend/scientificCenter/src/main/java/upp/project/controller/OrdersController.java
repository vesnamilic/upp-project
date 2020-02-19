package upp.project.controller;

import java.security.Principal;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.RuntimeService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
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

import upp.project.dto.OrderInformationDTO;
import upp.project.dto.OrderResponseDTO;
import upp.project.dto.RedirectDTO;
import upp.project.dto.UserOrderDTO;
import upp.project.dto.UserPurchasedItemsDTO;
import upp.project.model.IssueOrder;
import upp.project.model.Magazine;
import upp.project.model.MagazineIssue;
import upp.project.model.MagazineOrder;
import upp.project.model.Membership;
import upp.project.model.OrderStatus;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificPaper;
import upp.project.model.ScientificPaperOrder;
import upp.project.model.UserOrder;
import upp.project.model.UserSubscription;
import upp.project.security.JwtProvider;
import upp.project.services.MagazineIssueService;
import upp.project.services.MagazineService;
import upp.project.services.MembershipService;
import upp.project.services.ScientificPaperService;
import upp.project.services.UserCustomService;
import upp.project.services.UserOrderService;
import upp.project.services.UserSubscriptionService;

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
	private MagazineIssueService issueServicce;
	
	@Autowired
	private ScientificPaperService scientificPaperServicce;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@Autowired
	private UserSubscriptionService subscriptionService;
	
	//@PreAuthorize("hasRole('ROLE_REG_USER')")
	@PostMapping("/create")
	public ResponseEntity<?> createOrder(Principal principal, @RequestBody UserOrderDTO orderDTO) {
		UserOrder userOrder = null;
		RegisteredUser user = this.userCustomService.findUser(principal.getName());
		
		if(user == null) {
			return ResponseEntity.status(400).body("Niko nije ulogovan");
		}
		
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
		} else if(orderDTO.getType().equals("issue")) {
			Set<MagazineIssue> issues = new HashSet<MagazineIssue>();
			Magazine magazine = null;
			for(Long id : orderDTO.getIds()) {
				MagazineIssue issue = this.issueServicce.getOne(id);
				if(issue == null)
					return ResponseEntity.status(400).body("Nepostojeci issue");
				
				if(magazine == null)
					magazine = issue.getMagazine();
				
				issues.add(issue);
			}
			userOrder = new IssueOrder(issues);
			userOrder.setEmail(magazine.getEmail());
			userOrder.setPaymentAmount(issues.size() * magazine.getIssuePrice());
			
			
		} else {
			Set<ScientificPaper> papers = new HashSet<ScientificPaper>();
			Magazine magazine = null;
			for(Long id : orderDTO.getIds()) {
				ScientificPaper paper = this.scientificPaperServicce.getOne(id);
				if(paper == null)
					return ResponseEntity.status(400).body("nepostojeci paper");
				
				if(magazine == null)
					magazine = paper.getMagazineIssue().getMagazine();
				
				papers.add(paper);
			} 
			
			userOrder = new ScientificPaperOrder(papers);
			userOrder.setEmail(magazine.getEmail());
			userOrder.setPaymentAmount(papers.size() * magazine.getPaperPrice());
		}
		
		userOrder.setBuyer(user);
		userOrder.setOrderStatus(OrderStatus.INITIATED);
		userOrder.setPaymentCurrency("USD");

		userOrder = this.userOrderService.save(userOrder);
		if (userOrder == null) {
			return ResponseEntity.status(400).body("greska prilikom cuvanja");
		}

		OrderInformationDTO orderInformationDTO = new OrderInformationDTO();
		orderInformationDTO.setPaymentCurrency("USD");
		orderInformationDTO.setSuccessUrl("https://localhost:9991/orders/success"+ "?id=" + userOrder.getId() + "&type=" + orderDTO.getType());
		orderInformationDTO.setFailedUrl("https://localhost:9991/orders/failed"+ "?id=" + userOrder.getId() + "&type=" + orderDTO.getType());
		orderInformationDTO.setErrorUrl("https://localhost:9991/orders/error"+ "?id=" + userOrder.getId() + "&type=" + orderDTO.getType());
		orderInformationDTO.setOrderId(userOrder.getId());
		orderInformationDTO.setEmail(userOrder.getEmail());
		orderInformationDTO.setPaymentAmount(userOrder.getPaymentAmount());

		HttpEntity<OrderInformationDTO> request = new HttpEntity<>(orderInformationDTO);

		ResponseEntity<OrderResponseDTO> response = null;
		try {
			response = restTemplate.exchange("https://localhost:8762/api/client/orders/create", HttpMethod.POST, request, OrderResponseDTO.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			userOrder.setOrderStatus(OrderStatus.INVALID);
			userOrder = this.userOrderService.save(userOrder);
			return ResponseEntity.status(400).body("Greska prilikom kontaktiranja kpa");
		}
		userOrder.setOrderStatus(OrderStatus.CREATED);
		userOrder = this.userOrderService.save(userOrder);
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl(response.getBody().getRedirectUrl());
		return ResponseEntity.ok(redirectDTO);
	}
	
	@GetMapping("/successMembership")
	public ResponseEntity<?> successfulMembership(@RequestParam("id") Long id, @RequestParam("processId") String processId ) {
		UserOrder userOrder = this.userOrderService.getMagazineOrder(id);
		RegisteredUser user = userOrder.getBuyer();
		String magazineEmail = userOrder.getEmail();
		
		Magazine magazine = this.magazineServicce.findByEmail(magazineEmail);

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
		
		userOrder.setOrderStatus(OrderStatus.COMPLETED);
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
	public ResponseEntity<?> successfulOrder(@RequestParam("id") Long id, @RequestParam("type") String type, @RequestParam("processId") Optional<String> processId ) {
		
		if(processId.isPresent()) {
			this.runtimeService.setVariable(processId.get(), "paymentSuccessful" , true);
		}
		
		UserOrder userOrder = null;
		if(type.equals("magazine")) {
			userOrder = this.userOrderService.getMagazineOrder(id);
		} else if(type.equals("issue")) {
			userOrder = this.userOrderService.getIssueOrder(id);
		} else {
			userOrder = this.userOrderService.getScientificPaperOrder(id);
		}
		
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4205/success");
		
		if(userOrder == null) {
			redirectDTO.setUrl("https://localhost:4205/error");
			return ResponseEntity.ok(redirectDTO);
		}
		
		userOrder.setOrderStatus(OrderStatus.CANCELED);
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
			userOrder = this.userOrderService.getIssueOrder(id);
		} else {
			userOrder = this.userOrderService.getScientificPaperOrder(id);
		}
		
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4205/cancel");
		
		if(userOrder == null) {
			redirectDTO.setUrl("https://localhost:4205/error");
			return ResponseEntity.ok(redirectDTO);
		}
		
		userOrder.setOrderStatus(OrderStatus.CANCELED);
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
			userOrder = this.userOrderService.getIssueOrder(id);
		} else {
			userOrder = this.userOrderService.getScientificPaperOrder(id);
		}
		RedirectDTO redirectDTO = new RedirectDTO();
		redirectDTO.setUrl("https://localhost:4205/error");
		
		if(userOrder == null) {
			return ResponseEntity.ok(redirectDTO);
		}
		
		userOrder.setOrderStatus(OrderStatus.INVALID);
		this.userOrderService.save(userOrder);
		
		return ResponseEntity.ok(redirectDTO);
		
	}
	
	@GetMapping("/user")
	public ResponseEntity<?> getUserOrders(HttpServletRequest request) {
		String token = this.jwtProvider.getToken(request);
		String username = this.jwtProvider.getUsername(token);
		if(username == null) {
			System.out.println("username");
			return ResponseEntity.status(401).build();
		}
		RegisteredUser user = this.userCustomService.findUser(username);
		
		if(user == null) {
			System.out.println("USER");
			return ResponseEntity.status(401).build();
		}
		
		List<Magazine> magazines = this.userOrderService.getAllPurchasedMagazines(user);
		List<MagazineIssue> issues = this.userOrderService.getAllPurchasedIssues(user);
		List<ScientificPaper> papers = this.userOrderService.getAllPurchasedPapers(user);
		List<UserSubscription> subscriptions = this.subscriptionService.getAllUserSubscriptions(user);
		
		UserPurchasedItemsDTO dto = new UserPurchasedItemsDTO(magazines, issues, papers, subscriptions);
		return ResponseEntity.ok(dto);
	}
}
