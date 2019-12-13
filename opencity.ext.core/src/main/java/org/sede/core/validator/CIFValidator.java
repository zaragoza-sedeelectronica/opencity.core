package org.sede.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;


public class CIFValidator implements ConstraintValidator<CIF, String> {

	@Override
	public void initialize(CIF constraintAnnotation) {

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(value)) {
			return true;
		} else {
			return Utils.isCif(value);
		}
		
	}

}
