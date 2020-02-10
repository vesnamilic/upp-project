package upp.project.dto;

import java.util.List;

import org.camunda.bpm.engine.form.FormField;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormFieldsDTO {

	private String taskId;
	private String taskName;
	private List<FormField> formFields;
	private String processInstanceId;
}
