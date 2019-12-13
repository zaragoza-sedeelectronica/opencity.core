package org.sede.core.rest.view;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.UUID;

import javax.persistence.Id;
import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.HtmlContent;
import org.sede.core.anotaciones.Interno;
import org.sede.core.anotaciones.IsPropiedadSemanticaCentro;
import org.sede.core.anotaciones.IsUri;
import org.sede.core.anotaciones.PathId;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RdfMultiple;
import org.sede.core.anotaciones.RdfType;
import org.sede.core.entity.TienePropiedad;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.Geometria;
import org.sede.core.geo.LineString;
import org.sede.core.geo.Punto;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.MimeTypes;
import org.apache.commons.codec.CharEncoding;
import org.sede.core.anotaciones.Context;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.Propiedades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;


public class TransformadorBasicoRdf {
	private static final Logger logger = LoggerFactory.getLogger(TransformadorBasicoRdf.class);
	private Html2MarkDown transformadorMarkDown = new Html2MarkDown();

	Model model = ModelFactory.createDefaultModel();
	String uriBase = "http://vocab.linkeddata.es";
	Resource initRes = null;
	boolean genericId = true;

	public void transformarObjeto(StringBuilder respuesta, Object retorno,
			Peticion peticion, boolean primero, String prefijo)
			throws InvalidImplementationException, UnsupportedEncodingException, NoSuchAlgorithmException {
		
		if (primero){
			String id = UUID.randomUUID().toString();
			// FIXME externalizar string
			String path = "https://www.zaragoza.es" + Propiedades.getContexto();
			genericId = true;
			for (Field field : retorno.getClass().getDeclaredFields()) {
				if (field.getName().equals("id") || (field.isAnnotationPresent(Id.class)) ){				
					Object valor = Funciones.retrieveObjectValue(retorno, field.getName());
					id = valor.toString();
					genericId = false;
				}
			}
			if (retorno.getClass().isAnnotationPresent(PathId.class)){
				path = path + retorno.getClass().getAnnotation(PathId.class).value() + "/";
			} else {
				path = path + "/result/";
			}
			if (!genericId){
				initRes = model.createResource(path + id);
				if (retorno.getClass().getAnnotation(Rdf.class)!=null){
					String rdf = obtenerValorAnotacionRDF(retorno.getClass().getAnnotation(Rdf.class)).replaceAll("\"", "");
					if (rdf.endsWith("/"))
						rdf = rdf.substring(0,rdf.lastIndexOf('/'));
	
					Property initResProp;
					if (rdf.startsWith("http://"))
						initResProp = model.createProperty(rdf);
					else
						initResProp = model.createProperty(uriBase+ rdf);
					model.add(initRes, RDF.type, initResProp);
				} else if (retorno.getClass().getAnnotation(RdfMultiple.class)!=null){
					for (Rdf anot:retorno.getClass().getAnnotation(RdfMultiple.class).value()){
						String rdf = obtenerValorAnotacionRDF(anot).replaceAll("\"", "");
						if (rdf.endsWith("/"))
							rdf = rdf.substring(0,rdf.lastIndexOf('/'));
		
						Property initResProp;
						if (rdf.startsWith("http://"))
							initResProp = model.createProperty(rdf);
						else
							initResProp = model.createProperty(uriBase+ rdf);
						model.add(initRes, RDF.type, initResProp);
					}
				}
			}
			else
				initRes = model.createResource();
			
			for (Field field : retorno.getClass().getDeclaredFields()) {
				if ((field.isAnnotationPresent(RdfType.class)) ){				
					Object valor = Funciones.retrieveObjectValue(retorno, field.getName());
					if (valor instanceof Object[]) {
						Object[] listado = (Object[]) valor;
						for (Object element:listado){
							model.add(initRes, RDF.type, model.createProperty(element.toString()));
						}
					}
					else
						model.add(initRes, RDF.type, model.createProperty(valor.toString()));
				}
			}
			

		}		
		
		peticion.establecerLastModified(retorno);

		for (Field field : retorno.getClass().getDeclaredFields()) {
			boolean transformarCampo = transformarCampo(retorno, peticion, prefijo, field);
			if (transformarCampo) {

				Object valor = Funciones.retrieveObjectValue(retorno, field
						.getName());
				
				
				if (field.isAnnotationPresent(HtmlContent.class) && (valor instanceof String) && CheckeoParametros.RESPUESTAMARKDOWN.equals(peticion.getTipoEtiquetado())) {
					valor = transformadorMarkDown.transformar((String)valor);
				}

				
				
				if (field.isAnnotationPresent(IsPropiedadSemanticaCentro.class)){
					for (Object object: (Set<?>) valor){
						TienePropiedad psc = (TienePropiedad) object;
						Property prop = model.createProperty(psc.getPropiedad().getUri());
						model.add(initRes,prop,createResource(model, psc));
						
					}
					
				} else if (valor instanceof List) {
					List<?> listado = (List<?>) valor;

					if (!listado.isEmpty()){
						for (Object object : listado) {													
							Resource newRes = model.createResource(getPathResource(object,initRes.getURI()));													
							boolean output = transformarObjeto(respuesta, object, peticion, prefijo, newRes);
							
							if (output){
								if (object.getClass().getAnnotation(Rdf.class)!=null){
									String rdf = obtenerValorAnotacionRDF(object.getClass().getAnnotation(Rdf.class)).replaceAll("\"", "");
									if (rdf.endsWith("/"))
										rdf = rdf.substring(0,rdf.lastIndexOf('/'));
					
									Property initResProp;
									if (rdf.startsWith("http://"))
										initResProp = model.createProperty(rdf);
									else
										initResProp = model.createProperty(uriBase+ rdf);
									model.add(newRes, RDF.type, initResProp);
								} else if (object.getClass().getAnnotation(RdfMultiple.class)!=null){
									for (Rdf anot:object.getClass().getAnnotation(RdfMultiple.class).value()){
										String rdf = obtenerValorAnotacionRDF(anot).replaceAll("\"", "");
										if (rdf.endsWith("/"))
											rdf = rdf.substring(0,rdf.lastIndexOf('/'));
						
										Property initResProp;
										if (rdf.startsWith("http://"))
											initResProp = model.createProperty(rdf);
										else
											initResProp = model.createProperty(uriBase+ rdf);
										model.add(newRes, RDF.type, initResProp);
									}
								}
								
								if (containsRdfsType(object)) {				
									for (Field f : object.getClass().getDeclaredFields()) {
										if ((f.isAnnotationPresent(RdfType.class)) ){				
											Object val = Funciones.retrieveObjectValue(object, f.getName());
											if (val instanceof Object[]) {
												Object[] values = (Object[]) val;
												for (Object element:values){
													model.add(newRes, RDF.type, model.createProperty(element.toString()));
												}
											}
											else if (val!=null)
												model.add(newRes, RDF.type, model.createProperty(val.toString()));
										}
									
									}	
								}
								addResource(initRes, newRes, field, object);
							}
							
						}
					}
				} else if (valor instanceof Map<?, ?>) {
					transformarMap(respuesta, peticion, peticion.getFormato().getTransformador(), true, field, valor, initRes);
				} else if (valor instanceof Set) {
					Set<?> listado = (Set<?>) valor;
					if (!listado.isEmpty()){
						for (Object object : listado) {
							Resource newRes = model.createResource(getPathResource(object,initRes.getURI()));
							boolean output = transformarObjeto(respuesta, object, peticion, prefijo, newRes);
							if (output){
								addResource(initRes, newRes, field, object);
							}
							
						}
					}
				} else if (valor instanceof Object[]) {
					Object[] listado = (Object[]) valor;
					if (listado.length>0){
						for (Object object : listado) {
							Resource newRes = model.createResource(getPathResource(object));
							boolean output = transformarObjeto(respuesta, object, peticion, prefijo, newRes);
							if (output){
								addResource(initRes, newRes, field, object);
							}
							
						}
					}
				} else if (valor instanceof Geometria) {
					if (field.isAnnotationPresent(Rdf.class)) {
						Geometria geo = (Geometria) valor;
						String srsName = peticion.getSrsName();
						String sourceSrs = peticion.getSourceSrs();
						boolean aux = false;
						addGeometry(aux, field, sourceSrs, srsName, geo, initRes, peticion.isCargandoEnVirtuoso());
					}

				} else if (field.getType().isAnnotationPresent(XmlRootElement.class)){
					if (!field.isAnnotationPresent(Interno.class) && (field.isAnnotationPresent(Rdf.class) || field.isAnnotationPresent(RdfMultiple.class))){
						Resource newRes = model.createResource(getPathResource(valor,initRes.getURI()));													
						boolean output = transformarObjeto(respuesta, valor, peticion, prefijo, newRes);
						
						if (output){
							addResource(initRes, newRes, field, valor);
						}
					}
					
				} else {
				
					//ESTA PARTE ES DONDE DEBO AÑADIR LA PRUEBA DEL CAMPO CONTEXTO A VER QUE PUEDO HACER
						if (field.isAnnotationPresent(Rdf.class) && !field.isAnnotationPresent(Interno.class)) {
							String anot = obtenerValorAnotacionRDF(field.getAnnotation(Rdf.class)).replaceAll("\"", "");
							
							Property entityProp = model.createProperty(anot);
							if (field.isAnnotationPresent(IsUri.class))
								model.add(initRes, entityProp, model.createResource(valor.toString()));
							else
								model.add(initRes, entityProp, valor.toString());

						} else if (field.isAnnotationPresent(RdfMultiple.class) && !field.isAnnotationPresent(Interno.class)) {
							Rdf[] valores = field.getAnnotation(RdfMultiple.class).value();

							for (int i = 0; i < valores.length; i++) {							
								Property entityProp = model.createProperty(obtenerValorAnotacionRDF(valores[i]).replaceAll("\"", ""));					        
								if (field.isAnnotationPresent(IsUri.class))
									model.add(initRes, entityProp, model.createResource(valor.toString()));
								else
									model.add(initRes, entityProp, model.createTypedLiteral(valor.toString()));
							}

						}

				}
			}
		}
	
		if (primero){
			String format = "RDF/XML";
			if (peticion.getFormato().getExtension().equals(MimeTypes.TURTLE_EXTENSION))
				format = "TURTLE";
			else if (peticion.getFormato().getExtension().equals(MimeTypes.RDF_N3_EXTENSION))
				format = "N3";
								
			ByteArrayOutputStream obj = new ByteArrayOutputStream();
			model.write(obj, format);
			try {
		        respuesta.append(obj.toString(CharEncoding.UTF_8));
			} catch (UnsupportedEncodingException e) {
				respuesta.append(obj);
				
			}			 
		}
	}
	
