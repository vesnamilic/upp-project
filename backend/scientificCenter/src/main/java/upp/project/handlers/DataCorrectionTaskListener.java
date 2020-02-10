package upp.project.handlers;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Review;
import upp.project.services.ReviewService;

@Service
public class DataCorrectionTaskListener implements TaskListener {
	
	@Autowired
	private ReviewService reviewService;

	public void notify(DelegateTask delegateTask) {
		Long scientificPaperId = (Long) delegateTask.getExecution().getVariable("scientificPaperId");
		
		List<Review> reviews = this.reviewService.findAllScientificPaperReviews(scientificPaperId);
		
		if(reviews == null || reviews.size() == 0) {
			delegateTask.getExecution().setVariable("commentAuthorsList", "No reviews");
			return;
			
			
		}
		
		String commentAuthors = "";
		
		for(int i=0 ; i<reviews.size(); i++) {
			Review review = reviews.get(i);
			commentAuthors += i + ": " + review.getCommentAuthor() + " \n";
		}
		
		delegateTask.getExecution().setVariable("commentAuthorsList", commentAuthors);
		
		
		
		
	}


}
