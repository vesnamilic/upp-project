package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.RegisteredUser;

@Service
public class SendingNotificationToAuthorFinalCorrection implements JavaDelegate {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserCustomService userCustomService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String username = (String) execution.getVariable("currentUser");
		
		RegisteredUser user = this.userCustomService.findUser(username);
		
		if(user != null) {
			String text= "";
			
			if((Boolean) execution.getVariable("correctionApproval")) {
				text = "Cestitam! Rad je uspesno ispravljen i prihvacen";
			} else {
				text= "Rad je potrebno doraditi ponovo! Molimo Vas da obratite paznju na komentare recenzenata! ";
			}
			
			try {
				this.emailService.sendNotificaitionAsync(user.getEmail(),"Rezultati o radu", text );
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}

}
