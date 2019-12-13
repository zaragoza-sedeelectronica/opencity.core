package org.sede.core.filter;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import net.sf.ehcache.CacheManager;

import org.sede.core.rest.CheckeoParametros;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.sede.servicio.acceso.entity.Credenciales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolver;
import org.springframework.mobile.device.LiteDeviceResolver;

public class GZipFilter extends CachingFilter {
	private static final Logger logger = LoggerFactory
			.getLogger(GZipFilter.class);
	@Override
	protected CacheManager getCacheManager() {
		return CacheManager.getInstance();
	}

	
	@Override
	protected String calculateKey(HttpServletRequest httpRequest) {
		String formato;
		try {
			formato = InterceptorPeticion.obtenerFormato(httpRequest.getRequestURI(), httpRequest.getHeader(CheckeoParametros.ACCEPTHEADER)).getExtension();
		} catch (Exception e) {
			formato = "htm"; 
		}
       	String key = calcularClaveCache(httpRequest, formato);
       	logger.info("Clave para cache: {}", key);
       	return key;
	}


	public static String calcularClaveCache(HttpServletRequest httpRequest, String formato) {
		final DeviceResolver deviceResolver = new LiteDeviceResolver();
		Device dispositivo = deviceResolver.resolveDevice(httpRequest);
		String tipo = "NORMAL";
		if (dispositivo.isMobile()) {
			tipo = "MOBILE";
		} else if (dispositivo.isTablet()) {
			tipo = "TABLET";
		}
		StringBuilder stringbuilder = new StringBuilder();
		String clientId = httpRequest.getHeader(CheckeoParametros.HEADERCLIENTID);
		if (clientId == null && httpRequest.getSession().getAttribute(CheckeoParametros.SESSIONGCZ) != null) {
			clientId = ((Credenciales)httpRequest.getSession().getAttribute(CheckeoParametros.SESSIONGCZ)).getUsuario().getLogin();
		}
		
		String ciudadano = "";
		if (httpRequest.getSession().getAttribute(CheckeoParametros.SESSIONCIUDADANO) != null) {
			ciudadano = ((Ciudadano)httpRequest.getSession().getAttribute(CheckeoParametros.SESSIONCIUDADANO)).getAccount_id(); 
		}

		StringBuilder queryString = new StringBuilder();
		@SuppressWarnings("unchecked")
		Enumeration<String> parameterNames = httpRequest.getParameterNames();
		boolean tiene = false;
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			if (!CheckeoParametros.REFRESHPARAMETER.equals(paramName)) {
				String[] paramValues = httpRequest.getParameterValues(paramName);
				for (int i = 0; i < paramValues.length; i++) {
					if (tiene) {
						queryString.append("&");
					}
					tiene = true;
					queryString.append(paramName + "=" + paramValues[i]);
				}
			}
		}
		
        stringbuilder.append(clientId).append(ciudadano).append(httpRequest.getMethod()).append(tipo).append(formato).append(httpRequest.getRequestURI()).append(queryString);
        
        return stringbuilder.toString();
	}

}
