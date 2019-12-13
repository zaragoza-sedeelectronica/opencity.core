package org.sede.servicio.acceso.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;
import org.sede.core.anotaciones.InList;
import org.sede.core.anotaciones.SoloEnEstaEntidad;
import org.sede.core.dao.EntidadBase;
import org.sede.servicio.acceso.Constants;

@Entity
@Table(name = "GCZ_SERVICIO", schema = Constants.ESQUEMA)
@DynamicUpdate(value = true)
public class GczServicio extends EntidadBase implements java.io.Serializable {

	private String id;
	private String title;
	private String descripcion;
	private String estado;
	private String codigoSeccionDefecto;
	private String directorioBase;
	private String visible;
	private Date creationDate;
	private Date lastUpdated;
	private Date pubDate;
	private String usuarioAlta;
	private String usuarioMod;
	private String usuarioPub;
	private String gczUsuariosadmin;
	@SoloEnEstaEntidad
	private List<GczPerfil> gczPerfils = new ArrayList<GczPerfil>(0);
	@SoloEnEstaEntidad
	private List<GczUsuario> gczUsuarios = new ArrayList<GczUsuario>(0);
	@SoloEnEstaEntidad
	private List<GczSeccion> gczSeccions = new ArrayList<GczSeccion>(0);

	public GczServicio() {
	}

	public GczServicio(String codigoServicio) {
		this.id = codigoServicio;
	}

	@Id
	@Column(name = "CODIGO_SERVICIO", unique = true, nullable = false, length = 30)
	@NotBlank
	public String getId() {
		return this.id == null ? null : this.id.toUpperCase();
	}

	public void setId(String codigoServicio) {
		this.id = codigoServicio;
	}

	@Column(name = "NOMBRE", length = 100)
	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String nombre) {
		this.title = nombre;
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

	@Column(name = "CODIGO_SECCION_DEFECTO", length = 30)
	public String getCodigoSeccionDefecto() {
		return this.codigoSeccionDefecto;
	}

	public void setCodigoSeccionDefecto(String codigoSeccionDefecto) {
		this.codigoSeccionDefecto = codigoSeccionDefecto;
	}

	@Column(name = "DIRECTORIO_BASE", length = 400)
	public String getDirectorioBase() {
		return this.directorioBase;
	}

	public void setDirectorioBase(String directorioBase) {
		this.directorioBase = directorioBase;
	}

	@Column(name = "GCZ_PUBLICADO", length = 1)
	@NotBlank
	@InList({"S", "N"})
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

	@Column(name = "GCZ_USUARIOSADMIN", length = 300)
	public String getGczUsuariosadmin() {
		return this.gczUsuariosadmin;
	}

	public void setGczUsuariosadmin(String gczUsuariosadmin) {
		this.gczUsuariosadmin = gczUsuariosadmin;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gczServicio")
	public List<GczPerfil> getGczPerfils() {
		return this.gczPerfils;
	}

	public void setGczPerfils(List<GczPerfil> gczPerfils) {
		this.gczPerfils = gczPerfils;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_SERVICIO_AUDITOR", schema = Constants.ESQUEMA, joinColumns = { @JoinColumn(name = "CODIGO_SERVICIO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_USUARIO", nullable = false, updatable = false) })
	public List<GczUsuario> getGczUsuarios() {
		return this.gczUsuarios;
	}

	public void setGczUsuarios(List<GczUsuario> gczUsuarios) {
		this.gczUsuarios = gczUsuarios;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gczServicio")
	public List<GczSeccion> getGczSeccions() {
		return this.gczSeccions;
	}

	public void setGczSeccions(List<GczSeccion> gczSeccions) {
		this.gczSeccions = gczSeccions;
	}

	@Override
	public String toString() {
		return "GczServicio [id=" + id + ", title="
				+ title + ", descripcion=" + descripcion + ", estado="
				+ estado + ", codigoSeccionDefecto=" + codigoSeccionDefecto
				+ ", directorioBase=" + directorioBase + ", visible="
				+ visible + ", creationDate="
				+ creationDate + ", lastUpdated=" + lastUpdated
				+ ", pubDate=" + pubDate + ", usuarioAlta="
				+ usuarioAlta + ", usuarioMod=" + usuarioMod
				+ ", usuarioPub=" + usuarioPub + "]";
	}

}
