package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.RegisteredUser;

@Service
public class SendEmailNotificationScientificPaper implements JavaDelegate {

	@Autowired
	private EmailService emailService;	
	
	@Autowired
	private MagazineService magazineService;
	
	@Autowired
	private UserCustomService userCustomService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
				
		String magazineId = (String) execution.getVariable("magazineSelection");
		
		Magazine selectedMagazine = null;
		try {
			selectedMagazine = this.magazineService.findByIdActivated(Long.parseLong(magazineId));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw e;
		}
		
		// Slanje email-a glavnom uredniku
		if (selectedMagazine != null) {
			emailService.sendNotificaitionAsync(selectedMagazine.getMainEditor().getEmail(), "Scientific Paper", "RAD JE KREIRAN POGLEDAJ GA");
			
		}
		
		String username = (String) execution.getVariable("currentUser");
		
		RegisteredUser user = this.userCustomService.findUser(username);
		
		if(user != null) {
			emailService.sendNotificaitionAsync(user.getEmail(), "Scientific Paper", "RAD JE KREIRAN POGLEDAJ GA");
		}
		
		
	}
}
