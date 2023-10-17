package com.services.rexpres.services;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.persistence.entity.UsuarioEntity;
import com.services.rexpres.repository.UsuarioRepository;

@Service
public class UsuarioServicioImp implements UsuarioServicio {

	private final static Logger logger = LogManager.getLogger(UsuarioServicioImp.class);
	private final String ACTIVO = "1";
	private final String NO_ACTIVO = "2";
	private final String ADMIN = "adm";
	private final String USUARIO = "usu";

	@Autowired
	private UsuarioRepository usuarioRepositpry;

	@Autowired
	@Qualifier("passwordEncoder")
	BCryptPasswordEncoder bcry;

	@Override
	public List<UsuarioEntity> getAllUsuarios() {
		logger.info(">>>>>>>>>>>>>>>>>>>>getAllUsuarios<<<<<<<<<<<<<<<<<<<<");
		return usuarioRepositpry.findAll();
	}

	@Override
	public UsuarioEntity finByIdUsuario(Integer id) {

		if (id != null) {

			Optional<UsuarioEntity> usuario = usuarioRepositpry.findById(id);

			if (usuario.isPresent()) {
				logger.info(">>>>>>>>>>>>>>>>>>>>si existe<<<<<<<<<<<<<<<<<<<<");
				return usuario.get();
			} else {
				logger.info(">>>>>>>>>>>>>>>>>>>>no existe<<<<<<<<<<<<<<<<<<<<");
				return null;
			}
		} else {
			logger.info(">>>>>>>>>>>>>>>>>>>> el campo id oblogatorio<<<<<<<<<<<<<<<<<<<<");
			return null;
		}

	}

	@Override
	public UsuarioEntity altaUsuario(UsuarioEntity usuario) {
		// **datos obligatorios: nombre y correo
		// **nos aseguramos que tengan algun valor y no sean nulos

		if (usuario.getCorreo() == null || usuario.getNombre() == null || usuario.getCorreo().equalsIgnoreCase("")
				|| usuario.getNombre().equalsIgnoreCase("") || usuario.getIdusuario() != null) {
			logger.info(">>>>>>>>>>>>>>>>>>>> verifique los campos obligatorios<<<<<<<<<<<<<<<<<<<<");
			return null;
		} else {

			// ** busamos el usuario por correo.
			// ** si la respuesta no es nula el usuario exite, por lo que ya esta dado de
			// alta
			UsuarioEntity usuario_existente = usuarioRepositpry.findByCorreo(usuario.getCorreo());
			if (usuario_existente != null) {
				logger.info(">>>>>>>>>>>>>>>>>>>> usuario encontrado mendiate el correo<<<<<<<<<<<<<<<<<<<<");

				return null;
			} else {
				logger.info(">>>>>>>>>>>>>>>>>>>> usuario no encontrado mendiate el correo<<<<<<<<<<<<<<<<<<<<");
				// **si el usuario no existe llenamos los datos por defecto (en caso que esten
				// vacios)
				// ** por defecto: activo= 1 en BD, role USU y password 12345
				if (usuario.getActivo() == null || usuario.getActivo().equalsIgnoreCase("")) {
					logger.info(">>>>>>>>>>>>>>>>>>>>llenando campo ACTIVO con valor por defecto<<<<<<<<<<<<<<<<<<<<");
					usuario.setActivo(ACTIVO);
				}
				if (usuario.getRole() == null || usuario.getActivo().equalsIgnoreCase("")) {
					logger.info(">>>>>>>>>>>>>>>>>>>>llenando campo ROLE con valor por defecto<<<<<<<<<<<<<<<<<<<<");

					usuario.setRole(USUARIO.toUpperCase());
				}
				if (usuario.getPassword() == null || usuario.getPassword().equalsIgnoreCase("")) {
					logger.info(
							">>>>>>>>>>>>>>>>>>>>llenando campo PASSWORD con valor por defecto<<<<<<<<<<<<<<<<<<<<");
					// *usamos bcryp para codificar el usuario
					usuario.setPassword(bcry.encode("12345"));
				}

				usuario.getRole().toUpperCase();
				// ** grabamos el usaurio en base de datos
				return usuarioRepositpry.save(usuario);
			}
		}
	}

