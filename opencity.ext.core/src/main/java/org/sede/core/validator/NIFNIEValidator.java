package org.sede.core.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.apache.commons.lang3.StringUtils;


public class NIFNIEValidator implements ConstraintValidator<NIFNIE, String> {

	@Override
	public void initialize(NIFNIE constraintAnnotation) {

	}

	@Override
	public boolean isValid(String value, ConstraintValidatorContext context) {
		if (StringUtils.isEmpty(value)) {
			return true;
		} else {
			return (Utils.isNIF(value) || Utils.isNIE(value));
		}
		
	}

}
