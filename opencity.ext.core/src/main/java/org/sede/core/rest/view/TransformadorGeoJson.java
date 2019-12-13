package org.sede.core.rest.view;


import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
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
import org.sede.core.anotaciones.PublicName;
import org.sede.core.exception.FormatoNoSoportadoException;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;

import com.googlecode.genericdao.search.SearchResult;


public class TransformadorGeoJson implements TransformadorGenerico{
	private Html2MarkDown transformadorMarkDown = new Html2MarkDown();

	public String getSeparador() {
		return ",";
	}

	public String getInicioObjeto(String nombreObjeto) {
		return "{";
	}
	
	public String getFinObjeto(String nombreObjeto) {
		return "}";
	}
	public String getInicioCampo(String nombreCampo) {
		return "\"" + nombreCampo + "\":";
	}
	public String getInicioCampoObjeto(String nombreCampo) {
		return "\"" + nombreCampo + "\":[";
	}

	public String getFinCampoObjeto(String nombreCampo) {
		return "]";
	}

	public String escribirValorCampo(String nombreCampo, Object valor, boolean anotacionPresente, String formato) {
		if (valor instanceof Integer || valor instanceof Double || valor instanceof BigDecimal) {
			return "\"" + nombreCampo + "\":" + Funciones.escape(valor.toString()) + "";
		} else if (valor instanceof Date) {
			Date fecha = (Date) valor;
			return "\"" + nombreCampo + "\":\"" + ConvertDate.date2String(fecha, ConvertDate.ISO8601_FORMAT) + "\"";
		} else {
			if (anotacionPresente && CheckeoParametros.RESPUESTAMARKDOWN.equals(formato)) {
				return "\"" + nombreCampo + "\":\"" + Funciones.escape(transformadorMarkDown.transformar(valor.toString())) + "\"";
			} else {
				return "\"" + nombreCampo + "\":\"" + Funciones.escape(valor.toString()) + "\"";
			}
		}
	}

	public String escribirValor(String nombre, Object valor, boolean anotacionPresente, String formato) {
		if (valor instanceof Integer || valor instanceof Double || valor instanceof BigDecimal || valor instanceof Boolean) {
			return Funciones.escape(valor.toString());
		} else if (valor instanceof Date) {
			Date fecha = (Date) valor;
			return "\"" + ConvertDate.date2String(fecha, ConvertDate.ISO8601_FORMAT) + "\"";
		} else {
			if (anotacionPresente && CheckeoParametros.RESPUESTAMARKDOWN.equals(formato)) {
				return "\"" + Funciones.escape(transformadorMarkDown.transformar(valor.toString())) + "\"";
			} else {
				return "\"" + Funciones.escape(valor.toString()) + "\"";
			}
		}
	}
	public String escribirValorObjecto(String nombreCampo, String valor, boolean anotacionPresente, String formato) {
		if (anotacionPresente && CheckeoParametros.RESPUESTAMARKDOWN.equals(formato)) {
			return "\"" + nombreCampo + "\":" + transformadorMarkDown.transformar(valor) + "";
		} else {
			return "\"" + nombreCampo + "\":" + valor + "";
		}
	}
	public void enviarError(HttpServletResponse response, int statusCode, String mensaje, boolean peticionSoloHead) {
		String respuesta = "{\"error\":\"" + mensaje + "\"}";
		response.setContentType("application/json; charset=UTF-8");
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
		return "\"" + nombreCampo + "\":";
	}

	public String getFinCampoObjetoNoListado(String nombreCampo) {
		return "";
	}

