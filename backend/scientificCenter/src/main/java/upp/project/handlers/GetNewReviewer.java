package upp.project.handlers;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Magazine;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificArea;
import upp.project.services.MagazineService;
import upp.project.services.ScientificAreaService;
import upp.project.services.UserCustomService;

@Service
public class GetNewReviewer implements TaskListener {

	@Autowired
	private MagazineService magazineService;

	@Autowired
	private ScientificAreaService scientificAreaService;

	@Autowired
	private UserCustomService userCustomService;

	@SuppressWarnings("unchecked")
	public void notify(DelegateTask delegateTask) {
		TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId());
		String magazineIdString = (String) delegateTask.getExecution().getVariable("magazineSelection");
		String scientificAreaIdString = (String) delegateTask.getExecution().getVariable("scientificAreas");
		Long magazineId = null, scientifiAreaId = null;
		try {
			magazineId = Long.parseLong(magazineIdString);
			scientifiAreaId = Long.parseLong(scientificAreaIdString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (magazineId == null || scientifiAreaId == null) {
			return;
		}

		ScientificArea scArea = this.scientificAreaService.findById(scientifiAreaId);
		Magazine magazine = this.magazineService.findById(magazineId);
		if (scArea == null || magazine == null)
			return;

		List<RegisteredUser> reviewers = this.userCustomService.findReviewersByScientificAreas(scArea, magazineId);
		List<String> chosenReviewers = (List<String>) delegateTask.getExecution().getVariable("chosenReviewers");
		List<FormField> formFields = taskFormData.getFormFields();
		if (formFields != null) {
			for (FormField field : formFields) {
				if (field.getId().equals("newReviewer")) {
					// ovo je nase select polje
					HashMap<String, String> items = (HashMap<String, String>) field.getType().getInformation("values");
					items.clear();
					for (RegisteredUser user: reviewers) {
						if(reviewers.size() == chosenReviewers.size() || (reviewers.size() > chosenReviewers.size() && !chosenReviewers.contains(user.getUsername())))
							items.put(user.getId().toString(), user.getFirstName() + " " + user.getLastName());
					}
				}
			}
		}
	}

}
