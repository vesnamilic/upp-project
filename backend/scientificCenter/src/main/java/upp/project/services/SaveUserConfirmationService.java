package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.RegisteredUser;

@Service
public class SaveUserConfirmationService implements JavaDelegate {
	
	@Autowired
	private UserCustomService userCustomService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String username = (String)execution.getVariable("username");
		Boolean confirm = (Boolean) execution.getVariable("confirm");
		
		RegisteredUser user = this.userCustomService.findUser(username);
		if(confirm) {
			userCustomService.enableUser(user);
		}
		
		

	}
}
