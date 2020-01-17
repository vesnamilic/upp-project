package upp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.ScientificArea;

@Repository
public interface ScientificAreaRepository extends JpaRepository<ScientificArea, Long> {

	List<ScientificArea> findByDeleted(boolean deleted);
	
	ScientificArea findByIdAndDeleted(Long id, boolean deleted);
	
	ScientificArea findByNameAndDeleted(String name, boolean deleted);
}
