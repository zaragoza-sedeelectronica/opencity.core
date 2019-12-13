package org.sede.servicio.acceso.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;
import org.sede.core.anotaciones.InList;
import org.sede.core.dao.EntidadBase;
import org.sede.servicio.acceso.Constants;

@Entity
@Table(name = "GCZ_SECCION", schema = Constants.ESQUEMA)
@DynamicUpdate(value = true)
public class GczSeccion extends EntidadBase implements java.io.Serializable {

	private GczSeccionId id;
	private GczServicio gczServicio;
	private String nombre;
	private String descripcion;
	private String estado;
	private String wwwUrl;
	private String iniUrl;
	private String visible;
	private Date creationDate;
	private Date lastUpdated;
	private Date pubDate;
	private String usuarioAlta;
	private String usuarioMod;
	private String usuarioPub;
	@Valid
	private List<GczGrupoOperaciones> gczGrupoOperacioneses = new ArrayList<GczGrupoOperaciones>(
			0);

	public GczSeccion() {
	}
	public GczSeccion(String servicio, String seccion) {
		this.id = new GczSeccionId(seccion, servicio);
	}
	public GczSeccion(GczSeccionId id, GczServicio gczServicio) {
		this.id = id;
		this.gczServicio = gczServicio;
	}

	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "codigoSeccion", column = @Column(name = "CODIGO_SECCION", nullable = false, length = 30)),
			@AttributeOverride(name = "codigoServicio", column = @Column(name = "CODIGO_SERVICIO", nullable = false, length = 30)) })
	public GczSeccionId getId() {
		return this.id;
	}

	public void setId(GczSeccionId id) {
		this.id = id;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_SERVICIO", nullable = false, insertable = false, updatable = false)
	public GczServicio getGczServicio() {
		return this.gczServicio;
	}

	public void setGczServicio(GczServicio gczServicio) {
		this.gczServicio = gczServicio;
	}
	@NotBlank
	@Column(name = "NOMBRE", length = 100)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "DESCRIPCION", length = 4000)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "ESTADO", length = 1)
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "WWW_URL", length = 400)
	public String getWwwUrl() {
		return this.wwwUrl;
	}

	public void setWwwUrl(String wwwUrl) {
		this.wwwUrl = wwwUrl;
	}

	@Column(name = "INI_URL", length = 400)
	public String getIniUrl() {
		return this.iniUrl;
	}

	public void setIniUrl(String iniUrl) {
		this.iniUrl = iniUrl;
	}
	@NotBlank
	@InList({"S","N"})
	@Column(name = "GCZ_PUBLICADO", length = 1)
	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String gczPublicado) {
		this.visible = gczPublicado;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAALTA", length = 7, updatable=false)
	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date gczFechaalta) {
		this.creationDate = gczFechaalta;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAMOD", length = 7)
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	public void setLastUpdated(Date gczFechamod) {
		this.lastUpdated = gczFechamod;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAPUB", length = 7, updatable=false)
	public Date getPubDate() {
		return this.pubDate;
	}

	public void setPubDate(Date gczFechapub) {
		this.pubDate = gczFechapub;
	}

	@Column(name = "GCZ_USUARIOALTA", length = 100, updatable = false)
	public String getUsuarioAlta() {
		return this.usuarioAlta;
	}

	public void setUsuarioAlta(String gczUsuarioalta) {
		this.usuarioAlta = gczUsuarioalta;
	}

	@Column(name = "GCZ_USUARIOMOD", length = 100)
	public String getUsuarioMod() {
		return this.usuarioMod;
	}

	public void setUsuarioMod(String gczUsuariomod) {
		this.usuarioMod = gczUsuariomod;
	}

	@Column(name = "GCZ_USUARIOPUB", length = 100, updatable = false)
	public String getUsuarioPub() {
		return this.usuarioPub;
	}

	public void setUsuarioPub(String gczUsuariopub) {
		this.usuarioPub = gczUsuariopub;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gczSeccion", cascade = CascadeType.ALL)
	public List<GczGrupoOperaciones> getGczGrupoOperacioneses() {
		return this.gczGrupoOperacioneses;
	}

	public void setGczGrupoOperacioneses(
			List<GczGrupoOperaciones> gczGrupoOperacioneses) {
		this.gczGrupoOperacioneses = gczGrupoOperacioneses;
	}

	@Override
	public String toString() {
		return "GczSeccion [id=" + id + ", gczServicio=" + gczServicio
				+ ", nombre=" + nombre + ", descripcion=" + descripcion
				+ ", estado=" + estado + ", wwwUrl=" + wwwUrl + ", iniUrl="
				+ iniUrl + ", visible=" + visible
				+ ", creationDate="
				+ creationDate + ", lastUpdated=" + lastUpdated
				+ ", pubDate=" + pubDate + ", usuarioAlta="
				+ usuarioAlta + ", usuarioMod=" + usuarioMod
				+ ", usuarioPub=" + usuarioPub 
				+ "]";
	}

}
