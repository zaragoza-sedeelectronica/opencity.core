package org.sede.core.rest.view;


import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ValidationException;
import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.GeoJson;
import org.sede.core.anotaciones.HtmlContent;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.exception.FormatoNoSoportadoException;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.anotaciones.Context;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.Propiedades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.genericdao.search.SearchResult;


public class TransformadorGeoRss implements TransformadorGenerico {
	private static final Logger logger = LoggerFactory.getLogger(TransformadorGeoRss.class);
	private Html2MarkDown transformadorMarkDown = new Html2MarkDown();
	
	public String getSeparador() {
		return "";
	}

	public String getInicioObjeto(final String nombreObjeto) {
		return "<" + nombreObjeto + ">";
	}

	public String getFinObjeto(final String nombreObjeto) {
		return "</" + nombreObjeto + ">";
	}
	
	public String getInicioCampoObjeto(final String nombreCampo) {
		return "<" + nombreCampo + ">";
	}

	public String getFinCampoObjeto(final String nombreCampo) {
		return "</" + nombreCampo + ">";
	}

	public String escribirValorCampo(final String nombreCampo, final Object valor, boolean anotacionPresente, String formato) {
		if (valor instanceof Date) {
			Date fecha = (Date) valor;
			return "<" + nombreCampo + ">" + ConvertDate.date2String(fecha, ConvertDate.ISO8601_FORMAT) + "</" + nombreCampo + ">";
		} else {
			String txt = valor.toString();
			
			if (anotacionPresente && CheckeoParametros.RESPUESTAMARKDOWN.equals(formato)) {
				txt = transformadorMarkDown.transformar(txt);
			}
			
			if (txt.indexOf('<') > 0 || txt.indexOf('>') > 0 || txt.indexOf('&') > 0 && txt.indexOf("<![CDATA[") < 0) {
				txt = "<![CDATA[" + txt + "]]>";
			}
			
			return "<" + nombreCampo + ">" + txt + "</" + nombreCampo + ">";
		}
	}
	public String escribirValor(final String nombreCampo, final Object valor, boolean anotacionPresente, String formato) {
		return escribirValorCampo(nombreCampo, valor, anotacionPresente, formato);
	}
	public String escribirValorObjecto(String nombreCampo, String valor, boolean anotacionPresente, String formato) {
		return escribirValorCampo(nombreCampo, valor, anotacionPresente, formato);
	}
	public void enviarError(HttpServletResponse response, int statusCode, String mensaje, boolean peticionSoloHead) {
		String respuesta = "<error>" + mensaje + "</error>";
		response.setContentType("application/xml; charset=UTF-8");
		try {
			response.setStatus(statusCode);
			if (!peticionSoloHead) {
				response.getWriter().write(respuesta);
			}
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			;
		}
	}

	public Object pasarAObjeto(String valor, boolean esArray, Class<?>... type) throws FormatoNoSoportadoException {
		throw new FormatoNoSoportadoException();
	}

	public String getMensajeErrorValidacion(Set<ConstraintViolation<Object>> constraintViolations) {
		throw new ValidationException();
	}

	public String getMensajeErrorConstraint(
			Set<ConstraintViolation<?>> constraintViolations) {
		throw new ValidationException();
	}

	public String getInicioCampoObjetoNoListado(String nombreCampo) {
		return getInicioCampoObjeto(nombreCampo);
	}

	public String getFinCampoObjetoNoListado(String nombreCampo) {
		return getFinCampoObjeto(nombreCampo);
	}

