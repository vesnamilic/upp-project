package upp.project.controller;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.form.FormField;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import upp.project.dto.FormFieldsDTO;
import upp.project.dto.LoginDTO;
import upp.project.dto.RegistrationDTO;
import upp.project.dto.UserDTO;
import upp.project.model.Authority;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificArea;
import upp.project.model.UserRole;
import upp.project.model.UserTokenState;
import upp.project.security.JwtProvider;
import upp.project.services.AuthorityService;
import upp.project.services.ScientificAreaService;
import upp.project.services.UserCustomService;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

	@Value("${registrationProcessId}")
	private String processId;

	@Autowired
	private JwtProvider jwtProvider;

	@Lazy
	@Autowired
	private AuthenticationManager authenticationManger;

	@Autowired
	private UserCustomService userCustomService;

	@Autowired
	private RuntimeService runtimeService;
	
	@Autowired
	private IdentityService identityService;
	
	@Autowired
	private ScientificAreaService scientificAreasService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private TaskService taskService;
	
	@Autowired
	private FormService formService;
	
	@GetMapping(path = "/startProcess", produces = "application/json")
	public ResponseEntity<?> startRegistrationProcess() {
		System.out.println("PROCES");
		System.out.println(this.identityService.getCurrentAuthentication());
		String username = this.identityService.getCurrentAuthentication().getUserId();
		
		if(username!=null) {
			
			List<Group> groups = this.identityService.createGroupQuery().groupMember(username).list();
			List<String> userIds = groups.stream().map(Group::getId).collect(Collectors.toList());
			
			if(userIds.contains("guests")) {
				ProcessInstance pi = runtimeService.startProcessInstanceByKey("registration");
				Task task = taskService.createTaskQuery().processInstanceId(pi.getId()).list().get(0);
				TaskFormData taskFormData = formService.getTaskFormData(task.getId());
				List<FormField> formFieldsList = taskFormData.getFormFields();
				return ResponseEntity.ok(new FormFieldsDTO(task.getId(),task.getName(), formFieldsList, pi.getId()));
			}
			
		}
		return ResponseEntity.status(401).build();
		
	}

	@PostMapping(value = "/login")
	public ResponseEntity<?> createAuthenticationToken(@Valid @RequestBody LoginDTO loginRequest) {
		Authentication authentication = null;
		try {
			authentication = authenticationManger.authenticate(
					new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
		} catch (AuthenticationException e) {
			return new ResponseEntity<>("Pogrešno korisničko ime i/ili lozinka!", HttpStatus.BAD_REQUEST);
		}

		SecurityContextHolder.getContext().setAuthentication(authentication);

		RegisteredUser user = (RegisteredUser) authentication.getPrincipal();
		String token = jwtProvider.generateJwtToken(user.getUsername(), 1);
		if (user.isEnabled())
			return ResponseEntity.ok(new UserTokenState(token, user.getUsername(), user.getAuthorities(),
					jwtProvider.getExpirationDateFromToken(token)));

		return new ResponseEntity<>("Nalog nije aktiviran", HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/confirmRegistration")
	public ResponseEntity<?> confirmRegistration(@RequestParam("token") String token) {
		String username = jwtProvider.getUsername(token);
		if (username == null) {
			return new ResponseEntity<>("Invalid user token", HttpStatus.BAD_REQUEST);
		}

		RegisteredUser registeredUser = userCustomService.findUser(username);

		if (registeredUser == null) {
			return new ResponseEntity<>("Username invalid", HttpStatus.BAD_REQUEST);
		} else if (registeredUser.isEnabled()) {
			return new ResponseEntity<>("User is already activated!", HttpStatus.BAD_REQUEST);
		}

		Calendar cal = Calendar.getInstance();

		if ((jwtProvider.getExpirationDateFromToken(token).getTime() - cal.getTime().getTime()) <= 0) {
			return ResponseEntity.badRequest().build();
		}

		String processId = this.jwtProvider.getProcessIdFromToken(token);
		runtimeService.setVariable(processId, "confirm", true);
		HttpHeaders headersRedirect = new HttpHeaders();
		headersRedirect.add("Location", "https://localhost:4205/login");
		headersRedirect.add("Access-Control-Allow-Origin", "*");
		return new ResponseEntity<byte[]>(null, headersRedirect, HttpStatus.FOUND);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@GetMapping("/users/all")
	public ResponseEntity<?> getUsers(){
		
		List<RegisteredUser> users = this.userCustomService.findAllUsers();
		List<UserDTO> usersDTO = new ArrayList<UserDTO>();
		
		for(RegisteredUser user : users) {
			String roles = "";
			for (Iterator<? extends GrantedAuthority> iterator = user.getAuthorities().iterator(); iterator.hasNext();) {
				Authority auth = (Authority) iterator.next();
				roles += auth.getName() + " ";
				
			}
			usersDTO.add(new UserDTO(user.getUsername(), user.getFirstName(), user.getLastName(), user.getEmail(), roles));
		}
		
		
		return ResponseEntity.ok(usersDTO);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@GetMapping("/users/groups")
	public ResponseEntity<?> getUserGroups(){
		
		List<Group> groups = this.identityService.createGroupQuery().list();
		
		return ResponseEntity.ok(groups);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@GetMapping("/users/groups/{groupId}")
	public ResponseEntity<?> getGroupMembers(@PathVariable String groupId){
		
		List<User> groups = this.identityService.createUserQuery().memberOfGroup(groupId).list();
		
		return ResponseEntity.ok(groups);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMINISTRATOR')")
	@PostMapping("/adminRegistration")
	public ResponseEntity<?> registerUser(@RequestBody RegistrationDTO registrationDTO) {
		
		
		if (userCustomService.findUser(registrationDTO.getUsername()) != null) {
			return ResponseEntity.badRequest().build();
		}
		
		
		RegisteredUser user = new RegisteredUser();
		user.setFirstName(registrationDTO.getFirstName());
		user.setLastName(registrationDTO.getLastName());
		user.setCity(registrationDTO.getCity());
		user.setCountry(registrationDTO.getCountry());
		user.setTitle(registrationDTO.getTitle());
		user.setEmail(registrationDTO.getEmail());
		user.setUsername(registrationDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));
		user.setRequestedReviewerRole(registrationDTO.isRequestedReviewerRole());
		user.setEnabled(true);
		List<Long> scientificAreas = registrationDTO.getScientificAreas();
		for (Long id : scientificAreas) {
			ScientificArea area = this.scientificAreasService.findById(id);
			if (area == null) {
				break;
			} else {
				user.getScientificAreas().add(area);
			}
		}
		

		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(authorityService.findByName(UserRole.ROLE_EDITOR));
		if(user.isRequestedReviewerRole())
			authorities.add(authorityService.findByName(UserRole.ROLE_REVIEWER));
		
		
		user.setAuthorities(authorities);
		if (userCustomService.save(user) != null) {
			User camundaUser = identityService.newUser(user.getUsername());
			camundaUser.setEmail(user.getEmail());
			camundaUser.setFirstName(user.getFirstName());
			camundaUser.setLastName(user.getLastName());
			camundaUser.setPassword(user.getPassword());
			identityService.saveUser(camundaUser);
			identityService.createMembership(user.getUsername(), "editors");
			if(user.isRequestedReviewerRole())
				identityService.createMembership(user.getUsername(), "reviewers");
		}
		
		return ResponseEntity.status(201).build();
	}
}
