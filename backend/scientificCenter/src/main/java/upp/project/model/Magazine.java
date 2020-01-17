package upp.project.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class Magazine {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "ISSSN", nullable = false)
	private String ISSSN;

	@Enumerated(EnumType.STRING)
	@Column(name = "paymentMethod", nullable = false)
	private PaymentMethods paymentMethod;
	
	@Column(name = "approved")
	private boolean approved;
	
	@Column(name="deleted")
	private boolean deleted;

	@ManyToOne(fetch = FetchType.EAGER)
	private RegisteredUser mainEditor;

	@ManyToMany
	private Set<ScientificArea> scientificAreas;
	
	@OneToMany
	private Set<RegisteredUser> editors;
	
	@ManyToMany
	private Set<RegisteredUser> reviewers;
	
	public Magazine() {
		this.scientificAreas = new HashSet<>();
		this.editors = new HashSet<>();
		this.reviewers = new HashSet<>();
	}

}
