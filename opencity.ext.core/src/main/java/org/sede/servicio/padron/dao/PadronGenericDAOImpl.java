package org.sede.servicio.padron.dao;

import org.sede.core.utils.Propiedades;
import org.sede.servicio.padron.PadronController;
import org.sede.servicio.padron.dao.ws.PadronWS2TCPortTypeProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class PadronGenericDAOImpl implements PadronGenericDAO {
	private static final Logger logger = LoggerFactory.getLogger(PadronController.class);

	public Boolean checkEmpadronado(String d, Integer day, Integer m, Integer a) {
		PadronWS2TCPortTypeProxy padronWS2TCTest;
		try {
			int tipoPrimero = 1;
			int tipoSegundo = 2;
			d = d.toUpperCase();
			if (d.indexOf('X') == 0 || d.indexOf('Y') == 0 || d.indexOf('Z') == 0) {
				tipoPrimero = 3;
				tipoSegundo = 2;
			}
			padronWS2TCTest = new PadronWS2TCPortTypeProxy(Propiedades.getString("padron.ws"));
			boolean isEmpadronado = padronWS2TCTest.getEmpadronadoFechaNacimiento(tipoPrimero, d, new Long(day), new Long(m), new Long(a));
			if (isEmpadronado) {
				logger.error("Empadronado 1: " + d + ":" + day + "-" + m + "-" + a + " desde:" + Propiedades.getString("padron.ws"));
				return true;
			} else {
				isEmpadronado = padronWS2TCTest.getEmpadronadoFechaNacimiento(tipoSegundo, d.toUpperCase(), new Long(day), new Long(m), new Long(a));
				if (isEmpadronado) {
					logger.error("Empadronado 2: " + d + ":" + day + "-" + m + "-" + a + " desde:" + Propiedades.getString("padron.ws"));
					return true;
				} else {
					logger.error("No empadronado: " + d + ":" + day + "-" + m + "-" + a + " desde:" + Propiedades.getString("padron.ws"));
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("Error al obtener padron: " + d + ":" + day + "-" + m + "-" + a + " ERROR: " + e.getMessage() + " desde:" + Propiedades.getString("padron.ws"));
			return null;
		}
	}
	
	public Boolean checkEmpadronado(String d, Integer a) {
		PadronWS2TCPortTypeProxy padronWS2TCTest;
		try {
			int tipoPrimero = 1;
			int tipoSegundo = 2;
			d = d.toUpperCase();
			if (d.indexOf('X') == 0 || d.indexOf('Y') == 0 || d.indexOf('Z') == 0) {
				tipoPrimero = 3;
				tipoSegundo = 2;
			}
			padronWS2TCTest = new PadronWS2TCPortTypeProxy(Propiedades.getString("padron.ws"));
			boolean isEmpadronado = padronWS2TCTest.getEmpadronado(tipoPrimero, d, new Long(a));
			if (isEmpadronado) {
				logger.error("Empadronado 1: " + d + ":" + a + " desde:" + Propiedades.getString("padron.ws"));
				return true;
			} else {
				isEmpadronado = padronWS2TCTest.getEmpadronado(tipoSegundo, d.toUpperCase(), new Long(a));
				if (isEmpadronado) {
					logger.error("Empadronado 2: " + d + ":" + a + " desde:" + Propiedades.getString("padron.ws"));
					return true;
				} else {
					logger.error("No empadronado: " + d + ":" + a + " desde:" + Propiedades.getString("padron.ws"));
					return false;
				}
			}
		} catch (Exception e) {
			logger.error("Error al obtener padron: " + d + ":" + a + " ERROR: " + e.getMessage() + " desde:" + Propiedades.getString("padron.ws"));
			return null;
		}
	}

	public String showDistrito(String d, Integer a) {
		PadronWS2TCPortTypeProxy padronWS2TCTest;
		try {
			int tipoPrimero = 1;
			int tipoSegundo = 2;
			d = d.toUpperCase();
			if (d.indexOf('X') == 0 || d.indexOf('Y') == 0 || d.indexOf('Z') == 0) {
				tipoPrimero = 3;
				tipoSegundo = 2;
			}
			padronWS2TCTest = new PadronWS2TCPortTypeProxy(Propiedades.getString("padron.ws"));
			String distrito = padronWS2TCTest.getEmpadronadoDS(tipoPrimero, d, new Long(a));
			if (distrito != null && !"".equals(distrito)) {
				logger.error("distrito obtenido 1: " + d + ":" + a + distrito + " desde:" + Propiedades.getString("padron.ws"));
				return distrito;
			} else {
				distrito = padronWS2TCTest.getEmpadronadoDS(tipoSegundo, d.toUpperCase(), new Long(a));
				if (distrito != null && !"".equals(distrito)) {
					logger.error("Distrito obtenido 2: " + d + ":" + a + distrito + " desde:" + Propiedades.getString("padron.ws"));
					return distrito;
				} else {
					logger.error("Distrito NO obtenido: " + d + ":" + a + " desde:" + Propiedades.getString("padron.ws"));
					return null;
				}
			}
		} catch (Exception e) {
			logger.error("Error al obtener distrito: " + d + ":" + a + " ERROR: " + e.getMessage() + " desde:" + Propiedades.getString("padron.ws"));
			return null;
		}
	}
	
	public String showJunta(String d, Integer a) {
		PadronWS2TCPortTypeProxy padronWS2TCTest;
		try {
			int tipoPrimero = 1;
			int tipoSegundo = 2;
			d = d.toUpperCase();
			if (d.indexOf('X') == 0 || d.indexOf('Y') == 0 || d.indexOf('Z') == 0) {
				tipoPrimero = 3;
				tipoSegundo = 2;
			}
			padronWS2TCTest = new PadronWS2TCPortTypeProxy(Propiedades.getString("padron.ws"));
			String junta = padronWS2TCTest.getEmpadronadoJunta(tipoPrimero, d, new Long(a));
			if (junta != null && !"".equals(junta)) {
				logger.error("junta obtenida 1: " + d + ":" + a + ":" + junta.replaceAll("_", " ") + " desde:" + Propiedades.getString("padron.ws"));
				return junta.replaceAll("_", " ").replaceAll("¿", "ñ");
			} else {
				junta = padronWS2TCTest.getEmpadronadoJunta(tipoSegundo, d.toUpperCase(), new Long(a));
				if (junta != null && !"".equals(junta)) {
					logger.error("junta obtenida 2: " + d + ":" + a + ":" + junta.replaceAll("_", " ") + " desde:" + Propiedades.getString("padron.ws"));
					return junta.replaceAll("_", " ").replaceAll("¿", "ñ");
				} else {
					logger.error("junta no obtenida: " + d + ":" + a + " desde:" + Propiedades.getString("padron.ws"));
					return null;
				}
			}
		} catch (Exception e) {
			logger.error("Error al obtener junta: " + d + ":" + a + " ERROR: " + e.getMessage() + " desde:" + Propiedades.getString("padron.ws"));
			return null;
		}
	}
	
}