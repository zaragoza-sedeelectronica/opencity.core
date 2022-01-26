package org.sede.servicio.acceso;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.search.SearchParseException;
import org.sede.core.anotaciones.Cache;
import org.sede.core.anotaciones.Description;
import org.sede.core.anotaciones.Fiql;
import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.NoCache;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.PlantillaHTML;
import org.sede.core.anotaciones.ResponseClass;
import org.sede.core.dao.SearchFiql;
import org.sede.core.exception.SinCredencialesDefinidas;
import org.sede.core.exception.SinPermisoParaEjecutar;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.MimeTypes;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.LiderUtils;
import org.sede.servicio.ModelAttr;
import org.sede.servicio.acceso.dao.GczGroupUsuarioGenericDAO;
import org.sede.servicio.acceso.dao.GczUsuarioGenericDAO;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.GczGrupoUsuario;
import org.sede.servicio.acceso.entity.GczUsuario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.googlecode.genericdao.search.SearchResult;

// TODO: Auto-generated Javadoc
/**
 * Class GroupController.
 * 
 *  @author Ayuntamiento Zaragoza
 *  
 * 
 */
@Gcz(servicio="ADMIN",seccion="ADMIN")
@Transactional(ConfigAcceso.TM)
@Controller
@RequestMapping(value = "/" + GroupController.MAPPING, method = RequestMethod.GET)
@PlantillaHTML(CredencialesController.MAPPING)
public class GroupController {
	
	/** Constant RAIZ. */
	private static final String RAIZ = "credenciales/";
	
	/** Constant SERVICIO. */
	private static final String SERVICIO = "group";
	
	/** Constant MAPPING. */
	public static final String MAPPING = "servicio/" + RAIZ + SERVICIO;
	
	/** Constant MAPPING_FORM. */
	private static final String MAPPING_FORM = MAPPING + "/formulario";
	
	/** Constant MAPPING_FORM_LIDER. */
	private static final String MAPPING_FORM_LIDER = MAPPING + "/formulario-lider";
	
	/** Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(GroupController.class);

	/** message source. */
	@Autowired
	private MessageSource messageSource;
	
	/** dao. */
	@Autowired
	GczGroupUsuarioGenericDAO dao;
	
