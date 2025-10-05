package com.fiap.sishospitalar.service;

import java.security.Key;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.fiap.sishospitalar.config.JwtConfigProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@SuppressWarnings("deprecation")
@Service
public class JwtService {
	
    private final JwtConfigProperties jwtConfigProperties; // << NOVO CAMPO INJETADO

    public JwtService(JwtConfigProperties jwtConfigProperties) {
        this.jwtConfigProperties = jwtConfigProperties;
    }

	private Key getSigningKey() {
		// Use o getter da nova classe
		return Keys.hmacShaKeyFor(jwtConfigProperties.getSecret().getBytes());
	}

	public String gerarToken(UserDetails userDetails) {
		Date agora = new Date();
		Date validade = new Date(agora.getTime() + jwtConfigProperties.getExpirationMs()); 

		return Jwts.builder()
				.setSubject(userDetails.getUsername())
				.setIssuedAt(agora)
				.setExpiration(validade)
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

	public boolean validarToken(String token, UserDetails userDetails) {
		try {
			final String username = extrairUsername(token);
			return username.equals(userDetails.getUsername()) && !estaExpirado(token);
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}

	public String extrairUsername(String token) {
		return extrairClaims(token).getSubject();
	}

	private boolean estaExpirado(String token) {
		try {
			return extrairClaims(token).getExpiration().before(new Date());
		} catch (ExpiredJwtException e) {
			return true;
		} catch (Exception e) {
			return true;
		}
	}

	private Claims extrairClaims(String token) {
	    return Jwts.parser()
	            .verifyWith((SecretKey) getSigningKey())
	            .build()
	            .parseSignedClaims(token)
	            .getPayload();
	}
}