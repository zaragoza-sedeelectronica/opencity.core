/* Copyright 2009 The Revere Group
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.genericdao.dao.jpa;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.ciudadesabiertas.utils.DifferentSQLforDatabases;
import org.ciudadesabiertas.utils.DistinctSearch;
import org.ciudadesabiertas.utils.GroupBySearch;
import org.ciudadesabiertas.utils.Util;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.googlecode.genericdao.dao.DAOUtil;
import com.googlecode.genericdao.search.ExampleOptions;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.ISearch;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;

/**
 * Implementation of <code>GenericDAO</code> using Hibernate.
 * The SessionFactory property is annotated for automatic resource injection.
 * 
 * @author dwolverton
 * 
 * @param <T>
 *            The type of the domain object for which this instance is to be
 *            used.
 * @param <ID>
 *            The type of the id of the domain object for which this instance is
 *            to be used.
 */
@SuppressWarnings("unchecked")
@Service
public class GenericDAOImpl<T, ID extends Serializable> extends
		JPABaseDAO implements GenericDAO<T, ID> {
	private static final Logger logger = LoggerFactory.getLogger(GenericDAOImpl.class);
	private boolean persistir = true;
	
	protected Class<T> persistentClass = (Class<T>) DAOUtil.getTypeArguments(GenericDAOImpl.class, this.getClass()).get(0);

	
	public Session getSession() {
		return em().unwrap(Session.class);
	}
	
	public int count(ISearch search) {
		if (search == null)
			search = new Search();
		return _count(persistentClass, search);
	}

	public T find(ID id) {
		return _find(persistentClass, id);
	}

	public T[] find(ID... ids) {
		return _find(persistentClass, ids);
	}

	public List<T> findAll() {
		return _all(persistentClass);
	}

	public void flush() {
		_flush();
	}

	public T getReference(ID id) {
		return _getReference(persistentClass, id);
	}

	public T[] getReferences(ID... ids) {
		return _getReferences(persistentClass, ids);
	}

	public boolean isAttached(T entity) {
		return _contains(entity);
	}

	public void refresh(T... entities) {
		_refresh(entities);
	}

	public boolean remove(T entity) {
		return _removeEntity(entity);
	}

	public void remove(T... entities) {
		_removeEntities((Object[]) entities);
	}

	public boolean removeById(ID id) {
		return _removeById(persistentClass, id);
	}

	public void removeByIds(ID... ids) {
		_removeByIds(persistentClass, ids);
	}

	public void crear(T entity) {
		if (this.isPersistir()) {
			_merge(entity);
		}
		if (this.getSolr().indizar(entity)) {
			this.getSolr().save(entity);
			this.getSolr().commit();
		}
	}
	public void eliminarDeIndice(T entity) {
		if (this.getSolr().indizar(entity)) {
			this.getSolr().delete(entity);
			this.getSolr().commit();
		}
	}
	public void indizar(T entity) {
		if (this.getSolr().indizar(entity)) {
			this.getSolr().update(entity);
			this.getSolr().commit();
		}
	}
	public T merge(T entity) {
		return _merge(entity);
	}

	public T[] merge(T... entities) {
		return _merge(persistentClass, entities);
	}

	public void persist(T... entities) {
		_persist(entities);
	}
	
	public T save(T entity) {
		return _persistOrMerge(entity);
	}

	public T[] save(T... entities) {
		return _persistOrMerge(persistentClass, entities);
	}

	public <RT> List<RT> search(ISearch search) {
		if (search == null)
			return (List<RT>) findAll();
		Query ps = this.em().createNativeQuery("ALTER SESSION SET NLS_COMP='LINGUISTIC' NLS_SORT='BINARY_AI'");
        ps.executeUpdate();
		return _search(persistentClass, search);
	}

	public <RT> SearchResult<RT> searchAndCount(ISearch search) {
		if (search == null || search.showAll()) {
			SearchResult<RT> result = new SearchResult<RT>();
			result.setResult((List<RT>) findAll());
			result.setTotalCount(result.getResult().size());
			return result;
		}
		Query ps = this.em().createNativeQuery("ALTER SESSION SET NLS_COMP='LINGUISTIC' NLS_SORT='BINARY_AI'");
        ps.executeUpdate();
		return _searchAndCount(persistentClass, search);
	}

	public <RT> RT searchUnique(ISearch search) {
		return (RT) _searchUnique(persistentClass, search);
	}

	public Filter getFilterFromExample(T example) {
		return _getFilterFromExample(example);
	}

	public Filter getFilterFromExample(T example, ExampleOptions options) {
		return _getFilterFromExample(example, options);
	}
	public boolean isPersistir() {
		return persistir;
	}

	public void setPersistir(boolean persistir) {
		this.persistir = persistir;
	}
	
	
	public  List<Object> groupBy(String key,Class<T> type, GroupBySearch search, int page, int pageSize) throws Exception {

		List<Object> result = new ArrayList<Object>();
		
		if (key==null || "".equals(key)) {
			key = Util.extractKeyFromModelClass(type);
		}
		String nameClass=Util.extractNameFromModelClass(type);
		
		String query ="";
		
		
		if (page>=0)
		{
			
			try 
			{
				Query queryObj = null;
//				if (this.em().getKeys().contains(key))
//				{
					
					//Controlamos problemas en el where por distintas bbdd
					if (StringUtils.isNotEmpty(search.getWhere()))
					{
						search.setWhere(DifferentSQLforDatabases.transForm(search.getWhere()));
					}
					
					if (search.getWhere().contains("like"))
					{
						String finalWhere=DifferentSQLforDatabases.controlLikeConditions(search.getWhere());						
						search.setWhere(finalWhere);
					}
					
					if (search.getHaving().contains("like"))
					{							
						String finalHaving=DifferentSQLforDatabases.controlLikeConditions(search.getHaving());
						search.setHaving(finalHaving);
					}
					
					query = search.createQuery(nameClass);
					
					if (query.contains("*"))
					{
						logger.info("Group By Query With *: "+query);
						//Control de comodines en groupby
						query = query.replace("*", "%");
					}
					
					logger.info("Group By Query: "+query);
											
					queryObj = this.em().createQuery(query);						
//				}else {						
//					
//					//Controlamos problemas en el where por distintas bbdd
//					if (StringUtils.isNotEmpty(search.getWhere()))
//					{
//						search.setWhere(DifferentSQLforDatabases.transForm(search.getWhere()));
//					}
//					
//					if (search.getWhere().contains("like"))
//					{	
//						String finalWhere=DifferentSQLforDatabases.controlLikeConditions(search.getWhere());
//						search.setWhere(finalWhere);
//					}
//					
//					if (search.getHaving().contains("like"))
//					{	
//						String finalHaving=DifferentSQLforDatabases.controlLikeConditions(search.getHaving());
//						search.setHaving(finalHaving);
//					}
//					
//					
//					query = search.createQuery(nameClass);
//					if (query.contains("*"))
//					{
//						logger.info("Group By Query With *: "+query);
//						//Control de comodines en groupby
//						query = query.replace("*", "%");
//					}
//					
//					logger.info("Group By Query: "+query);
//					
//					queryObj = this.em().createQuery(query);	
//				}
					
				queryObj.setFirstResult(page * pageSize);
				queryObj.setMaxResults(pageSize);		
							
				result = (List<Object>) queryObj.getResultList();
								
							
					
			}catch (Exception e1) {
				String msg="executeSelect(query) [Hibernate Exception]:" + e1.getMessage() + " [query Fail:"+query+"]";
				logger.error(msg,e1);
				throw new Exception("Wrong parameters in the petition");			
			}
		
		}
		return result;		

	}
	public long rowCountGroupBy(String key,Class<T> type, GroupBySearch search) throws Exception {
		
		int size=0;	
		
		if (key==null || "".equals(key)) {
			key = Util.extractKeyFromModelClass(type);
		}
		
		String nameClass=Util.extractNameFromModelClass(type);
		
		
		Query queryObj = null;
//		if (multipleSessionFactory.getKeys().contains(key))
//		{
			
			//Controlamos problemas en el where por distintas bbdd
			if (Util.validValue(search.getWhere()))
			{
				search.setWhere(DifferentSQLforDatabases.transForm(search.getWhere()));
			}
			
			if (search.getWhere().contains("like"))
			{					
				String finalWhere=DifferentSQLforDatabases.controlLikeConditions(search.getWhere());
				search.setWhere(finalWhere);
			}
			
			if (search.getHaving().contains("like"))
			{	
				String finalHaving=DifferentSQLforDatabases.controlLikeConditions(search.getHaving());
				search.setHaving(finalHaving);
			}
			
			String query = search.createQuery(nameClass);
			
			
			if (query.contains("*"))
			{
				logger.info("Row Count Group By Query With *: "+query);
				//Control de comodines en groupby
				query = query.replace("*", "%");
			}
			
			
			query="Select count(*) "+query.substring(query.indexOf("from"));
			
			logger.info("Group By Count Query: "+query);
							
			queryObj  = this.em().createQuery(query);
//		}else {
//			
//			//Controlamos problemas en el where por distintas bbdd
//			if (Util.validValue(search.getWhere()))
//			{
//				search.setWhere(DifferentSQLforDatabases.transForm(search.getWhere()));
//			}
//			
//			
//			if (search.getWhere().contains("like"))
//			{	
//				String finalWhere=DifferentSQLforDatabases.controlLikeConditions(search.getWhere());
//				search.setWhere(finalWhere);
//			}
//			
//			if (search.getHaving().contains("like"))
//			{	
//				String finalHaving=DifferentSQLforDatabases.controlLikeConditions(search.getHaving());
//				search.setHaving(finalHaving);
//			}
//			
//			
//			String query = search.createQuery(nameClass);
//			
//			if (query.contains("*"))
//			{
//				logger.info("Row Count Group By Query With *: "+query);
//				//Control de comodines en groupby
//				query = query.replace("*", "%");
//			}
//			
//			
//			query="Select count(*) "+query.substring(query.indexOf("from"));
//			
//			logger.info("Group By Count Query: "+query);
//			
//			
//			
//			queryObj  = this.em().createQuery(query);
//		}
//		
		if (queryObj.getResultList().size()>0)
		{
			size = queryObj.getResultList().size();
		}
		
		return size;
	}	
	public long rowCountDistinct(String key,Class<T> type, DistinctSearch search) throws Exception {
    	logger.debug("[rowCountGroupBy][key:"+key+"][type:"+type+"][search:"+search+"]");
    	long size=0;	
//		Session opennedSession = null;
		
		if (key==null || "".equals(key)) {
			key = Util.extractKeyFromModelClass(type);
		}
		
		String nameClass=Util.extractNameFromModelClass(type);
						
		
		Query queryObj = null;
//		if (multipleSessionFactory.getKeys().contains(key))
//		{
//			log.debug(LiteralConstants.TXT_CUSTOM_CONECT);
//			
//			String query =search.createRowCountQuery(nameClass);
//			
//			log.info("Distinct Count Query: "+query);			
//			
//			opennedSession = multipleSessionFactory.getFactories().get(key).openSession();				
//			queryObj  = opennedSession.createQuery(query);
//		}else {
			
			
			String query =search.createRowCountQuery(nameClass);
			
			logger.info("Distinct Count Query: "+query);
			
			queryObj  = this.em().createQuery(query);
//		}
		
		if (queryObj.getResultList().size()>0)
		{
			size=(Long)queryObj.getResultList().get(0);
		}
		
//		if (opennedSession!=null)
//		{							
//			opennedSession.close();
//		}	
		
		return size;
    }
	public List<?> distinctSearch(String key,Class<T> type,DistinctSearch search, int page, int pageSize) throws Exception {
    	logger.debug("[search][key:"+key+"][search:"+search+"]");
    	//Restamos 1 a la pagina, porque la primera ahora es 1
    	page--;
    	List<Object> result = new ArrayList<Object>();
		Session opennedSession = null;
		
		if (key==null || "".equals(key)) {
			key = Util.extractKeyFromModelClass(type);
		}
		String nameClass=Util.extractNameFromModelClass(type);
		
		String query ="";
				
		
		if (page>=0)
		{
			
			try 
			{
				Query queryObj = null;
//				if (multipleSessionFactory.getKeys().contains(key))
//				{
//					log.debug("[distinctSearch]" +LiteralConstants.TXT_CUSTOM_CONECT);				
//					opennedSession = multipleSessionFactory.getFactories().get(key).openSession();
//					
//					query = search.createQuery(nameClass);
//					
//					log.info("[distinctSearch] [Distinct query: "+query+"]");
//											
//					queryObj = this.em().createQuery(query);						
//				}else {
					
					query = search.createQuery(nameClass);				
					
					logger.info("[distinctSearch] [Distinct query: "+query+"]");
					
					queryObj = this.em().createQuery(query);	
//				}
					
				queryObj.setFirstResult(page * pageSize);
				queryObj.setMaxResults(pageSize);		
							
				result =   (List<Object>) queryObj.getResultList();
								
//				if (opennedSession!=null)
//				{
//					opennedSession.close();
//				}					
					
			} catch (Exception e1)
			{
				String msg="executeSelect(query) [Hibernate Exception]:" + e1.getMessage() + " [query Fail:"+query+"]";
//				log.error(msg,e1);
				throw new Exception("Wrong parameters in the petition");			
			}
//			catch (Exception e1)
//			{			
//				if (e1.getCause().toString().contains("SQLGrammarException"))
//				{
//					throw Exception("Wrong parameters in the query");
//				}
//				String msg="executeSelect(query) [Hibernate Exception]:" + e1.getMessage() + " [query Fail:"+query+"]";
////				log.error(msg,e1);
//				throw new Exception(Constants.INTERNAL_ERROR);			
//			}		
		
		}
		return result;		
    }
}