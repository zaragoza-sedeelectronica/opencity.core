package org.sede.servicio.acceso.dao;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.sede.core.rest.Mensaje;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.GczUsuario;
import org.sede.servicio.acceso.entity.Usuario;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

/**
 * The Interface GczUsuarioGenericDAO.
 * 
 * @autor Ayuntamiento de Zaragoza
 */
public interface GczUsuarioGenericDAO extends GenericDAO<GczUsuario, BigDecimal> {
	
	/**
	 * Gets the credenciales.
	 *
	 * @param login Login
	 * @return the credenciales
	 */
	public Credenciales getCredenciales(String login);
	
	/**
	 * Validar.
	 *
	 * @param registro Registro
	 * @return sets the
	 */
	public Set<ConstraintViolation<Object>> validar(Object registro);
	
	/**
	 * Update visible.
	 *
	 * @param id Id
	 * @param value Value
	 * @return int
	 */
	public int updateVisible(BigDecimal id, String value);
	
	/**
	 * New password.
	 *
	 * @param user User
	 * @return mensaje
	 */
	public Mensaje newPassword(GczUsuario user);
	
	/**
	 * Gets the credenciales.
	 *
	 * @param login Login
	 * @param password Password
	 * @return the credenciales
	 */
	public Credenciales getCredenciales(String login, String password);
	
	/**
	 * Sets the pk.
	 *
	 * @param id Id
	 * @param pk Pk
	 * @return true, if successful
	 */
	public boolean setPk(String id, String pk);
	
	/**
	 * Update.
	 *
	 * @param id Id
	 * @param usuario Usuario
	 * @param password Password
	 * @return true, if successful
	 */
	public boolean update(String id, Usuario usuario, String password);
	
	/**
	 * Crear.
	 *
	 * @param usuario Usuario
	 * @param password Password
	 * @return string
	 */
	public String crear(Usuario usuario, String password);
	
	/**
	 * Removes the.
	 *
	 * @param login Login
	 * @return true, if successful
	 */
	public boolean remove(String login);
	
	/**
	 * Usuario existente.
	 *
	 * @param usuario Usuario
	 * @return true, if successful
	 */
	public boolean usuarioExistente(Usuario usuario);
	
	/**
	 * Generar token.
	 *
	 * @param clientId Client id
	 * @param idAplicacion Id aplicacion
	 * @return object
	 */
	public Object generarToken(final String clientId, final String idAplicacion);
	
	/**
	 * Enviar token recuperacion.
	 *
	 * @param email Email
	 * @return mensaje
	 */
	public Mensaje enviarTokenRecuperacion(String email);
	
	/**
	 * Obtener login de token.
	 *
	 * @param token Token
	 * @return string
	 */
	public String obtenerLoginDeToken(String token);
}
