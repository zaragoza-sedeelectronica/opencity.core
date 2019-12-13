package org.sede.servicio.acceso.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SelectBeforeUpdate;
import org.sede.core.anotaciones.SoloEnEstaEntidad;
import org.sede.core.dao.EntidadBase;
import org.sede.core.dao.PersistenceContextListener;
import org.sede.servicio.acceso.Constants;

@Entity
@EntityListeners(PersistenceContextListener.class)
@Table(name = "GCZ_USUARIO", schema = Constants.ESQUEMA, uniqueConstraints = @UniqueConstraint(columnNames = "LOGIN"))
@DynamicUpdate
@SelectBeforeUpdate
public class GczUsuario extends EntidadBase implements java.io.Serializable {

	private BigDecimal id;
	private String login;
	private String nombre;
	private String apellido1;
	private String apellido2;
	private String contrasenna;
	private String secretKey;
	private String correoElectronico;
	private String estado;
	private String bloqueado;
	private String codigoServicioDefecto;
	private String codigoSeccionDefecto;
	private BigDecimal numIntentosFallidos;
	private Date fechaUltimoAcceso;
	private String visible;
	private Date creationDate;
	private Date lastUpdated;
	private Date pubDate;
	private String usuarioAlta;
	private String usuarioMod;
	private String usuarioPub;
	private String administrador;
	@SoloEnEstaEntidad
	private List<GczServicio> gczServiciosQueAudita = new ArrayList<GczServicio>(0);
	@SoloEnEstaEntidad
	private List<GczGrupoUsuario> gczGrupoUsuarios = new ArrayList<GczGrupoUsuario>(0);
	@SoloEnEstaEntidad
	private List<GczPerfil> gczPerfils = new ArrayList<GczPerfil>(0);
	@SoloEnEstaEntidad
	private List<GczPropiedadUsuario> gczPropiedadUsuarios = new ArrayList<GczPropiedadUsuario>(0);

	public GczUsuario() {
		super();
	}

	public GczUsuario(BigDecimal id) {
		this.id = id;
	}

	@Id
	@Column(name = "ID_USUARIO", unique = true, nullable = false, precision = 14, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal idUsuario) {
		this.id = idUsuario;
	}

	@Column(name = "LOGIN", unique = true, nullable = false, length = 100, updatable = false)
	public String getLogin() {
		return this.login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	@Column(name = "NOMBRE", length = 100)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "APELLIDO_1", length = 100)
	public String getApellido1() {
		return this.apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	@Column(name = "APELLIDO_2", length = 100)
	public String getApellido2() {
		return this.apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	@Column(name = "CONTRASENNA", nullable = false, length = 100, updatable = false)
	public String getContrasenna() {
		return this.contrasenna;
	}

	public void setContrasenna(String contrasenna) {
		this.contrasenna = contrasenna;
	}

	@Column(name = "SECRETKEY", length = 200, updatable = false)
	public String getSecretKey() {
		return this.secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	@Column(name = "CORREO_ELECTRONICO", nullable = false, length = 100)
	public String getCorreoElectronico() {
		return this.correoElectronico;
	}

	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	@Column(name = "ESTADO", length = 1, updatable = false)
	public String getEstado() {
		return this.estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "BLOQUEADO", length = 1, updatable = false)
	public String getBloqueado() {
		return this.bloqueado;
	}

	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}

	@Column(name = "CODIGO_SERVICIO_DEFECTO", nullable = false, length = 30)
	public String getCodigoServicioDefecto() {
		return this.codigoServicioDefecto;
	}

	public void setCodigoServicioDefecto(String codigoServicioDefecto) {
		this.codigoServicioDefecto = codigoServicioDefecto;
	}

	@Column(name = "CODIGO_SECCION_DEFECTO", nullable = false, length = 30)
	public String getCodigoSeccionDefecto() {
		return this.codigoSeccionDefecto;
	}

	public void setCodigoSeccionDefecto(String codigoSeccionDefecto) {
		this.codigoSeccionDefecto = codigoSeccionDefecto;
	}

	@Column(name = "NUM_INTENTOS_FALLIDOS", nullable = false, precision = 22, scale = 0, updatable = false)
	public BigDecimal getNumIntentosFallidos() {
		return this.numIntentosFallidos;
	}

	public void setNumIntentosFallidos(BigDecimal numIntentosFallidos) {
		this.numIntentosFallidos = numIntentosFallidos;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ULTIMO_ACCESO", length = 7)
	public Date getFechaUltimoAcceso() {
		return this.fechaUltimoAcceso;
	}

	public void setFechaUltimoAcceso(Date fechaUltimoAcceso) {
		this.fechaUltimoAcceso = fechaUltimoAcceso;
	}

	@Column(name = "GCZ_PUBLICADO", nullable = false, length = 1)
	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String gczPublicado) {
		this.visible = gczPublicado;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAALTA", length = 7)
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
	@Column(name = "GCZ_FECHAPUB", length = 7)
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

	@Column(name = "GCZ_USUARIOPUB", length = 100)
	public String getUsuarioPub() {
		return this.usuarioPub;
	}

	public void setUsuarioPub(String gczUsuariopub) {
		this.usuarioPub = gczUsuariopub;
	}

	@Column(name = "ADMINISTRADOR", length = 1)
	public String getAdministrador() {
		return this.administrador == null ? "S" : this.administrador;
	}

	public void setAdministrador(String administrador) {
		this.administrador = administrador;
	}

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "gczUsuarios")
	public List<GczServicio> getGczServiciosQueAudita() {
		return this.gczServiciosQueAudita;
	}

	public void setGczServiciosQueAudita(List<GczServicio> gczServicios) {
		this.gczServiciosQueAudita = gczServicios;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_USUARIO_GRUPO_USUARIO", schema = Constants.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_USUARIO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_GRUPO_USUARIO", nullable = false, updatable = false) })
	public List<GczGrupoUsuario> getGczGrupoUsuarios() {
		return this.gczGrupoUsuarios;
	}

