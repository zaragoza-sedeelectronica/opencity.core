package org.sede.servicio.equipamiento.entity;

import javax.persistence.*;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.IsUri;
import org.sede.core.anotaciones.PathId;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RdfMultiple;
import org.sede.core.dao.BooleanConverter;
import org.sede.core.dao.EntidadBase;


@XmlRootElement(name = "property")
@Entity
@DynamicUpdate
@Table(name = "CENTRO_PROPIEDAD", schema = "INTRA")
@XmlAccessorType(XmlAccessType.FIELD)
@PathId("/recurso/urbanismo-infraestructuras/equipamiento/propiedad-centro/")
@Rdf(contexto = Context.MAPEOSEM, propiedad = "Property")
public class PropiedadSemanticaCentro extends EntidadBase implements java.io.Serializable {

	@Id
	@Column(name = "ID", unique = true, nullable = false)
	@Rdf(contexto = Context.DCT, propiedad = "identifier")
	private Integer id;
	
	@Column(name = "NOMBRE", nullable = false)
	@Size(max = 1000)
	@RdfMultiple({@Rdf(contexto = Context.RDFS, propiedad = "label"), @Rdf(contexto = Context.DCT, propiedad = "title"), @Rdf(contexto = Context.SCHEMA, propiedad = "name")})
	private String title;

	@Column(name = "URI", nullable = false)
	@Size(max = 1000)
	@Rdf(contexto = Context.MAPEOSEM, propiedad = "uri")
	@IsUri
	private String uri;

	@Column(name = "MAGNITUD")
	@Size(max = 200)
//	@Rdf(contexto = Context.MAPEOSEM, propiedad = "uri")
	private String magnitud;

	@Column(name = "MAGNITUD_SEMANTICA")
	@Size(max = 200)
//	@Rdf(contexto = Context.MAPEOSEM, propiedad = "uri")
	private String magnitudSemantica;

	@Column(name = "TIPO")
	@Size(max = 200)
//	@Rdf(contexto = Context.MAPEOSEM, propiedad = "uri")
	private String tipo;

	@Column(name = "ORDEN")
	private Integer orden;

	@Column(name = "PRINCIPAL")
	@Convert(converter = BooleanConverter.class)
	private Boolean principal;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "TEMA")
	@BatchSize(size=50)
//	@Rdf(contexto = Context.DCAT, propiedad = "theme")
	private MateriaPropiedadSemantica tema;
	

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getMagnitud() {
		return magnitud;
	}

	public void setMagnitud(String magnitud) {
		this.magnitud = magnitud;
	}

	public String getMagnitudSemantica() {
		return magnitudSemantica;
	}

	public void setMagnitudSemantica(String magnitudSemantica) {
		this.magnitudSemantica = magnitudSemantica;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public MateriaPropiedadSemantica getTema() {
		return tema;
	}

	public void setTema(MateriaPropiedadSemantica tema) {
		this.tema = tema;
	}

	public Boolean getPrincipal() {
		return principal;
	}

	public void setPrincipal(Boolean principal) {
		this.principal = principal;
	}


	@Override
	public String toString() {
		return "PropiedadSemanticaCentro [id=" + id + ", title=" + title
				+ ", uri=" + uri + ", magnitud=" + magnitud + ", tipo=" + tipo + ", orden=" + orden + ", principal=" + principal + ", tema=" + tema + "]";
	}

}
