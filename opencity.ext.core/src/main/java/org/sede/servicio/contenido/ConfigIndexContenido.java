package org.sede.servicio.contenido;

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
@Configuration(value = "indexContenidoCfg")
public class ConfigIndexContenido implements PropertyFileInterface {
	
	/** Constant ESQUEMA. */
	public static final String ESQUEMA = Esquema.INTRA;
	
	/** Constant TM. */
	public static final String TM = Esquema.TMINTRA;
	
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
		return "WebIntraDS";
	}

	/**
	 * Gets the entity.
	 *
	 * @return the entity
	 */
	public String getEntity() {
		return "org.sede.servicio.contenido.entity";
	}

}