	public void transformarObjeto(StringBuilder respuesta, Object retorno,
			Peticion peticion, boolean primero, String prefijo)
			throws InvalidImplementationException {
		Class<?> clase = descubrirClase(retorno);
		if (clase.isAnnotationPresent(GeoJson.class)) {
			String title = clase.getAnnotation(GeoJson.class).title();
			String icon = clase.getAnnotation(GeoJson.class).icon();
			String link = clase.getAnnotation(GeoJson.class).link();
			String description = clase.getAnnotation(GeoJson.class).description();
			String pathProp = "";
			if ("".equals(title) && retorno instanceof SearchResult) {
				SearchResult<?> c = (SearchResult<?>) retorno;
				title = (String)c.getPropiedades().get("title");
				icon = (String)c.getPropiedades().get("icon");
				link = (String)c.getPropiedades().get("link");
				description = (String)c.getPropiedades().get("description");
				peticion.setLastModified((Date)c.getPropiedades().get("lastUpdated"));
				pathProp = (String)c.getPropiedades().get("pathInterno");
			}
			StringBuilder objetos = new StringBuilder();
			StringBuilder identificadores = new StringBuilder();
			if (retorno instanceof SearchResult) {
				String path = Funciones.obtenerPath(((SearchResult<?>)retorno).getResult().get(0).getClass());
				if ("".equals(path)) {
					path = pathProp;
				}
				for (int i = 0; i < ((SearchResult<?>)retorno).getResult().size(); i++) {
					String id = Funciones.retrieveObjectValue(((SearchResult<?>)retorno).getResult().get(i), "id") == null 
							? "" + i 
							: Funciones.retrieveObjectValue(((SearchResult<?>)retorno).getResult().get(i), "id").toString();
					Geometria geometria = (Geometria) Funciones.retrieveObjectValue(((SearchResult<?>)retorno).getResult().get(i), "geometry");
					identificadores.append("<rdf:li resource=\"" + path + id + "\"/>");
					if (geometria != null) {
						anyadirObjeto(objetos, ((SearchResult<?>)retorno).getResult().get(i), peticion, primero, prefijo, path + id);
					}
				}
			} else {
				try {
					String path = Funciones.obtenerPath(retorno.getClass()) + Funciones.retrieveObjectValue(retorno, "id").toString();
					if ("".equals(path)) {
						path = pathProp;
					}
					identificadores.append("<rdf:li resource=\"" + path + "\"/>");
					anyadirObjeto(objetos, retorno, peticion, primero, prefijo, path);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
			}
			respuesta.append("<rdf:RDF xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\" xmlns=\"http://purl.org/rss/1.0/\" xmlns:content=\"http://purl.org/rss/1.0/modules/content/\" xmlns:georss=\"http://www.georss.org/georss\" xmlns:taxo=\"http://purl.org/rss/1.0/modules/taxonomy/\" xmlns:sy=\"http://purl.org/rss/1.0/modules/syndication/\" xmlns:geo=\"http://www.w3.org/2003/01/geo/wgs84_pos#\" xmlns:dcterms=\"http://purl.org/dc/elements/1.1/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:gml=\"http://www.opengis.net/gml\">"
					+ "<channel rdf:about=\"http://www.zaragoza.es" +  Propiedades.getContexto() + link + "\">"
					+ "<title>" + title + "</title>"
					+ "<link>http://www.zaragoza.es" + link + "</link>"
					+ "<description>" + description + "</description>"
					+ "<items><rdf:Seq>" + identificadores + "</rdf:Seq></items>"
					+ "</channel>"
					+ "<image><title>Icono</title>"
					+ "<url>http://www.zaragoza.es/contenidos/iconos/" + icon + ".png</url>"
					+ "<link>http://www.zaragoza.es/contenidos/iconos/" + icon + ".png</link></image>");
			respuesta.append(objetos);
			respuesta.append("</rdf:RDF>");
		} else {
			throw new InvalidImplementationException("Formato no soportado", new FormatoNoSoportadoException());
		}
	}

	private Class<?> descubrirClase(Object retorno) {
		if (retorno instanceof SearchResult) {
			return ((SearchResult<?>)retorno).getResult().get(0).getClass();
		} else {
			return retorno.getClass();
		}
	}

	private void anyadirObjeto(StringBuilder respuesta, Object retorno,
			Peticion peticion, boolean primero, String prefijo, String item) throws InvalidImplementationException {
		TransformadorGenerico transformador = peticion.getFormato().getTransformador();
		boolean primerCampo = true;
		if (!primero) {
			respuesta.append(transformador.getSeparador());
		}
		primero = false;
		String nombreObjeto = retorno.getClass().isAnnotationPresent(XmlRootElement.class) ? retorno.getClass().getAnnotation(XmlRootElement.class).name() : "interno";
		if (item.equals("")) {
			respuesta.append(transformador.getInicioObjeto(nombreObjeto));
		} else {
			respuesta.append("<item rdf:about=\"" + item + "\">");
		}

		for (Field field : retorno.getClass().getDeclaredFields()) {
			if (!"geometry".equals(field.getName()) && TransformadorBasico.transformarCampo(retorno, peticion, prefijo, field)) {
				Object valor = Funciones.retrieveObjectValue(retorno, field.getName());
				if (valor instanceof List) {
					List<?> listado = (List<?>) valor;
					if (!listado.isEmpty()) {
						if (!primerCampo) {
							respuesta.append(transformador.getSeparador());
						} else {
							primerCampo = false;
						}
						String pref = prefijo;
						if (!field.getName().equals(CheckeoParametros.PARAMRESULT)) {
							pref = prefijo + field.getName() + ".";
						}
						respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
						boolean primerInterno = true;
						for (Object object : listado) {
							peticion.establecerLastModified(object);
							if (object instanceof Map<?,?>) {
								if (!primerInterno) {
									respuesta.append(transformador.getSeparador());
								}
								respuesta.append(transformador.getInicioObjeto(field.getName()));
								TransformadorBasico.transformarMap(respuesta, peticion, transformador, true, field, object);
								respuesta.append(transformador.getFinObjeto(field.getName()));
							} else {
								anyadirObjeto(respuesta, object, peticion, primerInterno, pref, "");
							}
							primerInterno = false;
						}
						respuesta.append(transformador.getFinCampoObjeto(field.getName()));
					}
				} else if (valor instanceof Map<?, ?>) {
					primerCampo = TransformadorBasico.transformarMap(respuesta, peticion, transformador, primerCampo, field, valor);
				} else if (valor instanceof Set) {
					Set<?> listado = (Set<?>) valor;
					if (!listado.isEmpty()) {
						if (!primerCampo) {
							respuesta.append(transformador.getSeparador());
						} else {
							primerCampo = false;
						}
						String pref = prefijo;
						if (!field.getName().equals(CheckeoParametros.PARAMRESULT)) {
							pref = prefijo + field.getName() + ".";
						}
						respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
						boolean primerInterno = true;
						for (Object object : listado) {
							peticion.establecerLastModified(object);
							anyadirObjeto(respuesta, object, peticion, primerInterno, pref, "");
							primerInterno = false;
						}
						respuesta.append(transformador.getFinCampoObjeto(field.getName()));
					}
				} else if (valor instanceof Object[]) {
					Object[] listado = (Object[]) valor;
					if (listado.length > 0) {
						if (!primerCampo) {
							respuesta.append(transformador.getSeparador());
						} else {
							primerCampo = false;
						}
						String pref = prefijo;
						if (!field.getName().equals(CheckeoParametros.PARAMRESULT)) {
							pref = prefijo + field.getName() + ".";
						}
						respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
						boolean primerInterno = true;
						for (int i = 0; i < listado.length; i++) {
							Object object = listado[i];
							peticion.establecerLastModified(object);
							if (object instanceof Double) {
								if (i > 0) {
									respuesta.append(",");
								}
								respuesta.append(object.toString());
							} else if (object instanceof String) {
								if (i > 0) {
									respuesta.append(",");
								}
								respuesta.append("\"" + object.toString() + "\"");
							} else {
								anyadirObjeto(respuesta, object, peticion, primerInterno, pref, "");
							}
							primerInterno = false;
						}
						respuesta.append(transformador.getFinCampoObjeto(field.getName()));
					}
				} else  if (field.getType().isAnnotationPresent(XmlRootElement.class)) {
					if (!primerCampo) {
						respuesta.append(transformador.getSeparador());
					} else {
						primerCampo = false;
					}
					respuesta.append(transformador.getInicioCampoObjetoNoListado(field.getName()));
					if (valor instanceof Geometria) {
						respuesta.append(transformador.transformarGeometria((Geometria)valor, peticion));
					} else {
						anyadirObjeto(respuesta, valor, peticion, true, prefijo + field.getName() + ".", "");
					}
					respuesta.append(transformador.getFinCampoObjetoNoListado(field.getName()));
				} else {
					if (!primerCampo) {
						respuesta.append(transformador.getSeparador());
					} else {
						primerCampo = false;
					}
					if (field.getName().equals("id")) {
						respuesta.append(transformador.escribirValorCampo(transformador.getIDPrefijo() + field.getName(), Funciones.obtenerPath(retorno.getClass()) + valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
					} else {
						String propiedad = "";
						
						if (!field.isAnnotationPresent(Rdf.class) || "title".equals(field.getName()) || "description".equals(field.getName()) || "link".equals(field.getName())) {
							propiedad = field.getName();
						} else {
							if ("".equals(field.getAnnotation(Rdf.class).contexto())) {
								if ("".equals(field.getAnnotation(Rdf.class).uri())) {
									// no tiene asociada los valores en la etiqueta
									propiedad = field.getName();
								} else {
									// Coger los valores de uri y prefijo
									propiedad = field.getAnnotation(Rdf.class).prefijo() + ":" + field.getAnnotation(Rdf.class).propiedad();
								}
							} else {
								// coger los valores de la clase Context
								Context contexto = Context.listado.get(field.getAnnotation(Rdf.class).contexto());
								propiedad = contexto.getPrefijo() + ":" + field.getAnnotation(Rdf.class).propiedad();
							}
						}
						respuesta.append(transformador.escribirValorCampo(propiedad, valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
					}
				}
			} else if("geometry".equals(field.getName())){
				Geometria geometria = (Geometria) Funciones.retrieveObjectValue(retorno, "geometry");
				respuesta.append(geometria.asGEORSS(peticion.getSourceSrs(), peticion.getSrsName()));
			}
		}
		if (item.equals("")) {
		respuesta.append(transformador.getFinObjeto(nombreObjeto));
		} else {
			respuesta.append("</item>");
		}
	}
	
	public String getIDPrefijo() {
		return "";
	}

	public String transformarGeometria(Geometria valor, Peticion peticion) {
		return valor.asXML(peticion.getSourceSrs(), peticion.getSrsName());
	}
	public String getInicioCampoObjetoConSub(String nombreCampo) {
		return getInicioObjeto(nombreCampo);
	}
	public String getFinCampoObjetoConSub(String nombreCampo) {
		return getFinObjeto(nombreCampo);
	}
	public String getInicioCampo(String nombreObjeto) {
		return getInicioObjeto(nombreObjeto);
	}
	public String getInicioArray() {
		// Es erroneo analizar como implementarlo si se soporta georss en el body
		return "[";
	}
	public String getInicioArray(String nombreCampo) {
		return getInicioCampoObjeto(nombreCampo);
	}

	public String getFinArray(String nombreCampo) {
		return getFinCampoObjeto(nombreCampo);
	}
}
