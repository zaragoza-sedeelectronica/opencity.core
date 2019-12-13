package org.sede.servicio.acceso.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GczSeccionId implements java.io.Serializable {

	private String codigoSeccion;
	private String codigoServicio;

	public GczSeccionId() {
	}

	public GczSeccionId(String codigoSeccion, String codigoServicio) {
		this.codigoSeccion = codigoSeccion;
		this.codigoServicio = codigoServicio;
	}

	@Column(name = "CODIGO_SECCION", nullable = false, length = 30)
	public String getCodigoSeccion() {
		return this.codigoSeccion == null ? null : this.codigoSeccion.toUpperCase();
	}

	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	@Column(name = "CODIGO_SERVICIO", nullable = false, length = 30)
	public String getCodigoServicio() {
		return this.codigoServicio;
	}

	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof GczSeccionId))
			return false;
		GczSeccionId castOther = (GczSeccionId) other;

		return ((this.getCodigoSeccion() == castOther.getCodigoSeccion()) || (this
				.getCodigoSeccion() != null
				&& castOther.getCodigoSeccion() != null && this
				.getCodigoSeccion().equals(castOther.getCodigoSeccion())))
				&& ((this.getCodigoServicio().equals(castOther.getCodigoServicio())) || (this
						.getCodigoServicio() != null
						&& castOther.getCodigoServicio() != null && this
						.getCodigoServicio().equals(
								castOther.getCodigoServicio())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getCodigoSeccion() == null ? 0 : this.getCodigoSeccion()
						.hashCode());
		result = 37
				* result
				+ (getCodigoServicio() == null ? 0 : this.getCodigoServicio()
						.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "GczSeccionId [codigoSeccion=" + codigoSeccion
				+ ", codigoServicio=" + codigoServicio + "]";
	}

}
