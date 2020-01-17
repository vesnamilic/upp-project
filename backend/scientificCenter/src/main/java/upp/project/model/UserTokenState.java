package upp.project.model;

import java.util.Collection;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTokenState {

	private String token;
	private String type = "Bearer";
	private String username;
	private Collection<? extends GrantedAuthority> authorities;
	private Date expiratonDate;
	
	public UserTokenState() {
		super();
		this.token = null;
		this.username = null;
		this.authorities = null;
	}
	
	public UserTokenState(String token,String email, Collection<? extends GrantedAuthority> collection, Date date) {
		super();
		this.token = token;
		this.username = email;
		this.authorities = collection;
		this.expiratonDate = date;
	}

}
