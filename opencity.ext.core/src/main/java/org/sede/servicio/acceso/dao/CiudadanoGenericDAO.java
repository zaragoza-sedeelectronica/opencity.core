package org.sede.servicio.acceso.dao;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;

import org.sede.core.rest.Mensaje;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.springframework.web.multipart.MultipartFile;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

/**
 * Interface CiudadanoGenericDAO.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
public interface CiudadanoGenericDAO extends GenericDAO<Ciudadano, Integer> {
	
	/**
	 * Usuario existente.
	 *
	 * @param usuario Usuario
	 * @return true, if successful
	 */
	public boolean usuarioExistente(Ciudadano usuario);
	
	/**
	 * Activate.
	 *
	 * @param id Id
	 * @param email Email
	 * @param accountId Account id
	 * @return true, if successful
	 */
	public boolean activate(BigDecimal id, String email, String accountId);
	
	/**
	 * Sets the new password.
	 *
	 * @param email Email
	 * @return string
	 */
	public String setNewPassword(String email);
	
	/**
	 * Update password.
	 *
	 * @param id Id
	 * @param password Password
	 * @return true, if successful
	 */
	public boolean updatePassword(Integer id, String password);
	
	/**
	 * Update screen name.
	 *
	 * @param id Id
	 * @param screenName Screen name
	 * @return true, if successful
	 */
	public boolean updateScreenName(Integer id, String screenName);
	
	/**
	 * Update padron data.
	 *
	 * @param c C
	 * @return true, if successful
	 */
	public boolean updatePadronData(Ciudadano c);
	
	/**
	 * Usuario nif existente.
	 *
	 * @param usuario Usuario
	 * @return true, if successful
	 */
	public boolean usuarioNifExistente(Ciudadano usuario);
	
	/**
	 * Update district.
	 *
	 * @param c C
	 * @return boolean
	 */
	public Boolean updateDistrict(Ciudadano c);
	
	/**
	 * Update datos junta padron.
	 */
	public void updateDatosJuntaPadron();
	
	/**
	 * Update datos distrito seccion padron.
	 */
	public void updateDatosDistritoSeccionPadron();
	
	/**
	 * Update junta usuario.
	 *
	 * @param c C
	 * @return boolean
	 */
	public Boolean updateJuntaUsuario(Ciudadano c);
	
	/**
	 * Usuario mobile existente.
	 *
	 * @param c C
	 * @return true, if successful
	 */
	public boolean usuarioMobileExistente(String c);
	
	/**
	 * Creates the token mobile.
	 *
	 * @param id Id
	 * @param mobile Mobile
	 * @return mensaje
	 */
	public Mensaje createTokenMobile(Integer id, String mobile);
	
	/**
	 * Asociar movil.
	 *
	 * @param c C
	 * @param token Token
	 * @return true, if successful
	 */
	public boolean asociarMovil(Ciudadano c, String token);
	
	/**
	 * Crear usuario.
	 *
	 * @param c C
	 * @param password Password
	 * @return mensaje
	 */
	public Mensaje crearUsuario(Ciudadano c, String password);
	
	/**
	 * Obtener usuario.
	 *
	 * @param accountId Account id
	 * @return ciudadano
	 */
	public Ciudadano obtenerUsuario(String accountId);
	
	/**
	 * Obtener usuario empadronado.
	 *
	 * @param accountId Account id
	 * @return ciudadano
	 */
	public Ciudadano obtenerUsuarioEmpadronado(String accountId);
	
	/**
	 * Obtener usuario.
	 *
	 * @param id Id
	 * @return ciudadano
	 */
	public Ciudadano obtenerUsuario(Integer id);
	
	/**
	 * Obtener usuario by nif.
	 *
	 * @param nif Nif
	 * @return ciudadano
	 */
	public Ciudadano obtenerUsuarioByNif(String nif);
	
	/**
	 * Obtener usuario by email.
	 *
	 * @param email Email
	 * @return ciudadano
	 */
	public Ciudadano obtenerUsuarioByEmail(String email);
	
	/**
	 * Asociar imagen.
	 *
	 * @param ciudadano Ciudadano
	 * @param file File
	 * @return mensaje
	 */
	public Mensaje asociarImagen(Ciudadano ciudadano, MultipartFile file);

	/**
	 * Eliminar imagen.
	 *
	 * @param ciudadano Ciudadano
	 * @return mensaje
	 */
	public Mensaje removeImagen(Ciudadano ciudadano);

	/**
	 * Find by email.
	 *
	 * @param email Email
	 * @return big decimal
	 */
	public BigDecimal findByEmail(String email);
	
	/**
	 * Change allow mail.
	 *
	 * @param c C
	 * @param valor Valor
	 * @return long
	 */
	public long changeAllowMail(Ciudadano c, String valor);

	/**
	 * Send activation mail.
	 *
	 * @param ciudadano Ciudadano
	 * @throws MessagingException the messaging exception
	 * @throws InvalidKeyException the invalid key exception
	 * @throws NoSuchAlgorithmException the no such algorithm exception
	 * @throws NoSuchPaddingException the no such padding exception
	 * @throws IllegalBlockSizeException the illegal block size exception
	 * @throws BadPaddingException the bad padding exception
	 */
	public void sendActivationMail(Ciudadano ciudadano) throws MessagingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException;
	
	/**
	 * Enviar token recuperacion.
	 *
	 * @param email Email
	 * @return mensaje
	 */
	public Mensaje enviarTokenRecuperacion(String email);
	
	/**
	 * Obtener id from token.
	 *
	 * @param token Token
	 * @return big decimal
	 */
	public BigDecimal obtenerIdFromToken(String token);
	
	/**
	 * Obtener usuario by documento identificativo.
	 *
	 * @param documentoIdentificativo Documento identificativo
	 * @return ciudadano
	 */
	public Ciudadano obtenerUsuarioByDocumentoIdentificativo(String documentoIdentificativo);
	
	/**
	 * Update documento identificativo.
	 *
	 * @param c C
	 * @return boolean
	 */
	Boolean updateDocumentoIdentificativo(Ciudadano c);
	List<Ciudadano> buscarUsuariosByDocumentoIdentificativo(String documentoIdentificativo);
}

