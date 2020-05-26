package org.sede.core.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.MimeTypes;
import org.sede.core.rest.Rest;
import org.sede.core.utils.Funciones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;


@ControllerAdvice
class GlobalDefaultExceptionHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(GlobalDefaultExceptionHandler.class);
    public static final String DEFAULT_ERROR_VIEW = "error";

    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String defaultErrorHandler(HttpServletRequest request, HttpServletResponse response, Exception e, Model model) throws Exception {
    	if (Funciones.getPeticion() != null && Funciones.getPeticion().isDebug()) {
    		logger.error(Funciones.getStackTrace(e));
    	} else {
    		String query = "ORIGEN:" + request.getHeader("REFERER") + " PETICION:" + request.getRequestURI();
    		
    		if (request.getQueryString() != null && !request.getQueryString().isEmpty()) {
    			query = query + "?" + request.getQueryString();
    		}
    		logger.error("{}:{}",  query, e.getMessage());
    	}
    	
    	if (peticionJson(request)) {
    		response.setContentType(MimeTypes.JSON);
    		response.setHeader("Allow", Rest.METHODS);
    		response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", Rest.METHODS);
			response.setHeader("Access-Control-Allow-Headers", CheckeoParametros.CONTENT_TYPE + "," + CheckeoParametros.ACCEPTHEADER +  "," +CheckeoParametros.HEADERPASSWORD + "," + CheckeoParametros.HEADERCLIENTID + "," + CheckeoParametros.HEADERHMAC);
    		response.getOutputStream().print("{\"status\":" + HttpStatus.BAD_REQUEST.value() 
    				+ ", \"mensaje\":\"" + (e.getMessage() == null ? e.getClass().getCanonicalName() : e.getMessage().replaceAll("\"", "'")) + "\""
    					+ "}");
    		return null;
    	} else if (peticionXml(request)) {
    		response.setContentType(MimeTypes.XML);
    		response.setHeader("Allow", Rest.METHODS);
    		response.setHeader("Access-Control-Allow-Origin", "*");
			response.setHeader("Access-Control-Allow-Methods", Rest.METHODS);
			response.setHeader("Access-Control-Allow-Headers", CheckeoParametros.CONTENT_TYPE + "," + CheckeoParametros.ACCEPTHEADER +  "," +CheckeoParametros.HEADERPASSWORD + "," + CheckeoParametros.HEADERCLIENTID + "," + CheckeoParametros.HEADERHMAC);
    		response.getOutputStream().print("<error><status>" + HttpStatus.BAD_REQUEST.value() 
    				+ "</status><mensaje>" + e.getMessage() + "</mensaje></error>");
    		return null;
    	} else {
	        model.addAttribute("exception", e);
	        return DEFAULT_ERROR_VIEW;
    	}
    }

	private boolean peticionJson(HttpServletRequest request) {
		return (request.getRequestURL().indexOf("." + MimeTypes.JSON_EXTENSION) > 0 
				|| (request.getHeader(CheckeoParametros.ACCEPTHEADER) != null && (request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.JSON) >= 0 ||
					request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.JSONLD) >= 0 ||
					request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.GEOJSON) >= 0 ||
					request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.SPARQL_JSON) >= 0 ||
					request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.SOLR_JSON) >= 0
				)));
	}

	private boolean peticionXml(HttpServletRequest request) {
		return((request.getRequestURL().indexOf("." + MimeTypes.XML_EXTENSION) > 0 ||
				request.getRequestURL().indexOf("." + MimeTypes.RSS_EXTENSION) > 0 ||
				request.getRequestURL().indexOf("." + MimeTypes.GEORSS_EXTENSION) > 0 ||
				request.getRequestURL().indexOf("." + MimeTypes.RDF_EXTENSION) > 0) 
			|| (request.getHeader(CheckeoParametros.ACCEPTHEADER) != null 
				&& (request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.HTML) < 0 && (
				request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.XML) >= 0 ||
				request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.RSS) >= 0 ||
				request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.GEORSS) >= 0 ||
				request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.RDF) >= 0 ||
				request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.SPARQL_XML) >= 0 ||
				request.getHeader(CheckeoParametros.ACCEPTHEADER).indexOf(MimeTypes.SOLR_XML) >= 0)
				)));
	}
}