package org.sede.core.rest;

import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.Rdf;
import org.sede.servicio.ModelAttr;

@XmlRootElement(name=ModelAttr.MENSAJE)
public class Mensaje {
	private int status;
	private Integer code = null;
	@Rdf(contexto = Context.ZAR, propiedad="msg")
	public String mensaje;
	
	public Mensaje(int status, String mensaje) {
		super();
		this.status = status;
		this.mensaje = mensaje;
	}
	public Mensaje(int status, Integer code, String mensaje) {
		super();
		this.status = status;
		this.code = code;
		this.mensaje = mensaje;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMensaje() {
		return mensaje;
	}
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	@Override
	public String toString() {
		return "Mensaje [status=" + status + ", code=" + code + ", mensaje="
				+ mensaje + "]";
	}
	
}