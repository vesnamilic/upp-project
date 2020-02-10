package upp.project.repositories;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.Authority;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificArea;

@Repository
public interface RegisteredUserRepository extends JpaRepository<RegisteredUser,Long> {
	
	Optional<RegisteredUser> findByUsername(String username);
	
	List<RegisteredUser> findByDeletedAndEnabled(Boolean deleted, Boolean enabled);
		
	Boolean existsByUsername(String username);
	
	@SuppressWarnings("rawtypes")
	List<RegisteredUser> findByAuthorities(Collection authorities);
	
	@Query("select distinct us from RegisteredUser as us inner join us.scientificAreas as scia inner join us.authorities as auth where scia in ?1 and ?2 in auth and"
			+ " us.deleted = false")
	List<RegisteredUser> findByScientificAreasAndRole(List<ScientificArea> scientificAreas, Authority authority);

	
	@Query("SELECT distinct editors from Magazine as magazine inner join magazine.editors as editors inner join editors.scientificAreas as scientificAreas "
			+ "WHERE ?1 in scientificAreas and magazine.deleted = false and magazine.approved = true and magazine.id = ?2 ")
	List<RegisteredUser> findScientificEditor(ScientificArea scientificArea, Long magazineId);
	
	@Query("SELECT distinct reviewers from Magazine as magazine inner join magazine.reviewers as reviewers inner join reviewers.scientificAreas as scientificAreas "
			+ "WHERE ?1 in scientificAreas and magazine.deleted = false and magazine.approved = true and magazine.id = ?2 ")
	List<RegisteredUser> findReviewerByScientificAreas(ScientificArea scientificArea, Long magazineId);
}