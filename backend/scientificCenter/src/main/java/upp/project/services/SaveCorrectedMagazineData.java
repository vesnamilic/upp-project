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

@Service
public class SaveCorrectedMagazineData implements JavaDelegate {
	
	@Autowired
	private MagazineService magazineService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		Magazine magazine = this.magazineService.findById((Long) execution.getVariable("magazine_id"));
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		Map<String,Object> map = mapListToDto(list);
		magazine.setISSSN((String)map.get("issn"));
		magazine.setApproved(false);
		magazine.setName((String) map.get("name"));
		magazine.setEmail((String)map.get("email"));
		magazine.setPrice((Integer)map.get("price"));
		magazine.setPaymentMethod((((String) map.get("paymentMethod")).equals("authors"))? PaymentMethods.AUTHORS : PaymentMethods.READERS);
		this.magazineService.save(magazine);

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