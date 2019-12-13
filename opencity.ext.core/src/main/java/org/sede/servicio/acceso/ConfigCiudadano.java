package org.sede.servicio.acceso;

import org.sede.core.PropertyFileInterface;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigCiudadano implements PropertyFileInterface {
	public String getSchema() {
		return "noticias";
	}
	public String getJndi() {
		return "WebNoticiasDS";
	}

	public String getEntity() {
		return "org.sede.servicio.acceso.entity";
	}

}
