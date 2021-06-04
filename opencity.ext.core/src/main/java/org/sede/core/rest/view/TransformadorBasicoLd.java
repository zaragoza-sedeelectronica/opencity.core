package org.sede.core.rest.view;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.HtmlContent;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RdfMultiple;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.MimeTypes;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.Funciones;


public class TransformadorBasicoLd {
	
//	ESTA VARIABLE ALMACENA LOS IDS Y LAS URIS DE LOS ELEMENTOS QUE FORMAN EL CONTEXTO
//	SE UTILIZA PARA EVITAR AÑADIR ELEMENTOS QUE YA SE HAN RECONOCIDO
	Map<String, String> context = new HashMap<String, String>();
	String uriBase = "http://vocab.linkeddata.es";
	
	public void transformarObjeto(StringBuilder respuesta, Object retorno, Peticion peticion, boolean primero, String prefijo) throws InvalidImplementationException {
		TransformadorGenerico transformador = peticion.getFormato().getTransformador();
		// Al modificar esta salida tener en cuenta los transformadores que no la utilizan, por ejemplo geojson
		
		boolean primerCampo = true;
		if (!primero) {
			respuesta.append(transformador.getSeparador());
		}
		primero = false;

		String nombreObjeto = retorno.getClass().isAnnotationPresent(XmlRootElement.class) ? retorno.getClass().getAnnotation(XmlRootElement.class).name() : "interno";
		respuesta.append(transformador.getInicioObjeto(nombreObjeto));
		
		addJsonLdContext(respuesta, retorno, peticion, prefijo);
		
		peticion.establecerLastModified(retorno);
		for (Field field : retorno.getClass().getDeclaredFields()) {
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
						respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
						boolean primerInterno = true;
						for (Object object : listado) {
							if (object instanceof Map<?,?>) {
								if (!primerInterno) {
									respuesta.append(transformador.getSeparador());
								}
								respuesta.append(transformador.getInicioObjeto(field.getName()));
								transformarMap(respuesta, peticion, transformador, true, field, object);
								respuesta.append(transformador.getFinObjeto(field.getName()));
							} else if (object instanceof String) {
								if (!primerInterno) {
									respuesta.append(transformador.getSeparador());
								}
								respuesta.append(transformador.escribirValor(field.getName(), object, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
							} else {
								transformarObjetoPrivate(respuesta, object, peticion, primerInterno, pref);
							}
							primerInterno = false;
							
							context.clear();
						}
						respuesta.append(transformador.getFinCampoObjeto(field.getName()));
					}
				} else if (valor instanceof Map<?, ?>) {
					primerCampo = transformarMap(respuesta, peticion, transformador, primerCampo, field, valor);
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
							transformarObjetoPrivate(respuesta, object, peticion, primerInterno, pref);
							primerInterno = false;
							context.clear();
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
								transformarObjetoPrivate(respuesta, object, peticion, primerInterno, pref);
								context.clear();
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
						transformarObjetoPrivate(respuesta, valor, peticion, true, prefijo + field.getName() + ".");
						if (field.isAnnotationPresent(RdfMultiple.class)){
							String id = obtenerId(valor);
							boolean first = true;
							respuesta.append(",");
							for (int i=1;i<field.getAnnotation(RdfMultiple.class).value().length;i++){
								String append = "\""+field.getName()+"_"+i+"\" : {"
								+"\"@id\":\""+id+"\"}";
								if (!first)
									respuesta.append(",");
								respuesta.append(append);
								first = false;
							}
						}
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
					if ((formatoJsonLD(peticion) || formatoHTML(peticion)) && (field.getName().equals("id") || field.isAnnotationPresent(Id.class))) {
						respuesta.append(transformador.escribirValorCampo("@id", Funciones.obtenerPath(retorno.getClass()) + valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
						respuesta.append(",");
						if (retorno.getClass().isAnnotationPresent(Rdf.class) || retorno.getClass().isAnnotationPresent(RdfMultiple.class)){
							if (retorno.getClass().isAnnotationPresent(Rdf.class)){
								String rdf = obtenerValorAnotacionRDF(retorno.getClass().getAnnotation(Rdf.class)).replaceAll("\"", "");
								if (!rdf.startsWith("http://"))
									rdf = uriBase + rdf;
								if (rdf.endsWith("/"))
									rdf = rdf.substring(0,rdf.lastIndexOf('/'));
								respuesta.append(transformador.escribirValorCampo("@type", rdf, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
								respuesta.append(",");
							} else if (retorno.getClass().isAnnotationPresent(RdfMultiple.class)){
								Rdf[] valores = retorno.getClass().getAnnotation(RdfMultiple.class).value();
								String fullRdf = "[";
								boolean first = true;
								for (Rdf val:valores){								
									String rdf = obtenerValorAnotacionRDF(val).replaceAll("\"", "");
									if (!rdf.startsWith("http://"))
										rdf = uriBase + rdf;
									if (rdf.endsWith("/"))
										rdf = rdf.substring(0,rdf.lastIndexOf('/'));								
									if (!first)
										fullRdf=fullRdf + ",";
									fullRdf = fullRdf + "\"" + rdf + "\"";
									first = false;								
								}
								fullRdf = fullRdf + "]";
								respuesta.append("\"@type\":" + fullRdf + ",");
									
							}
						}
					}
					if (field.isAnnotationPresent(RdfMultiple.class)) {
						Rdf[] valores = field.getAnnotation(RdfMultiple.class).value();
						for (int i=0;i<valores.length;i++){
							if (i>0)
								respuesta.append(transformador.escribirValorCampo(obtenerContextRDF(valores[i]), valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
							else
								respuesta.append(transformador.escribirValorCampo(field.getName(), valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
							if (i<valores.length-1)
								respuesta.append(",");
						}
					} else {
						respuesta.append(transformador.escribirValorCampo(field.getName(), valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
					}
				}
				
			}
		}
		respuesta.append(transformador.getFinObjeto(nombreObjeto));
	}
	
	private void transformarObjetoPrivate(StringBuilder respuesta, Object retorno, Peticion peticion, boolean primero, String prefijo) throws InvalidImplementationException {
		TransformadorGenerico transformador = peticion.getFormato().getTransformador();
		// Al modificar esta salida tener en cuenta los transformadores que no la utilizan, por ejemplo geojson
		
		boolean primerCampo = true;
		if (!primero) {
			respuesta.append(transformador.getSeparador());
		}
		primero = false;

		String nombreObjeto = retorno.getClass().isAnnotationPresent(XmlRootElement.class) ? retorno.getClass().getAnnotation(XmlRootElement.class).name() : "interno";
		respuesta.append(transformador.getInicioObjeto(nombreObjeto));
		
		addJsonLdContext(respuesta, retorno, peticion, prefijo);
		
		peticion.establecerLastModified(retorno);
		
		for (Field field : retorno.getClass().getDeclaredFields()) {
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
						respuesta.append(transformador.getInicioCampoObjeto(field.getName()));
						boolean primerInterno = true;
						for (Object object : listado) {
							if (object instanceof Map<?,?>) {
								if (!primerInterno) {
									respuesta.append(transformador.getSeparador());
								}
								respuesta.append(transformador.getInicioObjeto(field.getName()));
								transformarMap(respuesta, peticion, transformador, true, field, object);
								respuesta.append(transformador.getFinObjeto(field.getName()));
							} else if (object instanceof String) {
								if (!primerInterno) {
									respuesta.append(transformador.getSeparador());
								}
								respuesta.append(transformador.escribirValor(field.getName(), object, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
							} else {
								transformarObjetoPrivate(respuesta, object, peticion, primerInterno, pref);
							}
							primerInterno = false;
						}
						respuesta.append(transformador.getFinCampoObjeto(field.getName()));
					}
				} else if (valor instanceof Map<?, ?>) {
					primerCampo = transformarMap(respuesta, peticion, transformador, primerCampo, field, valor);
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
							transformarObjetoPrivate(respuesta, object, peticion, primerInterno, pref);
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
								transformarObjetoPrivate(respuesta, object, peticion, primerInterno, pref);
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
						transformarObjetoPrivate(respuesta, valor, peticion, true, prefijo + field.getName() + ".");
						if (field.isAnnotationPresent(RdfMultiple.class)){
							String id = obtenerId(valor);
							boolean first = true;
							respuesta.append(",");
							for (int i=1;i<field.getAnnotation(RdfMultiple.class).value().length;i++){
								String append = "\""+field.getName()+"_"+i+"\" : {"
								+"\"@id\":\""+id+"\"}";
								if (!first)
									respuesta.append(",");
								respuesta.append(append);
								first = false;
							}
						}
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
					if ((formatoJsonLD(peticion) || formatoHTML(peticion)) && (field.getName().equals("id")|| field.isAnnotationPresent(Id.class))) {
						respuesta.append(transformador.escribirValorCampo("@id", Funciones.obtenerPath(retorno.getClass()) + valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
						respuesta.append(",");
						if (retorno.getClass().isAnnotationPresent(Rdf.class)){							
							String rdf = obtenerValorAnotacionRDF(retorno.getClass().getAnnotation(Rdf.class)).replaceAll("\"", "");
							if (!rdf.startsWith("http://"))
								rdf = uriBase + rdf;
							if (rdf.endsWith("/"))
								rdf = rdf.substring(0,rdf.lastIndexOf('/'));
							respuesta.append(transformador.escribirValorCampo("@type", rdf, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
							respuesta.append(",");
						} else if (retorno.getClass().isAnnotationPresent(RdfMultiple.class)){
							Rdf[] valores = retorno.getClass().getAnnotation(RdfMultiple.class).value();
							String fullRdf = "[";
							boolean first = true;
							for (Rdf val:valores){								
								String rdf = obtenerValorAnotacionRDF(val).replaceAll("\"", "");
								if (!rdf.startsWith("http://"))
									rdf = uriBase + rdf;
								if (rdf.endsWith("/"))
									rdf = rdf.substring(0,rdf.lastIndexOf('/'));								
								if (!first)
									fullRdf=fullRdf + ",";
								fullRdf = fullRdf + "\"" + rdf + "\"";
								first = false;								
							}
							fullRdf = fullRdf + "]";
							respuesta.append("\"@type\":" + fullRdf + ",");
								
						}
					}
					if (field.isAnnotationPresent(RdfMultiple.class)) {
						Rdf[] valores = field.getAnnotation(RdfMultiple.class).value();
						for (int i=0;i<valores.length;i++){
							if (i>0)
								respuesta.append(transformador.escribirValorCampo(obtenerContextRDF(valores[i]), valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
							else
								respuesta.append(transformador.escribirValorCampo(field.getName(), valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
							if (i<valores.length-1)
								respuesta.append(",");
						}
					} else {
						respuesta.append(transformador.escribirValorCampo(field.getName(), valor, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado()));
					}					
				}
			}
		}
		respuesta.append(transformador.getFinObjeto(nombreObjeto));
	}
		
	private void addJsonLdContext(StringBuilder inRespuesta, Object retorno, Peticion peticion, String prefijo) {
		StringBuilder respuesta = new StringBuilder();
		if (formatoJsonLD(peticion) && (retorno.getClass().isAnnotationPresent(Rdf.class) || retorno.getClass().isAnnotationPresent(RdfMultiple.class))) {
			respuesta.append("\"@context\":{");
			boolean primero = true;
			for (Field field : retorno.getClass().getDeclaredFields()) {
				if (transformarCampo(retorno, peticion, prefijo, field) 
						&& (field.isAnnotationPresent(Rdf.class) || field.isAnnotationPresent(RdfMultiple.class))) {
					if (!context.containsKey(field.getName())){
						if (!primero) {
							respuesta.append(",");
						} else {
							primero = false;
						}
						StringBuilder uri = new StringBuilder();
						
//						ASUMIMOS QUE LA ANOTACION MULTIPLE ESTARA COMPUESTA SOLO DE DOS ELEMENTOS
//						   - EL PRIMERO SERA EL TYPE
//						   - EL SEGUNDO SERA EL ID
						if (field.isAnnotationPresent(RdfMultiple.class)) {
							Rdf[] valores = field.getAnnotation(RdfMultiple.class).value();
							for (int i=0;i<valores.length;i++){
								uri.append("{");
								uri.append("\"@id\": "+obtenerValorAnotacionRDF(valores[i]));
								uri.append("}");
								if (i > 0){
									respuesta.append("\"" + obtenerContextRDF(valores[i])+"\":" + uri);
									context.put(obtenerContextRDF(valores[i]), uri.toString());
								}
								else
									respuesta.append("\"" + field.getName() + "\":" + uri);
								if (i<valores.length-1)
									respuesta.append(",");
								uri = new StringBuilder();
							}
						} else {
							if (field.getName().equals("geometry")){
								uri.append(addGeometryContext(peticion.getSrsName()));
								respuesta.append(uri);
							}
							else{
								uri.append(obtenerValorAnotacionRDF(field.getAnnotation(Rdf.class)));
								respuesta.append("\"" + field.getName() + "\":" + uri);
							}
						}
						context.put(field.getName(), uri.toString());						
					}
					
//					OBTENEMOS EL VALOR DEL CAMPO Y COMPROBAMOS SI CONTIENE ELEMENTOS ANOTADOS
					Object valor = Funciones.retrieveObjectValue(retorno, field.getName());
					// si no es una geomatria, seguimos con el metodo containsXmlRootElement
					// si es una geometria, ya hemos anyadido el contexto de la misma
					if (!field.getName().equals("geometry"))
						containsXmlRootElement(valor, respuesta, primero);
				}
			}
			
			respuesta.append("},");
			
		}

//		SOLO ANYADIMOS EL CAMPO CONTEXTO SI CONTIENE ELEMENTOS
		if (!respuesta.toString().equals("\"@context\":{},")){

			if (respuesta.toString().contains("lon"))
				respuesta = new StringBuilder(respuesta.toString().replaceAll("\"lon\"", "\"long\""));
			inRespuesta.append(respuesta);
		}
		
	}
	
	private String addGeometryContext(String srsName){
		String output = "";
		if (srsName.equals(CheckeoParametros.SRSUTM30N)){
			output = "\"geometry\" : \"http://www.opengis.net/ont/sf#hasGeometry\","
				+ "\"asWKT\" : {" 
	    		+ "\"@id\": \"http://www.opengis.net/ont/geosparql#asWKT\","
	    		+ "\"@type\": \"http://www.opengis.net/ont/geosparql#wktLiteral\""
	    		+ "}";
		} else {
			output = "\"geometry\" : \"http://www.w3.org/2003/01/geo/wgs84_pos#geometry\","
			+ "\"lat\" : \"http://www.w3.org/2003/01/geo/wgs84_pos#lat\","
    		+ "\"long\": \"http://www.w3.org/2003/01/geo/wgs84_pos#long\","
    		+ "\"asWKT\" : {" 
    		+ "\"@id\": \"http://www.opengis.net/ont/geosparql#asWKT\","
    		+ "\"@type\": \"http://www.opengis.net/ont/geosparql#wktLiteral\""
    		+ "}";
		}
		return output;
	}
	
	private boolean containsXmlRootElement(Object object, StringBuilder respuesta, boolean primero){
		
		if (object instanceof Set) {
			Set<?> listado = (Set<?>) object;
			for (Object obj : listado){			
				if (obj.getClass().isAnnotationPresent(XmlRootElement.class))
					
					for (Field aux: obj.getClass().getDeclaredFields()){
					
						if (aux.isAnnotationPresent(Rdf.class) || aux.isAnnotationPresent(RdfMultiple.class)){
																			
							if (!context.containsKey(aux.getName())){
								
								if (!primero) {
									respuesta.append(",");
								} else {
									primero = false;
								}
								StringBuilder uri = new StringBuilder();
//								ASUMIMOS QUE LA ANOTACION MULTIPLE ESTARA COMPUESTA SOLO DE DOS ELEMENTOS
//								   - EL PRIMERO SERA EL TYPE
//								   - EL SEGUNDO SERA EL ID
								if (aux.isAnnotationPresent(RdfMultiple.class)) {
									Rdf[] valores = aux.getAnnotation(RdfMultiple.class).value();
									for (int i=0;i<valores.length;i++){
										uri.append("{");
										uri.append("\"@id\": "+obtenerValorAnotacionRDF(valores[i]));
										uri.append("}");
										if (i>0)
											respuesta.append("\"" + aux.getName() + "_" + i + "\":" + uri);
										else
											respuesta.append("\"" + aux.getName() + "\":" + uri);
										if (i<valores.length-1)
											respuesta.append(",");
										uri = new StringBuilder();
									}
								} else {
									uri.append(obtenerValorAnotacionRDF(aux.getAnnotation(Rdf.class)));
									respuesta.append("\"" + aux.getName() + "\":" + uri);
								}
								context.put(aux.getName(), uri.toString());
							}
							
//							COMPROBAMOS A SU VEZ SI HAY MAS ELEMENTOS QUE CONTENGAN ANOTACIONES
							if (aux.getType().isAnnotationPresent(XmlRootElement.class) || aux.getClass().isAnnotationPresent(XmlRootElement.class)) {
					
								containsXmlRootElement(aux.getType().getDeclaredFields(), respuesta, primero);
							}
						}					
					}
			}
			
		} else if (object instanceof List){
			List<?> listado = (List<?>) object;
			for (Object obj : listado){			
				if (obj.getClass().isAnnotationPresent(XmlRootElement.class))
					
					for (Field aux: obj.getClass().getDeclaredFields()){
					
						if (aux.isAnnotationPresent(Rdf.class) || aux.isAnnotationPresent(RdfMultiple.class)){
												
							if (!context.containsKey(aux.getName())){
								StringBuilder uri = new StringBuilder();
								if (!primero) {
									respuesta.append(",");
								} else {
									primero = false;
								}
//								ASUMIMOS QUE LA ANOTACION MULTIPLE ESTARA COMPUESTA SOLO DE DOS ELEMENTOS
//								   - EL PRIMERO SERA EL TYPE
//								   - EL SEGUNDO SERA EL ID
								if (aux.isAnnotationPresent(RdfMultiple.class)) {
									Rdf[] valores = aux.getAnnotation(RdfMultiple.class).value();
									for (int i=0;i<valores.length;i++){
										uri.append("{");
										uri.append("\"@id\": "+obtenerValorAnotacionRDF(valores[i]));
										uri.append("}");
										if (i>0)
											respuesta.append("\"" + aux.getName() + "_" + i + "\":" + uri);
										else
											respuesta.append("\"" + aux.getName() + "\":" + uri);
										if (i<valores.length-1)
											respuesta.append(",");
										uri = new StringBuilder();
									}
								} else {
									uri.append(obtenerValorAnotacionRDF(aux.getAnnotation(Rdf.class)));
									respuesta.append("\"" + aux.getName() + "\":" + uri);
								}
								context.put(aux.getName(), uri.toString());
							}
							
//							COMPROBAMOS A SU VEZ SI HAY MAS ELEMENTOS QUE CONTENGAN ANOTACIONES
							if (aux.getType().isAnnotationPresent(XmlRootElement.class) || aux.getClass().isAnnotationPresent(XmlRootElement.class)) {
					
								containsXmlRootElement(aux.getType().getDeclaredFields(), respuesta, primero);
							}
						}					
					}
			}
		} else if (object instanceof Map<?, ?>){
			//NEED TO BE DETERMINED
			
		} else {
			Object obj = object;

			for (Field aux: obj.getClass().getDeclaredFields()){
				
				if (aux.isAnnotationPresent(Rdf.class) || aux.isAnnotationPresent(RdfMultiple.class)){
							
					if (!context.containsKey(aux.getName())){
						StringBuilder uri = new StringBuilder();
						if (!primero) {
							respuesta.append(",");
						} else {
							primero = false;
						}
//							ASUMIMOS QUE LA ANOTACION MULTIPLE ESTARA COMPUESTA SOLO DE DOS ELEMENTOS
//							   - EL PRIMERO SERA EL TYPE
//							   - EL SEGUNDO SERA EL ID
						if (aux.isAnnotationPresent(RdfMultiple.class)) {
							Rdf[] valores = aux.getAnnotation(RdfMultiple.class).value();
							for (int i=0;i<valores.length;i++){
								uri.append("{");
								uri.append("\"@id\": "+obtenerValorAnotacionRDF(valores[i]));
								uri.append("}");
								if (i>0)
									respuesta.append("\"" + aux.getName() + "_" + i + "\":" + uri);
								else
									respuesta.append("\"" + aux.getName() + "\":" + uri);
								if (i<valores.length-1)
									respuesta.append(",");
								uri = new StringBuilder();
							}
						} else {
							uri.append(obtenerValorAnotacionRDF(aux.getAnnotation(Rdf.class)));
							respuesta.append("\"" + aux.getName() + "\":" + uri);
						}
						context.put(aux.getName(), uri.toString());
					}
//						COMPROBAMOS A SU VEZ SI HAY MAS ELEMENTOS QUE CONTENGAN ANOTACIONES
					if (aux.getType().isAnnotationPresent(XmlRootElement.class) || aux.getClass().isAnnotationPresent(XmlRootElement.class)) {

						containsXmlRootElement(aux.getType(), respuesta, primero);
					}
				}					
			}

		}
		return false;
	}
	
	private boolean containsXmlRootElement(Field[] object, StringBuilder respuesta, boolean primero){
	
		for (Field aux: object){
			if (aux.isAnnotationPresent(Rdf.class) || aux.isAnnotationPresent(RdfMultiple.class)){
				if (!context.containsKey(aux.getName())){
					StringBuilder uri = new StringBuilder();
					if (!primero) {
						respuesta.append(",");
					} else {
						primero = false;
					}
//					ASUMIMOS QUE LA ANOTACION MULTIPLE ESTARA COMPUESTA SOLO DE DOS ELEMENTOS
//					   - EL PRIMERO SERA EL TYPE
//					   - EL SEGUNDO SERA EL ID
					if (aux.isAnnotationPresent(RdfMultiple.class)) {
						Rdf[] valores = aux.getAnnotation(RdfMultiple.class).value();
						for (int i=0;i<valores.length;i++){
							uri.append("{");
							uri.append("\"@id\": "+obtenerValorAnotacionRDF(valores[i]));
							uri.append("}");
							if (i>0)
								respuesta.append("\"" + aux.getName() + "_" + i + "\":" + uri);
							else
								respuesta.append("\"" + aux.getName() + "\":" + uri);
							if (i<valores.length-1)
								respuesta.append(",");
							uri = new StringBuilder();
						}
					} else {
						uri.append(obtenerValorAnotacionRDF(aux.getAnnotation(Rdf.class)));
						respuesta.append("\"" + aux.getName() + "\":" + uri);
					}
					context.put(aux.getName(), uri.toString());
				}
				if (aux.getType().isAnnotationPresent(XmlRootElement.class) || aux.getClass().isAnnotationPresent(XmlRootElement.class)) {
					containsXmlRootElement(aux.getType().getDeclaredFields(), respuesta, primero);
				}
			}					
		}
		
		return false;
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
	
	private String obtenerContextRDF(Rdf ann) {
		if ("".equals(ann.uri())) {
			return ann.contexto().trim()+ ":" + ann.propiedad().trim() + "";
		} else {
			return "\"" + ann.uri() + "\"";
		}
	}
	
	private String obtenerId(Object object) {
		String output = "";
		for (Field field:object.getClass().getDeclaredFields()){
			if (field.getName().equals("id") || field.isAnnotationPresent(Id.class)){
				Object valor = Funciones.retrieveObjectValue(object, field.getName());
				output = Funciones.obtenerPath(object.getClass()) + valor.toString();
			}
		}
		return output;
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
