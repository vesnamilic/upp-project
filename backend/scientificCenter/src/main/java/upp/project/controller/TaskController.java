package upp.project.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
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
import upp.project.dto.TaskDTO;
import upp.project.security.JwtProvider;

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
	private JwtProvider jwtProvider;

	@GetMapping(path = "/startProcess", produces = "application/json")
	public ResponseEntity<?> startRegistrationProcess(@RequestParam String processName) {
		ProcessInstance pi = runtimeService.startProcessInstanceByKey(processName);
		Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
		TaskFormData taskFormData = formService.getTaskFormData(task.getId());
		List<FormField> formFieldsList = taskFormData.getFormFields();
		return ResponseEntity.ok(new FormFieldsDTO(task.getId(), formFieldsList, pi.getId()));
	}

	@GetMapping(path = "/getTasks", produces = "application/json")
	public ResponseEntity<?> getAdminTasks(HttpServletRequest request) {
		String username = this.jwtProvider.getUsername(this.jwtProvider.getToken(request));
		if (username != null) {
			List<Task> tasks = taskService.createTaskQuery().taskAssignee(username).active().list();
			List<TaskDTO> dtos = new ArrayList<TaskDTO>();
			for (Task task : tasks) {
				TaskDTO t = new TaskDTO(task.getId(), task.getName(), task.getAssignee());
				dtos.add(t);
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
		return new FormFieldsDTO(task.getId(), properties, task.getProcessInstanceId());
	}

	@PostMapping(path = "commitForm/{taskId}", produces = "application/json")
	public ResponseEntity<?> commitFormTask(Principal principal, @RequestBody List<FormSubmitDTO> dto,
			@PathVariable String taskId) {
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
		if (principal != null) {
			List<Task> nextTasks = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list();
			if (!nextTasks.isEmpty()) {
				Task nextTask = null;
				for (Task t : nextTasks) {
					System.out.println(t.getAssignee());
					System.out.println(principal.getName());
					if (t.getAssignee().equals(principal.getName())) {
						nextTask = t;
						break;
					}
				}
				if (nextTask == null)
					return ResponseEntity.ok().build();
				TaskFormData taskFormData = formService.getTaskFormData(nextTask.getId());
				List<FormField> formFieldsList = taskFormData.getFormFields();
				return ResponseEntity
						.ok(new FormFieldsDTO(nextTask.getId(), formFieldsList, nextTask.getProcessInstanceId()));
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
