package org.sede.core.dao;

import javax.persistence.AttributeConverter;

public class BooleanSINOConverter implements AttributeConverter<Boolean, String> {

	@Override
	public String convertToDatabaseColumn(Boolean b) {
		if (b == null) {
            return null;
        }
        if (b.booleanValue()) {
            return "SI";
        }
        return "NO";
	}

	@Override
	public Boolean convertToEntityAttribute(String s) {
		if (s == null) {
            return null;
        }
        if (s.equals("SI") || s.equals("SÍ") || s.equals("si")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
	}

}
