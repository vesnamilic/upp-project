package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;

@Service
public class SavingMagazineAsActivated implements JavaDelegate {
	
	@Autowired
	private MagazineService magazineService;
	
	@Autowired
	private MagazineIssueService issueService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		Magazine magazine = this.magazineService.findById((Long)execution.getVariable("magazine_id"));
		magazine.setApproved(true);
		this.magazineService.save(magazine);
		this.issueService.createInitialIssue(magazine,1);

	}

}
