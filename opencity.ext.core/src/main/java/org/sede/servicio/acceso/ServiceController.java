package org.sede.servicio.acceso;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;

import javax.validation.ConstraintViolation;

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
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.MimeTypes;
import org.sede.core.utils.Funciones;
import org.sede.servicio.ModelAttr;
import org.sede.servicio.acceso.dao.GczPerfilGenericDAO;
import org.sede.servicio.acceso.dao.GczServicioGenericDAO;
import org.sede.servicio.acceso.entity.GczGrupoOperaciones;
import org.sede.servicio.acceso.entity.GczGrupoUsuario;
import org.sede.servicio.acceso.entity.GczPerfil;
import org.sede.servicio.acceso.entity.GczServicio;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.googlecode.genericdao.search.SearchResult;

/**
 * Class ServiceController.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
@Gcz(servicio="ADMIN",seccion="ADMIN")
@Transactional(ConfigAcceso.TM)
@Controller
@RequestMapping(value = "/" + ServiceController.MAPPING, method = RequestMethod.GET)
@PlantillaHTML(CredencialesController.MAPPING)
public class ServiceController {
	
	/** Constant RAIZ. */
	private static final String RAIZ = "credenciales/";
	
	/** Constant SERVICIO. */
	private static final String SERVICIO = "service";
	
	/** Constant MAPPING. */
	public static final String MAPPING = "servicio/" + RAIZ + SERVICIO;
	
	/** Constant MAPPING_FORM. */
	private static final String MAPPING_FORM = MAPPING + "/formulario";
	
	/** Constant MAPPING_FORM_PERFIL. */
	private static final String MAPPING_FORM_PERFIL = MAPPING + "/formulario-perfil";
	
	/** Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ServiceController.class);

	/** message source. */
	@Autowired
	private MessageSource messageSource;
	
	/** dao. */
	@Autowired
	GczServicioGenericDAO dao;
	
	/** dao perfil. */
	@Autowired
	GczPerfilGenericDAO daoPerfil;
	
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
	@ResponseClass(GczServicio.class)
	@RequestMapping(value = "/{identificador}", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiDetalle(@PathVariable String identificador) {
		GczServicio registro = dao.find(identificador);
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
	@ResponseClass(value = GczServicio.class)
	@RequestMapping(value = "/{identificador}", method = RequestMethod.PUT, consumes = {
			MimeTypes.JSON, MimeTypes.XML }, produces = { MimeTypes.JSON,
			MimeTypes.XML })
	@Permisos(Permisos.MOD)
	public @ResponseBody ResponseEntity<?> apiModificar(@PathVariable String identificador,
			@RequestBody GczServicio registro) {
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
	 * Api crear.
	 *
	 * @param registro Registro
	 * @return response entity
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
	@Permisos(Permisos.NEW)
	@Description("Crear registro")
	@ResponseClass(value = GczServicio.class)
	public @ResponseBody ResponseEntity<?> apiCrear(
			@RequestBody GczServicio registro) {
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
	public String newForm(GczServicio dato, BindingResult bindingResult,
			Model model) {
		model.addAttribute(ModelAttr.DATO, dato);
		model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(new GczServicio()));
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
	public String crear(GczServicio dato, BindingResult bindingResult, Model model, RedirectAttributes attr) {
		ResponseEntity<?> resultado = apiCrear(dato);
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro creado correctamente"));
			attr.addFlashAttribute(ModelAttr.REGISTRO, resultado);
			attr.addFlashAttribute(ModelAttr.DATO, resultado.getBody());
			return "redirect:/" + MAPPING + "/" + ((GczServicio)resultado.getBody()).getId() + "/edit";
		} else {
			model.addAttribute(ModelAttr.MENSAJE, resultado.getBody());
			model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(dato));
			model.addAttribute(ModelAttr.DATO, dato);
			return MAPPING_FORM;
		}
	}
	
	/**
	 * Api list user.
	 *
	 * @param search Search
	 * @return response entity
	 * @throws SearchParseException the search parse exception
	 */
	@NoCache
	@Permisos(Permisos.DET)
	@ResponseClass(value = GczServicio.class, entity = SearchResult.class)
	@RequestMapping(method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiListUser(
			@Fiql SearchFiql search
    		) throws SearchParseException  {
		return ResponseEntity.ok(dao.searchAndCount(search.getConditions(GczServicio.class)));
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
			@ModelAttribute GczServicio dato,
			@Fiql SearchFiql search) throws SearchParseException {
		model.addAttribute(ModelAttr.RESULTADO, apiListUser(search));
		model.addAttribute(ModelAttr.DATO, dato);
		return MAPPING + "/index";
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
	public String edit(@PathVariable String identificador, GczServicio dato,
			BindingResult bindingResult, Model model) {
		GczServicio reg = dao.find(identificador);
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
	public String modificar(@PathVariable String identificador, GczServicio dato,
			BindingResult bindingResult, Model model, RedirectAttributes attr) {

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
	public String lock(@PathVariable String identificador, Model model, RedirectAttributes attr) {
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
	public String unlock(@PathVariable String identificador, Model model, RedirectAttributes attr) {
		long resultado = dao.updateVisible(identificador, "S");		
		if (resultado > 0) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "" + resultado + " Registro desbloqueado correctamente."));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha desbloqueado el registro."));
		}
		return "redirect:/" + MAPPING + "/";
	}
	
	
	/**
	 * Api crear perfil.
	 *
	 * @param servicio Servicio
	 * @param registro Registro
	 * @return response entity
	 */
	@RequestMapping(value = "/{servicio}/profile", method = RequestMethod.POST, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
	@Permisos(Permisos.NEW)
	@Description("Crear registro")
	@ResponseClass(value = GczPerfil.class)
	public @ResponseBody ResponseEntity<?> apiCrearPerfil(
			@PathVariable String servicio,
			@RequestBody GczPerfil registro) {
		registro.setGczServicio(new GczServicio(servicio));
		Set<ConstraintViolation<Object>> errores = dao.validar(registro);
		if (!errores.isEmpty()) {
			return Funciones.generarMensajeError(errores);
		} else {
			registro.setVisible("S");
			registro.setUsuarioAlta(Funciones.getPeticion().getClientId());
			registro.setCreationDate(new Date());
			daoPerfil.save(registro);
			daoPerfil.flush();
			return ResponseEntity.ok(registro);
		}
	}
	
	/**
	 * New form.
	 *
	 * @param servicio Servicio
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.NEW)
	@RequestMapping(value = "/{servicio}/profile/new", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String newForm(@PathVariable String servicio, GczPerfil dato, BindingResult bindingResult,
			Model model) {
		dato.setGczServicio(new GczServicio(servicio));
		model.addAttribute(ModelAttr.DATO, dato);
		model.addAttribute("servicio", apiDetalle(servicio));		
		model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(new GczPerfil()));
		return MAPPING_FORM_PERFIL;
	}
	
	/**
	 * Crear.
	 *
	 * @param servicio Servicio
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.NEW)
	@RequestMapping(value = "/{servicio}/profile/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String crear(@PathVariable String servicio, GczPerfil dato, BindingResult bindingResult, Model model, RedirectAttributes attr) {
		ResponseEntity<?> resultado = apiCrearPerfil(servicio, dato);
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro creado correctamente"));
			attr.addFlashAttribute(ModelAttr.REGISTRO, resultado);
			model.addAttribute("servicio", apiDetalle(servicio));
			attr.addFlashAttribute(ModelAttr.DATO, resultado.getBody());
			return "redirect:/" + MAPPING + "/" + ((GczPerfil)resultado.getBody()).getGczServicio().getId() 
					+ "/profile/" + ((GczPerfil)resultado.getBody()).getId() + "/edit";
		} else {
			model.addAttribute(ModelAttr.MENSAJE, resultado.getBody());
			model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(dato));
			model.addAttribute("servicio", apiDetalle(servicio));
			model.addAttribute(ModelAttr.DATO, dato);
			return MAPPING_FORM_PERFIL;
		}
	}
	
	
	/**
	 * Api detalle perfil.
	 *
	 * @param servicio Servicio
	 * @param identificador Identificador
	 * @return response entity
	 */
	@Permisos(Permisos.DET)
	@Cache(Cache.DURACION_30MIN)
	@ResponseClass(GczPerfil.class)
	@RequestMapping(value = "/{servicio}/profile/{identificador}", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiDetallePerfil(@PathVariable String servicio, @PathVariable BigDecimal identificador) {
		GczPerfil registro = daoPerfil.find(identificador);
		if (registro == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale())));
		} else {
			return ResponseEntity.ok(registro);
		}
	}
	
	/**
	 * Api modificar perfil.
	 *
	 * @param servicio Servicio
	 * @param idPerfil Id perfil
	 * @param registro Registro
	 * @return response entity
	 */
	@ResponseClass(value = GczPerfil.class)
	@RequestMapping(value = "/{servicio}/profile/{idPerfil}", method = RequestMethod.PUT, consumes = {
			MimeTypes.JSON, MimeTypes.XML }, produces = { MimeTypes.JSON,
			MimeTypes.XML })
	@Permisos(Permisos.MOD)
	public @ResponseBody ResponseEntity<?> apiModificarPerfil(@PathVariable String servicio, @PathVariable BigDecimal idPerfil,
			@RequestBody GczPerfil registro) {
		
		ResponseEntity<?> resp = apiDetallePerfil(servicio, idPerfil);
		if (resp.getStatusCode().is2xxSuccessful()) {
			registro.setGczServicio(new GczServicio());
			registro.getGczServicio().setId(servicio);
			registro.setId(idPerfil);
			Set<ConstraintViolation<Object>> errores = daoPerfil.validar(registro);
			if (!errores.isEmpty()) {
				return Funciones.generarMensajeError(errores);
			} else {
				daoPerfil.merge(registro);
				daoPerfil.flush();
			}
			// devolvemos los datos del servicio
			return apiDetallePerfil(servicio, idPerfil);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Mensaje) resp.getBody());
		}
	}
	
	/**
	 * Edits the perfil.
	 *
	 * @param servicio Servicio
	 * @param idPerfil Id perfil
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@NoCache
	@RequestMapping(value = "/{servicio}/profile/{idPerfil}/edit", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String editPerfil(@PathVariable String servicio, @PathVariable BigDecimal idPerfil, GczPerfil dato,
			BindingResult bindingResult, Model model) {
		GczPerfil reg = daoPerfil.find(idPerfil);
		ResponseEntity<?> registro;
		if (reg == null) {
			registro = ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale())));
		} else {
			registro = ResponseEntity.ok(reg);
		}
		model.addAttribute(ModelAttr.DATO, registro.getBody());
		model.addAttribute("servicio", apiDetalle(servicio));
		model.addAttribute(ModelAttr.REGISTRO, registro);
		return MAPPING_FORM_PERFIL;
	}
	
	/**
	 * Modificar perfil.
	 *
	 * @param servicio Servicio
	 * @param idPerfil Id perfil
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@RequestMapping(value = "/{servicio}/profile/{idPerfil}/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String modificarPerfil(@PathVariable String servicio, @PathVariable BigDecimal idPerfil, GczPerfil dato,
			BindingResult bindingResult, Model model, RedirectAttributes attr) {

		if (dato.getGczUsuarios().isEmpty()) {
			String[] valores = bindingResult.getFieldValue("gczUsuarios").toString().split(",");
			if (valores.length > 0) {
				for (String val : valores) {
					try {
						dato.getGczUsuarios().add(new GczUsuario(new BigDecimal(val.trim())));
					} catch (Exception e) {
						
					}
				}
			}
		}
		if (dato.getGczGrupoUsuarios().isEmpty()) {
			String[] valores = bindingResult.getFieldValue("gczGrupoUsuarios").toString().split(",");
			if (valores.length > 0) {
				for (String val : valores) {
					try {
						dato.getGczGrupoUsuarios().add(new GczGrupoUsuario(new BigDecimal(val.trim())));
					} catch (Exception e) {
						
					}
				}
			}
		}
		if (dato.getGczGrupoOperacioneses().isEmpty()) {
			String[] valores = bindingResult.getFieldValue("gczGrupoOperacioneses").toString().split(",");
			if (valores.length > 0) {
				for (String val : valores) {
					try {
						dato.getGczGrupoOperacioneses().add(new GczGrupoOperaciones(Long.parseLong(val.trim())));
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
				}
			}
		}
		ResponseEntity<?> resultado = apiModificarPerfil(servicio, idPerfil, dato);
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro modificado correctamente"));
			attr.addFlashAttribute(ModelAttr.REGISTRO, resultado);
			attr.addFlashAttribute(ModelAttr.DATO, resultado.getBody());
			return "redirect:/" + MAPPING + "/" + servicio + "/profile/" + idPerfil + "/edit";
		} else {
			model.addAttribute(ModelAttr.MENSAJE, resultado.getBody());
			model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(dato));
			model.addAttribute(ModelAttr.DATO, dato);
			return MAPPING_FORM_PERFIL;
		}
	}
	
	/**
	 * Lock perfil.
	 *
	 * @param servicio Servicio
	 * @param idPerfil Id perfil
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.PUB)
	@RequestMapping(value = "/{servicio}/profile/{idPerfil}/lock", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String lockPerfil(@PathVariable String servicio, @PathVariable BigDecimal idPerfil, Model model, RedirectAttributes attr) {
		long resultado = daoPerfil.updateVisible(servicio, idPerfil, "N");		
		if (resultado > 0) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "" + resultado + " Registro desbloqueado correctamente."));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha desbloqueado el registro."));
		}
		return "redirect:/" + MAPPING + "/" + servicio + "/edit";
	}
	
	/**
	 * Unlock perfil.
	 *
	 * @param servicio Servicio
	 * @param idPerfil Id perfil
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.PUB)
	@RequestMapping(value = "/{servicio}/profile/{idPerfil}/unlock", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String unlockPerfil(@PathVariable String servicio, @PathVariable BigDecimal idPerfil, Model model, RedirectAttributes attr) {
		long resultado = daoPerfil.updateVisible(servicio, idPerfil, "S");		
		if (resultado > 0) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "" + resultado + " Registro desbloqueado correctamente."));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha desbloqueado el registro."));
		}
		return "redirect:/" + MAPPING + "/" + servicio + "/edit";
	}
}
