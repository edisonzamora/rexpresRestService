package com.services.rexpres;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class RexpresConfig {

	//codificacion de la contraseña
	@Autowired
	Environment ev;
	
	@Bean
	public  PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer properties=	new PropertySourcesPlaceholderConfigurer();
		
		String ed=	ev.getProperty("auth.usuarios");
		return properties;
	}
	
}
