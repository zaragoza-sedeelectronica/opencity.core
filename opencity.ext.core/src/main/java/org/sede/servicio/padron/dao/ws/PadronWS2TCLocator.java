/**
 * PadronWS2TCLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.padron.dao.ws;
@SuppressWarnings({"rawtypes", "unchecked"})
public class PadronWS2TCLocator extends org.apache.axis.client.Service implements org.sede.servicio.padron.dao.ws.PadronWS2TC {

/**
 * * WebService de PadrÃ³n para la tarjeta ciudadana
 *  *
 */

    public PadronWS2TCLocator() {
    }


    public PadronWS2TCLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public PadronWS2TCLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for PadronWS2TCPort
    private java.lang.String PadronWS2TCPort_address = "http://pc13216:8888/Padron-PadronWS-context-root/PadronWS2TC";

    public java.lang.String getPadronWS2TCPortAddress() {
        return PadronWS2TCPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String PadronWS2TCPortWSDDServiceName = "PadronWS2TCPort";

    public java.lang.String getPadronWS2TCPortWSDDServiceName() {
        return PadronWS2TCPortWSDDServiceName;
    }

    public void setPadronWS2TCPortWSDDServiceName(java.lang.String name) {
        PadronWS2TCPortWSDDServiceName = name;
    }

    public org.sede.servicio.padron.dao.ws.PadronWS2TCPortType getPadronWS2TCPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(PadronWS2TCPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getPadronWS2TCPort(endpoint);
    }

    public org.sede.servicio.padron.dao.ws.PadronWS2TCPortType getPadronWS2TCPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
    	org.sede.servicio.padron.dao.ws.PadronWS2TCBindingStub _stub = new org.sede.servicio.padron.dao.ws.PadronWS2TCBindingStub(portAddress, this);
        _stub.setPortName(getPadronWS2TCPortWSDDServiceName());
        return _stub;
    }

    public void setPadronWS2TCPortEndpointAddress(java.lang.String address) {
        PadronWS2TCPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (org.sede.servicio.padron.dao.ws.PadronWS2TCPortType.class.isAssignableFrom(serviceEndpointInterface)) {
            	org.sede.servicio.padron.dao.ws.PadronWS2TCBindingStub _stub = new org.sede.servicio.padron.dao.ws.PadronWS2TCBindingStub(new java.net.URL(PadronWS2TCPort_address), this);
                _stub.setPortName(getPadronWS2TCPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("PadronWS2TCPort".equals(inputPortName)) {
            return getPadronWS2TCPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://es/aytozgz/fiscalw1/webService/PadronWS2TC.wsdl", "PadronWS2TC");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://es/aytozgz/fiscalw1/webService/PadronWS2TC.wsdl", "PadronWS2TCPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("PadronWS2TCPort".equals(portName)) {
            setPadronWS2TCPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
