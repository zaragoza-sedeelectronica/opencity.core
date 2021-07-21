package org.sede.core.rest.view;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Inheritance;
import javax.persistence.Tuple;
import javax.persistence.TupleElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.ArrayUtils;
import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.HtmlContent;
import org.sede.core.anotaciones.PublicName;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RdfMultiple;
import org.sede.core.anotaciones.ResultsOnly;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.MimeTypes;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.Funciones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.genericdao.search.SearchResult;


public class TransformadorBasico {
	private static final Logger logger = LoggerFactory.getLogger(TransformadorBasico.class);
	
	public void transformarObjeto(StringBuilder respuesta, Object retorno, Peticion peticion, boolean primero, String prefijo) throws InvalidImplementationException {
		TransformadorGenerico transformador = peticion.getFormato().getTransformador();
		// Al modificar esta salida tener en cuenta los transformadores que no la utilizan, por ejemplo geojson
		boolean primerCampo = true;
		if (!primero) {
			respuesta.append(transformador.getSeparador());
		}
		primero = false;

		Field[] camposTransformar = mostrarSoloResultados(peticion, retorno);
		
		boolean mostrarSoloResultados = peticion.isResultsOnly();
		// Lo reiniciamos para que funcione correctamente de forma recursiva
		peticion.setResultsOnly(false);
		String nombreObjeto = retorno.getClass().isAnnotationPresent(XmlRootElement.class) ? retorno.getClass().getAnnotation(XmlRootElement.class).name() : "interno";
		
		if (!mostrarSoloResultados) {
			respuesta.append(transformador.getInicioObjeto(nombreObjeto));
		}
		addJsonLdContext(respuesta, retorno, peticion, prefijo);
		peticion.establecerLastModified(retorno);
		for (Field field : camposTransformar) {
			if (transformarCampo(retorno, peticion, prefijo, field)) {
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
						
						if (mostrarSoloResultados) {
							respuesta.append(transformador.getInicioArray(obtenerNombreCampo(listado, field.getName())));
						} else {
							respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
						}
						boolean primerInterno = true;
						for (Object object : listado) {
							if (object instanceof Map<?,?>) {
								if (!primerInterno) {
									respuesta.append(transformador.getSeparador());
								}
								respuesta.append(transformador.getInicioObjeto(field.getName()));
								transformarMap(respuesta, peticion, transformador, true, field, object);
								respuesta.append(transformador.getFinObjeto(field.getName()));
							} else if (object instanceof String || object instanceof BigDecimal || object instanceof Long || object instanceof Integer) {
								if (!primerInterno) {
									respuesta.append(transformador.getSeparador());
								}
								respuesta.append(transformador.escribirValor(field.getName(), object, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
							} else if (object instanceof Object[]) {
								if (!primerInterno) {
									respuesta.append(transformador.getSeparador());
								}
								transformarArray(respuesta, peticion, prefijo, transformador, true, field, object);
							} else if (object instanceof Tuple) {
								Tuple tuple = ((Tuple)object);
								if (!primerInterno) {
									respuesta.append(transformador.getSeparador());
								}
								List<TupleElement<?>> elements = tuple.getElements();
								boolean primerTuple = true;
								respuesta.append(transformador.getInicioObjeto(field.getName()));
							    for (TupleElement<?> element : elements ) {
							    	if (!primerTuple) {
							    		respuesta.append(transformador.getSeparador());	
							    	}
							    	
							    	primerTuple = false;
							        respuesta.append(transformador.escribirValorCampo(element.getAlias(), tuple.get(element.getAlias()), false, peticion.getTipoEtiquetado()));
							        
							        
							    }
							    respuesta.append(transformador.getFinObjeto(field.getName()));
							} else {
								transformarObjeto(respuesta, object, peticion, primerInterno, pref);
							}
							primerInterno = false;
						}
						if (mostrarSoloResultados) {
							respuesta.append(transformador.getFinArray(obtenerNombreCampo(listado, field.getName())));
						} else {
							respuesta.append(transformador.getFinCampoObjeto(field.getName()));
						}
					}
				} else if (valor instanceof Map<?, ?>) {
					primerCampo = transformarMap(respuesta, peticion, transformador, primerCampo, field, valor);
				} else if (valor instanceof Set) {
					Set<?> listado = (Set<?>) valor;
					try {
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
								transformarObjeto(respuesta, object, peticion, primerInterno, pref);
								primerInterno = false;
							}
							respuesta.append(transformador.getFinCampoObjeto(field.getName()));
						}
					} catch (Exception e) {
						// FIXME Error al acceder a los datos de utes cuando en un licitador no hay utes
						logger.error(e.getMessage());
					}
				} else if (valor instanceof Object[]) {
					primerCampo = transformarArray(respuesta, peticion, prefijo, transformador, primerCampo, field, valor);
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
						transformarObjeto(respuesta, valor, peticion, true, prefijo + field.getName() + ".");
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
					if (!primerCampo) {
						respuesta.append(transformador.getSeparador());
					} else {
						primerCampo = false;
					}
					if ((formatoJsonLD(peticion) || formatoHTML(peticion)) && field.getName().equals("id")) {
						respuesta.append(transformador.escribirValorCampo(field.getName(), Funciones.obtenerPath(retorno.getClass()) + valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
					} else {
						respuesta.append(transformador.escribirValorCampo(obtenerNombreCampo(field), valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
					}
				}
			}
		}
		if (!mostrarSoloResultados) {
			respuesta.append(transformador.getFinObjeto(nombreObjeto));
		}
	}

	private boolean transformarArray(StringBuilder respuesta, Peticion peticion,
			String prefijo, TransformadorGenerico transformador,
			boolean primerCampo, Field field, Object valor)
			throws InvalidImplementationException {
		Object[] listado = (Object[]) valor;

		if (listado.length > 0) {
			if (!primerCampo) {
				respuesta.append(transformador.getSeparador());
			} else {
				primerCampo = false;
			}
			
			String pref = prefijo;
			if (!field.getName().equals(CheckeoParametros.PARAMRESULT) 
					&& !field.getName().equals("records")
					&& (transformador instanceof TransformadorJson 
							|| transformador instanceof TransformadorGeoJson
							|| transformador instanceof TransformadorJsonLd)) {
				pref = prefijo + field.getName() + ".";
				respuesta.append(transformador.getInicioCampo(field.getName()));
			}
			respuesta.append(transformador.getInicioArray(field.getName()));
			boolean primerInterno = true;
			for (int i = 0; i < listado.length; i++) {
				Object object = listado[i];
				if (object instanceof Double || object instanceof Integer || object instanceof Long || object instanceof BigDecimal) {
					if (i > 0) {
						respuesta.append(",");
					}
					respuesta.append(object.toString());
				} else if (object instanceof String) {
					if (i > 0) {
						respuesta.append(",");
					}
					respuesta.append("\"" + Funciones.escape(object.toString()) + "\"");
				} else if (object == null) {
					if (i > 0) {
						respuesta.append(",");
					}
					respuesta.append("null");
				
				} else {
					transformarObjeto(respuesta, object, peticion, primerInterno, pref);
				}
				primerInterno = false;
			}
			respuesta.append(transformador.getFinArray(field.getName()));
		}
		return primerCampo;
	}

	private String obtenerNombreCampo(Field field) {
		if (field.isAnnotationPresent(PublicName.class)) {
			return field.getAnnotation(PublicName.class).value();
		} else {
			return field.getName();
		}
	}
	
	private String obtenerNombreCampo(List<?> listado, String name) {
		if (!listado.isEmpty()) {
			Object o = listado.get(0);
			if (o.getClass().isAnnotationPresent(ResultsOnly.class)) {
				return o.getClass().getAnnotation(ResultsOnly.class).xmlroot();
			} else {
				return name;
			}
		} else {
			return name;
		}
		
	}

	private Field[] mostrarSoloResultados(Peticion peticion, Object retorno) {
		if (retorno instanceof SearchResult) {
			SearchResult<?> ret = (SearchResult<?>) retorno;
			if (ret.getResult() != null && !ret.getResult().isEmpty()) {
				Object o = ret.getResult().get(0);
				if (verSoloResultados(o, peticion)) {
					try {
						peticion.setResultsOnly(true);
						return new Field[]{ret.getClass().getDeclaredField("result")};
					} catch (SecurityException e) {
						return retorno.getClass().getDeclaredFields();
					} catch (NoSuchFieldException e) {
						return retorno.getClass().getDeclaredFields();
					}
				} else {
					return retorno.getClass().getDeclaredFields();
				}
			} else {
				return retorno.getClass().getDeclaredFields();	
			}
		} else {
			if (retorno.getClass().getSuperclass().isAnnotationPresent(Inheritance.class)) {
				return ArrayUtils.addAll(retorno.getClass().getDeclaredFields(), retorno.getClass().getSuperclass().getDeclaredFields());	
			} else {
				return retorno.getClass().getDeclaredFields();
			}
		}
	}

	private boolean verSoloResultados(Object o, Peticion peticion) {
		boolean resultOnlyEnParametro = (peticion.getQueryParams().get(CheckeoParametros.RESULTSONLY) != null);
		if (resultOnlyEnParametro) {
			return peticion.isResultsOnly();
		} else {
			return o.getClass().isAnnotationPresent(ResultsOnly.class);
		}
	}

	private void addJsonLdContext(StringBuilder respuesta, Object retorno, Peticion peticion, String prefijo) {
		if (formatoJsonLD(peticion) && retorno.getClass().isAnnotationPresent(Rdf.class)) {
			respuesta.append("\"@context\":{");
			boolean primero = true;
			for (Field field : retorno.getClass().getDeclaredFields()) {
				if (transformarCampo(retorno, peticion, prefijo, field) 
						&& (field.isAnnotationPresent(Rdf.class) || field.isAnnotationPresent(RdfMultiple.class))) {
					if (!primero) {
						respuesta.append(",");
					} else {
						primero = false;
					}
					StringBuilder uri = new StringBuilder();
					if (field.isAnnotationPresent(RdfMultiple.class)) {
						Rdf[] valores = field.getAnnotation(RdfMultiple.class).value();
						uri.append("[");
						for (int i = 0; i < valores.length; i++) {
							if (i > 0) {
								uri.append(",");
							}
							uri.append(obtenerValorAnotacionRDF(valores[i]));
						}
						uri.append("]");
					} else {
						uri.append(obtenerValorAnotacionRDF(field.getAnnotation(Rdf.class)));
					}
					respuesta.append("\"" + field.getName() + "\":" + uri);
				}
			}
			
			respuesta.append("},");
		}
		
	}

	private boolean formatoJsonLD(Peticion peticion) {
		return peticion.getFormato().getNombre().equals(MimeTypes.JSONLD);
	}
	private boolean formatoHTML(Peticion peticion) {
		return peticion.getFormato().getNombre().equals(MimeTypes.HTML);
	}

	private String obtenerValorAnotacionRDF(Rdf ann) {
		if ("".equals(ann.uri())) {
			return "\"" + Context.listado.get(ann.contexto()).getUri() + ann.propiedad() + "\"";
		} else {
			return "\"" + ann.uri() + "\"";
		}
	}

	public static boolean transformarMap(StringBuilder respuesta, Peticion peticion, TransformadorGenerico transformador, boolean primerCampo, Field field, Object valor)
			throws InvalidImplementationException {
		Map<?,?> listado = (Map<?,?>) valor;
		if (listado.size() > 0) {
			Iterator<?> it = listado.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
			    Object object = listado.get(key);
				if (object instanceof Map) {
					if (!primerCampo) {
						respuesta.append(transformador.getSeparador());
					} else {
						primerCampo = false;
					}
					respuesta.append(transformador.getInicioCampoObjetoConSub(key));	
					transformarMap(respuesta, peticion, transformador, true, field, object);
					respuesta.append(transformador.getFinCampoObjetoConSub(field.getName()));
				} else if (object instanceof Object[]) {
					
					Object[] listadoInt =(Object[]) object;
					
					if (listadoInt.length > 0) {
						if (!primerCampo) {
							respuesta.append(transformador.getSeparador());
						} else {
							primerCampo = false;
						}
						
						respuesta.append(transformador.getInicioCampoObjeto(key));
						boolean primerInterno = true;
						for (int i = 0; i < listadoInt.length; i++) {
							Object objectInt = listadoInt[i];
							if (objectInt instanceof Double) {
								if (i > 0) {
									respuesta.append(",");
								}
								respuesta.append(objectInt.toString());
							} else if (objectInt instanceof String) {
								if (i > 0) {
									respuesta.append(",");
								}
								respuesta.append("\"" + objectInt.toString() + "\"");
							} else {
								transformador.transformarObjeto(respuesta, objectInt, peticion, primerInterno, null);
							}
							primerInterno = false;
						}
						respuesta.append(transformador.getFinCampoObjeto(key));
					}
				} else {
					if (peticion.quiereVerCampo("", key, peticion.getSelectedFields())) {
						if (!primerCampo) {
							respuesta.append(transformador.getSeparador());
						} else {
							primerCampo = false;
						}
						
						if (object.getClass().isAnnotationPresent(XmlRootElement.class)) {
							StringBuilder datosObj = new StringBuilder();
							transformador.transformarObjeto(datosObj, object, peticion, true, "");
							respuesta.append(transformador.escribirValorObjecto(key, datosObj.toString(), field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
						} else {
							respuesta.append(transformador.escribirValorCampo(key, object, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
						}
					}
				}
			}
		}
		return primerCampo;
	}

	public static boolean transformarCampo(Object retorno, Peticion peticion, String prefijo, Field field) {
		return peticion.puedeVerCampoEnSeccion(field, peticion.getPermisosEnSeccion(), peticion.getMetodo())
				&& peticion.quiereVerCampo(prefijo, field.getName(), peticion.getSelectedFields())
				&& Funciones.retrieveObjectValue(retorno, field.getName()) != null;
	}
	public static boolean transformarCampoCSV(Object retorno, Peticion peticion, String prefijo, Field field) {
		return peticion.puedeVerCampoEnSeccion(field, peticion.getPermisosEnSeccion(), peticion.getMetodo())
				&& peticion.quiereVerCampo(prefijo, field.getName(), peticion.getSelectedFields());
	}
}
