package upp.project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import upp.project.dto.FormFieldsDTO;
import upp.project.dto.FormSubmitDTO;
import upp.project.dto.RedirectDTO;
import upp.project.dto.TaskDTO;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/tasks", produces = MediaType.APPLICATION_JSON_VALUE)
public class TaskController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private FormService formService;
	
	@Autowired
	private IdentityService identityService;

	@GetMapping(path = "/startProcess", produces = "application/json")
	public ResponseEntity<?> startRegistrationProcess(@RequestParam String processName) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(processName);
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		TaskFormData taskFormData = formService.getTaskFormData(task.getId());
		List<FormField> formFieldsList = taskFormData.getFormFields();
		return ResponseEntity.ok(new FormFieldsDTO(task.getId(),task.getName(), formFieldsList, pi.getId()));
	}

	@GetMapping(path = "/getTasks", produces = "application/json")
	public ResponseEntity<?> getAdminTasks() {
		String username = this.identityService.getCurrentAuthentication().getUserId();
		if (username != null) {
			System.out.println(username);
			
			List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).active().list();
			List<TaskDTO> dtos = new ArrayList<TaskDTO>();
			for (Task task : tasks) {
				TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
				dtos.add(t);
			}
			
			List<Group> groups = this.identityService.createGroupQuery().groupMember(username).list();
			List<String> userIds = groups.stream().map(Group::getId).collect(Collectors.toList());
			
			for(String id : userIds) {
				List<Task> tasksGroup = taskService.createTaskQuery().taskCandidateGroup(id).active().list();
				for (Task task : tasksGroup) {
					TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
					dtos.add(t);
				}
			}
			
			
			return new ResponseEntity<List<TaskDTO>>(dtos, HttpStatus.OK);
		}

		return ResponseEntity.status(401).build();
	}

	@GetMapping(path = "/{taskId}", produces = "application/json")
	public @ResponseBody FormFieldsDTO getTask(@PathVariable String taskId) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		TaskFormData tfd = formService.getTaskFormData(task.getId());
		List<FormField> properties = tfd.getFormFields();
		return new FormFieldsDTO(task.getId(), task.getName(), properties, task.getProcessInstanceId());
	}

	@PostMapping(path = "commitForm/{taskId}", produces = "application/json")
	public ResponseEntity<?> commitFormTask(@RequestBody List<FormSubmitDTO> dto, @PathVariable String taskId) {
		HashMap<String, Object> map = this.mapListToDto(dto);
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		String processInstanceId = task.getProcessInstanceId();
		runtimeService.setVariable(processInstanceId, "data", dto);
		
		try {
			formService.submitTaskForm(task.getId(), map);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return new ResponseEntity<Object>("Camunda validation error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		ProcessInstance pi=runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
	    if(pi!=null) {
	    	if((String) runtimeService.getVariable(processInstanceId, "redirectUrl") != null) {
				RedirectDTO redirectDTO = new RedirectDTO();
				redirectDTO.setUrl((String) runtimeService.getVariable(processInstanceId, "redirectUrl"));
				runtimeService.setVariable(processInstanceId,"redirectUrl", null);
				return ResponseEntity.status(200).body(redirectDTO) ;
			}
	    }
		
		String username = this.identityService.getCurrentAuthentication().getUserId();
		if (username != null) {
			List<Task> nextTasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
			if (!nextTasks.isEmpty()) {
				Task nextTask = null;
				for (Task t : nextTasks) {
					if (t.getAssignee().equals(username)) {
						nextTask = t;
						break;
					}
				}
				if (nextTask == null)
					return ResponseEntity.ok().build();
				TaskFormData taskFormData = formService.getTaskFormData(nextTask.getId());
				List<FormField> formFieldsList = taskFormData.getFormFields();
				return ResponseEntity
						.ok(new FormFieldsDTO(nextTask.getId(), nextTask.getName(), formFieldsList, nextTask.getProcessInstanceId()));
			}
		}
		return ResponseEntity.ok().build();
	}

	private HashMap<String, Object> mapListToDto(List<FormSubmitDTO> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmitDTO temp : list) {
			if (!(temp.getFieldValue() instanceof List))
				map.put(temp.getFieldId(), temp.getFieldValue());
		}

		return map;
	}

}
