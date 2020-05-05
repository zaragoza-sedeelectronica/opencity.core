package org.sede.core.rest;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.googlecode.genericdao.search.SearchResult;

import org.sede.core.anotaciones.Interno;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.Rel;
import org.sede.core.anotaciones.ResponseClass;
import org.sede.core.anotaciones.SRSPorDefecto;
import org.sede.core.anotaciones.SoloEnDetalle;
import org.sede.core.anotaciones.SoloEnEstaEntidad;
import org.sede.core.anotaciones.SourceSRS;
import org.sede.core.utils.Propiedades;
import org.sede.servicio.acceso.entity.Credenciales;
import org.springframework.http.HttpMethod;
import org.springframework.web.method.HandlerMethod;

import com.googlecode.genericdao.search.Filter;


public class Peticion {

	private  String uri;
	private  String clientId;
	private  String hmac;
	
	private  String metodoHttp;
	
	private  Method metodo;
	private  String queryString;
	private  String tipoAcceso;
	
	private  Class<?> clase;
	
	private Class<?> claseRetorno;
	
	
	private  Map<String, String> pathParams = new HashMap<String, String>();
	private  Map<String, String[]> queryParams = new HashMap<String, String[]>();
	
	private  Map<String, String> selectedFields = new HashMap<String, String>();
	
	private  String cuerpoPeticion;
	
	private  Credenciales credenciales;
	
	private  List<String> permisosEnSeccion;
	
	private  Formato formato;
	
	private Date lastModified;
	
	private Filter filtroOpenData;
	
	private String password;
	
	private String referer;
	
	private String ip;
	
	private String srsName = null;
	
	private boolean resultsOnly = false;
	
	private boolean debug = false;
	
	private String contentType;
	
	private String tipoEtiquetado;
	
	private boolean cargandoEnVirtuoso = false;
	
	private boolean commiteadaEnMetodo = false;
	
	public Class<?> getClase() {
		return clase;
	}
	public void setClase(Class<?> clase) {
		this.clase = clase;
	}
	
