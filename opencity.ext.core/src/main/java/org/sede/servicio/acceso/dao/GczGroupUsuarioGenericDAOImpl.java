package org.sede.servicio.acceso.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.Query;
import org.sede.core.dao.JPAIgnoreTraversableResolver;
import org.sede.core.rest.Mensaje;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.LiderUtils;
import org.sede.servicio.acceso.Constants;
import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.GczGrupoUsuario;
import org.sede.servicio.acceso.entity.Lider;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
import com.googlecode.genericdao.search.SearchResult;
// TODO: Auto-generated Javadoc

/**
 * The Class GczGroupUsuarioGenericDAOImpl.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
@Repository
@Transactional(Constants.TM)
public class GczGroupUsuarioGenericDAOImpl extends GenericDAOImpl <GczGrupoUsuario, BigDecimal> implements GczGroupUsuarioGenericDAO {
	
	/**
	 * Sets the entity manager.
	 *
	 * @param entityManager the new entity manager
	 */
	@PersistenceContext(unitName=Constants.ESQUEMA)
	public void setEntityManager(EntityManager entityManager) {
		this.setEm(entityManager);
	}
	
	
	/**
	 * Validar.
	 *
	 * @param registro Registro
	 * @return sets the
	 */
	public Set<ConstraintViolation<Object>> validar(Object registro) {
		ValidatorFactory factory = Validation.byDefaultProvider().configure().traversableResolver(new JPAIgnoreTraversableResolver()).buildValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(registro);
	}
	
	/**
	 * Update visible.
	 *
	 * @param id Id
	 * @param value Value
	 * @return int
	 */
	public int updateVisible(BigDecimal id, String value) {
	
		Query propWeb = getSession().createSQLQuery("update " + persistentClass.getAnnotation(Table.class).name() 
				+ " set gcz_publicado=?, gcz_usuariopub=?, gcz_fechapub=? where ID_GRUPO_USUARIO=?");
		propWeb.setString(0, value);
		propWeb.setString(1, Funciones.getPeticion().getClientId());
		propWeb.setDate(2, new Date());
		propWeb.setBigDecimal(3, id);
		return propWeb.executeUpdate();

	}

	/**
	 * Obtener grupos asociados persona.
	 *
	 * @param credenciales Credenciales
	 * @return response entity
	 */
	@Override
	public ResponseEntity<?> obtenerGruposAsociadosPersona(Credenciales credenciales) {
		
		List<Lider> resultado =  LiderUtils.liderazgosGestor("org.sede.servicio.acceso.entity.GczGrupoUsuario", credenciales);
		List<BigDecimal> grupos = new ArrayList<BigDecimal>();
		for (Lider l : resultado) {
			grupos.add(l.getAssociatedId());
		}
		javax.persistence.Query q = em().createQuery("FROM GczGrupoUsuario WHERE id in (:groups) and visible='S'", GczGrupoUsuario.class)
				.setParameter("groups", grupos);
		@SuppressWarnings("unchecked")
		List<GczGrupoUsuario> lista = q.getResultList();
		SearchResult<GczGrupoUsuario> res = new SearchResult<GczGrupoUsuario>();
		res.setResult(lista);
		res.setTotalCount(lista.size());
		res.setRows(lista.size());
		res.setStart(0);
		return ResponseEntity.ok(res);
	}

	/**
	 * Asociar user A group.
	 *
	 * @param groupId Group id
	 * @param userId User id
	 * @return mensaje
	 */
	@Override
	public Mensaje asociarUserAGroup(BigDecimal groupId, Long userId) {
		if (!usuarioAsociado(groupId, userId)) {
			String q = "insert into GCZ_USUARIO_GRUPO_USUARIO(ID_GRUPO_USUARIO, ID_USUARIO) "
					+ "values (?, ?)";	
			javax.persistence.Query update = em().createNativeQuery(q);
			int x = 0;
			update.setParameter(++x, groupId);
			update.setParameter(++x, userId);
			update.executeUpdate();
			return new Mensaje(HttpStatus.OK.value(), "Registro asociado correctamente");
		} else {
			return new Mensaje(HttpStatus.BAD_REQUEST.value(), "La persona ya está asociada al grupo");
		}
	}
	
	/**
	 * Usuario asociado.
	 *
	 * @param groupId Group id
	 * @param userId User id
	 * @return true, if successful
	 */
	private boolean usuarioAsociado(BigDecimal groupId, Long userId) {
		try {
			javax.persistence.Query q = em().createNativeQuery("select id_usuario from GCZ_USUARIO_GRUPO_USUARIO "
					+ "where ID_GRUPO_USUARIO = ? and ID_USUARIO = ?")
					.setParameter(1, groupId)
					.setParameter(2, userId);
			@SuppressWarnings("unchecked")
			List<Object> retorno = q.getResultList();
			return (!retorno.isEmpty());
		} catch (NoResultException e) {
			return false;
		}
	}

	/**
	 * Eliminar de grupo.
	 *
	 * @param groupId Group id
	 * @param userId User id
	 * @return mensaje
	 */
	@Override
	public Mensaje eliminarDeGrupo(BigDecimal groupId, BigDecimal userId) {
		try {
			javax.persistence.Query update = em().createNativeQuery(
					"delete from GCZ_USUARIO_GRUPO_USUARIO "
					+ "where ID_GRUPO_USUARIO = ? and ID_USUARIO = ?");
			int x = 0;
			update.setParameter(++x, groupId);
			update.setParameter(++x, userId);
			update.executeUpdate();
			
			return new Mensaje(HttpStatus.OK.value(), "Persona eliminada del grupo correctamente");
		} catch (Exception e) {
			return new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage());
		}
	}
}