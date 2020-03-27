package org.sede.servicio.acceso.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.PathId;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RequiredSinValidacion;
import org.sede.core.dao.EntidadBase;
import org.sede.core.utils.Funciones;

import java.math.BigDecimal;

/**
 * The Class Valor.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
@Entity(name = "valor-acceso")
@DynamicUpdate
@XmlRootElement(name = "value")
@Table(name = "ACTO_VALORES", schema = "ACTIVIDADES")
@XmlAccessorType(XmlAccessType.FIELD)
@PathId("/servicio/actividades/value")
@BatchSize(size = 100)
public class Valor extends EntidadBase {

	/** Constant serialVersionUID. */
	private static final long serialVersionUID = -9207518377961644201L;

	/** id. */
	@Id
	@Column(name = "COD_VALOR", unique = true, nullable = false)
	@Digits(integer = 22, fraction = 0)
	@Rdf(contexto = Context.SKOS, propiedad = "notation")
	private BigDecimal id;

	/** title. */
	@Column(name = "TITLE")
	@Size(max = 80)
	@Rdf(contexto = Context.SKOS, propiedad = "prefLabel")
	@RequiredSinValidacion
	private String title;

	/** description. */
	@Column(name = "DESCRIPTION")
	@Size(max = 300)
	private String description;

	/**
	 * Instantiates a new valor.
	 */
	public Valor() { super(); }

	/**
	 * Instantiates a new valor.
	 *
	 * @param id Id
	 */
	public Valor(BigDecimal id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public BigDecimal getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(BigDecimal id) {
		this.id = id;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "Valor [id=" + id + ", title=" + title + ", description=" + description + "]";
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	public String getUri() {
		return Funciones.obtenerPath(this.getClass()) + getId();
	}
	
}
