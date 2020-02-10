package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.RegisteredUser;

@Service
public class SendNotificationToChooseNewReviewer implements JavaDelegate {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserCustomService userCustomService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String username = (String) execution.getVariable("scientificEditor");
		RegisteredUser user = this.userCustomService.findUser(username);
		
		if(user == null ) {
			return;
		}
		
		try {
			emailService.sendNotificaitionAsync(user.getEmail(), "Odabir novog recenzenta", "Vreme je isteklo recenzentu. Odaberi drugog!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
