package upp.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import upp.project.model.Magazine;
import upp.project.model.MagazineIssue;
import upp.project.model.PaymentMethods;

@Getter
@Setter
@NoArgsConstructor
public class IssueDTO {

	private MagazineIssue magazineIssue;
	
	private boolean paymentPossible;
	
	private int price;
	
	private Long magazineId;
	
	public IssueDTO(MagazineIssue magazineIssue) {
		this.magazineIssue = magazineIssue;
		this.paymentPossible = magazineIssue.getMagazine().getPaymentMethod().equals(PaymentMethods.READERS)? true : false;
		this.price = magazineIssue.getMagazine().getIssuePrice();
		this.magazineId = magazineIssue.getMagazine().getId();
	}
}
