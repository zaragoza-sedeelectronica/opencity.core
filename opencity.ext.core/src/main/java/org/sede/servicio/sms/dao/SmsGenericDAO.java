package org.sede.servicio.sms.dao;

import org.sede.core.rest.Mensaje;


public interface SmsGenericDAO {
    public Mensaje send(String[] phones, String message);

}