package upp.project.handlers;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.stereotype.Service;

import upp.project.model.RegisteredUser;

@Service
public class GetReviewersListener implements TaskListener {

	@SuppressWarnings("unchecked")
	public void notify(DelegateTask delegateTask) {
		TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().getFormService().getTaskFormData(delegateTask.getId());
		List<RegisteredUser> reviewers = (List<RegisteredUser>) delegateTask.getExecution().getVariable("reviewers");
		List<FormField> formFields = taskFormData.getFormFields();
		if (formFields != null) {

			for (FormField field : formFields) {
				if (field.getId().equals("chosenReviewers")) {
					// ovo je nase select polje
					HashMap<String, String> items = (HashMap<String, String>) field.getType().getInformation("values");
					items.clear();
					for (RegisteredUser reviewer : reviewers) {
						items.put(reviewer.getId().toString(), reviewer.getFirstName() + " " + reviewer.getLastName());
					}
				}
			}
		}
	}

}
