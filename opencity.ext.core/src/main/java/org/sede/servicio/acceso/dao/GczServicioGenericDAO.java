package org.sede.servicio.acceso.dao;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.sede.servicio.acceso.entity.GczServicio;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

public interface GczServicioGenericDAO extends GenericDAO<GczServicio, Serializable> {
	
	public Set<ConstraintViolation<Object>> validar(Object registro);
	public int updateVisible(String id, String value);
}
