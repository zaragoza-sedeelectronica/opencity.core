package org.sede.core.anotaciones;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import org.sede.core.validator.HTMLValidator;


@Target( { METHOD, FIELD, ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = HTMLValidator.class)
@Documented
public @interface ValidHTML {

    String message() default "El etiquetado no es correcto";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    
}
