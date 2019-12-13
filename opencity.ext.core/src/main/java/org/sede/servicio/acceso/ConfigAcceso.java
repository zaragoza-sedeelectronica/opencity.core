package org.sede.servicio.acceso;

import org.sede.core.PropertyFileInterface;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "credencialesCfg")
public class ConfigAcceso implements PropertyFileInterface {
	public String getSchema() {
		return "movil";
	}
	public String getJndi() {
		return "WebMovilDS";
	}

	public String getEntity() {
		return "org.sede.servicio.acceso.entity";
	}

}
