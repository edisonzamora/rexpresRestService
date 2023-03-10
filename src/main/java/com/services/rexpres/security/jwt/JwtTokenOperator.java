package com.services.rexpres.security.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtTokenOperator {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private int expiration;

	private final static Logger logger = LoggerFactory.getLogger(JwtTokenOperator.class);

	/**
	 * setIssuedAt --> Asigna fecha de creción del token setExpiration --> Asigna
	 * fehca de expiración signWith --> Firma
	 */
	public String generateToken(Authentication authentication) {
		UserDetails usuario_principal = (UserDetails) authentication.getPrincipal();
		return Jwts.builder()
				.setSubject(usuario_principal.getUsername())
				.setIssuedAt(new Date())
				.setExpiration(new Date(new Date().getTime() + (2 * 60 * 1000)))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public boolean validate(String token) {
		try {
			Jwts.parser().setSigningKey(secret).parseClaimsJws(token);
			return true;
		} catch (MalformedJwtException e) {
			logger.error("Token mal formado: "+ e.getMessage());
		} catch (UnsupportedJwtException e) {
			logger.error("Token no soportado: "+ e.getMessage());
		} catch (ExpiredJwtException e) {
			logger.error("Token expirado: "+ e.getMessage());
		} catch (IllegalArgumentException e) {
			logger.error("Token vacio: "+ e.getMessage());
		} catch (SignatureException e) {
			logger.error("Fallo con la firma: "+ e.getMessage());
		}
		return false;
	}

	public String getUsernameforToken(String token) {
		String nombre = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
		return nombre;
	}

}
