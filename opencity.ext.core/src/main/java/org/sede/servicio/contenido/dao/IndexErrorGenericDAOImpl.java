package org.sede.servicio.contenido.dao;

import com.googlecode.genericdao.dao.jpa.GenericDAOImpl;

import org.apache.commons.lang3.StringUtils;
import org.sede.core.anotaciones.Esquema;
import org.sede.core.dao.JPAIgnoreTraversableResolver;
import org.sede.core.utils.ConvertDate;
import org.sede.servicio.contenido.entity.IndexError;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Table;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

@Repository
@Transactional(Esquema.TMINTRA)
public class IndexErrorGenericDAOImpl extends GenericDAOImpl <IndexError, String> implements IndexErrorGenericDAO {
	
	@PersistenceContext(unitName=Esquema.INTRA)
	public void setEntityManager(EntityManager entityManager) {
		this.setEm(entityManager);
	}

	public Set<ConstraintViolation<Object>> validar(Object registro) {
		ValidatorFactory factory = Validation.byDefaultProvider().configure().traversableResolver(new JPAIgnoreTraversableResolver()).buildValidatorFactory();
		Validator validator = factory.getValidator();
		return validator.validate(registro);
	}
	
	public int updateRevisado(String id, String value) {
		
		IndexError reg = this.find(id); 
		
		Query propWeb = this.em().createNativeQuery("update " + persistentClass.getAnnotation(Table.class).name() 
				+ " set revisado=?, fecha_revisado=? where rowid=?");
		
		propWeb.setParameter(1, value);
		propWeb.setParameter(2, new Date());
		propWeb.setParameter(3, id);
		int retorno = propWeb.executeUpdate();
		
		propWeb = this.em().createNativeQuery("insert into intra.ELASTIC_UPDATE_URLS(url) values (?)");
		propWeb.setParameter(1, reg.getUrl());
		propWeb.executeUpdate();
		
		if (StringUtils.isNotEmpty(reg.getParent())) {
			Query q2 = em().createNativeQuery("select count(*) from intra.ELASTIC_UPDATE_URLS where url = ? ");
			BigDecimal resultSQL2 = (BigDecimal) q2.setParameter(1, reg.getParent()).getSingleResult();
		
			if(resultSQL2 != null && resultSQL2.intValue() <= 0) {
				propWeb = this.em().createNativeQuery("insert into intra.ELASTIC_UPDATE_URLS(url) values (?)");
				propWeb.setParameter(1, reg.getParent());
				propWeb.executeUpdate();
			}
		}
		
			
			
		
		return retorno;
	}

	@Override
	public String getStatistics() {
		StringBuilder data=new StringBuilder();
		try {
			Query query = em().createNativeQuery("select to_date(to_char(el.GCZ_FECHAALTA,'dd-mm-yyyy'),'dd-mm-yyyy'), nvl(errores.numeroalta,0), nvl(revisado.numerorevisado,0) " + 
					"from elastic_index_log_error el," + 
					"(select to_char(GCZ_FECHAALTA,'dd-mm-yyyy') as alta, count(*) as numeroalta from elastic_index_log_error group by to_char(GCZ_FECHAALTA,'dd-mm-yyyy'))  errores," + 
					"(select to_char(fecha_revisado,'dd-mm-yyyy') as revisado, count(*) as numerorevisado from elastic_index_log_error where fecha_revisado is not null group by to_char(fecha_revisado,'dd-mm-yyyy')) revisado " + 
					"where errores.alta = to_char(el.GCZ_FECHAALTA,'dd-mm-yyyy') and to_char(el.GCZ_FECHAALTA,'dd-mm-yyyy')=revisado.revisado(+) " +
					" and el.GCZ_FECHAALTA>add_months(sysdate,-1) "+
					"group by to_date(to_char(el.GCZ_FECHAALTA,'dd-mm-yyyy'),'dd-mm-yyyy'),errores.numeroalta,revisado.numerorevisado " + 
					"order by 1");
			@SuppressWarnings("unchecked")
			List<Object> resultado = query.getResultList();
			Iterator<Object> iterador = resultado.iterator();
			data.append("var data = [['día', 'errores', 'correcciones'],");
			boolean primero = true;
			while (iterador.hasNext()) {
				Object[] row = (Object[])iterador.next();
				if (!primero) {
					data.append(",");
				}
				primero = false;
				data.append("['" + ConvertDate.date2String((Date)row[0], ConvertDate.DATE_FORMAT) + "',  " + (BigDecimal)row[1] + ", " + (BigDecimal)row[2] + "]");
                
			}
			data.append("];");
			return data.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean updateStatusSinIndizar(String id) {
		Query propWeb = this.em().createNativeQuery("update " + persistentClass.getAnnotation(Table.class).name() 
				+ " set revisado=?, fecha_revisado=? where rowid=?");
		propWeb.setParameter(1, "S");
		propWeb.setParameter(2, new Date());
		propWeb.setParameter(3, id);
		return propWeb.executeUpdate() > 0;
	}

	@Override
	public String getStatisticsByCode() {
		StringBuilder data=new StringBuilder();
		try {
			Query query = em().createNativeQuery("select status, count(*) from elastic_index_log_error where revisado='N' group by status");
			@SuppressWarnings("unchecked")
			List<Object> resultado = query.getResultList();
			Iterator<Object> iterador = resultado.iterator();
			data.append("var dataCodes = [['Code', 'numero'],");
			boolean primero = true;
			while (iterador.hasNext()) {
				Object[] row = (Object[])iterador.next();
				if (!primero) {
					data.append(",");
				}
				primero = false;
				data.append("['" + (BigDecimal)row[0] + "',  " + (BigDecimal)row[1] + "]");
                
			}
			data.append("];");
			return data.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
}