package com.ysell.config.jwt.filter;

import java.io.IOException;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.ysell.config.WebSecurityConfig;
import com.ysell.config.jwt.models.AppUserDetails;
import com.ysell.config.jwt.service.JwtTokenUtil;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

	private final JwtTokenUtil jwtTokenUtil;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		
		final String requestTokenHeader = request.getHeader("Authorization");
		String url = request.getRequestURL().toString();
		boolean isAllowed = Arrays.asList(WebSecurityConfig.ALLOWED_URLS).stream()
							      .anyMatch(url::endsWith);
		if(isAllowed) {
			chain.doFilter(request, response);
			return;
		}
		
		String username = null;
		String jwtToken = null;
				
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7); // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
			
			try {
				username = jwtTokenUtil.getUsernameFromToken(jwtToken);
			} catch (IllegalArgumentException e) {
				logger.error(url + ": Unable to get JWT Token");
			} catch (ExpiredJwtException e) {
				logger.error(url + ": JWT Token has expired");
			}
		} else {
			logger.warn(url + ": JWT Token does not begin with Bearer String");
		}
		
		// Once we get the token validate it.
		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {					
			// if token is valid configure Spring Security to manually set authentication
			if (jwtTokenUtil.validateToken(jwtToken, username)) {	
				long userId = jwtTokenUtil.getUserIdFromToken(jwtToken);
				UserDetails userDetails = new AppUserDetails(userId, username, null);
				
				UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
						new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
				
				usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				
				// After setting the Authentication in the context, we specify that the current user is authenticated. 
				// So it passes the Spring Security Configurations successfully.
				SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
			}
		}
		
		chain.doFilter(request, response);
	}
}
