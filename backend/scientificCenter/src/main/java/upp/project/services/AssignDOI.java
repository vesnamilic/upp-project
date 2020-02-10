package upp.project.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.ScientificPaper;

@Service
public class AssignDOI implements JavaDelegate {
	
	@Autowired
	private ScientificPaperService scientificPaperService;

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		Long id = (Long) execution.getVariable("scientificPaperId");

		if (id == null) {
			return;
		} 

		ScientificPaper scientificPaper = this.scientificPaperService.getOne(id);
		
		if(scientificPaper!=null) {
			scientificPaper.setDoi("10." + RandomStringUtils.randomNumeric(4) + "/" + RandomStringUtils.randomNumeric(3));
			this.scientificPaperService.save(scientificPaper);
		}
	}

}
