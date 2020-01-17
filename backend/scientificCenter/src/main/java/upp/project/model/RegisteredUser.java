package upp.project.model;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import org.joda.time.DateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
public class RegisteredUser implements UserDetails {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4939662591959410237L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "username", nullable = false)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "enabled")
	private boolean enabled;

	@Column(name = "lastPasswordResetDate")
	private Timestamp lastPasswordResetDate;

	@Column(name = "firstName", nullable = false)
	private String firstName;

	@Column(name = "lastName", nullable = false)
	private String lastName;

	@Column(name = "city", nullable = true)
	private String city;

	@Column(name = "country", nullable = true)
	private String country;

	@Column(name = "title", nullable = true)
	private String title;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "requestedReviewerRole")
	private boolean requestedReviewerRole;

	@Column(name = "deleted")
	private boolean deleted;

	@ManyToMany
	private Set<ScientificArea> scientificAreas;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "userAuthorities", joinColumns = @JoinColumn(name = "id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "authorityId", referencedColumnName = "id"))
	protected Set<Authority> authorities;

	public RegisteredUser() {
		// TODO Auto-generated constructor stub
		super();
		this.authorities = new HashSet<Authority>();
		this.scientificAreas = new HashSet<ScientificArea>();
		Timestamp now = new Timestamp(DateTime.now().getMillis());
		this.lastPasswordResetDate = now;
		this.deleted = false;

	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public boolean isEnabled() {
		return enabled;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

}
