package upp.project.controller;

import java.util.Calendar;

import javax.validation.Valid;

import org.camunda.bpm.engine.RuntimeService;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import upp.project.dto.LoginDTO;
import upp.project.model.RegisteredUser;
import upp.project.model.UserTokenState;
import upp.project.security.JwtProvider;
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
		headersRedirect.add("Location", "http://localhost:4200/login");
		headersRedirect.add("Access-Control-Allow-Origin", "*");
		return new ResponseEntity<byte[]>(null, headersRedirect, HttpStatus.FOUND);
	}
}
