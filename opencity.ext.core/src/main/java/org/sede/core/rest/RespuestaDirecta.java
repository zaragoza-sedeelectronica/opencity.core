package org.sede.core.rest;

public class RespuestaDirecta {
	String respuesta;

	public RespuestaDirecta(String respuesta) {
		super();
		this.respuesta = respuesta;
	}

	public StringBuilder getRespuesta() {
		return new StringBuilder(respuesta);
	}
	public StringBuilder getRespuestaComillaSimple() {
		return new StringBuilder(respuesta.replace("\"", "'"));
	}
	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	@Override
	public String toString() {
		return "RespuestaDirecta [respuesta=" + respuesta + "]";
	}
	
	
}