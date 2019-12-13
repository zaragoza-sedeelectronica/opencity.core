package org.sede.core.filter;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.IOUtils;
import org.sede.core.anotaciones.Cache;
import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.PermisosUser;
import org.sede.core.exception.ErrorEnPeticionHMAC;
import org.sede.core.exception.FormatoNoSoportadoException;
import org.sede.core.exception.SinCredencialesDefinidas;
import org.sede.core.exception.SinPermisoParaEjecutar;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Formato;
import org.sede.core.rest.Hmac;
import org.sede.core.rest.MimeTypes;
import org.sede.core.rest.Peticion;
import org.sede.core.rest.Role;
import org.sede.core.utils.Funciones;
import org.sede.servicio.acceso.dao.Autorizacion;
import org.sede.servicio.acceso.dao.ComprobarIntegridad;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.userrequirements.RequirementsInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class InterceptorPeticion extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(InterceptorPeticion.class);
	
	@Autowired
	ComprobarIntegridad integridad;
	
	
	@Autowired
	private Map<String, RequirementsInterface> requirements;
	
	@Override
	public boolean preHandle(HttpServletRequest httprequest, HttpServletResponse response, Object handler) throws ServletException, FormatoNoSoportadoException, IOException, ErrorEnPeticionHMAC, SinCredencialesDefinidas, SinPermisoParaEjecutar {
		if (handler instanceof HandlerMethod) {
			HandlerMethod metodo = (HandlerMethod)handler;
			httprequest.setAttribute(CheckeoParametros.NOCACHE, cachearMetodo(metodo));
			httprequest.setAttribute(CheckeoParametros.ACTUAL_CACHE, metodo.getMethod().isAnnotationPresent(Cache.class) ? metodo.getMethod().getAnnotation(Cache.class).value() : null);
			boolean accesoApi = metodo.getMethod().isAnnotationPresent(ResponseBody.class);
			logger.info("Metodo: {} acceso api: {}", metodo.getMethod().getName(), accesoApi);
			
			String clientId = httprequest.getHeader(CheckeoParametros.HEADERCLIENTID);
			if (clientId == null && httprequest.getSession().getAttribute(CheckeoParametros.SESSIONGCZ) != null) {
				clientId = ((Credenciales)httprequest.getSession().getAttribute(CheckeoParametros.SESSIONGCZ)).getUsuario().getLogin();
				response.setHeader(CheckeoParametros.HEADERCLIENTID, clientId);
			}
			if (httprequest.getSession().getAttribute(CheckeoParametros.SESSIONCIUDADANO) != null) {
				Ciudadano c = Funciones.getUser(httprequest);
				response.setHeader(CheckeoParametros.HEADERCLIENTID, CheckeoParametros.SESSIONCIUDADANO + "_" + c.getId());
			}
			@SuppressWarnings("unchecked")
			Peticion peticion = establecerDatosPeticion(
					(HandlerMethod)handler,
					clientId, 
					httprequest.getHeader(CheckeoParametros.HEADERHMAC),
					httprequest.getHeader(CheckeoParametros.HEADERPASSWORD),
					httprequest.getRequestURI(),
					httprequest.getQueryString(), 
					(Map<String, String[]>)httprequest.getParameterMap(), 
					httprequest.getMethod(), 
					httprequest.getParameter(CheckeoParametros.PARAMFILTROCAMPOS),
					IOUtils.toString(httprequest.getInputStream(), CharEncoding.UTF_8),
					httprequest.getHeader(CheckeoParametros.ACCEPTHEADER), httprequest.getHeader(CheckeoParametros.REFERER), httprequest.getParameter(CheckeoParametros.RESULTSONLY), httprequest.getParameter(CheckeoParametros.DEBUG) != null, httprequest.getContentType(),
					Funciones.getIpUser(httprequest), httprequest.getParameter(CheckeoParametros.PARAMRESPUESTA));
			
			if (httprequest.getSession().getAttribute(CheckeoParametros.SESSIONGCZ) != null) {
				peticion.setCredenciales((Credenciales)httprequest.getSession().getAttribute(CheckeoParametros.SESSIONGCZ));
				String contenidoCompleto  = clientId + peticion.getMetodoHttp() + peticion.getUri() + peticion.getQueryString() + ""/*body post*/;
				peticion.setHmac(Hmac.calcular(contenidoCompleto, peticion.getCredenciales().getUsuario().getSecretKey()));
				if (peticion.getMetodo().getDeclaringClass().isAnnotationPresent(Gcz.class)) {
					Gcz servicioSeccion = peticion.getMetodo().getDeclaringClass().getAnnotation(Gcz.class);
					peticion.setPermisosEnSeccion(Autorizacion.obtenerPermisosSeccion(peticion.getCredenciales(), servicioSeccion.servicio(), servicioSeccion.seccion()));
				}
			} else {
				peticion.setPermisosEnSeccion(new ArrayList<String>());
			}
			
			httprequest.setAttribute(CheckeoParametros.ATTRPETICION, peticion);
			
			if (accesoApi) {
				peticion.setClaseRetorno(metodo);
				integridad.revisarMensaje(Funciones.getPeticion(), httprequest.getSession().getAttribute(CheckeoParametros.SESSIONGCZ) != null);
				Autorizacion autorizacion = new Autorizacion();
				
				autorizacion.revisarPeticion(Funciones.getPeticion(), httprequest);
			} else {
				boolean accesoPermisos = metodo.getMethod().isAnnotationPresent(Permisos.class);
				boolean accesoPermisosUsuario = metodo.getMethod().isAnnotationPresent(PermisosUser.class);
				if (accesoPermisos) {
					if (httprequest.getSession().getAttribute(CheckeoParametros.SESSIONGCZ) == null) {
						logger.info("REQUIERE PERMISO risp: {}:CONTEXT PATH:{}", httprequest.getRequestURI(), httprequest.getContextPath());
						response.sendRedirect(Funciones.getPathSecure(httprequest) + "/acceso?&t=a&r=" + URLEncoder.encode(httprequest.getRequestURI(), CharEncoding.UTF_8));
						return false;	
					}
					Autorizacion autorizacion = new Autorizacion();
					autorizacion.revisarPeticion(Funciones.getPeticion(), httprequest);
				}
				if (accesoPermisosUsuario) {
					if (httprequest.getSession().getAttribute(CheckeoParametros.SESSIONCIUDADANO) == null) {
						logger.info("REQUIERE PERMISO de Usuario: {}:CONTEXT PATH:{}", httprequest.getRequestURI(), httprequest.getContextPath());
						response.sendRedirect(Funciones.getPathSecure(httprequest) + "/acceso?r=" + URLEncoder.encode(httprequest.getRequestURI(), CharEncoding.UTF_8));
						return false;
					} else {
						if (metodo.getMethod().getAnnotation(PermisosUser.class).requirements().length > 0) {
							for (int i = 0; i < metodo.getMethod().getAnnotation(PermisosUser.class).requirements().length; i++) {
								Class<?> clase = metodo.getMethod().getAnnotation(PermisosUser.class).requirements()[i];
								logger.info("Requiere: {}", clase.getCanonicalName());
								RequirementsInterface req = requirements.get(clase.getSimpleName().toLowerCase());
								req.setMethod(metodo);
								if (!req.validate(Funciones.getUser(httprequest))) {
									if (httprequest.getSession().getAttribute(CheckeoParametros.REQUIREMENTESATTR) == null) {
										httprequest.getSession().setAttribute(CheckeoParametros.REQUIREMENTESATTR, metodo.getMethod().getAnnotation(PermisosUser.class).requirements());
									}
									
									StringBuilder queryString = new StringBuilder();
									@SuppressWarnings("unchecked")
									Enumeration<String> parameterNames = httprequest.getParameterNames();
									boolean tiene = false;
									while (parameterNames.hasMoreElements()) {
										String paramName = parameterNames.nextElement();
										if (!CheckeoParametros.REFRESHPARAMETER.equals(paramName)) {
											String[] paramValues = httprequest.getParameterValues(paramName);
											for (int j = 0; j < paramValues.length; j++) {
												if (tiene) {
													queryString.append("&");
												} else {
													queryString.append("?");
												}
												tiene = true;
												queryString.append(paramName + "=" + paramValues[j]);
											}
										}
									}
									logger.info("REQUIERE PERMISO risp: " + httprequest.getRequestURI() + queryString.toString() + ":CONTEXT PATH:" + httprequest.getContextPath());
									response.sendRedirect(Funciones.getPathSecure(httprequest) + "/acceso/requirements?r=" + URLEncoder.encode(httprequest.getRequestURI() + queryString.toString(), CharEncoding.UTF_8));
									return false;
								}
							
							}
						}
					}
				}
			}
		}
		return true;
			
	}
	
	private boolean cachearMetodo(HandlerMethod metodo) {
		return metodo.getMethod().isAnnotationPresent(Cache.class) 
				&& !metodo.getMethod().isAnnotationPresent(Permisos.class) 
				&& !metodo.getMethod().isAnnotationPresent(PermisosUser.class);
	}
	
	private Peticion establecerDatosPeticion(HandlerMethod handler, String clientId,
			String hmac, String password, String uri, String queryString,
			Map<String, String[]> parameterMap, String metodoHttp, String fl,
			String bodyPeticion, String acceptHeader, String referer, String resultsOnly, boolean debug, String contentType, String ip, String tipoEtiquetado)
			throws FormatoNoSoportadoException {
		Peticion peticion = new Peticion();
		peticion.setUri(uri);
		peticion.setSelectedFields(fl);
		peticion.setFormato(obtenerFormato(uri, acceptHeader));
		peticion.setMetodoHttp(metodoHttp);
		peticion.setClientId(clientId);
		peticion.setHmac(hmac);
		peticion.setPassword(password);
		peticion.setQueryParams((Map<String, String[]>)parameterMap);
		peticion.setQueryString((queryString == null || queryString.trim().length() == 0) ? "" : "?" + queryString);
		peticion.setCuerpoPeticion(bodyPeticion);
		peticion.setReferer(referer == null ? "" : referer);
		peticion.setContentType(contentType);
		peticion.setTipoEtiquetado(tipoEtiquetado == null ? CheckeoParametros.RESPUESTAHTML : tipoEtiquetado);
		
		// FIXME revisar cuando pongamos el acceso con credenciales se tiene que asociar el valor aquí o en otro sitio
		peticion.setTipoAcceso(Role.PUBLICO);

		peticion.setMetodo(handler.getMethod());
		
		boolean soloResultados;
		if (resultsOnly == null){
			soloResultados = false;
		} else if ("true".equals(resultsOnly)){
			soloResultados = true;
		} else {
			soloResultados = false;
		}
		peticion.setDebug(debug);
		peticion.setResultsOnly(soloResultados);
		peticion.setIp(ip);
		return peticion;
	}
	
	public static Formato obtenerFormato(final String uri, final String acceptHeader)
			throws FormatoNoSoportadoException {
		// Por defecto se devuelve la representación en HTML
		
		if ("*/*".equals(acceptHeader)) {
			return null;
		}
		Formato retorno = MimeTypes.listado.get(MimeTypes.HTML);
		if (uri.indexOf('.') >= 1) {
			retorno = MimeTypes.extensiones.get(uri.substring(uri.lastIndexOf('.') + 1,uri.length()));
			if (retorno == null  && uri.indexOf("/geometry/") >= 1) {
				retorno = MimeTypes.listado.get(MimeTypes.HTML);
			}
		} else if (acceptHeader != null) {
			String[] acceptLanguage = acceptHeader.split(",");
			for (int i = 0; i < acceptLanguage.length; i ++) {
				if (MimeTypes.listado.get(acceptLanguage[i].trim()) != null) {
					retorno = MimeTypes.listado.get(acceptLanguage[i].trim());
					break;
				}
			}
		}
		if (retorno != null) {
			if (retorno.getTransformador() == null && !retorno.isParaInforme()) {
				return null;
			} else {
				return retorno;
			}
		} else {
			throw new FormatoNoSoportadoException();
		}

	}
}
