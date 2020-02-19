package upp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.Magazine;
import upp.project.model.RegisteredUser;

@Repository
public interface MagazineRepository extends JpaRepository<Magazine, Long> {
	
	Magazine findByIdAndDeleted(Long id, boolean deleted);
	
	Magazine findByIdAndDeletedAndApproved(Long id, boolean deleted, boolean approved);
	
	List<Magazine> findByDeleted(boolean deleted);
	
	List<Magazine> findByDeletedAndApproved(boolean deleted, boolean approved);
	
	List<Magazine> findByDeletedAndApprovedAndRegisteredOnPaymentHub(boolean deleted, boolean approved, boolean regitered);
	
	Magazine findByEmailAndDeleted(String email, boolean deleted);
	
	@Query("select distinct mag from Magazine as mag inner join mag.editors as editors where ?1 in editors and mag.deleted = false")
	List<Magazine> findByEditor(RegisteredUser user);

}
