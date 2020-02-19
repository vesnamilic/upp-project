package upp.project.controller;

import java.security.Principal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import upp.project.dto.IssueDTO;
import upp.project.model.Magazine;
import upp.project.model.MagazineIssue;
import upp.project.model.RegisteredUser;
import upp.project.services.MagazineIssueService;
import upp.project.services.MagazineService;
import upp.project.services.UserCustomService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/issue", produces = MediaType.APPLICATION_JSON_VALUE)
public class IssueController {

	@Autowired	
	private MagazineIssueService issueService;
	
	@Autowired
	private MagazineService magazineService;
	
	@Autowired
	private UserCustomService userCustomService;
	
	
	@GetMapping("/magazine/{magazineId}")
	public ResponseEntity<?> getMagazineIssues(@PathVariable Long magazineId){
		Magazine magazine = this.magazineService.findById(magazineId);
		System.out.println("USAO +++++++++++++++++++++++++++++++++");
		System.out.println(magazine);
		if(magazine == null)
			return ResponseEntity.badRequest().build();
		
		List<MagazineIssue> issues = this.issueService.findByMagazinePublished(magazine);
		System.out.println(issues);
		List<IssueDTO> result = issues.stream().map(new Function<MagazineIssue, IssueDTO>() {
			@Override
			public IssueDTO apply(MagazineIssue paper) {
				IssueDTO paperDTO = new IssueDTO(paper);
				return paperDTO;
			}
		}).collect(Collectors.toList());
		System.out.println(result);
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/paymentMethod/{issueId}")
	public ResponseEntity<?> getPaymentMethod(@PathVariable Long issueId) {
		MagazineIssue issue = this.issueService.getOne(issueId);
		
		if(issue == null)
			return ResponseEntity.notFound().build();
		
		return ResponseEntity.ok(((Magazine)issue.getMagazine()).getPaymentMethod());
						
	}
	
	@PreAuthorize("hasRole('ROLE_EDITOR')")
	@GetMapping("/editor")
	public ResponseEntity<?> getIssuesEditor(Principal editor) {
		RegisteredUser user = this.userCustomService.findUser(editor.getName());
		
		if(user == null) {
			return ResponseEntity.status(400).body("Niko nije ulogovan");
		}
		System.out.println(this.issueService.getEditorIssues(user) + "++++++++++++++++++++++POY");
		return ResponseEntity.ok(this.issueService.getEditorIssues(user));
	}
}
