package org.sede.core.validator;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.json.JSONObject;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.Propiedades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Recaptcha {
	private Recaptcha() {
		super();
	}
	private static final Logger logger = LoggerFactory
			.getLogger(Recaptcha.class);
	private static final String USER_AGENT = "ciudad[web]";
	public static final String URL = "https://www.google.com/recaptcha/api/siteverify";
	public static final String SECRET = Propiedades.getString("recaptcha.secret");

	public static boolean verify(String gRecaptchaResponse, HttpServletRequest request) {
		if (Propiedades.isLocal() || Propiedades.excludedFromRecaptcha(request)) {
			return true;
		} else {
			Funciones.setProxy();
			if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
				return false;
			}
		
			HttpClient httpclient = new HttpClient();
			PostMethod method = new PostMethod(URL);
			try {
				method.addParameter(new NameValuePair("secret", SECRET));
				method.addParameter(new NameValuePair("response", gRecaptchaResponse));
				method.addRequestHeader(CheckeoParametros.USERAGENT, USER_AGENT);
				method.addRequestHeader("Accept-Language", "en-US,en;q=0.5");
				httpclient.executeMethod(method);
				String respuesta = new String(method.getResponseBody(), CharEncoding.UTF_8);
				JSONObject json = new JSONObject(respuesta);
				return json.getBoolean("success");
			} catch (Exception e) {
				logger.error(e.getMessage());
				return false;
			} finally {
				method.releaseConnection();
			} 
		}

	}
}