	public void setGczGrupoUsuarios(List<GczGrupoUsuario> gczGrupoUsuarios) {
		this.gczGrupoUsuarios = gczGrupoUsuarios;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_USUARIO", schema = Constants.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_USUARIO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) })
	public List<GczPerfil> getGczPerfils() {
		return this.gczPerfils;
	}

	public void setGczPerfils(List<GczPerfil> gczPerfils) {
		this.gczPerfils = gczPerfils;
	}

	@OneToMany(/*orphanRemoval=true, */fetch = FetchType.EAGER, mappedBy = "gczUsuario", cascade = {CascadeType.ALL})
	public List<GczPropiedadUsuario> getGczPropiedadUsuarios() {
		return this.gczPropiedadUsuarios;
	}

	public void setGczPropiedadUsuarios(
			List<GczPropiedadUsuario> gczPropiedadUsuarios) {
		this.gczPropiedadUsuarios = gczPropiedadUsuarios;
	}

	@Override
	public String toString() {
		return "GczUsuario [id=" + id + ", login=" + login
				+ ", nombre=" + nombre + ", apellido1=" + apellido1
				+ ", apellido2=" + apellido2 + ", contrasenna=" + contrasenna
				+ ", correoElectronico=" + correoElectronico + ", estado="
				+ estado + ", bloqueado=" + bloqueado
				+ ", codigoServicioDefecto=" + codigoServicioDefecto
				+ ", codigoSeccionDefecto=" + codigoSeccionDefecto
				+ ", numIntentosFallidos=" + numIntentosFallidos
				+ ", fechaUltimoAcceso=" + fechaUltimoAcceso + ", visible="
				+ visible + ", creationDate=" + creationDate + ", lastUpdated="
				+ lastUpdated + ", pubDate=" + pubDate + ", usuarioAlta="
				+ usuarioAlta + ", usuarioMod=" + usuarioMod + ", usuarioPub="
				+ usuarioPub + ", administrador=" + administrador
				+ ", gczServicios=" + gczServiciosQueAudita + ", grupos="
				+ gczGrupoUsuarios + ", gczPerfils=" + gczPerfils
				+ ", gczPropiedadUsuarios=" + gczPropiedadUsuarios + "]";
	}

	public Usuario toUsuario() {
		return new Usuario(this.getId(), this.getLogin(),
				this.getNombre(), this.getApellido1(), this.getApellido2(), this.getSecretKey(),
				this.getCorreoElectronico(), this.getCodigoServicioDefecto(),this.getCodigoSeccionDefecto(), 0, 
				new Date(), this.getVisible(), this.getBloqueado(), this.getAdministrador());
	}

}
