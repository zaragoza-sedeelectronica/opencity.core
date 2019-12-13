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

@Entity
@Table(name = "GCZ_PROPIEDAD_USUARIO", schema = Constants.ESQUEMA)
@DynamicUpdate(value = true)
public class GczPropiedadUsuario implements java.io.Serializable {

	private GczPropiedadUsuarioId id;
	@SoloEnEstaEntidad
	private GczUsuario gczUsuario;
	private String valor;

	public GczPropiedadUsuario() {
	}

	public GczPropiedadUsuario(GczPropiedadUsuarioId id, GczUsuario gczUsuario) {
		this.id = id;
		this.gczUsuario = gczUsuario;
	}

	public GczPropiedadUsuario(GczPropiedadUsuarioId id, GczUsuario gczUsuario,
			String valor) {
		this.id = id;
		this.gczUsuario = gczUsuario;
		this.valor = valor;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "idUsuario", column = @Column(name = "ID_USUARIO", nullable = false, precision = 22, scale = 0)),
			@AttributeOverride(name = "etiqueta", column = @Column(name = "ETIQUETA", nullable = false, length = 40)) })
	public GczPropiedadUsuarioId getId() {
		return this.id;
	}

	public void setId(GczPropiedadUsuarioId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_USUARIO", nullable = false, insertable = false, updatable = false)
	public GczUsuario getGczUsuario() {
		return this.gczUsuario;
	}

	public void setGczUsuario(GczUsuario gczUsuario) {
		this.gczUsuario = gczUsuario;
	}

	@Column(name = "VALOR", length = 200)
	public String getValor() {
		return this.valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "GczPropiedadUsuario [id=" + id + ", valor=" + valor + "]";
	}

}
