package com.ysell.config.jwt.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public class JwtTokenUtil implements Serializable {
	
	private static final long serialVersionUID = -2550185165626007488L;
	
	private static final long TIMEOUT_IN_SECONDS = 5 * 60 * 60;
	
	@Value("${jwt.secret}")
	private String secret;
	
	private String userIdKey = "user_id";

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}
	
	public UUID getUserIdFromToken(String token) {
		return getClaimFromToken(token, claims -> claims.get(userIdKey, UUID.class));
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

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	public String generateToken(String username, UUID userId) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(userIdKey, userId);
		return doGenerateToken(claims, username);
	}

	//while creating the token -
	//1. Define  claims of the token, like Issuer, Expiration, Subject, and the ID
	//2. Sign the JWT using the HS512 algorithm and secret key.
	//3. According to JWS Compact Serialization(https://tools.ietf.org/html/draft-ietf-jose-json-web-signature-41#section-3.1)
	//   compaction of the JWT to a URL-safe string 
	private String doGenerateToken(Map<String, Object> claims, String subject) {
		return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + TIMEOUT_IN_SECONDS * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Boolean validateToken(String token, String sentUsername) {
		final String username = getUsernameFromToken(token);
		return (username.equals(sentUsername) && !isTokenExpired(token));
	}
}
