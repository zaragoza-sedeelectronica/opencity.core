package org.sede.servicio.contenido;


import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import org.sede.core.anotaciones.Cache;
import org.sede.core.anotaciones.Description;
import org.sede.core.anotaciones.Esquema;
import org.sede.core.anotaciones.Fiql;
import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.NoCache;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.ResponseClass;
import org.sede.core.anotaciones.TestValue;
import org.sede.core.dao.EntidadBase;
import org.sede.core.dao.SearchFiql;
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.MimeTypes;
import org.sede.core.utils.Funciones;
import org.sede.servicio.ModelAttr;
import org.sede.servicio.contenido.dao.IndexErrorGenericDAO;
import org.sede.servicio.contenido.entity.IndexError;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;

@Gcz(servicio="CONTENIDOS",seccion="PAGINAS")
@Transactional(Esquema.TMINTRA)
@Controller
@RequestMapping(value = "/" + IndexErrorController.MAPPING, method = RequestMethod.GET)
public class IndexErrorController {
	private static final String SERVICIO = "index-error";
	public static final String MAPPING = "servicio/contenido/" + SERVICIO;
	private static final String MAPPING_FORM = MAPPING + "/formulario";
	private static final Logger logger = LoggerFactory.getLogger(IndexErrorController.class);

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	public IndexErrorGenericDAO dao;
	

	@Cache(Cache.DURACION_30MIN)
	@Permisos(Permisos.DET)
	@ResponseClass(value = IndexError.class, entity = SearchResult.class)
	@RequestMapping(method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiListar(@Fiql SearchFiql search) throws org.apache.cxf.jaxrs.ext.search.SearchParseException{
		Search busqueda = search.getConditions(IndexError.class);
		busqueda.addFilterEqual("revisado", "N");
		return ResponseEntity.ok(dao.searchAndCount(busqueda));
	}

	@Permisos(Permisos.DET)
	@Cache(Cache.DURACION_30MIN)
	@ResponseClass(IndexError.class)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiDetalle(@PathVariable @TestValue("160") String id)
			{
		IndexError registro = dao.find(id);
		if (registro == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Mensaje(HttpStatus.NOT_FOUND.value(), messageSource.getMessage("generic.notfound", null, LocaleContextHolder.getLocale())));
		} else {
			return ResponseEntity.ok(registro);
		}
	}
    @RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String redirect() {
		return "redirect:" + SERVICIO + "/";
	}

    @Permisos(Permisos.DET)
	@RequestMapping(path = "/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String home(Model model, @Fiql SearchFiql search) throws org.apache.cxf.jaxrs.ext.search.SearchParseException {

		model.addAttribute(ModelAttr.RESULTADO, apiListar(search));
		model.addAttribute("estadistica", dao.getStatistics());
		model.addAttribute("estadisticaCodes", dao.getStatisticsByCode());
		return MAPPING + "/index";
	}
    
    @Permisos(Permisos.DET)
	@RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String detalle(@PathVariable String id, Model model,
						  HttpServletRequest request) {
		model.addAttribute(ModelAttr.REGISTRO, apiDetalle(id));
		return MAPPING + "/detalle";
	}
	
