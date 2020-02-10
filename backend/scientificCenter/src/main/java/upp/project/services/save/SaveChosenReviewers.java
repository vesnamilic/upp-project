package upp.project.services.save;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.model.RegisteredUser;
import upp.project.services.UserCustomService;

@Service
public class SaveChosenReviewers implements JavaDelegate {

	@Autowired
	private UserCustomService userCustomService;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);
		
		List<Map<String, String>> reviewers = (List<Map<String, String>>) map.get("chosenReviewers");
		
		List<String> chosenReviewers = new ArrayList<String>();
		
		for (Map<String, String> reviewer : reviewers) {
			RegisteredUser registeredUser = this.userCustomService.getOne(Long.parseLong(reviewer.get("item_id")));
			if (registeredUser == null) {
				break;
			} else {
				chosenReviewers.add(registeredUser.getUsername());
			}
		}
		
		execution.setVariable("chosenReviewers", chosenReviewers);
	}

	private HashMap<String, Object> mapListToDto(List<FormSubmitDTO> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmitDTO temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}

		return map;
	}
}
