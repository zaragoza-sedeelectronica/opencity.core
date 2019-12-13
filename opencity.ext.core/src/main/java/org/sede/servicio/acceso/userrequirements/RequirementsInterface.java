package org.sede.servicio.acceso.userrequirements;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.sede.servicio.acceso.entity.Ciudadano;
import org.springframework.web.method.HandlerMethod;
//@Resource
public interface RequirementsInterface {
	public boolean validate(Ciudadano c);
	public String getFormElement(Ciudadano c, HttpServletRequest request);
	public String storeRequirement(Ciudadano c, HttpServletRequest request);
	public void setMethod(HandlerMethod metodo);
	public String getErrorMessage();
}
