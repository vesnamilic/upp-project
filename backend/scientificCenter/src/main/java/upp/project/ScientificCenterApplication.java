package upp.project;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.annotation.PostConstruct;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.client.RestTemplate;

import upp.project.model.RegisteredUser;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class ScientificCenterApplication {

	@Autowired
	private IdentityService identityService;

	public static void main(String[] args) {
		createDir();
		SpringApplication.run(ScientificCenterApplication.class, args);
	}

	private static void createDir() {
		try {
			if (Files.exists(Paths.get("scientific-papers-dir"))) {
				FileSystemUtils.deleteRecursively(Paths.get("scientific-papers-dir").toFile());
			}
			Files.createDirectory(Paths.get("scientific-papers-dir"));
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}

	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	@PostConstruct
	private void createUserGroup() {

		List<Group> groups = identityService.createGroupQuery()
				.groupIdIn("users", "reviewers", "admin", "editors", "guests").list();
		if (groups.isEmpty()) {

			Group usersGroup = identityService.newGroup("users");
			usersGroup.setName("Users");
			identityService.saveGroup(usersGroup);

			Group reviewesGroup = identityService.newGroup("reviewers");
			reviewesGroup.setName("Reviewers");
			identityService.saveGroup(reviewesGroup);

			Group adminGroup = identityService.newGroup("admin");
			adminGroup.setName("Administrators");
			identityService.saveGroup(adminGroup);

			Group editorsGroup = identityService.newGroup("editors");
			editorsGroup.setName("Editors");
			identityService.saveGroup(editorsGroup);

			Group guestsGroup = identityService.newGroup("guests");
			guestsGroup.setName("Guests");
			identityService.saveGroup(guestsGroup);
		}

		List<User> users = identityService.createUserQuery().userIdIn("vesnamilic", "reviewereditor", "reviewer",
				"editor", "editor2", "reviewer2", "recenzent4", "author1", "author2", "author3", "guests").list();
		if (users.isEmpty()) {
			registerInCamunda(new RegisteredUser(null, "vesnamilic", "admin", true, null, "Vesna", "Milic", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "reviewereditor", "admin", true, null, "Milica", "Milic",
					"Vrbas", "Serbia", "dr", "wesnamilic@gmail.com", true, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "reviewer", "admin", true, null, "Petar", "Komnenic", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", true, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "editor", "admin", true, null, "Marijana", "Matkovski", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "editor2", "admin", true, null, "Marijana", "Kolosnjaji",
					"Vrbas", "Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "reviewer2", "admin", true, null, "Andrea", "Babic", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", true, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "author1", "admin", true, null, "Tamara", "Grozdanic", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "author2", "admin", true, null, "Vladimir", "Micunovic", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "author3", "admin", true, null, "Maja", "Micunovic", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "guest", "guest", true, null, "Guest", "Micunovic", "Vrbas",
					"Serbia", "dr", "guest@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "reviewer3", "admin", true, null, "Reviewer", "3", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "reviewer4", "admin", true, null, "Reviewer", "4", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "reviewer5", "admin", true, null, "Reviewer", "5", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "editor3", "admin", true, null, "Editor", "3", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			registerInCamunda(new RegisteredUser(null, "editor4", "admin", true, null, "Editor", "4", "Vrbas",
					"Serbia", "dr", "wesnamilic@gmail.com", false, false, null, null, null));
			identityService.createMembership("vesnamilic", "admin");
			identityService.createMembership("reviewereditor", "editors");
			identityService.createMembership("reviewereditor", "reviewers");
			identityService.createMembership("reviewer", "reviewers");
			identityService.createMembership("editor", "editors");
			identityService.createMembership("editor2", "editors");
			identityService.createMembership("reviewer2", "reviewers");
			identityService.createMembership("author1", "users");
			identityService.createMembership("author2", "users");
			identityService.createMembership("author3", "users");
			identityService.createMembership("guest", "guests");
			identityService.createMembership("reviewer3", "reviewers");
			identityService.createMembership("reviewer4", "reviewers");
			identityService.createMembership("reviewer5", "reviewers");
			identityService.createMembership("editor3", "editors");
			identityService.createMembership("editor3", "reviewers");
			identityService.createMembership("editor4", "editors");

		}

	}

	private void registerInCamunda(RegisteredUser newUser) {
		try {
			User camundaUser = identityService.newUser(newUser.getUsername());
			camundaUser.setPassword(newUser.getPassword());
			camundaUser.setFirstName(newUser.getFirstName());
			camundaUser.setLastName(newUser.getLastName());
			camundaUser.setEmail(newUser.getEmail());
			identityService.saveUser(camundaUser);
		} catch (Exception e) {
			System.out.println("Korisnik vec postoji");
		}
	}

}
