package org.sede.servicio.acceso;


import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.jaxrs.ext.search.SearchParseException;
import org.sede.core.anotaciones.Fiql;
import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.NoCache;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.dao.SearchFiql;
import org.sede.core.rest.Mensaje;
import org.sede.servicio.ModelAttr;
import org.sede.servicio.acceso.dao.CiudadanoGenericDAO;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.sede.servicio.padron.dao.PadronGenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.util.StringUtils;

/**
 * Controlador CiudadanoAdmin
 *
 * @author Ayuntamiento Zaragoza
 *
 */
@Gcz(servicio="OGOB",seccion="OGOB")
@Transactional(ConfigCiudadano.TM)
@Controller
@RequestMapping(value = "/" + CiudadanoAdminController.MAPPING, method = RequestMethod.GET)
public class CiudadanoAdminController {
	
	/** Constant SERVICIO. */
	private static final String SERVICIO = "admin-user";
	
	/** Constant MAPPING. */
	public static final String MAPPING = "servicio/" + SERVICIO;
	
	/** dao. */
	@Autowired
	private CiudadanoGenericDAO dao;

	/** dao padron. */
	@Autowired
	private PadronGenericDAO daoPadron;
	
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
	 * Index.
	 *
	 * @param model Model
	 * @param dato Dato
	 * @param search Search
	 * @param request Request
	 * @return string
	 * @throws SearchParseException the search parse exception
	 */
	@NoCache
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	@Permisos(Permisos.ADMIN)
	public String index(Model model, 
			@ModelAttribute Ciudadano dato,
			@Fiql SearchFiql search, HttpServletRequest request) throws SearchParseException {
		String nif = request.getParameter("nifContains");
		if (!StringUtils.isEmpty(nif) 
				&& dato.getBirthYear() != null) {
			Boolean empadronado = daoPadron.checkEmpadronado(nif, dato.getBirthYear()); 
			if (empadronado == null) {
				model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "Error al acceder a los datos de padrón"));	
			} else if (empadronado == Boolean.TRUE) {
				StringBuilder mensaje = new StringBuilder();
				mensaje.append("<ul><li>Distrito/sección: " + daoPadron.showDistrito(nif, dato.getBirthYear()) + "</li>");
				mensaje.append("<li>" + daoPadron.showJunta(nif, dato.getBirthYear()) + "</li></ul>");
				model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Datos de padrón: " + mensaje.toString()));
			} else {
				model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "Los datos no corresponden a un ciudadano empadronado")); 
			}
		}
		
		model.addAttribute(ModelAttr.RESULTADO, ResponseEntity.ok(dao.searchAndCount(search.getConditions(Ciudadano.class))));
		model.addAttribute(ModelAttr.DATO, dato);
		return MAPPING + "/index";
	}
	
}
