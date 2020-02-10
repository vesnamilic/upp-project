package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Recommendation;
import upp.project.model.RegisteredUser;

@Service
public class SendingNotificationToAuthor implements JavaDelegate {

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserCustomService userCustomService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		
		String username = (String) execution.getVariable("currentUser");
		
		RegisteredUser user = this.userCustomService.findUser(username);
		
		if(user != null) {
			
			String finalApproval = (String) execution.getVariable("finalApproval");
			String text= "";
			
			Recommendation finalApprovalEnum = Recommendation.valueOf(finalApproval);
			
			if(finalApprovalEnum == Recommendation.ACCEPT) {
				text="Rad je prihvacen cestitam!";
			} else if(finalApprovalEnum == Recommendation.REFUSE) {
				text="Rad je odbijen.";
			} else {
				text="Rad je potrebno izmeniti. Pogledajte komentare recenzenata i u sladu sa njima izmenite rad!";
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
