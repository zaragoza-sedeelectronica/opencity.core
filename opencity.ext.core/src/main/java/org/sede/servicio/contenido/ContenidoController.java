package org.sede.servicio.contenido;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.solr.common.SolrInputDocument;
import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.NoCache;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.dao.Solr;
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.solr.Faceta;
import org.sede.core.tag.Utils;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.ProcesadorImagenes;
import org.sede.core.utils.Propiedades;
import org.sede.core.validator.HTMLValidator;
import org.sede.servicio.ModelAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;



/**
 * The Class ContenidoController.
 *
 * @author Ayuntamiento Zaragoza
 */
@Gcz(servicio="CONTENIDOS",seccion="PAGINAS")
@Controller
@RequestMapping(value = "/" + ContenidoController.MAPPING, method = RequestMethod.GET)
public class ContenidoController {
	
	/** Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(ContenidoController.class);
	
	/** Constant SERVICIO. */
	private static final String SERVICIO = "contenido";
	
	/** Constant MAPPING. */
	public static final String MAPPING = "servicio/" + SERVICIO;
	
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
	 * Nuevo.
	 *
	 * @param model Model
	 * @param path Path
	 * @return string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	@NoCache
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String nuevo(Model model, @RequestParam(name = "path", required = false) String path) throws IOException {
		if (path != null) {
			String pathFisico = Propiedades.getPathVistas() + path + ".xml";
			FileInputStream fisTargetFile = new FileInputStream(new File(pathFisico));
			model.addAttribute("path", path);
			model.addAttribute("content", IOUtils.toString(fisTargetFile, CharEncoding.UTF_8));
		} else {
			model.addAttribute("content", "<!DOCTYPE html>" + System.getProperty("line.separator") +
				"<html xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:th=\"http://www.thymeleaf.org\" xmlns:sede=\"http://www.zaragoza.es\" lang=\"es\">" + System.getProperty("line.separator") +
				"<head>" + System.getProperty("line.separator") +
				"    <meta charset=\"utf-8\" />	" + System.getProperty("line.separator") +
				"	<sede:meta title=\"\"></sede:meta>" + System.getProperty("line.separator") +
				"</head>" + System.getProperty("line.separator") +
				"<body>" + System.getProperty("line.separator") +
				"    <sede:content>" + System.getProperty("line.separator") +
				"" + System.getProperty("line.separator") +
				"    </sede:content>" + System.getProperty("line.separator") +
				"</body>" + System.getProperty("line.separator") +
				"</html>");
		}
		return MAPPING + "/edit";
	}
	
