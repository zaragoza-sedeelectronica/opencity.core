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
import org.sede.servicio.acceso.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "GCZ_PERFIL", schema = Constants.ESQUEMA, uniqueConstraints = @UniqueConstraint(columnNames = {
		"CODIGO_PERFIL", "CODIGO_SERVICIO" }))
@SequenceGenerator(name = "SECUENCIA_GCZ_PERFIL_SEQ", sequenceName = "GCZ_PERFIL_SEQ", allocationSize = 1)
@DynamicUpdate(value = true)
public class GczPerfil extends EntidadBase implements java.io.Serializable {
	private static final Logger logger = LoggerFactory.getLogger(GczPerfil.class);
	private BigDecimal id;
	private GczServicio gczServicio;
	private String code;
	private String title;
	private String descripcion;
	private String visible;
	private Date creationDate;
	private Date lastUpdated;
	private Date pubDate;
	private String usuarioAlta;
	private String usuarioMod;
	private String usuarioPub;
	@SoloEnEstaEntidad
	private List<GczGrupoUsuario> gczGrupoUsuarios = new ArrayList<GczGrupoUsuario>(0);
	@SoloEnEstaEntidad
	private List<GczGrupoOperaciones> gczGrupoOperacioneses = new ArrayList<GczGrupoOperaciones>(0);
	@SoloEnEstaEntidad
	private List<GczUsuario> gczUsuarios = new ArrayList<GczUsuario>(0);

	public GczPerfil() {
	}

	public GczPerfil(BigDecimal idPerfil, GczServicio gczServicio, String codigoPerfil) {
		this.id = idPerfil;
		this.gczServicio = gczServicio;
		this.code = codigoPerfil;
	}

	@Id
	@Column(name = "ID_PERFIL", unique = true, nullable = false, precision = 14, scale = 0)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA_GCZ_PERFIL_SEQ")
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal idPerfil) {
		this.id = idPerfil;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_SERVICIO", nullable = false)
	public GczServicio getGczServicio() {
		return this.gczServicio;
	}

	public void setGczServicio(GczServicio gczServicio) {
		this.gczServicio = gczServicio;
	}

	@Column(name = "CODIGO_PERFIL", nullable = false, length = 30)
	@NotBlank
	public String getCode() {
		return this.code == null ? null : this.code.toUpperCase();
	}

	public void setCode(String codigoPerfil) {
		this.code = codigoPerfil;
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

	@Column(name = "GCZ_PUBLICADO", length = 1)
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

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_GRUPO_USUARIO", schema = Constants.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_GRUPO_USUARIO", nullable = false, updatable = false) })
	public List<GczGrupoUsuario> getGczGrupoUsuarios() {
		return this.gczGrupoUsuarios;
	}

	public void setGczGrupoUsuarios(List<GczGrupoUsuario> gczGrupoUsuarios) {
		this.gczGrupoUsuarios = gczGrupoUsuarios;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_GRUPO_OPERACIONES", schema = Constants.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_GRUPO_OPERACIONES", nullable = false, updatable = false) })
	public List<GczGrupoOperaciones> getGczGrupoOperacioneses() {
		return this.gczGrupoOperacioneses;
	}

	public void setGczGrupoOperacioneses(
			List<GczGrupoOperaciones> gczGrupoOperacioneses) {
		this.gczGrupoOperacioneses = gczGrupoOperacioneses;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_USUARIO", schema = Constants.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_USUARIO", nullable = false, updatable = false) })
	public List<GczUsuario> getGczUsuarios() {
		return this.gczUsuarios;
	}

	public void setGczUsuarios(List<GczUsuario> gczUsuarios) {
		this.gczUsuarios = gczUsuarios;
	}

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
