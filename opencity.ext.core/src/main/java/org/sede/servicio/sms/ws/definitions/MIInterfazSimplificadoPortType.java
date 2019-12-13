/**
 * MIInterfazSimplificadoPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package org.sede.servicio.sms.ws.definitions;

import org.sede.servicio.sms.ws.schemas.MassivePersonalSMSSubmitReq;
import org.sede.servicio.sms.ws.schemas.MmsSubmitReq;
import org.sede.servicio.sms.ws.schemas.SmsBinarySubmitReq;
import org.sede.servicio.sms.ws.schemas.SmsTextSubmitReq;
import org.sede.servicio.sms.ws.schemas.SubmitRes;
import org.sede.servicio.sms.ws.schemas.WappushSubmitReq;

public interface MIInterfazSimplificadoPortType extends java.rmi.Remote {
    public SubmitRes smsTextSubmit(SmsTextSubmitReq msgsmsTextSubmitReq) throws java.rmi.RemoteException;
    public SubmitRes smsBinarySubmit(SmsBinarySubmitReq msgsmsBinarySubmitReq) throws java.rmi.RemoteException;
    public SubmitRes mmsSubmit(MmsSubmitReq msgmmsSubmitReq) throws java.rmi.RemoteException;
    public SubmitRes wappushSubmit(WappushSubmitReq msgwappushSubmitReq) throws java.rmi.RemoteException;
    public SubmitRes massivePersonalSMSSubmit(MassivePersonalSMSSubmitReq msgmassivePersonalSMSSubmitReq) throws java.rmi.RemoteException;
}
