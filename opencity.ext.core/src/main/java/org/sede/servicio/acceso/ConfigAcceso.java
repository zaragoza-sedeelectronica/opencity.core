package org.sede.servicio.acceso;

import org.sede.core.PropertyFileInterface;
import org.sede.core.anotaciones.Esquema;
import org.springframework.context.annotation.Configuration;

// TODO: Auto-generated Javadoc
/**
 * Class ConfigAcceso.
 * 
 * @author Ayuntamiento Zaragoza
 * 
 */
@Configuration(value = "credencialesCfg")
public class ConfigAcceso implements PropertyFileInterface {
	
	/** Constant ESQUEMA. */
	public static final String ESQUEMA = Esquema.MOVIL;
	
	/** Constant TM. */
	public static final String TM = Esquema.TMMOVIL;
	
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
		return "WebMovilDS";
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
