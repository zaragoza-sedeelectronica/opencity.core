package org.sede.servicio.analytics;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.MimeTypes;
import org.sede.core.utils.Propiedades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AsyncLog {
	private AsyncLog() {
		super();
	}
	private static final Logger logger = LoggerFactory
			.getLogger(AsyncLog.class);
	static ExecutorService executorService = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 3L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

	// Cambiarlo para cada aplicacion
	private static final String UUID = "6ee29e91-9075-ba5e-bb65-c348398c8830"; 
	public static void log(final String clientId, final String ip, final String method, final String uri, final String format, final String userAgent, final String referer, final String msgError) {
		executorService.execute(new Runnable() {
			public void run() {
				try {
					String mensaje = msgError;
					if (mensaje == null) {
						mensaje = "";
					} else {
						if (mensaje.length() >=150) {
							mensaje = mensaje.substring(0, 149);
						}
						mensaje = "&t=exception&exf=1&exd=" + mensaje;
					}
					String url = "https://www.google-analytics.com/collect?v=1&tid=" + Propiedades.getAnalyticsCode() + "&t=pageview"
							+ "&cid=" + UUID 
							+ (clientId == null ? "" : "&uid=" + clientId) 
							+ (ip == null ? "" : "&uip=" + ip) 
							+ "&dt=APIv2_" + format
							+ "&dl=" + URLEncoder.encode(uri,CharEncoding.UTF_8).replace("+", "%20")
							+ (userAgent == null ? "" : "&ua=" + URLEncoder.encode(userAgent,CharEncoding.UTF_8).replace("+", "%20"))
							+ "&ds=API" // Fuente de datos
							+ "&cn=sede" // Nombre de la campaña
							+ "&cs=" + format // Fuente de la campaña
							+ "&cm=" + method //Medio de la campañ
							+ mensaje;
					if (Propiedades.getPath().contains("www.zaragoza.es")) {
						postExterno(url, "", null, referer, userAgent);
					}
				} catch (Exception e) {
					logger.error("ERROR code:" + Propiedades.getAnalyticsCode());
					logger.error("ERROR uri:" + uri);
					logger.error("ERROR ua:" + userAgent);
				}
			}
		});
	}
	
	public static void postExterno(String uri, String json, Map<String, String> headers, String referer, String userAgent) throws IOException {
		HttpClient client = new HttpClient();

		PostMethod method = new PostMethod(uri);

		method.addRequestHeader(CheckeoParametros.USERAGENT, userAgent);
		method.addRequestHeader(CheckeoParametros.ACCEPTHEADER, MimeTypes.JSON);
		if (referer != null) {
			method.addRequestHeader(CheckeoParametros.REFERER, referer);
		}

		if (headers != null) {
			@SuppressWarnings("rawtypes")
			Iterator it = headers.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry e = (Map.Entry) it.next();
				method.addRequestHeader(e.getKey().toString(), e.getValue().toString());
			}
		}

		method.setRequestEntity(new StringRequestEntity(json));
		try {
			client.executeMethod(method);
			method.getResponseBody();
		} finally {
			method.releaseConnection();
		}
	}
}