	public boolean transformarObjeto(StringBuilder respuesta, Object retorno,
			Peticion peticion, String prefijo, Resource resource)
			throws InvalidImplementationException, UnsupportedEncodingException, NoSuchAlgorithmException {

		boolean output = false;
		
		peticion.establecerLastModified(retorno);

		for (Field field : retorno.getClass().getDeclaredFields()) {
			boolean transformarCampo = transformarCampo(retorno, peticion, prefijo, field);

			if (transformarCampo
					&& (field.isAnnotationPresent(Rdf.class) ||
							field.isAnnotationPresent(IsPropiedadSemanticaCentro.class) ||
							field.isAnnotationPresent(RdfMultiple.class)) && !field.isAnnotationPresent(Interno.class)) {
				Object valor = Funciones.retrieveObjectValue(retorno, field
						.getName());
				
 				if (field.isAnnotationPresent(IsPropiedadSemanticaCentro.class)){
					model.createProperty("http://www.loa-cnr.it/ontologies/DUL.owl#hasDataValue");
					model.createProperty("http://www.loa-cnr.it/ontologies/DUL.owl#isClassifiedBy");
					for (Object object: (Set<?>) valor){
						TienePropiedad psc = (TienePropiedad) object;
						Property prop = model.createProperty(psc.getPropiedad().getUri());
						model.add(resource,prop,createResource(model, psc));
						
					}
					
				}

				if (valor instanceof List) {
					List<?> listado = (List<?>) valor;
					if (!listado.isEmpty()){
						for (Object object : listado) {
							Resource newRes = model.createResource(getPathResource(object,resource.getURI()));
							
							boolean auxOutput = transformarObjeto(respuesta, object, peticion, prefijo, newRes);
							if (auxOutput && !output)
								output = true;
							if (auxOutput){
								if (object.getClass().getAnnotation(Rdf.class)!=null){
									String rdf = obtenerValorAnotacionRDF(object.getClass().getAnnotation(Rdf.class)).replaceAll("\"", "");
									if (rdf.endsWith("/"))
										rdf = rdf.substring(0,rdf.lastIndexOf('/'));
					
									Property initResProp;
									if (rdf.startsWith("http://"))
										initResProp = model.createProperty(rdf);
									else
										initResProp = model.createProperty(uriBase+ rdf);
									model.add(newRes, RDF.type, initResProp);
								} else if (object.getClass().getAnnotation(RdfMultiple.class)!=null){
									for (Rdf anot:object.getClass().getAnnotation(RdfMultiple.class).value()){
										String rdf = obtenerValorAnotacionRDF(anot).replaceAll("\"", "");
										if (rdf.endsWith("/"))
											rdf = rdf.substring(0,rdf.lastIndexOf('/'));
						
										Property initResProp;
										if (rdf.startsWith("http://"))
											initResProp = model.createProperty(rdf);
										else
											initResProp = model.createProperty(uriBase+ rdf);
										model.add(newRes, RDF.type, initResProp);
									}
								}
								addResource(resource, newRes, field, object);
							}
						}
					}
				} else if (valor instanceof Map<?, ?>) {
					/*Map<?,?> listado = (Map<?,?>) valor*/;
				} else if (valor instanceof Set) {
					Set<?> listado = (Set<?>) valor;
					if (!listado.isEmpty()){
						for (Object object : listado) {

							Resource newRes = model.createResource(getPathResource(object,resource.getURI()));							
							boolean auxOutput = transformarObjeto(respuesta, object, peticion, prefijo, newRes);
							if (auxOutput && !output)
								output = true;
							if (auxOutput){
								addResource(resource, newRes, field, object);
							}
						}
					}
				} else if (valor instanceof Object[]) {
					Object[] listado = (Object[]) valor;

					if (listado.length>0){
						for (Object object : listado) {

							Resource newRes = model.createResource(getPathResource(object));
							boolean auxOutput = transformarObjeto(respuesta, object, peticion, prefijo, newRes);
							if (auxOutput && !output)
								output = true;
							if (auxOutput){
								addResource(resource, newRes, field, object);
							}
						}
					}
				} else if (valor instanceof Geometria) {
					
					if (field.isAnnotationPresent(Rdf.class)) {
						Geometria geo = (Geometria) valor;
						String srsName = peticion.getSrsName();
						String sourceSrs = peticion.getSourceSrs();
						output = addGeometry(output, field, sourceSrs, srsName, geo, resource, peticion.isCargandoEnVirtuoso());
					}
					
				} else if (field.getType().isAnnotationPresent(XmlRootElement.class)){
					if (!field.isAnnotationPresent(Interno.class) && (field.isAnnotationPresent(Rdf.class) || field.isAnnotationPresent(RdfMultiple.class))){					
						Resource newRes = model.createResource(getPathResource(valor,resource.getURI()));							
						boolean auxOutput = transformarObjeto(respuesta, valor, peticion, prefijo, newRes);
						if (auxOutput && !output)
							output = true;
						if (auxOutput){

							addResource(resource, newRes, field, valor);
						}
						if (auxOutput && !output)
							output = true;
					}
				} else {
				
						if (field.isAnnotationPresent(Rdf.class) && !field.isAnnotationPresent(Interno.class)) {
							String anot = obtenerValorAnotacionRDF(field.getAnnotation(Rdf.class)).replaceAll("\"", "");
							Property entityProp = model.createProperty(anot);
							if (field.isAnnotationPresent(IsUri.class))
								model.add(resource, entityProp, model.createResource(valor.toString()));
							else
								model.add(resource, entityProp, valor.toString());
							output = true;
						} else if (field.isAnnotationPresent(RdfMultiple.class) && !field.isAnnotationPresent(Interno.class)) {
							Rdf[] valores = field.getAnnotation(RdfMultiple.class).value();
							
							for (int i = 0; i < valores.length; i++) {							
								Property entityProp = model.createProperty(obtenerValorAnotacionRDF(valores[i]).replaceAll("\"", ""));					        								
								if (field.isAnnotationPresent(IsUri.class))
									model.add(resource, entityProp, model.createResource(valor.toString()));
								else
									model.add(resource, entityProp, model.createTypedLiteral(valor.toString()));
							}
							output = true;
						}

				}
			}
		}
		
		return output;
	}
	
