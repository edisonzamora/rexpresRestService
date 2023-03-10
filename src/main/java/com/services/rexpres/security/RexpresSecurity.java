package com.services.rexpres.security;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.services.rexpres.security.jwt.JwtAuthEntryPoint;
import com.services.rexpres.security.jwt.JwtTokenFilter;

@Configuration
@EnableWebSecurity
public class RexpresSecurity extends WebSecurityConfigurerAdapter {

	protected final Logger logger = LogManager.getLogger(getClass());
	@Autowired
	private UserDetailsService userServices;

	@Autowired
	private BCryptPasswordEncoder bcryp;

	@Autowired
	private JwtAuthEntryPoint jwtAuthEntryPoint;

	@Bean
	public JwtTokenFilter jwtTokenFilter() {
		return new JwtTokenFilter();
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userServices).passwordEncoder(bcryp);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		logger.warn(">>>>>>>>>>>>>>>>>>>>configure<<<<<<<<<<<<<<<<<<<<");

		// Habilitar CORS y deshabilitar CSRF
		http.cors().and().csrf().disable()

				/*
				 * Establecemos los perimos a las rutas
				 */
				.authorizeRequests()
				// permitidas para cualquiera
				.antMatchers("/usuapi/usuario/**").permitAll()
				.antMatchers("/usuapi/login").permitAll()
				.antMatchers("/auth/usuarios").permitAll()
				// permitidas para roles ADMIN
				.antMatchers("/usuapi/alta").hasRole("ADMIN")
				.antMatchers("/usuapi/actualizar").hasRole("ADMIN")
				.antMatchers("/usuapi/baja/**").hasRole("ADMIN")
				.anyRequest().authenticated();
		// .httpBasic();
		// http =
		// anulamos el estado de la sesión
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// controlador de excepciones solicitudes no autorizadas
		http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
			@Override
			public void commence(HttpServletRequest request, HttpServletResponse response,
					AuthenticationException authException) throws IOException, ServletException {
				logger.error(">>>>>>>>>>>>>>>>>>>> Fallo authenticationEntryPoint: " + authException.getMessage());
				response.setContentType("application/json;charset=UTF-8");
				response.setStatus(401);
				response.getWriter().write("{\"status\":401,\"timestamp\":" + System.currentTimeMillis()
						+ ",\"mensage\":\"Acceso no autorizado\"}");

			}
		});
		http.exceptionHandling().accessDeniedHandler(new AccessDeniedHandler() {

			@Override
			public void handle(HttpServletRequest request, HttpServletResponse response,
					AccessDeniedException accessDeniedException) throws IOException, ServletException {
				logger.error(">>>>>>>>>>>>>>>>>>>> Fallo accessDeniedHandler: " + accessDeniedException.getMessage());
				response.setContentType("application/json;charset=UTF-8");
				response.setStatus(403);
				response.getWriter().write("{\"status\":403,\"timestamp\":" + System.currentTimeMillis()
						+ ",\"mensage\":\"Acceso denegado, compruebe sus roles\"}");

			}
		});

		// añadir el fitro que valida el token
		http.addFilterAfter(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}	

}
