package org.sede.core.rest.view;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.PublicName;
import org.sede.core.exception.InvalidImplementationException;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.Peticion;
import org.sede.core.utils.Funciones;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;


public class TransformadorFIQL {
	public void addCriterioCampo(Search search, Field nombreCampo, Object value) {
		if (value instanceof String) {
			String valor = value.toString();
			final String[] listaValores = valor.split(" ");
			int ite = 0;
			List<Filter> filtros = new ArrayList<Filter>();
			while (ite < listaValores.length && ite < 9) {
				if (listaValores[ite] != null) {
					filtros.add(Filter.ilike(obtenerNombreCampo(nombreCampo), "%"
						+ listaValores[ite].toLowerCase() + "%"));
				}
				ite++;
			
			}
			search.addFilter(Filter.and(filtros.toArray(new Filter[filtros.size()])));
		} else if (value instanceof Date 
				&& nombreCampo.isAnnotationPresent(Temporal.class) 
				&& nombreCampo.getAnnotation(Temporal.class).value().equals(TemporalType.TIMESTAMP)) {
			Date fecha = (Date) value;
			Calendar cal = Calendar.getInstance();
	        cal.setTime(fecha);
	        cal.add(Calendar.DATE, 1); 
			search.addFilter(Filter.and(Filter.greaterOrEqual(obtenerNombreCampo(nombreCampo), value),
					Filter.lessThan(obtenerNombreCampo(nombreCampo), cal.getTime())
					));
		} else {
			search.addFilter(Filter.equal(obtenerNombreCampo(nombreCampo), value));
		}
	}
	public void addCriteriosBusqueda(Search search, Object retorno,
			Peticion peticion, boolean primero, String prefijo)
			throws InvalidImplementationException {
		anyadirObjeto(search, retorno, peticion, prefijo);
	}
	private void anyadirObjeto(Search search, Object retorno,
			Peticion peticion, String prefijo) throws InvalidImplementationException {
		for (Field field : retorno.getClass().getDeclaredFields()) {
			if (!field.isAnnotationPresent(Transient.class)
					&& TransformadorBasico.transformarCampo(retorno, peticion, prefijo, field)) {
				Object valor = Funciones.retrieveObjectValue(retorno, field.getName());
				if (valor instanceof List) {
					List<?> listado = (List<?>) valor;
					if (!listado.isEmpty()) {
						String pref = prefijo;
						if (!field.getName().equals(CheckeoParametros.PARAMRESULT)) {
							pref = prefijo + field.getName() + ".";
						}
						for (Object object : listado) {
							peticion.establecerLastModified(object);
							
							if (object instanceof Map<?,?>) {
//								TransformadorBasico.transformarMap(respuesta, peticion, this, true, field, object);
							} else {
								anyadirObjeto(search, object, peticion, pref);
							}
						}
					}
				} else if (valor instanceof Map<?, ?>) {
//					primerCampo = TransformadorBasico.transformarMap(respuesta, peticion, this, primerCampo, field, valor);
				} else if (valor instanceof Set) {
					Set<?> listado = (Set<?>) valor;
					if (!listado.isEmpty()) {
						String pref = prefijo;
						if (!field.getName().equals(CheckeoParametros.PARAMRESULT)) {
							pref = prefijo + field.getName() + ".";
						}
						for (Object object : listado) {
							peticion.establecerLastModified(object);
							anyadirObjeto(search, object, peticion, pref);
						}
					}
				} else if (valor instanceof Object[]) {
					Object[] listado = (Object[]) valor;
					if (listado.length > 0) {
						String pref = prefijo;
						if (!field.getName().equals(CheckeoParametros.PARAMRESULT)) {
							pref = prefijo + field.getName() + ".";
						}
						for (int i = 0; i < listado.length; i++) {
							Object object = listado[i];
							peticion.establecerLastModified(object);
							anyadirObjeto(search, object, peticion, pref);
						}
					}
				} else  if (field.getType().isAnnotationPresent(XmlRootElement.class)) {
					anyadirObjeto(search, valor, peticion, prefijo + field.getName() + ".");
				} else {
					this.addCriterioCampo(search, field, valor);
				}
			}
		}
	}
	private String obtenerNombreCampo(Field field) {
		if (field.isAnnotationPresent(PublicName.class)) {
			return field.getAnnotation(PublicName.class).value();
		} else {
			return field.getName();
		}
	}
}
