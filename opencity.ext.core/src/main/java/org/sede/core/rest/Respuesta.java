package org.sede.core.rest;

import org.sede.core.rest.json.JSONArray;
import org.sede.core.rest.json.JSONObject;
import org.sede.servicio.ModelAttr;

public class Respuesta {
	private int status;
	private String contenido;
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getContenido() {
		return contenido;
	}
	public void setContenido(String contenido) {
		this.contenido = contenido;
	}
	public JSONObject getJson() {
		return new JSONObject(contenido);
	}
	public JSONArray getJsonArray() {
		return new JSONArray(contenido);
	}
	public String getMensajeError() {
		String mensaje = (this.getJson()).has(ModelAttr.MENSAJE) ? (this.getJson()).getString(ModelAttr.MENSAJE)
				: (this.getJson()).has("error")
						&& this.getJson().get("error") instanceof String ? 
								( this.getJson()).getString("error") 
								: "";
		if ("".equals(mensaje) && (this.getJson()).has("error")) {
			JSONArray errores = (this.getJson()).getJSONArray("error");
			StringBuilder err = new StringBuilder();
			if (errores != null) {
				err.append("<div class=\"alert alert-error error\" id=\"msg\">Errores:<ul>");
				for (int i = 0; i < errores.length(); i++) {
					JSONObject obj = errores.getJSONObject(i);
					err.append("<li><strong>" + JSONObject.getNames(obj)[0] + "</strong>: " + trataError(obj.getString(JSONObject.getNames(obj)[0])) + "</li>");
				}
				err.append("</ul></div>");
			}
			mensaje = err.toString();
		} else {
			if (!"".equals(mensaje)) {
				mensaje = "<div class=\"alert alert-error error\" id=\"msg\">" + mensaje + "</div>";
			}
		}
		return mensaje;
	
	}
	private String trataError(String mensaje) {
		return "<![CDATA[" + mensaje + "]]>";
	}
	@Override
	public String toString() {
		return "Respuesta [status=" + status + ", contenido=" + contenido + "]";
	}
}
