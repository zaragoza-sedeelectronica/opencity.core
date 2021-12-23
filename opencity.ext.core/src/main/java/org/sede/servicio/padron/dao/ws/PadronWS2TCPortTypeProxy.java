package org.sede.servicio.padron.dao.ws;

import java.rmi.RemoteException;

public class PadronWS2TCPortTypeProxy implements org.sede.servicio.padron.dao.ws.PadronWS2TCPortType {
  private String _endpoint = null;
  private org.sede.servicio.padron.dao.ws.PadronWS2TCPortType padronWS2TCPortType = null;
  
  public PadronWS2TCPortTypeProxy() {
    _initPadronWS2TCPortTypeProxy();
  }
  
  public PadronWS2TCPortTypeProxy(String endpoint) {
    _endpoint = endpoint;
    _initPadronWS2TCPortTypeProxy();
  }
  
  private void _initPadronWS2TCPortTypeProxy() {
    try {
      padronWS2TCPortType = (new org.sede.servicio.padron.dao.ws.PadronWS2TCLocator()).getPadronWS2TCPort();
      if (padronWS2TCPortType != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)padronWS2TCPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)padronWS2TCPortType)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (padronWS2TCPortType != null)
      ((javax.xml.rpc.Stub)padronWS2TCPortType)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public org.sede.servicio.padron.dao.ws.PadronWS2TCPortType getPadronWS2TCPortType() {
    if (padronWS2TCPortType == null)
      _initPadronWS2TCPortTypeProxy();
    return padronWS2TCPortType;
  }
  
  public boolean getEmpadronado(int tipoIdentificacion, java.lang.String nif, long anioNacimiento) throws java.rmi.RemoteException{
    if (padronWS2TCPortType == null)
      _initPadronWS2TCPortTypeProxy();
    return padronWS2TCPortType.getEmpadronado(tipoIdentificacion, nif, anioNacimiento);
  }
  
  public java.lang.String getEmpadronadoIdent(int tipoIdentificacion, java.lang.String nif, long anioNacimiento) throws java.rmi.RemoteException{
    if (padronWS2TCPortType == null)
      _initPadronWS2TCPortTypeProxy();
    return padronWS2TCPortType.getEmpadronadoIdent(tipoIdentificacion, nif, anioNacimiento);
  }
  
  public java.lang.String getEmpadronadoDS(int tipoIdentificacion, java.lang.String nif, long anioNacimiento) throws java.rmi.RemoteException{
    if (padronWS2TCPortType == null)
      _initPadronWS2TCPortTypeProxy();
    return padronWS2TCPortType.getEmpadronadoDS(tipoIdentificacion, nif, anioNacimiento);
  }
  
  public java.lang.String getEmpadronadoJunta(int tipoIdentificacion, java.lang.String nif, long anioNacimiento) throws java.rmi.RemoteException{
    if (padronWS2TCPortType == null)
      _initPadronWS2TCPortTypeProxy();
    return padronWS2TCPortType.getEmpadronadoJunta(tipoIdentificacion, nif, anioNacimiento);
  }

	@Override
	public boolean getEmpadronadoFechaNacimiento(int tipoIdentificacion, String nif, Long diaNacimiento, Long mesNacimiento,
			Long anioNacimiento) throws RemoteException {
		if (padronWS2TCPortType == null)
		      _initPadronWS2TCPortTypeProxy();
		    return padronWS2TCPortType.getEmpadronadoFechaNacimiento(tipoIdentificacion, nif, diaNacimiento, mesNacimiento, anioNacimiento);
	}
  
  
}