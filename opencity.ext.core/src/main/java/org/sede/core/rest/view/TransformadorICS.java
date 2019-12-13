package org.sede.core.rest.view;

import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.exception.FormatoNoSoportadoException;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;

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
public class TransformadorICS implements TransformadorGenerico {

	public void transformarObjeto(StringBuilder respuesta, Object retorno,
			Peticion peticion, boolean primero, String prefijo)
			throws InvalidImplementationException {
		StringBuilder ical = new StringBuilder();
		ical.append("BEGIN:VCALENDAR" + System.getProperty("line.separator")
				+ "VERSION:2.0" + System.getProperty("line.separator")
				+ "PRODID:-//ZContent.net//Zap Calendar 1.0//ES" + System.getProperty("line.separator")
				+ "CALSCALE:GREGORIAN" + System.getProperty("line.separator")
				+ "METHOD:PUBLISH" + System.getProperty("line.separator"));
		if (retorno instanceof SearchResult) {
			for (Object obj : ((SearchResult<?>) retorno).getResult()) {
				transformarEntidad(ical, obj);
			}
		} else if (retorno.getClass().isAnnotationPresent(XmlRootElement.class)) {
			transformarEntidad(ical, retorno);
		}
		ical.append("END:VCALENDAR");
		respuesta.append(String.format(ical.toString(), ConvertDate.date2String(Funciones.getPeticion().getLastModified(), ConvertDate.RFC_5545_FORMAT)));
	}

	private void transformarEntidad(StringBuilder respuesta, Object obj) {
		String id = Funciones.retrieveObjectValue(obj, "id").toString();
		String title = (String)Funciones.retrieveObjectValue(obj, "title");
		String description = (String)Funciones.retrieveObjectValue(obj, "description");
		
		String startDate = ConvertDate.date2String((Date)Funciones.retrieveObjectValue(obj, "startDate"), ConvertDate.RFC_5545_FORMAT);
		String created = ConvertDate.date2String((Date)Funciones.retrieveObjectValue(obj, "creationDate"), ConvertDate.RFC_5545_FORMAT);
		String lastUpdated = ConvertDate.date2String((Date)Funciones.retrieveObjectValue(obj, "lastUpdated"), ConvertDate.RFC_5545_FORMAT);
		
		if ("".equals(lastUpdated)) {
			lastUpdated = created;
		}
		if (!"".equals(startDate)) {
			Funciones.getPeticion().establecerLastModified(obj);
			respuesta.append("BEGIN:VEVENT" + System.getProperty("line.separator")
					+ "DTSTART:" + startDate + System.getProperty("line.separator")
					+ "DTSTAMP:%1$s" + System.getProperty("line.separator")
					+ "UID:" + id + System.getProperty("line.separator")
					+ "CREATED:" + created + System.getProperty("line.separator")
					+ "DESCRIPTION:" + (description == null ? "" : description) + System.getProperty("line.separator")
					+ "LAST-MODIFIED:" + lastUpdated + System.getProperty("line.separator")
					+ "STATUS:CONFIRMED" + System.getProperty("line.separator")
					+ "SUMMARY:" + title + System.getProperty("line.separator")
					+ "END:VEVENT" + System.getProperty("line.separator"));
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
