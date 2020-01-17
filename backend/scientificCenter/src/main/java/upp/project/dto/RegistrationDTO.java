package upp.project.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class RegistrationDTO {
	
	@NotNull
	private String username;

	@NotNull
	private String password;
	
	private String firstName;
	
	private String lastName;
	
	private String city;
	
	private String country;
	
	private String title;
	
	private String email;
	
	private boolean requestedReviewerRole;
	
	private List<Long> scientificAreas;


}
