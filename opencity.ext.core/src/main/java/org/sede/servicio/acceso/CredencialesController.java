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

@Gcz(servicio="ADMIN",seccion="ADMIN")
@Transactional(Constants.TM)
@Controller
@RequestMapping(value = "/" + CredencialesController.MAPPING, method = RequestMethod.GET)
public class CredencialesController {
	private static final String SERVICIO = "credenciales";
	public static final String MAPPING_PLANTILLA = "portal/" + SERVICIO;
	public static final String MAPPING = "servicio/" + SERVICIO;
		
	@Autowired
	GczUsuarioGenericDAO dao;
	
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String redirect() {
		return "redirect:" + SERVICIO + "/";
	}
	
	@RequestMapping(path = "/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	@Permisos(Permisos.DET)
	public String home() {
		return MAPPING + "/index";
	}

}
