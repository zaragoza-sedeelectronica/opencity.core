package org.sede.servicio.acceso.userrequirements;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.sede.core.utils.Funciones;
import org.sede.servicio.acceso.dao.CiudadanoGenericDAO;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.sede.servicio.padron.dao.PadronGenericDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.web.method.HandlerMethod;
@Resource
@Repository
public class Padron implements RequirementsInterface {
	
	@Autowired
    private PadronGenericDAO daoPadron;
	@Autowired
	private CiudadanoGenericDAO dao;
		
	private static final String NIFFIELD = "nif";
	private static final String BIRTHYEARFIELD = "birthyear";
	
	public boolean validate(Ciudadano c) {
		return "Si".equals(c.getEmpadronado());
	}

	@Override
	public String getFormElement(Ciudadano c, HttpServletRequest request) {
		return "<fieldset><legend>Comprobación de estar empadronado en Zaragoza</legend><div class=\"form-group\">"
			  + "<label class=\"col-sm-3 col-md-3 col-lg-2 control-label\" for=\"" + NIFFIELD + "\"><span class=\"obligatorio\">DNI:</span><span class=\"oculto\"> (campo obligatorio)</span></label>"
			  + "<div class=\"col-sm-6 col-md-5 col-lg-4\">"
			  + "<input class=\"form-control\" type=\"text\" size=\"100\" required=\"true\" id=\"" + NIFFIELD + "\" name=\"" + NIFFIELD + "\" value=\"" + (request.getParameter(NIFFIELD) == null ? "" : request.getParameter(NIFFIELD)) + "\"/>"
			  + "</div>"
			  + "</div>"
			  + "<div class=\"form-group\">"
			  + "<label class=\"col-sm-3 col-md-3 col-lg-2 control-label\" for=\"" + BIRTHYEARFIELD + "\"><span class=\"obligatorio\">Año de Nacimiento:</span><span class=\"oculto\"> (campo obligatorio)</span></label>"
			  + "<div class=\"col-sm-6 col-md-3 col-lg-2\">"
			  + "<input class=\"form-control\" type=\"text\" size=\"100\" required=\"true\" id=\"" + BIRTHYEARFIELD + "\" name=\"" + BIRTHYEARFIELD + "\" value=\"" + (request.getParameter(BIRTHYEARFIELD) == null ? "" : request.getParameter(BIRTHYEARFIELD)) + "\"/>"
			  + "</div>"
			  + "</div></fieldset>";
	}

	@Override
	public String storeRequirement(Ciudadano c, HttpServletRequest request) {
		
		c.setNif(Funciones.cleanNif(request.getParameter(NIFFIELD)));
		try {
			c.setBirthYear(Integer.parseInt(request.getParameter(BIRTHYEARFIELD)));
		} catch (NumberFormatException e) {
			return "El año de nacimiento debe ser un número válido";
		}
		Boolean retorno = daoPadron.checkEmpadronado(c.getNif(), c.getBirthYear());
		if (retorno == Boolean.TRUE) {
			if (dao.usuarioNifExistente(c)) {
				return "Ya existe una persona asociada al DNI/NIF introducido";
			} else {
				c.setEmpadronado("Si");
				String[] distritoSeccion = daoPadron.showDistrito(c.getNif(), c.getBirthYear()).split("-");
				c.setDistrict(Integer.parseInt(distritoSeccion[0]));
				c.setSection(Integer.parseInt(distritoSeccion[1]));
				c.setJunta(daoPadron.showJunta(c.getNif(), c.getBirthYear()));
				dao.updatePadronData(c);
				return "";
			}
		} else if (retorno == Boolean.FALSE) {
			return "Los datos proporcionados no corresponden a una persona empadronada en Zaragoza";
		} else {
			return "Se produjo un error al consultar los datos de padrón";
		}
	}
	
	@Override
	public void setMethod(HandlerMethod metodo) {
		
	}

	@Override
	public String getErrorMessage() {
		return "Sólo pueden participar personas empadronadas en Zaragoza";
	}
}
