package upp.project.security.authentication;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

	public AuthenticationTokenFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
		super();
		this.jwtProvider = jwtProvider;
		this.userDetailsService = userDetailsService;
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
				UserDetails userDetails = userDetailsService.loadUserByUsername(username);
				if (jwtProvider.validateToken(authenticationToken, userDetails)) {
					UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
					usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
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
			try {
				filterChain.doFilter(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			}
		}

	}
}
