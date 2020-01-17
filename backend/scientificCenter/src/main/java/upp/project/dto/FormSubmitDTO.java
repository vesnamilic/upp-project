package upp.project.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class FormSubmitDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6867966182468155722L;
	private String fieldId;
	private Object fieldValue;
}
