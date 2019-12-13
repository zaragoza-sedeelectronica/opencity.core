package org.sede.core.anotaciones;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ResponseClass {
	 public Class<?> value();
	 public Class<?> entity() default Object.class;
}