	public static boolean transformarMap(StringBuilder respuesta, Peticion peticion, TransformadorGenerico transformador, boolean primerCampo, Field field, Object valor, Resource resource)
			throws InvalidImplementationException {
		Map<?,?> listado = (Map<?,?>) valor;
		if (listado.size() > 0) {
			Iterator<?> it = listado.keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
			    Object object = listado.get(key);
				if (object instanceof Map) {
					
					transformarMap(respuesta, peticion, transformador, true, field, object, resource);
					
				} else if (object instanceof Object[]) {
					
					Object[] listadoInt =(Object[]) object;
					
					if (listadoInt.length > 0) {
					
						boolean primerInterno = true;
						for (int i = 0; i < listadoInt.length; i++) {
							Object objectInt = listadoInt[i];
							if (objectInt instanceof Double) {
								if (i > 0) {
									respuesta.append(",");
								}

							} else if (objectInt instanceof String) {
								if (i > 0) {
									respuesta.append(",");
								}

							} else {
								transformador.transformarObjeto(respuesta, objectInt, peticion, primerInterno, null);
							}
							primerInterno = false;
						}
					}
				} else {
					if (peticion.quiereVerCampo("", key, peticion.getSelectedFields())) {

						if (object.getClass().isAnnotationPresent(XmlRootElement.class)) {
							StringBuilder datosObj = new StringBuilder();
							transformador.transformarObjeto(datosObj, object, peticion, true, "");

						} else if (object.getClass().isAnnotationPresent(Rdf.class)){
							transformador.escribirValorCampo(key, object, field.isAnnotationPresent(HtmlContent.class), peticion.getTipoEtiquetado());
						}
					}
				}
			}
		}
		return primerCampo;
	}
	
	private boolean addGeometry(boolean output, Field field, String sourceSrs, String srsNameRequested, Geometria geo, Resource resource, boolean cargandoEnVirtuoso) throws UnsupportedEncodingException, NoSuchAlgorithmException{
		
		if (geo instanceof Punto) {
			Punto p = (Punto) geo;
			Resource geoRes;
			String anot = obtenerValorAnotacionRDF(field.getAnnotation(Rdf.class)).replaceAll("\"", "");
			Property entityProp = model.createProperty(anot);
			DecimalFormat df = new DecimalFormat();
			df.setMaximumFractionDigits(6);
			df.setDecimalFormatSymbols(new DecimalFormatSymbols(Locale.UK));
			if (srsNameRequested.equals(CheckeoParametros.SRSWGS84) || cargandoEnVirtuoso) {
				
				String term = Geometria.transformAsString(p.getCoordinates()[0], p.getCoordinates()[1], sourceSrs, srsNameRequested);
		
				Property latProp = model
						.createProperty("http://www.w3.org/2003/01/geo/wgs84_pos#lat");
				Property lngProp = model
						.createProperty("http://www.w3.org/2003/01/geo/wgs84_pos#long");
				StringTokenizer st = new StringTokenizer(
						term, ",");
				String lngSt = df.format(Float.valueOf(st.nextToken()));				
				String latSt = df.format(Float.valueOf(st.nextToken()));
				Literal litLat = model.createTypedLiteral(latSt,XSDDatatype.XSDdouble);
				Literal litLng = model.createTypedLiteral(lngSt,XSDDatatype.XSDdouble);
				
				geoRes = model.createResource("http://www.zaragoza.es/sede/servicio/geometry/WGS84/"+ latSt + "_" + lngSt);
				model.add(geoRes,RDF.type,model.createProperty("http://www.w3.org/2003/01/geo/wgs84_pos#Point"));
				model.add(geoRes,RDF.type,model.createProperty("http://www.opengis.net/ont/sf#Point"));
				geoRes.addLiteral(latProp, litLat);
				geoRes.addLiteral(lngProp, litLng);
								
				Property crsProperty = model.createProperty("http://www.opengis.net/ont/geosparql#crs");
				Resource crsRes = model.createResource("http://www.opengis.net/def/crs/OGC/1.3/CRS84");
				geoRes.addProperty(crsProperty, crsRes);
				
				Property asWKTProperty = model.createProperty("http://www.opengis.net/ont/geosparql#asWKT");
				String wktText = "POINT("
					+ lngSt
					+ " "
					+ latSt + ")";
				Literal literal = model.createTypedLiteral(wktText,"http://www.opengis.net/ont/geosparql#wktLiteral");
				geoRes.addLiteral(asWKTProperty, literal);
				
				model.add(resource, entityProp, geoRes);
				output = true;
			} 
			if (srsNameRequested.equals(CheckeoParametros.SRSETRS89) || cargandoEnVirtuoso) {
				
				double[] term = Geometria.transform(p.getCoordinates()[0], p.getCoordinates()[1], sourceSrs, srsNameRequested);
				
				geoRes = model.createResource("http://www.zaragoza.es/sede/servicio/geometry/EPSG25830/"+ term[0] + "_" + term[1]);
				model.add(geoRes,RDF.type,model.createProperty("http://www.opengis.net/ont/sf#Point"));
				
				Property crsProperty = model.createProperty("http://www.opengis.net/ont/geosparql#crs");
				Resource crsRes = model.createResource("http://www.opengis.net/def/crs/EPSG/0/25830");
				geoRes.addProperty(crsProperty, crsRes);
				
				Property asWKTProperty = model.createProperty("http://www.opengis.net/ont/geosparql#asWKT");
				String wktText = "POINT("
						+ term[0]
						+ " "
						+ term[1] + ")";
				Literal literal = model.createTypedLiteral(wktText,"http://www.opengis.net/ont/geosparql#wktLiteral");
				geoRes.addLiteral(asWKTProperty, literal);
				model.add(resource, entityProp, geoRes);
				output = true;
			}
			if (srsNameRequested.equals(CheckeoParametros.SRSUTM30N) || cargandoEnVirtuoso) {
				geoRes = model.createResource("http://www.zaragoza.es/sede/servicio/geometry/EPSG23030/"+ p.getCoordinates()[0] + "_" + p.getCoordinates()[1]);
				model.add(geoRes,RDF.type,model.createProperty("http://www.opengis.net/ont/sf#Point"));
				
				Property crsProperty = model.createProperty("http://www.opengis.net/ont/geosparql#crs");
				Resource crsRes = model.createResource("http://www.opengis.net/def/crs/EPSG/0/23030");
				geoRes.addProperty(crsProperty, crsRes);
				
				Property asWKTProperty = model.createProperty("http://www.opengis.net/ont/geosparql#asWKT");
				String wktText = "POINT("
						+ p.getCoordinates()[0]
						+ " "
						+ p.getCoordinates()[1] + ")";
				Literal literal = model.createTypedLiteral(wktText,"http://www.opengis.net/ont/geosparql#wktLiteral");
				geoRes.addLiteral(asWKTProperty, literal);
				model.add(resource, entityProp, geoRes);
				output = true;
			}
			
		} else if (geo instanceof LineString) {
			LineString ls = (LineString) geo;		
			Double[][] coordinates = ls.getCoordinates();
			
			
			String anot = obtenerValorAnotacionRDF(field.getAnnotation(Rdf.class)).replaceAll("\"", "");
			Property entityProp = model.createProperty(anot);
			if (srsNameRequested.equals(CheckeoParametros.SRSWGS84)) {
				
				String wktText = "<![CDATA[<gml:LineString srsName=\"EPSG:4258\" xmlns:gml=\"http://www.opengis.net/gml\"><gml:coordinates decimal=\".\" cs=\",\" ts=\" \">";
				int ind = 0;
				while (ind<coordinates.length){
					Double lat = coordinates[ind][0];
					Double lng = coordinates[ind][1];
					wktText = wktText + lat + ","+lng+" ";
					ind++;
				}
				wktText = wktText + "</gml:coordinates></gml:LineString>]]>";
				Resource geoRes = model.createResource("http://www.zaragoza.es/sede/servicio/geometry/"+ getHash(wktText));
				model.add(geoRes,RDF.type,model.createProperty("http://www.opengis.net/ont/gml#LineString"));
				Literal literal = model.createTypedLiteral(wktText,"http://www.opengis.net/ont/geosparql#gmlLiteral");
				Property asGMLroperty = model.createProperty("http://www.opengis.net/ont/geosparql#asGML");
				geoRes.addLiteral(asGMLroperty, literal);
				model.add(resource, entityProp, geoRes);
			} else {
				
				String wktText = "<![CDATA[<http://www.opengis.net/def/crs/OGC/1.3/CRS84>LineString(";
				int ind = 0;
				while (ind<coordinates.length){
					Double lat = coordinates[ind][0];
					Double lng = coordinates[ind][1];
					if (ind!=0)
						wktText = wktText + ",";
					wktText = wktText + lat + " "+lng;
					ind++;
				}
				wktText = wktText + ")]]>";
				Resource geoRes = model.createResource("http://www.zaragoza.es/sede/servicio/geometry/"+ getHash(wktText));
				model.add(geoRes,RDF.type,model.createProperty("http://www.opengis.net/ont/sf#LineString"));
				Literal literal = model.createTypedLiteral(wktText,"http://www.opengis.net/ont/geosparql#wktLiteral");
				Property asGMLroperty = model.createProperty("http://www.opengis.net/ont/geosparql#asWKT");
				geoRes.addLiteral(asGMLroperty, literal);
				model.add(resource, entityProp, geoRes);
			}

		}		
		
		return output;
		
	}
	
	private void addResource(Resource parentRes, Resource newRes, Field field, Object object){
		
		if (field.isAnnotationPresent(Rdf.class)){
			String anot = obtenerValorAnotacionRDF(field.getAnnotation(Rdf.class)).replaceAll("\"", "");
			Property entityProp = model.createProperty(anot);
			
			model.add(parentRes, entityProp, newRes);
			//A�ADIMOS EL TIPO DEL RECURSO
			addResourceType(newRes, object);

		} else if (field.isAnnotationPresent(RdfMultiple.class)){
			for(Rdf annotation:field.getAnnotation(RdfMultiple.class).value()){
				String anot = obtenerValorAnotacionRDF(annotation).replaceAll("\"", "");
				
				Property entityProp = model.createProperty(anot);
				model.add(parentRes, entityProp, newRes);
								
				//A�ADIMOS EL TIPO DEL RECURSO
				addResourceType(newRes, object);

			}
		}
		
	}
	
	private void addResourceType(Resource newRes, Object object){
		
		if (object.getClass().isAnnotationPresent(Rdf.class)){
			String rdf = obtenerValorAnotacionRDF(object.getClass().getAnnotation(Rdf.class)).replaceAll("\"", "");
			if (rdf.endsWith("/"))
				rdf = rdf.substring(0,rdf.lastIndexOf('/'));

			Property initResProp;
			if (rdf.startsWith("http://"))
				initResProp = model.createProperty(rdf);
			else
				initResProp = model.createProperty(uriBase+ rdf);
			model.add(newRes, RDF.type, initResProp);
		} else if (object.getClass().isAnnotationPresent(RdfMultiple.class)){
			for(Rdf annotation:object.getClass().getAnnotation(RdfMultiple.class).value()){
				String rdf = obtenerValorAnotacionRDF(annotation).replaceAll("\"", "");
				if (rdf.endsWith("/"))
					rdf = rdf.substring(0,rdf.lastIndexOf('/'));

				Property initResProp;
				if (rdf.startsWith("http://"))
					initResProp = model.createProperty(rdf);
				else
					initResProp = model.createProperty(uriBase+ rdf);
				model.add(newRes, RDF.type, initResProp);
			}
		}
		
	}
	
	private String getPathResource(Object retorno){		
		String id = UUID.randomUUID().toString();
		//FIXME externalizar string
		String path = "https://www.zaragoza.es" + Propiedades.getContexto();
		for (Field fld : retorno.getClass().getDeclaredFields()) {
			if (fld.getName().equals("id") || (fld.isAnnotationPresent(Id.class))){
				
				Object val = Funciones.retrieveObjectValue(retorno, fld.getName());
				id = val.toString();
				if (id.contains(" ") || id.contains(",") || (id.contains ("[") || id.contains ("]"))){
					id = getComplexId(id);					
				}
			}
		}
		if (retorno.getClass().isAnnotationPresent(PathId.class)){
			path = path + retorno.getClass().getAnnotation(PathId.class).value() + "/";
		} else {
			path = path + "/recurso/";
		}
		return path + id;
	}
	
	private String getPathResource(Object retorno, String raiz){		
		String id = UUID.randomUUID().toString();
//		FIXME externalizar string
		String path = "https://www.zaragoza.es" + Propiedades.getContexto();
		boolean complexId = false;
		for (Field fld : retorno.getClass().getDeclaredFields()) {
			if (fld.getName().equals("id") || (fld.isAnnotationPresent(Id.class))){
				
				Object val = Funciones.retrieveObjectValue(retorno, fld.getName());
				if (val != null) {
					id = val.toString();
					
					if (id.contains(" ") || id.contains(",") || (id.contains ("[") || id.contains ("]"))){
						String prefijo = retorno.getClass().getAnnotation(Rdf.class).prefijo();
						id = getComplexId(id,raiz,prefijo);	
						complexId = true;
					}
				} else {
					id = Funciones.generarHash(retorno.toString());
				}
			}
		}
		if (complexId){
			path = raiz;
		}
		else if (retorno.getClass().isAnnotationPresent(PathId.class)){
			path = path + retorno.getClass().getAnnotation(PathId.class).value() + "/";
		} else {
			path = path + "/recurso/";
		}
		return path + id;
	}
	
	private String getComplexId(String id){
		String output = "";
		String subString = id.substring(id.indexOf('[')+1, id.indexOf(']'));
		StringTokenizer st = new StringTokenizer(subString, ",");
		String token = st.nextToken().trim();
		if (token.contains("=")){
			token = token.substring(token.indexOf('=')+1);
		}
		output = token;
		
		while (st.hasMoreElements()){
			token = st.nextToken().trim();
			if (token.contains("=")){
				token = token.substring(token.indexOf('=')+1);
			}
			output = output + "_" + token;
		}
		try {
			output = URLEncoder.encode(output,CharEncoding.UTF_8).replace("+", "%20");
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
		
		return output;
	}
	
	private String getComplexId(String id, String raiz, String prefijo){
		String output = "";
		String subString = id.substring(id.indexOf('[')+1, id.indexOf(']'));
		StringTokenizer st = new StringTokenizer(subString, ",");
		String token = st.nextToken().trim();
		if (token.contains("=")){
			token = token.substring(token.indexOf('=')+1);
		}
		output = token;
		if (raiz.endsWith("/"+output)){
			token = st.nextToken().trim();
			if (token.contains("=")){
				token = token.substring(token.indexOf('=')+1);
			}
			output = "/" + prefijo + "/" +token;
		}
		while (st.hasMoreElements()){
			token = st.nextToken().trim();
			if (token.contains("=")){
				token = token.substring(token.indexOf('=')+1);
			}
			output = output + "-" + token;
		}
		return output;
	}
	
	private String obtenerValorAnotacionRDF(Rdf ann) {
		if ("".equals(ann.uri())) {
			return "\"" + Context.listado.get(ann.contexto()).getUri()
					+ ann.propiedad() + "\"";
		} else {
			return "\"" + ann.uri() + "\"";
		}
	}

	public static boolean transformarCampo(Object retorno, Peticion peticion,
			String prefijo, Field field) {

		return peticion.puedeVerCampoEnSeccion(field, peticion
				.getPermisosEnSeccion(), peticion.getMetodo())
				&& peticion.quiereVerCampo(prefijo, field.getName(), peticion
						.getSelectedFields())
				&& Funciones.retrieveObjectValue(retorno, field.getName()) != null;
	}
	
	public static String getHash(String message) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        MessageDigest md;
        byte[] buffer;
        byte[] digest;
        String hash = "";

        
        buffer = message.getBytes(CharEncoding.UTF_8);
        md = MessageDigest.getInstance("SHA1");
        
        md.update(buffer);
        digest = md.digest();
        for (byte aux : digest) {
            int b = aux & 0xff;
            String s = Integer.toHexString(b);
            if (s.length() == 1) {
                hash += "0";
            }
            hash += s;
        }
        return hash;
    }
	
	public static boolean containsRdfsType(Object object){
		for (Field f : object.getClass().getDeclaredFields()) {
			if ((f.isAnnotationPresent(RdfType.class)) )
				return true;
		}
		return false;
	}
	
	private static Resource createResource(Model model, TienePropiedad ctp){
		
		Property dataValue = model.createProperty("http://www.loa-cnr.it/ontologies/DUL.owl#hasDataValue");
		Property classifiedBy = model.createProperty("http://www.loa-cnr.it/ontologies/DUL.owl#isClassifiedBy");
		
		Resource blankNode = model.createResource();
		model.createProperty(ctp.getPropiedad().getUri());
		Literal lit = null;
		if (ctp.getPropiedad().getTipo()!=null && !ctp.getPropiedad().getTipo().equals("")){
			if (ctp.getPropiedad().getTipo().equals("xsd:integer"))				
				lit = model.createTypedLiteral(ctp.getValor(), XSDDatatype.XSDinteger);
			else if (ctp.getPropiedad().getTipo().equals("xsd:int"))				
				lit = model.createTypedLiteral(ctp.getValor(), XSDDatatype.XSDint);
			else if (ctp.getPropiedad().getTipo().equals("xsd:float"))				
				lit = model.createTypedLiteral(ctp.getValor(), XSDDatatype.XSDfloat);
			else if (ctp.getPropiedad().getTipo().equals("xsd:double"))				
				lit = model.createTypedLiteral(ctp.getValor(), XSDDatatype.XSDdouble);
			else if (ctp.getPropiedad().getTipo().equals("xsd:date"))				
				lit = model.createTypedLiteral(ctp.getValor(), XSDDatatype.XSDdate);
			else if (ctp.getPropiedad().getTipo().equals("xsd:boolean"))				
				lit = model.createTypedLiteral(ctp.getValor(), XSDDatatype.XSDboolean);
			else
				lit = model.createTypedLiteral(ctp.getValor(), XSDDatatype.XSDstring);
		} else {
			lit = model.createTypedLiteral(ctp.getValor(), XSDDatatype.XSDstring);
		}
		
		model.add(blankNode,dataValue,lit);
		
		if (ctp.getPropiedad().getMagnitud()!=null && !ctp.getPropiedad().getMagnitud().equals("")){
			model.add(blankNode,classifiedBy,model.createResource(ctp.getPropiedad().getMagnitud()));
		}
		
		
		return blankNode;
	}

}
