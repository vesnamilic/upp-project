package upp.project.services.save;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.model.ScientificPaper;
import upp.project.services.ScientificPaperService;

@Service
public class SaveScientificPaperApproval implements JavaDelegate {

	@Autowired
	private ScientificPaperService scientificPaperService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Long scientificPaperId = (Long) execution.getVariable("scientificPaperId");
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);
		if (scientificPaperId == null)
			return;

		ScientificPaper paper = this.scientificPaperService.getOne(scientificPaperId);

		if (paper != null) {
			paper.setApproved((Boolean) map.get("correctionApproval"));
			this.scientificPaperService.save(paper);
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
