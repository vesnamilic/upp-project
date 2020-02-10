package upp.project.services.validation;

import java.util.HashMap;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;

@Service
public class ValidateCoauthorData implements JavaDelegate {


	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);

		if(!isValidEmailAddress((String) map.get("email"))) {
			execution.setVariable("validationSuccessful", false);
			return;
		}
		
		execution.setVariable("validationSuccessful", true);
	}	
	
	private HashMap<String, Object> mapListToDto(List<FormSubmitDTO> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmitDTO temp : list){
				map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	
	private boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
	}

}
