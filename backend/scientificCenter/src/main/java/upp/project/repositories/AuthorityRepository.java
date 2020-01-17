package upp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import upp.project.model.Authority;
import upp.project.model.UserRole;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
	
	Authority findByName(UserRole name);
}