package org.sede.core.dao;

import javax.persistence.AttributeConverter;

public class BooleanConverter implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean b) {
		if (b == null) {
            return null;
        }
        if (b.booleanValue()) {
            return "S";
        }
        return "N";
	}

	@Override
	public Boolean convertToEntityAttribute(String s) {
		if (s == null) {
            return null;
        }
        if (s.equals("S") || s.equals("s")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
	}

}
