package org.sede.core.utils;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;

public class Propiedades {
	/**
	 * Nombre del fichero de propiedades
	 */
	private static final String BUNDLE_NAME = "application"; //$NON-NLS-1$
	/**
	 * Recurso
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);
	/**
	 * Constructor
	 *
	 */
	private Propiedades() {
		super();
	}
	/**
	 * 
	 * @param key Elemento a recoger
	 * @return valor del elemento en el fichero
	 */
	public static String getString(final String key) {
		String retorno;
		try {
			retorno = RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			retorno = '!' + key + '!';
		}
		return retorno;
	}
	
	public static String getContexto() {
		return Propiedades.getString("contexto");
	}
	
	public static String getPath() {
		return Propiedades.getString("path");
	}
	public static String getPathAllowed() {
		return Propiedades.getString("path.allowed");
	}
	public static String getProxyHost() {
		return Propiedades.getString("proxy.host");
	}
	public static String getProxyPort() {
		return Propiedades.getString("proxy.port");
	}

	public static String getMailServer() {
		return Propiedades.getString("mail.server");
	}
	public static String getPathApi() {
		return Propiedades.getString("api.path");
	}
	public static String getApiAgent() {
		return Propiedades.getString("api.agent");
	}
	public static String getApiClientId() {
		return Propiedades.getString("api.clientId");
	}
	public static String getApiPk() {
		return Propiedades.getString("api.pk");
	}
	public static boolean isLocal() {
		return "local".equals(Propiedades.getString("entorno"));
	}
	public static String getPathVistas() {
		return Propiedades.getString("path.vistas.disk");
	}
	public static String getPathCont() {
		return Propiedades.getString("path.cont");
	}
	public static String getPathContDisk() {
		return Propiedades.getString("path.cont.disk");
	}
	public static String getPathContExternal() {
		return Propiedades.getString("path.cont.external");
	}
	public static String getMailUser() {
		return Propiedades.getString("mail.user");
	}
	public static String getMailPass() {
		return Propiedades.getString("mail.pass");
	}
	public static String getSparqlEndPoint() {
		return Propiedades.getString("virtuoso.sparql");
	}
	public static String getAnalyticsCode() {
		return Propiedades.getString("analytics.code");
	}
	public static String getAnalyticsKeyFile() {
		return Propiedades.getString("analytics.keyfile");
	}
	public static String getAnalyticsServiceEmail() {
		return Propiedades.getString("analytics.serviceemail");
	}
	public static String getAnalyticsViewId() {
		return Propiedades.getString("analytics.viewid");
	}
	public static String getFehacienteRutaSellado() {
		return Propiedades.getString("fehaciente.rutasellado");
	}
	public static String getFehacienteClientId() {
		return Propiedades.getString("fehaciente.clientId");
	}
	public static String getFehacientePass() {
		return Propiedades.getString("fehaciente.pass");
	}
	public static String getSmsServer() {
		return Propiedades.getString("sms.server");
	}
	public static String getSmsFrom() {
		return Propiedades.getString("sms.from");
	}
	public static String getSmsUser() {
		return Propiedades.getString("sms.user");
	}
	public static String getSmsPass() {
		return Propiedades.getString("sms.pass");
	}
	public static String getCapazUri() {
		return Propiedades.getString("capaz.uri");
	}
	public static String getCapazToken() {
		return Propiedades.getString("capaz.token");
	}
	public static String getCapazErrorMail() {
		return Propiedades.getString("capaz.errormail");
	}
	public static String getPathAplicacionesDisk() {
		return Propiedades.getString("path.aplicaciones.disk");
	}
	public static boolean excludedFromRecaptcha(HttpServletRequest request) {
		if (Propiedades.isLocal()) {
			return true;
		} else {
			String[] lista = Propiedades.getString("recaptcha.exclude").split(",");
			String ip = Funciones.getIpUser(request);
			if (lista.length > 0) {
				for (String val : lista) {
					if (StringUtils.isNotEmpty(ip)
							&& StringUtils.isNotEmpty(val)
							&& ip.contains(val)) {
						return true;
					}
				}
			}
			return false;
		}
	}

	public static String getHostTarjetaCiudadana() {
		return Propiedades.getString("tarjetaciudadana.host");
	}
	public static String getUserTarjetaCiudadana() {
		return Propiedades.getString("tarjetaciudadana.user");
	}
	public static String getPasswordTarjetaCiudadana() {
		return Propiedades.getString("tarjetaciudadana.password");
	}
	
	// Firebase Cloud Messaging
	public static String getFCMUrl() {
		return Propiedades.getString("fcm.url");
	}
	public static String getFCMZgz16ServerKey() {
		return Propiedades.getString("fcm.zgz16.serverkey");
	}
	
	// Apple Push Notification service
	public static String getAPNZgz16CertPath() {
		return Propiedades.getString("apn.zgz16.cert.path");
	}
	public static String getAPNZgz16CertPassword() {
		return Propiedades.getString("apn.zgz16.cert.pass");
	}
	public static String getGeoNetworkURI() {
		return Propiedades.getString("geonetwork.uri");
	}
	public static boolean containsKey(String key) {
		return RESOURCE_BUNDLE.containsKey(key);
	}
	
	public static String getHostElastic() {
		return Propiedades.getString("elastic.host");
	}
	public static String getPortElastic() {
		return Propiedades.getString("elastic.port");
	}
	public static String getMethodElastic() {
		return Propiedades.getString("elastic.method");
	}
	public static String getThymeleafView() {
		return Propiedades.getString("thymeleaf.view");
	}
	public static String getThymeleafViewAlternative() {
		return Propiedades.getString("thymeleaf.view.alternative");
	}
	public static boolean isThymeleafStrictMode() {
		if (RESOURCE_BUNDLE.containsKey("thymeleaf.strictMode")) {
			return "true".equals(Propiedades.getString("thymeleaf.strictMode"));
		}else {
			return true;
		}
		
	}
	
	public static String getPathi18n() {
		return Propiedades.getString("path.i18n");
	}
	public static boolean isDatasourceJdbc() {
		if (RESOURCE_BUNDLE.containsKey("datasource.jdbc")) {
			return "true".equals(Propiedades.getString("datasource.jdbc"));
		} else {
			return false;
		}
	}
	public static String getActivationUrl() {
		return Propiedades.getString("activation.url");
	}
	
	public static String getDatasourcePrefix() {
		if (RESOURCE_BUNDLE.containsKey("datasource.prefix")) {
			return Propiedades.getString("datasource.prefix");
		} else {
			return "";
		}
	}
}
