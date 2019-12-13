package org.sede.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.cxf.jaxrs.ext.search.ConditionType;
import org.apache.cxf.jaxrs.ext.search.PrimitiveStatement;
import org.sede.core.anotaciones.Rel;
import org.sede.core.rest.Peticion;
import org.sede.core.rest.Role;

import com.googlecode.genericdao.search.Filter;


public class Busqueda {
	private Busqueda() {
		super();
	}
	public static <T> Filter addRestriccion(PrimitiveStatement stat, Class<T> clase, Object entity) {
		return addRestriccion(stat.getProperty(), stat.getValue(), stat.getCondition(), clase, entity);
	}
	
	
	public static <T> Filter addRestriccion(String propiedad, Object value, ConditionType condicion, Class<T> clase, Object entity) {
		if (Funciones.getPeticion().puedeVerCampoBusqueda(propiedad, clase)) {
			String valorNoSingleton = obtenerValorCampo(propiedad, value, entity);
			
			switch (condicion) {
			case EQUALS:
				if (value.toString().indexOf('*') >= 0) {
					if ("*".equals(value.toString())) {
						return Filter.isNotNull(propiedad);	
					} else {
						String valor = valorNoSingleton.replace("*", "");
						final String[] listaValores = valor.split(" ");
						int ite = 0;
						List<Filter> filtros = new ArrayList<Filter>();
						while (ite < listaValores.length && ite < 9) {
							if (listaValores[ite] != null) {
								filtros.add(Filter.ilike(propiedad, "%"
									+ listaValores[ite] + "%"));
							}
							ite++;
						
						}
						return Filter.and(filtros.toArray(new Filter[filtros.size()]));
					}
				} else {
					return Filter.equal(propiedad, valorNoSingleton);
				}
			case NOT_EQUALS:
				return Filter.notEqual(propiedad, valorNoSingleton);
			case GREATER_THAN:
				return Filter.greaterThan(propiedad, valorNoSingleton);
			case GREATER_OR_EQUALS:
				return Filter.greaterOrEqual(propiedad, valorNoSingleton);
			case LESS_THAN:
				return Filter.lessThan(propiedad, valorNoSingleton);
			case LESS_OR_EQUALS:
				return Filter.lessOrEqual(propiedad, valorNoSingleton);
			default:
				String msg = String.format("Condition type %s is not supported",
						condicion.name());
				throw new RuntimeException(msg);
			}
		} else {
			throw new IllegalArgumentException("No esta permitida la consulta por el campo <" + propiedad + ">");
		}
	}

	@SuppressWarnings("unchecked")
	private static String obtenerValorCampo(String property, Object value, Object entity) {
		if (value.getClass().getName().indexOf("SingletonSet") >= 0
				|| value.getClass().getName().indexOf("SingletonList") >= 0
				|| value.getClass().isAnnotationPresent(XmlRootElement.class)) {
			
			try {
				if (value.getClass().isAnnotationPresent(Rel.class)) {
					HashMap<String,String> transientField = (HashMap<String,String>)value.getClass().getField("transientField").get(null);
					if (transientField.get(property) != null) {
						property = transientField.get(property);
						value = Funciones.obtenerValorCampo(entity, property);
					}
				}
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
			String propiedad = property.substring(property.lastIndexOf('.') + 1, property.length());
			String valor = value.toString().substring((value.toString().indexOf(propiedad + "=") + propiedad.length() + 1), value.toString().length());
			valor = valor.substring(0, (valor.indexOf(',') > 0 && valor.indexOf(']') > valor.indexOf(',') ? valor.indexOf(',') : valor.indexOf(']')));
			return valor.replace("'", "");
		} else {
			try {
				if (entity.getClass().isAnnotationPresent(Rel.class)) {
					HashMap<String,String> transientField = (HashMap<String,String>)entity.getClass().getField("transientField").get(null);
					if (transientField.get(property) != null) {
						property = transientField.get(property);
						value = Funciones.obtenerValorCampo(entity, property);
					}
				}
			} catch (SecurityException e) {
			} catch (NoSuchFieldException e) {
			} catch (NullPointerException e) {
			} catch (IllegalArgumentException e) {
			} catch (IllegalAccessException e) {
			}
			return value.toString();
		}
	}
	
	
	public static String createDefaultCriteria(String busqueda, Peticion peticion, String criterioPorDefecto) {
		if (peticion.getTipoAcceso().equals(Role.PUBLICO)) {
			return busqueda ==null ? criterioPorDefecto : busqueda + ";" + criterioPorDefecto;
		} else {
			return busqueda;
		}
	}
	public static boolean consultaDeBorradoMultipleValida(String searchExpression) {
		if (searchExpression == null || searchExpression.trim().length() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	public static Object obtenerValorCampo(String property, Object value) {
		if (value instanceof java.util.Set) {
			java.util.Set<?> arr = (java.util.Set<?>)value;
			return obtenerValorCampo(property.substring(property.indexOf('.') + 1, property.length()), arr.iterator().next());
		} else if (value instanceof java.util.List) {
			java.util.List<?> arr = (java.util.List<?>)value;
			return obtenerValorCampo(property.substring(property.indexOf('.') + 1, property.length()), arr.iterator().next());
		} else {
			String propiedadObtener = property;
			String[] propiedades = property.split("\\.");
			if (property.length() > 0) {
				propiedadObtener = propiedades[0];
			}
			Object entidad = Funciones.obtenerValorCampoObj(value, propiedadObtener);
			if (propiedades.length > 1) {
				return obtenerValorCampo(property.substring(property.indexOf('.') + 1, property.length()), entidad);
			} else {
				return entidad;
			}
		}
	}
}
