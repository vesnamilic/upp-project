package upp.project.services.validation;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.services.ScientificAreaService;

@Service
public class ValidateScientificPaperData implements JavaDelegate {

	@Autowired
	private ScientificAreaService scientificAreaService;
	
	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);

		String scientificAreaId = (String) map.get("scientificAreas");

		if (this.scientificAreaService.findById(Long.parseLong(scientificAreaId)) == null) {
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
