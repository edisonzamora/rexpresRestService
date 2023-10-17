package com.services.rexpres.repository;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.persistence.entity.UsuarioEntity;


public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Integer>{

	
	public  UsuarioEntity findByNombre(String nombre);
	
	public UsuarioEntity findByCorreo(String correo);
	
	public List<UsuarioEntity> findByActivo(String activo);

}
