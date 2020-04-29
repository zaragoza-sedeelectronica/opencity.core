package org.sede.servicio.acceso;

import org.sede.core.PropertyFileInterface;
import org.sede.core.anotaciones.Esquema;
import org.springframework.context.annotation.Configuration;

/**
 * The Class ConfigCiudadano.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
@Configuration
public class ConfigCiudadano implements PropertyFileInterface {
	
	/** Constant ESQUEMA. */
	public static final String ESQUEMA = "GENERAL";
	
	/** Constant TM. */
	public static final String TM = "transactionManagerGeneral";
	
	/**
	 * Gets the schema.
	 *
	 * @return the schema
	 */
	public String getSchema() {
		return ESQUEMA.toLowerCase();
	}
	
	/**
	 * Gets the jndi.
	 *
	 * @return the jndi
	 */
	public String getJndi() {
		return "WebGeneralDS";
	}

	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	public String getEntity() {
		return "org.sede.servicio.acceso.entity";
	}

}
