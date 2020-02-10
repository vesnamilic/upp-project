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
import upp.project.model.RegisteredUser;

@Service
public class SavingReviewersMagazineEditors implements JavaDelegate {

	@Autowired
	private UserCustomService userCustomService;

	@Autowired
	private MagazineService magazineService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("OVDEEEEEEEEEEEEEEE");
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);
		List<Map<String, String>> reviewers = (List<Map<String, String>>) map.get("reviewers");
		List<Map<String, String>> editors = (List<Map<String, String>>) map.get("editors");
		System.out.println((Long)execution.getVariable("magazine_id"));
		Magazine magazine = this.magazineService.findById((Long)execution.getVariable("magazine_id"));
		if (magazine != null) {
			for (Map<String, String> editor : reviewers) {
				RegisteredUser user = this.userCustomService.findUser(editor.get("item_id"));
				if (user == null) {
					break;
				} else {
					magazine.getReviewers().add(user);
				}
			}
			for (Map<String, String> editor : editors) {
				RegisteredUser user = this.userCustomService.findUser(editor.get("item_id"));
				if (user == null) {
					break;
				} else {
					magazine.getEditors().add(user);
				}
			}
			Magazine saved = this.magazineService.save(magazine);
			System.out.println(saved);
		}

	}

	private HashMap<String, Object> mapListToDto(List<FormSubmitDTO> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmitDTO temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}

		return map;
	}

}
