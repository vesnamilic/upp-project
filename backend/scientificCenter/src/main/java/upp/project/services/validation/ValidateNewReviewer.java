package upp.project.services.validation;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.model.RegisteredUser;
import upp.project.services.UserCustomService;

@Service
public class ValidateNewReviewer implements JavaDelegate {

	@Autowired
	private UserCustomService userCustomService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {

		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);
		Long reviewer = (Long) map.get("newReviewer");

		RegisteredUser registeredUser = this.userCustomService.getOne(reviewer);
		if (registeredUser == null) {
			execution.setVariable("validationSuccessful", false);
			return;
		}

		execution.setVariable("validationSuccessful", true);
	}

	private HashMap<String, Object> mapListToDto(List<FormSubmitDTO> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmitDTO temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}

		return map;
	}

}
