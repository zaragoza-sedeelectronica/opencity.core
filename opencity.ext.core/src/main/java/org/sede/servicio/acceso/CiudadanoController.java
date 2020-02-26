package org.sede.servicio.acceso;


import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.apache.axis.utils.StringUtils;
import org.apache.cxf.jaxrs.ext.search.SearchParseException;
import org.sede.core.anotaciones.Description;
import org.sede.core.anotaciones.Esquema;
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
//import org.sede.servicio.preguntas.dao.PreguntaGenericDAO;
//import org.sede.servicio.presupuestosparticipativos.dao.PropuestaGenericDAO;
//import org.sede.servicio.presupuestosparticipativos.entity.Propuesta;
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

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;

@Gcz(servicio="REUTILIZADOR",seccion="APLICACION")
@Description("Desarrolladores")
@Transactional(ConfigCiudadano.TM)
@Controller
@RequestMapping(value = "/" + CiudadanoController.MAPPING, method = RequestMethod.GET)
public class CiudadanoController {
	
	private static final Logger logger = LoggerFactory.getLogger(CiudadanoController.class);
	
	private static final String SERVICIO = "zona-personal";
	public static final String MAPPING = "servicio/" + SERVICIO;
	public static final String MAPPING_DETALLE = "servicio/" + SERVICIO + "/detalle";
	public static final String MAPPING_CREAR = "servicio/" + SERVICIO + "/new";
	
	@Autowired
	private CiudadanoGenericDAO dao;

//	@Autowired
//	private PropuestaGenericDAO daoPropuesta;
//
//	@Autowired
//	private PreguntaGenericDAO daoPregunta;
	
	@PermisosUser
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String detalle(Model model, HttpServletRequest request) {
		
		request.getSession().removeAttribute(CheckeoParametros.REQUIREMENTESATTR);
		
		Search busqueda = new Search().setMaxResults(150).setFirstResult(0);
		Ciudadano ciudadano = Funciones.getUser(request);
		if ("Si".equals(ciudadano.getEmpadronado())) {
			busqueda.addFilter(Filter.equal("userId", dao.obtenerUsuario(ciudadano.getAccount_id()).getId()));
//			SearchResult<Propuesta> listado = daoPropuesta.searchAndCount(busqueda);
//			BigDecimal numComentariosPropuestas = daoPropuesta.numComentariosTotalesUsuario(ciudadano);
//			BigDecimal numVotos = daoPropuesta.numVotosTotalesUsuario(ciudadano);
//			
//			if (listado.getResult().isEmpty() && numComentariosPropuestas.intValue() == 0 && numVotos.intValue() == 0) {
//				model.addAttribute("modificarJunta", true);
//			} 
			
//			model.addAttribute("propuestas", listado);
//			model.addAttribute("comentariosPropuestas", numComentariosPropuestas);
//			model.addAttribute("votosPropuestas", numVotos);
//			model.addAttribute("preguntas", daoPregunta.searchAndCount(busqueda));
//			model.addAttribute("votoLinea2Tranvia", daoPropuesta.usuarioHaVotadoLinea2Tranvia(ciudadano.getNif()));
		}
		
		model.addAttribute(ModelAttr.DATO, ciudadano);
		return MAPPING_DETALLE;
	}

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
	
	@PermisosUser(requirements = Padron.class)
	@RequestMapping(value = "/asociar-padron", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String asociarPadron(Model model, HttpServletRequest request) {
		return "redirect:/" + MAPPING + "/";
	}
	
	@PermisosUser(requirements = Mobile.class)
	@RequestMapping(value = "/asociar-movil", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String asociarMovil(Model model, HttpServletRequest request) {
		return "redirect:/" + MAPPING + "/";
	}
	
//	@OpenData
//	@Permisos(Permisos.DET)
	@NoCache
	@ResponseClass(Mensaje.class)
	@RequestMapping(value = "/update-padron", method = RequestMethod.GET, produces = {MimeTypes.JSON})
	public @ResponseBody ResponseEntity<?> apiUpdatePadron() {
		dao.updateDatosJuntaPadron();
		return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(), "Actualización completada"));
	}

//	@OpenData
//	@Permisos(Permisos.DET)
	@NoCache
	@ResponseClass(Mensaje.class)
	@RequestMapping(value = "/update-distrito-seccion", method = RequestMethod.GET, produces = {MimeTypes.JSON})
	public @ResponseBody ResponseEntity<?> apiUpdateDistritoSeccion() {
		dao.updateDatosDistritoSeccionPadron();
		return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(), "Actualización completada"));
		
	}
	
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

//	@Cacheable(Cache.DURACION_30MIN)
	@Permisos(Permisos.ADJ)
	@ResponseClass(value = Ciudadano.class, entity = SearchResult.class)
	@RequestMapping(value = "/usuarios", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiUsuariosListar(@Fiql SearchFiql search) throws SearchParseException {
		return ResponseEntity.ok(dao.searchAndCount(search.getConditions(Ciudadano.class)));
	}

}
