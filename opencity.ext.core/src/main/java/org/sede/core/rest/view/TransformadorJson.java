package org.sede.core.rest.view;


import java.io.IOException;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;

import org.sede.core.exception.FormatoNoSoportadoException;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.Xss;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class TransformadorJson implements TransformadorGenerico {
	private Html2MarkDown transformadorMarkDown = new Html2MarkDown();
	ObjectMapper mapper = new ObjectMapper();
	
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
	public String getFinCampoObjetoConSub(String nombreCampo) {
		return getFinObjeto(nombreCampo);
	}
	public String getInicioCampoObjetoConSub(String nombreCampo) {
		return "\"" + nombreCampo + "\":{";
	}
	
	public String escribirValorCampo(String nombreCampo, Object valor, boolean anotacionPresente, String formato) {
		if (valor instanceof Integer || valor instanceof Double || valor instanceof BigDecimal || valor instanceof Boolean) {
			return "\"" + nombreCampo + "\":" + Funciones.escape(valor.toString()) + "";
		} else if (valor instanceof Date) {
			Date fecha = (Date) valor;
			return "\"" + nombreCampo + "\":\"" + ConvertDate.date2String(fecha, ConvertDate.ISO8601_FORMAT) + "\"";
		} else {
			if (anotacionPresente && CheckeoParametros.RESPUESTAMARKDOWN.equals(formato)) {
				return "\"" + nombreCampo + "\":\"" +  Funciones.escape(transformadorMarkDown.transformar(valor.toString())) + "\"";
			} else {
				return "\"" + nombreCampo + "\":\"" + Funciones.escape(valor.toString()) + "\"";
			}
		}
	}
	public String escribirValor(String nombreCampo, Object valor, boolean anotacionPresente, String formato) {
		if (valor instanceof Integer || valor instanceof Double || valor instanceof Long || valor instanceof BigDecimal || valor instanceof Boolean) {
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
			return "\"" + nombreCampo + "\":" + Funciones.escape(transformadorMarkDown.transformar(valor)) + "";
		} else {
			return "\"" + nombreCampo + "\":" + valor + "";
		}
		
	}
	public void enviarError(HttpServletResponse response, int statusCode, String mensaje, boolean peticionSoloHead) {
		String respuesta;
		if (mensaje != null && mensaje.startsWith("[")) {
			respuesta = "{\"error\":" + mensaje + "}";
		} else {
			respuesta = "{\"error\":\"" + mensaje + "\"}";
		}
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

	public Object pasarAObjeto(String valor, boolean esArray, Class<?>... type) throws FormatoNoSoportadoException, InstantiationException {
		try {
			if (esArray) {
				Object instancia = Array.newInstance(type[0], calcularTamanyo(valor));
		        instancia = mapper.readValue(Xss.comprobar(valor), type[0]);
				return instancia;
			} else {
				Object instancia = type[0].newInstance();
				instancia = mapper.readValue(Xss.comprobar(valor), type[0]);
				return instancia;
			}
		} catch (JsonParseException e) {
			throw new InstantiationException(e.getMessage());
		} catch (JsonMappingException e) {
			throw new InstantiationException(e.getMessage());
		} catch (IOException e) {
			throw new InstantiationException(e.getMessage());
		} catch (IllegalAccessException e) {
			throw new InstantiationException(e.getMessage());
		}
	
	}

	private int calcularTamanyo(String valor) throws IOException {
		// TODO mirar alternativa para no volver a transformar el objeto
		ObjectMapper mapperObj = new ObjectMapper();
		JsonFactory factory = mapperObj.getFactory();
		JsonParser jp = factory.createParser(valor);
		JsonNode nodo = mapperObj.readTree(jp);
		return nodo.size();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getMensajeErrorValidacion(Set<ConstraintViolation<Object>> constraintViolations) {
		StringBuilder respuesta = new StringBuilder();
		respuesta.append("[");
		for (Iterator iterator = constraintViolations.iterator(); iterator.hasNext();) {
			ConstraintViolation<Object> constraintViolation = (ConstraintViolation<Object>) iterator.next();
			respuesta.append("{" + getInicioCampo(constraintViolation.getPropertyPath().toString()) + "\"" + constraintViolation.getMessage() + "\"},");
		}
		return respuesta.substring(0, respuesta.length() - 1) + "]";
	}
	// El codigo es igual que en getMensajeErrorValidacion, solo cambia el tipo del Set<ConstraintViolation>
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getMensajeErrorConstraint(Set<ConstraintViolation<?>> constraintViolations) {
		StringBuilder respuesta = new StringBuilder();
		respuesta.append("[");
		for (Iterator iterator = constraintViolations.iterator(); iterator.hasNext();) {
			ConstraintViolation<Object> constraintViolation = (ConstraintViolation<Object>) iterator.next();
			respuesta.append("{" + getInicioCampo(constraintViolation.getPropertyPath().toString()) + "\"" + constraintViolation.getMessage() + "\"},");
		}
		return respuesta.substring(0, respuesta.length() - 1) + "]";
	}

	public String getInicioCampoObjetoNoListado(String nombreCampo) {
		return "\"" + nombreCampo + "\":";
	}

	public String getFinCampoObjetoNoListado(String nombreCampo) {
		return "";
	}
	public String getIDPrefijo() {
		return "@";
	}
	public void transformarObjeto(StringBuilder respuesta, Object retorno,
			Peticion peticion, boolean primero, String prefijo)
			throws InvalidImplementationException {
		TransformadorBasico trans = new TransformadorBasico();
		trans.transformarObjeto(respuesta, retorno, peticion, primero, prefijo);
	}

	public String transformarGeometria(Geometria valor, Peticion peticion) {
		return valor.asJson(peticion.getSourceSrs(), peticion.getSrsName());
	}

	public String getInicioArray() {
		return "[";
	}
	public String getInicioArray(String nombreCampo) {
		return getInicioArray();
	}

	public String getFinArray(String nombreCampo) {
		return getFinCampoObjeto(nombreCampo);
	}
}
