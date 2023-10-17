package com.services.rexpres.security.jwt;

import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import com.services.rexpres.security.services.UserServices;


public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	UserServices userDetailsService;

	@Autowired
	JwtTokenOperator jwtTokenOperator;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		try {
			// obtenemos la cabecera
			String header = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (header.isEmpty() || !header.startsWith("Bearer ")) {
				filterChain.doFilter(request, response);
				return;
			}
			// Obtenemos el Token
			String token = header.split(" ")[1].trim();
			token.replace("Bearer", "");
			if (!jwtTokenOperator.validate(token)) {
				filterChain.doFilter(request, response);
				return;
			}
			String nombre = jwtTokenOperator.getUsernameforToken(token);
			UserDetails userDetails = userDetailsService.loadUserByUsername(nombre);
			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			e.getMessage();
		}
		filterChain.doFilter(request, response);
	}

}
