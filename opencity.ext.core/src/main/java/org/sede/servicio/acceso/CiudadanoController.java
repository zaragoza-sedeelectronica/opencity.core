package org.sede.servicio.acceso;


import javax.servlet.http.HttpServletRequest;

import org.apache.axis.utils.StringUtils;
import org.apache.cxf.jaxrs.ext.search.SearchParseException;
import org.sede.core.anotaciones.Description;
import org.sede.core.anotaciones.Fiql;
import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.NoCache;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.PermisosUser;
import org.sede.core.anotaciones.ResponseClass;
import org.sede.core.dao.SearchFiql;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.MimeTypes;
import org.sede.core.utils.Funciones;
import org.sede.servicio.ModelAttr;
import org.sede.servicio.acceso.dao.CiudadanoGenericDAO;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.sede.servicio.acceso.userrequirements.Mobile;
import org.sede.servicio.acceso.userrequirements.Padron;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.googlecode.genericdao.search.SearchResult;

/**
 * Class CiudadanoController.
 * 
 * @author Ayuntamiento Zaragoza1
 * 
 */
@Gcz(servicio="REUTILIZADOR",seccion="APLICACION")
@Description("Desarrolladores")
@Transactional(ConfigCiudadano.TM)
@Controller
@RequestMapping(value = "/" + CiudadanoController.MAPPING, method = RequestMethod.GET)
public class CiudadanoController {
	
	/** Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(CiudadanoController.class);
	
	/** Constant SERVICIO. */
	private static final String SERVICIO = "zona-personal";
	
	/** Constant MAPPING. */
	public static final String MAPPING = "servicio/" + SERVICIO;
	
	/** Constant MAPPING_DETALLE. */
	public static final String MAPPING_DETALLE = "servicio/" + SERVICIO + "/detalle";
	
	/** Constant MAPPING_CREAR. */
	public static final String MAPPING_CREAR = "servicio/" + SERVICIO + "/new";

	public static final String MAPPING_DETALLE_MIS_PARTICIPACIONES = "servicio/" + SERVICIO + "/detalle-mis-participaciones";

	/** dao. */
	@Autowired
	private CiudadanoGenericDAO dao;
	
	/**
 * Detalle.
 *
 * @param model Model
 * @param request Request
 * @return string
 */
@PermisosUser
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String detalle(Model model, HttpServletRequest request) {
		
		request.getSession().removeAttribute(CheckeoParametros.REQUIREMENTESATTR);
		
		Ciudadano ciudadano = Funciones.getUser(request);
		
		model.addAttribute(ModelAttr.DATO, ciudadano);
		return MAPPING_DETALLE;
	}

