package org.sede.servicio.sms.dao;

import org.apache.commons.lang3.StringUtils;
import org.sede.core.rest.Mensaje;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.Propiedades;
import org.sede.servicio.sms.ws.definitions.MIInterfazSimplificadoBindingStub;
import org.sede.servicio.sms.ws.definitions.MIInterfazSimplificadoLocator;
import org.sede.servicio.sms.ws.schemas.SmsTextSubmitReq;
import org.sede.servicio.sms.ws.schemas.SubmitRes;
import org.sede.servicio.sms.ws.schemas.VersionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;


@Repository
public class SmsGenericDAOImpl implements SmsGenericDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(SmsGenericDAOImpl.class);
	
	static MIInterfazSimplificadoLocator localizador = new MIInterfazSimplificadoLocator();

	static MIInterfazSimplificadoBindingStub cliente = null; 

	static SmsTextSubmitReq sms = new SmsTextSubmitReq();

	@Override
	public Mensaje send(String[] phones, String message) {
		StringBuilder telefonos = new StringBuilder();
		for (String phone : phones) {
			telefonos.append(phone + " ");
		}
		if (StringUtils.isEmpty(Propiedades.getSmsServer())) {
			
			logger.error("Servidor de sms no especificado: Destino: " + telefonos.toString() + " Msg: " + message);
			return new Mensaje(HttpStatus.OK.value(), "Enviado");
		} else {
			try {
				
				Funciones.setProxy();

//	logger.error("javax.net.ssl.trustStore: " + Propiedades.getSmsKeyStore());
//				System.setProperty("javax.net.debug", "all");
//				System.setProperty("javax.net.ssl.trustStore", Propiedades.getSmsKeyStore());
				
				// Indicamos los datos de autenticación
				sms.setVersion(VersionType.fromString("1.0"));
				sms.setAuthorization((Propiedades.getSmsUser() + ":" + Propiedades.getSmsPass()).getBytes()); 
				// Indicamos el remitente 
				sms.setSender(Propiedades.getSmsFrom());
				// Indicamos la lista de destinatarios 
				sms.setRecipients(phones);
				// Indicamos el texto del SMS 
				sms.setSMSText(message);
				//Indicamos la URL de envío 
				localizador.setMIInterfazSimplificadoPortEndpointAddress(Propiedades.getSmsServer());
				// Generamos un cliente a partir del localizador 
				cliente = (MIInterfazSimplificadoBindingStub) localizador.getMIInterfazSimplificadoPort(); 
				// Enviamos la petición y almacenamos la respuesta 
				SubmitRes respuesta = cliente.smsTextSubmit(sms); 
				if ("1000".equals(respuesta.getStatus().getStatusCode())) {
					logger.error("SMS Enviado: " + telefonos.toString() + " Msg: " + message);
					return new Mensaje(HttpStatus.OK.value(), "Enviado");
				} else {
					logger.error("SMS NO Enviado: " + telefonos.toString() + " Msg: " + message + " respuesta: " + respuesta.getStatus().getStatusText());
					return new Mensaje(HttpStatus.BAD_REQUEST.value(), Integer.parseInt(respuesta.getStatus().getStatusCode()), respuesta.getStatus().getStatusText());
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				return new Mensaje(HttpStatus.BAD_REQUEST.value(), e.getMessage());
			}
		}
	}

}