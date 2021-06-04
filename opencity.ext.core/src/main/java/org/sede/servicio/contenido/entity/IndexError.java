package org.sede.servicio.contenido.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.InList;
import org.sede.core.anotaciones.PathId;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.dao.EntidadBase;
import org.sede.core.utils.Funciones;
import org.sede.servicio.contenido.IndexErrorController;


@XmlRootElement(name = "index-error")
@Entity
@DynamicUpdate
@Table(name = "elastic_index_log_error", schema = "INTRA")
@XmlAccessorType(XmlAccessType.FIELD)
@PathId("/" + IndexErrorController.MAPPING)
public class IndexError extends EntidadBase {
	
	@Id
	@Rdf(contexto = Context.DCT, propiedad = "identifier")
	@Column(name = "ROWID", nullable = false, unique = true)@Digits(integer = 22, fraction = 0)
	private String id;

	@Column(name = "STATUS")
	private BigDecimal status;
	
	@Column(name = "URL")
	private String url;
	
	@Column(name = "PARENT")
	private String parent;
	
	@Column(name = "MSG")
	private String msg;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAALTA")
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FECHA_REVISADO")
	private Date revisedDate;
	
	@Permisos(Permisos.PUB)
	@Column(name = "REVISADO", updatable = false)
	@Size(max = 1)
	@InList({ "S", "N" })
	private String revisado;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrlId() {
		return "contenido-" + Funciones.calcularHashCode(url).replace("-", "n");
	}
	public String getParentId() {
		return "contenido-" + Funciones.calcularHashCode(parent).replace("-", "n");
	}
	


	public BigDecimal getStatus() {
		return status;
	}


	public void setStatus(BigDecimal status) {
		this.status = status;
	}


	public String getUrl() {
		return url;
	}


	public void setUrl(String url) {
		this.url = url;
	}


	public String getParent() {
		return parent;
	}


	public void setParent(String parent) {
		this.parent = parent;
	}


	public String getMsg() {
		return msg;
	}


	public void setMsg(String msg) {
		this.msg = msg;
	}


	public Date getCreationDate() {
		return creationDate;
	}


	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}


	public Date getRevisedDate() {
		return revisedDate;
	}


	public void setRevisedDate(Date revisedDate) {
		this.revisedDate = revisedDate;
	}


	public String getRevisado() {
		return revisado;
	}


	public void setRevisado(String revisado) {
		this.revisado = revisado;
	}


	public String getUri() {
		return Funciones.obtenerPath(this.getClass()) + getId();
	}

}
