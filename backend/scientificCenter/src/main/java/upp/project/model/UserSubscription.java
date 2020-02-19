package upp.project.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class UserSubscription {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@JsonIgnore
	@Column(name = "uuid")
	private String uuid;
	
	@Column(name = "orderStatus")
	@Enumerated(EnumType.STRING)
	private OrderStatus subscriptionStatus;
	
	@JsonIgnore
	@ManyToOne
	private RegisteredUser buyer;
	
	@ManyToOne
	private Magazine magazine;

	public UserSubscription() {
		super();
	}

	public UserSubscription(String uuid, Date expirationDate, OrderStatus subscriptionStatus, String successUrl, String errorUrl, String failedUrl, RegisteredUser buyer) {
		super();
		this.uuid = uuid;
		this.subscriptionStatus = subscriptionStatus;
		this.buyer = buyer;
	}

	
}
