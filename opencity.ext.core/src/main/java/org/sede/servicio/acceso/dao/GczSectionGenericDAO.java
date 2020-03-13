package org.sede.servicio.acceso.dao;

import java.io.Serializable;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.sede.servicio.acceso.entity.GczSeccion;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

// TODO: Auto-generated Javadoc
/**
 * The Interface GczSectionGenericDAO.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
public interface GczSectionGenericDAO extends GenericDAO<GczSeccion, Serializable> {
	
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
	 * @param seccion Seccion
	 * @param servicio Servicio
	 * @param value Value
	 * @return int
	 */
	public int updateVisible(String seccion, String servicio, String value);
}
