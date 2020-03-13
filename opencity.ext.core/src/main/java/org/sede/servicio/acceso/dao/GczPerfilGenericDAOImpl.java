package org.sede.servicio.acceso.dao;

import java.math.BigDecimal;
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
import org.sede.servicio.acceso.Constants;
import org.sede.servicio.acceso.entity.GczPerfil;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
// TODO: Auto-generated Javadoc

/**
 * The Class GczPerfilGenericDAOImpl.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
@Repository
@Transactional(Constants.TM)
public class GczPerfilGenericDAOImpl extends GenericDAOImpl <GczPerfil, BigDecimal> implements GczPerfilGenericDAO {

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
	 * @param servicio Servicio
	 * @param id Id
	 * @param value Value
	 * @return int
	 */
	public int updateVisible(String servicio, BigDecimal id, String value) {
		
		Query propWeb = getSession().createSQLQuery("update " + persistentClass.getAnnotation(Table.class).name() 
				+ " set gcz_publicado=? where CODIGO_SERVICIO=? and ID_PERFIL=?");
		propWeb.setString(0, value);
		propWeb.setString(1, servicio);
		propWeb.setBigDecimal(2, id);
		return propWeb.executeUpdate();

	}
	
}