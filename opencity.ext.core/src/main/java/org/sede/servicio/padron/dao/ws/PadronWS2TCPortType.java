/**
 * PadronWS2TCPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.padron.dao.ws;

public interface PadronWS2TCPortType extends java.rmi.Remote {
    public boolean getEmpadronado(int tipoIdentificacion, java.lang.String nif, long anioNacimiento) throws java.rmi.RemoteException;
    public java.lang.String getEmpadronadoIdent(int tipoIdentificacion, java.lang.String nif, long anioNacimiento) throws java.rmi.RemoteException;
    public java.lang.String getEmpadronadoDS(int tipoIdentificacion, java.lang.String nif, long anioNacimiento) throws java.rmi.RemoteException;
    public java.lang.String getEmpadronadoJunta(int tipoIdentificacion, java.lang.String nif, long anioNacimiento) throws java.rmi.RemoteException;
}
