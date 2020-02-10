package upp.project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import upp.project.model.Coauthor;
import upp.project.repositories.CoauthorRepository;

@Service
public class CoauthorService {

	@Autowired
	private CoauthorRepository coauthorRepository;
	
	public Coauthor save(Coauthor coauthor) {
		return this.coauthorRepository.save(coauthor);
	}
}
