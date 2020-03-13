package org.sede.servicio.acceso.entity;


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicUpdate;
import org.sede.core.anotaciones.SoloEnEstaEntidad;
import org.sede.servicio.acceso.Constants;

// TODO: Auto-generated Javadoc
/**
 * The Class GczPropiedadUsuario.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
@Entity
@Table(name = "GCZ_PROPIEDAD_USUARIO", schema = Constants.ESQUEMA)
@DynamicUpdate(value = true)
public class GczPropiedadUsuario implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3875372855637988537L;

	/** id. */
	private GczPropiedadUsuarioId id;
	
	/** gcz usuario. */
	@SoloEnEstaEntidad
	private GczUsuario gczUsuario;
	
	/** valor. */
	private String valor;

	/**
	 * Instantiates a new gcz propiedad usuario.
	 */
	public GczPropiedadUsuario() {
	}

	/**
	 * Instantiates a new gcz propiedad usuario.
	 *
	 * @param id Id
	 * @param gczUsuario Gcz usuario
	 */
	public GczPropiedadUsuario(GczPropiedadUsuarioId id, GczUsuario gczUsuario) {
		this.id = id;
		this.gczUsuario = gczUsuario;
	}

	/**
	 * Instantiates a new gcz propiedad usuario.
	 *
	 * @param id Id
	 * @param gczUsuario Gcz usuario
	 * @param valor Valor
	 */
	public GczPropiedadUsuario(GczPropiedadUsuarioId id, GczUsuario gczUsuario,
			String valor) {
		this.id = id;
		this.gczUsuario = gczUsuario;
		this.valor = valor;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idUsuario", column = @Column(name = "ID_USUARIO", nullable = false, precision = 22, scale = 0)),
			@AttributeOverride(name = "etiqueta", column = @Column(name = "ETIQUETA", nullable = false, length = 40)) })
	public GczPropiedadUsuarioId getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(GczPropiedadUsuarioId id) {
		this.id = id;
	}

	/**
	 * Gets the gcz usuario.
	 *
	 * @return the gcz usuario
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false, insertable = false, updatable = false)
	public GczUsuario getGczUsuario() {
		return this.gczUsuario;
	}

	/**
	 * Sets the gcz usuario.
	 *
	 * @param gczUsuario the new gcz usuario
	 */
	public void setGczUsuario(GczUsuario gczUsuario) {
		this.gczUsuario = gczUsuario;
	}

	/**
	 * Gets the valor.
	 *
	 * @return the valor
	 */
	@Column(name = "VALOR", length = 200)
	public String getValor() {
		return this.valor;
	}

	/**
	 * Sets the valor.
	 *
	 * @param valor the new valor
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "GczPropiedadUsuario [id=" + id + ", valor=" + valor + "]";
	}

}
