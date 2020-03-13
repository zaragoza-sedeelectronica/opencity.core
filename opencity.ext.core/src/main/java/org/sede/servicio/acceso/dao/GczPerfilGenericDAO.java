package org.sede.servicio.acceso.dao;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.sede.servicio.acceso.entity.GczPerfil;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
// TODO: Auto-generated Javadoc

/**
 * The Interface GczPerfilGenericDAO.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
public interface GczPerfilGenericDAO extends GenericDAO<GczPerfil, BigDecimal> {
	

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
	 * @param servicio Servicio
	 * @param id Id
	 * @param value Value
	 * @return int
	 */
	public int updateVisible(String servicio, BigDecimal id, String value);
}
