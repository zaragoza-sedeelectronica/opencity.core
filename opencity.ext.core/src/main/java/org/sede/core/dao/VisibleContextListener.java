package org.sede.core.dao;

import javax.persistence.PostLoad;

import org.sede.core.anotaciones.Permisos;
import org.sede.core.exception.NoVisibleException;
import org.sede.core.utils.Funciones;
public class VisibleContextListener {
	 
	@PostLoad
	public void PostLoad(Object object) throws NoVisibleException {
		if ("N".equals(Funciones.retrieveObjectValue(object, "visible")) 
				&& !Funciones.getPeticion().getPermisosEnSeccion().contains(Permisos.DET)) {
			throw new NoVisibleException("Registro no encontrado");
		}
	}
}
