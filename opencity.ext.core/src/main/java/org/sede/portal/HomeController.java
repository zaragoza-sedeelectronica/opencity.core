package org.sede.portal;

import java.text.ParseException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.sede.core.anotaciones.Cache;
import org.sede.core.anotaciones.OpenData;
import org.sede.core.anotaciones.PlantillaHTML;
import org.sede.core.anotaciones.ResponseClass;
import org.sede.core.entity.CalendarDay;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.MimeTypes;
import org.sede.portal.dao.StaticData;
//import org.sede.servicio.actividades.dao.ActoGenericDAO;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.googlecode.genericdao.search.SearchResult;
import com.ibm.icu.util.Calendar;

@Controller
@PlantillaHTML(HomeController.MAPPING)
public class HomeController {

	public static final String MAPPING = "portal/sede";

	@Autowired
	private StaticData datos;
//	@Autowired
//	private ActoGenericDAO daoActo;
	
	@Cache(Cache.DURACION_30MIN)
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {
			MediaType.TEXT_HTML_VALUE, "*/*"})
	public String home(Model model, HttpServletRequest request) throws ParseException {
		datos.setRefresh(request.getParameter(CheckeoParametros.REFRESHPARAMETER) != null);
//		model.addAttribute("avisoDestacadoPrincipal", datos.getAvisoDestacadoPrincipal());
//		model.addAttribute("avisosDestacados", datos.getAvisosDestacados());
//		model.addAttribute("actosDestacados", datos.getActosDestacados());
//		model.addAttribute("actosDelDia", datos.getActosDelCalendario(new Date()));
//		model.addAttribute("tramitesEnPlazo", datos.getTramitesEnPlazo());
//		model.addAttribute("tramitesDestacados", datos.getTramitesDestacados());
//		model.addAttribute("noticiaDestacada", datos.getNoticiaDestacada());
//		model.addAttribute("ultimasNoticias", datos.getUltimasNoticias());
//		model.addAttribute("ultimasOfertasEmpleo", datos.getUltimasOfertasEmpleo());
//		model.addAttribute("ultimosContratos", datos.getUltimosContratos());
		model.addAttribute("temperatura", datos.getTemperatura());

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		model.addAttribute("month", c.get(Calendar.MONTH) + 1);
		model.addAttribute("year", c.get(Calendar.YEAR));
		model.addAttribute("fecha", c.getTime());
		model.addAttribute("days", apiMonth(c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR)));

		datos.setRefresh(false);
		return MAPPING + "/index";
	}

	@OpenData
	@Cache(Cache.DURACION_30MIN)
	@ResponseClass(CalendarDay.class)
	@RequestMapping(value = "/", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiHome() {
		return ResponseEntity.ok(datos);
	}
	
	@OpenData
	@Cache(Cache.DURACION_30MIN)
	@ResponseClass(CalendarDay.class)
	@RequestMapping(value = "/days/{month}-{year}", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF, MimeTypes.TURTLE, MimeTypes.RDF_N3})
	public @ResponseBody ResponseEntity<?> apiMonth(@PathVariable(value = "month") Integer month, @PathVariable(value = "year") Integer year) throws ParseException {
//		try {
//			return ResponseEntity.ok(daoActo.getDays(month, year, null, null));
//		} catch (Exception e) {
//			return ResponseEntity.ok(new SearchResult<CalendarDay>());
//		}
		return ResponseEntity.ok(new SearchResult<CalendarDay>());
	}

}
