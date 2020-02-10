package upp.project.controller;

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
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import upp.project.dto.FormFieldsDTO;
import upp.project.services.ScientificPaperService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/scientificPaper", produces = MediaType.APPLICATION_JSON_VALUE)
public class ScientificPaperController {

	@Autowired
	private TaskService taskService;

	@Autowired
	private RuntimeService runtimeService;

	@Autowired
	private FormService formService;

	@Autowired
	private ScientificPaperService scientificPaperService;

	@Autowired
	private IdentityService identityService;

	@PreAuthorize("hasRole('ROLE_REG_USER')")
	@GetMapping(path = "/startProcess", produces = "application/json")
	public ResponseEntity<?> startProcess() {
		String username = this.identityService.getCurrentAuthentication().getUserId();

		if (username != null) {

			List<Group> groups = this.identityService.createGroupQuery().groupMember(username).list();
			List<String> userIds = groups.stream().map(Group::getId).collect(Collectors.toList());
			if (userIds.contains("users")) {
				ProcessInstance pi = null;
				try {
					pi = runtimeService.startProcessInstanceByKey("processing_text");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("OVDE 1");
				}
				Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
				TaskFormData taskFormData = null;
				try {
					taskFormData = formService.getTaskFormData(task.getId());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					System.out.println("OVDE 2");
				}
				List<FormField> formFieldsList = taskFormData.getFormFields();

				return ResponseEntity.ok(new FormFieldsDTO(task.getId(), task.getName(), formFieldsList, pi.getId()));
			}

		}
		return ResponseEntity.status(401).build();
	}

	@PostMapping("/post")
	public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("processInstance") String processId) {
		System.out.println(processId);
		System.out.println("USAO SAM DA UPLOAD FILE-a URADIM");
		String fileDownloadUrl = "";
		String username = (String) runtimeService.getVariable(processId, "currentUser");
		try {
			fileDownloadUrl = scientificPaperService.store(file, processId, username);
		} catch (Exception e) {
			String message = "FAIL to upload " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);
		}

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		System.out.println(fileDownloadUrl);
		runtimeService.setVariable(processId, "pdf", fileDownloadUrl);
		runtimeService.setVariable(processId, "pdfFileLocation",
				"scientific-papers-dir/" + username + "/" + processId + "/" + fileName);
		return ResponseEntity.status(HttpStatus.OK).body(fileDownloadUrl);
	}

	@GetMapping("/download/{processId}/{fileName:.+}")
	public ResponseEntity<?> downloadFileFromLocal(@PathVariable String processId, @PathVariable String fileName) {
		Resource resource = this.scientificPaperService.downloadFile(processId,(String) runtimeService.getVariable(processId, "currentUser"), fileName);
		return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF)
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
