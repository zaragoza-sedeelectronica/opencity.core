package org.sede.portal;

import javax.servlet.http.HttpServletRequest;

import org.sede.core.anotaciones.Cache;
import org.sede.core.plantilla.LayoutInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/portal", method = RequestMethod.GET)
public class PortalController {

	private static final Logger logger = LoggerFactory.getLogger(PortalController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE})
	public String home(Model model) {
		logger.info("LLEGA a raiz de portal");
		return "portal/index";
	}
	@Cache(Cache.DEFAULT_CACHE_NAME)
	@RequestMapping(value = "/**/*", method = RequestMethod.GET)
	public ModelAndView contenido(Model model, HttpServletRequest request) {
		String path = request.getServletPath();
		int indexServicio = path.indexOf("/servicio/");
		
		if (indexServicio > 0) {
			if (path.indexOf("/portal/") >= 0) {
				request.setAttribute(LayoutInterceptor.PLANTILLA_PORTAL, path.substring(0, indexServicio));
			}
			return new ModelAndView("forward:" + path.substring(indexServicio, path.length()));
		} else {
			
			int indexEnlace = path.indexOf("/enlace/");
			String vista;
			if (indexEnlace > 0) {
				request.setAttribute(LayoutInterceptor.PLANTILLA_PORTAL, path.substring(0, indexEnlace));
				vista = "/portal/" + path.substring(indexEnlace + 8, path.length());
			} else {
				vista = path.substring(1, path.length());
			}
			if (vista.length() == vista.lastIndexOf('/') + 1) {
				vista = vista + "index";
			}
			return new ModelAndView(vista);
		}
	}
	
}
