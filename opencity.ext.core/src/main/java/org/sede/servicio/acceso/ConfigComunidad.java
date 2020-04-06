package org.sede.servicio.acceso;

import org.sede.core.PropertyFileInterface;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "credencialesComunidad")
public class ConfigComunidad implements PropertyFileInterface {
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
		return "general";
	}
	
	/**
	 * Gets the jndi.
	 *
	 * @return the jndi
	 */
	public String getJndi() {
		return "WebGeneralDS";
	}
	public String getEntity() {
		return "org.sede.servicio.comunidad.entity";
	}

}
