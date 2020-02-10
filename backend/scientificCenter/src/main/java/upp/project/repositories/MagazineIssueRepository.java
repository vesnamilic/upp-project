package upp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.Magazine;
import upp.project.model.MagazineIssue;

@Repository
public interface MagazineIssueRepository extends JpaRepository<MagazineIssue, Long> {

	MagazineIssue findByPublishedAndMagazine(boolean published, Magazine magazine);
	List<MagazineIssue> findByMagazine(Magazine magazine);
	
}
