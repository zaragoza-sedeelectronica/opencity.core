package org.sede.servicio.acceso.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Seccion implements Serializable {
	private String nombre;
	private String codigoServicio;
	private String codigoSeccion;
	private String codigoGrupoUsuario;
	private String nombreGrupoUsuario;
	private List<String> permisos;
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCodigoServicio() {
		return codigoServicio;
	}
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}
	public String getCodigoSeccion() {
		return codigoSeccion;
	}
	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	public String getCodigoGrupoUsuario() {
		return codigoGrupoUsuario;
	}
	public void setCodigoGrupoUsuario(String codigoGrupoUsuario) {
		this.codigoGrupoUsuario = codigoGrupoUsuario;
	}
	public String getNombreGrupoUsuario() {
		return nombreGrupoUsuario;
	}
	public void setNombreGrupoUsuario(String nombreGrupoUsuario) {
		this.nombreGrupoUsuario = nombreGrupoUsuario;
	}
	public List<String> getPermisos() {
		return permisos;
	}
	public void setPermisos(List<String> permisos) {
		this.permisos = permisos;
	}
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
	@Override
	public String toString() {
		return "Seccion [nombre=" + nombre + ", codigoServicio="
				+ codigoServicio + ", codigoSeccion=" + codigoSeccion
				+ ", codigoGrupoUsuario=" + codigoGrupoUsuario
				+ ", nombreGrupoUsuario=" + nombreGrupoUsuario + ", permisos="
				+ permisos + "]";
	}
	
	
	
}
