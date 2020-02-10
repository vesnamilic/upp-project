package upp.project.services;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificArea;

@Service
public class GettingReviewers implements JavaDelegate {
	
	@Autowired
	private MagazineService magazineService;
	
	@Autowired
	private ScientificAreaService scientificAreaService;
	
	@Autowired
	private UserCustomService userCustomService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String magazineIdString  = (String) execution.getVariable("magazineSelection");
		String scientificAreaIdString = (String) execution.getVariable("scientificAreas");
		Long magazineId = null, scientifiAreaId = null;
		try {
			magazineId = Long.parseLong(magazineIdString);
			scientifiAreaId = Long.parseLong(scientificAreaIdString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (magazineId == null || scientifiAreaId == null) {
			return;
		}
		
		ScientificArea scArea = this.scientificAreaService.findById(scientifiAreaId);
		Magazine magazine = this.magazineService.findById(magazineId);
		if(scArea == null || magazine == null)
			return;
		
		List<RegisteredUser> reviewers = this.userCustomService.findReviewersByScientificAreas(scArea, magazineId);
		
		execution.setVariable("reviewers", reviewers);
		execution.setVariable("reviewersListSize", reviewers.size());
		
	}
}
