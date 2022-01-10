package com.services.rexpres.services;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.services.rexpres.entities.Usuario;
import com.services.rexpres.repository.UsuarioRepositpry;

@Service
public class UsuarioServicioImp implements UsuarioServicio {

	private final String ACTIVO = "1";
	private final String NO_ACTIVO = "2";
	private final String ADMIN = "adm";
	private final String USUARIO = "usu";

	@Autowired
	private UsuarioRepositpry usuarioRepositpry;

	@Override
	public List<Usuario> getAllUsuarios() {

		return usuarioRepositpry.findAll();
	}

	@Override
	public Usuario finByIdUsuario(Integer id) {

		if (id != null) {

			Optional<Usuario> usuario = usuarioRepositpry.findById(id);

			if (usuario.isPresent())

				return usuario.get();
			else

				return null;
		} else {

			return null;
		}
	}

	@Override
	public Usuario altaUsuario(Usuario usuario) {

		if (usuario.getCorreo() == null || usuario.getNombre() == null || usuario.getCorreo().equalsIgnoreCase("")
				|| usuario.getNombre().equalsIgnoreCase("") || usuario.getIdusuario() != null) {
			return null;
		} else {
			Usuario usuario_existente = usuarioRepositpry.findByCorreo(usuario.getCorreo());

			if (usuario_existente != null) {

				return null;
			} else {

				if (usuario.getActivo() == null || usuario.getActivo().equalsIgnoreCase("")) {
					usuario.setActivo(ACTIVO);
				}
				if (usuario.getRole() == null || usuario.getActivo().equalsIgnoreCase("")) {
					usuario.setRole(USUARIO.toUpperCase());
				}

				usuario.getRole().toUpperCase();
				return usuarioRepositpry.save(usuario);
			}
		}
	}

	@Override
	public Usuario actualizaUsuario(Usuario usuario) {
		if (null != usuario.getIdusuario())

			if (usuarioRepositpry.findById(usuario.getIdusuario()).isPresent()) {

				Usuario usuario_existente = ((Optional<Usuario>) usuarioRepositpry.findById(usuario.getIdusuario()))
						.get();
				if (null != usuario.getNombre()) {
					if (!usuario.getNombre().equalsIgnoreCase(""))
						usuario_existente.setNombre(usuario.getNombre());
				}
				if (null != usuario.getApellido()) {
					if (!usuario.getApellido().equalsIgnoreCase(""))
						usuario_existente.setApellido(usuario.getApellido());
				}
				if (null != usuario.getCorreo()) {
					if (!usuario.getCorreo().equalsIgnoreCase(""))
						usuario_existente.setCorreo(usuario.getCorreo());
				}
				if (null != usuario.getRole()) {
					if (!usuario.getRole().equalsIgnoreCase(""))
						usuario_existente.setRole(usuario.getRole().toUpperCase());
				}
				if (null != usuario.getActivo()) {
					if (!usuario.getActivo().equalsIgnoreCase(""))
						usuario_existente.setActivo(usuario.getActivo());
				}
				usuarioRepositpry.save(usuario_existente);

				usuario_existente = null;

				Usuario usuario_actualizado = ((Optional<Usuario>) usuarioRepositpry.findById(usuario.getIdusuario()))
						.get();

				return usuario_actualizado;

			} else {

				return null;
			}
		else

			return null;
	}

	@Override
	public Boolean elimiarUsuario(Integer id) {
		// comprovar que la id este enviado y que exista
		Usuario usuario_existente = finByIdUsuario(id);

		if (usuario_existente == null) {
			// el usuario no existe, por lo que no se pude eliminar
			return false;

		} else {
			// el usuario existe
			// elimar el usuario
			usuarioRepositpry.delete(usuario_existente);

			// volver a comprovar que no existe y confirmamos
			usuario_existente = finByIdUsuario(id);
			if (usuario_existente == null) {
				// el usuario en la comprovacion no existe , ok

				return true;

			} else {
				// el usuario aun existe, y no a sido eliminado

				return false;
			}
		}

	}

	@Override
	public Usuario finByNombre(String nombre) {
		// TODO Auto-generated method stub
		return null;
	}

}