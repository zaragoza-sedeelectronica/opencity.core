package org.sede.servicio.acceso.dao;

import org.sede.core.exception.ErrorEnPeticionHMAC;
import org.sede.core.rest.Hmac;
import org.sede.core.rest.Peticion;
import org.sede.core.rest.Role;
import org.sede.servicio.acceso.entity.Credenciales;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ComprobarIntegridad {
	private static final Logger logger = LoggerFactory
			.getLogger(ComprobarIntegridad.class);
	
	@Autowired
	private GczUsuarioGenericDAO daoAcceso;
	
    public void revisarMensaje(Peticion peticion, boolean usuarioEnSesion) throws ErrorEnPeticionHMAC {
    	String clientId = peticion.getClientId();
    	if (clientId == null) {
    		peticion.setTipoAcceso(Role.PUBLICO);
		} else {
			if (!usuarioEnSesion) {
				comprobarIntegridadDelMensaje(peticion, clientId);
			}
			peticion.setTipoAcceso(Role.ADMINISTRADOR);
		}
    }

    private void comprobarIntegridadDelMensaje(Peticion peticion, String clientId) throws ErrorEnPeticionHMAC {
    	Credenciales credenciales = getCredenciales(clientId);
    	peticion.setCredenciales(credenciales);
    	String contenido = peticion.getCuerpoPeticion();
        String contenidoCompleto  = clientId + peticion.getMetodoHttp() + peticion.getUri() + peticion.getQueryString() + contenido;
        if (peticion.isDebug()) {
        	logger.error("CONTENT TYPE:{}", peticion.getContentType());
        	logger.error("CONTENIDO RECIBIDO:{}", peticion.getCuerpoPeticion());
        	logger.error(contenidoCompleto);
        	logger.error("PK EnBBDD:{}", credenciales.getUsuario().getSecretKey());
        	logger.error("HMACCALCULADA:{}", Hmac.calcular(contenidoCompleto, credenciales.getUsuario().getSecretKey()));
        	logger.error("HMACRECIBIDA:{}", peticion.getHmac());
        }
        if (credenciales.getUsuario().getSecretKey() == null || !Hmac.calcular(contenidoCompleto, credenciales.getUsuario().getSecretKey()).equals(peticion.getHmac())) {
    		throw new ErrorEnPeticionHMAC();
    	}
		
	}
	
	private Credenciales getCredenciales(String strLogin) {
		return daoAcceso.getCredenciales(strLogin);

	}
}
