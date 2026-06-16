package com.filmnema.filmnema_api.service;

import com.filmnema.filmnema_api.config.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

	private final JwtProperties jwtProperties;
	private final Key signingKey;

	public JwtService(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
		this.signingKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtProperties.getSecret()));
	}

	public String generateToken(String username) {
		Instant now = Instant.now();
		Instant expiresAt = now.plusSeconds(jwtProperties.getExpirationMinutes() * 60L);

		return Jwts.builder()
				.setSubject(username)
				.setIssuer(jwtProperties.getIssuer())
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(expiresAt))
				.signWith(signingKey, SignatureAlgorithm.HS256)
				.compact();
	}

	public String extractUsername(String token) {
		return parseClaims(token).getBody().getSubject();
	}

	public boolean isTokenValid(String token) {
		try {
			Claims claims = parseClaims(token).getBody();
			return claims.getSubject() != null && claims.getExpiration() != null && claims.getExpiration().after(new Date());
		} catch (RuntimeException exception) {
			return false;
		}
	}

	private Jws<Claims> parseClaims(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(signingKey)
				.build()
				.parseClaimsJws(token);
	}
}