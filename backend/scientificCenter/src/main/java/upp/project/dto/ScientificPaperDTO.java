package upp.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import upp.project.model.Magazine;
import upp.project.model.PaymentMethods;
import upp.project.model.ScientificPaper;

@Getter
@Setter
@NoArgsConstructor
public class ScientificPaperDTO {

	private ScientificPaper scientificPaper;

	private boolean paymentPossible;

	private int price;
	
	private Long magazineId;
	
	public ScientificPaperDTO(ScientificPaper scientificPaper) {
		Magazine magazine = (Magazine) scientificPaper.getMagazineIssue().getMagazine();
		this.scientificPaper = scientificPaper;
		this.paymentPossible = (magazine.getPaymentMethod().equals(PaymentMethods.READERS))? true : false;
		this.price =magazine.getPaperPrice();
		this.magazineId = scientificPaper.getMagazineIssue().getMagazine().getId();
	}
}
