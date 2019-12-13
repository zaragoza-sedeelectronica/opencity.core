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


@XmlRootElement(name="usuario")
public class Usuario implements Serializable {
	public static final String PROPWEB = "sitioWebReutilizador";
	private Long id;
	@NotNull
	private String login;
	@NotNull @Size(min=2,max=100)
	private String nombre;
	@NotNull @Size(min=2,max=100)
	private String apellido1;
	@NotNull @Size(min=2,max=100)
	private String apellido2;
	private String secretKey;
	@NotNull @Email
	private String email;
	@URL
	private String web;
	
	private HashMap<String,String> propiedades;
	
	private String servicioPorDefecto;
	private String seccionPorDefecto;
	private Integer intentosFallidos;
	private Date fechaUltimoAcceso;
	private String publicado;
	private String bloqueado;
	private String administrador;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido1() {
		return apellido1;
	}
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}
	public String getApellido2() {
		return apellido2;
	}
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getServicioPorDefecto() {
		return servicioPorDefecto;
	}
	public void setServicioPorDefecto(String servicioPorDefecto) {
		this.servicioPorDefecto = servicioPorDefecto;
	}
	public String getSeccionPorDefecto() {
		return seccionPorDefecto;
	}
	public void setSeccionPorDefecto(String seccionPorDefecto) {
		this.seccionPorDefecto = seccionPorDefecto;
	}
	public Integer getIntentosFallidos() {
		return intentosFallidos;
	}
	public void setIntentosFallidos(Integer intentosFallidos) {
		this.intentosFallidos = intentosFallidos;
	}
	public Date getFechaUltimoAcceso() {
		return fechaUltimoAcceso;
	}
	public void setFechaUltimoAcceso(Date fechaUltimoAcceso) {
		this.fechaUltimoAcceso = fechaUltimoAcceso;
	}
	public String getPublicado() {
		return publicado;
	}
	public void setPublicado(String publicado) {
		this.publicado = publicado;
	}
	
	public String getBloqueado() {
		return bloqueado;
	}
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}
	public String getSecretKey() {
		return secretKey;
	}
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	public HashMap<String, String> getPropiedades() {
		return propiedades;
	}
	public void setPropiedades(HashMap<String, String> propiedades) {
		this.propiedades = propiedades;
	}
	
	public String getWeb() {
		if (this.getPropiedades() != null && this.getPropiedades().get(Usuario.PROPWEB) != null && !"".equals(this.getPropiedades().get(Usuario.PROPWEB))) {
			return this.getPropiedades().get(Usuario.PROPWEB);
		} else {
			return null;
		}
	}
	public void setWeb(String web) {
		if (this.getPropiedades() == null) {
			this.setPropiedades(new HashMap<String, String>()); 
		}
		this.getPropiedades().put(Usuario.PROPWEB, web);
	}
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

	public String getAdministrador() {
		return administrador;
	}
	public void setAdministrador(String administrador) {
		this.administrador = administrador;
	}
	public Usuario() {
		super();
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((login == null) ? 0 : Math.abs(login.hashCode()));
		return result;
	}
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
