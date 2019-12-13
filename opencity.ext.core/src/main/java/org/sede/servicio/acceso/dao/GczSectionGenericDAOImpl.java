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
import org.sede.servicio.acceso.Constants;
import org.sede.servicio.acceso.entity.GczSeccion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;
@Repository
@Transactional(Constants.TM)
public class GczSectionGenericDAOImpl extends GenericDAOImpl <GczSeccion, Serializable> implements GczSectionGenericDAO {

	@PersistenceContext(unitName=Constants.ESQUEMA)
	public void setEntityManager(EntityManager entityManager) {
		this.setEm(entityManager);
	}
	
	public Set<ConstraintViolation<Object>> validar(Object registro) {
		ValidatorFactory factory = Validation.byDefaultProvider().configure().traversableResolver(new JPAIgnoreTraversableResolver()).buildValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(registro);
	}
	
	public int updateVisible(String seccion, String servicio, String value) {
		
		Query propWeb = getSession().createSQLQuery("update " + persistentClass.getAnnotation(Table.class).name() 
				+ " set gcz_publicado=? where CODIGO_SECCION=? and CODIGO_SERVICIO=?");
		propWeb.setString(0, value);
		propWeb.setString(1, seccion);
		propWeb.setString(2, servicio);
		return propWeb.executeUpdate();

	}
	
}