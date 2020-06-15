package org.sede.core.dao;


import com.googlecode.genericdao.search.SearchResult;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CommonsHttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.sede.core.anotaciones.CampoSolr;
import org.sede.core.anotaciones.PublicUri;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.solr.Faceta;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.Propiedades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class Solr {
	private static final Logger logger = LoggerFactory
					.getLogger(Solr.class);
	protected static Solr servidor;
	protected static CommonsHttpSolrServer server;

	
	private static final String PATH = Propiedades.getString("path.solr");
	private static final String USERPROP = Propiedades.getString("solr.usuario");
	private static final String USERPASS = Propiedades.getString("solr.password");
	
	
	public static final Map<String, String> iconos = new HashMap<String, String>();

	static {
		iconos.put("Actividades", "http://www.zaragoza.es/contenidos/iconos/agenda.png");
		iconos.put("Agenda Juvenil", "http://www.zaragoza.es/contenidos/iconos/agenda.png");
		iconos.put("Recursos", "http://www.zaragoza.es/contenidos/iconos/recursos.png");
		iconos.put("Monumentos", "http://www.zaragoza.es/contenidos/iconos/arte.png");
		iconos.put("Alojamientos", "http://www.zaragoza.es/contenidos/iconos/alojamiento2.png");
		iconos.put("Arte Público", "http://www.zaragoza.es/contenidos/iconos/arte.png");
		iconos.put("Edificios Histórico Artísticos", "http://www.zaragoza.es/contenidos/iconos/arte.png");
		iconos.put("Restaurantes", "http://www.zaragoza.es/contenidos/iconos/restaurante20.png");
		iconos.put("Incidencias", "http://www.zaragoza.es/contenidos/iconos/afecciones.png");
		iconos.put("Calidad del aire", "http://www.zaragoza.es/contenidos/iconos/calidaddelAire.png");
		iconos.put("Calidad del agua", "http://www.zaragoza.es/contenidos/iconos/fuenteAguaPotable.png");
		iconos.put("Bizi", "http://www.zaragoza.es/contenidos/iconos/estacionesBizi2.png");
	}

	/**
	 * Gets the instance of this class.
	 *
	 * @return
	 */
	public static Solr getInstance() {
		if (servidor == null) {
			synchronized (Solr.class) {
				servidor = new Solr();
			}
		}
		return servidor;
	}

	/**
	 * Private constructor to prohibit instantiation of this class.
	 */
	private Solr() {
		try {
			URL serverUrl = new URL("http://" + PATH);
			int puerto = serverUrl.getPort();
			String servidorUrl = serverUrl.getHost();
			
			if (!StringUtils.isEmpty(USERPROP)) {
				server = new CommonsHttpSolrServer("http://" + USERPROP + ":" + USERPASS + "@" + servidorUrl + ":" + puerto + "/buscador");
				Credentials defaultcreds = new UsernamePasswordCredentials(USERPROP, USERPASS);
				server.getHttpClient().getState().setCredentials(new AuthScope(servidorUrl, puerto), defaultcreds);
				server.getHttpClient().getParams().setAuthenticationPreemptive(true);
				server.getHttpClient().getParams().setSoTimeout(1000);
				logger.info("Servidor SOLR definido con credenciales: " + servidorUrl);
			} else {
				server = new CommonsHttpSolrServer("http://" + servidorUrl + ":" + puerto + "/buscador");
				server.getHttpClient().getParams().setSoTimeout(1000);
				logger.error("No se ha definido credenciales para SOLR: " + servidorUrl);
			}

			String proxy = Propiedades.getProxyHost();
			if (proxy.length() > 0) {
				server.getHttpClient().getHostConfiguration().setProxy(proxy, Integer.parseInt(Propiedades.getProxyPort()));
				logger.info("Proxy: " + proxy);
			} else {
				logger.info("No proxy");
			}

		} catch (MalformedURLException e) {
			logger.error(e.getMessage());
		}
	}

	public int save(Object[] entities) {
		if (!StringUtils.isEmpty(USERPROP)) {
			ArrayList<SolrInputDocument> listado = new ArrayList<SolrInputDocument>();
			for (int i = 0; i < entities.length; i++) {
				SolrInputDocument doc = obtenerDatosEntidad(entities[i]);
				if (doc != null) {
					listado.add(doc);
				}
			}
			try {
				if (!listado.isEmpty()) {
					UpdateResponse upres = server.add(listado);
					return upres.getStatus();
				} else {
					return -1;
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
				return -1;
			} catch (SolrServerException e) {
				logger.error(e.getMessage());
				return -1;
			}
		} else {
			logger.error("Datos no persistidos en SOLR al no estar definidas las credenciales");
			return -1;
		}
	}

	public int save(SolrInputDocument doc) {
		if (!StringUtils.isEmpty(USERPROP)) {
			try {
				if (doc != null) {
					UpdateResponse upres = server.add(doc);
					return upres.getStatus();
				} else {
					return -1;
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
				return -1;
			} catch (SolrServerException e) {
				logger.error(e.getMessage());
				return -1;
			}
		} else {
			logger.error("Datos no persistidos en SOLR al no estar definidas las credenciales");
			return -1;
		}	
	}

	public int save(Object entity) {
		if (!StringUtils.isEmpty(USERPROP)) {
			try {
				// Guardar por anotaciones o ejecutar método toSOLR si existe
				SolrInputDocument doc = obtenerDatosEntidad(entity);
				if (doc != null) {
					UpdateResponse upres = server.add(doc);
					return upres.getStatus();
				} else {
					return -1;
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
				return -1;
			} catch (SolrServerException e) {
				logger.error(e.getMessage());
				return -1;
			}
		} else {
			logger.error("Datos no persistidos en SOLR al no estar definidas las credenciales");
			return -1;
		}
	}

	private SolrInputDocument obtenerDatosEntidad(Object entity) {
		SolrInputDocument doc;
		try {
			Method method = entity.getClass().getDeclaredMethod("toSolr");
			doc = (SolrInputDocument) method.invoke(entity);
		} catch (NoSuchMethodException e) {
			doc = obtenerDatosDeAnotaciones(entity);
		} catch (IllegalArgumentException e) {
			doc = null;
		} catch (IllegalAccessException e) {
			doc = null;
		} catch (InvocationTargetException e) {
			doc = null;
		}
		return doc;
	}

	private SolrInputDocument obtenerDatosDeAnotaciones(Object entity) {
		SolrInputDocument doc = new SolrInputDocument();

		for (Field field : entity.getClass().getDeclaredFields()) {
			if (field.isAnnotationPresent(org.apache.solr.client.solrj.beans.Field.class)) {
				Object valor = Funciones.retrieveObjectValue(entity, field.getName());
				if (field.getName().equals("id")) {
					String prefijo = entity.getClass().getAnnotation(org.sede.core.anotaciones.Solr.class).prefijo();
					doc.addField("id", prefijo + "-" + valor);
					doc.addField("uri", "http://" + Propiedades.getPath() + entity.getClass().getAnnotation(PublicUri.class).value() + valor);
				} else {
					String clave = field.getAnnotation(org.apache.solr.client.solrj.beans.Field.class).value().equals("") ? field.getName() : field.getAnnotation(org.apache.solr.client.solrj.beans.Field.class).value();
					doc.addField(clave, valor);
				}
			}
		}
		return doc;
	}

	public void delete(Object entity) {
		if (!StringUtils.isEmpty(USERPROP)) {
			try {
				String prefijo = entity.getClass().getAnnotation(org.sede.core.anotaciones.Solr.class).prefijo();
				String id = Funciones.obtenerValorCampo(entity, "id");
				if (id != null) {
					server.deleteByQuery("id:" + prefijo + "-" + id);
				}
			} catch (SolrServerException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Datos no persistidos en SOLR al no estar definidas las credenciales");
		}
	}

	public void deleteById(Class<?> type, Serializable id) {
		if (!StringUtils.isEmpty(USERPROP)) {
			if (type.isAnnotationPresent(org.sede.core.anotaciones.Solr.class)) {
				try {
					String prefijo = type.getAnnotation(org.sede.core.anotaciones.Solr.class).prefijo();
					if (id != null) {
						server.deleteByQuery("id:" + prefijo + "-" + id);
						server.commit();
					}
				} catch (SolrServerException e) {
					logger.error(e.getMessage());
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
			}
		} else {
			logger.error("Datos no persistidos en SOLR al no estar definidas las credenciales");
		}	
	}

	public void deleteByQuery(String query, Class<?> clase) {
		if (!StringUtils.isEmpty(USERPROP)) {
			try {
	
				if (clase.isAnnotationPresent(org.sede.core.anotaciones.Solr.class)) {
					query = convertirCampos(query, clase) + " AND category:(\"" + clase.getAnnotation(org.sede.core.anotaciones.Solr.class).categoria() + "\")";
					query = query.replaceAll("id:", "id:" + clase.getAnnotation(org.sede.core.anotaciones.Solr.class).prefijo() + "-");
				}
	
				server.deleteByQuery(query);
			} catch (SolrServerException e) {
				logger.error(e.getMessage());
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("Datos no persistidos en SOLR al no estar definidas las credenciales");
		}
	}

	public UpdateResponse deleteId(String id) {
		if (!StringUtils.isEmpty(USERPROP)) {
			try {
				UpdateResponse respuesta = server.deleteByQuery("id:" + id);
				server.commit();
				return respuesta;
			} catch (SolrServerException e) {
				logger.error(e.getMessage());
				return null;
			} catch (IOException e) {
				logger.error(e.getMessage());
				return null;
			}
		} else {
			logger.error("Datos no persistidos en SOLR al no estar definidas las credenciales");
			return null;
		}
	}

	public UpdateResponse deleteUri(String uri) {
		if (!StringUtils.isEmpty(USERPROP)) {
			try {
				UpdateResponse respuesta = server.deleteByQuery("uri:" + uri.replace("https://", "*").replace("http://", "*"));
				server.commit();
				return respuesta;
			} catch (SolrServerException e) {
				logger.error(e.getMessage());
				return null;
			} catch (IOException e) {
				logger.error(e.getMessage());
				return null;
			}
		} else {
			logger.error("Datos no persistidos en SOLR al no estar definidas las credenciales");
			return null;
		}
	}

	public void update(Object entity) {
		delete(entity);
		save(entity);
	}

	public int commit() {
		if (!StringUtils.isEmpty(USERPROP)) {
			try {
				UpdateResponse upres = server.commit(true, true);
				return upres.getStatus();
			} catch (SolrServerException e) {
				logger.error(e.getMessage());
				return -1;
			} catch (IOException e) {
				logger.error(e.getMessage());
				return -1;
			}
		} else {
			logger.error("Datos no persistidos en SOLR al no estar definidas las credenciales");
			return -1;
		}
	}

	public int optimize() {
		if (!StringUtils.isEmpty(USERPROP)) {
			try {
				UpdateResponse upres = server.optimize(true, true);
				return upres.getStatus();
			} catch (SolrServerException e) {
				logger.error(e.getMessage());
				return -1;
			} catch (IOException e) {
				logger.error(e.getMessage());
				return -1;
			}
		} else {
			logger.error("Datos no persistidos en SOLR al no estar definidas las credenciales");
			return -1;
		}
	}

	public boolean indizar(Object entity) {
		return entity.getClass().isAnnotationPresent(org.sede.core.anotaciones.Solr.class);
	}

	public Object porId(String id, Map<String, String> fl, Class<?> clase) {
		return query("id:" + id, "", null, null, 1, 0, fl, null, null, clase, true).getResult().get(0);
	}

	public SearchResult<?> query(String query, String fq, String p, Integer radio, int rows, int start, Map<String, String> fl, Map<String, List<Faceta>> facetas, String sort, Class<?> clase, boolean historico) {
		return query(query, fq, null, p, radio, rows, start, fl, facetas, sort, clase, historico);
	}

	public SearchResult<?> query(String query, String fq, String facetSort, String p, Integer radio, int rows, int start, Map<String, String> fl, Map<String, List<Faceta>> facetas, String sort, Class<?> clase, boolean historico) {

		String categoria = null;
		String prefijo = null;
		if (clase.isAnnotationPresent(org.sede.core.anotaciones.Solr.class)) {
			categoria = clase.getAnnotation(org.sede.core.anotaciones.Solr.class).categoria();
			prefijo = clase.getAnnotation(org.sede.core.anotaciones.Solr.class).prefijo();
		}
		return query(categoria, prefijo, query, fq, facetSort, p, radio, rows, start, fl, facetas, sort, clase, historico);
	}

	public SearchResult<?> query(String categoria, String prefijo, String query, String fq, String facetSort, String p, Integer radio, int rows, int start, Map<String, String> fl, Map<String, List<Faceta>> facetas, String sort, Class<?> clase, boolean historico) {
//		query = Utils.toUnicode(query);
		SolrQuery solrQuery = new SolrQuery();
		String filtroCampos = obtenerCamposSolrFiltro(fl, clase);
		if (!"".equals(filtroCampos)) {
			solrQuery.setParam("fl", filtroCampos);
		}
		solrQuery.set("q.op", "AND");
		solrQuery.setRows(rows);
		solrQuery.setStart(start);
		if (StringUtils.isNotEmpty(categoria)) {
			query = convertirCampos(query, clase);
			if (categoria.length() > 0) {
				query = query + " AND category:(\"" + categoria + "\")"
								+ " AND -tipocontenido_s:estatico";
			} else {
				query = query + " AND -tipocontenido_s:historico";
			}
			query = query.replaceAll("id:", "id:" + prefijo + "-");
		} else {
			query = convertirCampos(query, clase);
		}

		if (StringUtils.isNotEmpty(sort)) {
			solrQuery.setParam("sort", convertirSort(sort, clase));
		} else {
			solrQuery.setParam("sort", "score desc," + Faceta.FACET_FECHA_MODIFICADO + " desc");
		}
		if (StringUtils.isNotEmpty(fq)) {
			solrQuery.setParam("fq", Funciones.convert(fq));
		}
		/*Busqueda por distancia*/
		if (!"".equals(p) && radio != null) {
			Double distancia = (radio / 1000.0);
			solrQuery.setParam("fq", "{!bbox}");
			solrQuery.setParam("sfield", "coordenadas_p");
			solrQuery.setParam("pt", Geometria.invertirCoordenadas(p));
			solrQuery.setParam("d", "" + distancia);
			solrQuery.setParam("sort", "geodist() asc");
		}

		if (facetas != null && facetas.size() > 0) {
			solrQuery.setParam("facet", "true");
			solrQuery.setParam("facet.mincount", "1");
			solrQuery.setParam("facet.field", convertirFacetas(facetas, clase));
			if (facetSort != null) {
				solrQuery.setParam("facet.sort", "index");
			}
		}


		if (!historico) {
			query = query + " AND -tipocontenido_s:historico";
		}
		solrQuery.setQuery(Funciones.convert(query));

		QueryResponse queryResponse;
		try {
			logger.info("Query solr:{}", solrQuery);
			queryResponse = server.query(solrQuery);

			SolrDocumentList solrDocumentList = queryResponse.getResults();

			SearchResult<Object> resultado = new SearchResult<Object>();
			resultado.setStart(start);
			resultado.setRows(rows);
			resultado.setTotalCount(Integer.parseInt("" + solrDocumentList.getNumFound()));

			List<Object> listado = new ArrayList<Object>();
			for (int i = 0; i < solrDocumentList.size(); i++) {
				SolrDocument doc = solrDocumentList.get(i);
				listado.add(documento2Entity(doc, clase));
			}

			resultado.convertirAFaceta(queryResponse.getFacetFields(), facetas);

			resultado.setResult(listado);
			return resultado;

		} catch (Exception e) {
//			if (Funciones.getRequest().getHeader("User-Agent") == null || !Funciones.getRequest().getHeader("User-Agent").contains("AspiegelBot")) {
				//FIXME aparecen errores de navegacion por bots, por lo que no los almacenamos
			if (Funciones.getPeticion().isDebug()) {
				logger.error("ERROR query solr: {}:{} " + Funciones.getRequest().getHeader("User-Agent"), solrQuery, e.getMessage());
				logger.error(Funciones.getStackTrace(e));
			}
			return null;
		}

	}


	private String[] convertirFacetas(Map<String, List<Faceta>> facetas, Class<?> clase) {
		String[] transformado = new String[facetas.size()];
		int i = 0;
		for (String key : facetas.keySet()) {
			Field campo;
			try {
				campo = clase.getDeclaredField(key);
				if (campo.isAnnotationPresent(CampoSolr.class)) {
					transformado[i] = campo.getAnnotation(CampoSolr.class).value();
				} else {
					transformado[i] = key;
				}
			} catch (SecurityException e) {
				transformado[i] = key;
			} catch (NoSuchFieldException e) {
				transformado[i] = key;
			}
			i++;
		}
		return transformado;
	}

	private String convertirCampos(String texto, Class<?> clase) {
		if ("".equals(texto.trim())) {
			texto = "*:*";
		} else {
			texto = convertir(texto, clase);
		}
		return texto;
	}

	private String convertir(String texto, Class<?> clase) {
		for (int i = 0; i < clase.getDeclaredFields().length; i++) {
			Field campo = clase.getDeclaredFields()[i];
			if (campo.isAnnotationPresent(CampoSolr.class) && !"".equals(campo.getAnnotation(CampoSolr.class).value())) {
				texto = texto.replaceAll(campo.getName(), campo.getAnnotation(CampoSolr.class).value());
			}
		}
		return texto;
	}

	public String convertirSort(String texto, Class<?> clase) {
		for (int i = 0; i < clase.getDeclaredFields().length; i++) {
			Field campo = clase.getDeclaredFields()[i];
			if (campo.isAnnotationPresent(CampoSolr.class) && !"".equals(campo.getAnnotation(CampoSolr.class).value())) {
				texto = texto.replaceAll(campo.getName(), campo.getAnnotation(CampoSolr.class).value());
			}
		}

		return texto;
	}

	private String obtenerCamposSolrFiltro(Map<String, String> fl, Class<?> clase) {
		StringBuilder res = new StringBuilder();
		if (fl != null) {
			boolean primero = true;
			for (Map.Entry<String, String> entry : fl.entrySet()) {
				try {
					if (clase.getDeclaredField(entry.getKey()).isAnnotationPresent(CampoSolr.class)) {
						if (!primero) {
							res.append(",");
						}
						if ("".equals(clase.getDeclaredField(entry.getKey()).getAnnotation(CampoSolr.class).value())) {
							res.append(entry.getKey());
						} else {
							res.append(clase.getDeclaredField(entry.getKey()).getAnnotation(CampoSolr.class).value());
						}
						primero = false;
					}
				} catch (SecurityException e) {
					;
				} catch (NoSuchFieldException e) {
					;
				}
			}
			if (res.length() > 0 && res.indexOf(Faceta.FACET_FECHA_MODIFICADO) < 0) {
				res.append("," + Faceta.FACET_FECHA_MODIFICADO);
			}
		}
		return res.toString();
	}

	private Object documento2Entity(SolrDocument doc, Class<?> clase) {

		try {
			Object c = clase.newInstance();
			Method method = clase.getDeclaredMethod("fromSolr", SolrDocument.class);
			return method.invoke(c, doc);
		} catch (NoSuchMethodException e) {
			logger.error(e.getMessage());
			return null;
		} catch (IllegalArgumentException e) {
			logger.error(e.getMessage());
			return null;
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage());
			return null;
		} catch (InvocationTargetException e) {
			logger.error(e.getMessage());
			return null;
		} catch (InstantiationException e) {
			logger.error(e.getMessage());
			return null;
		}

	}

	public static String[] asList(Collection<Object> fieldValues) {
		if (fieldValues != null) {
			String[] listado = new String[fieldValues.size()];
			Iterator<Object> iterator = fieldValues.iterator();
			int i = 0;
			while (iterator.hasNext()) {
				Object element = iterator.next();
				listado[i] = element.toString();
				i++;
			}
			return listado;
		} else {
			return null;
		}
	}

	public static String obtenerIcono(String categoria) {
		return iconos.get(categoria);
	}

	public SolrDocument findById(String id) throws SolrServerException {
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setQuery("id:" + id);
		QueryResponse queryResponse = server.query(solrQuery);
		SolrDocumentList solrDocumentList = queryResponse.getResults();
		if (solrDocumentList.size() > 0) {
			return solrDocumentList.get(0);
		} else {
			return null;
		}
	}

}
