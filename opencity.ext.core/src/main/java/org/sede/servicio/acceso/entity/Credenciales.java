package org.sede.servicio.acceso.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


// TODO: Auto-generated Javadoc
/**
 * The Class Credenciales.
 * 
 * @autor Ayuntamiento de Zaragoza
 */
@XmlRootElement(name="credenciales")
public class Credenciales implements Serializable {
	
	/** Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** usuario. */
	private Usuario usuario;
	
	/** servicios. */
	private List<Servicio> servicios;
	
	/** lider. */
	private List<Lider> lider;
	
	/** group. */
	private List<Grupo> group;
	
	/**
	 * Gets the usuario.
	 *
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}
	
	/**
	 * Sets the usuario.
	 *
	 * @param usuario the new usuario
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * Gets the servicios.
	 *
	 * @return the servicios
	 */
	public List<Servicio> getServicios() {
		return servicios;
	}
	
	/**
	 * Sets the servicios.
	 *
	 * @param servicios the new servicios
	 */
	public void setServicios(List<Servicio> servicios) {
		this.servicios = servicios;
	}

	/**
	 * Gets the group.
	 *
	 * @return the group
	 */
	public List<Grupo> getGroup() {
		return group;
	}
	
	/**
	 * Sets the group.
	 *
	 * @param group the new group
	 */
	public void setGroup(List<Grupo> group) {
		this.group = group;
	}
	
	/**
	 * Gets the lider.
	 *
	 * @return the lider
	 */
	public List<Lider> getLider() {
		return lider;
	}
	
	/**
	 * Sets the lider.
	 *
	 * @param lider the new lider
	 */
	public void setLider(List<Lider> lider) {
		this.lider = lider;
	}
	
	/**
	 * Instantiates a new credenciales.
	 */
	public Credenciales() {
		super();
		usuario = new Usuario();
		servicios = new ArrayList<Servicio>();
		lider = new ArrayList<Lider>();
		group = new ArrayList<Grupo>();
	}
	
	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "Credenciales [usuario=" + usuario 
				+ ", servicios=" + servicios
				+ ", lider=" + lider
				+ "]";
	}

}
