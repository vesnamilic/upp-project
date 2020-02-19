package upp.project.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import upp.project.model.OrderStatus;
import upp.project.model.RegisteredUser;
import upp.project.model.ScientificPaper;
import upp.project.model.ScientificPaperOrder;

@Repository
public interface ScientificPaperOrderRepository  extends JpaRepository<ScientificPaperOrder, Long> {

	@Query("SELECT distinct o from IssueOrder as o WHERE o.orderStatus = ?1 or o.orderStatus = ?2 ")
	List<ScientificPaperOrder> findOrdersBasedOnOrderStatused(OrderStatus orderStatus1, OrderStatus orderStatus2);
	
	@Query("SELECT distinct mag from ScientificPaperOrder as o inner join o.buyer as buyer inner join o.papers as mag  WHERE buyer = ?1")
	List<ScientificPaper> findAllMagazinesFromOrders(RegisteredUser user);
}
