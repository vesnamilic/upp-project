package upp.project.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import upp.project.dto.FormFieldsDTO;
import upp.project.model.Magazine;
import upp.project.services.MagazineService;

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
	private IdentityService identityService;
	
	@Autowired
	private MagazineService magazineService;
	
	
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	@GetMapping(path = "/startProcess", produces = "application/json")
	public ResponseEntity<?> startProcess() {
		
		String username = this.identityService.getCurrentAuthentication().getUserId();
		
		if(username!=null) {
			
			List<Group> groups = this.identityService.createGroupQuery().groupMember(username).list();
			List<String> userIds = groups.stream().map(Group::getId).collect(Collectors.toList());
			
			if(userIds.contains("editors")) {
				ProcessInstance pi = runtimeService.startProcessInstanceByKey("magazine_creation");
				Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
				TaskFormData taskFormData = formService.getTaskFormData(task.getId());
				List<FormField> formFieldsList = taskFormData.getFormFields();
				return ResponseEntity.ok(new FormFieldsDTO(task.getId(),task.getName(), formFieldsList, pi.getId()));
			}
		}
		
		return ResponseEntity.status(401).build();
		
		
	}
	
	@GetMapping("/all")
	public ResponseEntity<?> getAll(){
		return ResponseEntity.ok(this.magazineService.findAllActivated());
	}
	
	@GetMapping("/paymentMethod/{magazineId}")
	public ResponseEntity<?> getPaymentMethod(@PathVariable Long magazineId) {
		Magazine magazine = this.magazineService.findById(magazineId);
		
		if(magazine == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(magazine.getPaymentMethod());
						
	}
	
	@GetMapping(value = "/registration/{processInstanceId}/{magazineId}")
	public ResponseEntity<?> successRegistration(@PathVariable String processInstanceId, @PathVariable Long magazineId) {
		
		System.out.println("MAG | registered on PaymentHub");
		
		Magazine magazine = magazineService.findById(magazineId);
		
		if(magazine != null ) {
			//set registration flag
			magazine.setRegisteredOnPaymentHub(true);
			
			runtimeService.createMessageCorrelation("PaymentHubRegistration").processInstanceId(processInstanceId).correlateWithResult();
			
			return ResponseEntity.ok().build();
		}
		
		return ResponseEntity.badRequest().build();
	}
}
