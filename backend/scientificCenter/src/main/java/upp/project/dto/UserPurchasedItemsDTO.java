package upp.project.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import upp.project.model.Magazine;
import upp.project.model.MagazineIssue;
import upp.project.model.ScientificPaper;
import upp.project.model.UserSubscription;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPurchasedItemsDTO {

	private List<Magazine> magazines;

	private List<MagazineIssue> issues;

	private List<ScientificPaper> scientificPapers;
	private List<UserSubscription> subscriptions;
}
