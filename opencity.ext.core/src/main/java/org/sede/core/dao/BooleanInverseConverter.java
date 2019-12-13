package org.sede.core.dao;

import javax.persistence.AttributeConverter;

public class BooleanInverseConverter implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean b) {
        if (b == null) {
            return null;
        }
        if (b.booleanValue()) {
            return "N";
        }
        return "S";
    }

    @Override
    public Boolean convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        if (s.equals("N") || s.equals("n")) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}

