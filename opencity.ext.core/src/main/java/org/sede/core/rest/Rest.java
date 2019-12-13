package org.sede.core.rest;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.sede.core.utils.Propiedades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rest {
	private static final Logger logger = LoggerFactory
			.getLogger(Rest.class);
	private static final String SERVER = Propiedades.getPathApi();
	private static final String AGENT = "ciudad[web]";
	
	public static final String METHODS = "PATCH,OPTIONS,GET,HEAD,POST,PUT,DELETE";
	
	protected static Rest servicio;
	
	private Rest() {
		
	}
	public static Rest getInstance() {
		if (servicio == null) {
			servicio = new Rest();
		}
		return servicio;
	}

	
	public Respuesta getLD(String uri) {
		return getLD(uri, "", "", null);
	}
	public Respuesta getLD(String uri, String clientId, String clavePrivada) {
		return getLD(uri, clientId, clavePrivada, null);
	}
	public Respuesta getLD(String uri, Map<String, String> headers) {
		return getLD(uri, "", "", headers);
	}
	
	public Respuesta getLD(String uri, String clientId, String clavePrivada, Map<String, String> headers) {
		return get(uri, clientId, clavePrivada, headers, MimeTypes.JSONLD);
	}
	
	
	public Respuesta get(String uri) {
		return get(uri, "", "", null);
	}
	public Respuesta get(String uri, String clientId, String clavePrivada) {
		return get(uri, clientId, clavePrivada, null);
	}
	public Respuesta get(String uri, Map<String, String> headers) {
		return get(uri, "", "", headers);
	}
	
	public Respuesta get(String uri, String clientId, String clavePrivada, Map<String, String> headers) {
		return get(uri, clientId, clavePrivada, headers, MimeTypes.JSON);
	}
	
	public Respuesta get(String uri, String clientId, String clavePrivada, Map<String, String> headers, String accept) {
		return get(SERVER, uri, clientId, clavePrivada, headers, accept);
	}
	
	public Respuesta get(String server, String uri, String clientId, String clavePrivada, Map<String, String> headers, String accept) {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(server + uri);
		logger.info("GET: {}{}", server, uri);
		method.addRequestHeader(CheckeoParametros.USERAGENT, AGENT);
		method.addRequestHeader(CheckeoParametros.ACCEPTHEADER, accept);
		method.addRequestHeader(CheckeoParametros.REFERER, SERVER + "/ciudad/");

		String proxy = Propiedades.getProxyHost();
		if (proxy.length() > 0) {
			client.getHostConfiguration().setProxy(proxy, Integer.parseInt(Propiedades.getProxyPort()));
		}
        
		if (headers != null) {
			@SuppressWarnings("rawtypes")
			Iterator it = headers.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry e = (Map.Entry)it.next();
				method.addRequestHeader(e.getKey().toString(), e.getValue().toString());
			}
		}
		
		if (!"".equals(clientId) && !"".equals(clavePrivada)) {
			method.addRequestHeader(CheckeoParametros.HEADERCLIENTID, clientId);
			method.addRequestHeader(CheckeoParametros.HEADERHMAC, Hmac.calcular(clientId + "GET" + uri, clavePrivada));
		}
		Respuesta resp = new Respuesta();
		try {
			resp.setStatus(client.executeMethod(method));
			resp.setContenido(new String(method.getResponseBody(), CharEncoding.UTF_8));
		} catch (HttpException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} catch (IOException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		return resp;

	}
	
	public Respuesta getUri(String uri, Map<String, String> headers, String accept) {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(uri);
		method.addRequestHeader(CheckeoParametros.USERAGENT, AGENT);
		method.addRequestHeader(CheckeoParametros.ACCEPTHEADER, accept);
		method.addRequestHeader(CheckeoParametros.REFERER, SERVER + "/ciudad/");
        
		if (headers != null) {
			@SuppressWarnings("rawtypes")
			Iterator it = headers.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry e = (Map.Entry)it.next();
				method.addRequestHeader(e.getKey().toString(), e.getValue().toString());
			}
		}
		String proxy = Propiedades.getProxyHost();
		if (proxy.length() > 0) {
			client.getHostConfiguration().setProxy(proxy, Integer.parseInt(Propiedades.getProxyPort()));
		}
		Respuesta resp = new Respuesta();
		try {
			resp.setStatus(client.executeMethod(method));
			resp.setContenido(new String(method.getResponseBody(), CharEncoding.UTF_8));
		} catch (HttpException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} catch (IOException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} finally {
			// Release the connection.
			method.releaseConnection();
		}
		return resp;

	}
	
	public Respuesta delete(String uri, String clientId, String clavePrivada) {
		HttpClient client = new HttpClient();
		DeleteMethod method = new DeleteMethod(SERVER + uri);

		method.addRequestHeader(CheckeoParametros.USERAGENT, AGENT);
		method.addRequestHeader(CheckeoParametros.ACCEPTHEADER, MimeTypes.JSON);
		method.addRequestHeader(CheckeoParametros.REFERER, SERVER + "/ciudad/");
		
		method.addRequestHeader(CheckeoParametros.HEADERCLIENTID, clientId);
		method.addRequestHeader(CheckeoParametros.HEADERHMAC, Hmac.calcular(clientId + "DELETE" + uri, clavePrivada));
		String proxy = Propiedades.getProxyHost();
		if (proxy.length() > 0) {
			client.getHostConfiguration().setProxy(proxy, Integer.parseInt(Propiedades.getProxyPort()));
		}
		Respuesta resp = new Respuesta();
		try {
			resp.setStatus(client.executeMethod(method));
			resp.setContenido(new String(method.getResponseBody(), CharEncoding.UTF_8));
		} catch (HttpException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} catch (IOException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} finally {
			method.releaseConnection();
		}
		return resp;

	}
	public Respuesta post(String uri, String json,  String clientId, String clavePrivada) {
		return post(uri, json, clientId, clavePrivada, null);
	}
	public Respuesta post(String uri, String json,  String clientId, String clavePrivada, Map<String, String> headers) {
		HttpClient client = new HttpClient();
		
		PostMethod method = new PostMethod(SERVER + uri);

		method.addRequestHeader(CheckeoParametros.USERAGENT, AGENT);
		method.addRequestHeader(CheckeoParametros.ACCEPTHEADER, MimeTypes.JSON);
		method.addRequestHeader(CheckeoParametros.REFERER, SERVER + "/ciudad/");
		
		if (headers != null) {
			@SuppressWarnings("rawtypes")
			Iterator it = headers.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry e = (Map.Entry)it.next();
				method.addRequestHeader(e.getKey().toString(), e.getValue().toString());
			}
		}
		
		method.addRequestHeader(CheckeoParametros.HEADERCLIENTID, clientId);
		method.addRequestHeader(CheckeoParametros.HEADERHMAC, Hmac.calcular(clientId + "POST" + uri + json, clavePrivada));
		method.setRequestEntity( new StringRequestEntity(json) );
		String proxy = Propiedades.getProxyHost();
		if (proxy.length() > 0) {
			client.getHostConfiguration().setProxy(proxy, Integer.parseInt(Propiedades.getProxyPort()));
		}
		Respuesta resp = new Respuesta();
		try {
			resp.setStatus(client.executeMethod(method));
			resp.setContenido(new String(method.getResponseBody(), CharEncoding.UTF_8));
		} catch (HttpException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} catch (IOException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} finally {
			method.releaseConnection();
		}
		return resp;

	}
	public Respuesta put(String uri, String json,  String clientId, String clavePrivada) {
		return put(uri, json, clientId, clavePrivada, null);
	}
	public Respuesta put(String uri, String json,  String clientId, String clavePrivada, Map<String, String> headers) {
		HttpClient client = new HttpClient();
		
		PutMethod method = new PutMethod(SERVER + uri);

		method.addRequestHeader(CheckeoParametros.USERAGENT, AGENT);
		method.addRequestHeader(CheckeoParametros.ACCEPTHEADER, MimeTypes.JSON);
		
		method.addRequestHeader(CheckeoParametros.REFERER, SERVER + "/ciudad/");
		
		if (headers != null) {
			@SuppressWarnings("rawtypes")
			Iterator it = headers.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry e = (Map.Entry)it.next();
				method.addRequestHeader(e.getKey().toString(), e.getValue().toString());
			}
		}
		method.addRequestHeader(CheckeoParametros.HEADERCLIENTID, clientId);
		method.addRequestHeader(CheckeoParametros.HEADERHMAC, Hmac.calcular(clientId + "PUT" + uri + json, clavePrivada));
		method.setRequestEntity( new StringRequestEntity(json) );
		String proxy = Propiedades.getProxyHost();
		if (proxy.length() > 0) {
			client.getHostConfiguration().setProxy(proxy, Integer.parseInt(Propiedades.getProxyPort()));
		}
		Respuesta resp = new Respuesta();
		try {
			resp.setStatus(client.executeMethod(method));
			resp.setContenido(new String(method.getResponseBody(), CharEncoding.UTF_8));
		} catch (HttpException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} catch (IOException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} finally {
			method.releaseConnection();
		}
		return resp;

	}
	
	public String addExtension(String uri, String extension) {
		
		if (uri.indexOf('?') >= 1) {
			String tmp = uri.substring(0, uri.indexOf('?')); 
			if (tmp.lastIndexOf('/') + 1 == tmp.length()) {
				tmp = tmp.substring(0, tmp.length() - 1); 
			}
			
			return tmp + "." + extension + (uri.substring(uri.indexOf('?'), uri.length()).replaceAll("&", "&amp;"));
		} else {
			if (uri.lastIndexOf('/') + 1 == uri.length()) {
				uri = uri.substring(0, uri.length() - 1); 
			}
			return uri + "." + extension;
		}
	}
	
	public Respuesta postExterno(String uri, String json, Map<String, String> headers, String referer, String userAgent) {
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
				method.addRequestHeader(e.getKey().toString(), e.getValue()
						.toString());
			}
		}

		method.setRequestEntity(new StringRequestEntity(json));
		String proxy = Propiedades.getProxyHost();
		if (proxy.length() > 0) {
			client.getHostConfiguration().setProxy(proxy, Integer.parseInt(Propiedades.getProxyPort()));
		}
		Respuesta resp = new Respuesta();
		try {
			resp.setStatus(client.executeMethod(method));
			resp.setContenido(new String(method.getResponseBody(), CharEncoding.UTF_8));
		} catch (HttpException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} catch (IOException e) {
			resp.setStatus(400);
			resp.setContenido(e.getMessage());
		} finally {
			method.releaseConnection();
		}
		return resp;
	}
	
	public String query(String query) throws UnsupportedEncodingException {
		String consulta = Propiedades.getSparqlEndPoint() + "?query=" + URLEncoder.encode(query, CharEncoding.UTF_8) + "&format=" + URLEncoder.encode(MimeTypes.SPARQL_JSON, CharEncoding.UTF_8) + "&timeout=0&debug=on";
		return makeHttpGet(consulta);
	}
	public String query(String query, String graph) throws UnsupportedEncodingException  {
		String consulta = Propiedades.getSparqlEndPoint() + "?default-graph-uri=" + graph 
				+ "&query=" + URLEncoder.encode(query, CharEncoding.UTF_8) + "&format=" + URLEncoder.encode(MimeTypes.SPARQL_JSON, CharEncoding.UTF_8) + "&timeout=0&debug=on";
		
		return makeHttpGet(consulta);
	}
	//Sparql externo
	public String query(String externo,String query, String graph) throws UnsupportedEncodingException {
		String consulta = externo + "/sparql?default-graph-uri=" + graph 
				+ "&query=" + URLEncoder.encode(query, CharEncoding.UTF_8) + "&format=" + URLEncoder.encode(MimeTypes.SPARQL_JSON, CharEncoding.UTF_8) + "&timeout=0&debug=on";
		
		return makeHttpGet(consulta);
	}
	
	public static String getExterno(String uri) {		
		return makeHttpGet(uri);
	}
	
	//Query para paginar Sparql
	public String queryPaginacion(String query, int numpag, int incremento) throws UnsupportedEncodingException {
		query=query.replace("WHERE", "count(*) as ?numreg WHERE" );
		String query2= query +" OFFSET "+(numpag*incremento)+" LIMIT "+(numpag+incremento);
		String consulta = Propiedades.getSparqlEndPoint() + "?query=" + URLEncoder.encode(query2, CharEncoding.UTF_8) + "&format=" + URLEncoder.encode(MimeTypes.SPARQL_JSON, CharEncoding.UTF_8) + "&timeout=0&debug=on";
		
		return makeHttpGet(consulta);
	}
	public static String queryCsv(String query, String graph) throws UnsupportedEncodingException {
		String consulta = Propiedades.getSparqlEndPoint() + "?default-graph-uri=" + graph 
				+ "&query=" + URLEncoder.encode(query, CharEncoding.UTF_8) + "&format=" + URLEncoder.encode("text/csv", CharEncoding.UTF_8) + "&timeout=0&debug=on";
		return makeHttpGet(consulta);
	}
	
	private static String makeHttpGet(String url) {
		 
		HttpClient client = new HttpClient();
		
		GetMethod method = new GetMethod(url);
		if (Propiedades.getProxyHost().length() >0) {
			client.getHostConfiguration().setProxy(Propiedades.getProxyHost(), Integer.parseInt(Propiedades.getProxyPort()));
		}
		method.addRequestHeader(CheckeoParametros.USERAGENT, AGENT);
		method.addRequestHeader(CheckeoParametros.REFERER, "http://www.zaragoza.es/");
		
		String retorno = "";
		try {
			int status = client.executeMethod(method);
			if (status == 200) {
				retorno = new String(method.getResponseBody(), CharEncoding.UTF_8);
			} else {
				retorno = null;
			}
		} catch (HttpException e) {
			logger.error(e.getMessage());
			retorno = null;
		} catch (IOException e) {
			logger.error(e.getMessage());
			retorno = null;
		} finally {
			method.releaseConnection();
		}
		return retorno;
	}
}
