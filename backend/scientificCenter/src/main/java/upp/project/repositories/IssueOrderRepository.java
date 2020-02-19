package upp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.IssueOrder;
import upp.project.model.MagazineIssue;
import upp.project.model.OrderStatus;
import upp.project.model.RegisteredUser;

@Repository
public interface IssueOrderRepository  extends JpaRepository<IssueOrder, Long> {

	@Query("SELECT distinct o from IssueOrder as o WHERE o.orderStatus = ?1 or o.orderStatus = ?2 ")
	List<IssueOrder> findOrdersBasedOnOrderStatused(OrderStatus orderStatus1, OrderStatus orderStatus2);
	
	@Query("SELECT distinct mag from IssueOrder as o inner join o.buyer as buyer inner join o.issue as mag  WHERE buyer = ?1")
	List<MagazineIssue> findAllMagazinesFromOrders(RegisteredUser user);
}
