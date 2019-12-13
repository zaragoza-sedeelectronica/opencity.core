package org.sede.core.dao;
import java.lang.reflect.Field;
import java.util.Date;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

import org.sede.core.utils.Funciones;
public class PersistenceContextListener {
	 
     @PrePersist
	 public void prePersist(Object object) {
	 	setPropiedad(object, "usuarioAlta", Funciones.getPeticion().getClientId());
	 	setPropiedad(object, "creationDate", new Date());
	 }

	 @PostPersist
	 public void postPersist(Object object){
	 
	 }

	 @PreRemove
	 public void PreRemove(Object object){
	 
	 }

	 @PostRemove
	 public void PostRemove(Object object){
	 
	 }

	 @PreUpdate
	 public void PreUpdate(Object object) {
	
		 setPropiedad(object, "usuarioMod", Funciones.getPeticion().getClientId());
		 setPropiedad(object, "lastUpdated", new Date());
	 }

	public void setPropiedad(Object object, String propiedad, Object valor) {
		try {
			 Class<?> ftClass = object.getClass();
			 Field f2 = ftClass.getDeclaredField(propiedad);
			 f2.setAccessible(true);
			 f2.set(object, valor);
		 } catch (Exception e) {

		 }
	}

	 @PostUpdate
	 public void PostUpdate(Object object){
	
	 }
}
