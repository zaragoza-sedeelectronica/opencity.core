package org.sede.core.rest.view;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.xml.bind.JAXBContext;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;

import org.apache.commons.codec.CharEncoding;
import org.sede.core.exception.FormatoNoSoportadoException;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.ConvertDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransformadorXml implements TransformadorGenerico {
	private static final Logger logger = LoggerFactory.getLogger(TransformadorXml.class);
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
				txt = transformadorMarkDown.transformar(txt) + "";
			}
			if ((txt.indexOf('<') > 0 || txt.indexOf('>') > 0 || txt.indexOf('&') > 0) && txt.indexOf("<![CDATA[") < 0) {
				txt = "<![CDATA[" + txt + "]]>";
			}			
			return "<" + nombreCampo + ">" + txt + "</" + nombreCampo + ">";
		}
	}
	public String escribirValorObjecto(String nombreCampo, String valor, boolean anotacionPresente, String formato) {
		if (anotacionPresente && CheckeoParametros.RESPUESTAMARKDOWN.equals(formato)) {
			return transformadorMarkDown.transformar(valor);
		} else {
			return valor;	
		}
	}
	public String escribirValor(final String nombreCampo, final Object valor, boolean anotacionPresente, String formato) {
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
		try {
			JAXBContext jc = JAXBContext.newInstance(type);
			XMLInputFactory xif = XMLInputFactory.newInstance();
			XMLStreamReader xsr = xif.createXMLStreamReader(new ByteArrayInputStream(valor.getBytes(CharEncoding.UTF_8)));
			xsr = new MyStreamReaderDelegate(xsr);
			return jc.createUnmarshaller().unmarshal(xsr);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new FormatoNoSoportadoException();
		}
	}

	private static class MyStreamReaderDelegate extends StreamReaderDelegate {
		 
        public MyStreamReaderDelegate(XMLStreamReader xsr) {
            super(xsr);
        }
 
        @Override
        public String getAttributeLocalName(int index) {
        	
            return super.getAttributeLocalName(index).toLowerCase().intern();
        }
 
        @Override
        public String getLocalName() {
        	if ("totalcount".equals(super.getLocalName().toLowerCase().intern())) {
        		return "totalCount";
        	}
        	if ("lastupdated".equals(super.getLocalName().toLowerCase().intern())) {
        		return "lastUpdated";
        	}
            return super.getLocalName().toLowerCase().intern();
        }
    }
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getMensajeErrorValidacion(Set<ConstraintViolation<Object>> constraintViolations) {
		StringBuilder respuesta = new StringBuilder();
		for (Iterator iterator = constraintViolations.iterator(); iterator.hasNext();) {
			ConstraintViolation<Object> constraintViolation = (ConstraintViolation<Object>) iterator.next();
			respuesta.append("<error>" + constraintViolation.getPropertyPath().toString() + ":" + constraintViolation.getMessage() + "</error>");
		}
		return respuesta.toString();
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String getMensajeErrorConstraint(
			Set<ConstraintViolation<?>> constraintViolations) {
		StringBuilder respuesta = new StringBuilder();
		for (Iterator iterator = constraintViolations.iterator(); iterator.hasNext();) {
			ConstraintViolation<Object> constraintViolation = (ConstraintViolation<Object>) iterator.next();
			respuesta.append("<error>" + constraintViolation.getPropertyPath().toString() + ":" + constraintViolation.getMessage() + "</error>");
		}
		return respuesta.toString();
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
//		ejecutar método asXML
		try {
			Method method = peticion.getMetodo().getDeclaringClass().getDeclaredMethod(peticion.getMetodo().getName() + "AsXml", Object.class);
			respuesta.append(method.invoke(null, retorno));
		} catch (Exception e) {
			TransformadorBasico trans = new TransformadorBasico();
			trans.transformarObjeto(respuesta, retorno, peticion, primero, prefijo);
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
		// Es erroneo analizar como implementarlo si se soporta xml en el body
		return "[";
	}

	public String getInicioArray(String nombreCampo) {
		return getInicioCampoObjetoNoListado(nombreCampo);
	}

	public String getFinArray(String nombreCampo) {
		return getFinObjeto(nombreCampo);
	}
}
