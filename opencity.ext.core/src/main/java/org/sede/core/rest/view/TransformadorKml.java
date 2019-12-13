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

import org.sede.core.anotaciones.HtmlContent;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.exception.FormatoNoSoportadoException;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.GeoJson;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;

import com.googlecode.genericdao.search.SearchResult;


public class TransformadorKml implements TransformadorGenerico {
	private Html2MarkDown transformadorMarkDown = new Html2MarkDown();

	public String getSeparador() {
		return "";
	}

	public String getInicioObjeto(final String nombreObjeto) {
		return "<ExtendedData><SchemaData schemaUrl=\"#kmlData\">";
	}

	public String getFinObjeto(final String nombreObjeto) {
		return "</SchemaData></ExtendedData>";
	}
	
	public String getInicioCampoObjeto(final String nombreCampo) {
		return "<SimpleData name=\"" + nombreCampo +"\">";
	}

	public String getFinCampoObjeto(final String nombreCampo) {
		return "</SimpleData>";
	}

	public String escribirValorCampo(final String nombreCampo, final Object valor, boolean anotacionPresente, String formato) {
		if (valor instanceof Date) {
			Date fecha = (Date) valor;
			return ConvertDate.date2String(fecha, ConvertDate.ISO8601_FORMAT);
		} else {
			String txt = valor.toString();
			
			if (anotacionPresente && CheckeoParametros.RESPUESTAMARKDOWN.equals(formato)) {
				txt = Funciones.escape(transformadorMarkDown.transformar(txt)); 
			}
			
			if ((txt.indexOf('<') > 0 || txt.indexOf('>') > 0 || txt.indexOf('&') > 0) && txt.indexOf("<![CDATA[") < 0) {
				txt = "<![CDATA[" + txt + "]]>";
			}
			
			return txt;
		}
	}
	public String escribirValor(String name, Object object, boolean anotacionPresente, String formato) {
		return escribirValorCampo(name, object, anotacionPresente, formato);
	}
	public String escribirValorObjecto(String nombreCampo, String valor, boolean anotacionPresente, String formato) {
		return escribirValor(nombreCampo, valor, anotacionPresente, formato);
	}
	public void enviarError(HttpServletResponse response, int statusCode, String mensaje, boolean peticionSoloHead) {
		response.setContentType("text/csv; charset=UTF-8");
		try {
			response.setStatus(statusCode);
			if (!peticionSoloHead) {
				response.getWriter().write(mensaje);
			}
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e) {
			
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
		respuesta.append("<?xml version=\"1.0\" encoding=\"utf-8\" ?><kml xmlns=\"http://www.opengis.net/kml/2.2\">" + 
				"<Document id=\"root_doc\">" + 
				"<Schema name=\"clavosPol01a40\" id=\"kmlData\">");
		for (Field field : clase.getDeclaredFields()) {
			if (TransformadorBasico.transformarCampoCSV(retorno, peticion, prefijo, field)) {
				
				respuesta.append("<SimpleField name=\"" + field.getName() + "\" type=\"" + valorPrimitivo(field.getType()) + "\"/>");
			}
		}
		respuesta.append("</Schema>");
		respuesta.append(System.getProperty("line.separator"));
		respuesta.append("<Folder><name>" + clase.getAnnotation(GeoJson.class).title() + "</name>");
		StringBuilder objetos = new StringBuilder();
		if (retorno instanceof SearchResult) {
			for (int i = 0; i < ((SearchResult<?>)retorno).getResult().size(); i++) {
				Geometria geometria = (Geometria) Funciones.retrieveObjectValue(((SearchResult<?>)retorno).getResult().get(i), "geometry");
				
				if (geometria != null) {
					objetos.append("<Placemark>");
					String title = (String) Funciones.retrieveObjectValue(((SearchResult<?>)retorno).getResult().get(i), "title");
					if (title != null) {
						objetos.append("<name>" + title + "</name>");
					}
					String description = (String) Funciones.retrieveObjectValue(((SearchResult<?>)retorno).getResult().get(i), "description");
					if (description != null) {
						
						if (CheckeoParametros.RESPUESTAMARKDOWN.equals(peticion.getTipoEtiquetado())) {
							description = transformadorMarkDown.transformar(description) + "";
						}
						if ((description.indexOf('<') > 0 || description.indexOf('>') > 0 || description.indexOf('&') > 0) && description.indexOf("<![CDATA[") < 0) {
							description = "<![CDATA[" + description + "]]>";
						}
						
						objetos.append("<description>" + description + "</description>");
					}
					anyadirObjeto(objetos, ((SearchResult<?>)retorno).getResult().get(i), peticion, primero, prefijo);
					objetos.append("<Point><coordinates>" + geometria.asCSV(peticion.getSourceSrs(), CheckeoParametros.SRSWGS84) + "</coordinates></Point>");
					objetos.append("</Placemark>");
				}
				objetos.append(System.getProperty("line.separator"));
			}
		} else {
			anyadirObjeto(objetos, retorno, peticion, primero, prefijo);
			objetos.append(System.getProperty("line.separator"));
		}
		respuesta.append(objetos);
		respuesta.append("</Folder></Document></kml>");
	}

	private Class<?> descubrirClase(Object retorno) {
		if (retorno instanceof SearchResult) {
			return ((SearchResult<?>)retorno).getResult().get(0).getClass();
		} else {
			return retorno.getClass();
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

		for (Field field : retorno.getClass().getDeclaredFields()) {
			if (TransformadorBasico.transformarCampo(retorno, peticion, prefijo, field)) {
				Object valor = Funciones.retrieveObjectValue(retorno, field.getName());
				
				if (valor == null) {
					if (!primerCampo) {
						respuesta.append(transformador.getSeparador());
					} else {
						primerCampo = false;
					}
					respuesta.append("\"\"");
				} else {
					if (valor instanceof List) {
						List<?> listado = (List<?>) valor;
						if (!listado.isEmpty()) {
							if (!primerCampo) {
								respuesta.append(transformador.getSeparador());
							} else {
								primerCampo = false;
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
									anyadirIdObjeto(respuesta, object);
								}
								primerInterno = false;
							}
							respuesta.append(transformador.getFinCampoObjeto(field.getName()));
						} else {
							if (!primerCampo) {
								respuesta.append(transformador.getSeparador());
							} else {
								primerCampo = false;
							}
							respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
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
							respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
							boolean primerInterno = true;
							for (Object object : listado) {
								if (!primerInterno) {
									respuesta.append(",");
								}
								peticion.establecerLastModified(object);
								anyadirIdObjeto(respuesta, object);
								primerInterno = false;
							}
							respuesta.append(transformador.getFinCampoObjeto(field.getName()));
						} else {
							if (!primerCampo) {
								respuesta.append(transformador.getSeparador());
							} else {
								primerCampo = false;
							}
							respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
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
							respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
							
							for (int i = 0; i < listado.length; i++) {
								Object object = listado[i];
								peticion.establecerLastModified(object);
								if (i > 0) {
									respuesta.append(";");
								}
								if (object instanceof Double) {
									respuesta.append(object.toString());
								} else if (object instanceof String) {
									respuesta.append("\"" + object.toString() + "\"");
								} else {
									anyadirIdObjeto(respuesta, object);
								}
								
							}
							respuesta.append(transformador.getFinCampoObjeto(field.getName()));
						} else {
							if (!primerCampo) {
								respuesta.append(transformador.getSeparador());
							} else {
								primerCampo = false;
							}
							respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
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
							anyadirIdObjeto(respuesta, valor);
						}
						respuesta.append(transformador.getFinCampoObjetoNoListado(field.getName()));
					} else if (valor instanceof Geometria) {
						if (!primerCampo) {
							respuesta.append(transformador.getSeparador());
						} else {
							primerCampo = false;
						}
						respuesta.append(transformador.getInicioCampoObjetoNoListado(field.getName()));
						respuesta.append(transformador.transformarGeometria((Geometria)valor, peticion));
						respuesta.append(transformador.getFinCampoObjetoNoListado(field.getName()));
					} else {
						if ("image".equals(field.getName())) {
							respuesta.append(transformador.getInicioCampoObjetoNoListado("gx_media_links"));
						} else {
							respuesta.append(transformador.getInicioCampoObjetoNoListado(field.getName()));
						}
						
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
					
						respuesta.append(transformador.getFinCampoObjetoNoListado(field.getName()));
					}
				}
			}
		}
		respuesta.append(transformador.getFinObjeto(nombreObjeto));
	}
	
	private void anyadirIdObjeto(StringBuilder respuesta, Object object) {
		
		Object id = Funciones.retrieveObjectValue(object, "id");
		if (id != null) {
			respuesta.append(Funciones.obtenerPath(object.getClass()) + id);
		}
		
	}

	public String getIDPrefijo() {
		return "";
	}

	public String transformarGeometria(Geometria valor, Peticion peticion) {
		return valor.asCSV(peticion.getSourceSrs(), peticion.getSrsName());
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
	
	private String valorPrimitivo(Class<?> clase) {
		String simpleName = clase.getSimpleName();
		String retorno = simpleName;
		if ("Boolean".equals(simpleName)) {
			retorno = "boolean";
		} else if ("String".equals(simpleName)) {
			retorno = "string";
		} else if ("Integer".equals(simpleName)) {
			retorno = "integer";
		} else if ("Double".equals(simpleName)) {
			retorno = "double";
		} else if ("BigDecimal".equals(simpleName)) {
			retorno = "float";
		} else if ("Float".equals(simpleName)) {
			retorno = "float";
		} else if ("Date".equals(simpleName)) {
			retorno = "string";
		} else if (clase.isAnnotationPresent(XmlRootElement.class)) {
			retorno = clase.getAnnotation(XmlRootElement.class).name();
		}

		return retorno;
	}
}
