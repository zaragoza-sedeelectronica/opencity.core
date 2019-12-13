package org.sede.core.entity;

import org.sede.servicio.equipamiento.entity.PropiedadSemanticaCentro;

public interface TienePropiedad {

	public PropiedadSemanticaCentro getPropiedad();

	public void setPropiedad(PropiedadSemanticaCentro propiedad);

	public String getValor();

	public void setValor(String valor);
}
