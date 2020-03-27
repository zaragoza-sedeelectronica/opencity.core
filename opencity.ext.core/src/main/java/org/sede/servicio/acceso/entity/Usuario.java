package org.sede.servicio.acceso.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.URL;
import org.sede.core.utils.AESSec;


/**
 * The Class Usuario.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
@XmlRootElement(name="usuario")
public class Usuario implements Serializable {
	
	/** Constant serialVersionUID. */
	private static final long serialVersionUID = -3477716386553420005L;

	/** Constant PROPWEB. */
	public static final String PROPWEB = "sitioWebReutilizador";
	
	/** id. */
	private Long id;
	
	/** login. */
	@NotNull
	private String login;
	
	/** nombre. */
	@NotNull @Size(min=2,max=100)
	private String nombre;
	
	/** apellido 1. */
	@NotNull @Size(min=2,max=100)
	private String apellido1;
	
	/** apellido 2. */
	@NotNull @Size(min=2,max=100)
	private String apellido2;
	
	/** secret key. */
	private String secretKey;
	
	/** email. */
	@NotNull @Email
	private String email;
	
	/** web. */
	@URL
	private String web;
	
	/** propiedades. */
	private HashMap<String,String> propiedades;
	
	/** servicio por defecto. */
	private String servicioPorDefecto;
	
	/** seccion por defecto. */
	private String seccionPorDefecto;
	
	/** intentos fallidos. */
	private Integer intentosFallidos;
	
	/** fecha ultimo acceso. */
	private Date fechaUltimoAcceso;
	
	/** publicado. */
	private String publicado;
	
	/** bloqueado. */
	private String bloqueado;
	
	/** administrador. */
	private String administrador;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	
	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(Long id) {
		this.id = id;
	}
	
	/**
	 * Gets the login.
	 *
	 * @return the login
	 */
	public String getLogin() {
		return login;
	}
	
	/**
	 * Sets the login.
	 *
	 * @param login the new login
	 */
	public void setLogin(String login) {
		this.login = login;
	}
	
	/**
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}
	
	/**
	 * Sets the nombre.
	 *
	 * @param nombre the new nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	/**
	 * Gets the apellido 1.
	 *
	 * @return the apellido 1
	 */
	public String getApellido1() {
		return apellido1;
	}
	
	/**
	 * Sets the apellido 1.
	 *
	 * @param apellido1 the new apellido 1
	 */
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	
	/**
	 * Gets the apellido 2.
	 *
	 * @return the apellido 2
	 */
	public String getApellido2() {
		return apellido2;
	}
	
	/**
	 * Sets the apellido 2.
	 *
	 * @param apellido2 the new apellido 2
	 */
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	
	/**
	 * Gets the email.
	 *
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}
	
	/**
	 * Sets the email.
	 *
	 * @param email the new email
	 */
	public void setEmail(String email) {
		this.email = email;
	}
	
	/**
	 * Gets the servicio por defecto.
	 *
	 * @return the servicio por defecto
	 */
	public String getServicioPorDefecto() {
		return servicioPorDefecto;
	}
	
	/**
	 * Sets the servicio por defecto.
	 *
	 * @param servicioPorDefecto the new servicio por defecto
	 */
	public void setServicioPorDefecto(String servicioPorDefecto) {
		this.servicioPorDefecto = servicioPorDefecto;
	}
	
	/**
	 * Gets the seccion por defecto.
	 *
	 * @return the seccion por defecto
	 */
	public String getSeccionPorDefecto() {
		return seccionPorDefecto;
	}
	
	/**
	 * Sets the seccion por defecto.
	 *
	 * @param seccionPorDefecto the new seccion por defecto
	 */
	public void setSeccionPorDefecto(String seccionPorDefecto) {
		this.seccionPorDefecto = seccionPorDefecto;
	}
	
	/**
	 * Gets the intentos fallidos.
	 *
	 * @return the intentos fallidos
	 */
	public Integer getIntentosFallidos() {
		return intentosFallidos;
	}
	
	/**
	 * Sets the intentos fallidos.
	 *
	 * @param intentosFallidos the new intentos fallidos
	 */
	public void setIntentosFallidos(Integer intentosFallidos) {
		this.intentosFallidos = intentosFallidos;
	}
	
	/**
	 * Gets the fecha ultimo acceso.
	 *
	 * @return the fecha ultimo acceso
	 */
	public Date getFechaUltimoAcceso() {
		return fechaUltimoAcceso;
	}
	
	/**
	 * Sets the fecha ultimo acceso.
	 *
	 * @param fechaUltimoAcceso the new fecha ultimo acceso
	 */
	public void setFechaUltimoAcceso(Date fechaUltimoAcceso) {
		this.fechaUltimoAcceso = fechaUltimoAcceso;
	}
	
	/**
	 * Gets the publicado.
	 *
	 * @return the publicado
	 */
	public String getPublicado() {
		return publicado;
	}
	
	/**
	 * Sets the publicado.
	 *
	 * @param publicado the new publicado
	 */
	public void setPublicado(String publicado) {
		this.publicado = publicado;
	}
	
	/**
	 * Gets the bloqueado.
	 *
	 * @return the bloqueado
	 */
	public String getBloqueado() {
		return bloqueado;
	}
	
	/**
	 * Sets the bloqueado.
	 *
	 * @param bloqueado the new bloqueado
	 */
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	/**
	 * Gets the secret key.
	 *
	 * @return the secret key
	 */
	public String getSecretKey() {
		return secretKey;
	}
	
	/**
	 * Sets the secret key.
	 *
	 * @param secretKey the new secret key
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	/**
	 * Gets the propiedades.
	 *
	 * @return the propiedades
	 */
	public HashMap<String, String> getPropiedades() {
		return propiedades;
	}
	
	/**
	 * Sets the propiedades.
	 *
	 * @param propiedades Propiedades
	 */
	public void setPropiedades(HashMap<String, String> propiedades) {
		this.propiedades = propiedades;
	}
	
	/**
	 * Gets the web.
	 *
	 * @return the web
	 */
	public String getWeb() {
		if (this.getPropiedades() != null && this.getPropiedades().get(Usuario.PROPWEB) != null && !"".equals(this.getPropiedades().get(Usuario.PROPWEB))) {
			return this.getPropiedades().get(Usuario.PROPWEB);
		} else {
			return null;
		}
	}
	
	/**
	 * Sets the web.
	 *
	 * @param web the new web
	 */
	public void setWeb(String web) {
		if (this.getPropiedades() == null) {
			this.setPropiedades(new HashMap<String, String>()); 
		}
		this.getPropiedades().put(Usuario.PROPWEB, web);
	}
	
	/**
	 * Instantiates a new usuario.
	 *
	 * @param id Id
	 * @param login Login
	 * @param nombre Nombre
	 * @param apellido1 Apellido 1
	 * @param apellido2 Apellido 2
	 * @param secretKey Secret key
	 * @param email Email
	 * @param servicioPorDefecto Servicio por defecto
	 * @param seccionPorDefecto Seccion por defecto
	 * @param intentosFallidos Intentos fallidos
	 * @param fechaUltimoAcceso Fecha ultimo acceso
	 * @param publicado Publicado
	 * @param bloqueado Bloqueado
	 * @param administrador Administrador
	 */
	public Usuario(BigDecimal id, String login, String nombre, String apellido1,
			String apellido2, String secretKey, String email,
			String servicioPorDefecto, String seccionPorDefecto,
			Integer intentosFallidos, Date fechaUltimoAcceso, String publicado,
			String bloqueado, String administrador) {
		super();
		this.id = id.longValue();
		this.login = login;
		this.nombre = nombre;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		try {
			this.secretKey = secretKey == null ? null : AESSec.decrypt(secretKey);
		} catch (Exception e) {
			;
		}
		this.email = email;
		this.servicioPorDefecto = servicioPorDefecto;
		this.seccionPorDefecto = seccionPorDefecto;
		this.intentosFallidos = intentosFallidos;
		this.fechaUltimoAcceso = fechaUltimoAcceso;
		this.publicado = publicado;
		this.bloqueado = bloqueado;
		this.administrador = administrador;
	}

	/**
	 * Gets the administrador.
	 *
	 * @return the administrador
	 */
	public String getAdministrador() {
		return administrador;
	}
	
	/**
	 * Sets the administrador.
	 *
	 * @param administrador the new administrador
	 */
	public void setAdministrador(String administrador) {
		this.administrador = administrador;
	}
	
	/**
	 * Instantiates a new usuario.
	 */
	public Usuario() {
		super();
	}
	
	/**
	 * Hash code.
	 *
	 * @return int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : Math.abs(login.hashCode()));
		return result;
	}
	
	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", login=" + login + ", nombre=" + nombre
				+ ", apellido1=" + apellido1 + ", apellido2=" + apellido2
				+ ", secretKey=" + secretKey + ", email=" + email + ", web="
				+ web + ", propiedades=" + propiedades
				+ ", servicioPorDefecto=" + servicioPorDefecto
				+ ", seccionPorDefecto=" + seccionPorDefecto
				+ ", intentosFallidos=" + intentosFallidos
				+ ", fechaUltimoAcceso=" + fechaUltimoAcceso + ", publicado="
				+ publicado + ", bloqueado=" + bloqueado + "]";
	}

}
