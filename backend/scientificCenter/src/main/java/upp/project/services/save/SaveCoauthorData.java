package upp.project.services.save;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.model.Coauthor;
import upp.project.model.ScientificPaper;
import upp.project.services.CoauthorService;
import upp.project.services.ScientificPaperService;

@Service
public class SaveCoauthorData implements JavaDelegate {

	@Autowired
	private ScientificPaperService scientificPaperService;

	@Autowired
	private CoauthorService coauthorService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);
		Coauthor coauthor = new Coauthor();
		coauthor.setFirstName((String) map.get("firstName"));
		coauthor.setLastName((String) map.get("lastName"));
		coauthor.setCity((String) map.get("city"));
		coauthor.setCountry((String) map.get("country"));
		coauthor.setEmail((String) map.get("email"));

		Long scientificPaperId = (Long) execution.getVariable("scientificPaperId");
		coauthor = this.coauthorService.save(coauthor);
		ScientificPaper paper = this.scientificPaperService.getOne(scientificPaperId);

		if (coauthor != null && paper != null) {
			Set<Coauthor> coauthors = paper.getCoauthors();
			if (coauthors == null) {
				coauthors = new HashSet<Coauthor>();
			}

			coauthors.add(coauthor);
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
