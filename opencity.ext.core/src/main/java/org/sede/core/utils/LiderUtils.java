package org.sede.core.utils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.sede.servicio.acceso.entity.Credenciales;
import org.sede.servicio.acceso.entity.Lider;

public class LiderUtils {
	private LiderUtils() {
		super();
	}
	public static List<Lider> liderazgosGestor(String entity, Credenciales credenciales) {
		List<Lider> retorno = new ArrayList<Lider>();
		if (credenciales.getLider() != null) {
			for (Lider l : credenciales.getLider()) {
				if (entity.equals(l.getAssociatedType())) {
					retorno.add(l);
				}
			}
		}
		
		return retorno;
	}
	public static boolean puedeGestionarRecurso(String entity, Credenciales credenciales, BigDecimal identificador) {
		boolean retorno = false;
		if (credenciales != null) {
			List<Lider> lista = LiderUtils.liderazgosGestor(entity, credenciales);
			for (Lider l : lista) {
				if (l.getAssociatedId().equals(identificador)) {
					retorno = true;
					break;
				}
			}
		}
		
		return retorno;
	}
	
	public static BigDecimal getRecursoQueLidera(Credenciales credenciales, Class<?> clase) {
		if (credenciales != null) {
			List<Lider> lista = LiderUtils.liderazgosGestor(clase.getName(), credenciales);
			for (Lider l : lista) {
				return l.getAssociatedId();
			}
		}
		return null;
	}
	
	public static List<BigDecimal> getRecursosQueLidera(Credenciales credenciales, Class<?> clase) {
		List<BigDecimal> ids = new ArrayList<BigDecimal>();
		if (credenciales != null) {
			List<Lider> lista = LiderUtils.liderazgosGestor(clase.getName(), credenciales);
			for (Lider l : lista) {
				ids.add(l.getAssociatedId());
			}
		}
		return ids;
	}
}
