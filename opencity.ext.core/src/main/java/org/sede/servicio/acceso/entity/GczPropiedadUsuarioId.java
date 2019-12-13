package org.sede.servicio.acceso.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class GczPropiedadUsuarioId implements java.io.Serializable {

	private BigDecimal idUsuario;
	private String etiqueta;

	public GczPropiedadUsuarioId() {
	}

	public GczPropiedadUsuarioId(BigDecimal idUsuario, String etiqueta) {
		this.idUsuario = idUsuario;
		this.etiqueta = etiqueta;
	}

	@Column(name = "ID_USUARIO", nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdUsuario() {
		return this.idUsuario;
	}

	public void setIdUsuario(BigDecimal idUsuario) {
		this.idUsuario = idUsuario;
	}

	@Column(name = "ETIQUETA", nullable = false, length = 40)
	public String getEtiqueta() {
		return this.etiqueta;
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof GczPropiedadUsuarioId))
			return false;
		GczPropiedadUsuarioId castOther = (GczPropiedadUsuarioId) other;

		return ((this.getIdUsuario() == castOther.getIdUsuario()) || (this
				.getIdUsuario() != null && castOther.getIdUsuario() != null && this
				.getIdUsuario().equals(castOther.getIdUsuario())))
				&& ((this.getEtiqueta() == castOther.getEtiqueta()) || (this
						.getEtiqueta() != null
						&& castOther.getEtiqueta() != null && this
						.getEtiqueta().equals(castOther.getEtiqueta())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getIdUsuario() == null ? 0 : this.getIdUsuario().hashCode());
		result = 37 * result
				+ (getEtiqueta() == null ? 0 : this.getEtiqueta().hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "GczPropiedadUsuarioId [idUsuario=" + idUsuario + ", etiqueta="
				+ etiqueta + "]";
	}

}
