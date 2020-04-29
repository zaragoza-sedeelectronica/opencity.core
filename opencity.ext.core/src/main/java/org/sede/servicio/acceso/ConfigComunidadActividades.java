package org.sede.servicio.acceso;

import org.sede.core.PropertyFileInterface;
import org.sede.core.anotaciones.Esquema;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "credencialesComunidadActividades")
public class ConfigComunidadActividades implements PropertyFileInterface {
	public static final String ESQUEMA = "GENERAL";
	public static final String TM = "transactionManagerGeneral";
	
	public String getSchema() {
		return ESQUEMA.toLowerCase();
	}
	public String getJndi() {
		return "WebGeneralDS";
	}

	public String getEntity() {
		return "org.sede.servicio.actividades.entity";
	}

}
