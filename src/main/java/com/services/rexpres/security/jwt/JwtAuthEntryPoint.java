package com.services.rexpres.security.jwt;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthEntryPoint implements AuthenticationEntryPoint {

	private final static Logger logger = LoggerFactory.getLogger(JwtAuthEntryPoint.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		logger.error("Fallo el metodo commence: " + authException);
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(401);
		response.getWriter().write(
				"{\"status\":401,\"timestamp\":" + System.currentTimeMillis() + ",\"mensage\":\"Acceso no autorizado\"}");

	}

}
