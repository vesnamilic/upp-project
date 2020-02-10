package upp.project.handlers;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.ScientificArea;
import upp.project.repositories.ScientificAreaRepository;

@Service
public class CreateRegistrationFormListener implements TaskListener {

	@Autowired
	private ScientificAreaRepository scientificAreasRepository;

	@SuppressWarnings("unchecked")
	public void notify(DelegateTask delegateTask) {
		TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().getFormService()
				.getTaskFormData(delegateTask.getId());
		List<ScientificArea> scientificAreas = this.scientificAreasRepository.findAll();
		List<FormField> formFields = taskFormData.getFormFields();
		if (formFields != null) {
			for (FormField field : formFields) {
				if (field.getId().equals("scientificAreas")) {
					// ovo je nase select polje
					HashMap<String, String> items = (HashMap<String, String>) field.getType().getInformation("values");
					items.clear();
					for (ScientificArea scientificArea : scientificAreas) {
						items.put(scientificArea.getId().toString(), scientificArea.getName());
					}
				}
			}
		}

	}
}
