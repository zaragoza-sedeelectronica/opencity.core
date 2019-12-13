package org.sede.servicio.padron;

import org.sede.core.anotaciones.Cache;
import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.ResponseClass;
import org.sede.core.rest.Mensaje;
import org.sede.core.rest.MimeTypes;
import org.sede.servicio.padron.dao.PadronGenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Gcz(servicio="PADRON", seccion="PADRON")
@Controller
@RequestMapping(value = "/" + PadronController.MAPPING, method = RequestMethod.GET)
public class PadronController {

    public static final String MAPPING = "servicio/padron-municipal";

    @Autowired
    private PadronGenericDAO dao;

    @Permisos(Permisos.DET)
    @Cache(Cache.DURACION_30MIN)
    @ResponseClass(value = Mensaje.class)
    @RequestMapping(value = "/consulta", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV})
    public @ResponseBody  ResponseEntity<?> apiCheckEmpadronado(@RequestParam(name = "d") String d, @RequestParam(name = "a") Integer a) {
        if (dao.checkEmpadronado(d, a)) {
            return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(), "Los datos proporcionados corresponden a un ciudadano empadronado"));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new Mensaje(HttpStatus.BAD_REQUEST.value(), "Los datos proporcionados NO corresponden a ningún ciudadano empadronado"));
        }

    }
    @Permisos(Permisos.DET)
    @Cache(Cache.DURACION_30MIN)
    @ResponseClass(value = Mensaje.class)
    @RequestMapping(value = "/distrito", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV})
    public @ResponseBody  ResponseEntity<?> apiDistrito(@RequestParam(name = "d") String d, @RequestParam(name = "a") Integer a) {
        String distrito = dao.showDistrito(d, a);
        if (distrito != null && !"".equals(distrito)) {
            return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(), distrito));
        } else {
            return ResponseEntity.ok(new Mensaje(HttpStatus.BAD_REQUEST.value(), "Los datos proporcionados NO corresponden a ningún ciudadano empadronado"));
        }

    }
    @Permisos(Permisos.DET)
    @Cache(Cache.DURACION_30MIN)
    @ResponseClass(value = Mensaje.class)
    @RequestMapping(value = "/junta", method = RequestMethod.GET, produces = {MimeTypes.JSON, MimeTypes.XML, MimeTypes.CSV})
    public @ResponseBody  ResponseEntity<?> apiJunta(@RequestParam(name = "d") String d, @RequestParam(name = "a") Integer a) {
        String distrito = dao.showJunta(d, a);
        if (distrito != null && !"".equals(distrito)) {
            return ResponseEntity.ok(new Mensaje(HttpStatus.OK.value(), distrito));
        } else {
            return ResponseEntity.ok(new Mensaje(HttpStatus.BAD_REQUEST.value(), "Los datos proporcionados NO corresponden a ningún ciudadano empadronado"));
        }

    }
}
