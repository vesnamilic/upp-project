package upp.project.services;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificArea;

@Service
public class SettingScientificEditor implements JavaDelegate {

	@Autowired
	private ScientificAreaService scientificAreaService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserCustomService userCustomService;
	
	@Autowired
	private MagazineService magazineService;


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
		
		
		RegisteredUser editor = this.userCustomService.findScientificEditor(scArea, magazineId);
		
		
		if(editor == null) {
			execution.setVariable("scientificEditor", magazine.getMainEditor().getUsername());
		} else {
			execution.setVariable("scientificEditor", editor.getUsername());
			try {
				this.emailService.sendNotificaitionAsync(editor.getEmail(), "EDITORU POSTOJI RAD", "POGLEDAJ RAD");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return;
		
		
	}

	
}
