package org.sede.servicio.acceso.dao;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.sede.core.rest.Mensaje;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.GczGrupoUsuario;
import org.springframework.http.ResponseEntity;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

public interface GczGroupUsuarioGenericDAO extends GenericDAO<GczGrupoUsuario, BigDecimal> {
	public Set<ConstraintViolation<Object>> validar(Object registro);
	public int updateVisible(BigDecimal id, String value);
	public ResponseEntity<?> obtenerGruposAsociadosPersona(Credenciales credenciales);
	public Mensaje asociarUserAGroup(BigDecimal groupId, Long userId);
	public Mensaje eliminarDeGrupo(BigDecimal groupId, BigDecimal userId);
	
}
