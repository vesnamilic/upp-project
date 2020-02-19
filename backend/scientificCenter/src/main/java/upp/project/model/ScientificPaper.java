package upp.project.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ScientificPaper {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;

	@Column(name="title")
	private String title;

	@Column(name="keywords")
	private String keywords;

	@Column(name="paperAbstract")
	private String paperAbstract;
	
	@Column(name="paperPath")
	private String paperPath;
	
	@Column(name="approved")
	private boolean approved;
	
	@Column(name="doi")
	private String doi;

	@ManyToOne
	private ScientificArea scientificArea;
	
	@ManyToOne
	private RegisteredUser author;
	
	@JsonIgnore
	@ManyToOne
	private MagazineIssue magazineIssue;

	@OneToMany
	private Set<Coauthor> coauthors;
	

}