	/**
	 * Guardar.
	 *
	 * @param path Path
	 * @param content Content
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@RequestMapping(value = "/save", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String guardar(@RequestParam(value="path", required=false) String path,
			@RequestParam(value="content", required = true) String content, Model model) {
		HTMLValidator validador = new HTMLValidator();
		try {
			validador.validate(content);
			Funciones.saveFile(Propiedades.getPathVistas() + path + ".xml", content);
			model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro guardado correctamente"));
		} catch (ParserConfigurationException e) {
			model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (SAXException e) {
			model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		} catch (IOException e) {
			model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		}
		
		return "fragmentos/error";
	}

	/**
	 * Imagen.
	 *
	 * @param path Path
	 * @param file File
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@RequestMapping(value = "/img", method = RequestMethod.POST, produces = {MediaType.TEXT_HTML_VALUE, "*/*" })
	public String imagen(@RequestParam(value="path", required=false) String path,
			@RequestParam("imagen") MultipartFile file, Model model) {
		if (!file.isEmpty()) {
			try {
				String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.'), file.getOriginalFilename().length());
				Funciones.saveFileOverWrite(Propiedades.getPathVistas() + path, file.getOriginalFilename() + extension, file.getInputStream());
			} catch (IOException e) {
				model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "Error al cargar fichero " + file.getOriginalFilename() + " => " + e.getMessage()));
			}
		}
		return "fragmentos/error";
	}
	
	/**
	 * Indizar.
	 *
	 * @param uri Uri
	 * @param title Title
	 * @param description Description
	 * @param category Category
	 * @param author Author
	 * @param subject Subject
	 * @param lastModified Last modified
	 * @param language Language
	 * @param keywords Keywords
	 * @param audience Audience
	 * @param text Text
	 * @param priority Priority
	 * @param robots Robots
	 * @param links Links
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.MOD)
	@RequestMapping(value = "/index-solr", method = RequestMethod.POST, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*" })
	public String indizar(
			@RequestParam(value="uri") String uri,
			@RequestParam(value="title") String title,
			@RequestParam(value="description") String description,
			@RequestParam(value="category") String category,
			@RequestParam(value="author") String author,
			@RequestParam(value="subject") String subject,
			@RequestParam(value=Faceta.FACET_FECHA_MODIFICADO) String lastModified,
			@RequestParam(value="language", defaultValue = "es") String language,
			@RequestParam(value="keywords") String keywords,
			@RequestParam(value="audience", defaultValue = "") String audience,
			@RequestParam(value="text") String text,
			@RequestParam(value="priority", defaultValue = "") String priority,
			@RequestParam(value="robots", defaultValue = "") String robots,
			@RequestParam(value="links") String links, 
			Model model) {
		if (!robots.toLowerCase().contains("noindex")) {
			try {
				String docId = "contenido-" + String.valueOf(uri.hashCode()).replace("-", "n");
				SolrInputDocument registro = new SolrInputDocument();
				registro.addField("id", docId);
				registro.addField("uri", Funciones.corregirUri(uri));
				if (StringUtils.isNotEmpty(category)) {
					registro.addField("category", category);
				}
				if (uri.endsWith("/") || !"".equals(priority.trim())) { //A las páginas índice les damos más popularidad
					registro.addField("title",  title, 2);
				} else {
					registro.addField("title",  title);	
				} 
				if (StringUtils.isNotEmpty(author)) {
					registro.addField("author",  author);
				}

				registro.addField("language", language);
				registro.addField("keywords_smultiple", keywords);
				registro.addField("description", Utils.removeHTMLEntity(description));
				registro.addField("texto_t", Utils.removeHTMLEntity(description));
				registro.addField("text", Utils.removeHTMLEntity(text));
				registro.addField("content_type", "HTML");
				if (StringUtils.isNotEmpty(lastModified)) {
					try {
						registro.addField(Faceta.FACET_FECHA_MODIFICADO, ConvertDate.date2String(ConvertDate.string2Date(lastModified, "EEE MMM dd HH:mm:ss 'CET' yyyy", Locale.US), ConvertDate.ISO8601_FORMAT));
					} catch (Exception e) {
						registro.addField(Faceta.FACET_FECHA_MODIFICADO, lastModified);
						logger.error("Error en last-modified:{}", uri);
					}
				} else {
					registro.addField(Faceta.FACET_FECHA_MODIFICADO, ConvertDate.date2String(new Date(), ConvertDate.ISO8601_FORMAT));
				}
	
		        if (links != null) {
		            registro.addField("links", links);
				}
				String[] valores = subject.trim().split(",");
				
				for (int i = 0; i < valores.length; i++) {
					if (!"".equals(valores[i].trim())) {
						registro.addField(Faceta.FACET_TEMA, valores[i].trim());
					}
				}
				valores = audience.split(",");
				for (int i = 0; i < valores.length; i++) {
					if (!"".equals(valores[i].trim())) {
						registro.addField(Faceta.FACET_DIRIGIDO, valores[i].trim());
					}
				}
				registro.addField("content_type", "HTML");

				int resultado = Solr.getInstance().save(registro);
				if (resultado < 0) {
					model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "No se ha indizado el contenido"));	
				} else {
					Solr.getInstance().commit();
					model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Registro indizado correctamente"));
				}
				
			} catch (Exception e) {
				model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
			}
		} else {
			Solr.getInstance().deleteUri(uri);
			model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "Registro eliminado del índice al contener robots noindex"));
		}
		
		return "fragmentos/error";
	}
	
	/**
	 * Removes the.
	 *
	 * @param path Path
	 * @param model Model
	 * @return string
	 */
	@Permisos(Permisos.DEL)
	@RequestMapping(value = "/remove", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE, "*/*" })
	public String remove(@RequestParam(value="path") String path, Model model) {
		File f = new File(Propiedades.getPathVistas() + path + ".xml");
		if (f.delete()) {
			model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.OK.value(), "Fichero en path " + path + " eliminado correctamente"));
		} else {
			model.addAttribute(ModelAttr.MENSAJE, new Mensaje(HttpStatus.BAD_REQUEST.value(), "Fichero en path " + path + " NO se ha eliminado"));
		}
		return "fragmentos/error";
	}
	
	
	@Permisos(Permisos.DET)
	@RequestMapping(value = "/list", method = RequestMethod.GET, produces = {MediaType.TEXT_HTML_VALUE, "*/*" })
	public String list(@RequestParam(value="path", required = false, defaultValue="") String path,
			Model model) {
		String carpeta = Propiedades.getPathVistas() + "portal/" + Funciones.getPeticion().getCredenciales().getUsuario().getPropiedades().get("carpeta") + path + "/";
		logger.info(carpeta);
		File folder = new File(carpeta);
		model.addAttribute("resultado", folder.listFiles());
		
		return MAPPING + "/list";
	}
	
	
	@Permisos(Permisos.DET)
	@RequestMapping(value = "/upload", method = RequestMethod.POST, produces = {MediaType.TEXT_HTML_VALUE, "*/*" })
	public String upload(@RequestParam(value="image", required = false) String image,
			@RequestParam("upload") MultipartFile upload,
			Model model) throws IOException {		
		try {
			
	        String filename =  Funciones.normalizar(upload.getOriginalFilename());
	        	        
			if (image == null) {
				filename = Funciones.saveFileGetFileName((Propiedades.getPathContDisk() + "vistas/portal/" + Funciones.getPeticion().getCredenciales().getUsuario().getPropiedades().get("carpeta") + "/doc/").replace("///", "/"), filename, upload.getInputStream());
				model.addAttribute("linkFile", ("/cont/vistas/portal/" + Funciones.getPeticion().getCredenciales().getUsuario().getPropiedades().get("carpeta") + "/doc/").replace("///", "/") + filename);
			} else {
				ProcesadorImagenes pi = new ProcesadorImagenes();
				BufferedImage in = ImageIO.read(upload.getInputStream());
				BufferedImage anexoMiniatura = pi.escalarATamanyo(in, 800, 800);
				filename = pi.salvarImagen(anexoMiniatura, (Propiedades.getPathContDisk() + "vistas/portal/" + Funciones.getPeticion().getCredenciales().getUsuario().getPropiedades().get("carpeta") + "/img").replace("///", "/"), filename, "jpg", false);
				model.addAttribute("linkFile", ("/cont/vistas/portal/" + Funciones.getPeticion().getCredenciales().getUsuario().getPropiedades().get("carpeta") + "/img/").replace("///", "/") + filename);
			}
			
			return MAPPING + "/upload";
		} catch (Exception e) {
			logger.error(Funciones.getStackTrace(e));
			return null;
		}
	}
	
}