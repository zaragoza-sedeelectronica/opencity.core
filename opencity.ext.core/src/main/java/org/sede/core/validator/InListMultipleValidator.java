package org.sede.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.sede.core.anotaciones.InListMultiple;



public class InListMultipleValidator implements ConstraintValidator<InListMultiple, String[]> {

	private String[] valores;

	public void initialize(InListMultiple constraintAnnotation) {
		this.valores = constraintAnnotation.value();
	}

	public boolean isValid(String[] object,
			ConstraintValidatorContext constraintContext) {

		if (object == null) {
			return true;
		}
		boolean retornoGeneral = false;
		for (int j = 0; j < object.length; j++) {
			boolean retorno = false;
			for (int i = 0; i < valores.length; i++) {
				if (valores[i].equals(object[j])) {
					retorno = true;
				}
			}
			if (!retorno) {
				return false;
			} else {
				retornoGeneral = true;
			}
		}
		return retornoGeneral;
	}

}