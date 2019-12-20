package org.sede.servicio.acceso;

import org.sede.core.PropertyFileInterface;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "credencialesCfg")
public class ConfigAcceso implements PropertyFileInterface {
	
	public static final String ESQUEMA = "GENERAL";
	public static final String TM = "transactionManagerGeneral";
	
	public String getSchema() {
		return "general";
	}
	public String getJndi() {
		return "WebGeneralDS";
	}

	public String getEntity() {
		return "org.sede.servicio.acceso.entity";
	}

}
