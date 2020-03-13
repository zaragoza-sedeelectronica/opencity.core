package org.sede.servicio.acceso.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Seccion.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 *
 */
public class Seccion implements Serializable {
	
	/** Constant serialVersionUID. */
	private static final long serialVersionUID = -938579884885718397L;
	
	/** nombre. */
	private String nombre;
	
	/** codigo servicio. */
	private String codigoServicio;
	
	/** codigo seccion. */
	private String codigoSeccion;
	
	/** codigo grupo usuario. */
	private String codigoGrupoUsuario;
	
	/** nombre grupo usuario. */
	private String nombreGrupoUsuario;
	
	/** permisos. */
	private List<String> permisos;
	
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
	 * Gets the codigo seccion.
	 *
	 * @return the codigo seccion
	 */
	public String getCodigoSeccion() {
		return codigoSeccion;
	}
	
	/**
	 * Sets the codigo seccion.
	 *
	 * @param codigoSeccion the new codigo seccion
	 */
	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	/**
	 * Gets the codigo grupo usuario.
	 *
	 * @return the codigo grupo usuario
	 */
	public String getCodigoGrupoUsuario() {
		return codigoGrupoUsuario;
	}
	
	/**
	 * Sets the codigo grupo usuario.
	 *
	 * @param codigoGrupoUsuario the new codigo grupo usuario
	 */
	public void setCodigoGrupoUsuario(String codigoGrupoUsuario) {
		this.codigoGrupoUsuario = codigoGrupoUsuario;
	}
	
	/**
	 * Gets the nombre grupo usuario.
	 *
	 * @return the nombre grupo usuario
	 */
	public String getNombreGrupoUsuario() {
		return nombreGrupoUsuario;
	}
	
	/**
	 * Sets the nombre grupo usuario.
	 *
	 * @param nombreGrupoUsuario the new nombre grupo usuario
	 */
	public void setNombreGrupoUsuario(String nombreGrupoUsuario) {
		this.nombreGrupoUsuario = nombreGrupoUsuario;
	}
	
	/**
	 * Gets the permisos.
	 *
	 * @return the permisos
	 */
	public List<String> getPermisos() {
		return permisos;
	}
	
	/**
	 * Sets the permisos.
	 *
	 * @param permisos the new permisos
	 */
	public void setPermisos(List<String> permisos) {
		this.permisos = permisos;
	}
	
	/**
	 * Instantiates a new seccion.
	 *
	 * @param nombre Nombre
	 * @param codigoServicio Codigo servicio
	 * @param codigoSeccion Codigo seccion
	 * @param codigoGrupoUsuario Codigo grupo usuario
	 * @param nombreGrupoUsuario Nombre grupo usuario
	 */
	public Seccion(String nombre, String codigoServicio, String codigoSeccion,
			String codigoGrupoUsuario, String nombreGrupoUsuario) {
		super();
		this.nombre = nombre;
		this.codigoServicio = codigoServicio;
		this.codigoSeccion = codigoSeccion;
		this.codigoGrupoUsuario = codigoGrupoUsuario;
		this.nombreGrupoUsuario = nombreGrupoUsuario;
		permisos = new ArrayList<String>();
	}
	
	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "Seccion [nombre=" + nombre + ", codigoServicio="
				+ codigoServicio + ", codigoSeccion=" + codigoSeccion
				+ ", codigoGrupoUsuario=" + codigoGrupoUsuario
				+ ", nombreGrupoUsuario=" + nombreGrupoUsuario + ", permisos="
				+ permisos + "]";
	}
	
	
	
}
