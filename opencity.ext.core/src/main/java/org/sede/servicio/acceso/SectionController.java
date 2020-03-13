package org.sede.servicio.acceso;


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
import org.sede.servicio.acceso.dao.GczSectionGenericDAO;
import org.sede.servicio.acceso.entity.GczSeccion;
import org.sede.servicio.acceso.entity.GczSeccionId;
import org.sede.servicio.acceso.entity.GczServicio;
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
 * Class SectionController.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 * 
 */
@Gcz(servicio="ADMIN",seccion="ADMIN")
@Transactional(Constants.TM)
@Controller
@RequestMapping(value = "/" + SectionController.MAPPING, method = RequestMethod.GET)
@PlantillaHTML(CredencialesController.MAPPING)
public class SectionController {
	
	/** Constant RAIZ. */
	private static final String RAIZ = "credenciales/";
	
	/** Constant SERVICIO. */
	private static final String SERVICIO = "section";
	
	/** Constant MAPPING. */
	public static final String MAPPING = "servicio/" + RAIZ + SERVICIO;
	
	/** Constant MAPPING_FORM. */
	private static final String MAPPING_FORM = MAPPING + "/formulario";
	
	/** message source. */
	@Autowired
	private MessageSource messageSource;
	
	/** dao. */
	@Autowired
	GczSectionGenericDAO dao;
	
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
	 * @param seccion Seccion
	 * @param servicio Servicio
	 * @return response entity
	 */
	@Permisos(Permisos.DET)
	@Cache(Cache.DURACION_30MIN)
	@ResponseClass(GczSeccion.class)
	@RequestMapping(value = "/{seccion}/{servicio}", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiDetalle(@PathVariable String seccion, @PathVariable String servicio) {
		GczSeccion registro = dao.find(new GczSeccionId(seccion, servicio));
		if (registro == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale())));
		} else {
			return ResponseEntity.ok(registro);
		}
	}
	
	
	/**
	 * Api crear.
	 *
	 * @param servicio Servicio
	 * @param registro Registro
	 * @return response entity
	 */
	@RequestMapping(value = "/{servicio}", method = RequestMethod.POST, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
	@Permisos(Permisos.NEW)
	@Description("Crear registro")
	@ResponseClass(value = GczSeccion.class)
	public @ResponseBody ResponseEntity<?> apiCrear(
			@PathVariable String servicio,
			@RequestBody GczSeccion registro) {
		registro.setGczServicio(new GczServicio(servicio));
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
	 * @param servicio Servicio
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.NEW)
	@RequestMapping(value = "/{servicio}/new", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String newForm(@PathVariable String servicio, GczSeccion dato, BindingResult bindingResult,
			Model model) {
		dato.setGczServicio(new GczServicio(servicio));
		model.addAttribute(ModelAttr.DATO, dato);
		model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(new GczSeccion()));
		return MAPPING_FORM;
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
	@RequestMapping(value = "/{servicio}/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String crear(@PathVariable String servicio,  
			GczSeccion dato, BindingResult bindingResult, Model model, RedirectAttributes attr) {
		ResponseEntity<?> resultado = apiCrear(servicio, dato);
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro creado correctamente"));
			attr.addFlashAttribute(ModelAttr.REGISTRO, resultado);
			attr.addFlashAttribute(ModelAttr.DATO, resultado.getBody());
			return "redirect:/" + MAPPING + "/" + ((GczSeccion)resultado.getBody()).getId().getCodigoSeccion() 
					+ "/" + ((GczSeccion)resultado.getBody()).getId().getCodigoServicio() + "/edit";
		} else {
			model.addAttribute(ModelAttr.MENSAJE, resultado.getBody());
			model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(dato));
			model.addAttribute(ModelAttr.DATO, dato);
			return MAPPING_FORM;
		}
	}
	
	
	/**
	 * Api modificar.
	 *
	 * @param seccion Seccion
	 * @param servicio Servicio
	 * @param registro Registro
	 * @return response entity
	 */
	@ResponseClass(value = GczSeccion.class)
	@RequestMapping(value = "/{seccion}/{servicio}", method = RequestMethod.PUT, consumes = {
			MimeTypes.JSON, MimeTypes.XML }, produces = { MimeTypes.JSON,
			MimeTypes.XML })
	@Permisos(Permisos.MOD)
	public @ResponseBody ResponseEntity<?> apiModificar(@PathVariable String seccion, @PathVariable String servicio,
			@RequestBody GczSeccion registro) {
		ResponseEntity<?> resp = apiDetalle(seccion, servicio);
		
		if (resp.getStatusCode().is2xxSuccessful()) {
			registro.setId(new GczSeccionId(seccion, servicio));
			Set<ConstraintViolation<Object>> errores = dao.validar(registro);
			if (!errores.isEmpty()) {
				return Funciones.generarMensajeError(errores);
			} else {
				registro.setLastUpdated(new Date());
				registro.setUsuarioMod(Funciones.getPeticion().getClientId());
				dao.merge(registro);
				dao.flush();
			}
			return apiDetalle(seccion, servicio);
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
	@ResponseClass(value = GczSeccion.class, entity = SearchResult.class)
	@RequestMapping(method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiListUser(
			@Fiql SearchFiql search
    		) throws SearchParseException {
		return ResponseEntity.ok(dao.searchAndCount(search.getConditions(GczSeccion.class)));
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
			@ModelAttribute GczSeccion dato,
			@Fiql SearchFiql search) throws SearchParseException {
		model.addAttribute(ModelAttr.RESULTADO, apiListUser(search));
		model.addAttribute(ModelAttr.DATO, dato);
		return MAPPING + "/index";
	}
	
	/**
	 * Edits the.
	 *
	 * @param seccion Seccion
	 * @param servicio Servicio
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@NoCache
	@RequestMapping(value = "/{seccion}/{servicio}/edit", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String edit(@PathVariable String seccion, @PathVariable String servicio, GczSeccion dato,
			BindingResult bindingResult, Model model) {
		ResponseEntity<?> registro = apiDetalle(seccion, servicio);
		model.addAttribute(ModelAttr.DATO, registro.getBody());
		model.addAttribute(ModelAttr.REGISTRO, registro);
		return MAPPING_FORM;
	}
	
	/**
	 * Modificar.
	 *
	 * @param seccion Seccion
	 * @param servicio Servicio
	 * @param dato Dato
	 * @param bindingResult Binding result
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@RequestMapping(value = "/{seccion}/{servicio}/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String modificar(@PathVariable String seccion, @PathVariable String servicio, GczSeccion dato,
			BindingResult bindingResult, Model model, RedirectAttributes attr) {
		ResponseEntity<?> resultado = apiModificar(seccion, servicio, dato);
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro modificado correctamente"));
			attr.addFlashAttribute(ModelAttr.REGISTRO, resultado);
			attr.addFlashAttribute(ModelAttr.DATO, resultado.getBody());
			return "redirect:/" + MAPPING + "/" + seccion + "/" + servicio + "/edit";
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
	 * @param seccion Seccion
	 * @param servicio Servicio
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.PUB)
	@RequestMapping(value = "/{seccion}/{servicio}/lock", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String lock(@PathVariable String seccion, @PathVariable String servicio, Model model, RedirectAttributes attr) {
		long resultado = dao.updateVisible(seccion, servicio, "N");		
		if (resultado > 0) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "" + resultado + " Registro desbloqueado correctamente."));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha desbloqueado el registro."));
		}
		return "redirect:/" + ServiceController.MAPPING + "/" + servicio + "/edit";
	}
	
	/**
	 * Unlock.
	 *
	 * @param seccion Seccion
	 * @param servicio Servicio
	 * @param model Model
	 * @param attr Attr
	 * @return string
	 */
	@Permisos(Permisos.PUB)
	@RequestMapping(value = "/{seccion}/{servicio}/unlock", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String unlock(@PathVariable String seccion, @PathVariable String servicio, Model model, RedirectAttributes attr) {
		long resultado = dao.updateVisible(seccion, servicio, "S");		
		if (resultado > 0) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "" + resultado + " Registro desbloqueado correctamente."));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha desbloqueado el registro."));
		}
		return "redirect:/" + ServiceController.MAPPING + "/" + servicio + "/edit";
	}
}
