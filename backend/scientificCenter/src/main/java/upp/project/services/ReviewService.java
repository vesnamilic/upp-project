package upp.project.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Review;
import upp.project.repositories.ReviewRepository;

@Service
public class ReviewService {

	@Autowired
	private ReviewRepository reviewRepository;
	
	public Review save(Review review) {
		Review saved = null;
		
		try {
			saved = this.reviewRepository.save(review);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return saved;
	}
	
	public Review getOne(Long id) {
		return this.reviewRepository.getOne(id);
	}
	
	public List<Review> findAllScientificPaperReviews(Long id) {
		return reviewRepository.findAllScientificPaperReviewes(id);
	}
	
}
