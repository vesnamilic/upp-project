package upp.project.services.save;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.model.Magazine;
import upp.project.model.MagazineIssue;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificArea;
import upp.project.model.ScientificPaper;
import upp.project.services.MagazineIssueService;
import upp.project.services.MagazineService;
import upp.project.services.ScientificAreaService;
import upp.project.services.ScientificPaperService;
import upp.project.services.UserCustomService;

@Service
public class SaveScientificPaperData implements JavaDelegate {
	
	@Autowired
	private ScientificAreaService scientificAreaService;
	
	@Autowired
	private ScientificPaperService scientificPaperService;
	
	@Autowired
	private MagazineIssueService magazineIssueService;
	
	@Autowired
	private MagazineService magazineService;
	
	@Autowired
	private UserCustomService userService;
	

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
	
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		Map<String,Object> map = mapListToDto(list);
		String magazineId  = (String) execution.getVariable("magazineSelection");
		String username = (String) execution.getVariable("currentUser");
		
		RegisteredUser user = this.userService.findUser(username);
		
		if(user == null)
			return;
		
		
		
		Magazine selectedMagazine = null;
		try {
			selectedMagazine = this.magazineService.findByIdActivated(Long.parseLong(magazineId));
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			return;
		}
		Long id = (Long) execution.getVariable("scientificPaperId");
		
		ScientificPaper scientificPaper;
		if(id == null) {
			scientificPaper = new ScientificPaper();
			MagazineIssue issue = this.magazineIssueService.findByMagazineUnpublished(selectedMagazine);
			scientificPaper.setMagazineIssue(issue);
		} else {
			// TODO: dodati paranoicnu proveru
			scientificPaper = this.scientificPaperService.getOne(id); 
		}
		
		System.out.println("PATHHHHHHHHHH" + (String)execution.getVariable("pdf"));
		scientificPaper.setAuthor(user);
		scientificPaper.setKeywords((String) map.get("keywords"));
		scientificPaper.setPaperAbstract((String) map.get("abstract"));
		scientificPaper.setTitle((String) map.get("title"));
		scientificPaper.setPaperPath((String)execution.getVariable("pdf"));
		scientificPaper.setApproved(false);
		String scientificAreaId = (String) execution.getVariable("scientificAreas");
		ScientificArea scientificArea = this.scientificAreaService.findById(Long.parseLong(scientificAreaId));
		if ( scientificArea != null) {
			scientificPaper.setScientificArea(scientificArea);
		}
		
		ScientificPaper saved = this.scientificPaperService.save(scientificPaper);
		
		if(saved != null) {
			execution.setVariable("scientificPaperId", saved.getId());
		}
	}
	
	private HashMap<String, Object> mapListToDto(List<FormSubmitDTO> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmitDTO temp : list){
				map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}

}
