package upp.project.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.NoArgsConstructor;
import upp.project.model.RegisteredUser;

@Component
@NoArgsConstructor
public class JwtProvider {

	@Value("${spring.application.name}")
	private String APPLICATION_NAME;
	
	private int jwtExpiration = 36000;

	@Value("${jwtSecret}")
	private String jwtSecret;
	
	@Value("Authorization")
	private String AUTH_HEADER;

	private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

	public String generateJwtToken(String RegisteredUsername, int hours) {
		return Jwts.builder().setIssuer("APPLICATION_NAME").setSubject(RegisteredUsername).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpiration * hours * 100))
				.signWith(SIGNATURE_ALGORITHM, jwtSecret).compact();

	}
	
	public String generateJwtTokenRegistration(String RegisteredUsername, String processId, int hours) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("processId", processId);
		return Jwts.builder().setClaims(map).setIssuer(APPLICATION_NAME).setSubject(RegisteredUsername).setIssuedAt(new Date())
				.setExpiration(new Date((new Date()).getTime() + jwtExpiration * hours * 100))
				.signWith(SIGNATURE_ALGORITHM, jwtSecret).compact();

	}
	
	public String getProcessIdFromToken(String token) {
		final Claims claims = this.getAllClaimsFromToken(token);
		return (String) claims.getOrDefault("processId", -1);
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		RegisteredUser registeredUser = (RegisteredUser) userDetails;
		final String username = getUsername(token);
		final Date created = getIssuedAtDateFromToken(token);

		return (username != null && username.equals(registeredUser.getUsername())
				&& !isCreatedBeforeLastPasswordReset(created, registeredUser.getLastPasswordResetDate()));
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) {
		final Date created = this.getIssuedAtDateFromToken(token);
		return (!(this.isCreatedBeforeLastPasswordReset(created, lastPasswordReset))
				&& (!(this.isTokenExpired(token))));
	}
	
	public String refreshToken(String token) {
		String refreshedToken;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			claims.setIssuedAt(new Date());
			refreshedToken = Jwts.builder()
					.setClaims(claims)
					.setExpiration(new Date((new Date()).getTime() + jwtExpiration * 1000))
					.signWith(SIGNATURE_ALGORITHM, jwtSecret).compact();
		} catch (Exception e) {
			refreshedToken = null;
		}
		return refreshedToken;
	}


	private Boolean isTokenExpired(String token) {
		final Date expiration = this.getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}
	
	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return (lastPasswordReset != null && created.before(lastPasswordReset));
	}

	private Claims getAllClaimsFromToken(String token) {
		Claims claims;
		try {
			claims = Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
		} catch (Exception e) {
			claims = null;
		}
		return claims;
	}

	public String getUsername(String token) {
		String RegisteredUsername;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			RegisteredUsername = claims.getSubject();
		} catch (Exception e) {
			RegisteredUsername = null;
		}
		return RegisteredUsername;
	}
	
	
	public Date getIssuedAtDateFromToken(String token) {
		Date issueAt;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			issueAt = claims.getIssuedAt();
		} catch (Exception e) {
			issueAt = null;
		}
		return issueAt;
	}

	
	public Date getExpirationDateFromToken(String token) {
		Date expiration;
		try {
			final Claims claims = this.getAllClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (Exception e) {
			expiration = null;
		}
		return expiration;
	}

	
	public String getToken(HttpServletRequest request) {
		String authHeader = (String)getAuthHeaderFromHeader(request);
		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			return authHeader.substring(7);
		}

		return null;
	}

	public String getAuthHeaderFromHeader(HttpServletRequest request) {
		return request.getHeader(AUTH_HEADER);
	}

	public int getJwtExpiration() {
		return jwtExpiration;
	}
	
}