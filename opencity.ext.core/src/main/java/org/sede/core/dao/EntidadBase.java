package org.sede.core.dao;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RdfMultiple;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;


public class EntidadBase implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger logger = LoggerFactory
			.getLogger(EntidadBase.class);
	
	public Map<String, String> get(String fieldName) {
		return get(fieldName, null);
	}
	
	public Map<String, String> get(String fieldName, String dtFormat) {
		try {
			
			Map<String, String> valores = new HashMap<String, String>();
			Field f = this.getClass().getDeclaredField(fieldName);
			if (f.isAnnotationPresent(RdfMultiple.class)) {
				RdfMultiple an = f.getAnnotation(RdfMultiple.class);
				StringBuilder val = new StringBuilder();
				for (Rdf r : an.value()) {
					val.append(r.contexto() + ":" + r.propiedad() + " ");
				}
				valores.put("property", val.toString().trim());
			}
			if (f.isAnnotationPresent(Rdf.class)) {
				Rdf an = f.getAnnotation(Rdf.class);
				valores.put("property", an.contexto() + ":" + an.propiedad());			
			
			}
			Object value = Funciones.getValueFromObject(this, fieldName);
			String retorno;
			if (value instanceof String) {
				retorno = (String)value;
			} else if (value instanceof Date) {
				String format;
				if (dtFormat == null) {
					format = ConvertDate.DF_DEFECTO;
				} else {
					format = dtFormat;
				}
				valores.put("content", ConvertDate.date2String((Date) value, ConvertDate.ISO8601_FORMAT));
				retorno = ConvertDate.date2String((Date) value, format);
			} else {
				retorno = value.toString();
			}
			valores.put("text", retorno);
			return valores;
			
		} catch (SecurityException e) {
			logger.error("No se puede acceder al campo " + fieldName + " en la clase " + this.getClass().getCanonicalName());
			return null;
		} catch (NoSuchFieldException e) {
			logger.error("No existe el campo " + fieldName + " en la clase " + this.getClass().getCanonicalName());
			return null;
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException al campo " + fieldName + " en la clase " + this.getClass().getCanonicalName());
			return null;
		} 
	}
	
	public Map<String, String> prop(String fieldName) {
		try {
			Map<String, String> valores = new HashMap<String, String>();
			Field f = this.getClass().getDeclaredField(fieldName);
			if (f.isAnnotationPresent(RdfMultiple.class)) {
				RdfMultiple an = f.getAnnotation(RdfMultiple.class);
				StringBuilder val = new StringBuilder();
				for (Rdf r : an.value()) {
					val.append(r.contexto() + ":" + r.propiedad() + " ");
				}
				valores.put("property", fieldName);
				valores.put("typeof", val.toString().trim());
			}
			if (f.isAnnotationPresent(Rdf.class)) {
				Rdf an = f.getAnnotation(Rdf.class);
				valores.put("property", fieldName);
				valores.put("typeof", an.contexto() + ":" + an.propiedad());			
			}
			return valores;
			
		} catch (SecurityException e) {
			logger.error("No se puede acceder al campo " + fieldName + " en la clase " + this.getClass().getCanonicalName());
			return null;
		} catch (NoSuchFieldException e) {
			logger.error("No existe el campo " + fieldName + " en la clase " + this.getClass().getCanonicalName());
			return null;
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException al campo " + fieldName + " en la clase " + this.getClass().getCanonicalName());
			return null;
		} 
	}
	public Map<String, String> prop() {
		try {
			Map<String, String> valores = new HashMap<String, String>();
			if (this.getClass().isAnnotationPresent(RdfMultiple.class)) {
				RdfMultiple an = this.getClass().getAnnotation(RdfMultiple.class);
				StringBuilder val = new StringBuilder();
				for (Rdf r : an.value()) {
					val.append(r.contexto() + ":" + r.propiedad() + " ");
				}
				valores.put("property", val.toString().trim());
			}
			if (this.getClass().isAnnotationPresent(Rdf.class)) {
				Rdf an = this.getClass().getAnnotation(Rdf.class);
				valores.put("property", an.contexto() + ":" + an.propiedad());			
			
			}
			return valores;
			
		} catch (SecurityException e) {
			logger.error("SecurityException en la clase " + this.getClass().getCanonicalName());
			return null;
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException en la clase " + this.getClass().getCanonicalName());
			return null;
		}
	}
	public Map<String, String> voc(String fieldName) {
		try {
			Map<String, String> valores = new HashMap<String, String>();
			valores.put("property", fieldName);
			Field f = this.getClass().getDeclaredField(fieldName);
			if (f.isAnnotationPresent(RdfMultiple.class)) {
				RdfMultiple an = f.getAnnotation(RdfMultiple.class);
				StringBuilder val = new StringBuilder();
				for (Rdf r : an.value()) {
					val.append(r.contexto() + ":" + r.propiedad() + " ");
					valores.put("xmlns:" + r.contexto(), Context.listado.get(r.contexto()).getUri());
				}
				Object obj = Funciones.retrieveObjectValue(this, "type");
				if (obj instanceof String[]) {
					String[] tipos = (String[])obj;
					for (int i = 0; i < tipos.length; i++) {
						val.append(tipos[i] + " ");
					}
				}
				valores.put("typeof", val.toString().trim());
			}
			if (f.isAnnotationPresent(Rdf.class)) {
				Rdf an = f.getAnnotation(Rdf.class);
				valores.put("xmlns:" + an.contexto(), Context.listado.get(an.contexto()).getUri());
				Object obj = Funciones.retrieveObjectValue(this, "type");
				StringBuilder val = new StringBuilder();
				val.append(an.contexto() + ":" + an.propiedad() + " ");
				if (obj instanceof String[]) {
					String[] tipos = (String[])obj;
					for (int i = 0; i < tipos.length; i++) {
						val.append(tipos[i] + " ");
					}
				}
				
				valores.put("typeof", val.toString().trim());			
			
			}
			return valores;
			
		} catch (SecurityException e) {
			logger.error("No se puede acceder al campo " + fieldName + " en la clase " + this.getClass().getCanonicalName());
			return null;
		} catch (NoSuchFieldException e) {
			logger.error("No existe el campo " + fieldName + " en la clase " + this.getClass().getCanonicalName());
			return null;
		} catch (IllegalArgumentException e) {
			logger.error("IllegalArgumentException al campo " + fieldName + " en la clase " + this.getClass().getCanonicalName());
			return null;
		} 
	}
	
	public Map<String, String> voc() {
		try {
			Map<String, String> valores = new HashMap<String, String>();
			if (this.getClass().isAnnotationPresent(RdfMultiple.class)) {
				RdfMultiple an = this.getClass().getAnnotation(RdfMultiple.class);
				StringBuilder val = new StringBuilder();
				StringBuilder vocab = new StringBuilder();
				for (Rdf r : an.value()) {
					val.append(r.contexto() + ":" + r.propiedad() + " ");
					if (StringUtils.isNotEmpty(r.contexto())) {
						vocab.append(Context.listado.get(r.contexto()).getUri() + " ");
						valores.put("xmlns:" + r.contexto(), Context.listado.get(r.contexto()).getUri());
					} else {
						vocab.append(r.uri() + " ");
					}
					
				}
				Object obj = Funciones.retrieveObjectValue(this, "type");
				if (obj instanceof String[]) {
					String[] tipos = (String[])obj;
					for (int i = 0; i < tipos.length; i++) {
						val.append(tipos[i] + " ");
					}
				}
				valores.put("vocab", vocab.toString().trim());
				valores.put("typeof", val.toString().trim());
			}
			if (this.getClass().isAnnotationPresent(Rdf.class)) {
				Rdf an = this.getClass().getAnnotation(Rdf.class);
				valores.put("xmlns:" + an.contexto(), Context.listado.get(an.contexto()).getUri());
				valores.put("vocab", Context.listado.get(an.contexto()).getUri());
				Object obj = Funciones.retrieveObjectValue(this, "type");
				StringBuilder val = new StringBuilder();
				val.append(an.contexto() + ":" + an.propiedad() + " ");
				if (obj instanceof String[]) {
					String[] tipos = (String[])obj;
					for (int i = 0; i < tipos.length; i++) {
						val.append(tipos[i] + " ");
					}
				}
				
				valores.put("typeof", val.toString().trim());
			}
			return valores;
			
		} catch (Exception e) {
			logger.error("Error al obtener vocabulario " + this.getClass().getCanonicalName() + ":"  + e.getMessage());
			return null;
		} 
	}
	
	public static void combinar(Object original, Object update) {
	    if(!original.getClass().isAssignableFrom(update.getClass())){
	        return;
	    }
	    // se pueden modificar todos los campos por tener permiso a nivel de sección (se asocia en la descripción del servicio)
	    // si se requiere otro permiso lo anotamos a nivel de campo y es el que comprobaremos
	    Method[] methods = original.getClass().getMethods();

	    for(Method fromMethod: methods){
	        if(fromMethod.getDeclaringClass().equals(original.getClass())
	                && fromMethod.getName().startsWith("get")){

	            String fromName = fromMethod.getName();
	            String toName = fromName.replace("get", "set");

	            try {
	                Method toMetod = original.getClass().getMethod(toName, fromMethod.getReturnType());
	                Object value = fromMethod.invoke(update, (Object[])null);
	                if(value != null){
	                	if (value.toString().equals("")) {
	                		toMetod.invoke(original, new Object[]{ null });
	                	} else {
	                		toMetod.invoke(original, value);
	                	}
	                }
	            } catch (Exception e) {
	            	logger.error(e.getMessage());
	            } 
	        }
	    }
	}
	// FIXME Borrar sino se utiliza
	public static void copyNonNullProperties(Object src, Object target) {
	    BeanUtils.copyProperties(src, target, getNullPropertyNames(src));
	}

	private static String[] getNullPropertyNames (Object source) {
	    final BeanWrapper src = new BeanWrapperImpl(source);
	    java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

	    Set<String> emptyNames = new HashSet<String>();
	    for(java.beans.PropertyDescriptor pd : pds) {
	        Object srcValue = src.getPropertyValue(pd.getName());
	        if (srcValue == null) emptyNames.add(pd.getName());
	    }
	    String[] result = new String[emptyNames.size()];
	    return emptyNames.toArray(result);
	}	
}
