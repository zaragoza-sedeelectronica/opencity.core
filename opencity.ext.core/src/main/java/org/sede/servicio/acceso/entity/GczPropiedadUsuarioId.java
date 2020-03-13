package org.sede.servicio.acceso.entity;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embeddable;

// TODO: Auto-generated Javadoc
/**
 * The Class GczPropiedadUsuarioId.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
@Embeddable
public class GczPropiedadUsuarioId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8472239367485945860L;

	/** id usuario. */
	private BigDecimal idUsuario;
	
	/** etiqueta. */
	private String etiqueta;

	/**
	 * Instantiates a new gcz propiedad usuario id.
	 */
	public GczPropiedadUsuarioId() {
	}

	/**
	 * Instantiates a new gcz propiedad usuario id.
	 *
	 * @param idUsuario Id usuario
	 * @param etiqueta Etiqueta
	 */
	public GczPropiedadUsuarioId(BigDecimal idUsuario, String etiqueta) {
		this.idUsuario = idUsuario;
		this.etiqueta = etiqueta;
	}

	/**
	 * Gets the id usuario.
	 *
	 * @return the id usuario
	 */
	@Column(name = "ID_USUARIO", nullable = false, precision = 22, scale = 0)
	public BigDecimal getIdUsuario() {
		return this.idUsuario;
	}

	/**
	 * Sets the id usuario.
	 *
	 * @param idUsuario the new id usuario
	 */
	public void setIdUsuario(BigDecimal idUsuario) {
		this.idUsuario = idUsuario;
	}

	/**
	 * Gets the etiqueta.
	 *
	 * @return the etiqueta
	 */
	@Column(name = "ETIQUETA", nullable = false, length = 40)
	public String getEtiqueta() {
		return this.etiqueta;
	}

	/**
	 * Sets the etiqueta.
	 *
	 * @param etiqueta the new etiqueta
	 */
	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	/**
	 * Equals.
	 *
	 * @param other Other
	 * @return true, if successful
	 */
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

	/**
	 * Hash code.
	 *
	 * @return int
	 */
	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getIdUsuario() == null ? 0 : this.getIdUsuario().hashCode());
		result = 37 * result
				+ (getEtiqueta() == null ? 0 : this.getEtiqueta().hashCode());
		return result;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "GczPropiedadUsuarioId [idUsuario=" + idUsuario + ", etiqueta="
				+ etiqueta + "]";
	}

}
