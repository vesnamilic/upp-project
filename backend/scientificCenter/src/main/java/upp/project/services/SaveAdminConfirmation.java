package upp.project.services;

import java.util.Set;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Authority;
import upp.project.model.RegisteredUser;
import upp.project.model.UserRole;

@Service
public class SaveAdminConfirmation implements JavaDelegate {

	@Autowired
	private UserCustomService userCustomService;

	@Autowired
	private AuthorityService authorityService;
	
	@Autowired
	private IdentityService identityService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		// TODO Auto-generated method stub\
		System.out.println(execution.getVariable("username"));
		String username = (String) execution.getVariable("username");
		RegisteredUser user = userCustomService.findUser(username);
		if (user != null && (Boolean) execution.getVariable("administratorApproval")) {
			Authority authority = this.authorityService.findByName(UserRole.ROLE_REVIEWER);
			if (authority != null) {
				Set<Authority> authorities = (Set<Authority>) user.getAuthorities();
				authorities.add(authority);
				this.userCustomService.save(user);
				identityService.createMembership(user.getUsername(), "reviewers");
			}
		}

	}

}
