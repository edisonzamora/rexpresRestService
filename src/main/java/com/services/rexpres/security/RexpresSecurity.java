package com.services.rexpres.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.services.rexpres.security.jwt.JwtAuthEntryPoint;
import com.services.rexpres.security.jwt.JwtTokenFilter;

@Configuration
@EnableWebSecurity
public class RexpresSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService userServices;

	@Autowired
	BCryptPasswordEncoder bcryp;

	@Autowired
	JwtAuthEntryPoint jwtAuthEntryPoint;

	
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

		// Habilitar CORS y deshabilitar CSRF
		http.cors().and().csrf().disable();

		// anulamos el estado de la sesión
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and();

		// controlador de excepciones solicitudes no autorizadas

		http = http.exceptionHandling().authenticationEntryPoint(jwtAuthEntryPoint).and();

//	http = http.exceptionHandling().authenticationEntryPoint(new AuthenticationEntryPoint() {
//
//			@Override
//			public void commence(HttpServletRequest request, HttpServletResponse response,
//					AuthenticationException authException) throws IOException, ServletException {
//
//				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
//
//			}
//		}).and();

		/*
		 * Establecemos los perimos a las rutas
		 */

		http.authorizeRequests().

		// permitidas
				antMatchers("/usuario/**").permitAll().
				antMatchers("/login").permitAll()
				// no permitidas
				.anyRequest().authenticated().and();
		// .httpBasic();

		// añadir el fitro que valida el token
		http.addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

}
