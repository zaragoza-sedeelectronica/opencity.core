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
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;
import org.sede.core.anotaciones.SoloEnEstaEntidad;
import org.sede.core.dao.EntidadBase;
import org.sede.servicio.acceso.ConfigAcceso;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class GczPerfil.
 * 
 * @autor Ayuntamiento de Zaragoza
 */
@Entity
@Table(name = "GCZ_PERFIL", schema = ConfigAcceso.ESQUEMA, uniqueConstraints = @UniqueConstraint(columnNames = {
		"CODIGO_PERFIL", "CODIGO_SERVICIO" }))
@SequenceGenerator(name = "SECUENCIA_GCZ_PERFIL_SEQ", sequenceName = "GCZ_PERFIL_SEQ", allocationSize = 1)
@DynamicUpdate(value = true)
public class GczPerfil extends EntidadBase implements java.io.Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3969916431510953825L;

	/** Constant logger. */
	private static final Logger logger = LoggerFactory.getLogger(GczPerfil.class);
	
	/** id. */
	private BigDecimal id;
	
	/** gcz servicio. */
	private GczServicio gczServicio;
	
	/** code. */
	private String code;
	
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
	
	/** gcz grupo usuarios. */
	@SoloEnEstaEntidad
	private List<GczGrupoUsuario> gczGrupoUsuarios = new ArrayList<GczGrupoUsuario>(0);
	
	/** gcz grupo operacioneses. */
	@SoloEnEstaEntidad
	private List<GczGrupoOperaciones> gczGrupoOperacioneses = new ArrayList<GczGrupoOperaciones>(0);
	
	/** gcz usuarios. */
	@SoloEnEstaEntidad
	private List<GczUsuario> gczUsuarios = new ArrayList<GczUsuario>(0);

	/**
	 * Instantiates a new gcz perfil.
	 */
	public GczPerfil() {
	}

	/**
	 * Instantiates a new gcz perfil.
	 *
	 * @param idPerfil Id perfil
	 * @param gczServicio Gcz servicio
	 * @param codigoPerfil Codigo perfil
	 */
	public GczPerfil(BigDecimal idPerfil, GczServicio gczServicio, String codigoPerfil) {
		this.id = idPerfil;
		this.gczServicio = gczServicio;
		this.code = codigoPerfil;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Id
	@Column(name = "ID_PERFIL", unique = true, nullable = false, precision = 14, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA_GCZ_PERFIL_SEQ")
	public BigDecimal getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param idPerfil the new id
	 */
	public void setId(BigDecimal idPerfil) {
		this.id = idPerfil;
	}

	/**
	 * Gets the gcz servicio.
	 *
	 * @return the gcz servicio
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_SERVICIO", nullable = false)
	public GczServicio getGczServicio() {
		return this.gczServicio;
	}

	/**
	 * Sets the gcz servicio.
	 *
	 * @param gczServicio the new gcz servicio
	 */
	public void setGczServicio(GczServicio gczServicio) {
		this.gczServicio = gczServicio;
	}

	/**
	 * Gets the code.
	 *
	 * @return the code
	 */
	@Column(name = "CODIGO_PERFIL", nullable = false, length = 30)
	@NotBlank
	public String getCode() {
		return this.code == null ? null : this.code.toUpperCase();
	}

	/**
	 * Sets the code.
	 *
	 * @param codigoPerfil the new code
	 */
	public void setCode(String codigoPerfil) {
		this.code = codigoPerfil;
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
	 * Gets the visible.
	 *
	 * @return the visible
	 */
	@Column(name = "GCZ_PUBLICADO", length = 1)
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
	 * Gets the gcz grupo usuarios.
	 *
	 * @return the gcz grupo usuarios
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_GRUPO_USUARIO", schema = ConfigAcceso.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_GRUPO_USUARIO", nullable = false, updatable = false) })
	public List<GczGrupoUsuario> getGczGrupoUsuarios() {
		return this.gczGrupoUsuarios;
	}

	/**
	 * Sets the gcz grupo usuarios.
	 *
	 * @param gczGrupoUsuarios the new gcz grupo usuarios
	 */
	public void setGczGrupoUsuarios(List<GczGrupoUsuario> gczGrupoUsuarios) {
		this.gczGrupoUsuarios = gczGrupoUsuarios;
	}

	/**
	 * Gets the gcz grupo operacioneses.
	 *
	 * @return the gcz grupo operacioneses
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_GRUPO_OPERACIONES", schema = ConfigAcceso.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_GRUPO_OPERACIONES", nullable = false, updatable = false) })
	public List<GczGrupoOperaciones> getGczGrupoOperacioneses() {
		return this.gczGrupoOperacioneses;
	}

	/**
	 * Sets the gcz grupo operacioneses.
	 *
	 * @param gczGrupoOperacioneses the new gcz grupo operacioneses
	 */
	public void setGczGrupoOperacioneses(
			List<GczGrupoOperaciones> gczGrupoOperacioneses) {
		this.gczGrupoOperacioneses = gczGrupoOperacioneses;
	}

	/**
	 * Gets the gcz usuarios.
	 *
	 * @return the gcz usuarios
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_USUARIO", schema = ConfigAcceso.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_USUARIO", nullable = false, updatable = false) })
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
		return "GczPerfil [id=" + id + ", gczServicio="
				+ gczServicio + ", code=" + code + ", title="
				+ title + ", descripcion=" + descripcion + ", visible="
				+ visible + ", creationDate="
				+ creationDate + ", lastUpdated=" + lastUpdated
				+ ", pubDate=" + pubDate + ", usuarioAlta="
				+ usuarioAlta + ", usuarioMod=" + usuarioMod
				+ ", usuarioPub=" + usuarioPub + "]";
	}
	
	/**
	 * Contiene.
	 *
	 * @param valor Valor
	 * @return true, if successful
	 */
	public boolean contiene(GczGrupoOperaciones valor) {
		try {
			if (this.getGczGrupoOperacioneses() != null) {
				for (GczGrupoOperaciones grupo : this.getGczGrupoOperacioneses()) {
					if (grupo.getGczSeccion().getId().getCodigoSeccion().equals(valor.getGczSeccion().getId().getCodigoSeccion())
							&& grupo.getGczSeccion().getId().getCodigoServicio().equals(valor.getGczSeccion().getId().getCodigoServicio())
							&& grupo.getCodigoGrupoOperaciones().equals(valor.getCodigoGrupoOperaciones())) {
						return true;
					}
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	} 
}
