package upp.project.services.save;

import java.util.HashMap;
import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.dto.FormSubmitDTO;
import upp.project.model.Recommendation;
import upp.project.model.RegisteredUser;
import upp.project.model.Review;
import upp.project.model.ScientificPaper;
import upp.project.services.ReviewService;
import upp.project.services.ScientificPaperService;
import upp.project.services.UserCustomService;

@Service
public class SaveReview implements JavaDelegate {

	@Autowired
	private UserCustomService userCustomService;
	
	@Autowired
	private ScientificPaperService scientificPaperService;
	
	@Autowired
	private ReviewService reviewService;

	@SuppressWarnings("unchecked")
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		String username = (String) execution.getVariable("oneReviewer");
		Long scientificPaperId = (Long) execution.getVariable("scientificPaperId");
		List<FormSubmitDTO> list = (List<FormSubmitDTO>) execution.getVariable("data");
		HashMap<String, Object> map = this.mapListToDto(list);
		
		RegisteredUser user = this.userCustomService.findUser(username);
		ScientificPaper scientificPaper = this.scientificPaperService.getOne(scientificPaperId);
		
		
		Review review = new Review();
		review.setCommentAuthor((String)map.get("commentAuthors"));
		review.setCommentEditor((String)map.get("commentEditor"));
		review.setRecommendation(Recommendation.valueOf((String) map.get("recommendation")));
		review.setReviewer(user);
		review.setScientificPaper(scientificPaper);
		this.reviewService.save(review);
	}

	private HashMap<String, Object> mapListToDto(List<FormSubmitDTO> list) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (FormSubmitDTO temp : list) {
			map.put(temp.getFieldId(), temp.getFieldValue());
		}

		return map;
	}

}
