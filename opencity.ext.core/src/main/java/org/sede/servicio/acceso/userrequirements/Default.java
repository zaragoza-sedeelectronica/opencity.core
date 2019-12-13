package org.sede.servicio.acceso.userrequirements;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.sede.servicio.acceso.entity.Ciudadano;
import org.springframework.stereotype.Repository;
import org.springframework.web.method.HandlerMethod;

@Resource
@Repository
public class Default implements RequirementsInterface {
	
	public boolean validate(Ciudadano c) {
		return true;
	}

	public String getFormElement(Ciudadano c, HttpServletRequest request) {
		return null;
	}

	@Override
	public String storeRequirement(Ciudadano c, HttpServletRequest request) {
		return "";
	}

	@Override
	public void setMethod(HandlerMethod metodo) {
		
	}

	@Override
	public String getErrorMessage() {
		return null;
	}
}
