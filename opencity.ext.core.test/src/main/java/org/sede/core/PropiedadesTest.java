package org.sede.core;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class PropiedadesTest {
	
	public static final String CLIENTID = "test.usuario";
	public static final String PK = "test.usuariopk";
	
	
	/**
	 * Nombre del fichero de propiedades
	 */
	private static final String BUNDLE_NAME = "test";
	/**
	 * Recurso
	 */
	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);
	/**
	 * Constructor
	 *
	 */
	private PropiedadesTest() {
		super();
	}
	/**
	 * 
	 * @param key Elemento a recoger
	 * @return valor del elemento en el fichero
	 */
	public static String getString(final String key) {
		String retorno;
		try {
			retorno = RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			retorno = '!' + key + '!';
		}
		return retorno;
	}
	public static String getClientID() {
		return getString(CLIENTID);
	}
	public static String getPK() {
		return getString(PK);
	}
	public static String getUserId() {
		return getString("test.ciudadanoid");
	}
	public static String getCiudadanoLogin() {
		return getString("test.ciudadano.login");
	}
	public static String getCiudadanoPassword() {
		return getString("test.ciudadano.password");
	}
	
}
