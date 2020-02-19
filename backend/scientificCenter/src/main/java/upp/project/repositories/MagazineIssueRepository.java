package upp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.Magazine;
import upp.project.model.MagazineIssue;
import upp.project.model.RegisteredUser;

@Repository
public interface MagazineIssueRepository extends JpaRepository<MagazineIssue, Long> {

	List<MagazineIssue> findByPublishedAndMagazine(boolean published, Magazine magazine);
	List<MagazineIssue> findByMagazine(Magazine magazine);
	
	@Query("SELECT distinct issue from MagazineIssue as issue inner join issue.magazine as magazine inner join magazine.mainEditor as user"
			+ " WHERE user=?1")
	List<MagazineIssue> getAllEditorsIssues(RegisteredUser editor);
	
}
