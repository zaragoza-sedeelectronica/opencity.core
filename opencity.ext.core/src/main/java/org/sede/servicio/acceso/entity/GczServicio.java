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
import org.sede.servicio.acceso.ConfigAcceso;

/**
 * The Class GczServicio.
 */
@Entity
@Table(name = "GCZ_SERVICIO", schema = ConfigAcceso.ESQUEMA)
@DynamicUpdate(value = true)
public class GczServicio extends EntidadBase implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6827619892753089372L;

	/** id. */
	private String id;
	
	/** title. */
	private String title;
	
	/** descripcion. */
	private String descripcion;
	
	/** estado. */
	private String estado;
	
	/** codigo seccion defecto. */
	private String codigoSeccionDefecto;
	
	/** directorio base. */
	private String directorioBase;
	
	/** visible. */
	private String visible;
	
	/** creation date. */
	private Date creationDate;
	
	/** last updated. */
	private Date lastUpdated;
	
	/** pub date. */
	private Date pubDate;
	
	/** usuario alta. */
	private String usuarioAlta;
	
	/** usuario mod. */
	private String usuarioMod;
	
	/** usuario pub. */
	private String usuarioPub;
	
	/** gcz usuariosadmin. */
	private String gczUsuariosadmin;
	
	/** gcz perfils. */
	@SoloEnEstaEntidad
	private List<GczPerfil> gczPerfils = new ArrayList<GczPerfil>(0);
	
	/** gcz usuarios. */
	@SoloEnEstaEntidad
	private List<GczUsuario> gczUsuarios = new ArrayList<GczUsuario>(0);
	
	/** gcz seccions. */
	@SoloEnEstaEntidad
	private List<GczSeccion> gczSeccions = new ArrayList<GczSeccion>(0);

	/**
	 * Instantiates a new gcz servicio.
	 */
	public GczServicio() {
	}

	/**
	 * Instantiates a new gcz servicio.
	 *
	 * @param codigoServicio Codigo servicio
	 */
	public GczServicio(String codigoServicio) {
		this.id = codigoServicio;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Id
	@Column(name = "CODIGO_SERVICIO", unique = true, nullable = false, length = 30)
	@NotBlank
	public String getId() {
		return this.id == null ? null : this.id.toUpperCase();
	}

	/**
	 * Sets the id.
	 *
	 * @param codigoServicio the new id
	 */
	public void setId(String codigoServicio) {
		this.id = codigoServicio;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	@Column(name = "NOMBRE", length = 100)
	@NotBlank
	public String getTitle() {
		return this.title;
	}

	/**
	 * Sets the title.
	 *
	 * @param nombre the new title
	 */
	public void setTitle(String nombre) {
		this.title = nombre;
	}

	/**
	 * Gets the descripcion.
	 *
	 * @return the descripcion
	 */
	@Column(name = "DESCRIPCION", length = 4000)
	public String getDescripcion() {
		return this.descripcion;
	}

	/**
	 * Sets the descripcion.
	 *
	 * @param descripcion the new descripcion
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * Gets the estado.
	 *
	 * @return the estado
	 */
	@Column(name = "ESTADO", length = 1)
	public String getEstado() {
		return this.estado;
	}

	/**
	 * Sets the estado.
	 *
	 * @param estado the new estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Gets the codigo seccion defecto.
	 *
	 * @return the codigo seccion defecto
	 */
	@Column(name = "CODIGO_SECCION_DEFECTO", length = 30)
	public String getCodigoSeccionDefecto() {
		return this.codigoSeccionDefecto;
	}

	/**
	 * Sets the codigo seccion defecto.
	 *
	 * @param codigoSeccionDefecto the new codigo seccion defecto
	 */
	public void setCodigoSeccionDefecto(String codigoSeccionDefecto) {
		this.codigoSeccionDefecto = codigoSeccionDefecto;
	}

	/**
	 * Gets the directorio base.
	 *
	 * @return the directorio base
	 */
	@Column(name = "DIRECTORIO_BASE", length = 400)
	public String getDirectorioBase() {
		return this.directorioBase;
	}

	/**
	 * Sets the directorio base.
	 *
	 * @param directorioBase the new directorio base
	 */
	public void setDirectorioBase(String directorioBase) {
		this.directorioBase = directorioBase;
	}

	/**
	 * Gets the visible.
	 *
	 * @return the visible
	 */
	@Column(name = "GCZ_PUBLICADO", length = 1)
	@NotBlank
	@InList({"S", "N"})
	public String getVisible() {
		return this.visible;
	}

	/**
	 * Sets the visible.
	 *
	 * @param gczPublicado the new visible
	 */
	public void setVisible(String gczPublicado) {
		this.visible = gczPublicado;
	}

	/**
	 * Gets the creation date.
	 *
	 * @return the creation date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAALTA", length = 7, updatable=false)
	public Date getCreationDate() {
		return this.creationDate;
	}

	/**
	 * Sets the creation date.
	 *
	 * @param gczFechaalta the new creation date
	 */
	public void setCreationDate(Date gczFechaalta) {
		this.creationDate = gczFechaalta;
	}

	/**
	 * Gets the last updated.
	 *
	 * @return the last updated
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAMOD", length = 7)
	public Date getLastUpdated() {
		return this.lastUpdated;
	}

	/**
	 * Sets the last updated.
	 *
	 * @param gczFechamod the new last updated
	 */
	public void setLastUpdated(Date gczFechamod) {
		this.lastUpdated = gczFechamod;
	}

	/**
	 * Gets the pub date.
	 *
	 * @return the pub date
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAPUB", length = 7, updatable=false)
	public Date getPubDate() {
		return this.pubDate;
	}

	/**
	 * Sets the pub date.
	 *
	 * @param gczFechapub the new pub date
	 */
	public void setPubDate(Date gczFechapub) {
		this.pubDate = gczFechapub;
	}

	/**
	 * Gets the usuario alta.
	 *
	 * @return the usuario alta
	 */
	@Column(name = "GCZ_USUARIOALTA", length = 100, updatable = false)
	public String getUsuarioAlta() {
		return this.usuarioAlta;
	}

	/**
	 * Sets the usuario alta.
	 *
	 * @param gczUsuarioalta the new usuario alta
	 */
	public void setUsuarioAlta(String gczUsuarioalta) {
		this.usuarioAlta = gczUsuarioalta;
	}

	/**
	 * Gets the usuario mod.
	 *
	 * @return the usuario mod
	 */
	@Column(name = "GCZ_USUARIOMOD", length = 100)
	public String getUsuarioMod() {
		return this.usuarioMod;
	}

	/**
	 * Sets the usuario mod.
	 *
	 * @param gczUsuariomod the new usuario mod
	 */
	public void setUsuarioMod(String gczUsuariomod) {
		this.usuarioMod = gczUsuariomod;
	}

	/**
	 * Gets the usuario pub.
	 *
	 * @return the usuario pub
	 */
	@Column(name = "GCZ_USUARIOPUB", length = 100, updatable = false)
	public String getUsuarioPub() {
		return this.usuarioPub;
	}

	/**
	 * Sets the usuario pub.
	 *
	 * @param gczUsuariopub the new usuario pub
	 */
	public void setUsuarioPub(String gczUsuariopub) {
		this.usuarioPub = gczUsuariopub;
	}

	/**
	 * Gets the gcz usuariosadmin.
	 *
	 * @return the gcz usuariosadmin
	 */
	@Column(name = "GCZ_USUARIOSADMIN", length = 300)
	public String getGczUsuariosadmin() {
		return this.gczUsuariosadmin;
	}

	/**
	 * Sets the gcz usuariosadmin.
	 *
	 * @param gczUsuariosadmin the new gcz usuariosadmin
	 */
	public void setGczUsuariosadmin(String gczUsuariosadmin) {
		this.gczUsuariosadmin = gczUsuariosadmin;
	}

	/**
	 * Gets the gcz perfils.
	 *
	 * @return the gcz perfils
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gczServicio")
	public List<GczPerfil> getGczPerfils() {
		return this.gczPerfils;
	}

	/**
	 * Sets the gcz perfils.
	 *
	 * @param gczPerfils the new gcz perfils
	 */
	public void setGczPerfils(List<GczPerfil> gczPerfils) {
		this.gczPerfils = gczPerfils;
	}

	/**
	 * Gets the gcz usuarios.
	 *
	 * @return the gcz usuarios
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_SERVICIO_AUDITOR", schema = ConfigAcceso.ESQUEMA, joinColumns = { @JoinColumn(name = "CODIGO_SERVICIO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_USUARIO", nullable = false, updatable = false) })
	public List<GczUsuario> getGczUsuarios() {
		return this.gczUsuarios;
	}

	/**
	 * Sets the gcz usuarios.
	 *
	 * @param gczUsuarios the new gcz usuarios
	 */
	public void setGczUsuarios(List<GczUsuario> gczUsuarios) {
		this.gczUsuarios = gczUsuarios;
	}

	/**
	 * Gets the gcz seccions.
	 *
	 * @return the gcz seccions
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gczServicio")
	public List<GczSeccion> getGczSeccions() {
		return this.gczSeccions;
	}

	/**
	 * Sets the gcz seccions.
	 *
	 * @param gczSeccions the new gcz seccions
	 */
	public void setGczSeccions(List<GczSeccion> gczSeccions) {
		this.gczSeccions = gczSeccions;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
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
