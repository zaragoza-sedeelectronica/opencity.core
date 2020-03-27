package org.sede.servicio.acceso.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * The Class GczSeccionId.
 *
 * @autor Ayuntamiento de Zaragoza
 *
 */
@Embeddable
public class GczSeccionId implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8355530779366240713L;

	/** codigo seccion. */
	private String codigoSeccion;
	
	/** codigo servicio. */
	private String codigoServicio;

	/**
	 * Instantiates a new gcz seccion id.
	 */
	public GczSeccionId() {
	}

	/**
	 * Instantiates a new gcz seccion id.
	 *
	 * @param codigoSeccion Codigo seccion
	 * @param codigoServicio Codigo servicio
	 */
	public GczSeccionId(String codigoSeccion, String codigoServicio) {
		this.codigoSeccion = codigoSeccion;
		this.codigoServicio = codigoServicio;
	}

	/**
	 * Gets the codigo seccion.
	 *
	 * @return the codigo seccion
	 */
	@Column(name = "CODIGO_SECCION", nullable = false, length = 30)
	public String getCodigoSeccion() {
		return this.codigoSeccion == null ? null : this.codigoSeccion.toUpperCase();
	}

	/**
	 * Sets the codigo seccion.
	 *
	 * @param codigoSeccion the new codigo seccion
	 */
	public void setCodigoSeccion(String codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	/**
	 * Gets the codigo servicio.
	 *
	 * @return the codigo servicio
	 */
	@Column(name = "CODIGO_SERVICIO", nullable = false, length = 30)
	public String getCodigoServicio() {
		return this.codigoServicio;
	}

	/**
	 * Sets the codigo servicio.
	 *
	 * @param codigoServicio the new codigo servicio
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
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

	/**
	 * Hash code.
	 *
	 * @return int
	 */
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

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "GczSeccionId [codigoSeccion=" + codigoSeccion
				+ ", codigoServicio=" + codigoServicio + "]";
	}

}
