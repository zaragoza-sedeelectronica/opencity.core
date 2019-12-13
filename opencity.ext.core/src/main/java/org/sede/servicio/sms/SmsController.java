package org.sede.servicio.sms;

import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.ResponseClass;
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.MimeTypes;
import org.sede.servicio.sms.dao.SmsGenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Gcz(servicio="OGOB",seccion="OGOB")
@RequestMapping(value = "/" + SmsController.MAPPING, method = RequestMethod.GET)
public class SmsController {

	public static final String MAPPING = "servicio/sms";
	
	@Autowired
	private SmsGenericDAO dao;
	
	
	@Permisos(Permisos.DET)
	@ResponseClass(value = Mensaje.class)
	@RequestMapping(method = RequestMethod.GET, produces = { MimeTypes.JSON,
			MimeTypes.XML, MimeTypes.CSV, MimeTypes.JSONLD, MimeTypes.RDF,
			MimeTypes.TURTLE, MimeTypes.RDF_N3 })
	public @ResponseBody ResponseEntity<?> apiSendSms() {
		try {
			Mensaje m = dao.send(new String[]{"<tel>"}, "prueba");
			return ResponseEntity.status(HttpStatus.OK).body(m);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
		}
	}
}
