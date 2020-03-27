package org.sede.servicio.acceso.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.hibernate.Query;
import org.sede.core.dao.JPAIgnoreTraversableResolver;
import org.sede.core.utils.Funciones;
import org.sede.servicio.acceso.ConfigAcceso;
import org.sede.servicio.acceso.entity.GczServicio;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;

/**
 * The Class GczServicioGenericDAOImpl.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
@Repository
@Transactional(ConfigAcceso.TM)
public class GczServicioGenericDAOImpl extends GenericDAOImpl <GczServicio, Serializable> implements GczServicioGenericDAO {

	/**
	 * Sets the entity manager.
	 *
	 * @param entityManager the new entity manager
	 */
	@PersistenceContext(unitName=ConfigAcceso.ESQUEMA)
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
	public int updateVisible(String id, String value) {
		
		Query propWeb = getSession().createSQLQuery("update " + persistentClass.getAnnotation(Table.class).name() 
				+ " set gcz_publicado=?, gcz_usuariopub=?, gcz_fechapub=? where CODIGO_SERVICIO=?");
		propWeb.setString(0, value);
		propWeb.setString(1, Funciones.getPeticion().getClientId());
		propWeb.setDate(2, new Date());
		propWeb.setString(3, id);
		return propWeb.executeUpdate();

	}
	
}