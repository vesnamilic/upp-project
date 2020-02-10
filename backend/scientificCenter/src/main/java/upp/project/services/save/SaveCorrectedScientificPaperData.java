package upp.project.services.save;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.ScientificPaper;
import upp.project.services.ScientificPaperService;

@Service
public class SaveCorrectedScientificPaperData implements JavaDelegate {

	@Autowired
	private ScientificPaperService scientificPaperService;
	
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Long scientificPaperId = (Long) execution.getVariable("scientificPaperId");
		
		if(scientificPaperId == null)
			return;
		
		ScientificPaper paper = this.scientificPaperService.getOne(scientificPaperId);
		
		if(paper != null) {
			paper.setPaperPath((String)execution.getVariable("pdfFileLocation"));
			this.scientificPaperService.save(paper);
		}
		
	}

}