	@Override
	public UsuarioEntity actualizaUsuario(UsuarioEntity usuario) {
		if (null != usuario.getIdusuario())

			if (usuarioRepositpry.findById(usuario.getIdusuario()).isPresent()) {

				UsuarioEntity usuario_existente = ((Optional<UsuarioEntity>) usuarioRepositpry.findById(usuario.getIdusuario()))
						.get();
				if (null != usuario.getNombre()) {
					if (!usuario.getNombre().equalsIgnoreCase(""))
						logger.info(">>>>>>>>>>>>>>>>>>>>set nombre<<<<<<<<<<<<<<<<<<<<");

					usuario_existente.setNombre(usuario.getNombre());
				}
				if (null != usuario.getApellido()) {
					if (!usuario.getApellido().equalsIgnoreCase(""))
						logger.info(">>>>>>>>>>>>>>>>>>>>set apellido<<<<<<<<<<<<<<<<<<<<");

					usuario_existente.setApellido(usuario.getApellido());
				}
				if (null != usuario.getCorreo()) {
					if (!usuario.getCorreo().equalsIgnoreCase(""))
						logger.info(">>>>>>>>>>>>>>>>>>>>set correo<<<<<<<<<<<<<<<<<<<<");

					usuario_existente.setCorreo(usuario.getCorreo());
				}
				if (null != usuario.getRole()) {
					if (!usuario.getRole().equalsIgnoreCase(""))
						logger.info(">>>>>>>>>>>>>>>>>>>>set role<<<<<<<<<<<<<<<<<<<<");
						usuario_existente.setRole(usuario.getRole().toUpperCase());
				}
				if (null != usuario.getActivo()) {
					if (!usuario.getActivo().equalsIgnoreCase(""))
						logger.info(">>>>>>>>>>>>>>>>>>>>set activo<<<<<<<<<<<<<<<<<<<<");
						usuario_existente.setActivo(usuario.getActivo());
				}
				// controlar exepcion de caso de error
				usuarioRepositpry.save(usuario_existente);

				usuario_existente = null;

				UsuarioEntity usuario_actualizado = ((Optional<UsuarioEntity>) usuarioRepositpry.findById(usuario.getIdusuario()))
						.get();
				logger.info(">>>>>>>>>>>>>>>>>>>>actualizado<<<<<<<<<<<<<<<<<<<<");
				return usuario_actualizado;

			} else {

				return null;
			}
		else
			logger.info(">>>>>>>>>>>>>>>>>>>>campo oblogario no enviaso (ID)<<<<<<<<<<<<<<<<<<<<");

		return null;
	}

	@Override
	public Boolean elimiarUsuario(Integer id) {
		// validacion de id en caso de null
		//
		
		// comprovar que la id este enviado y que exista
		UsuarioEntity usuario_existente = finByIdUsuario(id);

		if (usuario_existente == null) {
			// el usuario no existe, por lo que no se pude eliminar
			logger.info(">>>>>>>>>>>>>>>>>>>>ID de usuario no existe em base de datos<<<<<<<<<<<<<<<<<<<<");

			return false;

		} else {
			// el usuario existe
			// elimar el usuario
			logger.info(">>>>>>>>>>>>>>>>>>>>ID encontrada en base de datos<<<<<<<<<<<<<<<<<<<<");
			usuarioRepositpry.delete(usuario_existente);

			// volver a comprovar que no existe y confirmamos
			usuario_existente = finByIdUsuario(id);
			if (usuario_existente == null) {
				// el usuario en la comprovacion no existe , ok
				logger.info(">>>>>>>>>>>>>>>>>>>>ID eliminado <<<<<<<<<<<<<<<<<<<<");
				return true;

			} else {
				// el usuario aun existe, y no a sido eliminado
				logger.info(">>>>>>>>>>>>>>>>>>>>ID no fue eliminado <<<<<<<<<<<<<<<<<<<<");
				return false;
			}
		}

	}

	@Override
	public UsuarioEntity finByNombre(String nombre) {
		UsuarioEntity usuario = usuarioRepositpry.findByNombre(nombre);
		return usuario;
	}

}