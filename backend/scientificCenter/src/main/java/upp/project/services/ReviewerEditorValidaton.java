package upp.project.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.model.RegisteredUser;

@Service
public class ReviewerEditorValidaton implements JavaDelegate {

	@Autowired
	private UserCustomService userCustomService;

	@Autowired
	private MagazineService magazineService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("VALIDACIJA UMRLA");
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);
		List<Map<String, String>> reviewers = (List<Map<String, String>>) map.get("reviewers");
		List<Map<String, String>> editors = (List<Map<String, String>>) map.get("editors");
		execution.setVariable("validationSuccessful ", false);
		if (reviewers == null || reviewers.size() < 2) {
			execution.setVariable("validationSuccessful ", false);
			return;
		}

		for (Map<String, String> reviewer : reviewers) {
			for (Map.Entry<String, String> entry : reviewer.entrySet()) {
				RegisteredUser user = this.userCustomService.findUser(entry.getKey());
				if (user == null) {
					execution.setVariable("validationSuccessful ", false);
					return;
				}

			}
		}

		for (Map<String, String> editor : editors) {
			for (Map.Entry<String, String> entry : editor.entrySet()) {
				RegisteredUser user = this.userCustomService.findUser(entry.getKey());
				if (user == null) {
					execution.setVariable("validationSuccessful ", false);
					return;
				} else if (!this.magazineService.findByEditor(user).isEmpty()) {
					execution.setVariable("validationSuccessful ", false);
					return;
				}

			}
		}

		execution.setVariable("validationSuccessful ", true);
	}

	private HashMap<String, Object> mapListToDto(List<FormSubmitDTO> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmitDTO temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}

		return map;
	}

}
