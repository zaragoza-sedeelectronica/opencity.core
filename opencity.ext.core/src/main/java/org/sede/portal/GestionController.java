package org.sede.portal;

import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.Permisos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/gestion", method = RequestMethod.GET)
@Gcz(servicio="REUTILIZADOR",seccion="APLICACION")
public class GestionController {

	private static final Logger logger = LoggerFactory.getLogger(GestionController.class);
	
	@Permisos(Permisos.DET)
	@RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.HEAD}, produces = {
			MediaType.TEXT_HTML_VALUE})
	public String home(Model model) {
		logger.info("LLEGA a raiz de portal");
		return "servicio/gestion/index";
	}
	
	
}
