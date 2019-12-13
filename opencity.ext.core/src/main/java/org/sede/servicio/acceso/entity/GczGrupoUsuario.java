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
import org.sede.servicio.acceso.Constants;

@Entity
@DynamicUpdate(value = true)
@Table(name = "GCZ_GRUPO_USUARIO", schema = Constants.ESQUEMA, uniqueConstraints = @UniqueConstraint(columnNames = "CODIGO_GRUPO_USUARIO"))
@SequenceGenerator(name = "SECUENCIA_GCZ_GRUPO_USUARIO_SEQ", sequenceName = "GCZ_GRUPO_USUARIO_SEQ", allocationSize = 1)
public class GczGrupoUsuario extends EntidadBase implements java.io.Serializable {

	private BigDecimal id;
	private String title;
	private String descripcion;
	private String visible;
	private Date creationDate;
	private Date lastUpdated;
	private Date pubDate;
	private String usuarioAlta;
	private String usuarioMod;
	private String usuarioPub;
	private String code;
	@SoloEnEstaEntidad
	private List<GczPerfil> gczPerfils = new ArrayList<GczPerfil>(0);
	@SoloEnEstaEntidad
	private List<GczUsuario> gczUsuarios = new ArrayList<GczUsuario>(0);

	public GczGrupoUsuario() {
	}

	public GczGrupoUsuario(BigDecimal idGrupoUsuario, String gczPublicado,
			String codigoGrupoUsuario) {
		this.id = idGrupoUsuario;
		this.visible = gczPublicado;
		this.code = codigoGrupoUsuario;
	}

	public GczGrupoUsuario(BigDecimal id) {
		this.id = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA_GCZ_GRUPO_USUARIO_SEQ")
	@Column(name = "ID_GRUPO_USUARIO", unique = true, nullable = false, precision = 14, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal idGrupoUsuario) {
		this.id = idGrupoUsuario;
	}

	@Column(name = "NOMBRE", length = 100)
	@Size(max = 100)
	@NotBlank
	public String getTitle() {
		return this.title;
	}

	public void setTitle(String nombre) {
		this.title = nombre;
	}

	@Column(name = "DESCRIPCION", length = 2000)
	@Size(max = 2000)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "GCZ_PUBLICADO", nullable = false, length = 1)
	@InList({"S", "N"})
	@NotBlank
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

	@Column(name = "CODIGO_GRUPO_USUARIO", unique = true, nullable = false, length = 50)
	@NotBlank
	public String getCode() {
		return this.code == null ? null : this.code.toLowerCase();
	}

	public void setCode(String codigoGrupoUsuario) {
		this.code = codigoGrupoUsuario;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_GRUPO_USUARIO", schema = Constants.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_GRUPO_USUARIO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) })
	public List<GczPerfil> getGczPerfils() {
		return this.gczPerfils;
	}

	public void setGczPerfils(List<GczPerfil> gczPerfils) {
		this.gczPerfils = gczPerfils;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_USUARIO_GRUPO_USUARIO", schema = Constants.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_GRUPO_USUARIO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_USUARIO", nullable = false, updatable = false) })
	public List<GczUsuario> getGczUsuarios() {
		return this.gczUsuarios;
	}

	public void setGczUsuarios(List<GczUsuario> gczUsuarios) {
		this.gczUsuarios = gczUsuarios;
	}

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
