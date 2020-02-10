package upp.project.handlers;

import java.util.Date;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.Membership;
import upp.project.model.PaymentMethods;
import upp.project.services.MagazineService;
import upp.project.services.MembershipService;

@Service
public class CompleteMagazineSelectionListener implements TaskListener {
	
	@Autowired
	private MagazineService magazineService;
	
	@Autowired
	private MembershipService membershipService;

	public void notify(DelegateTask delegateTask) {
		String id  = (String) delegateTask.getExecution().getVariable("magazineSelection");
		String username  = (String) delegateTask.getExecution().getVariable("currentUser");
		Magazine selectedMagazine = null;
		try {
			selectedMagazine = this.magazineService.findByIdActivated(Long.parseLong(id));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return;
		}
		
		if(selectedMagazine != null) {
			delegateTask.getExecution().setVariable("openAccess", selectedMagazine.getPaymentMethod() == PaymentMethods.AUTHORS);
			if(selectedMagazine.getPaymentMethod() == PaymentMethods.AUTHORS) {
				Membership membership = this.membershipService.findMembership(selectedMagazine.getId(), username);
				if(membership != null && membership.getPayedUntil().after(new Date()) )
				{
					delegateTask.getExecution().setVariable("membershipFee", true);
				}else {
					delegateTask.getExecution().setVariable("membershipFee", false);
				}
			}
			
		
			delegateTask.getExecution().setVariable("mainEditor", selectedMagazine.getMainEditor().getUsername());
		
		}
		
		
	}
}
