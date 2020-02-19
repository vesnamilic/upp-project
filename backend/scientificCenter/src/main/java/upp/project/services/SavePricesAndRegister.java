package upp.project.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import upp.project.dto.FormSubmitDTO;
import upp.project.dto.RegistrationDTO;
import upp.project.dto.RegistrationResponseDTO;
import upp.project.model.Magazine;


@Service
public class SavePricesAndRegister implements JavaDelegate {

	@Autowired
	private MagazineService magazineService;
	
	@Autowired
	private RestTemplate restTemplate;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		Map<String, Object> map = mapListToDto(list);
		Magazine magazine = this.magazineService.findById((Long) execution.getVariable("magazine_id"));
		magazine.setSubscriptionPrice((Integer) map.get("subscriptionPrice"));
		magazine.setIssuePrice((Integer) map.get("issuePrice"));
		magazine.setPaperPrice((Integer) map.get("paperPrice"));

		this.magazineService.save(magazine);

		String successUrl = "https://localhost:9992/magazine/registration/" + execution.getProcessInstanceId() + "/"
				+ magazine.getId();

		RegistrationDTO registrationDTO = new RegistrationDTO(magazine.getEmail(), magazine.getName(), successUrl, "https://localhost:4205/");

		HttpEntity<RegistrationDTO> request = new HttpEntity<>(registrationDTO);

		// send a request to PaymentHub with information about the magazine
		ResponseEntity<RegistrationResponseDTO> response = null;
		try {
			response = restTemplate.exchange("https://localhost:8762/api/client/seller", HttpMethod.POST, request,
					RegistrationResponseDTO.class);
		} catch (RestClientException e) {
			throw e;
		}

		// set the link for redirection as a process variable
		if (response != null && response.getBody().getResponseUrl() != null) {
			execution.setVariable("redirectUrl", response.getBody().getResponseUrl());
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
