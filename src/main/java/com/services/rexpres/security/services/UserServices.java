package com.services.rexpres.security.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.persistence.entity.UsuarioEntity;
import com.services.rexpres.services.UsuarioServicio;

@Service
public class UserServices implements UserDetailsService{

	@Autowired
	UsuarioServicio usuarioServicio;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//BUscamos en usuario por nombre
		UsuarioEntity usuario_exiatente= usuarioServicio.finByNombre(username);
		if(usuario_exiatente==null) {
			throw new UsernameNotFoundException("No se ha encontado el usuario");
			
		}
		List<GrantedAuthority> roles=new ArrayList<>();
		
		if(usuario_exiatente.getRole().equalsIgnoreCase("ADM")) {
		      	
			roles.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
			roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		if(usuario_exiatente.getRole().equalsIgnoreCase("USU")) {
	      	
			roles.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		//agredamos un roll para el usuario(de momto el role va se Admin por defecto)
		
		//creamos el userDetails con el usuario,contreseña recogidas de base de datos y la lista de roles pero defecto
		UserDetails user=new User(usuario_exiatente.getNombre(),usuario_exiatente.getPassword(),roles);
		return user;
	}

}