	@RequestMapping(method = RequestMethod.POST, consumes = { MimeTypes.JSON,
			MimeTypes.XML }, produces = { MimeTypes.JSON, MimeTypes.XML })
	@Permisos(Permisos.NEW)
	@Description("Crear registro")
	@ResponseClass(value = IndexError.class)
	public @ResponseBody ResponseEntity<?> apiCrear(
			@RequestBody IndexError registro) {
		Set<ConstraintViolation<Object>> errores = dao.validar(registro);
		if (!errores.isEmpty()) {
			return Funciones.generarMensajeError(errores);
		}
		registro.setRevisado("N");
		dao.save(registro);
		return ResponseEntity.ok(registro);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT, consumes = {
			MimeTypes.JSON, MimeTypes.XML }, produces = { MimeTypes.JSON,
			MimeTypes.XML })
	@Permisos(Permisos.MOD)
	@Description("Modificar registro")
	@ResponseClass(value = IndexError.class)
	public @ResponseBody ResponseEntity<?> apiModificar(@PathVariable String id,
													 @RequestBody IndexError registro) {
		ResponseEntity<?> resp = apiDetalle(id);
		if (resp.getStatusCode().is2xxSuccessful()) {
			IndexError reg = (IndexError) resp.getBody();
			EntidadBase.combinar(reg, registro);
			reg.setId(id);
			Set<ConstraintViolation<Object>> errores = dao.validar(registro);
			if (!errores.isEmpty()) {
				return Funciones.generarMensajeError(errores);
			} else {
				dao.save(reg);
			}
			return ResponseEntity.ok(reg);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((Mensaje) resp.getBody());
		}
	}

	@RequestMapping(value = "/{id}/remove", method = RequestMethod.DELETE, produces = { MimeTypes.JSON,
			MimeTypes.XML })
	@Permisos(Permisos.DEL)
	@Description("Eliminar registro")
	@ResponseClass(value = Mensaje.class)
	public @ResponseBody ResponseEntity<?> apiDelete(@PathVariable String id) {
		if (dao.updateStatusSinIndizar(id)) {
			return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(),
					"Registro eliminado"));
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registro no eliminado");
		}
	}


	@Permisos(Permisos.MOD)
	@NoCache
	@RequestMapping(value = "/{id}/edit", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String edit(@PathVariable String id, IndexError dato,
					   BindingResult bindingResult, Model model) {
		ResponseEntity<?> registro = apiDetalle(id);
		model.addAttribute(ModelAttr.DATO, registro.getBody());
		model.addAttribute(ModelAttr.REGISTRO, registro);
		return MAPPING_FORM;
	}

	
	@Permisos(Permisos.MOD)
	@RequestMapping(value = "/{id}/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String modificar(@PathVariable String id, IndexError dato,
							BindingResult bindingResult, Model model, RedirectAttributes attr) {
		ResponseEntity<?> resultado = apiModificar(id, dato);
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro modificado correctamente"));
			attr.addFlashAttribute(ModelAttr.REGISTRO, resultado);
			attr.addFlashAttribute(ModelAttr.DATO, resultado.getBody());
			return "redirect:/" + MAPPING + "/" + id + "/edit";
		} else {
			model.addAttribute(ModelAttr.MENSAJE, resultado.getBody());
			model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(dato));
			model.addAttribute(ModelAttr.DATO, dato);
			return MAPPING_FORM;
		}
	}

	@Permisos(Permisos.DEL)
	@RequestMapping(value = "/{id}/delete", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String eliminar(@PathVariable String id, Model model, RedirectAttributes attr) {
		ResponseEntity<?> resultado = apiDelete(id);
		if (resultado.getStatusCode().is2xxSuccessful()) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro eliminado correctamente"));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, resultado.getBody());
		}
		return "redirect:/" + MAPPING + "/";
	}
	
	@Permisos(Permisos.PUB)
	@RequestMapping(value = "/{identifier}/lock", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String lock(@PathVariable String identifier, Model model, RedirectAttributes attr) {
		long resultado = dao.updateRevisado(identifier, "S");		
		if (resultado > 0) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "" + resultado + " Registro marcado como revisado."));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha marcado el registro como revisado."));
		}
		return "redirect:/" + MAPPING + "/";
	}
	@Permisos(Permisos.PUB)
	@RequestMapping(value = "/{identifier}/unlock", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String unlock(@PathVariable String identifier, Model model, RedirectAttributes attr) {
		long resultado = dao.updateRevisado(identifier, "N");		
		if (resultado > 0) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "" + resultado + " Registro marcado como NO revisado."));
		} else {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha marcado el registro como NO revisado."));
		}
		return "redirect:/" + MAPPING + "/";
	}

}
