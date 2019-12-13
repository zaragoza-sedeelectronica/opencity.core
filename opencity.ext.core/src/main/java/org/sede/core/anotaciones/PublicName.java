package org.sede.core.anotaciones;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/**
 * Para cambiar el nombre del atributo en la salida
 * @author ob544e
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PublicName {

	String value() default "";
	
	
}