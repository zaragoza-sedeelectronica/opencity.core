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

@Entity(name = "valor-acceso")
@DynamicUpdate
@XmlRootElement(name = "value")
@Table(name = "ACTO_VALORES", schema = "ACTIVIDADES")
@XmlAccessorType(XmlAccessType.FIELD)
@PathId("/servicio/actividades/value")
@BatchSize(size = 100)
public class Valor extends EntidadBase {

	@Id
	@Column(name = "COD_VALOR", unique = true, nullable = false)
	@Digits(integer = 22, fraction = 0)
	@Rdf(contexto = Context.SKOS, propiedad = "notation")
	private BigDecimal id;

	@Column(name = "TITLE")
	@Size(max = 80)
	@Rdf(contexto = Context.SKOS, propiedad = "prefLabel")
	@RequiredSinValidacion
	private String title;

	@Column(name = "DESCRIPTION")
	@Size(max = 300)
	private String description;

	public Valor() { super(); }

	public Valor(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Valor [id=" + id + ", title=" + title + ", description=" + description + "]";
	}

	public String getUri() {
		return Funciones.obtenerPath(this.getClass()) + getId();
	}
	
}
