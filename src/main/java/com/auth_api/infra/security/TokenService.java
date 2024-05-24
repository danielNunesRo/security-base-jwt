package com.auth_api.infra.security;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth_api.domain.user.Users;

@Service
public class TokenService {
	
	public String generateToken(Users user) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret-key");
			String token = JWT.create().withIssuer("auth-api").withSubject(user.getLogin())
					.withExpiresAt(generateExpirationDate()).sign(algorithm);
					
			return token;
		} catch(JWTCreationException exception) {
			throw new RuntimeException("Error while generating token", exception);
		}
	}
	
	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256("secret-key");
			return JWT.require(algorithm).withIssuer("auth-api").build().verify(token).getSubject();
		} 
		catch(JWTVerificationException exception) {
			return "";
		}
	}
	
	private Instant generateExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
	
}
