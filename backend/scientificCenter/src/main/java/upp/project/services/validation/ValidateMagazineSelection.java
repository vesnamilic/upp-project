package upp.project.services.validation;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.services.MagazineService;

@Service
public class ValidateMagazineSelection implements JavaDelegate {

	@Autowired
	private MagazineService magazineService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String id  = (String) execution.getVariable("magazineSelection");
		Magazine selectedMagazine = null;
		try {
			selectedMagazine = this.magazineService.findByIdActivated(Long.parseLong(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			execution.setVariable("validationSuccessful", false);
		}
		
		if(selectedMagazine != null) {
			execution.setVariable("validationSuccessful", true);
			return;
		}
		
		execution.setVariable("validationSuccessful", false);
		return;
	}

}
