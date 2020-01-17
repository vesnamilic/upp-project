package upp.project.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.model.Authority;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificArea;
import upp.project.model.UserRole;
import upp.project.services.AuthorityService;
import upp.project.services.MagazineService;
import upp.project.services.ScientificAreaService;
import upp.project.services.UserCustomService;

@Service
public class CreateEditorReviewersListener implements TaskListener {

	@Autowired
	private UserCustomService userCustomService;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private ScientificAreaService scientificAreaService;
	
	@Autowired
	private MagazineService magazineService;

	@SuppressWarnings("unchecked")
	public void notify(DelegateTask delegateTask) {
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) delegateTask.getVariable("data");
		Map<String, Object> map = mapListToDto(list);
		TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId());
		List<FormField> formFields = taskFormData.getFormFields();
		Authority reviewerAuthority = this.authorityService.findByName(UserRole.ROLE_REVIEWER);
		Authority editorAuthority = this.authorityService.findByName(UserRole.ROLE_EDITOR);
		List<Map<String, String>> scientificAreas = (List<Map<String, String>>) map.get("scientificAreas");
		List<ScientificArea> areas = new ArrayList<ScientificArea>();
		for (Map<String, String> area : scientificAreas) {
			ScientificArea sc = this.scientificAreaService.findById(Long.parseLong(area.get("item_id")));
			if (sc != null) {
				areas.add(sc);
			}
		}
		List<RegisteredUser> reviewers = this.userCustomService.findByScientificAreasAndRole(areas, reviewerAuthority);
		List<RegisteredUser> editors = this.userCustomService.findByScientificAreasAndRole(areas, editorAuthority);

		if (formFields != null) {

			for (FormField field : formFields) {
				System.out.println(field.getId());
				if (field.getId().equals("reviewers")) {
					// ovo je nase select polje
					HashMap<String, String> items = (HashMap<String, String>) field.getType().getInformation("values");
					items.clear();
					for (RegisteredUser user : reviewers) {
						items.put(user.getUsername(), user.getUsername());
					}
				} else if (field.getId().equals("editors")) {
					// ovo je nase select polje
					HashMap<String, String> items = (HashMap<String, String>) field.getType().getInformation("values");
					items.clear();
					for (RegisteredUser user : editors) {
						if(magazineService.findByEditor(user).isEmpty())
							items.put(user.getUsername(), user.getUsername());
					}
				}
			}
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
