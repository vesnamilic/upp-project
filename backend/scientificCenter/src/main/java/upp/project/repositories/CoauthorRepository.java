package upp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.Coauthor;

@Repository
public interface CoauthorRepository  extends JpaRepository<Coauthor, Long> {


}
