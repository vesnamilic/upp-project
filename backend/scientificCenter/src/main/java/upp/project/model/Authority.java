package upp.project.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * 
 * @author Vesna Milic
 *
 */
@Entity
public class Authority implements GrantedAuthority {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3341562479841484573L;

	@Id
    @Column(name="id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Enumerated(EnumType.STRING)
    @Column(name="name")
    private UserRole name;

	public Authority() {
		
	}
	public Authority(UserRole name) {
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.core.GrantedAuthority#getAuthority()
	 */
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return name.name();
	}
	
	public void setName(UserRole name) {
        this.name = name;
    }

    @JsonIgnore
    public UserRole getName() {
        return name;
    }

    @JsonIgnore
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}