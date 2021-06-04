package org.sede.servicio.contenido.dao;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

import org.sede.servicio.contenido.entity.IndexError;

import javax.validation.ConstraintViolation;

import java.util.Set;

public interface IndexErrorGenericDAO extends GenericDAO<IndexError, String> {

	public Set<ConstraintViolation<Object>> validar(Object registro);
	
	public int updateRevisado(String id, String value);
	
	public String getStatistics();

	public boolean updateStatusSinIndizar(String id);

	public String getStatisticsByCode();
}
