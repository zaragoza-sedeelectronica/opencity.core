package org.sede.servicio.acceso.dao;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.sede.core.anotaciones.Gcz;
import org.sede.core.anotaciones.OpenData;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.exception.SinCredencialesDefinidas;
import org.sede.core.exception.SinPermisoParaEjecutar;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.Funciones;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.Seccion;
import org.sede.servicio.acceso.entity.Servicio;

public class Autorizacion {

	public void revisarPeticion(Peticion peticion, HttpServletRequest httprequest) throws SinCredencialesDefinidas, SinPermisoParaEjecutar {
		if (!peticion.getMetodo().isAnnotationPresent(OpenData.class)) {
			if (peticion.getMetodo().isAnnotationPresent(Permisos.class)) {
				Gcz servicioSeccion = peticion.getMetodo().getDeclaringClass().getAnnotation(Gcz.class);
				Permisos p = peticion.getMetodo().getAnnotation(Permisos.class);
				if (!comprobarPermiso(peticion.getCredenciales(), p.value(), servicioSeccion.servicio(), servicioSeccion.seccion())) {
					throw new SinPermisoParaEjecutar();
				}
				peticion.setPermisosEnSeccion(obtenerPermisosSeccion(peticion.getCredenciales(), servicioSeccion.servicio(), servicioSeccion.seccion()));
				
			} else {
				throw new SinCredencialesDefinidas();
			}
		} else {
			if (peticion.getMetodo().isAnnotationPresent(Permisos.class)) {
				Gcz servicioSeccion = peticion.getMetodo().getDeclaringClass().getAnnotation(Gcz.class);
				peticion.setPermisosEnSeccion(obtenerPermisosSeccion(peticion.getCredenciales(), servicioSeccion.servicio(), servicioSeccion.seccion()));
			} else {
				peticion.setPermisosEnSeccion(Funciones.getPeticion().getPermisosEnSeccion());
			}
		}
		httprequest.setAttribute(CheckeoParametros.ATTRPETICION, peticion);
    }

    

	public static List<String> obtenerPermisosSeccion(Credenciales credenciales, String servicio, String seccion) {
		for (int i = 0; (i < credenciales.getServicios().size()); i++) {
			Servicio s = credenciales.getServicios().get(i);
			if (servicio.equals(s.getCodigoServicio())) {
				for (int j = 0; (j < s.getSecciones().size()); j++) {
					Seccion sec = s.getSecciones().get(j);
					if (seccion.equals(sec.getCodigoSeccion())) {
						return sec.getPermisos();
					}
				}
				break;
			}
		}
		return new ArrayList<String>();
	}

	public static boolean comprobarPermiso(Credenciales credenciales, String permisoNecesario, String servicio, String seccion) {
		boolean puedeEjecutar = false;
		if (permisoNecesario != null) {
			for (int i = 0; (i < credenciales.getServicios().size() && !puedeEjecutar); i++) {
				Servicio s = credenciales.getServicios().get(i);
				if (servicio.equals(s.getCodigoServicio())) {
					for (int j = 0; (j < s.getSecciones().size() && !puedeEjecutar); j++) {
						Seccion sec = s.getSecciones().get(j);
						if (seccion.equals(sec.getCodigoSeccion())) {
							for (int k = 0; k < sec.getPermisos().size(); k++) {
								if (permisoNecesario.equals(sec.getPermisos().get(k))) {
									puedeEjecutar = true;
									break;
								}
							}
						}
					}
				}
			}
		}
		return puedeEjecutar;
	}

	public boolean puedeAccederServicio(Credenciales cred, Class<?> clase) {
		if (clase.isAnnotationPresent(Gcz.class)){
			String servicio = clase.getAnnotation(Gcz.class).servicio();
			for (int i = 0; (i < cred.getServicios().size()); i++) {
				Servicio s = cred.getServicios().get(i);
				if (s.getCodigoServicio().equals(servicio)) {
					return true;
				}
			}
		}
		for (int i = 0; i < clase.getMethods().length; i++) {
			Method m = clase.getMethods()[i];
			if (m.isAnnotationPresent(org.sede.core.anotaciones.OpenData.class) && clase.getName().indexOf("AccesoPublic") < 0) {
				return true;
			}
		}
		return usuarioLogueado(cred);
	}
	
	public static boolean usuarioLogueado(Credenciales cred) {
		return cred.getUsuario().getLogin() != null && cred.getUsuario().getLogin().length() > 0;
	}

}
