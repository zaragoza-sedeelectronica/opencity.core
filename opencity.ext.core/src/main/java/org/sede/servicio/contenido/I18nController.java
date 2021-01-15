package org.sede.servicio.contenido;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Properties;

import org.sede.core.anotaciones.Fiql;
import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.NoCache;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.dao.SearchFiql;
import org.sede.core.rest.Mensaje;
import org.sede.core.utils.Propiedades;
import org.sede.servicio.ModelAttr;
import org.sede.servicio.contenido.entity.Property;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Gcz(servicio="CONTENIDOS",seccion="I18N")
@Controller
@RequestMapping(value = "/" + I18nController.MAPPING, method = RequestMethod.GET)
public class I18nController {
	private static final String SERVICIO = "i18n";
	public static final String MAPPING = "servicio/" + SERVICIO;
	private static final String MAPPING_FORM = MAPPING + "/formulario";
	private static final Logger logger = LoggerFactory.getLogger(I18nController.class);

	@Autowired
	private MessageSource messageSource;
	
	
	@RequestMapping(method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String redirect() {
		return "redirect:" + SERVICIO + "/";
	}

	@Permisos(Permisos.DET)
	@RequestMapping(path = "/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String home(Model model, @Fiql SearchFiql search) throws org.apache.cxf.jaxrs.ext.search.SearchParseException, FileNotFoundException, IOException {
		
		String pathI18n = Propiedades.getPathi18n() + "_es.properties";
		final Properties props = new Properties();
		props.load(new InputStreamReader(new FileInputStream(pathI18n), Charset.forName("UTF-8")));
		
		model.addAttribute(ModelAttr.RESULTADO, props);

		return MAPPING + "/index";
	}
	
	
	@Permisos(Permisos.NEW)
	@RequestMapping(value = "/new", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String newForm(Property dato, BindingResult bindingResult,
						  Model model) {
		model.addAttribute(ModelAttr.DATO, dato);
		model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(new Property()));
		return MAPPING_FORM;
	}

	@Permisos(Permisos.MOD)
	@NoCache
	@RequestMapping(value = "/edit", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String edit(@RequestParam(name = "id", required = true) String id, Property dato,
					   BindingResult bindingResult, Model model) {
		
		Property p = new Property();
		p.setId(id);
		p.setEs(messageSource.getMessage(id, null, new Locale("es")));
		p.setEn(messageSource.getMessage(id, null, new Locale("en")));
		p.setIt(messageSource.getMessage(id, null, new Locale("it")));
		p.setFr(messageSource.getMessage(id, null, new Locale("fr")));
		
		
		ResponseEntity<?> registro = ResponseEntity.ok(p);
		model.addAttribute(ModelAttr.DATO, registro.getBody());
		model.addAttribute(ModelAttr.REGISTRO, registro);
		return MAPPING_FORM;
	}

	@Permisos(Permisos.MOD)
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String crear(Property dato, BindingResult bindingResult, Model model, RedirectAttributes attr) {
		ResponseEntity<?> resultadoEs = almacenar(dato, "es", dato.getEs());
		ResponseEntity<?> resultadoEn = almacenar(dato, "en", dato.getEn());
		ResponseEntity<?> resultadoFr = almacenar(dato, "fr", dato.getFr());
		ResponseEntity<?> resultadoIt = almacenar(dato, "it", dato.getIt());
		if (resultadoEs.getStatusCode().is2xxSuccessful()
				&& resultadoEn.getStatusCode().is2xxSuccessful()
				&& resultadoFr.getStatusCode().is2xxSuccessful()
				&& resultadoIt.getStatusCode().is2xxSuccessful()
				) {
			attr.addFlashAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Etiqueta almacenada correctamente, puede tardar algunos minutos en ser efectivo el cambio"));
			attr.addFlashAttribute(ModelAttr.REGISTRO, resultadoEs);
			return "redirect:/" + MAPPING + "/";
		} else {
			model.addAttribute(ModelAttr.MENSAJE, resultadoEs.getBody());
			model.addAttribute(ModelAttr.REGISTRO, ResponseEntity.ok(dato));
			model.addAttribute(ModelAttr.DATO, dato);
		}
		return MAPPING_FORM;
	}

	private ResponseEntity<?> almacenar(Property dato, String lang, String valor) {
		FileOutputStream os = null;
		try {
			String pathI18n = Propiedades.getPathi18n() + "_" + lang + ".properties";
			final Properties props = new Properties();
			props.load(new InputStreamReader(new FileInputStream(pathI18n), Charset.forName("UTF-8")));
			
	        props.setProperty(dato.getId(), valor);
	
	        props.store(new OutputStreamWriter(new FileOutputStream(Propiedades.getPathi18n() + "_" + lang + ".properties"), "UTF-8"), null);
	        return ResponseEntity.ok(dato);
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
	}

}
