package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.RegisteredUser;

@Service
public class SendEmailNotificationPaperCorrection implements JavaDelegate {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserCustomService userCustomService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String username = (String) execution.getVariable("currentUser");
		
		RegisteredUser user = this.userCustomService.findUser(username);
		
		if(user != null) {
			try {
				this.emailService.sendNotificaitionAsync(user.getEmail(),"ISPRAVI RAD", "Molim te ispravi rad");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
}
