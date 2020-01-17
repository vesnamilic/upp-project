package upp.project.controller;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import upp.project.dto.FormFieldsDTO;
import upp.project.security.JwtProvider;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/magazine", produces = MediaType.APPLICATION_JSON_VALUE)
public class MagazineController {
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private FormService formService;
	
	@Autowired
	private JwtProvider jwtProvider;
	
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	@GetMapping(path = "/startProcess", produces = "application/json")
	public ResponseEntity<?> startRegistrationProcess(HttpServletRequest request, Principal principal) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey("magazine_creation");
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		TaskFormData taskFormData = formService.getTaskFormData(task.getId());
		List<FormField> formFieldsList = taskFormData.getFormFields();
		runtimeService.setVariable(pi.getId(), "currentUser", this.jwtProvider.getUsername(this.jwtProvider.getToken(request)));
		return ResponseEntity.ok(new FormFieldsDTO(task.getId(), formFieldsList, pi.getId()));
	}
	
}
