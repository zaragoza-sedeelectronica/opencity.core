package org.sede.servicio.acceso.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Servicio implements Serializable {

	private String nombre;
	private String codigoServicio;
	private String codigoSeccionDefecto;
	private List<Seccion> secciones;
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
	public String getCodigoSeccionDefecto() {
		return codigoSeccionDefecto;
	}
	public void setCodigoSeccionDefecto(String codigoSeccionDefecto) {
		this.codigoSeccionDefecto = codigoSeccionDefecto;
	}
	public List<Seccion> getSecciones() {
		return secciones;
	}
	public void setSecciones(List<Seccion> secciones) {
		this.secciones = secciones;
	}
	public Servicio(String nombre, String codigoServicio,
			String codigoSeccionDefecto) {
		super();
		this.nombre = nombre;
		this.codigoServicio = codigoServicio;
		this.codigoSeccionDefecto = codigoSeccionDefecto;
		this.secciones = new ArrayList<Seccion>(); 
	}
	@Override
	public String toString() {
		return "Servicio [nombre=" + nombre + ", codigoServicio="
				+ codigoServicio + ", codigoSeccionDefecto="
				+ codigoSeccionDefecto + ", secciones=" + secciones + "]";
	}
	
	
}
