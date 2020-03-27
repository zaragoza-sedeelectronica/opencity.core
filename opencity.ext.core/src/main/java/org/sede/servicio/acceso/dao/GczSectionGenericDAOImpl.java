package org.sede.servicio.acceso.dao;

import java.io.Serializable;
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
import org.sede.servicio.acceso.ConfigAcceso;
import org.sede.servicio.acceso.entity.GczSeccion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
/**
 * The Class GczSectionGenericDAOImpl.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
@Repository
@Transactional(ConfigAcceso.TM)
public class GczSectionGenericDAOImpl extends GenericDAOImpl <GczSeccion, Serializable> implements GczSectionGenericDAO {

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
	 * @param seccion Seccion
	 * @param servicio Servicio
	 * @param value Value
	 * @return int
	 */
	public int updateVisible(String seccion, String servicio, String value) {
		
		Query propWeb = getSession().createSQLQuery("update " + persistentClass.getAnnotation(Table.class).name() 
				+ " set gcz_publicado=? where CODIGO_SECCION=? and CODIGO_SERVICIO=?");
		propWeb.setString(0, value);
		propWeb.setString(1, seccion);
		propWeb.setString(2, servicio);
		return propWeb.executeUpdate();

	}
	
}