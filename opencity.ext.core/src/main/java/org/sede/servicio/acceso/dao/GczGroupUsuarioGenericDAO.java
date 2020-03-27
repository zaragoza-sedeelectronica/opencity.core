package org.sede.servicio.acceso.dao;

import java.math.BigDecimal;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.sede.core.rest.Mensaje;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.GczGrupoUsuario;
import org.springframework.http.ResponseEntity;

import com.googlecode.genericdao.dao.jpa.GenericDAO;

/**
 * Interface GczGroupUsuarioGenericDAO.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 * 
 */
public interface GczGroupUsuarioGenericDAO extends GenericDAO<GczGrupoUsuario, BigDecimal> {
	
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
	public int updateVisible(BigDecimal id, String value);
	
	/**
	 * Obtener grupos asociados persona.
	 *
	 * @param credenciales Credenciales
	 * @return response entity
	 */
	public ResponseEntity<?> obtenerGruposAsociadosPersona(Credenciales credenciales);
	
	/**
	 * Asociar user A group.
	 *
	 * @param groupId Group id
	 * @param userId User id
	 * @return mensaje
	 */
	public Mensaje asociarUserAGroup(BigDecimal groupId, Long userId);
	
	/**
	 * Eliminar de grupo.
	 *
	 * @param groupId Group id
	 * @param userId User id
	 * @return mensaje
	 */
	public Mensaje eliminarDeGrupo(BigDecimal groupId, BigDecimal userId);
	
}
