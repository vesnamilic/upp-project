package upp.project.handlers;

import java.util.List;

import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Review;
import upp.project.services.ReviewService;

@Service
public class ApproveScientificPaperTaskListener implements TaskListener {

	@Autowired
	private ReviewService reviewService;

	public void notify(DelegateTask delegateTask) {
		Long scientificPaperId = (Long) delegateTask.getExecution().getVariable("scientificPaperId");

		List<Review> reviews = this.reviewService.findAllScientificPaperReviews(scientificPaperId);

		if (reviews == null || reviews.size() == 0) {
			delegateTask.getExecution().setVariable("commentEditorList", "No reviews");
			delegateTask.getExecution().setVariable("recommendationList", "No reviews");
			return;

		}

		String commentEditors = "";
		String recommendaationList = "";

		for (Review review : reviews) {
			String name = review.getReviewer().getFirstName() + " " + review.getReviewer().getLastName();
			commentEditors += name + ": " + review.getCommentEditor() + " \n";
			recommendaationList += name + ": " + review.getRecommendation() + " \n";
		}

		delegateTask.getExecution().setVariable("commentEditorList", commentEditors);
		delegateTask.getExecution().setVariable("recommendationList", recommendaationList);

	}

}
