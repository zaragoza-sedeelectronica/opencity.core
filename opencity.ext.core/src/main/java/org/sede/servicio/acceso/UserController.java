package org.sede.servicio.acceso;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.apache.cxf.jaxrs.ext.search.SearchParseException;
import org.sede.core.anotaciones.Cache;
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
import org.sede.servicio.acceso.dao.GczUsuarioGenericDAO;
import org.sede.servicio.acceso.dao.GczUsuarioGenericDAOImpl;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.GczGrupoUsuario;
import org.sede.servicio.acceso.entity.GczUsuario;
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

// TODO: Auto-generated Javadoc
/**
 * Class UserController.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 * 
 */
@Gcz(servicio="ADMIN",seccion="ADMIN")
@Transactional(Constants.TM)
@Controller
@RequestMapping(value = "/" + UserController.MAPPING, method = RequestMethod.GET)
@PlantillaHTML(CredencialesController.MAPPING)
public class UserController {
	
	/** Constant RAIZ. */
	private static final String RAIZ = "credenciales/";
	
	/** Constant SERVICIO. */
	private static final String SERVICIO = "user";
	
	/** Constant MAPPING. */
	public static final String MAPPING = "servicio/" + RAIZ + SERVICIO;
	
	/** Constant MAPPING_FORM. */
	private static final String MAPPING_FORM = MAPPING + "/formulario";

	/** message source. */
	@Autowired
	private MessageSource messageSource;
	
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
	 * Api detalle.
	 *
	 * @param identificador Identificador
	 * @return response entity
	 */
	@Permisos(Permisos.DET)
	@Cache(Cache.DURACION_30MIN)
	@ResponseClass(GczUsuario.class)
	@RequestMapping(value = "/{identificador}", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiDetalle(@PathVariable BigDecimal identificador) {
		GczUsuario registro = dao.find(identificador);
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
	@ResponseClass(value = GczUsuario.class)
	@RequestMapping(value = "/{identificador}", method = RequestMethod.PUT, consumes = {
			MimeTypes.JSON, MimeTypes.XML }, produces = { MimeTypes.JSON,
			MimeTypes.XML })
	@Permisos(Permisos.MOD)
	public @ResponseBody ResponseEntity<?> apiModificar(@PathVariable BigDecimal identificador,
			@RequestBody GczUsuario registro) {
		ResponseEntity<?> resp = apiDetalle(identificador);
		if (resp.getStatusCode().is2xxSuccessful()) {
			registro.setId(identificador);
			Set<ConstraintViolation<Object>> errores = dao.validar(registro);
			if (!errores.isEmpty()) {
				return Funciones.generarMensajeError(errores);
			} else {
				// reestablecemos los perfiles para que no se eliminen
				registro.setGczPerfils(((GczUsuario)resp.getBody()).getGczPerfils());
				dao.save(registro);
				dao.flush();
			}
			return apiDetalle(identificador);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Mensaje) resp.getBody());
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
	@ResponseClass(value = GczUsuario.class, entity = SearchResult.class)
	@RequestMapping(method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiListUser(
			@Fiql SearchFiql search) throws SearchParseException  {
		return ResponseEntity.ok(dao.searchAndCount(search.getConditions(GczUsuario.class)));
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
			@ModelAttribute GczUsuario dato,
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
	public String edit(@PathVariable BigDecimal identificador, GczUsuario dato,
			BindingResult bindingResult, Model model) {
		GczUsuario reg = dao.find(identificador);
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
	 * Profile.
	 *
	 * @param identificador Identificador
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@NoCache
	@RequestMapping(value = "/{identificador}/profile", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String profile(@PathVariable BigDecimal identificador, Model model) {
		GczUsuario usr = (GczUsuario)apiDetalle(identificador).getBody();
		Credenciales credenciales = dao.getCredenciales(usr.getLogin(), GczUsuarioGenericDAOImpl.SAFE);
		model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(credenciales));
		return MAPPING + "/profile";
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
	public String modificar(@PathVariable BigDecimal identificador, GczUsuario dato,
			BindingResult bindingResult, Model model, RedirectAttributes attr) {
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
	 * Newpass.
	 *
	 * @param identificador Identificador
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@RequestMapping(value = "/{identificador}/newpass", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String newpass(@PathVariable BigDecimal identificador, Model model, RedirectAttributes attr) {
		attr.addFlashAttribute(ModelAttr.MENSAJE, dao.newPassword((GczUsuario)apiDetalle(identificador).getBody()));		
		return "redirect:/" + MAPPING + "/";
	}
	
}