	/** dao usuario. */
	@Autowired
	GczUsuarioGenericDAO daoUsuario;
	
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
	 * Api detalle.
	 *
	 * @param identificador Identificador
	 * @return response entity
	 */
	@Permisos(Permisos.DET)
	@Cache(Cache.DURACION_30MIN)
	@ResponseClass(GczGrupoUsuario.class)
	@RequestMapping(value = "/{identificador}", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiDetalle(@PathVariable BigDecimal identificador) {
		GczGrupoUsuario registro = dao.find(identificador);
		if (registro == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale())));
		} else {
			return ResponseEntity.ok(registro);
		}
	}
	
	/**
	 * Api modificar.
	 *
	 * @param identificador Identificador
	 * @param registro Registro
	 * @return response entity
	 */
	@ResponseClass(value = GczGrupoUsuario.class)
	@RequestMapping(value = "/{identificador}", method = RequestMethod.PUT, consumes = {
			MimeTypes.JSON, MimeTypes.XML }, produces = { MimeTypes.JSON,
			MimeTypes.XML })
	@Permisos(Permisos.MOD)
	public @ResponseBody ResponseEntity<?> apiModificar(@PathVariable BigDecimal identificador,
			@RequestBody GczGrupoUsuario registro) {
		ResponseEntity<?> resp = apiDetalle(identificador);
		if (resp.getStatusCode().is2xxSuccessful()) {
			registro.setId(identificador);
			Set<ConstraintViolation<Object>> errores = dao.validar(registro);
			if (!errores.isEmpty()) {
				return Funciones.generarMensajeError(errores);
			} else {
				registro.setLastUpdated(new Date());
				registro.setUsuarioMod(Funciones.getPeticion().getClientId());
				dao.merge(registro);
				dao.flush();
			}
			return apiDetalle(identificador);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Mensaje) resp.getBody());
		}
	}
	
	
	/**
	 * Api list.
	 *
	 * @param search Search
	 * @return response entity
	 * @throws SearchParseException the search parse exception
	 */
	@NoCache
	@Permisos(Permisos.DET)
	@ResponseClass(value = GczGrupoUsuario.class, entity = SearchResult.class)
	@RequestMapping(method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiList(@Fiql SearchFiql search) throws SearchParseException {
		try {
		return ResponseEntity.ok(dao.searchAndCount(search.getConditions(GczGrupoUsuario.class)));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
    }
	
	/**
	 * Index.
	 *
	 * @param model Model
	 * @param dato Dato
	 * @param search Search
	 * @return string
	 * @throws SearchParseException the search parse exception
	 */
	@NoCache
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	@Permisos(Permisos.DET)
	public String index(Model model, 
			@ModelAttribute GczGrupoUsuario dato,
			@Fiql SearchFiql search) throws SearchParseException {
		model.addAttribute(ModelAttr.RESULTADO, apiList(search));
		model.addAttribute(ModelAttr.DATO, dato);
		return MAPPING + "/index";
	}
	
	/**
	 * Api crear.
	 *
	 * @param registro Registro
	 * @return response entity
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
	@Permisos(Permisos.NEW)
	@Description("Crear registro")
	@ResponseClass(value = GczGrupoUsuario.class)
	public @ResponseBody ResponseEntity<?> apiCrear(
			@RequestBody GczGrupoUsuario registro) {
		Set<ConstraintViolation<Object>> errores = dao.validar(registro);
		if (!errores.isEmpty()) {
			return Funciones.generarMensajeError(errores);
		} else {
			registro.setVisible("S");
			registro.setUsuarioAlta(Funciones.getPeticion().getClientId());
			registro.setCreationDate(new Date());
			dao.save(registro);
			dao.flush();
			return ResponseEntity.ok(registro);
		}
	}
	
	/**
	 * New form.
	 *
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.NEW)
	@RequestMapping(value = "/new", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String newForm(GczGrupoUsuario dato, BindingResult bindingResult,
			Model model) {
		model.addAttribute(ModelAttr.DATO, dato);
		model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(new GczGrupoUsuario()));
		return MAPPING_FORM;
	}
	
	/**
	 * Crear.
	 *
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.NEW)
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String crear(GczGrupoUsuario dato, BindingResult bindingResult, Model model, RedirectAttributes attr) {
		ResponseEntity<?> resultado = apiCrear(dato);
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro creado correctamente"));
			attr.addFlashAttribute(ModelAttr.REGISTRO, resultado);
			attr.addFlashAttribute(ModelAttr.DATO, resultado.getBody());
			return "redirect:/" + MAPPING + "/" + ((GczGrupoUsuario)resultado.getBody()).getId() + "/edit";
		} else {
			model.addAttribute(ModelAttr.MENSAJE, resultado.getBody());
			model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(dato));
			model.addAttribute(ModelAttr.DATO, dato);
			return MAPPING_FORM;
		}
	}
	
	
	
	/**
	 * Edits the.
	 *
	 * @param identificador Identificador
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@NoCache
	@RequestMapping(value = "/{identificador}/edit", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String edit(@PathVariable BigDecimal identificador, GczGrupoUsuario dato,
			BindingResult bindingResult, Model model) {
		GczGrupoUsuario reg = dao.find(identificador);
		ResponseEntity<?> registro;
		if (reg == null) {
			registro = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale())));
		} else {
			registro = ResponseEntity.ok(reg);
		}
		model.addAttribute(ModelAttr.DATO, registro.getBody());
		model.addAttribute(ModelAttr.REGISTRO, registro);
		return MAPPING_FORM;
	}
	
	/**
	 * Modificar.
	 *
	 * @param identificador Identificador
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@RequestMapping(value = "/{identificador}/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String modificar(@PathVariable BigDecimal identificador, GczGrupoUsuario dato,
			BindingResult bindingResult, Model model, RedirectAttributes attr) {

		if (dato.getGczUsuarios().isEmpty()) {
			String[] valores = bindingResult.getFieldValue("gczUsuarios").toString().split(",");
			for (String val : valores) {
				try{
					dato.getGczUsuarios().add(new GczUsuario(new BigDecimal(val.trim())));
				} catch (Exception e) {
					
				}
			}
		}
		
		ResponseEntity<?> resultado = apiModificar(identificador, dato);
		
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro modificado correctamente"));
			attr.addFlashAttribute(ModelAttr.REGISTRO, resultado);
			attr.addFlashAttribute(ModelAttr.DATO, resultado.getBody());
			return "redirect:/" + MAPPING + "/" + identificador + "/edit";
		} else {
			model.addAttribute(ModelAttr.MENSAJE, resultado.getBody());
			model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(dato));
			model.addAttribute(ModelAttr.DATO, dato);
			return MAPPING_FORM;
		}
	}
	
	/**
	 * Lock.
	 *
	 * @param identificador Identificador
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.PUB)
	@RequestMapping(value = "/{identificador}/lock", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String lock(@PathVariable BigDecimal identificador, Model model, RedirectAttributes attr) {
		long resultado = dao.updateVisible(identificador, "N");		
		if (resultado > 0) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "" + resultado + " Registro desbloqueado correctamente."));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha desbloqueado el registro."));
		}
		return "redirect:/" + MAPPING + "/";
	}
	
	/**
	 * Unlock.
	 *
	 * @param identificador Identificador
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.PUB)
	@RequestMapping(value = "/{identificador}/unlock", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String unlock(@PathVariable BigDecimal identificador, Model model, RedirectAttributes attr) {
		long resultado = dao.updateVisible(identificador, "S");		
		if (resultado > 0) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "" + resultado + " Registro desbloqueado correctamente."));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha desbloqueado el registro."));
		}
		return "redirect:/" + MAPPING + "/";
	}
	
	/**
	 * Eliminar.
	 *
	 * @param id Id
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.DEL)
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String eliminar(@PathVariable BigDecimal id, Model model, RedirectAttributes attr) {
		ResponseEntity<?> resultado = apiDelete(id);
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro eliminado correctamente"));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, resultado.getBody());
		}		
		return "redirect:/" + MAPPING + "/";
	}
	
	/**
	 * Api delete.
	 *
	 * @param id Id
	 * @return response entity
	 */
	@RequestMapping(value = "/{id}/remove", method = RequestMethod.DELETE, produces = { MimeTypes.JSON,
			MimeTypes.XML })
	@Permisos(Permisos.DEL)
	@Description("Eliminar registro")
	@ResponseClass(value = Mensaje.class)
	public @ResponseBody ResponseEntity<?> apiDelete(@PathVariable BigDecimal id) {
		if (dao.removeById(id)) {
			return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(),
    				"Registro eliminado"));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registro no eliminado");
		}
	}
	
	
	/**
	 * Admin por lideres.
	 *
	 * @param model Model
	 * @param request Request
	 * @return string
	 * @throws SinCredencialesDefinidas the sin credenciales definidas
	 */
	@NoCache
	@RequestMapping(value = "/admin/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String adminPorLideres(Model model, HttpServletRequest request) throws SinCredencialesDefinidas {
		Credenciales credenciales = (Credenciales)request.getSession().getAttribute(CheckeoParametros.SESSIONGCZ);
		if (request.getSession().getAttribute(CheckeoParametros.SESSIONGCZ) != null) {
			model.addAttribute("ocultarFormulario", true);
			model.addAttribute(ModelAttr.RESULTADO, dao.obtenerGruposAsociadosPersona(credenciales));
			return MAPPING + "/index";
		} else {
			throw new SinCredencialesDefinidas();
		}
	}
	
	/**
	 * Edits the por lider.
	 *
	 * @param identificador Identificador
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @param request Request
	 * @return string
	 * @throws SinPermisoParaEjecutar the sin permiso para ejecutar
	 */
	@NoCache
	@RequestMapping(value = "/admin/{identificador}/edit", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String editPorLider(@PathVariable BigDecimal identificador, GczGrupoUsuario dato,
			BindingResult bindingResult, Model model, HttpServletRequest request) throws SinPermisoParaEjecutar {
		
		if (LiderUtils.puedeGestionarRecurso(GczGrupoUsuario.class.getName(), 
				(Credenciales)request.getSession().getAttribute(CheckeoParametros.SESSIONGCZ), 
				identificador)) {
			GczGrupoUsuario reg = dao.find(identificador);
			ResponseEntity<?> registro;
			if (reg == null) {
				registro = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale())));
			} else {
				registro = ResponseEntity.ok(reg);
			}
			model.addAttribute(ModelAttr.DATO, registro.getBody());
			model.addAttribute(ModelAttr.REGISTRO, registro);
			return MAPPING_FORM_LIDER;
		} else {
			throw new SinPermisoParaEjecutar();
		}
	}
	
	/**
	 * Modificar por lider.
	 *
	 * @param identificador Identificador
	 * @param login Login
	 * @param model Model
	 * @param attr Attr
	 * @param request Request
	 * @return string
	 * @throws SinPermisoParaEjecutar the sin permiso para ejecutar
	 */
	@RequestMapping(value = "/admin/{identificador}/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String modificarPorLider(@PathVariable BigDecimal identificador, 
			@RequestParam(name = "login", required = true) String login,
			Model model, RedirectAttributes attr, HttpServletRequest request) throws SinPermisoParaEjecutar {
		if (LiderUtils.puedeGestionarRecurso(GczGrupoUsuario.class.getName(), 
				(Credenciales)request.getSession().getAttribute(CheckeoParametros.SESSIONGCZ), 
				identificador)) {
			Credenciales user = daoUsuario.getCredenciales(login);
			if (StringUtils.isNotEmpty(user.getUsuario().getLogin())) {
				Mensaje m = dao.asociarUserAGroup(identificador, user.getUsuario().getId());
				attr.addFlashAttribute(ModelAttr.MENSAJE, m);
			} else {
				attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "Login no encontrado"));
			}
			
			return "redirect:/" + MAPPING + "/admin/" + identificador + "/edit";
			
		} else {
			throw new SinPermisoParaEjecutar();
		}
	}

	/**
	 * Eliminar por lider.
	 *
	 * @param id Id
	 * @param userid Userid
	 * @param model Model
	 * @param attr Attr
	 * @param request Request
	 * @return string
	 * @throws SinPermisoParaEjecutar the sin permiso para ejecutar
	 */
	@RequestMapping(value = "/admin/{id}/user/{userid}/remove", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String eliminarPorLider(@PathVariable BigDecimal id, @PathVariable BigDecimal userid, Model model, 
			RedirectAttributes attr, HttpServletRequest request) throws SinPermisoParaEjecutar {
		if (LiderUtils.puedeGestionarRecurso(GczGrupoUsuario.class.getName(), 
				(Credenciales)request.getSession().getAttribute(CheckeoParametros.SESSIONGCZ), 
				id)) {
			
			attr.addFlashAttribute(ModelAttr.MENSAJE, dao.eliminarDeGrupo(id, userid));
					
			return "redirect:/" + MAPPING + "/admin/" + id + "/edit";
		} else {
			throw new SinPermisoParaEjecutar();
		}
	}
}
