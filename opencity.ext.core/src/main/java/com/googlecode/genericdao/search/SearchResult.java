package com.googlecode.genericdao.search;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.ext.search.SearchCondition;
import org.apache.cxf.jaxrs.ext.search.SearchParseException;
import org.apache.cxf.jaxrs.ext.search.fiql.FiqlParser;
import org.apache.cxf.jaxrs.ext.search.jpa.JPACriteriaQueryVisitor;
import org.apache.solr.client.solrj.response.FacetField;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.ciudadesabiertas.utils.Result;
import org.ciudadesabiertas.utils.Util;
import org.sede.core.dao.SearchFiql;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.solr.Faceta;
import org.sede.core.rest.solr.ValorFaceta;
import org.sede.core.utils.Funciones;
import org.sede.servicio.ModelAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.*;
import java.util.Map.Entry;

@XmlRootElement(name = ModelAttr.RESULTADO)
public class SearchResult<T> implements Serializable {

	private static final Logger logger = LoggerFactory.getLogger(SearchResult.class);

	private static final long serialVersionUID = 1L;

	protected int totalCount = -1;
	protected int start = 0;
	protected int rows = 0;
	protected Map<String, Object> propiedades = null;

	protected List<Faceta> facetas = null;

	protected List<T> result;

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> results) {
		this.result = results;
	}

	public Map<String, Object> getPropiedades() {
		return propiedades;
	}

	public void setPropiedades(Map<String, Object> propiedades) {
		this.propiedades = propiedades;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public List<Faceta> getFacetas() {
		return facetas;
	}

	public void setFacetas(List<Faceta> facetas) {
		this.facetas = facetas;
	}

	@Override
	public String toString() {
		return "SearchResult [totalCount=" + totalCount + ", start=" + start
						+ ", rows=" + rows + ", propiedades=" + propiedades
						+ ", facetas=" + facetas + ", result=" + result + "]";
	}

	@SuppressWarnings("rawtypes")
	public void convertirAFaceta(List<FacetField> facetFields, Map<String, List<Faceta>> facetasOrdenadas) {
		if (facetFields != null && !facetFields.isEmpty()) {
			this.setFacetas(new ArrayList<Faceta>());

			Iterator<Entry<String, List<Faceta>>> it = facetasOrdenadas.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry pair = it.next();

				for (FacetField faceta : facetFields) {
					if (pair.getKey().equals(faceta.getName()) && faceta.getValues() != null) {
						Faceta f = new Faceta();
						f.setName(faceta.getName());
						f.setValues(new ArrayList<ValorFaceta>());
						for (Count c : faceta.getValues()) {
							ValorFaceta valor = new ValorFaceta();
							valor.setName(c.getName());
							valor.setCount(c.getCount());
							f.getValues().add(valor);
						}
						this.getFacetas().add(f);
					}
				}


				it.remove(); // avoids a ConcurrentModificationException
			}
		}
	}

	public String eliminarQueryFilter(HttpServletRequest request) {
		String fqPart = String.format("%s=%s", CheckeoParametros.PARAMFQ, getFQParamsOrEmpty(request));
		return "?" + fqPart;
	}

	private static String getFQParamsOrEmpty(HttpServletRequest request) {
		try {
			return URLEncoder.encode(request.getParameter(CheckeoParametros.PARAMFQ), "UTF-8");
		} catch (Exception e) {
			return StringUtils.EMPTY;
		}
	}

	public String getEnlacesReutilizacion(HttpServletRequest request) {
		return getEnlacesReutilizacion(request, null);
	}
	public String getEnlacesReutilizacion(HttpServletRequest request, String... formato) {
		StringBuilder retorno = new StringBuilder();

		String uri;
		String querystring = "";
		if (request.getRequestURI().lastIndexOf('/') + 1 == request.getRequestURI().length()) {
			// Eliminamos la ultima /
			uri = request.getRequestURI().substring(0, request.getRequestURI().length() - 1);
		} else {
			uri = request.getRequestURI();
		}
		if (request.getQueryString() != null) {
			querystring = "?" + request.getQueryString();
		}
		if (formato == null) {
			retorno.append("<li class=\"hidden-xs\"> | <a href=\"" + uri + ".solrjson" + querystring + "\">JSON</a></li>");
			retorno.append("<li class=\"hidden-xs\"> | <a href=\"" + uri + ".solrxml" + querystring + "\">XML</a></li>");
			retorno.append("<li class=\"hidden-xs\"> | <a href=\"" + uri + ".csv" + querystring + "\">CSV</a></li>");
		} else {
			for (String f : formato) {
				retorno.append("<li class=\"hidden-xs\"> | <a href=\"" + uri + "." + f + querystring + "\">" + f.toUpperCase() + "</a></li>");	
			}
		}
		return retorno.toString();
	}

	public String getPaginacion() {
		return getPaginacion(Funciones.getRequest());
	}
	@SuppressWarnings("rawtypes")
	public String getPaginacion(HttpServletRequest request) {
		try {
			StringBuilder xhtm = new StringBuilder();
			if (this.totalCount > this.rows) {

				xhtm.append("<nav class=\"pull-right\"><ul class=\"pagination\">");
				long paginas = this.totalCount / this.rows;
				String pageLink = request.getRequestURI() + "?";
				Map map = request.getParameterMap();
				for (Object key : map.keySet()) {
					String keyStr = (String) key;
					if (!"start".equals(keyStr)) {
						String[] value = (String[]) map.get(keyStr);
						for (String val : value) {
							pageLink = pageLink + "&" + keyStr + "=" + URLEncoder.encode(val, CharEncoding.UTF_8);
						}
					}
				}


				if (this.start - this.rows >= 0) {
					xhtm.append("<li><a class=\"btn btn-primary btn-mini\" href=\"" + pageLink + "&amp;start=" + (this.start - this.rows) + "\"><span aria-hidden=\"true\">&laquo;</span></a></li>");
				}

				int paginaActual = this.start / this.rows;
				int pos;
				int sum;
				if (paginaActual <= 5) {
					pos = 0;
					sum = 10;
				} else {
					pos = paginaActual - 5;
					sum = paginaActual + 4;
				}

				while (pos < sum && pos <= paginas) {
					long siguiente = (this.rows * pos);
					if (pos == paginaActual) {
						xhtm.append("<li class=\"active\"><a href=\"#\">" + (pos + 1) + "</a></li>");
					} else {
						xhtm.append("<li><a class=\"btn btn-primary btn-mini\" href=\"" + pageLink + "&amp;start=" + siguiente + "\">" + (pos + 1) + "</a></li>");
					}
					pos++;
				}

				if (this.start + this.rows <= this.totalCount) {
					xhtm.append("<li><a class=\"btn btn-primary btn-mini\" href=\"" + pageLink + "&amp;start=" + (this.start + this.rows) + "\"><span aria-hidden=\"true\">&raquo;</span></a></li>");
				}
				xhtm.append("</ul></nav>");
			}

			return xhtm.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	public String getPaginacion(HttpServletRequest request, String anchor) {
		if ((anchor == null) || anchor.isEmpty()) {
			return getPaginacion(request);
		}
		try {
			StringBuilder xhtm = new StringBuilder();
			if (this.totalCount > this.rows) {
				xhtm.append("<nav class=\"pull-right\"><ul class=\"pagination\">");
				long paginas = this.totalCount / this.rows;
				String pageLink = request.getRequestURI() + "?";
				@SuppressWarnings("unchecked")
				Map<String, String[]> map = request.getParameterMap();
				for (Object key: map.keySet())
				{
					String keyStr = (String)key;
					if (!"start".equals(keyStr)) {
						String[] value = map.get(keyStr);
						for (String val : value) {
							pageLink = pageLink + "&" + keyStr + "=" + URLEncoder.encode(val, CharEncoding.UTF_8);
						}
					}
				}

				if (this.start - this.rows >= 0) {
					xhtm.append("<li><a class=\"btn btn-primary btn-mini\" href=\"" + pageLink + "&amp;start=" + (this.start - this.rows) + "#" + anchor + "\"><span aria-hidden=\"true\">&laquo;</span></a></li>");
				}

				int paginaActual = this.start / this.rows;
				int pos;
				int sum;
				if (paginaActual <= 5) {
					pos = 0;
					sum = 10;
				} else {
					pos = paginaActual - 5;
					sum = paginaActual + 4;
				}

				while (pos < sum && pos <= paginas) {
					long siguiente = (this.rows * pos);
					if (pos == paginaActual) {
						xhtm.append("<li class=\"active\"><a href=\"#" + anchor + "\">" + (pos + 1) + "</a></li>");
					} else {
						xhtm.append("<li><a class=\"btn btn-primary btn-mini\" href=\"" + pageLink + "&amp;start=" + siguiente + "#" + anchor + "\">" + (pos + 1) + "</a></li>");
					}
					pos++;
				}

				if (this.start + this.rows <= this.totalCount) {
					xhtm.append("<li><a class=\"btn btn-primary btn-mini\" href=\"" + pageLink + "&amp;start=" + (this.start + this.rows) + "#" + anchor + "\"><span aria-hidden=\"true\">&raquo;</span></a></li>");
				}
				xhtm.append("</ul></nav>");
			}

			return xhtm.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	@SuppressWarnings("rawtypes")
	public String getMetaPaginacion(HttpServletRequest request) {
		try {
			StringBuilder xhtm = new StringBuilder();
			if (this.totalCount > this.rows) {

				String pageLink = request.getRequestURI() + "?";
				Map map = request.getParameterMap();
				for (Object key : map.keySet()) {
					String keyStr = (String) key;
					if (!"start".equals(keyStr)) {
						String[] value = (String[]) map.get(keyStr);
						for (String val : value) {
							pageLink = pageLink + "&" + keyStr + "=" + URLEncoder.encode(val, CharEncoding.UTF_8);
						}
					}
				}

				if (this.start - this.rows >= 0) {
					xhtm.append("<link rel=\"prev\" href=\"" + pageLink + "&amp;start=" + (this.start - this.rows) + "\"/>");
				}

				if (this.start + this.rows <= this.totalCount) {
					xhtm.append("<link rel=\"next\" href=\"" + pageLink + "&amp;start=" + (this.start + this.rows) + "\"/>");
				}

			}

			return xhtm.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void consultar(SearchFiql search, Class clase, EntityManager em) throws SearchParseException {

		SearchCondition<T> sc = new FiqlParser<T>(clase, null).parse(search.getSearchExpression());
		JPACriteriaQueryVisitor<T, Long> jpa = new JPACriteriaQueryVisitor(em, clase, Long.class);
		sc.accept(jpa);
		int count = jpa.count().intValue();

		JPACriteriaQueryVisitor<T, T> visitor = new JPACriteriaQueryVisitor<T, T>(em, clase, clase);
		sc.accept(visitor);


		EntityManagerFactory emf = em.getEntityManagerFactory();
		Metamodel metamodel = emf.getMetamodel();
		EntityType<T> pClass = metamodel.entity(clase);
		List<SingularAttribute<T, ?>> selections = new LinkedList<SingularAttribute<T, ?>>();
		selections.add((SingularAttribute<T, ?>) pClass.getSingularAttribute("id"));
		selections.add((SingularAttribute<T, ?>) pClass.getSingularAttribute("title"));
		visitor.selectConstruct(selections);

		TypedQuery<T> typedQuery = visitor.getTypedQuery();

		List listado = typedQuery.setMaxResults(search.getRows()).setFirstResult(search.getStart()).getResultList();

		this.setTotalCount(count);
		this.setRows(search.getRows());
		this.setStart(search.getStart());
		this.setResult(listado);
	}

	public Result<T> asResult() {
		Result<T> respuesta = new Result<>();
		
		Map<String, String> pageMetadataCalculation = Util.pageMetadataCalculation(Funciones.getRequest(), this.getTotalCount(), this.getRows());
		
		respuesta.setRecords(this.getResult());
		
		respuesta.setPage((this.getStart() / this.getRows()) + 1);
		respuesta.setPageSize(this.getRows());
		respuesta.setTotalRecords(this.getTotalCount());
		respuesta.setPageRecords(this.getResult().size());
		respuesta.setStatus(HttpStatus.OK.value());
		respuesta.setResponseDate(new Date());
		respuesta.setFirst(pageMetadataCalculation.get("first"));
		respuesta.setLast(pageMetadataCalculation.get("last"));
		respuesta.setNext(pageMetadataCalculation.get("next"));
		respuesta.setPrev(pageMetadataCalculation.get("prev"));
		respuesta.setSelf(pageMetadataCalculation.get("self"));
		respuesta.setContentMD5(Util.generateHash(this.getResult().toString()));
		
		return respuesta;
	}
}
