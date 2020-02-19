package upp.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class UserOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	private Long id;

	@Column(name = "orderStatus")
	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus;

	@Column(name = "paymentAmount")
	private double paymentAmount;

	@Column(name = "paymentCurrency")
	private String paymentCurrency;
	
	@Column(name = "email")
	private String email;

	@ManyToOne
	private RegisteredUser buyer;


	public UserOrder() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserOrder(Date expirationDate, OrderStatus orderStatus, double paymentAmount, String paymentCurrency,
			String successUrl, String errorUrl, String failedUrl, RegisteredUser buyer) {
		super();
		this.orderStatus = orderStatus;
		this.paymentAmount = paymentAmount;
		this.paymentCurrency = paymentCurrency;
		this.buyer = buyer;
	}





}
