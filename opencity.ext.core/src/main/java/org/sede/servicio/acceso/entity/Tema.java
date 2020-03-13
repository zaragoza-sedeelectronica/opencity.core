package org.sede.servicio.acceso.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.Interno;
import org.sede.core.anotaciones.PathId;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RequiredSinValidacion;
import org.sede.core.dao.EntidadBase;
import org.sede.core.utils.Funciones;

// TODO: Auto-generated Javadoc
/**
 * The Class Tema.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
@Entity(name = "tema-acto")
@DynamicUpdate
@XmlRootElement(name = "tema")
@Table(name = "TEMAS", schema = "ACTIVIDADES")
@XmlAccessorType(XmlAccessType.FIELD)
@PathId("/servicio/actividades/category")
@SequenceGenerator(name = "SECUENCIA", sequenceName = "SEQ_CODTEMA", allocationSize = 1)
@BatchSize(size=100)
public class Tema extends EntidadBase implements java.io.Serializable {
	
	/** Constant serialVersionUID. */
	private static final long serialVersionUID = 3606873321023741839L;

	/** id. */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA")
	@Column(name = "COD_TEMA", unique = true, nullable = false)
	@Digits(integer = 22, fraction = 0)
	@Rdf(contexto = Context.SKOS, propiedad = "notation")
	@NotNull
	private BigDecimal id;
	
	/** title. */
	@Column(name = "TEMA")
	@Size(max = 80)
	@Rdf(contexto = Context.SKOS, propiedad = "prefLabel")
	@RequiredSinValidacion
	private String title;

	/** image. */
	@Column(name = "IMAGEN")
	@Size(max = 80)
	@Rdf(contexto = Context.SCHEMA, propiedad = "image")
	private String image;

	/** image alt. */
	@Column(name = "IMAGEN2")
	@Size(max = 80)
	@Rdf(contexto = Context.SCHEMA, propiedad = "image")
	private String imageAlt;

	
	/** order. */
	@Interno
	@Column(name = "ORDEN")
	@Digits(integer = 22, fraction = 0)
	private Integer order;

	/**
	 * Instantiates a new tema.
	 */
	public Tema() {
		super();
	}

	/**
	 * Instantiates a new tema.
	 *
	 * @param id Id
	 */
	public Tema(BigDecimal id) {
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
	 * Gets the image.
	 *
	 * @return the image
	 */
	public String getImage() {
		return image == null ? null : "//www.zaragoza.es/cont/paginas/actividades/" + image;
	}

	/**
	 * Sets the image.
	 *
	 * @param image the new image
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * Gets the order.
	 *
	 * @return the order
	 */
	public Integer getOrder() {
		return order;
	}

	/**
	 * Sets the order.
	 *
	 * @param order the new order
	 */
	public void setOrder(Integer order) {
		this.order = order;
	}

	/**
	 * Gets the image alt.
	 *
	 * @return the image alt
	 */
	public String getImageAlt() {
		return imageAlt == null ? null : "//www.zaragoza.es/cont/paginas/actividades/" + imageAlt;
	}

	/**
	 * Sets the image alt.
	 *
	 * @param imageAlt the new image alt
	 */
	public void setImageAlt(String imageAlt) {
		this.imageAlt = imageAlt;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "Tema [id=" + id + ", title=" + title
				+ ", image=" + image + ", imageAlt=" + imageAlt
				+ ", order=" + order + "]";
	}

	/**
	 * Gets the uri.
	 *
	 * @return the uri
	 */
	public String getUri() {
		return Funciones.obtenerPath(this.getClass()) + getId();
	}

	/**
	 * Hash code.
	 *
	 * @return int
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * Equals.
	 *
	 * @param obj Obj
	 * @return true, if successful
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tema other = (Tema) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		return true;
	}

}
