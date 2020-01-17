package upp.project.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public enum PaymentMethods {
	AUTHORS ("authors"),
	READERS ("readers")
	;
	
	private final String name;
    
}
