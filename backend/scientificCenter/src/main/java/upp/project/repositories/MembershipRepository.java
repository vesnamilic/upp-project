package upp.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.Membership;

@Repository
public interface MembershipRepository extends JpaRepository<Membership, Long> {
	
	@Query("SELECT membership from RegisteredUser as user inner join user.memberships as membership inner join membership.magazine as magazine WHERE "
			+ "magazine.id = ?1 and user.username = ?2")
	Membership findMagazineMembership(Long magazineId, String username);
}
