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
import upp.project.services.MagazineService;

@Service
public class GetMagazinesListener implements TaskListener {
	
	@Autowired
	private MagazineService magazineService;

	@SuppressWarnings("unchecked")
	public void notify(DelegateTask delegateTask) {
		TaskFormData taskFormData = delegateTask.getExecution().getProcessEngineServices().getFormService().getTaskFormData(delegateTask.getId());
		List<Magazine> magazines = this.magazineService.findAllActivated();
		List<FormField> formFields = taskFormData.getFormFields();
		if (formFields != null) {

			for (FormField field : formFields) {
				if (field.getId().equals("magazineSelection")) {
					// ovo je nase select polje
					HashMap<String, String> items = (HashMap<String, String>) field.getType().getInformation("values");
					items.clear();
					for (Magazine magazine : magazines) {
						System.out.println(magazine.getId());
						items.put(magazine.getId().toString(), magazine.getName());
					}
				}
			}
		}
	}

}
