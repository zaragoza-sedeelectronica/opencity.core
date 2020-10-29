package org.sede.core.dao;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Embeddable;
import javax.persistence.Transient;
import javax.servlet.http.HttpServletRequest;

import org.apache.cxf.jaxrs.ext.search.AndSearchCondition;
import org.apache.cxf.jaxrs.ext.search.OrSearchCondition;
import org.apache.cxf.jaxrs.ext.search.PrimitiveSearchCondition;
import org.apache.cxf.jaxrs.ext.search.SearchCondition;
import org.apache.cxf.jaxrs.ext.search.SearchParseException;
import org.apache.cxf.jaxrs.ext.search.SimpleSearchCondition;
import org.apache.cxf.jaxrs.ext.search.fiql.FiqlParser;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.Rel;
import org.sede.core.geo.Geometria;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.utils.Busqueda;
import org.sede.core.utils.Funciones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.googlecode.genericdao.search.BaseSearchProcessor;
import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;

public class SearchFiql {
	private static final Logger logger = LoggerFactory
			.getLogger(SearchFiql.class);
	private boolean ignoreQueryString = false;
	private Class<?> clase;
	private Integer start = Integer.parseInt(CheckeoParametros.START);
	private Integer rows = Integer.parseInt(CheckeoParametros.ROWS);
	
	private boolean showAll = false;
	
	private String sort = "";
	private String point = "";
	private String fl = "";
	private Integer distance = Integer.parseInt(CheckeoParametros.DISTANCE);
	
	Map<String, String> contextProperties = null;
	
	private String searchExpression = "";
	
	private String[] excludedFields = new String[]{};
	
	public SearchFiql() {
		super();
	}
	
	public SearchFiql(String searchExpression, String sort,
			Map<String, String> contextProperties, 
			String[] excludedFields) {
		super();
		this.ignoreQueryString = true;
		this.sort = sort;
		this.contextProperties = contextProperties;
		this.searchExpression = searchExpression;
		this.excludedFields = excludedFields;
	}

	public void setIgnoreQueryString(Boolean ignoreQueryString) {
		this.ignoreQueryString = ignoreQueryString;
	}
	
