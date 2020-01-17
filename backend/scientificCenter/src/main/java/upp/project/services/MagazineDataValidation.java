package upp.project.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;

@Service
public class MagazineDataValidation implements JavaDelegate {

	@Autowired
	private ScientificAreaService scientificAreaService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);
		List<Map<String, String>> scientificAreas = (List<Map<String, String>>) map.get("scientificAreas");

		if (scientificAreas.size() < 1) {
			execution.setVariable("validationSuccessful", false);
			return;
		}

		for (Map<String, String> area : scientificAreas) {
			if (this.scientificAreaService.findById(Long.parseLong(area.get("item_id"))) == null) {
				execution.setVariable("validationSuccessful", false);
				return;
			}
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
