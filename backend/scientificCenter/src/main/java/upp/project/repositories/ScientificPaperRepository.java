package upp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.ScientificPaper;

@Repository
public interface ScientificPaperRepository extends JpaRepository<ScientificPaper,Long> {
	
	

}
