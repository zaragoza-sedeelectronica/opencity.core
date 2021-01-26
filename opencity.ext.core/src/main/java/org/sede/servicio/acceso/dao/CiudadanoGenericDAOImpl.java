package org.sede.servicio.acceso.dao;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.mail.MessagingException;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.sede.core.rest.Mensaje;
import org.sede.core.utils.AESSec;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.ProcesadorImagenes;
import org.sede.core.utils.Propiedades;
import org.sede.core.utils.RandomGUID;
import org.sede.servicio.acceso.ConfigCiudadano;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.sede.servicio.padron.dao.PadronGenericDAO;
import org.sede.servicio.sms.dao.SmsGenericDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;

/**
 * Class CiudadanoGenericDAOImpl.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
@Repository
@Transactional(ConfigCiudadano.TM)
public class CiudadanoGenericDAOImpl extends GenericDAOImpl <Ciudadano, Integer> implements CiudadanoGenericDAO {
	
	/** Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(CiudadanoGenericDAOImpl.class);
	
	/** Constant IMG_DISK_PATH. */
	public static final String IMG_DISK_PATH = Propiedades.getPathContDisk() + "paginas/zona-personal/";
	
	/** Constant IMG_HTTP_PATH. */
	public static final String IMG_HTTP_PATH = Propiedades.getPathContExternal() + "paginas/zona-personal/";
	
	/** dao padron. */
	@Autowired
	private PadronGenericDAO daoPadron;
	
	/** dao sms. */
	@Autowired
	private SmsGenericDAO daoSms;

	
	/**
	 * Sets the entity manager.
	 *
	 * @param entityManager the new entity manager
	 */
	@PersistenceContext(unitName=ConfigCiudadano.ESQUEMA)
	public void setEntityManager(EntityManager entityManager) {
		this.setEm(entityManager);
	}
	
	/**
	 * Usuario existente.
	 *
	 * @param usuario Usuario
	 * @return true, if successful
	 */
	public boolean usuarioExistente(Ciudadano usuario) {
		
		Query query = this.em().createNativeQuery("select email from USERS where upper(email) = ?");
		@SuppressWarnings("unchecked")
		List<Object> lista = query.setParameter(1, usuario.getEmail().toUpperCase()).getResultList();
		return !lista.isEmpty();
		
	}
	
	/**
	 * Activate.
	 *
	 * @param id Id
	 * @param email Email
	 * @param accountId Account id
	 * @return true, if successful
	 */
	public boolean activate(BigDecimal id, String email, String accountId) {
		Query update = this.em().createNativeQuery("Update USERS set activado = 1 where upper(EMAIL) like ? and id =? and guid = ?");
		update.setParameter(1, email.toUpperCase());
		update.setParameter(2, id);
		update.setParameter(3, accountId);
		int registros = update.executeUpdate();
		return registros > 0;
	}
	
	/**
	 * Sets the new password.
	 *
	 * @param email Email
	 * @return string
	 */
	@Override
	public String setNewPassword(String email) {
		String newPassword = Funciones.generateRandomPassword();
		Query deletePropWeb = this.em().createNativeQuery("Update USERS set password = ?, activado=1 where upper(EMAIL) = ?");
		deletePropWeb.setParameter(1, Funciones.calculateUserPassword(newPassword));
		deletePropWeb.setParameter(2, email.toUpperCase());
		
		
		int registros = deletePropWeb.executeUpdate();
		if (registros > 0) {
			return newPassword;
		} else {
			return "";
		}
	}
	
	/**
	 * Update password.
	 *
	 * @param id Id
	 * @param password Password
	 * @return true, if successful
	 */
	@Override
	public boolean updatePassword(Integer id, String password) {
		Query deletePropWeb = this.em().createNativeQuery("Update USERS set password = ? where id = ?");
		deletePropWeb.setParameter(1, Funciones.calculateUserPassword(password));
		deletePropWeb.setParameter(2, id);
		
		
		int registros = deletePropWeb.executeUpdate();
		if (registros > 0) {
			Query deleteToken = this.em().createNativeQuery("delete from USERS_RECUPERAR_PASS where id_usuario=?");
			deleteToken.setParameter(1, id);
			deleteToken.executeUpdate();
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Update screen name.
	 *
	 * @param id Id
	 * @param screenName Screen name
	 * @return true, if successful
	 */
	@Override
	public boolean updateScreenName(Integer id, String screenName) {
		Query update = this.em().createNativeQuery("Update USERS set screenname = ? where id = ?");
		update.setParameter(1, screenName);
		update.setParameter(2, id);
		return (update.executeUpdate() > 0);
	}
	
	/**
	 * Usuario nif existente.
	 *
	 * @param usuario Usuario
	 * @return true, if successful
	 */
	public boolean usuarioNifExistente(Ciudadano usuario) {
		
		Query query = this.em().createNativeQuery("select nif from USERS where upper(nif) like ? and ANIO_NACIMIENTO is not null");
		@SuppressWarnings("unchecked")
		List<Object> lista = query.setParameter(1, usuario.getNif().toUpperCase().trim()).getResultList();
		return (!lista.isEmpty());
		
	}
	
	/**
	 * Usuario mobile existente.
	 *
	 * @param mobile Mobile
	 * @return true, if successful
	 */
	public boolean usuarioMobileExistente(String mobile) {
		
		Query query = this.em().createNativeQuery("select mobile from USERS where mobile like ?");
		@SuppressWarnings("unchecked")
		List<Object> lista = query.setParameter(1, mobile.trim()).getResultList();
		return (!lista.isEmpty());
		
	}
	
	/**
	 * Update padron data.
	 *
	 * @param c C
	 * @return true, if successful
	 */
	@Override
	public boolean updatePadronData(Ciudadano c) {
		Query update = this.em().createNativeQuery("Update USERS set NIF=?, EMPADRONADO = 'Si', ANIO_NACIMIENTO=?,ID_DISTRITO=?,ID_SECCION=?,JUNTA=? where ID = ?");
		update.setParameter(1, c.getNif());
		update.setParameter(2, c.getBirthYear());
		update.setParameter(3, c.getDistrict());
		update.setParameter(4, c.getSection());
		update.setParameter(5, c.getJunta());
		update.setParameter(6, c.getId());
		int registros = update.executeUpdate();
		return (registros > 0);
	}
	
	/**
	 * Update district.
	 *
	 * @param c C
	 * @return boolean
	 */
	@Override
	public Boolean updateDistrict(Ciudadano c) {
		Query update = this.em().createNativeQuery("Update USERS set ID_DISTRITO=? where ID = ?");
		update.setParameter(1, c.getDistrict());
		update.setParameter(2, c.getId());
		int registros = update.executeUpdate();
		return (registros > 0);
		
	}
	
	/**
	 * Update junta usuario.
	 *
	 * @param c C
	 * @return boolean
	 */
	@Override
	public Boolean updateJuntaUsuario(Ciudadano c) {
		Query update = this.em().createNativeQuery("Update USERS set JUNTA_USER=? where ID = ?");
		update.setParameter(1, c.getJuntaUser());
		update.setParameter(2, c.getId());
		int registros = update.executeUpdate();
		return (registros > 0);
		
	}
	
	/**
	 * Update documento identificativo.
	 *
	 * @param c C
	 * @return boolean
	 */
	@Override
	public Boolean updateDocumentoIdentificativo(Ciudadano c) {
		Query update = this.em().createNativeQuery("Update USERS set DOCUMENTO_IDENTIFICATIVO=? where ID = ?");
		update.setParameter(1, c.getDocumentoIdentificativo());
		update.setParameter(2, c.getId());
		int registros = update.executeUpdate();
		return (registros > 0);
		
	}
	
	/**
	 * Update datos junta padron.
	 */
	public void updateDatosJuntaPadron() {
    	try {
	    	Query q = this.em().createNativeQuery("select nif,anio_nacimiento,junta from noticias.users where empadronado='Si' and nif is not null and anio_nacimiento is not null");
	    	@SuppressWarnings("unchecked")
			List<Object> result = q.getResultList();
			Query update = this.em().createNativeQuery("update USERS set junta = ? where nif =? and anio_nacimiento = ?");
			Query noEmpadronado = this.em().createNativeQuery("update USERS set junta = null,id_distrito=null,id_seccion=null,empadronado=null  where nif =? and anio_nacimiento = ?");
			for (Iterator<Object> iterador = result.iterator(); iterador.hasNext();) {
	    		Object[] row = (Object[])iterador.next();
	    		String nif = row[0].toString();
	    		Integer year = Integer.parseInt(row[1].toString()); 
	    		String juntaActual = row[2] == null ? "" : row[2].toString();
	    		String junta = daoPadron.showJunta(nif, year);
	    		
	    		if (junta != null && !juntaActual.equals(junta)) {
	    			logger.error("Modificada Junta de: {}:{}", nif, junta);
		    		update.setParameter(1, junta);
		    		update.setParameter(2, nif);
		    		update.setParameter(3, year);
		    		update.executeUpdate();
	    		} else if (junta == null){
	    			String distrito = daoPadron.showDistrito(nif, year);
	    			if (StringUtils.isEmpty(distrito)) {
	    				logger.error("Junta y distrito null para: {}:{}", nif, year);
	    				noEmpadronado.setParameter(1, nif);
	    				noEmpadronado.setParameter(2, year);
	    				noEmpadronado.executeUpdate();
	    			} else {
	    				logger.error("Junta null pero distrito: {}:{}", nif, year + distrito);
	    			}
	    		}
			}
    	} catch (Exception e) {
    		logger.error(e.getMessage());
    	}
    	
    }
	
	/**
	 * Update datos distrito seccion padron.
	 */
	public void updateDatosDistritoSeccionPadron() {
    	try {
	    	Query q = this.em().createNativeQuery("select nif,anio_nacimiento,id_distrito,id_seccion from noticias.users where empadronado='Si' and nif is not null and anio_nacimiento is not null");
	    	@SuppressWarnings("unchecked")
			List<Object> result = q.getResultList();
			Query update = this.em().createNativeQuery("update USERS set id_distrito = ?, id_seccion=? where nif =? and anio_nacimiento = ?");
			for (Iterator<Object> iterador = result.iterator(); iterador.hasNext();) {
	    		Object[] row = (Object[])iterador.next();
	    		String nif = row[0].toString();
	    		Integer year = Integer.parseInt(row[1].toString()); 
	    		String distritoActual = row[2] == null ? "" : row[2].toString();
	    		String seccionActual = row[3] == null ? "" : row[3].toString();
	    		String distrito = daoPadron.showDistrito(nif, year);
	    		
	    		String distAct = distritoActual + "-" + seccionActual;

	    		Integer idDistrito = -1;
	    		Integer idSeccion = -1;
	    		
	    		if (distrito != null) {
		    		String[] mod = distrito.split("-");
	    			idDistrito = Integer.parseInt((String)mod[0]);
    				idSeccion = Integer.parseInt((String)mod[1]);
				}
	    		if (!distAct.equals(idDistrito + "-" + idSeccion)) {
	    			logger.error("Modificado distrito de: " + nif + ":" + distAct + " por " + idDistrito + "-" + idSeccion);
	    			
		    		update.setParameter(1, idDistrito);
		    		update.setParameter(2, idSeccion);
		    		update.setParameter(3, nif);
		    		update.setParameter(4, year);
		    		update.executeUpdate();
	    		} else{
	    			logger.error("Mismo distrito: " + nif + ":" + year + ":" + distrito);
	    		}
			}
    	} catch (Exception e) {
    		logger.error(e.getMessage());
    	}
    	
    }
//	public void updateDatosJuntaPadron() {
//    	Query q = this.em().createNativeQuery("select dni,birthyear from participacion.votaciones_linea2_tranvia where id_usuario_adentra=-1");
//		List<Object> result = q.getResultList();
//		
//		for (Iterator<Object> iterador = result.iterator(); iterador.hasNext();) {
//    		Object[] row = (Object[])iterador.next();
//    		String nif = row[0].toString();
//    		Integer year = Integer.parseInt(row[1].toString()); 
//    		String junta = daoPadron.showJunta(nif, year);
//    		logger.error(nif + ":" + junta);
//		}
//    	
//    }    
//    public void updateDatosJuntaPadron() {
//    	Query q = this.em().createNativeQuery("select nif,anio_nacimiento from noticias.users where empadronado='Si' and id_distrito is null");
//		List<Object> result = q.getResultList();
//		Query update = this.em().createNativeQuery("update USERS set id_distrito = ?,id_seccion = ? where nif =? and anio_nacimiento = ?");
//		for (Iterator<Object> iterador = result.iterator(); iterador.hasNext();) {
//    		Object[] row = (Object[])iterador.next();
//    		String nif = row[0].toString();
//    		Integer year = Integer.parseInt(row[1].toString()); 
//    		String[] distritoSeccion = daoPadron.showDistrito(nif, year).split("-");
//			Integer distrito = Integer.parseInt(distritoSeccion[0]);
//			Integer seccion  = Integer.parseInt(distritoSeccion[1]);
//			logger.error(nif + ":" + distrito + ":" + seccion);
//    		if (distrito != null) {
//	    		update.setParameter(1, distrito);
//	    		update.setParameter(2, seccion);
//	    		update.setParameter(3, nif);
//	    		update.setParameter(4, year);
//	    		update.executeUpdate();
//    		}
//		}
//    	
/**
 * Creates the token mobile.
 *
 * @param id Id
 * @param mobile Mobile
 * @return mensaje
 */
//    }
	@Override
	public Mensaje createTokenMobile(Integer id, String mobile) {
		Query deletePropWeb = this.em().createNativeQuery("Update USERS_MOBILE_TOKEN set active='N' where mobile = ?");
		deletePropWeb.setParameter(1, mobile);
		deletePropWeb.executeUpdate();
		
		deletePropWeb = this.em().createNativeQuery(
				"insert into USERS_MOBILE_TOKEN(id,user_id,mobile,token,fecha,ip,active)"
				+ "values(SEQ_USERS_MOBILE_TOKEN.nextval,?,?,?,sysdate,?,'S')"
				);
		
		Integer codeSms = Funciones.calculateRandomNumber();
		deletePropWeb.setParameter(1, id);
		deletePropWeb.setParameter(2, mobile);
		deletePropWeb.setParameter(3, codeSms);
		deletePropWeb.setParameter(4, Funciones.getPeticion().getIp());
		int registros = deletePropWeb.executeUpdate();
		if (registros > 0) {
			return daoSms.send(new String[]{mobile}, "Código de verificación: " + codeSms);
		} else {
			return new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha creado el código sms");
		}
	}
	
	/**
	 * Asociar movil.
	 *
	 * @param c C
	 * @param token Token
	 * @return true, if successful
	 */
	@Override
	public boolean asociarMovil(Ciudadano c, String token) {
		try {
			String sql = "select MOBILE from noticias.USERS_MOBILE_TOKEN where active='S' and token=? and user_id=?";
			Query query = this.em().createNativeQuery(sql);
			String mobile = (String)query.setParameter(1, token)
					.setParameter(2, c.getId())
					.getSingleResult();
			if (mobile == null) {
				return false;
			} else {
				Query update = this.em().createNativeQuery("Update USERS set mobile = ? where id=?");
				update.setParameter(1, mobile);
				update.setParameter(2, c.getId());
				update.executeUpdate();
				c.setMobile(mobile);
				return true;
			}
		} catch (NoResultException e) {
			return false;
		}
		
	}

	/**
	 * Crear usuario.
	 *
	 * @param c C
	 * @param password Password
	 * @return mensaje
	 */
	public Mensaje crearUsuario(Ciudadano c, String password) {
		if (this.usuarioExistente(c)) {
			return new Mensaje(HttpStatus.BAD_REQUEST.value(), "Ya existe un usuario/a asociado al correo electrónico introducido");
		} else {
			c.setEmail(c.getEmail().toLowerCase());
			c.setAccount_id(new RandomGUID().toString());
			c.setActivado(new BigDecimal(0));
			c.setIsAdmin("0");
			c.setPassword(Funciones.calculateUserPassword(password));
			c.setCreationDate(new Date());
			if (c.getAceptaMail() == null) {
				c.setAceptaMail(Boolean.FALSE);
			}

			try {
				Ciudadano respuesta = this.save(c);
				sendActivationMail(respuesta);
				return new Mensaje(HttpStatus.OK.value(), "Usted se ha registrado correctamente en la Plataforma de Gobierno Abierto."
						+ "<br/>Recibir&aacute; un correo electr&oacute;nico con el remitente \"Ayuntamiento de Zaragoza\" para realizar la activaci&oacute;n de su cuenta. Si no est&aacute; en la bandeja de entrada, busque en las carpetas. Si un filtro antispam o una regla de correo electr&oacute;nico ha movido el correo, puede que se encuentre en la carpeta Spam, Correo no deseado, Papelera, Elementos eliminados o Archivo."
						+ "<br/>En caso de no recibir el correo electr&oacute;nico pongase en contacto con nosotros a través de la dirección de correo electrónico <a href=\"mailto:gobiernoabierto@zaragoza.es\">gobiernoabierto@zaragoza.es</a>"
						+ "<br/>Volver al <a href=\"//www.zaragoza.es/ciudadania/gobierno-abierto/\">portal de Gobierno Abierto</a>.");
			} catch (Exception e) {
				return new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			}
		}
	}
	
	/**
	 * Obtener usuario.
	 *
	 * @param accountId Account id
	 * @return ciudadano
	 */
	@Override
	public Ciudadano obtenerUsuario(String accountId) {
		Query q = em().createNativeQuery("select id, person_name, email, screenname,EMPADRONADO,ANIO_NACIMIENTO,id_distrito,junta,nif,junta_user, mobile, image from noticias.users where activado=1 and guid=?");
		try {
			Object[] row = (Object[])q.setParameter(1, accountId).getSingleResult();
			Ciudadano usr = new Ciudadano();
			usr.setId(((BigDecimal)row[0]).intValue());
			usr.setName((String) row[1]);
			usr.setEmail((String) row[2]);
			usr.setScreen_name((String) row[3]);
			usr.setEmpadronado(row[4] == null ? null : (String) row[4]);
			
			usr.setBirthYear(row[5] == null ? null : Integer.parseInt((String) row[5]));
			usr.setDistrict(row[6] == null ? null : ((BigDecimal) row[6]).intValue());
			usr.setJunta(row[7] == null ? null : (String) row[7]);
			usr.setNif(row[8] == null ? null : (String) row[8]);
			usr.setJuntaUser(row[9] == null ? null : (String) row[9]);
			usr.setMobile(row[10] == null ? null : (String) row[10]);
			usr.setImage(row[11] == null ? null : (String) row[11]);
			return usr;
		} catch (NoResultException e) {
			return null;
		}

	}
	
	/**
	 * Obtener usuario.
	 *
	 * @param id Id
	 * @return ciudadano
	 */
	@Override
	public Ciudadano obtenerUsuario(Integer id) {
		Query q = em().createNativeQuery("select id, person_name, email, screenname,EMPADRONADO,ANIO_NACIMIENTO,id_distrito,junta,nif,junta_user, mobile, image, ACEPTAMAIL,guid from noticias.users where activado=1 and id=?");
		try {
			Object[] row = (Object[])q.setParameter(1, id).getSingleResult();
			Ciudadano usr = new Ciudadano();
			usr.setId(((BigDecimal)row[0]).intValue());
			usr.setName((String) row[1]);
			usr.setEmail((String) row[2]);
			usr.setScreen_name((String) row[3]);
			usr.setEmpadronado(row[4] == null ? null : (String) row[4]);

			usr.setBirthYear(row[5] == null ? null : Integer.parseInt((String) row[5]));
			usr.setDistrict(row[6] == null ? null : ((BigDecimal) row[6]).intValue());
			usr.setJunta(row[7] == null ? null : (String) row[7]);
			usr.setNif(row[8] == null ? null : (String) row[8]);
			usr.setJuntaUser(row[9] == null ? null : (String) row[9]);
			usr.setMobile(row[10] == null ? null : (String) row[10]);
			usr.setImage(row[11] == null ? null : (String) row[11]);
			usr.setAceptaMail(row[12] == null ? Boolean.FALSE : ("S".equals(String.valueOf(row[12])) ? Boolean.TRUE : Boolean.FALSE ));
			usr.setAccount_id(row[13] == null ? null : (String) row[13]);
			return usr;
		} catch (NoResultException e) {
			return null;
		}

	}
	
	/**
	 * Obtener usuario empadronado.
	 *
	 * @param accountId Account id
	 * @return ciudadano
	 */
	@Override
	public Ciudadano obtenerUsuarioEmpadronado(String accountId) {
		Query q = em().createNativeQuery("select id, person_name, email, screenname,EMPADRONADO,ANIO_NACIMIENTO,id_distrito,junta,nif,junta_user, mobile, image from noticias.users where empadronado='Si' and activado=1 and guid=?");
		try {
			Object[] row = (Object[])q.setParameter(1, accountId).getSingleResult();
			Ciudadano usr = new Ciudadano();
			usr.setId(((BigDecimal)row[0]).intValue());
			usr.setName((String) row[1]);
			usr.setEmail((String) row[2]);
			usr.setScreen_name((String) row[3]);
			usr.setEmpadronado(row[4] == null ? null : (String) row[4]);
			
			usr.setBirthYear(row[5] == null ? null : Integer.parseInt((String) row[5]));
			usr.setDistrict(row[6] == null ? null : ((BigDecimal) row[6]).intValue());
			usr.setJunta(row[7] == null ? null : (String) row[7]);
			usr.setNif(row[8] == null ? null : (String) row[8]);
			usr.setJuntaUser(row[9] == null ? null : (String) row[9]);
			usr.setMobile(row[10] == null ? null : (String) row[10]);
			usr.setImage(row[11] == null ? null : (String) row[11]);
			return usr;
		} catch (NoResultException e) {
			return null;
		}

	}
	
	/**
	 * Obtener usuario by nif.
	 *
	 * @param nif Nif
	 * @return ciudadano
	 */
	public Ciudadano obtenerUsuarioByNif(String nif) {
		Query q = em().createNativeQuery("select id, person_name, email, screenname,EMPADRONADO,ANIO_NACIMIENTO,id_distrito,junta,nif,junta_user, mobile,image,documento_identificativo from noticias.users where upper(nif)=? and junta is not null");
		try {
			Object[] row = (Object[])q.setParameter(1, nif.toUpperCase().trim()).getSingleResult();
			Ciudadano usr = new Ciudadano();
			usr.setId(((BigDecimal)row[0]).intValue());
			usr.setName((String) row[1]);
			usr.setEmail((String) row[2]);
			usr.setScreen_name((String) row[3]);
			usr.setEmpadronado(row[4] == null ? null : (String) row[4]);
			
			usr.setBirthYear(row[5] == null ? null : Integer.parseInt((String) row[5]));
			usr.setDistrict(row[6] == null ? null : ((BigDecimal) row[6]).intValue());
			usr.setJunta(row[7] == null ? null : (String) row[7]);
			usr.setNif(row[8] == null ? null : (String) row[8]);
			usr.setJuntaUser(row[9] == null ? null : (String) row[9]);
			usr.setMobile(row[10] == null ? null : (String) row[10]);
			usr.setImage(row[11] == null ? null : (String) row[11]);
			usr.setDocumentoIdentificativo(row[12] == null ? null : (String) row[12]);
			return usr;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Obtener usuario by email.
	 *
	 * @param email Email
	 * @return ciudadano
	 */
	public Ciudadano obtenerUsuarioByEmail(String email) {
		Query q = em().createNativeQuery("select id, person_name, email, screenname,EMPADRONADO,ANIO_NACIMIENTO,id_distrito,junta,nif,junta_user, mobile,image,documento_identificativo from noticias.users where upper(email)=?");
		try {
			Object[] row = (Object[])q.setParameter(1, email.toUpperCase().trim()).getSingleResult();
			Ciudadano usr = new Ciudadano();
			usr.setId(((BigDecimal)row[0]).intValue());
			usr.setName((String) row[1]);
			usr.setEmail((String) row[2]);
			usr.setScreen_name((String) row[3]);
			usr.setEmpadronado(row[4] == null ? null : (String) row[4]);
			
			usr.setBirthYear(row[5] == null ? null : Integer.parseInt((String) row[5]));
			usr.setDistrict(row[6] == null ? null : ((BigDecimal) row[6]).intValue());
			usr.setJunta(row[7] == null ? null : (String) row[7]);
			usr.setNif(row[8] == null ? null : (String) row[8]);
			usr.setJuntaUser(row[9] == null ? null : (String) row[9]);
			usr.setMobile(row[10] == null ? null : (String) row[10]);
			usr.setImage(row[11] == null ? null : (String) row[11]);
			usr.setDocumentoIdentificativo(row[12] == null ? null : (String) row[12]);
			return usr;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Obtener usuario by documento identificativo.
	 *
	 * @param documentoIdentificativo Documento identificativo
	 * @return ciudadano
	 */
	@Override
	public Ciudadano obtenerUsuarioByDocumentoIdentificativo(String documentoIdentificativo) {
		Query q = em().createNativeQuery("select id, person_name, email, screenname, EMPADRONADO, ANIO_NACIMIENTO, id_distrito, junta, nif, junta_user, mobile, image, documento_identificativo from noticias.users where upper(documento_identificativo)=?");
		try {
			Object[] row = (Object[])q.setParameter(1, documentoIdentificativo.toUpperCase().trim()).getSingleResult();
			Ciudadano usr = new Ciudadano();
			usr.setId(((BigDecimal)row[0]).intValue());
			usr.setName((String) row[1]);
			usr.setEmail((String) row[2]);
			usr.setScreen_name((String) row[3]);
			usr.setEmpadronado(row[4] == null ? null : (String) row[4]);
			
			usr.setBirthYear(row[5] == null ? null : Integer.parseInt((String) row[5]));
			usr.setDistrict(row[6] == null ? null : ((BigDecimal) row[6]).intValue());
			usr.setJunta(row[7] == null ? null : (String) row[7]);
			usr.setNif(row[8] == null ? null : (String) row[8]);
			usr.setJuntaUser(row[9] == null ? null : (String) row[9]);
			usr.setMobile(row[10] == null ? null : (String) row[10]);
			usr.setImage(row[11] == null ? null : (String) row[11]);
			usr.setDocumentoIdentificativo(row[12] == null ? null : (String) row[12]);
			return usr;
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@Override
	public List<Ciudadano> buscarUsuariosByDocumentoIdentificativo(String documentoIdentificativo) {
		List<Ciudadano> users = new ArrayList<Ciudadano>();
		Query q = em().createNativeQuery("select id, person_name, email, screenname, EMPADRONADO, ANIO_NACIMIENTO, id_distrito, junta, nif, junta_user, mobile, image, documento_identificativo from noticias.users where upper(documento_identificativo)=?");
		@SuppressWarnings("unchecked")
		List<Object[]> rows = (List<Object[]>) q.setParameter(1, documentoIdentificativo.toUpperCase().trim()).getResultList();
		for (Object[] row : rows) {
			Ciudadano usr = new Ciudadano();
			usr.setId(((BigDecimal)row[0]).intValue());
			usr.setName((String) row[1]);
			usr.setEmail((String) row[2]);
			usr.setScreen_name((String) row[3]);
			usr.setEmpadronado(row[4] == null ? null : (String) row[4]);
			
			usr.setBirthYear(row[5] == null ? null : Integer.parseInt((String) row[5]));
			usr.setDistrict(row[6] == null ? null : ((BigDecimal) row[6]).intValue());
			usr.setJunta(row[7] == null ? null : (String) row[7]);
			usr.setNif(row[8] == null ? null : (String) row[8]);
			usr.setJuntaUser(row[9] == null ? null : (String) row[9]);
			usr.setMobile(row[10] == null ? null : (String) row[10]);
			usr.setImage(row[11] == null ? null : (String) row[11]);
			usr.setDocumentoIdentificativo(row[12] == null ? null : (String) row[12]);
			users.add(usr);
		}
		return users;
	}
	
	/**
	 * Asociar imagen.
	 *
	 * @param ciudadano Ciudadano
	 * @param file File
	 * @return mensaje
	 */
	@Override
	public Mensaje asociarImagen(Ciudadano ciudadano, MultipartFile file) {
		try {
			String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'), file.getOriginalFilename().length());
			String fileName = UUID.randomUUID().toString() + extension;
			ProcesadorImagenes pi = new ProcesadorImagenes();
			BufferedImage in = ImageIO.read(file.getInputStream());
			BufferedImage anexoMiniatura = pi.escalarATamanyo(in, 200, 200);
			pi.salvarImagen(anexoMiniatura, IMG_DISK_PATH, fileName, "jpg", false);
			if (StringUtils.isNotEmpty(ciudadano.getImageName())) {
				File f = new File(IMG_DISK_PATH + ciudadano.getImageName());
				if (!f.delete()) {
					logger.error("No se ha eliminado el fichero {}", f.getAbsolutePath());
				}
			}
			Query update = this.em().createNativeQuery("Update USERS set image = ? where id =?");
			update.setParameter(1, fileName);
			update.setParameter(2, ciudadano.getId());
			int registros = update.executeUpdate();
			if (registros > 0) {
				ciudadano.setImage(fileName);
				return new Mensaje(HttpStatus.OK.value(), "Imagen asociada correctamente");
			} else {
				File f = new File(IMG_DISK_PATH + fileName);
				if (!f.delete()) {
					logger.error("No se ha eliminado el fichero {}", f.getAbsolutePath());
				}
				return new Mensaje(HttpStatus.BAD_REQUEST.value(), "Error al asociar la imagen no se ha encontrado el registro");
			}

		} catch (Exception e) {
			return new Mensaje(HttpStatus.BAD_REQUEST.value(), "Error al asociar la imagen: " + e.getMessage());
		}

	}

	/**
	 * Find by email.
	 *
	 * @param email Email
	 * @return big decimal
	 */
	public BigDecimal findByEmail(String email) {
		Query query = this.em().createNativeQuery("select id from USERS where upper(email) = ?");
		return (BigDecimal) query.setParameter(1, email.toUpperCase()).getSingleResult();

	}
	
	/**
	 * Change allow mail.
	 *
	 * @param c C
	 * @param valor Valor
	 * @return long
	 */
	@Override
	public long changeAllowMail(Ciudadano c, String valor) {
		Query propWeb = this.em().createNativeQuery("update users set ACEPTAMAIL=? where id=?");
		
		propWeb.setParameter(1, valor);
		propWeb.setParameter(2, c.getId());
		return propWeb.executeUpdate();
	}

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
	@Override
	public void sendActivationMail(Ciudadano ciudadano) throws MessagingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		String url = Propiedades.getActivationUrl()+"/acceso/activate?email=" + ciudadano.getEmail() + "&amp;token=" + AESSec.encrypt(ciudadano.getId() + "#" + ciudadano.getAccount_id());
		String txtMensaje = "<p>Se ha creado una nueva cuenta de usuario con su direcci&oacute;n de correo electr&oacute;nico en la " +
				"Plataforma de Gobierno Abierto del Ayuntamiento de Zaragoza. <br/> Para confirmar dicha " +
				"cuenta de usuario y poder acceder a las funciones de la Plataforma deber&aacute; confirmar su cuenta accediendo al siguiente " +
				"enlace: </p><p><a href=\"" + url + "\">Activar cuenta de usuario</a></p>";
		Funciones.sendMail("Plataforma de Gobierno Abierto: Confirmación de Usuario", txtMensaje, ciudadano.getEmail(), "", "HTML");
	}

	/**
	 * Enviar token recuperacion.
	 *
	 * @param email Email
	 * @return mensaje
	 */
	public Mensaje enviarTokenRecuperacion(String email) {
		String sql = "select id,email from users where trim(lower(email)) = ?";
		Query query = this.em().createNativeQuery(sql);
		Object[] obj = (Object[])query.setParameter(1, email.toLowerCase()).getSingleResult();
		if (obj != null) {
			String token = new RandomGUID().toString();
			BigDecimal id = (BigDecimal) obj[0]; 
			String login = (String) obj[1];
			Query deletePropWeb = this.em().createNativeQuery("delete from USERS_RECUPERAR_PASS where id_usuario=?");
			deletePropWeb.setParameter(1, id);
			deletePropWeb.executeUpdate();

			Query propWeb = this.em().createNativeQuery("insert into USERS_RECUPERAR_PASS(id_usuario,token) values(?, ?)");
			propWeb.setParameter(1, id);
			propWeb.setParameter(2, token);
			propWeb.executeUpdate();
			
			String texto = "Buenos días, "
					+ System.getProperty("line.separator") + "Para tener acceso a su perfil en la Plataforma de Gobierno Abierto del Ayuntamiento de Zaragoza, tiene que hacer clic en el siguiente enlace desde el que se le solicitará que detalle una nueva contraseña."
					+ System.getProperty("line.separator") + System.getProperty("line.separator") + "https://www.zaragoza.es/sede/acceso/restaurar?token=" + token
					+ System.getProperty("line.separator") + System.getProperty("line.separator") + "Esa será la contraseña que, a partir de ahora, le requerirá el sistema cuando quiera acceder a su perfil."
					+ System.getProperty("line.separator") + System.getProperty("line.separator") + "Un saludo,"
					+ System.getProperty("line.separator") + "Oficina de Participación, Transparencia y Gobierno Abierto";
			try {
				Funciones.sendMail("Plataforma de Gobierno Abierto de Zaragoza. Recuperación de contraseña", 
						texto, 
						login,
						"Plataforma de Gobierno Abierto de Zaragoza<" + Propiedades.getMailUser() + "@zaragoza.es>",
						"text/plain");
				return new Mensaje(HttpStatus.OK.value(), "Se ha enviado al correo electrónico un enlace para actualizar su contraseña");
				
			} catch (Exception e) {
				return new Mensaje(HttpStatus.BAD_REQUEST.value(), "Se produjo un error al enviar el correo electrónico");	
			}
		} else {
			return new Mensaje(HttpStatus.BAD_REQUEST.value(), "No existen usuarios con la dirección de correo indicada");
		}

		
	}

	/**
	 * Obtener id from token.
	 *
	 * @param token Token
	 * @return big decimal
	 */
	public BigDecimal obtenerIdFromToken(String token) {

		String sql = "select id_usuario from USERS_RECUPERAR_PASS where token=?";
		Query query = this.em().createNativeQuery(sql);
		BigDecimal obj = (BigDecimal)query.setParameter(1, token).getSingleResult();
		if (obj == null) {
			return null;
		} else {
			return obj;
		}
		
	}
	
	
}
