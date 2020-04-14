package org.sede.servicio.acceso;

import org.sede.core.PropertyFileInterface;
import org.sede.core.anotaciones.Esquema;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "credencialesComunidadActividades")
public class ConfigComunidadActividades implements PropertyFileInterface {
	public static final String ESQUEMA = Esquema.MOVIL;
	public static final String TM = Esquema.TMMOVIL;
	
	public String getSchema() {
		return ESQUEMA.toLowerCase();
	}
	public String getJndi() {
		return "WebMovilDS";
	}

	public String getEntity() {
		return "org.sede.servicio.actividades.entity";
	}

}
