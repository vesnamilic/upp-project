package upp.project.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.model.Authority;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificArea;
import upp.project.model.UserRole;
import upp.project.repositories.RegisteredUserRepository;
import upp.project.security.JwtProvider;

@Service
public class UserCustomService implements UserDetailsService, JavaDelegate {

	@Autowired
	private RegisteredUserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private AuthorityService authorityService;

	@Autowired
	private IdentityService identityService;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ScientificAreaService scientificAreaService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		RegisteredUser user = userRepository.findByUsername(username).orElseThrow(
				() -> new UsernameNotFoundException("User Not Found with -> username or email : " + username));
		return user;
	}

	public RegisteredUser findUser(String username) {
		Optional<RegisteredUser> user = this.userRepository.findByUsername(username);

		if (user.isPresent())
			return user.get();

		return null;
	}

	public List<RegisteredUser> findAllUsers() {
		return this.userRepository.findByDeletedAndEnabled(false, true);
	}
	
	public RegisteredUser save(RegisteredUser user) {
		RegisteredUser savedUser = null;

		try {
			savedUser = this.userRepository.save(user);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return savedUser;
	}

	public void enableUser(RegisteredUser user) {
		user.setEnabled(true);
		this.save(user);
	}

	public List<RegisteredUser> findUserByAuthority(Authority auth) {
		Set<Authority> authorities = new HashSet<>();
		authorities.add(auth);
		return this.userRepository.findByAuthorities(authorities);
	}
	
	public List<RegisteredUser> findByScientificAreasAndRole(List<ScientificArea> areas, Authority auth) {
		return this.userRepository.findByScientificAreasAndRole(areas, auth);
	}
	
	public RegisteredUser findScientificEditor(ScientificArea scientificArea, Long magazineId) {
		List<RegisteredUser> editors = this.userRepository.findScientificEditor(scientificArea, magazineId);
		
		if(editors != null) {
			return (editors.size() > 0)? editors.get(0) : null;
		}
		
		return null;
	}
	
	public List<RegisteredUser> findReviewersByScientificAreas(ScientificArea scientificArea, Long magazineId) {
		List<RegisteredUser> reviewers = this.userRepository.findReviewerByScientificAreas(scientificArea, magazineId);
		
		if(reviewers != null) {
			return reviewers;
		}
		
		return new ArrayList<RegisteredUser>();
		
	}
	
	public RegisteredUser getOne(Long id) {
		return this.userRepository.getOne(id);
	}
	

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);
		RegisteredUser user = new RegisteredUser();
		user.setFirstName((String)map.get("name"));
		user.setLastName((String)map.get("lastName"));
		user.setCity((String) map.get("city"));
		user.setCountry((String) map.get("country"));
		user.setTitle((String) map.get("title"));
		user.setEmail((String) map.get("email"));
		user.setUsername((String) map.get("username"));
		user.setPassword(passwordEncoder.encode((String) map.get("password")));
		System.out.println(map.get("reviewer"));
		user.setRequestedReviewerRole((Boolean) map.get("reviewer"));
		
		List<Map<String, String>> scientificAreas = (List<Map<String, String>>) map.get("scientificAreas");
		for (Map<String, String> areaMap : scientificAreas) {
			ScientificArea area = this.scientificAreaService.findById(Long.parseLong(areaMap.get("item_id")));
			if (area == null) {
				break;
			} else {
				user.getScientificAreas().add(area);
			}
		}
		

		Set<Authority> authorities = new HashSet<Authority>();
		authorities.add(authorityService.findByName(UserRole.ROLE_REG_USER));
		user.setAuthorities(authorities);
		if (save(user) != null) {
			User camundaUser = identityService.newUser(user.getUsername());
			camundaUser.setEmail(user.getEmail());
			camundaUser.setFirstName(user.getFirstName());
			camundaUser.setLastName(user.getLastName());
			camundaUser.setPassword(user.getPassword());
			identityService.saveUser(camundaUser);
			identityService.createMembership(user.getUsername(), "users");
		}

		String text = "Poštovani korisniče, \n\nMolimo vas da potvrdite svoju registraciju kako biste mogli da koristite naše usluge: \n"
				+ "Potvrda registracije se vrši klikom da dati link: "
				+ "https://localhost:9991/auth/confirmRegistration?token="
				+ jwtProvider.generateJwtTokenRegistration((String) execution.getVariable("username"),
						execution.getProcessInstanceId(), 24)
				+ "\nS poštovanjem, \nNaucna centrala";
		
		try {
			this.emailService.sendNotificaitionAsync(user.getEmail(), "Registracija", text);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	private HashMap<String, Object> mapListToDto(List<FormSubmitDTO> list)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for(FormSubmitDTO temp : list){
				map.put(temp.getFieldId(), temp.getFieldValue());
		}
		
		return map;
	}
	
}
