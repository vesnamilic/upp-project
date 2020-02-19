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

import upp.project.dto.OrderInformationDTO;
import upp.project.dto.OrderResponseDTO;
import upp.project.model.Magazine;
import upp.project.model.MagazineOrder;
import upp.project.model.OrderStatus;
import upp.project.model.RegisteredUser;
import upp.project.model.UserOrder;

@Service
public class PaymentOfMemebership implements JavaDelegate {

	@Autowired
	private MagazineService magazineService;
	
	@Autowired
	private UserOrderService userOrderService;
	
	@Autowired
	private UserCustomService userCustomService;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String id  = (String) execution.getVariable("magazineSelection");
		String username  = (String) execution.getVariable("currentUser");
		Magazine selectedMagazine = null;
		try {
			selectedMagazine = this.magazineService.findByIdActivated(Long.parseLong(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RegisteredUser user = this.userCustomService.findUser(username);
		
		if(selectedMagazine == null || user == null) {
			return;
		}

		UserOrder userOrder = new MagazineOrder(selectedMagazine);
		userOrder.setBuyer(user);
		userOrder.setEmail(selectedMagazine.getEmail());
		userOrder.setOrderStatus(OrderStatus.INITIATED);
		userOrder.setPaymentAmount(selectedMagazine.getSubscriptionPrice());
		userOrder.setPaymentCurrency("USD");

		userOrder = this.userOrderService.save(userOrder);
		if (userOrder == null) {
			return;
		}

		OrderInformationDTO orderInformationDTO = new OrderInformationDTO();
		orderInformationDTO.setPaymentCurrency("USD");
		orderInformationDTO.setPaymentAmount(selectedMagazine.getSubscriptionPrice().doubleValue());
		orderInformationDTO.setEmail(selectedMagazine.getEmail());
		orderInformationDTO.setSuccessUrl("https://localhost:9991/orders/successMembership"+ "?id=" + userOrder.getId() + "&processId="+execution.getProcessInstanceId());
		orderInformationDTO.setFailedUrl("https://localhost:9991/orders/failed"+ "?id=" + userOrder.getId() + "&processId="+execution.getProcessInstanceId());
		orderInformationDTO.setErrorUrl("https://localhost:9991/orders/error"+ "?id=" + userOrder.getId() + "&processId="+execution.getProcessInstanceId());
		orderInformationDTO.setOrderId(userOrder.getId());

		HttpEntity<OrderInformationDTO> request = new HttpEntity<>(orderInformationDTO);

		ResponseEntity<OrderResponseDTO> response = null;
		try {
			response = restTemplate.exchange("https://localhost:8762/api/client/orders/create", HttpMethod.POST, request, OrderResponseDTO.class);
		} catch (RestClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			userOrder.setOrderStatus(OrderStatus.INVALID);
			return;
		}
		
		userOrder.setOrderStatus(OrderStatus.CREATED);
		userOrder = this.userOrderService.save(userOrder);
		if (userOrder == null) {
			return;
		}
		execution.setVariable("redirectUrl", response.getBody().getRedirectUrl());
		return ;
	}
	
}
