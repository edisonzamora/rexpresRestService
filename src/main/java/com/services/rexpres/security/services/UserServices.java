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

import com.services.rexpres.entities.Usuario;
import com.services.rexpres.services.UsuarioServicio;

@Service
public class UserServices implements UserDetailsService{

	@Autowired
	UsuarioServicio usuarioServicio;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//BUscamos en usuario por nombre
		Usuario usuario_exiatente= usuarioServicio.finByNombre(username);
		
		//agredamos un roll para el usuario(de momto el role va se Admin por defecto)
		List<GrantedAuthority> roles=new ArrayList<>();
		roles.add(new SimpleGrantedAuthority("ADMIN"));
		
		//creamos el userDetails con el usuario,contreseña recogidas de base de datos y la lista de roles pero defecto
		UserDetails user=new User(usuario_exiatente.getNombre(),usuario_exiatente.getPassword(),roles);
		return user;
	}

}
