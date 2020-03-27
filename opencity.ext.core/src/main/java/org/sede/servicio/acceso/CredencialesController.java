package org.sede.servicio.acceso;

import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.Permisos;
import org.sede.servicio.acceso.dao.GczUsuarioGenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Class CredencialesController.
 * 
 *  @author Ayuntamiento Zaragoza
 *  
 */
@Gcz(servicio="ADMIN",seccion="ADMIN")
@Transactional(ConfigAcceso.TM)
@Controller
@RequestMapping(value = "/" + CredencialesController.MAPPING, method = RequestMethod.GET)
public class CredencialesController {
	
	/** Constant SERVICIO. */
	private static final String SERVICIO = "credenciales";
	
	/** Constant MAPPING_PLANTILLA. */
	public static final String MAPPING_PLANTILLA = "portal/" + SERVICIO;
	
	/** Constant MAPPING. */
	public static final String MAPPING = "servicio/" + SERVICIO;
		
	/** dao. */
	@Autowired
	GczUsuarioGenericDAO dao;
	
	/**
	 * Redirect.
	 *
	 * @return string
	 */
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String redirect() {
		return "redirect:" + SERVICIO + "/";
	}
	
	/**
	 * Home.
	 *
	 * @return string
	 */
	@RequestMapping(path = "/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	@Permisos(Permisos.DET)
	public String home() {
		return MAPPING + "/index";
	}

}
