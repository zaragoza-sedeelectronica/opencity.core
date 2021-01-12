package org.sede.core.rest.view;


import java.lang.reflect.Method;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.Rss;
import org.sede.core.exception.FormatoNoSoportadoException;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.genericdao.search.SearchResult;
/**
 * Para que funcione el transformador a iCalendar es necesario que 
 * la entidad que se desea transformar tenga los siguientes atributos:
 * id
 * title
 * description
 * startDate
 * creationDate
 * lastUpdated
 * @author ob544e
 *
 */
public class TransformadorRss implements TransformadorGenerico {
	private static final Logger logger = LoggerFactory.getLogger(TransformadorRss.class);
	public void transformarObjeto(StringBuilder respuesta, Object retorno,
			Peticion peticion, boolean primero, String prefijo)
			throws InvalidImplementationException {
		Class<?> clase = Funciones.descubrirClase(retorno);
		StringBuilder ical = new StringBuilder();
		
		if (retorno instanceof SearchResult) {
			
			for (Object obj : ((SearchResult<?>) retorno).getResult()) {
				transformarEntidad(ical, obj);
			}
			
		} else if (retorno.getClass().isAnnotationPresent(XmlRootElement.class)) {
			transformarEntidad(ical, retorno);
		}
		ical.append("</channel></rss>");
		respuesta.append("<?xml version=\"1.0\"?>"
				+ "<rss version=\"2.0\" "
					+ "xmlns:content=\"http://purl.org/rss/1.0/modules/content/\" "
					+ "xmlns:wfw=\"http://wellformedweb.org/CommentAPI/\" "
					+ "xmlns:dc=\"http://purl.org/dc/elements/1.1/\" "
					+ "xmlns:atom=\"http://www.w3.org/2005/Atom\" "
					+ "xmlns:sy=\"http://purl.org/rss/1.0/modules/syndication/\" "
					+ "xmlns:slash=\"http://purl.org/rss/1.0/modules/slash/\" "
				+ ">"
				   + "<channel>"
				      + "<title>" + clase.getAnnotation(Rss.class).title() + "</title>"
				      + "<link>" + clase.getAnnotation(Rss.class).link() + "</link>"
				      + "<description>" + clase.getAnnotation(Rss.class).description() + "</description>"
				      + "<language>es-us</language>"
				      + "<pubDate>" + clase.getAnnotation(Rss.class).pubDate() + "</pubDate>"
				      + "<lastBuildDate>" + ConvertDate.date2StringGmt(Funciones.getPeticion().getLastModified()) + "</lastBuildDate>"
//				      + "<docs>http://blogs.law.harvard.edu/tech/rss</docs>"
//				      + "<generator>Weblog Editor 2.0</generator>"
//				      + "<managingEditor>editor@example.com</managingEditor>"
//				      + "<webMaster>webmaster@example.com</webMaster>"
					);
		respuesta.append(ical.toString());
	}

	private void transformarEntidad(StringBuilder respuesta, Object obj) {
		
		try {
			Method method = obj.getClass().getDeclaredMethod("toRss");
			respuesta.append((StringBuilder) method.invoke(obj));
			
		} catch(Exception e) {
			logger.error(e.getMessage());
			String id = Funciones.retrieveObjectValue(obj, "id").toString();
			String uri = Funciones.obtenerPath(obj.getClass()) + id;
			String title = Funciones.removeHTMLEntity((String)Funciones.retrieveObjectValue(obj, "title"));
			String description = (String)Funciones.retrieveObjectValue(obj, "description");
			String summary = (String)Funciones.retrieveObjectValue(obj, "summary");
			
			String dateCreated = ConvertDate.date2StringGmt((Date)Funciones.retrieveObjectValue(obj, "dateCreated"));
			String created = ConvertDate.date2StringGmt((Date)Funciones.retrieveObjectValue(obj, "creationDate"));
			String lastUpdated = ConvertDate.date2StringGmt((Date)Funciones.retrieveObjectValue(obj, "lastUpdated"));
			String pubDate = ConvertDate.date2StringGmt((Date)Funciones.retrieveObjectValue(obj, "pubDate"));
			
			if ("".equals(lastUpdated)) {
				lastUpdated = created;
			}
			if ("".equals(dateCreated)) {
				dateCreated = created;
			}
			if ("".equals(pubDate)) {
				pubDate = created;
			}
			
			if (!"".equals(dateCreated)) {
				Funciones.getPeticion().establecerLastModified(obj);
	
				respuesta.append("<item>"
				         + "<title>" + title + "</title>"
				         + "<link>" + uri + "</link>"
				         + "<description><![CDATA[" + (summary == null ? "" : summary) + "]]></description>"
				         + "<pubDate>" + pubDate + "</pubDate>"
//				         + "<op-published>" + pubDate  + "</op-published>"
//				         + "<op-modified>" + lastUpdated + "</op-modified>"
				         + "<guid>" + uri + "</guid>"
				         + "<content:encoded><![CDATA[" + description == null  ? "" : description + "]]></content:encoded>"
				      + "</item>");
			}
		}
	}

	@Override
	public String getSeparador() {
		return null;
	}

	@Override
	public String getInicioObjeto(String nombreObjeto) {
		return null;
	}

	@Override
	public String getFinObjeto(String nombreObjeto) {
		return null;
	}

	@Override
	public String getInicioCampoObjeto(String nombreCampo) {
		return null;
	}

	@Override
	public String getInicioArray() {
		return null;
	}

	@Override
	public String getInicioArray(String nombreCampo) {
		return null;
	}

	@Override
	public String getFinArray(String nombreCampo) {
		return null;
	}

	@Override
	public String getFinCampoObjeto(String nombreCampo) {
		return null;
	}

	@Override
	public String escribirValorCampo(String nombreCampo, Object valor,
			boolean anotacionPresente, String formato) {
		return null;
	}

	@Override
	public void enviarError(HttpServletResponse response, int statusCode,
			String mensaje, boolean peticionSoloHead) {
		
	}

	@Override
	public Object pasarAObjeto(String valor, boolean esArray, Class<?>... type)
			throws FormatoNoSoportadoException, InstantiationException,
			IllegalAccessException {
		return null;
	}

	@Override
	public String getMensajeErrorValidacion(
			Set<ConstraintViolation<Object>> constraintViolations) {
		return null;
	}

	@Override
	public String getMensajeErrorConstraint(
			Set<ConstraintViolation<?>> constraintViolations) {
		return null;
	}

	@Override
	public String getInicioCampoObjetoNoListado(String nombreCampo) {
		return null;
	}

	@Override
	public String getFinCampoObjetoNoListado(String nombreCampo) {
		return null;
	}

	@Override
	public String getIDPrefijo() {
		return null;
	}

	@Override
	public String transformarGeometria(Geometria valor, Peticion peticion) {
		return null;
	}

	@Override
	public String getInicioCampoObjetoConSub(String key) {
		return null;
	}

	@Override
	public String getFinCampoObjetoConSub(String key) {
		return null;
	}

	@Override
	public String getInicioCampo(String name) {
		return null;
	}

	@Override
	public String escribirValor(String name, Object object,
			boolean anotacionPresente, String formato) {
		return null;
	}

	@Override
	public String escribirValorObjecto(String key, String string,
			boolean anotacionPresente, String formato) {
		return null;
	}
}
