package org.sede.core.anotaciones;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Rdf {

	String uri() default "";
	String prefijo() default "";
	
	String contexto() default "";
	String propiedad() default "";
	
	
}
