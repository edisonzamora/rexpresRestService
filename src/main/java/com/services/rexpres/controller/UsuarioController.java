package com.services.rexpres.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.services.rexpres.entities.Usuario;
import com.services.rexpres.repository.UsuarioRepositpry;

@RestController
public class UsuarioController {

	@Autowired
	private UsuarioRepositpry usuarioRepositpry;

	// localhost:8080/usuario/api/usuarios
	@GetMapping("/usuario/api/usuarios")
	private List<Usuario> altaUsuario() {

		return usuarioRepositpry.findAll();
	}

	// localhost:8080/usuario/api/usuarios/ID
	@GetMapping("/usuario/api/usuarios/{idusuario}")
	private ResponseEntity<Usuario> altaUsuario(@PathVariable Integer idusuario) {
		Optional<Usuario> usuario = usuarioRepositpry.findById(idusuario);
		if (usuario.isPresent())

			return ResponseEntity.ok(usuario.get());

		else

			return ResponseEntity.notFound().build();
	}

	// localhost:8080/usuario/api/usuarios/alata
	@PostMapping("/usuario/api/usuarios/alta")
	private Usuario altaUsuaruio(@RequestBody Usuario usuario) {
		return usuarioRepositpry.save(usuario);

	}

	// localhost:8080/usuario/api/usuarios
	/**
	 * 
	 * @param Usuario (json)
	 * @return usuario actalizado
	 * **/
	@PutMapping("/usuario/api/usuarios")
	private ResponseEntity<Usuario> actualizarUsuario(@RequestBody Usuario usuarioNew) {

		if (null != usuarioNew.getIdusuario())
			if (usuarioRepositpry.findById(usuarioNew.getIdusuario()).isPresent()) {

				Usuario usuarioOld = ((Optional<Usuario>) usuarioRepositpry.findById(usuarioNew.getIdusuario())).get();
				if (null != usuarioNew.getNombre()) {
					if (!usuarioNew.getNombre().equalsIgnoreCase(""))
						usuarioOld.setNombre(usuarioNew.getNombre());
				}
				if (null != usuarioNew.getApellido()) {
					if (!usuarioNew.getApellido().equalsIgnoreCase(""))
						usuarioOld.setApellido(usuarioNew.getApellido());
				}
				if (null != usuarioNew.getCorreo()) {
					if (!usuarioNew.getCorreo().equalsIgnoreCase(""))
						usuarioOld.setCorreo(usuarioNew.getCorreo());
				}
				if (null != usuarioNew.getRole()) {
					if (!usuarioNew.getRole().equalsIgnoreCase(""))
						usuarioOld.setRole(usuarioNew.getRole().toUpperCase());
				}
				if (null != usuarioNew.getActivo()) {
					if (!usuarioNew.getActivo().equalsIgnoreCase(""))
						usuarioOld.setActivo(usuarioNew.getActivo());
				}
				usuarioRepositpry.save(usuarioOld);
				Usuario repUsuario = ((Optional<Usuario>) usuarioRepositpry.findById(usuarioNew.getIdusuario())).get();
				return ResponseEntity.ok(repUsuario);

			} else {

				return ResponseEntity.notFound().build();
			}
		else
			return ResponseEntity.notFound().build();
	}

	@DeleteMapping("/usuario/api/usuarios/baja/{id}")
	private void elimiarUsuario(@PathVariable Integer id) {

		usuarioRepositpry.deleteById(id);
	}

}
