package org.sede.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.sede.core.anotaciones.InList;


public class InListValidator implements ConstraintValidator<InList, String> {

	private String[] valores;

	public void initialize(InList constraintAnnotation) {
		this.valores = constraintAnnotation.value();
	}

	public boolean isValid(String object,
			ConstraintValidatorContext constraintContext) {

		if (object == null || "".equals(object)) {
			return true;
		}

		for (int i = 0; i < valores.length; i++) {
			if (valores[i].equals(object)) {
				return true;
			}
		}
		return false;
	}

}