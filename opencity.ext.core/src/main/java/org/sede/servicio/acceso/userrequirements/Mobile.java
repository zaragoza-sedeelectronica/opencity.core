package org.sede.servicio.acceso.userrequirements;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.sede.core.rest.Mensaje;
import org.sede.core.utils.Funciones;
import org.sede.servicio.acceso.dao.CiudadanoGenericDAO;
import org.sede.servicio.acceso.entity.Ciudadano;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.method.HandlerMethod;
@Resource
@Repository
public class Mobile implements RequirementsInterface {

	@Autowired
	private CiudadanoGenericDAO dao;
		
	private static final String MOBILEFIELD = "mobile";
	private static final String TOKENMOBILEFIELD = "token_mobile";
	
	public boolean validate(Ciudadano c) {
		return c.getMobile() != null && c.getMobile().length() > 0;
	}

	@Override
	public String getFormElement(Ciudadano c, HttpServletRequest request) {
		if (request.getParameter(MOBILEFIELD) == null && request.getParameter(TOKENMOBILEFIELD) == null) {
			return "<fieldset><legend>Introduzca su teléfono móvil para poder participar</legend><div class=\"form-group\">"
					  + "<label class=\"col-sm-3 col-md-3 col-lg-2 control-label\" for=\"" + MOBILEFIELD + "\"><span class=\"obligatorio\">Teléfono móvil:</span><span class=\"oculto\"> (campo obligatorio)</span></label>"
					  + "<div class=\"col-sm-6 col-md-5 col-lg-4\">"
					  + "<input class=\"form-control\" type=\"text\" size=\"100\" required=\"true\" id=\"" + MOBILEFIELD + "\" name=\"" + MOBILEFIELD + "\" value=\"" + (request.getParameter(MOBILEFIELD) == null ? "" : request.getParameter(MOBILEFIELD)) + "\"/>"
					  + "</div>"
					  + "</div>"
					  + "</div></fieldset>";	
		} else {
			return "<fieldset><legend>Introduzca el código que recibirá en el teléfono móvil</legend><div class=\"form-group\">"
					  + "<label class=\"col-sm-3 col-md-3 col-lg-2 control-label\" for=\"" + TOKENMOBILEFIELD + "\"><span class=\"obligatorio\">Código:</span><span class=\"oculto\"> (campo obligatorio)</span></label>"
					  + "<div class=\"col-sm-6 col-md-5 col-lg-4\">"
					  + "<input class=\"form-control\" type=\"text\" size=\"100\" required=\"true\" id=\"" + TOKENMOBILEFIELD + "\" name=\"" + TOKENMOBILEFIELD + "\" value=\"" + (request.getParameter(TOKENMOBILEFIELD) == null ? "" : request.getParameter(TOKENMOBILEFIELD)) + "\"/>"
					  + "</div>"
					  + "</div>"
					  + "</div></fieldset>";
		}
		
	}

	@Override
	public String storeRequirement(Ciudadano c, HttpServletRequest request) {
		if (request.getParameter(MOBILEFIELD) != null) {
			// Enviamos el token al movil
			String mobile = Funciones.cleanMobile(request.getParameter(MOBILEFIELD)); 
			if (dao.usuarioMobileExistente(mobile)) {
				return "Ya existe una persona asociada al teléfono móvil introducido";
			} else {
				Mensaje retorno = dao.createTokenMobile(c.getId(), mobile);
				if (retorno.getStatus() == HttpStatus.OK.value()) {
					return "Introduzca el código que hemos enviado por mensaje sms al teléfono indicado";
				} else {
					return retorno.getMensaje();
				}
			}
		} else {
			// comprobamos el token
			String token = request.getParameter(TOKENMOBILEFIELD);
			if (dao.asociarMovil(c, token)) {
				
				return "";
			} else {
				return "Código de verificación incorrecto";
			}
		}
	}
	
	@Override
	public void setMethod(HandlerMethod metodo) {
		
	}

	@Override
	public String getErrorMessage() {
		return "Sólo pueden participar personas que tienen asociado un teléfono móvil";
	}
}
