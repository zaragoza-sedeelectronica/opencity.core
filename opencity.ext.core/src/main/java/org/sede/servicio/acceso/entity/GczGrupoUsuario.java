package org.sede.servicio.acceso.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Size;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;
import org.sede.core.anotaciones.InList;
import org.sede.core.anotaciones.SoloEnEstaEntidad;
import org.sede.core.dao.EntidadBase;
import org.sede.servicio.acceso.ConfigAcceso;

/**
 * The Class GczGrupoUsuario.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
@Entity
@DynamicUpdate(value = true)
@Table(name = "GCZ_GRUPO_USUARIO", schema = ConfigAcceso.ESQUEMA, uniqueConstraints = @UniqueConstraint(columnNames = "CODIGO_GRUPO_USUARIO"))
@SequenceGenerator(name = "SECUENCIA_GCZ_GRUPO_USUARIO_SEQ", sequenceName = "GCZ_GRUPO_USUARIO_SEQ", allocationSize = 1)
public class GczGrupoUsuario extends EntidadBase implements java.io.Serializable {

	/** Constant serialVersionUID. */
	private static final long serialVersionUID = -1790393653922292631L;

	/** id. */
	private BigDecimal id;
	
	/** title. */
	private String title;
	
	/** descripcion. */
	private String descripcion;
	
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
	
	/** code. */
	private String code;
	
	/** gcz perfils. */
	@SoloEnEstaEntidad
	private List<GczPerfil> gczPerfils = new ArrayList<GczPerfil>(0);
	
	/** gcz usuarios. */
	@SoloEnEstaEntidad
	private List<GczUsuario> gczUsuarios = new ArrayList<GczUsuario>(0);

	/**
	 * Instantiates a new gcz grupo usuario.
	 */
	public GczGrupoUsuario() {
	}

	/**
	 * Instantiates a new gcz grupo usuario.
	 *
	 * @param idGrupoUsuario Id grupo usuario
	 * @param gczPublicado Gcz publicado
	 * @param codigoGrupoUsuario Codigo grupo usuario
	 */
	public GczGrupoUsuario(BigDecimal idGrupoUsuario, String gczPublicado,
			String codigoGrupoUsuario) {
		this.id = idGrupoUsuario;
		this.visible = gczPublicado;
		this.code = codigoGrupoUsuario;
	}

	/**
	 * Instantiates a new gcz grupo usuario.
	 *
	 * @param id Id
	 */
	public GczGrupoUsuario(BigDecimal id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA_GCZ_GRUPO_USUARIO_SEQ")
	@Column(name = "ID_GRUPO_USUARIO", unique = true, nullable = false, precision = 14, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param idGrupoUsuario the new id
	 */
	public void setId(BigDecimal idGrupoUsuario) {
		this.id = idGrupoUsuario;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	@Column(name = "NOMBRE", length = 100)
	@Size(max = 100)
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
	@Column(name = "DESCRIPCION", length = 2000)
	@Size(max = 2000)
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
	 * Gets the visible.
	 *
	 * @return the visible
	 */
	@Column(name = "GCZ_PUBLICADO", nullable = false, length = 1)
	@InList({"S", "N"})
	@NotBlank
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
	 * Gets the code.
	 *
	 * @return the code
	 */
	@Column(name = "CODIGO_GRUPO_USUARIO", unique = true, nullable = false, length = 50)
	@NotBlank
	public String getCode() {
		return this.code == null ? null : this.code.toLowerCase();
	}

	/**
	 * Sets the code.
	 *
	 * @param codigoGrupoUsuario the new code
	 */
	public void setCode(String codigoGrupoUsuario) {
		this.code = codigoGrupoUsuario;
	}

	/**
	 * Gets the gcz perfils.
	 *
	 * @return the gcz perfils
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_GRUPO_USUARIO", schema = ConfigAcceso.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_GRUPO_USUARIO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) })
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
	@JoinTable(name = "GCZ_USUARIO_GRUPO_USUARIO", schema = ConfigAcceso.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_GRUPO_USUARIO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_USUARIO", nullable = false, updatable = false) })
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
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "GczGrupoUsuario [id=" + id
				+ ", title=" + title + ", descripcion=" + descripcion
				+ ", visible=" + visible + ", creationDate="
				+ creationDate + ", lastUpdated=" + lastUpdated
				+ ", pubDate=" + pubDate + ", usuarioAlta="
				+ usuarioAlta + ", usuarioMod=" + usuarioMod
				+ ", usuarioPub=" + usuarioPub + ", code="
				+ code + "]";
	}

}