	public void transformarObjeto(StringBuilder respuesta, Object retorno,
			Peticion peticion, boolean primero, String prefijo)
			throws InvalidImplementationException {
		
		Class<?> clase = Funciones.descubrirClase(retorno);
		if (clase.isAnnotationPresent(GeoJson.class)) {
			String title = clase.getAnnotation(GeoJson.class).title();
			String icon = clase.getAnnotation(GeoJson.class).icon();
			String link = clase.getAnnotation(GeoJson.class).link();
			String description = clase.getAnnotation(GeoJson.class).description();
			if ("".equals(title) && retorno instanceof SearchResult) {
				SearchResult<?> c = (SearchResult<?>) retorno;
				title = (String)c.getPropiedades().get("title");
				link = (String)c.getPropiedades().get("link");
				description = (String)c.getPropiedades().get("description");
				peticion.setLastModified((Date)c.getPropiedades().get("lastUpdated"));
			}
			if (retorno instanceof SearchResult) {
				SearchResult<?> c = (SearchResult<?>) retorno;
				if (c.getPropiedades() != null && c.getPropiedades().containsKey("icon")) {
					icon = (String)c.getPropiedades().get("icon");
				}
			}
			respuesta.append("{\"type\":\"FeatureCollection\",");
			
			if (peticion.getSrsName().equals(CheckeoParametros.SRSUTM30N)) {
				respuesta.append("\"crs\":{\"type\":\"EPSG\",\"properties\":{\"code\":23030}},");
			} else if (peticion.getSrsName().equals(CheckeoParametros.SRSETRS89)) {
				respuesta.append("\"crs\":{\"type\":\"EPSG\",\"properties\":{\"code\":25830}},");
			} else {
				if (!peticion.getQueryParams().containsKey(CheckeoParametros.PARAMREMOVEPROPERTIES)) {
					respuesta.append("\"crs\":{\"type\":\"name\",\"properties\":{\"name\":\"urn:ogc:def:crs:OGC:1.3:CRS84\"}},");
				}
			}
			
			if (peticion.getQueryParams().containsKey(CheckeoParametros.GEORSS_ICON)) {
				icon = peticion.getQueryParams().get(CheckeoParametros.GEORSS_ICON)[0];
			}
			if (!peticion.getQueryParams().containsKey(CheckeoParametros.PARAMREMOVEPROPERTIES)) {
				respuesta.append("\"properties\":{"
						+ "\"title\":\"" + title + "\","
						+ "\"icon\":\"https://www.zaragoza.es/contenidos/iconos/" + icon +".png\","
						+ "\"link\":\"http://www.zaragoza.es" + link + "\","
						+ "\"description\":\"" + description + "\"},");
			}
			respuesta.append("\"features\":");
			boolean primerElem = true;
			if (retorno instanceof SearchResult) {
				respuesta.append("[");
				for (int i = 0; i < ((SearchResult<?>)retorno).getResult().size(); i++) {
					Geometria geometria = (Geometria) Funciones.retrieveObjectValue(((SearchResult<?>)retorno).getResult().get(i), "geometry");
					if (geometria != null) {
						if (!primerElem) {
							respuesta.append(",");
						}
						primerElem = false;
						respuesta.append("{\"type\":\"Feature\","
								+ "\"geometry\":" + geometria.asJson(peticion.getSourceSrs(), peticion.getSrsName()) + ","
								+ "\"properties\":");
						anyadirObjeto(respuesta, ((SearchResult<?>)retorno).getResult().get(i), peticion, primero, prefijo);
						respuesta.append("}");
					}
				}
				respuesta.append("]");
			} else {
				respuesta.append("[");
				Geometria geometria = (Geometria) Funciones.retrieveObjectValue(retorno, "geometry");
				respuesta.append("{\"type\":\"Feature\","
						+ "\"geometry\":" + geometria.asJson(peticion.getSourceSrs(), peticion.getSrsName()) + ","
						+ "\"properties\":");
				anyadirObjeto(respuesta, retorno, peticion, primero, prefijo);
				respuesta.append("}");
				respuesta.append("]");
			}
			
			respuesta.append("}");
		} else {
			throw new InvalidImplementationException("Formato no soportado", new FormatoNoSoportadoException());
		}
	}
	

	private void anyadirObjeto(StringBuilder respuesta, Object retorno,
			Peticion peticion, boolean primero, String prefijo) throws InvalidImplementationException {
		TransformadorGenerico transformador = peticion.getFormato().getTransformador();
		boolean primerCampo = true;
		if (!primero) {
			respuesta.append(transformador.getSeparador());
		}
		primero = false;
		String nombreObjeto = retorno.getClass().isAnnotationPresent(XmlRootElement.class) ? retorno.getClass().getAnnotation(XmlRootElement.class).name() : "interno";
		respuesta.append(transformador.getInicioObjeto(nombreObjeto));
		peticion.establecerLastModified(retorno);
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
								anyadirObjeto(respuesta, object, peticion, primerInterno, pref);
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
							anyadirObjeto(respuesta, object, peticion, primerInterno, pref);
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
								anyadirObjeto(respuesta, object, peticion, primerInterno, pref);
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
						anyadirObjeto(respuesta, valor, peticion, true, prefijo + field.getName() + ".");
					}
					respuesta.append(transformador.getFinCampoObjetoNoListado(field.getName()));
				} else {
					if (!primerCampo) {
						respuesta.append(transformador.getSeparador());
					} else {
						primerCampo = false;
					}
					respuesta.append(transformador.escribirValorCampo(obtenerNombreCampo(field), valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
				}
			}
		}
		respuesta.append(transformador.getFinObjeto(nombreObjeto));
		
		
	}
	private String obtenerNombreCampo(Field field) {
		if (field.isAnnotationPresent(PublicName.class)) {
			return field.getAnnotation(PublicName.class).value();
		} else {
			return field.getName();
		}
	}
	public String getIDPrefijo() {
		return "";
	}

	public String transformarGeometria(Geometria valor, Peticion peticion) {
		return valor.asJson(peticion.getSourceSrs(), peticion.getSrsName());
	}

	public String getFinCampoObjetoConSub(String nombreCampo) {
		return "}";
	}
	public String getInicioCampoObjetoConSub(String nombreCampo) {
		return "\"" + nombreCampo + "\":{";
	}
	public String getInicioArray() {
		return "[";
	}
	public String getInicioArray(String nombreCampo) {
		return getInicioArray(nombreCampo);
	}

	public String getFinArray(String nombreCampo) {
		return getFinCampoObjeto(nombreCampo);
	}
}
