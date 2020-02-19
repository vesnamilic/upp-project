package upp.project.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.model.Magazine;
import upp.project.model.PaymentMethods;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificArea;

@Service
public class SavingMagazineData implements JavaDelegate {
	
	@Autowired
	private ScientificAreaService scientificAreaService;
	
	@Autowired
	private UserCustomService userCustomService;
	
	@Autowired
	private MagazineService magazineService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		Map<String,Object> map = mapListToDto(list);
		
		Magazine magazine = new Magazine();
		magazine.setISSSN((String)map.get("issn"));
		magazine.setApproved(false);
		magazine.setName((String) map.get("name"));
		magazine.setEmail((String)map.get("email"));
		magazine.setPaymentMethod((((String) map.get("paymentMethod")).equals("authors"))? PaymentMethods.AUTHORS : PaymentMethods.READERS);
		List<Map<String, String>> scientificAreas = (List<Map<String, String>>) map.get("scientificAreas");
		for (Map<String, String> areaMap : scientificAreas) {
			ScientificArea area = this.scientificAreaService.findById(Long.parseLong(areaMap.get("item_id")));
			if (area == null) {
				break;
			} else {
				magazine.getScientificAreas().add(area);
			}
		}
		
		RegisteredUser user = this.userCustomService.findUser((String)execution.getVariable("currentUser"));
		
		if(user != null) {
			magazine.setMainEditor(user);
			Magazine saved = this.magazineService.save(magazine);
			if(saved != null)
				execution.setVariable("magazine_id", saved.getId());
		}
		
		return;

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
