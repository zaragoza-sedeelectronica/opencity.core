package org.sede.core.anotaciones;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.sede.servicio.acceso.userrequirements.Default;

@Retention(RetentionPolicy.RUNTIME)
public @interface PermisosUser {
	Class<?>[] requirements() default Default.class;
}
