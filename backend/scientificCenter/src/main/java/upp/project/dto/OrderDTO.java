package upp.project.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderDTO {

	@NotNull
	@Positive
	private Double paymentAmount;

	@NotNull
	private Long magazineId;

}
