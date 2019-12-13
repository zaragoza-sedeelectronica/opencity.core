package org.sede.servicio.sms.ws.definitions;

import org.sede.servicio.sms.ws.schemas.MassivePersonalSMSSubmitReq;
import org.sede.servicio.sms.ws.schemas.MmsSubmitReq;
import org.sede.servicio.sms.ws.schemas.SmsBinarySubmitReq;
import org.sede.servicio.sms.ws.schemas.SmsTextSubmitReq;
import org.sede.servicio.sms.ws.schemas.SubmitRes;
import org.sede.servicio.sms.ws.schemas.WappushSubmitReq;

public class MIInterfazSimplificadoPortTypeProxy implements MIInterfazSimplificadoPortType {
  private String _endpoint = null;
  private MIInterfazSimplificadoPortType mIInterfazSimplificadoPortType = null;
  
  public MIInterfazSimplificadoPortTypeProxy() {
    _initMIInterfazSimplificadoPortTypeProxy();
  }
  
  public MIInterfazSimplificadoPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initMIInterfazSimplificadoPortTypeProxy();
  }
  
  private void _initMIInterfazSimplificadoPortTypeProxy() {
    try {
      mIInterfazSimplificadoPortType = (new MIInterfazSimplificadoLocator()).getMIInterfazSimplificadoPort();
      if (mIInterfazSimplificadoPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)mIInterfazSimplificadoPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)mIInterfazSimplificadoPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (mIInterfazSimplificadoPortType != null)
      ((javax.xml.rpc.Stub)mIInterfazSimplificadoPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public MIInterfazSimplificadoPortType getMIInterfazSimplificadoPortType() {
    if (mIInterfazSimplificadoPortType == null)
      _initMIInterfazSimplificadoPortTypeProxy();
    return mIInterfazSimplificadoPortType;
  }
  
  public SubmitRes smsTextSubmit(SmsTextSubmitReq msgsmsTextSubmitReq) throws java.rmi.RemoteException{
    if (mIInterfazSimplificadoPortType == null)
      _initMIInterfazSimplificadoPortTypeProxy();
    return mIInterfazSimplificadoPortType.smsTextSubmit(msgsmsTextSubmitReq);
  }
  
  public SubmitRes smsBinarySubmit(SmsBinarySubmitReq msgsmsBinarySubmitReq) throws java.rmi.RemoteException{
    if (mIInterfazSimplificadoPortType == null)
      _initMIInterfazSimplificadoPortTypeProxy();
    return mIInterfazSimplificadoPortType.smsBinarySubmit(msgsmsBinarySubmitReq);
  }
  
  public SubmitRes mmsSubmit(MmsSubmitReq msgmmsSubmitReq) throws java.rmi.RemoteException{
    if (mIInterfazSimplificadoPortType == null)
      _initMIInterfazSimplificadoPortTypeProxy();
    return mIInterfazSimplificadoPortType.mmsSubmit(msgmmsSubmitReq);
  }
  
  public SubmitRes wappushSubmit(WappushSubmitReq msgwappushSubmitReq) throws java.rmi.RemoteException{
    if (mIInterfazSimplificadoPortType == null)
      _initMIInterfazSimplificadoPortTypeProxy();
    return mIInterfazSimplificadoPortType.wappushSubmit(msgwappushSubmitReq);
  }
  
  public SubmitRes massivePersonalSMSSubmit(MassivePersonalSMSSubmitReq msgmassivePersonalSMSSubmitReq) throws java.rmi.RemoteException{
    if (mIInterfazSimplificadoPortType == null)
      _initMIInterfazSimplificadoPortTypeProxy();
    return mIInterfazSimplificadoPortType.massivePersonalSMSSubmit(msgmassivePersonalSMSSubmitReq);
  }
  
  
}