	public Class<?> getClaseRetorno() {
		return claseRetorno;
	}
	public void setClaseRetorno(HandlerMethod metodo) {
		if (metodo.getMethod().isAnnotationPresent(ResponseClass.class)) {
			this.setClaseRetorno(metodo.getMethod().getAnnotation(ResponseClass.class).value());
		}
		
	}
	public void setClaseRetorno(Class<?> claseRetorno) {
		this.claseRetorno = claseRetorno;
	}
	public Method getMetodo() {
		return metodo;
	}
	public void setMetodo(Method metodo) {
		this.metodo = metodo;
	}
	public Map<String, String> getPathParams() {
		return pathParams;
	}
	public void setPathParams(Map<String, String> pathParams) {
		this.pathParams = pathParams;
	}
	public Map<String, String[]> getQueryParams() {
		return queryParams;
	}
	public void setQueryParams(Map<String, String[]> queryParams) {
		this.queryParams = queryParams;
	}
	public Credenciales getCredenciales() {
		return credenciales == null ? new Credenciales() : credenciales;
	}
	public void setCredenciales(Credenciales credenciales) {
		this.credenciales = credenciales;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getCuerpoPeticion() {
		if (this.getMetodoHttp().equals(HttpMethod.DELETE.name()) || this.getMetodoHttp().equals(HttpMethod.GET.name())) {
			return "";
		} else {
			return cuerpoPeticion;
		}
	}
	
	public void setCuerpoPeticion(String cuerpoPeticion) {
		this.cuerpoPeticion = cuerpoPeticion;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	public List<String> getPermisosEnSeccion() {
		return permisosEnSeccion == null ? new ArrayList<String>() : permisosEnSeccion;
	}
	public void setPermisosEnSeccion(List<String> permisosEnSeccion) {
		this.permisosEnSeccion = permisosEnSeccion;
	}
	public String getTipoAcceso() {
		return tipoAcceso;
	}
	public void setTipoAcceso(String tipoAcceso) {
		this.tipoAcceso = tipoAcceso;
	}
	public String getQueryString() {
		return queryString;
	}
	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}
	public String getMetodoHttp() {
		return metodoHttp;
	}
	public void setMetodoHttp(String metodoHttp) {
		this.metodoHttp = metodoHttp;
	}
	public Formato getFormato() {
		return formato;
	}
	public void setFormato(Formato formato) {
		this.formato = formato;
	}
	
	public Date getLastModified() {
		return lastModified;
	}
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	
	public Map<String, String> getSelectedFields() {
		return selectedFields;
	}
	public void setSelectedFields(String selectedFields) {
		if (selectedFields != null) {
			this.selectedFields.put(CheckeoParametros.HEADERNUMFOUND, CheckeoParametros.HEADERNUMFOUND);
			this.selectedFields.put(CheckeoParametros.PARAMROWS, CheckeoParametros.PARAMROWS);
			this.selectedFields.put(CheckeoParametros.PARAMSTART, CheckeoParametros.PARAMSTART);
			this.selectedFields.put(CheckeoParametros.PARAMRESULT, CheckeoParametros.PARAMRESULT);
			String[] filtro = selectedFields.split(",");
			for (int i = 0; i < filtro.length; i++) {
				if (filtro[i].trim().indexOf('.') >= 1) {
					this.selectedFields.put(filtro[i].trim().split("\\.")[0], filtro[i].trim().split("\\.")[0]);	
				}
				this.selectedFields.put(filtro[i].trim(), filtro[i].trim());
			}
		}
	}
	@Override
	public String toString() {
		return "Peticion ["
				+ "uri=" + uri + ", "
				+ "clientId=" + clientId + ", hmac="
				+ hmac + ", metodoHttp=" + metodoHttp + ", metodo=" + metodo
				+ ", queryString=" + queryString + ", tipoAcceso=" + tipoAcceso
				+ ", clase=" + clase + ", pathParams=" + pathParams
				+ ", queryParams=" + queryParams + ", cuerpoPeticion="
				+ cuerpoPeticion + ", credenciales=" + credenciales
				+ ", permisosEnSeccion=" + permisosEnSeccion + ", "
				+ "formato=" + formato + ", "
				+ "lastModified=" + lastModified + "]";
	}
	
	
	public boolean puedeVerCampoEnSeccion(Field field, List<String> permisosEnSecc, Method metodoRespuesta) {
		if (field.isAnnotationPresent(Interno.class) || "handler".equals(field.getName()) || "pathInterno".equals(field.getName()) || "transientField".equals(field.getName())  || "serialVersionUID".equals(field.getName())) {
			return false;
		}
		if (field.isAnnotationPresent(SoloEnEstaEntidad.class) && metodoRespuesta != null) {
			if (metodoRespuesta.isAnnotationPresent(ResponseClass.class)) {
				if (metodoRespuesta.getAnnotation(ResponseClass.class).value() == field.getDeclaringClass()) {
					// continua
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		if (field.isAnnotationPresent(SoloEnDetalle.class) && metodoRespuesta != null) {
			if (metodoRespuesta.isAnnotationPresent(ResponseClass.class)) {
				if (metodoRespuesta.getAnnotation(ResponseClass.class).entity() != null 
						&& metodoRespuesta.getAnnotation(ResponseClass.class).entity() != SearchResult.class
						) {
					// continua
				} else {
					return false;
				}
			} else {
				return false;
			}
		}
		if (field.isAnnotationPresent(Permisos.class)) {
			if (permisosEnSecc != null) {
				for (int i = 0; i < permisosEnSecc.size(); i++) {
					if (field.getAnnotation(Permisos.class).value().equals(permisosEnSecc.get(i))) {
						return true;
					}
				}
			}
			return false;
		} else {
			return true;
		}
	}
	
	public boolean puedeVerCampoEnSeccionBusqueda(Field field, List<String> permisosEnSecc) {
		if (field.isAnnotationPresent(Interno.class) || "pathInterno".equals(field.getName()) || "transientField".equals(field.getName())  || "serialVersionUID".equals(field.getName())) {
			boolean campoTransient = false;
			try {
				if (field.getDeclaringClass().isAnnotationPresent(Rel.class)) {
					@SuppressWarnings("unchecked")
					HashMap<String,String> transientField = (HashMap<String,String>)field.getDeclaringClass().getField("transientField").get(null);
					for (Map.Entry<String, String> entry : transientField.entrySet()) {
					    String value = entry.getValue();
					    if (value.equals(field.getName())) {
					    	campoTransient = true; 
					    }
					}
				}
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}	
			if (!campoTransient) {
				return false;
			}
		}
		if (field.isAnnotationPresent(Permisos.class)) {
			if (permisosEnSecc != null) {
				for (int i = 0; i < permisosEnSecc.size(); i++) {
					if (field.getAnnotation(Permisos.class).value().equals(permisosEnSecc.get(i))) {
						return true;
					}
				}
			}
			return false;
		} else {
			return true;
		}
	}
	
	
	
	public boolean quiereVerCampo(String prefijo, String field, Map<String, String> selectedFields) {
		if ((selectedFields.size() == 0 || selectedFields.get(prefijo + field) != null) && !"pathInterno".equals(field)) {
			return true;
		} else {
			return (prefijo != null && prefijo.length() > 0 && selectedFields.get(prefijo.substring(0, prefijo.lastIndexOf('.'))) != null); 
		}
	}
	
	public void establecerLastModified(Object object) {
		try {
			Field field = object.getClass().getDeclaredField("lastUpdated");
			field.setAccessible(true);
			if (field.get(object) != null && (this.getLastModified() == null || this.getLastModified().before((Date) field.get(object)))) {
				this.setLastModified((Date) field.get(object));
			} else {
				field = object.getClass().getDeclaredField("creationDate");
				field.setAccessible(true);
				if (field.get(object) != null && (this.getLastModified() == null || this.getLastModified().before((Date) field.get(object)))) {
					this.setLastModified((Date) field.get(object));
				}
			}
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		
		
	}
	
	public boolean puedeVerCampo(String campo, Class<?> entidad) {
		if (campo.indexOf('.') < 0) {
			for (Field field : entidad.getDeclaredFields()) {
				if (campo.equals(field.getName())
						&& this.puedeVerCampoEnSeccion(field, this.getPermisosEnSeccion(), this.getMetodo())) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}
	public boolean puedeVerCampoBusqueda(String campo, Class<?> entidad) {
		if (campo.indexOf('.') < 0) {
			for (Field field : entidad.getDeclaredFields()) {
				if (campo.equals(field.getName())
						&& this.puedeVerCampoEnSeccionBusqueda(field, this.getPermisosEnSeccion())) {
					return true;
				}
			}
			return false;
		} else {
			return true;
		}
	}

	
	
	/*Sólo se modifica desde el api, porque se hace una transformación a XML interna*/
	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}
	public String getSrsName() {
		if (this.srsName == null) {
			String srs = CheckeoParametros.SRSUTM30N;
			if (this.getMetodo().getDeclaringClass().isAnnotationPresent(SRSPorDefecto.class)) {
				srs = this.getMetodo().getDeclaringClass().getAnnotation(SRSPorDefecto.class).value();
			}
			return this.getQueryParams().get(CheckeoParametros.PARAMSRS) == null 
					? srs
					: (this.getQueryParams().get(CheckeoParametros.PARAMSRS)[0].equals(CheckeoParametros.SRSWGS84) 
							? CheckeoParametros.SRSWGS84
							: (this.getQueryParams().get(CheckeoParametros.PARAMSRS)[0].equals(CheckeoParametros.SRSETRS89) ?
								CheckeoParametros.SRSETRS89 : CheckeoParametros.SRSUTM30N));
		} else {
			return srsName; 
		}
	}
	public Filter getFiltroOpenData() {
		return filtroOpenData;
	}
	public void setFiltroOpenData(Filter filtroOpenData) {
		this.filtroOpenData = filtroOpenData;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getReferer() {
		return referer;
	}
	public void setReferer(String referer) {
		this.referer = referer;
	}
	public boolean desdeOrigen() {
		String[] paths = Propiedades.getPathAllowed().split(",");
		for (int i = 0; i < paths.length; i++) {
			if (referer.indexOf(paths[i]) >= 0) {
				return true;
			}
		}
		return false;
	}
	public boolean isResultsOnly() {
		return resultsOnly;
	}
	public void setResultsOnly(boolean resultsOnly) {
		this.resultsOnly = resultsOnly;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public boolean isDebug() {
		return debug;
	}
	public void setDebug(boolean debug) {
		this.debug = debug;
	}
	public String getPathInformes(String informe) {
//		 FIXME path para informes
		return null;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public boolean isCommiteadaEnMetodo() {
		return commiteadaEnMetodo;
	}
	public void setCommiteadaEnMetodo(boolean commiteadaEnMetodo) {
		this.commiteadaEnMetodo = commiteadaEnMetodo;
	}
	public boolean isCargandoEnVirtuoso() {
		return cargandoEnVirtuoso;
	}
	public void setCargandoEnVirtuoso(boolean cargandoEnVirtuoso) {
		this.cargandoEnVirtuoso = cargandoEnVirtuoso;
	}
	public String getTipoEtiquetado() {
		return tipoEtiquetado;
	}
	public void setTipoEtiquetado(String tipoEtiquetado) {
		this.tipoEtiquetado = tipoEtiquetado;
	}
	public String getSourceSrs() {
		if (this.getClaseRetorno() != null && this.getClaseRetorno().isAnnotationPresent(SourceSRS.class)) {
			return this.getClaseRetorno().getAnnotation(SourceSRS.class).value();
		} else {
			return CheckeoParametros.SRSUTM30N;
		}
	}
	

}
