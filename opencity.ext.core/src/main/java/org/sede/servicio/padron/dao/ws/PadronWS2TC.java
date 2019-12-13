/**
 * PadronWS2TC.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.padron.dao.ws;


public interface PadronWS2TC extends javax.xml.rpc.Service {

/**
 * * WebService de PadrÃ³n para la tarjeta ciudadana
 *  *
 */
    public java.lang.String getPadronWS2TCPortAddress();

    public org.sede.servicio.padron.dao.ws.PadronWS2TCPortType getPadronWS2TCPort() throws javax.xml.rpc.ServiceException;

    public org.sede.servicio.padron.dao.ws.PadronWS2TCPortType getPadronWS2TCPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
