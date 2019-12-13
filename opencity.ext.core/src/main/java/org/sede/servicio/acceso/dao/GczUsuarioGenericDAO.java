package org.sede.servicio.acceso.dao;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.sede.core.rest.Mensaje;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.GczUsuario;
import org.sede.servicio.acceso.entity.Usuario;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

public interface GczUsuarioGenericDAO extends GenericDAO<GczUsuario, BigDecimal> {
	public Credenciales getCredenciales(String login);
	public Set<ConstraintViolation<Object>> validar(Object registro);
	public int updateVisible(BigDecimal id, String value);
	public Mensaje newPassword(GczUsuario user);
	public Credenciales getCredenciales(String login, String password);
	public boolean setPk(String id, String pk);
	public boolean update(String id, Usuario usuario, String password);
	public String crear(Usuario usuario, String password);
	public boolean remove(String login);
	public boolean usuarioExistente(Usuario usuario);
	public Object generarToken(final String clientId, final String idAplicacion);
	public Mensaje enviarTokenRecuperacion(String email);
	public String obtenerLoginDeToken(String token);
}
