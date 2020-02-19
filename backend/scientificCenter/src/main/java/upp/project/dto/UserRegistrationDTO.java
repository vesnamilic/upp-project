package upp.project.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class UserRegistrationDTO {
	
	@NotNull
	private String username;

	@NotNull
	private String password;
	
	@NotNull
	private String firstName;
	
	@NotNull
	private String lastName;
	
	@NotNull
	private String city;
	
	@NotNull
	private String country;
	
	private String title;
	
	@NotNull
	private String email;
	
	private boolean requestedReviewerRole;
	
	private List<Long> scientificAreas;


}
