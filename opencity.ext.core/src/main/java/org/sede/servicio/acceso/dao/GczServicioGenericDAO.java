package org.sede.servicio.acceso.dao;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.sede.servicio.acceso.entity.GczServicio;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

// TODO: Auto-generated Javadoc
/**
 * The Interface GczServicioGenericDAO.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
public interface GczServicioGenericDAO extends GenericDAO<GczServicio, Serializable> {
	
	/**
	 * Validar.
	 *
	 * @param registro Registro
	 * @return sets the
	 */
	public Set<ConstraintViolation<Object>> validar(Object registro);
	
	/**
	 * Update visible.
	 *
	 * @param id Id
	 * @param value Value
	 * @return int
	 */
	public int updateVisible(String id, String value);
}
