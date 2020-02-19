package upp.project.dto;

import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserOrderDTO {

	private String type;
	
	private List<Long> ids;
}
