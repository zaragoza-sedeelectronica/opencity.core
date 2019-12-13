package org.sede.core.rest;

import org.apache.commons.codec.digest.HmacUtils;

public class Hmac {
	private Hmac() {
		super();
	}
	public static String calcular(String texto, String clave) {
	 
		return HmacUtils.hmacSha1Hex(clave, texto);
	}
	
	//Cambio para la sede modularizada
	public static String calcular(String texto, String clave, Object obj) {
		 
		return HmacUtils.hmacSha1Hex(clave, texto);
	}
	

}
