package com.services.rexpres.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.services.rexpres.services.UsuarioServicio;

@RestController
public class UsuarioController {

	@Autowired
	private UsuarioServicio usuarioServicio;

	// localhost:8080/usuario/api/usuarios
	@GetMapping("/usuario/api/usuarios")
	private List<Usuario> altaUsuario() {

		return usuarioServicio.getAllUsuarios();
	}

	// tets -> localhost:8080/usuario/api/id/
	@GetMapping("/usuario/api/id/{idusuario}")
	private ResponseEntity<Usuario> altaUsuario(@PathVariable Integer idusuario) {

		Usuario usuario = usuarioServicio.finByIdUsuario(idusuario);

		if (usuario != null)

			return ResponseEntity.ok(usuario);

		else

			return ResponseEntity.notFound().build();
	}

	// localhost:8080/usuario/api/usuarios/alata
	@PostMapping("/alta")
	private ResponseEntity<Usuario> altaUsuaruio(@RequestBody Usuario usuario_ingresado) {

		Usuario nuevo_usuairo = usuarioServicio.altaUsuario(usuario_ingresado);

		if (nuevo_usuairo == null)

			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

		else

			return ResponseEntity.ok(nuevo_usuairo);

	}

	// localhost:8080/usuario/api/usuarios
	/**
	 * 
	 * @param Usuario (json)
	 * @return usuario actalizado
	 **/
	@PutMapping("/actualizar")
	private ResponseEntity<Usuario> actualizarUsuario(@RequestBody Usuario usuario) {

		Usuario usuairo_actualizado = usuarioServicio.actualizaUsuario(usuario);

		if (usuairo_actualizado == null)

			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

		else

			return ResponseEntity.ok(usuairo_actualizado);

	}

	@DeleteMapping("/usuario/api/baja/{id}")
	private ResponseEntity elimiarUsuario(@PathVariable Integer id) {

		if (usuarioServicio.elimiarUsuario(id))

			return ResponseEntity.status(HttpStatus.ACCEPTED).build();

		else

			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}

}