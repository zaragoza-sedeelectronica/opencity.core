package org.sede.core.rest.view;


import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Locale;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.IOUtils;
import org.sede.core.anotaciones.AvoidLastModifiedHeader;
import org.sede.core.exception.FormatoNoSoportadoException;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.filter.InterceptorPeticion;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.MimeTypes;
import org.sede.core.rest.Peticion;
import org.sede.core.rest.RespuestaDirecta;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.GenericHttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;

public class Transformador extends AbstractGenericHttpMessageConverter<Object> 
	implements GenericHttpMessageConverter<Object> {

	private static final Logger log = LoggerFactory
			.getLogger(Transformador.class);
	
	public static final Charset DEFAULT_CHARSET = Charset.forName(CharEncoding.UTF_8);
	
	private MediaType mediaType = null;
	
	public Transformador() {
		super();
	}

	@Override
	protected boolean supports(Class<?> clazz) {
		return true;
	}

	@Override
	public boolean canRead(Class<?> clazz, MediaType mediaType) {
		return MediaType.APPLICATION_JSON.isCompatibleWith(mediaType);
	}

	
	private Charset getCharset(HttpHeaders headers) {
		if (headers == null || headers.getContentType() == null || headers.getContentType().getCharset() == null) {
			return DEFAULT_CHARSET;
		}
		return headers.getContentType().getCharset();
	}
	public boolean canRead(Type arg0, Class<?> arg1, MediaType mediaType) {
		this.mediaType = mediaType;
		return true;
	}

	public boolean canWrite(Type arg0, Class<?> arg1, MediaType mediaType) {
		this.mediaType = mediaType;
		return true;
	}

	@Override
	protected Object readInternal(Class<? extends Object> arg0,
			HttpInputMessage inputMessage) throws IOException {
		return new Object();
	}

	public Object read(Type tipo, Class<?> arg1, HttpInputMessage body)
			throws IOException {
		
		Peticion peticion = Funciones.getPeticion();
		Object retorno = null;
		try {
			retorno = peticion.getFormato().getTransformador().pasarAObjeto(IOUtils.toString(body.getBody(), CharEncoding.UTF_8), false, getClass(tipo));
		} catch (FormatoNoSoportadoException e) {
			log.error(e.getLocalizedMessage());
		} catch (InstantiationException e) {
			log.error(e.getLocalizedMessage());
		} catch (IllegalAccessException e) {
			log.error(e.getLocalizedMessage());
		} catch (ClassNotFoundException e) {
			log.error(e.getLocalizedMessage());
		}
		return retorno;
	}


	@Override
	protected void writeInternal(Object registro, Type arg1, HttpOutputMessage outputMessage)
			throws IOException {
		Charset charset = getCharset(outputMessage.getHeaders());
		StringBuilder respuesta = new StringBuilder();
		Peticion peticion = Funciones.getPeticion();
		if (peticion.getFormato() == null) {
			peticion.setFormato(MimeTypes.listado.get(mediaType.toString()));
		}
		if (peticion.isDebug()) {
			log.info("PETICION: {}", peticion);
		}
		try {
			if (registro instanceof byte[]) {
				outputMessage.getBody().write((byte[])registro);
			} else {
				if (peticion.isDebug()) {
					log.error("inicio transformador");
				}
				if (registro instanceof RespuestaDirecta) {
					respuesta.append(((RespuestaDirecta)registro).getRespuesta());
				} else {
					if (peticion.getFormato() == null) {
						peticion.setFormato(InterceptorPeticion.obtenerFormato(peticion.getUri(), null));
					} 
					peticion.getFormato().getTransformador().transformarObjeto(respuesta, registro, peticion, true, "");
					
				}
				if (
					(peticion.getClaseRetorno() == null 
						|| !peticion.getClaseRetorno().isAnnotationPresent(AvoidLastModifiedHeader.class)
					) && peticion.getLastModified() != null
				) {
					outputMessage.getHeaders().add(CheckeoParametros.HEADERLASTMODIFIED, ConvertDate.date2String(peticion.getLastModified(), ConvertDate.PATTERN_RFC1123, Locale.US));
				} else {
					log.info("la petición no contiene atributo " + CheckeoParametros.HEADERLASTMODIFIED);
				}
				
				if (peticion.isDebug()) {
					log.error("llega");
					log.error(respuesta.toString());
					log.error("fin transformador");
				}
				OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody(), charset);
				writer.append(respuesta);
				writer.close();
				if (peticion.isDebug()) {
					log.error("fin escritura");
				}
			}
			
		} catch (InvalidImplementationException e) {
			if (outputMessage instanceof ServerHttpResponse) {
				((ServerHttpResponse) outputMessage).setStatusCode(HttpStatus.BAD_REQUEST);
			}
			OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody()/*, charset*/);
			writer.append("error: " + e.getMessage());
			writer.close();
		} catch (Exception e) {
			if (outputMessage instanceof ServerHttpResponse) {
				((ServerHttpResponse) outputMessage).setStatusCode(HttpStatus.BAD_REQUEST);
			}
			OutputStreamWriter writer = new OutputStreamWriter(outputMessage.getBody()/*, charset*/);
			writer.append("error: " + e.getMessage());
			writer.close();
			if (peticion.isDebug()) {
				log.error(Funciones.getStackTrace(e));
			}
			log.error("{} URI: {}", e.getMessage(), peticion.getUri());
		}
	}

	private static Class<?> getClass(Type type) throws ClassNotFoundException {
	    if (type==null) {
	        return null;
	    }
	    String className = type.toString();
	    String typeNamePrefix = "class ";
		if (className.startsWith(typeNamePrefix)) {
	        className = className.substring(typeNamePrefix.length());
	    }
	    return Class.forName(className);
	}
}