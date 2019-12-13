package org.sede.core.validator;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Retention(RUNTIME)
@Constraint(validatedBy = { NIFNIEValidator.class })
@Documented
@Target( { METHOD, FIELD, ANNOTATION_TYPE })
public @interface NIFNIE {
	String message() default "El valor introducido no es un nif o nie válido";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
