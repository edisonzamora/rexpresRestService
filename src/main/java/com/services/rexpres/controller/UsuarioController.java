package com.services.rexpres.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.Logger;
import org.apache.catalina.core.ApplicationContext;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.boot.web.servlet.filter.ApplicationContextHeaderFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.persistence.entity.UsuarioEntity;
import com.services.rexpres.dto.JwtDto;
import com.services.rexpres.security.jwt.JwtTokenOperator;
import com.services.rexpres.services.UsuarioServicio;

@RestController
@RequestMapping("/usuapi")
public class UsuarioController {

	@Value("${auth.usuarios}")
	public String valor;
	
	protected final  Logger logger = LogManager.getLogger(getClass());

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
	 **/
	// localhost:8080/usuario/api/usuarios
	@GetMapping("/usuario/api/usuarios")
	private List<UsuarioEntity> todosUsuarios(HttpServletRequest request) {
		AnnotationConfigServletWebServerApplicationContext cotex=(AnnotationConfigServletWebServerApplicationContext)	request.getAttribute("org.springframework.web.servlet.DispatcherServlet.CONTEXT");
		logger.info(valor);
		logger.info(">>>>>>>>>>>>>>>>>>>>todosUsuarios<<<<<<<<<<<<<<<<<<<<");
		return usuarioServicio.getAllUsuarios();
	}

	/**
	 * @author Edison Zamora
	 * @param idusuario
	 * @return usuario (json)
	 **/
	// localhost:8080/usuario/api/id/
	@GetMapping("/usuario/api/id/{idusuario}")
	private ResponseEntity<UsuarioEntity> buscarUsuario(@PathVariable Integer idusuario) {
		
		logger.info(">>>>>>>>>>>>>>>>>>>>buscarUsuario<<<<<<<<<<<<<<<<<<<<");
		UsuarioEntity usuario = usuarioServicio.finByIdUsuario(idusuario);

		if (usuario != null) {
			logger.info(">>>>>>>>>>>>>>>>>>>>Id encontrado<<<<<<<<<<<<<<<<<<<<");
			return ResponseEntity.ok(usuario);

		} else {

			logger.info(">>>>>>>>>>>>>>>>>>>>Id no encontrado<<<<<<<<<<<<<<<<<<<<");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
		}
	}

	/**
	 * @author Edison Zamora
	 * @param usuario_ingresado
	 * @return Usuario (json)
	 */
	// localhost:8080/alata
	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/alta")
	private ResponseEntity<UsuarioEntity> altaUsuario(@RequestBody UsuarioEntity usuario_ingresado) {
		logger.info(">>>>>>>>>>>>>>>>>>>>altaUsuario<<<<<<<<<<<<<<<<<<<<");

		UsuarioEntity nuevo_usuairo = usuarioServicio.altaUsuario(usuario_ingresado);

		if (nuevo_usuairo == null) {
			logger.info(">>>>>>>>>>>>>>>>>>>> unsuario ya existente o no se a podido registrar<<<<<<<<<<<<<<<<<<<<");
			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();

		} else {
			logger.info(">>>>>>>>>>>>>>>>>>>> unsuario registrado<<<<<<<<<<<<<<<<<<<<");
			return ResponseEntity.ok(nuevo_usuairo);
		}
	}

	/**
	 * @param UsuarioEntity_ (json)
	 * @return usuario actalizado
	 * @author Edison Zamora
	 **/
	// localhost:8080/actualizar
	@PreAuthorize("hasRole('ADMIN')")
	@PutMapping("/actualizar")
	private ResponseEntity<UsuarioEntity> actualizarUsuario(@RequestBody UsuarioEntity usuario) {
		logger.info(">>>>>>>>>>>>>>>>>>>>actualizarUsuario<<<<<<<<<<<<<<<<<<<<");
		UsuarioEntity usuairo_actualizado = usuarioServicio.actualizaUsuario(usuario);

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
	@DeleteMapping("/baja/{id}")
	private ResponseEntity<?> elimiarUsuario(@PathVariable Integer id) {
		logger.info(">>>>>>>>>>>>>>>>>>>>elimiarUsuario<<<<<<<<<<<<<<<<<<<<");
		if (usuarioServicio.elimiarUsuario(id))

			return ResponseEntity.status(HttpStatus.ACCEPTED).build();

		else

			return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).build();
	}

	/**
	 * @author Edison Zamora
	 * @param UsuarioEntity_ (json)
	 * @return ResponseEntity= json con token, nombre y roles
	 */
	// localhost:8080/login
	@PostMapping(value = "/login",produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ResponseEntity<?> login(@RequestBody UsuarioEntity loginUsuario) {
//        if (bindingResult.hasErrors())
//            return new ResponseEntity(new Mensaje("Campos mal"), HttpStatus.BAD_REQUEST);
		logger.info(">>>>>>>>>>>>>>>>>>>>login<<<<<<<<<<<<<<<<<<<<");
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginUsuario.getNombre(), loginUsuario.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtTokenOperator.generateToken(authentication);
		logger.info("token: "+jwt);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		JwtDto jwtDto = new JwtDto(jwt, userDetails.getUsername(), userDetails.getAuthorities());
		return new ResponseEntity<>(jwtDto, HttpStatus.OK);
	}

}