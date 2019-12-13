/**
 * MIInterfazSimplificadoLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.sms.ws.definitions;
@SuppressWarnings({"rawtypes", "unchecked"})
public class MIInterfazSimplificadoLocator extends org.apache.axis.client.Service implements MIInterfazSimplificado {

    public MIInterfazSimplificadoLocator() {
    }


    public MIInterfazSimplificadoLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public MIInterfazSimplificadoLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for MIInterfazSimplificadoPort
    private java.lang.String MIInterfazSimplificadoPort_address = "https://194.224.196.17:9905/isadpt_generico1";

    public java.lang.String getMIInterfazSimplificadoPortAddress() {
        return MIInterfazSimplificadoPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String MIInterfazSimplificadoPortWSDDServiceName = "MI-InterfazSimplificado-Port";

    public java.lang.String getMIInterfazSimplificadoPortWSDDServiceName() {
        return MIInterfazSimplificadoPortWSDDServiceName;
    }

    public void setMIInterfazSimplificadoPortWSDDServiceName(java.lang.String name) {
        MIInterfazSimplificadoPortWSDDServiceName = name;
    }

    public MIInterfazSimplificadoPortType getMIInterfazSimplificadoPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(MIInterfazSimplificadoPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getMIInterfazSimplificadoPort(endpoint);
    }

    public MIInterfazSimplificadoPortType getMIInterfazSimplificadoPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            MIInterfazSimplificadoBindingStub _stub = new MIInterfazSimplificadoBindingStub(portAddress, this);
            _stub.setPortName(getMIInterfazSimplificadoPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setMIInterfazSimplificadoPortEndpointAddress(java.lang.String address) {
        MIInterfazSimplificadoPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (MIInterfazSimplificadoPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                MIInterfazSimplificadoBindingStub _stub = new MIInterfazSimplificadoBindingStub(new java.net.URL(MIInterfazSimplificadoPort_address), this);
                _stub.setPortName(getMIInterfazSimplificadoPortWSDDServiceName());
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
        if ("MI-InterfazSimplificado-Port".equals(inputPortName)) {
            return getMIInterfazSimplificadoPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/definitions", "MI-InterfazSimplificado");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/definitions", "MI-InterfazSimplificado-Port"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("MIInterfazSimplificadoPort".equals(portName)) {
            setMIInterfazSimplificadoPortEndpointAddress(address);
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
