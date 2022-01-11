package com.services.rexpres;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class RexpresConfig {

	//codificacion de la contraseña
	@Bean
	public BCryptPasswordEncoder bcruptPassword() {
		return new BCryptPasswordEncoder();
	}
}
