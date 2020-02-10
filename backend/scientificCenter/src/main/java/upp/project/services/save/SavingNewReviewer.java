package upp.project.services.save;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.RegisteredUser;
import upp.project.services.UserCustomService;

@Service
public class SavingNewReviewer implements JavaDelegate {

	@Autowired
	private UserCustomService userCustomService;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String oldReviewerUsername = (String) execution.getVariable("oneReviewer");
		List<String> chosenReviewers = (List<String>) execution.getVariable("chosenReviewers");
		String reviewerId = (String) execution.getVariable("newReviewer");
		
		Long id = null;
		
		try {
			id = Long.parseLong(reviewerId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		RegisteredUser user = this.userCustomService.getOne(id);
		
		if(user != null)
			chosenReviewers.remove(oldReviewerUsername);
			chosenReviewers.add(user.getUsername());
			execution.setVariable("chosenReviewers", chosenReviewers);
			execution.setVariable("oneReviewer", user.getUsername());
		
		
	}

}
