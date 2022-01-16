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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.services.rexpres.security.jwt.JwtAuthEntryPoint;
import com.services.rexpres.security.jwt.JwtTokenFilter;

@Configuration
@EnableWebSecurity
public class RexpresSecurity extends WebSecurityConfigurerAdapter {

	private final static Logger logger = LogManager.getLogger(RexpresSecurity.class);
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
				.antMatchers("/usuario/**").permitAll()
				.antMatchers("/login").permitAll()
				// permitidas para roles ADMIN
				.antMatchers("/alta").hasRole("ADMIN")
				.antMatchers("/actualizar").hasRole("ADMIN")
				.anyRequest().authenticated().and()
				// .httpBasic();
				// controlador de excepciones solicitudes no autorizadas
				.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
					// http =
					// http.exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint).and();
					@Override
					public void commence(HttpServletRequest request, HttpServletResponse response,
							AuthenticationException authException) throws IOException, ServletException {
						logger.error("Fallo el metodo commence");
					}
				}).and()

				// anulamos el estado de la sesión
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		// añadir el fitro que valida el token
		http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
