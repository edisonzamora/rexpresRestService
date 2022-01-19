package com.services.rexpres.controller;

import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.services.rexpres.dto.JwtDto;
import com.services.rexpres.entities.Usuario;
import com.services.rexpres.security.jwt.JwtTokenOperator;
import com.services.rexpres.services.UsuarioServicio;

@RestController
public class UsuarioController {
	
	private final static Logger logger = LogManager.getLogger(UsuarioController.class);

	@Autowired
	private UsuarioServicio usuarioServicio;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenOperator jwtTokenOperator;

	/**
	 * @author Edison Zamora
	 * @param nada
	 * @return lista de usuarios
	 * **/
	// localhost:8080/usuario/api/usuarios
	@GetMapping("/usuario/api/usuarios")
	private List<Usuario> todosUsuarios() {
		logger.info(">>>>>>>>>>>>>>>>>>>>todosUsuarios<<<<<<<<<<<<<<<<<<<<");
		return usuarioServicio.getAllUsuarios();
	}

	/**
	 * @author Edison Zamora
	 * @param idusuario 
	 * @return usuario (json)
	 **/
	//localhost:8080/usuario/api/id/
	@GetMapping("/usuario/api/id/{idusuario}")
	private ResponseEntity<Usuario> buscarUsuario(@PathVariable Integer idusuario) {
		logger.info(">>>>>>>>>>>>>>>>>>>>buscarUsuario<<<<<<<<<<<<<<<<<<<<");
		Usuario usuario = usuarioServicio.finByIdUsuario(idusuario);

		if (usuario != null)

			return ResponseEntity.ok(usuario);

		else
			logger.info(">>>>>>>>>>>>>>>>>>>>buscarUsuario<<<<<<<<<<<<<<<<<<<<");
			return ResponseEntity.notFound().build();
	}

	/**
	 * @author Edison Zamora
	 * @param usuario_ingresado
	 * @return Usuario (json)
	 */
	// localhost:8080/alata
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/alta")
	private ResponseEntity<Usuario> altaUsuario(@RequestBody Usuario usuario_ingresado) {
		logger.info(">>>>>>>>>>>>>>>>>>>>altaUsuario<<<<<<<<<<<<<<<<<<<<");

		Usuario nuevo_usuairo = usuarioServicio.altaUsuario(usuario_ingresado);

		if (nuevo_usuairo == null)

			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

		else

			return ResponseEntity.ok(nuevo_usuairo);

	}

	/**
	 * @param Usuario (json)
	 * @return usuario actalizado
	 * @author Edison Zamora
	 **/
	// localhost:8080/actualizar
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/actualizar")
	private ResponseEntity<Usuario> actualizarUsuario(@RequestBody Usuario usuario) {
		logger.info(">>>>>>>>>>>>>>>>>>>>actualizarUsuario<<<<<<<<<<<<<<<<<<<<");
		Usuario usuairo_actualizado = usuarioServicio.actualizaUsuario(usuario);

		if (usuairo_actualizado == null)

			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

		else

			return ResponseEntity.ok(usuairo_actualizado);

	}
	/**
	 * @author Edison Zamora
	 * @param id
	 * @return ResponseEntity= 202 Accepted o 406 Not Acceptable.
	 */
	// localhost:8080/usuario/api/baja/
	@PreAuthorize("hasRole('ADMIN')")
	@DeleteMapping("/usuario/api/baja/{id}")
	private ResponseEntity<?> elimiarUsuario(@PathVariable Integer id) {
		logger.info(">>>>>>>>>>>>>>>>>>>>elimiarUsuario<<<<<<<<<<<<<<<<<<<<");
		if (usuarioServicio.elimiarUsuario(id))

			return ResponseEntity.status(HttpStatus.ACCEPTED).build();

		else

			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}

	/**
	 * @author Edison Zamora
	 * @param Usuario (json)
	 * @return ResponseEntity= json con token, nombre y roles
	 */
	// localhost:8080/login
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Usuario loginUsuario) {
//        if (bindingResult.hasErrors())
//            return new ResponseEntity(new Mensaje("Campos mal"), HttpStatus.BAD_REQUEST);
		logger.info(">>>>>>>>>>>>>>>>>>>>login<<<<<<<<<<<<<<<<<<<<");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUsuario.getNombre(), loginUsuario.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtTokenOperator.generateToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
		return new ResponseEntity<>(jwtDto, HttpStatus.OK);
	}

}