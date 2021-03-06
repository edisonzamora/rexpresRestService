package com.services.rexpres.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.services.rexpres.entities.Usuario;

public interface UsuarioRepositpry extends JpaRepository<Usuario, Integer>{

	
	public  Usuario findByNombre(String nombre);
	
	public Usuario findByCorreo(String correo);
	
	public List<Usuario> findByActivo(String activo);

}
