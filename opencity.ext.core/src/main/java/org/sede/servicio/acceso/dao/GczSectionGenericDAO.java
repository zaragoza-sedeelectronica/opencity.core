package org.sede.servicio.acceso.dao;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.sede.servicio.acceso.entity.GczSeccion;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

public interface GczSectionGenericDAO extends GenericDAO<GczSeccion, Serializable> {
	
	public Set<ConstraintViolation<Object>> validar(Object registro);
	public int updateVisible(String seccion, String servicio, String value);
}
