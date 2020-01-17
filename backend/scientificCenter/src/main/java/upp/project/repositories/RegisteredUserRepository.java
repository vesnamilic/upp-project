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
		
	Boolean existsByUsername(String username);
	
	@SuppressWarnings("rawtypes")
	List<RegisteredUser> findByAuthorities(Collection authorities);
	
	@Query("select distinct us from RegisteredUser as us inner join us.scientificAreas as scia inner join us.authorities as auth where scia in ?1 and ?2 in auth and"
			+ " us.deleted = false")
	List<RegisteredUser> findByScientificAreasAndRole(List<ScientificArea> scientificAreas, Authority authority);

}