package com.services.rexpres.services;

import java.util.List;

import com.persistence.entity.UsuarioEntity;





public interface UsuarioServicio {

	public List<UsuarioEntity> getAllUsuarios();

	public UsuarioEntity finByIdUsuario(Integer id);
	
	public UsuarioEntity finByNombre(String nombre);

	public UsuarioEntity altaUsuario(UsuarioEntity usuario);

	public UsuarioEntity actualizaUsuario(UsuarioEntity usuario);
	
	public Boolean elimiarUsuario(Integer id);
	
	
}
