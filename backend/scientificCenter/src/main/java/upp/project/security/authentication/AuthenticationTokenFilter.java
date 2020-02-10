package upp.project.security.authentication;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.impl.identity.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import upp.project.security.JwtProvider;

@Component
public class AuthenticationTokenFilter extends OncePerRequestFilter {

	private JwtProvider jwtProvider;

	private UserDetailsService userDetailsService;

	private IdentityService identityService;

	public AuthenticationTokenFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService,
			IdentityService identityService) {
		super();
		this.jwtProvider = jwtProvider;
		this.userDetailsService = userDetailsService;
		this.identityService = identityService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		String username;
		String authenticationToken = jwtProvider.getToken(request);
		HttpServletResponse res = (HttpServletResponse) response;
		res.setHeader("Access-Control-Allow-Origin", "*");
		res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		res.setHeader("Access-Control-Max-Age", "3600");
		res.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, x-requested-with, authorization");

		if (authenticationToken != null) {
			username = jwtProvider.getUsername(authenticationToken);

			if (username != null) {
				UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
				if (jwtProvider.validateToken(authenticationToken, userDetails)) {
					List<Group> groups = this.identityService.createGroupQuery().groupMember(username).list();
					List<String> userIds = groups.stream().map(Group::getId).collect(Collectors.toList());
					Authentication auth = new Authentication(username, userIds);
					this.identityService.setAuthentication(auth);
					this.identityService.setAuthenticatedUserId(username);
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
							userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken
							.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					// After setting the Authentication in the context, we specify
					// that the current user is authenticated. So it passes the
					// Spring Security Configurations successfully.
					SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

				}
			}
		}

		if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {

			response.setStatus(HttpServletResponse.SC_OK);
		} else {

			if (identityService.getCurrentAuthentication() == null) {
				List<String> userIds = new ArrayList<>();
				userIds.add("guests");
				Authentication auth = new Authentication("guest", userIds);
				this.identityService.setAuthenticatedUserId("guest");
				this.identityService.setAuthentication(auth);
				response.setStatus(HttpServletResponse.SC_OK);
			}
			try {
				filterChain.doFilter(request, response);
			} catch (Exception e) {
				// response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}

		}

	}
}
