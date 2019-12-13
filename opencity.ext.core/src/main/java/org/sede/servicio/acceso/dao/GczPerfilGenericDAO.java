package org.sede.servicio.acceso.dao;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.sede.servicio.acceso.entity.GczPerfil;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
public interface GczPerfilGenericDAO extends GenericDAO<GczPerfil, BigDecimal> {
	

	public Set<ConstraintViolation<Object>> validar(Object registro);
	public int updateVisible(String servicio, BigDecimal id, String value);
}
