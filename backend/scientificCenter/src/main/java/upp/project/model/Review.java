package upp.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Review {

	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "commentEditor", nullable = false)
	private String commentEditor;

	@Column(name = "commentAuthor", nullable = false)
	private String commentAuthor;

	@Enumerated(EnumType.STRING)
	@Column(name = "recommendation", nullable = false)
	private Recommendation recommendation;

	@ManyToOne
	private RegisteredUser reviewer;

	@ManyToOne
	private ScientificPaper scientificPaper;
}
