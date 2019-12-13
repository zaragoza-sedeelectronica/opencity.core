package org.sede.servicio.acceso.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement(name="credenciales")
public class Credenciales implements Serializable {
	private Usuario usuario;
	private List<Servicio> servicios;
	private List<Lider> lider;
	private List<Grupo> group;
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public List<Servicio> getServicios() {
		return servicios;
	}
	public void setServicios(List<Servicio> servicios) {
		this.servicios = servicios;
	}

	public List<Grupo> getGroup() {
		return group;
	}
	public void setGroup(List<Grupo> group) {
		this.group = group;
	}
	public List<Lider> getLider() {
		return lider;
	}
	public void setLider(List<Lider> lider) {
		this.lider = lider;
	}
	public Credenciales() {
		super();
		usuario = new Usuario();
		servicios = new ArrayList<Servicio>();
		lider = new ArrayList<Lider>();
		group = new ArrayList<Grupo>();
	}
	@Override
	public String toString() {
		return "Credenciales [usuario=" + usuario 
				+ ", servicios=" + servicios
				+ ", lider=" + lider
				+ "]";
	}

}