	/**
	 * Acceso a página Mis Participaciones
	 * @param model
	 * @param request
	 * @return
	 */
	@PermisosUser
	@RequestMapping(value="/detalle-mis-participaciones", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE, "*/*" })
	public String detalleMisParticipaciones(Model model, HttpServletRequest request) {

		request.getSession().removeAttribute(CheckeoParametros.REQUIREMENTESATTR);

		Ciudadano ciudadano = Funciones.getUser(request);

		model.addAttribute(ModelAttr.DATO, ciudadano);
		return MAPPING_DETALLE_MIS_PARTICIPACIONES;
	}

	/**
	 * Save.
	 *
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @param request Request
	 * @return string
	 */
	@PermisosUser
	@RequestMapping(value = "/update", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String save(Ciudadano dato,
			BindingResult bindingResult, Model model, HttpServletRequest request) {
		try {
			Ciudadano c = Funciones.getUser(request);
			
			if (StringUtils.isEmpty(c.getScreen_name())) {
				c.setScreen_name(dato.getScreen_name());
				if(dao.updateScreenName(c.getId(), c.getScreen_name())){
					model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Pseudónimo/Nick modificado correctamente"));
					request.getSession().setAttribute(CheckeoParametros.SESSIONCIUDADANO, c);
					return detalle(model, request);
				} else {
					model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha realizado la modificación del Pseudónimo/Nick"));
					return detalle(model, request);
				}
			} else {
				model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha realizado la modificación del Pseudónimo/Nick"));
				return detalle(model, request);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			return detalle(model, request);
		}
	}
	
	/**
	 * Change password.
	 *
	 * @param pass Pass
	 * @param passRep Pass rep
	 * @param model Model
	 * @param request Request
	 * @return string
	 */
	@PermisosUser
	@RequestMapping(value = "/change-password", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String changePassword(@RequestParam(name = "pass", required = true) String pass,
			@RequestParam(name = "pass_rep", required = true) String passRep,
			Model model, HttpServletRequest request) {
		
		if (pass != null && pass.length() > 2 && pass.equals(passRep)) {
			Ciudadano ciudadano = Funciones.getUser(request);
			boolean correcto = dao.updatePassword(ciudadano.getId(), pass);
			if (correcto) {
				model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Contraseña modificada correctamente"));
				return detalle(model, request);
			} else {
				model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha actualizado ningún registro"));
			}
		} else {
			model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "Las contraseñas introducidas no coinciden"));
		}
		return detalle(model, request);
	}

	/**
	 * Change domain.
	 *
	 * @param domain Domain
	 * @param model Model
	 * @param request Request
	 * @param attr Attr
	 * @return string
	 */
	@PermisosUser
	@RequestMapping(value = "/change-domain", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String changeDomain(@RequestParam(name = "domain", required = true) String domain,
			Model model, HttpServletRequest request, RedirectAttributes attr) {
		Ciudadano ciudadano = Funciones.getUser(request);
		ciudadano.setJuntaUser(domain);
		boolean correcto = dao.updateJuntaUsuario(ciudadano);
		if (correcto) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro modificado correctamente"));
			return detalle(model, request);
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha actualizado ningún registro"));
		}
		return detalle(model, request);
	}
	
	/**
	 * Asociar padron.
	 *
	 * @param model Model
	 * @param request Request
	 * @return string
	 */
	@PermisosUser(requirements = Padron.class)
	@RequestMapping(value = "/asociar-padron", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String asociarPadron(Model model, HttpServletRequest request) {
		return "redirect:/" + MAPPING + "/";
	}
	
	/**
	 * Asociar movil.
	 *
	 * @param model Model
	 * @param request Request
	 * @return string
	 */
	@PermisosUser(requirements = Mobile.class)
	@RequestMapping(value = "/asociar-movil", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String asociarMovil(Model model, HttpServletRequest request) {
		return "redirect:/" + MAPPING + "/";
	}
	
//	@OpenData
/**
 * Api update padron.
 *
 * @return response entity
 */
	@NoCache
	@ResponseClass(Mensaje.class)
	@RequestMapping(value = "/update-padron", method = RequestMethod.GET, produces = {MimeTypes.JSON})
	public @ResponseBody ResponseEntity<?> apiUpdatePadron() {
		dao.updateDatosJuntaPadron();
		return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(), "Actualización completada"));
	}

//	@OpenData
/**
 * Api update distrito seccion.
 *
 * @return response entity
 */
	@NoCache
	@ResponseClass(Mensaje.class)
	@RequestMapping(value = "/update-distrito-seccion", method = RequestMethod.GET, produces = {MimeTypes.JSON})
	public @ResponseBody ResponseEntity<?> apiUpdateDistritoSeccion() {
		dao.updateDatosDistritoSeccionPadron();
		return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(), "Actualización completada"));
		
	}
	
	/**
	 * Asociar imagen.
	 *
	 * @param c C
	 * @param file File
	 * @param bindingResult Binding result
	 * @param model Model
	 * @param attr Attr
	 * @param request Request
	 * @return string
	 */
	@PermisosUser
	@RequestMapping(value = "/set-image", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String asociarImagen(Ciudadano c, @RequestParam("file") MultipartFile file, BindingResult bindingResult,
			Model model, RedirectAttributes attr, HttpServletRequest request) {
		if (!file.isEmpty()) {
			Ciudadano ciudadano = Funciones.getUser(request);
			attr.addFlashAttribute(ModelAttr.MENSAJE, dao.asociarImagen(ciudadano, file));
			request.getSession().setAttribute(CheckeoParametros.SESSIONCIUDADANO, ciudadano);
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "Debe seleccionar una imagen"));
		}
		return "redirect:/" + MAPPING;
	}

	/**
	 * Eliminar imagen.
	 *
	 * @param c C
	 * @param attr Attr
	 * @param request Request
	 * @return string
	 */
	@PermisosUser
	@RequestMapping(value = "/del-image", method = RequestMethod.POST, produces = {MediaType.TEXT_HTML_VALUE, "*/*" })
	public String removeImagen(Ciudadano c, RedirectAttributes attr, HttpServletRequest request) {
		Ciudadano ciudadano = Funciones.getUser(request);
		attr.addFlashAttribute(ModelAttr.MENSAJE, dao.removeImagen(ciudadano));
		request.getSession().setAttribute(CheckeoParametros.SESSIONCIUDADANO, ciudadano);
		return "redirect:/" + MAPPING;
	}

	/**
	 * Api usuarios listar.
	 *
	 * @param search Search
	 * @return response entity
	 * @throws SearchParseException the search parse exception
	 */
	@Permisos(Permisos.ADJ)
	@ResponseClass(value = Ciudadano.class, entity = SearchResult.class)
	@RequestMapping(value = "/usuarios", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiUsuariosListar(@Fiql SearchFiql search) throws SearchParseException {
		return ResponseEntity.ok(dao.searchAndCount(search.getConditions(Ciudadano.class)));
	}

}
