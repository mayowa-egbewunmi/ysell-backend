package com.ysell.config.jwt.service;

import com.ysell.config.jwt.models.AppUserDetails;
import com.ysell.config.jwt.models.YsellAuthority;
import com.ysell.jpa.entities.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil implements Serializable {
	
	private static final long serialVersionUID = -2550185165626007488L;
	
	private static final long AUTH_TOKEN_TIMEOUT_IN_SECONDS = 24 * 60 * 60; //1 day

	private static final long REFRESH_TOKEN_TIMEOUT_IN_SECONDS = 365 * 24 * 60 * 60; //1 year

	private String userIdKey = "user_id";

	private String rolesKey = "roles";

	private String authorityKey = "authorities";

	private String clientIdKey = "client_id";

	private final ClientService clientService;

	@Value("${jwt.secret}")
	private String secret;


	public String getSubjectFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}


	@SuppressWarnings("unchecked")
	public AppUserDetails getUserDetailsFromToken(String token) {
		Claims claims = getAllClaimsFromToken(token);

		UUID userId = UUID.fromString(claims.get(userIdKey, String.class));
		String username = getSubjectFromToken(token);
		List<Role> roles = claims.get(rolesKey, ArrayList.class);
		List<String> authorityValues = claims.get(authorityKey, ArrayList.class);
		Set<GrantedAuthority> authorities = authorityValues.stream().map(YsellAuthority::new)
				.collect(Collectors.toSet());

		return AppUserDetails.builder()
				.userId(userId)
				.username(username)
				.roles(roles)
				.authorities(authorities)
				.enabled(true)
				.build();
	}


	public Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}


	public String generateAuthToken(AppUserDetails userDetails) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(userIdKey, userDetails.getUserId());
		claims.put(rolesKey, userDetails.getRoles());
		claims.put(authorityKey, userDetails.getAuthorities().stream()
				.map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet()));

		return doGenerateToken(claims, userDetails.getUsername(), AUTH_TOKEN_TIMEOUT_IN_SECONDS);
	}


	public String generateRefreshToken(String clientId) {
		clientService.validateClientId(clientId);

		Map<String, Object> claims = new HashMap<>();
		claims.put(clientIdKey, clientId);

		return doGenerateToken(claims, clientId, REFRESH_TOKEN_TIMEOUT_IN_SECONDS);
	}


	public Boolean validateToken(String token, String sentUsername) {
		final String username = getSubjectFromToken(token);
		return (username.equals(sentUsername) && !isTokenExpired(token));
	}


	private Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}


	private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}


	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}


	private String doGenerateToken(Map<String, Object> claims, String subject, long expiryInSeconds) {
		return Jwts.builder()
				.setClaims(claims)
				.setSubject(subject)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiryInSeconds * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}
}
