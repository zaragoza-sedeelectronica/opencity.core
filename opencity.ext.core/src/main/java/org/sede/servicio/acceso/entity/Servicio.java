package org.sede.servicio.acceso.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Class Servicio.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
public class Servicio implements Serializable {

	/** Constant serialVersionUID. */
	private static final long serialVersionUID = 3324700323911699528L;
	
	/** nombre. */
	private String nombre;
	
	/** codigo servicio. */
	private String codigoServicio;
	
	/** codigo seccion defecto. */
	private String codigoSeccionDefecto;
	
	/** secciones. */
	private List<Seccion> secciones;
	
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
	 * Gets the codigo servicio.
	 *
	 * @return the codigo servicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}
	
	/**
	 * Sets the codigo servicio.
	 *
	 * @param codigoServicio the new codigo servicio
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	
	/**
	 * Gets the codigo seccion defecto.
	 *
	 * @return the codigo seccion defecto
	 */
	public String getCodigoSeccionDefecto() {
		return codigoSeccionDefecto;
	}
	
	/**
	 * Sets the codigo seccion defecto.
	 *
	 * @param codigoSeccionDefecto the new codigo seccion defecto
	 */
	public void setCodigoSeccionDefecto(String codigoSeccionDefecto) {
		this.codigoSeccionDefecto = codigoSeccionDefecto;
	}
	
	/**
	 * Gets the secciones.
	 *
	 * @return the secciones
	 */
	public List<Seccion> getSecciones() {
		return secciones;
	}
	
	/**
	 * Sets the secciones.
	 *
	 * @param secciones the new secciones
	 */
	public void setSecciones(List<Seccion> secciones) {
		this.secciones = secciones;
	}
	
	/**
	 * Instantiates a new servicio.
	 *
	 * @param nombre Nombre
	 * @param codigoServicio Codigo servicio
	 * @param codigoSeccionDefecto Codigo seccion defecto
	 */
	public Servicio(String nombre, String codigoServicio,
			String codigoSeccionDefecto) {
		super();
		this.nombre = nombre;
		this.codigoServicio = codigoServicio;
		this.codigoSeccionDefecto = codigoSeccionDefecto;
		this.secciones = new ArrayList<Seccion>(); 
	}
	
	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "Servicio [nombre=" + nombre + ", codigoServicio="
				+ codigoServicio + ", codigoSeccionDefecto="
				+ codigoSeccionDefecto + ", secciones=" + secciones + "]";
	}
	
	
}
