package upp.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.UserOrder;
import upp.project.repositories.UserOrderRepository;

@Service
public class UserOrderService {

	@Autowired
	private UserOrderRepository userOrderRepository;
	
	public UserOrder save(UserOrder order) {
		UserOrder saved = null;
		
		try {
			saved = this.userOrderRepository.save(order);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return saved;
	}
	
	public UserOrder getUserOrder(Long id) {
		return this.userOrderRepository.getOne(id);
	}
}