	public String getSearchExpression() {
		return getSearchExpression(false);
	}
	public String getSearchExpressionElastic() {
		return getSearchExpression(true);
	}
	public String getSearchExpression(boolean elastic) {
		
		if ("".equals(this.searchExpression) && !ignoreQueryString) {
		
			ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletRequest req = sra.getRequest();
			StringBuilder query = new StringBuilder();
			@SuppressWarnings("unchecked")
			Enumeration<String> parameterNames = req.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				if (CheckeoParametros.PARAMQUERY.equals(paramName)) {
					if (elastic) {
						query.append(convertirEspaciosAAND(req.getParameter(paramName)) + " AND ");
					} else {
						query.append(req.getParameter(paramName) + ";");
					}
				} else if (CheckeoParametros.PARAMDISTANCEQUERYSOLR.equals(paramName)) {
					this.distance = Integer.parseInt(req.getParameter(paramName));
				} else if (CheckeoParametros.PARAMSTART.equals(paramName)) {
					this.start = Integer.parseInt(req.getParameter(paramName));
				} else if (CheckeoParametros.PAGESIZE.equals(paramName) || CheckeoParametros.PAGE.equals(paramName)) {
					Integer page = req.getParameter(CheckeoParametros.PAGE) == null 
							? CheckeoParametros.defaultPage : Integer.parseInt(req.getParameter(CheckeoParametros.PAGE));
					Integer pageSize = Integer.parseInt(req.getParameter(CheckeoParametros.PAGESIZE) == null 
							? CheckeoParametros.ROWS : req.getParameter(CheckeoParametros.PAGESIZE));
					
					this.rows = pageSize;
					this.start = (page - 1) * pageSize;
					
				} else if (CheckeoParametros.PARAMSORT.equals(paramName)) {
					String orden = req.getParameter(paramName);
					if (orden.indexOf("-") >= 0) {
						String[] ordenes = orden.split(",");
						StringBuilder res = new StringBuilder();
						for (String o : ordenes) {
							if (o.indexOf("-") >= 0) {
								o = o.replace("-", "") + " desc";
							}
							res.append(o + ",");
						}
						orden = res.toString();
					}
					this.sort = orden;
				} else if (CheckeoParametros.PARAMSHOWALL.equals(paramName)) {
					this.showAll = true;
				} else if (CheckeoParametros.PARAMROWS.equals(paramName)) {
					this.rows = Integer.parseInt(req.getParameter(paramName));
				} else if (CheckeoParametros.PARAMPUNTOQUERYSOLR.equals(paramName)) {
					this.point = req.getParameter(paramName);
				} else if (CheckeoParametros.PARAMFILTROCAMPOS.equals(paramName)) {
					this.fl = req.getParameter(paramName);
				} else if (!CheckeoParametros.PARAMRESPUESTA.equals(paramName)
						&& !CheckeoParametros.PARAMFQ.equals(paramName)
						&& !CheckeoParametros.PARAMREMOVEPROPERTIES.equals(paramName)
						&& !CheckeoParametros.DEBUG.equals(paramName)
						&& !CheckeoParametros.TEST.equals(paramName)
						&& !CheckeoParametros.LOCALE.equals(paramName)

						// Parametros que recibimos al publicar enlaces en FB o Google 
						&& !"fbclid".equals(paramName)
						&& !"gclid".equals(paramName)
						
						&& !CheckeoParametros.GEORSS_ICON.equals(paramName)
						&& !CheckeoParametros.REFRESHPARAMETER.equals(paramName)
						&& !CheckeoParametros.RESULTSONLY.equals(paramName)
						&& !CheckeoParametros.PAGE.equals(paramName)
						&& !CheckeoParametros.PAGESIZE.equals(paramName)
						&& !"_".equals(paramName)
						&& !CheckeoParametros.PARAMSRS.equals(paramName)
						&& !isExcluded(paramName)) {
					String[] paramValues = req.getParameterValues(paramName);
					if (paramValues.length > 1) {
						query.append("(");
					}
					boolean tiene = false;
					for (int i = 0; i < paramValues.length; i++) {
						String paramValue = paramValues[i];
						if (paramValue.length() > 0) {
							if (tiene) {
								query.append(",");
							}
							tiene = true;
							if (elastic) {
								query.append(addParameterElastic(paramName, paramValue));
							} else {
								query.append(addParameter(paramName, paramValue));
							}
						}
					}
					if (paramValues.length > 1) {
						query.append(")");
					}
					if (tiene) {
						if (elastic) {
							query.append(" AND ");
						} else {
							query.append(";");
						}
					}
				}
			}
			if (query.length() > 0) {
				if (elastic) {
					query.delete(query.lastIndexOf(" AND "), query.length());
				} else {
					query.deleteCharAt(query.length() - 1);	
				}
			}
			logger.info((elastic ? "ElasticSearch: " : "FIQL: ") + query.toString());
			searchExpression = query.toString();
			return query.toString();
		} else {
			return this.searchExpression;
		}
	}

	
	private boolean isExcluded(String paramName) {
		for (String s : excludedFields) {
			if (paramName.equals(s)) {
				return true;
			}
		}
		return false;
	}

	private String addParameter(String paramName, String paramValue) {
		String operador = "==";
		String retorno = paramName + operador + paramValue;
		if (paramName.indexOf("Contains") > 0) {
			paramName = paramName.replace("Contains", "");
			paramValue = paramValue + "*";
			retorno = paramName + operador + paramValue;
		}
		if (paramName.indexOf("Till") > 0) {
			operador = "=lt=";
			paramName = paramName.replace("Till", "");
			retorno = paramName + operador + paramValue;
		}
		if (paramName.indexOf("From") > 0) {
			operador = "=ge=";
			paramName = paramName.replace("From", "");
			retorno = paramName + operador + paramValue;
		}

		if (paramName.indexOf("InList") > 0) {
			StringBuilder consulta = new StringBuilder();
			String[] values = paramValue.split(",");
			paramName = paramName.replace("InList", "");
			if (values.length > 0) {
				consulta.append("(");
				for (int i = 0; i < values.length; i++) {
					if (i > 0) {
						consulta.append(",");
					}
					consulta.append(paramName + operador + values[i].trim());
				}
				consulta.append(")");
			}
			retorno = consulta.toString();
		}
		if (paramName.indexOf("NotEquals") > 0) {
			paramName = paramName.replace("NotEquals", "");
			operador = "!=";
			retorno = paramName + operador + paramValue;
		}
		return retorno;
	}
	private String addParameterElastic(String paramName, String paramValue) {
		String operador = ":";
		String retorno = paramName + operador + convertirEspaciosAAND(paramValue);
//		if (paramName.indexOf("Contains") > 0) {
//			paramName = paramName.replace("Contains", "");
//			paramValue = paramValue + "*";
//			retorno = paramName + operador + paramValue;
//		}
//		if (paramName.indexOf("Till") > 0) {
//			operador = "=lt=";
//			paramName = paramName.replace("Till", "");
//			retorno = paramName + operador + paramValue;
//		}
//		if (paramName.indexOf("From") > 0) {
//			operador = "=ge=";
//			paramName = paramName.replace("From", "");
//			retorno = paramName + operador + paramValue;
//		}
//
//		if (paramName.indexOf("InList") > 0) {
//			StringBuilder consulta = new StringBuilder();
//			String[] values = paramValue.split(",");
//			paramName = paramName.replace("InList", "");
//			for (int i = 0; i < values.length; i++) {
//				if (i > 0) {
//					consulta.append(",");
//				}
//				consulta.append(paramName + operador + values[i].trim());
//			}
//			retorno = consulta.toString();
//		}
//		if (paramName.indexOf("NotEquals") > 0) {
//			paramName = paramName.replace("NotEquals", "");
//			operador = "!=";
//			retorno = paramName + operador + paramValue;
//		}
		return retorno;
	}

	private String convertirEspaciosAAND(String paramValue) {
		if (paramValue.indexOf("AND") > 0) {
			return paramValue;
		} else {
			String[] retorno = paramValue.split(" ");
			StringBuilder req = new StringBuilder();
			for (int i = 0; i < retorno.length; i++) {
				if (i > 0) {
					req.append(" AND ");
				}
				req.append(retorno[i]);
			}
			if (retorno.length > 1) {
				return "(" + req.toString() + ")";
			} else {
				return req.toString();
			}
		}
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getPoint() {
		return point;
	}

	public void setPoint(String point) {
		this.point = point;
	}

	public Integer getDistance() {
		return distance;
	}

	public void setDistance(Integer distance) {
		this.distance = distance;
	}

	public String getFl() {
		return fl;
	}

	public void setFl(String fl) {
		this.fl = fl;
	}

	public void setContextProperties(Map<String, String> contextProperties) {
		this.contextProperties = contextProperties;
	}

	public Search getConditions(Class<?> clase) throws SearchParseException {
		
		Search busqueda;
		String busquedaStr = this.getSearchExpression();
		if ("".equals(busquedaStr)) {
			busqueda = new Search(clase).setMaxResults(rows).setFirstResult(start);
			busqueda.setShowAll(showAll);
		} else {
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			SearchCondition<?> sc = new FiqlParser(clase, contextProperties).parse(busquedaStr);
			busqueda = new Search();
			busqueda.setSearchClass(clase);
			busqueda.setMaxResults(rows);
			busqueda.setFirstResult(start);
			busqueda.setShowAll(showAll);
			this.clase = clase;
			
			busqueda.addFilters(addCondition(sc));
		}
		if (!"".equals(point)){
			String[] coord = point.split(",");
			Double[] coordenadas = new Double[]{Double.parseDouble(coord[0]), Double.parseDouble(coord[1])};
			if (Funciones.getPeticion().getSrsName().equals(CheckeoParametros.SRSWGS84)) {
				coordenadas = Geometria.pasarAUTM30(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]));	
			} else if (Funciones.getPeticion().getSrsName().equals(CheckeoParametros.SRSETRS89)) {
				coordenadas = Geometria.pasarETRS89AUTM30(Double.parseDouble(coord[0]), Double.parseDouble(coord[1]));	
			}
			busqueda.addFilterAnd(Filter.distancia(distance, coordenadas[0], coordenadas[1]));
			busqueda.addSortAsc(BaseSearchProcessor.SORTDISTANCIA + "_" + Math.abs(coordenadas[0]) + "_" + coordenadas[1]);
		}
		addFilterVisible(busqueda, clase);
		establecerOrden(busqueda);
		return busqueda;
	}
	private void establecerOrden(Search search) {
		if (sort != null && !"".equals(sort)) {
			String[] ordenes = sort.split(",");
			for (int i = 0; i < ordenes.length; i++) {
	    		String[] campos = ordenes[i].split(" ");
	    		String valorCampo = campos[0];
	    		if (campos.length == 1 || campos[1].equals("asc")) {
	    			search.addSortAsc(valorTransient(valorCampo, search.getSearchClass()));
	    		} else {
	    			search.addSortDesc(valorTransient(valorCampo, search.getSearchClass()));
	    		}
	    	}
		}
	}
	@SuppressWarnings("unchecked")
	private static String valorTransient(String campo, Class<?> clase) {
		try {
			if (clase != null && clase.isAnnotationPresent(Rel.class)) {
				HashMap<String,String> transientField = (HashMap<String,String>)clase.getField("transientField").get(null);
				if (transientField.get(campo) != null) {
					campo = transientField.get(campo);
				}
			}
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		return campo;
	}
	private void addFilterVisible(Search busqueda, Class<?> clase) {
		try {
			Field campoVisible = clase.getDeclaredField("visible"); 
			if (campoVisible != null 
					&& !campoVisible.isAnnotationPresent(Transient.class) 
					&& !Funciones.getPeticion().getPermisosEnSeccion().contains(Permisos.DET)) {
				busqueda.addFilterAnd(Filter.equal("visible", "S"));
			}
		} catch (SecurityException e) {
			;
		} catch (NoSuchFieldException e) {
			;
		}
		
	}

	private Filter[] addCondition(SearchCondition<?> sc) {
		if (sc instanceof AndSearchCondition) {
			return addToSearchAnd((AndSearchCondition<?>) sc);
		} else if (sc instanceof OrSearchCondition) {
			return addToSearchOr((OrSearchCondition<?>) sc);
		} else if (sc instanceof SimpleSearchCondition) {
			return addToSearchSimple((SimpleSearchCondition<?>) sc);
		} else if (sc instanceof PrimitiveSearchCondition) {
			return addToSearchPrimitive((PrimitiveSearchCondition<?>) sc);
		} else {
			return null;
		}
		
	}
	
	private Filter[] addToSearchPrimitive(PrimitiveSearchCondition<?> sc) {
		Filter filtro;
		if (sc.getStatement().getProperty().indexOf(".id") > 0
    			&& !sc.getStatement().getValue().getClass().isAnnotationPresent(Embeddable.class)) {
			filtro = Filter.some(sc.getStatement().getProperty().substring(0, sc.getStatement().getProperty().lastIndexOf(".id")),
							Busqueda.addRestriccion("id", sc.getStatement().getValue(), sc.getStatement().getCondition(), clase, sc.getCondition()));
		} else {
			filtro = Busqueda.addRestriccion(sc.getStatement(), clase, sc.getCondition());
		}
		return new Filter[]{filtro};
	}

	private Filter[] addToSearchSimple(SimpleSearchCondition<?> sc) {
		String[]element = sc.getStatement().getProperty().split("\\.");
		Filter filtro;
		if (element.length < 3) {
			if (sc.getStatement().getProperty().indexOf(".id") > 0 
					&& !sc.getStatement().getValue().getClass().isAnnotationPresent(Embeddable.class)
					) {
				filtro = Filter.some(sc.getStatement().getProperty().substring(0, sc.getStatement().getProperty().lastIndexOf(".id")),
								Busqueda.addRestriccion("id", sc.getStatement().getValue(), sc.getStatement().getCondition(), clase, sc.getCondition()));
			} else if (sc.getStatement().getValue().getClass().getCanonicalName().indexOf("java.util.Collection")>=0) {
				Class<?> claseConsulta = clase;
				String propiedad = sc.getStatement().getProperty().substring(0, sc.getStatement().getProperty().lastIndexOf('.'));
				try {
					ParameterizedType p = ((ParameterizedType) clase.getDeclaredField(propiedad).getGenericType());					
					Type type = p.getActualTypeArguments()[0];
					claseConsulta = (Class<?>) type;
				} catch (SecurityException e) {

				} catch (NoSuchFieldException e) {

				}
								
				String propiedadInterna = sc.getStatement().getProperty().substring(sc.getStatement().getProperty().lastIndexOf('.') + 1, sc.getStatement().getProperty().length());
				filtro = Filter.some(propiedad,
						Busqueda.addRestriccion(propiedadInterna, Busqueda.obtenerValorCampo(sc.getStatement().getProperty(), sc.getStatement().getValue()), sc.getStatement().getCondition(), claseConsulta, sc.getCondition())
					);
			} else {
				filtro = Filter.and(Busqueda.addRestriccion(sc.getStatement(), clase, sc.getCondition()));
			}
		} else {
			// Consultas de tres niveles
			String propiedad = sc.getStatement().getProperty();
			propiedad = propiedad.substring(propiedad.indexOf('.') + 1, propiedad.length());
			String propiedadFin = propiedad.substring(propiedad.lastIndexOf('.') + 1, propiedad.length());
			filtro = Filter.some(sc.getStatement().getProperty().substring(0, sc.getStatement().getProperty().lastIndexOf('.')),
					Busqueda.addRestriccion(propiedadFin, 
							Busqueda.obtenerValorCampo(propiedad, sc.getStatement().getValue()), 
							sc.getStatement().getCondition(), clase, sc.getCondition())
				);
		}
		return new Filter[]{filtro};
	}

	public Filter[] addToSearchAnd(AndSearchCondition<?> sc) {
    	List<Filter> filtros = new ArrayList<Filter>();
    	for (int i = 0; i < sc.getSearchConditions().size(); i++) {
			SearchCondition<?> o = sc.getSearchConditions().get(i);
			if (o instanceof AndSearchCondition || o instanceof OrSearchCondition) {
				filtros.addAll(Arrays.asList(addCondition(o)));
			} else if (o instanceof SimpleSearchCondition) {
				filtros.addAll(Arrays.asList(addToSearchSimple((SimpleSearchCondition<?>)o)));
			} else if (o instanceof PrimitiveSearchCondition) {
				filtros.addAll(Arrays.asList(addToSearchPrimitive((PrimitiveSearchCondition<?>)o)));
			}
		}
    	Filter[] filt = filtros.toArray(new Filter[filtros.size()]);
    	return new Filter[]{Filter.and(filt)};
	}
	

	public Filter[] addToSearchOr(OrSearchCondition<?> sc) {
		List<Filter> filtros = new ArrayList<Filter>();
    	for (int i = 0; i < sc.getSearchConditions().size(); i++) {
			SearchCondition<?> o = sc.getSearchConditions().get(i);
			if (o.getStatement() == null) {
				filtros.addAll(Arrays.asList(addCondition(o)));
			} else {
				
			    String[]element = o.getStatement().getProperty().split("\\.");
				if (element.length < 3) {
					if (o.getStatement().getProperty().indexOf(".id") > 0 &&
							!o.getStatement().getValue().getClass().isAnnotationPresent(Embeddable.class)) {
						filtros.add(Filter.some(o.getStatement().getProperty().substring(0, o.getStatement().getProperty().lastIndexOf(".id")),
										Busqueda.addRestriccion("id", o.getStatement().getValue(), o.getStatement().getCondition(), clase, sc.getCondition())));
					} else {
						filtros.add(Busqueda.addRestriccion(o.getStatement(), clase, o.getCondition()));
					}
				} else {
					// Consultas de tres niveles
					filtros.add(Filter.some(o.getStatement().getProperty().substring(0, o.getStatement().getProperty().lastIndexOf('.')),
							Busqueda.addRestriccion(o.getStatement().getProperty(), Busqueda.obtenerValorCampo(o.getStatement().getProperty(), o.getStatement().getValue()), o.getStatement().getCondition(), clase, sc.getCondition())
						));
				}
			}
			
		}
    	Filter[] filt = filtros.toArray(new Filter[filtros.size()]);
    	return new Filter[]{Filter.or(filt)};
	}

	public void addCondition(String fiql) {
		String busqueda = getSearchExpression();
        busqueda = (busqueda.equals("")) ? "" : busqueda + ";";
        busqueda += fiql;
		this.searchExpression = busqueda;
	}

	public void setSearchExpression(String searchExpression) {
		this.searchExpression = searchExpression;
		
	}

	public void setExcludeFields(String... campo) {
		this.excludedFields = campo;
	}
}
