package com.services.rexpres.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

public class JwtDto {

	private String token;
	
	private String bearer = "Bearer";
	
	private String nombre;
	
	private Collection<? extends GrantedAuthority>  roles;


	public JwtDto(String token, String nombre, Collection<? extends GrantedAuthority> roles) {
		this.token = token;
		this.nombre = nombre;
		this.roles = roles;
	}

	public String getBearer() {
		return bearer;
	}

	public void setBearer(String bearer) {
		this.bearer = bearer;
	}


	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Collection<? extends GrantedAuthority> getRoles() {
		return roles;
	}

	public void setRoles(Collection<? extends GrantedAuthority> roles) {
		this.roles = roles;
	}
}
