package com.services.rexpres.services;

import java.util.List;

import com.services.rexpres.entities.Usuario;

public interface UsuarioServicio {

	public List<Usuario> getAllUsuarios();

	public Usuario finByIdUsuario(Integer id);
	
	public Usuario finByNombre(String nombre);

	public Usuario altaUsuario(Usuario usuario);

	public Usuario actualizaUsuario(Usuario usuario);
	
	public Boolean elimiarUsuario(Integer id);
	
	
}
