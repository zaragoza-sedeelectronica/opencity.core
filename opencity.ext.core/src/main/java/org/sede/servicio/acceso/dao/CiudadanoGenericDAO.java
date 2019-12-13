package org.sede.servicio.acceso.dao;

import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.mail.MessagingException;

import org.sede.core.rest.Mensaje;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.springframework.web.multipart.MultipartFile;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

public interface CiudadanoGenericDAO extends GenericDAO<Ciudadano, Integer> {
	public boolean usuarioExistente(Ciudadano usuario);
	public boolean activate(BigDecimal id, String email, String accountId);
	public String setNewPassword(String email);
	public boolean updatePassword(Integer id, String password);
	public boolean updateScreenName(Integer id, String screenName);
	public boolean updatePadronData(Ciudadano c);
	public boolean usuarioNifExistente(Ciudadano usuario);
	public Boolean updateDistrict(Ciudadano c);
	public void updateDatosJuntaPadron();
	public void updateDatosDistritoSeccionPadron();
	public Boolean updateJuntaUsuario(Ciudadano c);
	public boolean usuarioMobileExistente(String c);
	public Mensaje createTokenMobile(Integer id, String mobile);
	public boolean asociarMovil(Ciudadano c, String token);
	public Mensaje crearUsuario(Ciudadano c, String password);
	public Ciudadano obtenerUsuario(String accountId);
	public Ciudadano obtenerUsuarioEmpadronado(String accountId);
	public Ciudadano obtenerUsuario(Integer id);
	public Ciudadano obtenerUsuarioByNif(String nif);
	public Ciudadano obtenerUsuarioByEmail(String email);
	public Mensaje asociarImagen(Ciudadano ciudadano, MultipartFile file);
	public BigDecimal findByEmail(String email);
	public long changeAllowMail(Ciudadano c, String valor);

	public void sendActivationMail(Ciudadano ciudadano) throws MessagingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException;
	public Mensaje enviarTokenRecuperacion(String email);
	public BigDecimal obtenerIdFromToken(String token);
	public Ciudadano obtenerUsuarioByDocumentoIdentificativo(String documentoIdentificativo);
	Boolean updateDocumentoIdentificativo(Ciudadano c);
}

