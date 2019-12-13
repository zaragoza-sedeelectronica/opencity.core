/**
 * MIInterfazSimplificadoBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.sms.ws.definitions;

import org.sede.servicio.sms.ws.schemas.ContentType;
import org.sede.servicio.sms.ws.schemas.DeliveryReportType;
import org.sede.servicio.sms.ws.schemas.MassivePersonalRecipientType;
import org.sede.servicio.sms.ws.schemas.MassivePersonalSMSSubmitReq;
import org.sede.servicio.sms.ws.schemas.MmsSubmitReq;
import org.sede.servicio.sms.ws.schemas.RelativeOrAbsoluteDateType;
import org.sede.servicio.sms.ws.schemas.ResponseStatusType;
import org.sede.servicio.sms.ws.schemas.SmsBinarySubmitReq;
import org.sede.servicio.sms.ws.schemas.SmsClassType;
import org.sede.servicio.sms.ws.schemas.SmsTextSubmitReq;
import org.sede.servicio.sms.ws.schemas.SubmitRes;
import org.sede.servicio.sms.ws.schemas.VersionType;
import org.sede.servicio.sms.ws.schemas.WappushSubmitReq;
@SuppressWarnings({"rawtypes", "unused", "unchecked"})
public class MIInterfazSimplificadoBindingStub extends org.apache.axis.client.Stub implements MIInterfazSimplificadoPortType {
	private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[5];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("smsTextSubmit");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "smsTextSubmitReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">smsTextSubmitReq"), SmsTextSubmitReq.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">SubmitRes"));
        oper.setReturnClass(SubmitRes.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "SubmitRes"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[0] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("smsBinarySubmit");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "smsBinarySubmitReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">smsBinarySubmitReq"), SmsBinarySubmitReq.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">SubmitRes"));
        oper.setReturnClass(SubmitRes.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "SubmitRes"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[1] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("mmsSubmit");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "mmsSubmitReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">mmsSubmitReq"), MmsSubmitReq.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">SubmitRes"));
        oper.setReturnClass(SubmitRes.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "SubmitRes"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[2] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("wappushSubmit");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "wappushSubmitReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">wappushSubmitReq"), WappushSubmitReq.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">SubmitRes"));
        oper.setReturnClass(SubmitRes.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "SubmitRes"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[3] = oper;

        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("massivePersonalSMSSubmit");
        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "massivePersonalSMSSubmitReq"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">massivePersonalSMSSubmitReq"), MassivePersonalSMSSubmitReq.class, false, false);
        oper.addParameter(param);
        oper.setReturnType(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">SubmitRes"));
        oper.setReturnClass(SubmitRes.class);
        oper.setReturnQName(new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "SubmitRes"));
        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
        oper.setUse(org.apache.axis.constants.Use.LITERAL);
        _operations[4] = oper;

    }

    public MIInterfazSimplificadoBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public MIInterfazSimplificadoBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public MIInterfazSimplificadoBindingStub(javax.xml.rpc.Service service) {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class<?> cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class<?> beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class<?> beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class<?> enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class<?> enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class<?> arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class<?> arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class<?> simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class<?> simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class<?> simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class<?> simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">massivePersonalSMSSubmitReq");
            cachedSerQNames.add(qName);
            cls = MassivePersonalSMSSubmitReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">mmsSubmitReq");
            cachedSerQNames.add(qName);
            cls = MmsSubmitReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">smsBinarySubmitReq");
            cachedSerQNames.add(qName);
            cls = SmsBinarySubmitReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">smsTextSubmitReq");
            cachedSerQNames.add(qName);
            cls = SmsTextSubmitReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">SubmitRes");
            cachedSerQNames.add(qName);
            cls = SubmitRes.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", ">wappushSubmitReq");
            cachedSerQNames.add(qName);
            cls = WappushSubmitReq.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "contentType");
            cachedSerQNames.add(qName);
            cls = ContentType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "deliveryReportType");
            cachedSerQNames.add(qName);
            cls = DeliveryReportType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "massivePersonalRecipientsType");
            cachedSerQNames.add(qName);
            cls = MassivePersonalRecipientType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "massivePersonalRecipientType");
            qName2 = new javax.xml.namespace.QName("", "Recipient");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "massivePersonalRecipientType");
            cachedSerQNames.add(qName);
            cls = MassivePersonalRecipientType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "mmsContentsType");
            cachedSerQNames.add(qName);
            cls = ContentType[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "contentType");
            qName2 = new javax.xml.namespace.QName("", "Content");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "recipientsType");
            cachedSerQNames.add(qName);
            cls = java.lang.String[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string");
            qName2 = new javax.xml.namespace.QName("", "To");
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "relativeOrAbsoluteDateType");
            cachedSerQNames.add(qName);
            cls = RelativeOrAbsoluteDateType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(org.apache.axis.encoding.ser.BaseSerializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleSerializerFactory.class, cls, qName));
            cachedDeserFactories.add(org.apache.axis.encoding.ser.BaseDeserializerFactory.createFactory(org.apache.axis.encoding.ser.SimpleDeserializerFactory.class, cls, qName));

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "responseStatusType");
            cachedSerQNames.add(qName);
            cls = ResponseStatusType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "smsClassType");
            cachedSerQNames.add(qName);
            cls = SmsClassType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

            qName = new javax.xml.namespace.QName("http://www.telefonica.es/MI/InterfazSimplificado/schemas", "versionType");
            cachedSerQNames.add(qName);
            cls = VersionType.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(enumsf);
            cachedDeserFactories.add(enumdf);

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Throwable _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public SubmitRes smsTextSubmit(SmsTextSubmitReq msgsmsTextSubmitReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("smsTextSubmit");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "smsTextSubmit"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {msgsmsTextSubmitReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (SubmitRes) _resp;
            } catch (java.lang.Exception _exception) {
                return (SubmitRes) org.apache.axis.utils.JavaUtils.convert(_resp, SubmitRes.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public SubmitRes smsBinarySubmit(SmsBinarySubmitReq msgsmsBinarySubmitReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("smsBinarySubmit");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "smsBinarySubmit"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {msgsmsBinarySubmitReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (SubmitRes) _resp;
            } catch (java.lang.Exception _exception) {
                return (SubmitRes) org.apache.axis.utils.JavaUtils.convert(_resp, SubmitRes.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public SubmitRes mmsSubmit(MmsSubmitReq msgmmsSubmitReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("mmsSubmit");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "mmsSubmit"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {msgmmsSubmitReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (SubmitRes) _resp;
            } catch (java.lang.Exception _exception) {
                return (SubmitRes) org.apache.axis.utils.JavaUtils.convert(_resp, SubmitRes.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public SubmitRes wappushSubmit(WappushSubmitReq msgwappushSubmitReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("wappushSubmit");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "wappushSubmit"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {msgwappushSubmitReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (SubmitRes) _resp;
            } catch (java.lang.Exception _exception) {
                return (SubmitRes) org.apache.axis.utils.JavaUtils.convert(_resp, SubmitRes.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public SubmitRes massivePersonalSMSSubmit(MassivePersonalSMSSubmitReq msgmassivePersonalSMSSubmitReq) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[4]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("massivePersonalSMSSubmit");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("", "massivePersonalSMSSubmit"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {msgmassivePersonalSMSSubmitReq});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (SubmitRes) _resp;
            } catch (java.lang.Exception _exception) {
                return (SubmitRes) org.apache.axis.utils.JavaUtils.convert(_resp, SubmitRes.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
