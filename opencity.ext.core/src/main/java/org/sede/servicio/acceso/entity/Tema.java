package org.sede.servicio.acceso.entity;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
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

/**
 * The Class Tema.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
@Entity(name = "TemaActo")
@DynamicUpdate
@XmlRootElement(name = "tema")
@Table(name = "TEMAS", schema = "ACTIVIDADES")
@XmlAccessorType(XmlAccessType.FIELD)
@PathId("/servicio/actividades/category")
@SequenceGenerator(name = "SECUENCIA", sequenceName = "SEQ_CODTEMA", allocationSize = 1)
@BatchSize(size=100)
public class Tema extends EntidadBase implements java.io.Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA")
	@Column(name = "COD_TEMA", unique = true, nullable = false)
	@Digits(integer = 22, fraction = 0)
	@Rdf(contexto = Context.SKOS, propiedad = "notation")
	@NotNull
	private BigDecimal id;
	
	@Column(name = "TEMA")
	@Size(max = 80)
	@Rdf(contexto = Context.SKOS, propiedad = "prefLabel")
	@RequiredSinValidacion
	private String title;

	@Column(name = "IMAGEN")
	@Size(max = 80)
	@Rdf(contexto = Context.SCHEMA, propiedad = "image")
	private String image;

	@Column(name = "IMAGEN2")
	@Size(max = 80)
	@Rdf(contexto = Context.SCHEMA, propiedad = "image")
	private String imageAlt;

	@Transient
	private Set<MediaFile> file_array = new HashSet<MediaFile>(0);

	@Interno
	@Column(name = "ORDEN")
	@Digits(integer = 22, fraction = 0)
	private Integer order;

	public Tema() {
		super();
	}

	public Tema(BigDecimal id) {
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

	public String getImage() {
		return image == null ? null : "//www.zaragoza.es/cont/paginas/actividades/" + image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getOrder() {
		return order;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getImageAlt() {
		return imageAlt == null ? null : "//www.zaragoza.es/cont/paginas/actividades/" + imageAlt;
	}

	public void setImageAlt(String imageAlt) {
		this.imageAlt = imageAlt;
	}

	public Set<MediaFile> getFile_array() {
		return file_array;
	}

	public void setFile_array(Set<MediaFile> file_array) {
		this.file_array = file_array;
	}

	@Override
	public String toString() {
		return "Tema [id=" + id + ", title=" + title
				+ ", image=" + image + ", imageAlt=" + imageAlt
				+ ", order=" + order + "]";
	}

	public String getUri() {
		return Funciones.obtenerPath(this.getClass()) + getId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((file_array == null) ? 0 : file_array.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

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
