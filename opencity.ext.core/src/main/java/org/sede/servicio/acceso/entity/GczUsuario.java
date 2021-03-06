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
import org.sede.servicio.acceso.ConfigAcceso;

/**
 * The Class GczUsuario.
 * 
 * @autor Ayuntamiento de Zaragoza
 */
@Entity
@EntityListeners(PersistenceContextListener.class)
@Table(name = "GCZ_USUARIO", schema = ConfigAcceso.ESQUEMA, uniqueConstraints = @UniqueConstraint(columnNames = "LOGIN"))
@DynamicUpdate
@SelectBeforeUpdate
public class GczUsuario extends EntidadBase implements java.io.Serializable {

	/** Constant serialVersionUID. */
	private static final long serialVersionUID = -390853362927679017L;
	
	/** id. */
	private BigDecimal id;
	
	/** login. */
	private String login;
	
	/** nombre. */
	private String nombre;
	
	/** apellido 1. */
	private String apellido1;
	
	/** apellido 2. */
	private String apellido2;
	
	/** contrasenna. */
	private String contrasenna;
	
	/** secret key. */
	private String secretKey;
	
	/** correo electronico. */
	private String correoElectronico;
	
	/** estado. */
	private String estado;
	
	/** bloqueado. */
	private String bloqueado;
	
	/** codigo servicio defecto. */
	private String codigoServicioDefecto;
	
	/** codigo seccion defecto. */
	private String codigoSeccionDefecto;
	
	/** num intentos fallidos. */
	private BigDecimal numIntentosFallidos;
	
	/** fecha ultimo acceso. */
	private Date fechaUltimoAcceso;
	
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
	
	/** administrador. */
	private String administrador;
	
	/** gcz servicios que audita. */
	@SoloEnEstaEntidad
	private List<GczServicio> gczServiciosQueAudita = new ArrayList<GczServicio>(0);
	
	/** gcz grupo usuarios. */
	@SoloEnEstaEntidad
	private List<GczGrupoUsuario> gczGrupoUsuarios = new ArrayList<GczGrupoUsuario>(0);
	
	/** gcz perfils. */
	@SoloEnEstaEntidad
	private List<GczPerfil> gczPerfils = new ArrayList<GczPerfil>(0);
	
	/** gcz propiedad usuarios. */
	@SoloEnEstaEntidad
	private List<GczPropiedadUsuario> gczPropiedadUsuarios = new ArrayList<GczPropiedadUsuario>(0);

	/**
	 * Instantiates a new gcz usuario.
	 */
	public GczUsuario() {
		super();
	}

	/**
	 * Instantiates a new gcz usuario.
	 *
	 * @param id Id
	 */
	public GczUsuario(BigDecimal id) {
		this.id = id;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Id
	@Column(name = "ID_USUARIO", unique = true, nullable = false, precision = 14, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param idUsuario the new id
	 */
	public void setId(BigDecimal idUsuario) {
		this.id = idUsuario;
	}

	/**
	 * Gets the login.
	 *
	 * @return the login
	 */
	@Column(name = "LOGIN", unique = true, nullable = false, length = 100, updatable = false)
	public String getLogin() {
		return this.login;
	}

	/**
	 * Sets the login.
	 *
	 * @param login the new login
	 */
	public void setLogin(String login) {
		this.login = login;
	}

	/**
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	@Column(name = "NOMBRE", length = 100)
	public String getNombre() {
		return this.nombre;
	}

	/**
	 * Sets the nombre.
	 *
	 * @param nombre the new nombre
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * Gets the apellido 1.
	 *
	 * @return the apellido 1
	 */
	@Column(name = "APELLIDO_1", length = 100)
	public String getApellido1() {
		return this.apellido1;
	}

	/**
	 * Sets the apellido 1.
	 *
	 * @param apellido1 the new apellido 1
	 */
	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	/**
	 * Gets the apellido 2.
	 *
	 * @return the apellido 2
	 */
	@Column(name = "APELLIDO_2", length = 100)
	public String getApellido2() {
		return this.apellido2;
	}

	/**
	 * Sets the apellido 2.
	 *
	 * @param apellido2 the new apellido 2
	 */
	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	/**
	 * Gets the contrasenna.
	 *
	 * @return the contrasenna
	 */
	@Column(name = "CONTRASENNA", nullable = false, length = 100, updatable = false)
	public String getContrasenna() {
		return this.contrasenna;
	}

	/**
	 * Sets the contrasenna.
	 *
	 * @param contrasenna the new contrasenna
	 */
	public void setContrasenna(String contrasenna) {
		this.contrasenna = contrasenna;
	}

	/**
	 * Gets the secret key.
	 *
	 * @return the secret key
	 */
	@Column(name = "SECRETKEY", length = 200, updatable = false)
	public String getSecretKey() {
		return this.secretKey;
	}

	/**
	 * Sets the secret key.
	 *
	 * @param secretKey the new secret key
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
	
	/**
	 * Gets the correo electronico.
	 *
	 * @return the correo electronico
	 */
	@Column(name = "CORREO_ELECTRONICO", nullable = false, length = 100)
	public String getCorreoElectronico() {
		return this.correoElectronico;
	}

	/**
	 * Sets the correo electronico.
	 *
	 * @param correoElectronico the new correo electronico
	 */
	public void setCorreoElectronico(String correoElectronico) {
		this.correoElectronico = correoElectronico;
	}

	/**
	 * Gets the estado.
	 *
	 * @return the estado
	 */
	@Column(name = "ESTADO", length = 1, updatable = false)
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
	 * Gets the bloqueado.
	 *
	 * @return the bloqueado
	 */
	@Column(name = "BLOQUEADO", length = 1, updatable = false)
	public String getBloqueado() {
		return this.bloqueado;
	}

	/**
	 * Sets the bloqueado.
	 *
	 * @param bloqueado the new bloqueado
	 */
	public void setBloqueado(String bloqueado) {
		this.bloqueado = bloqueado;
	}

	/**
	 * Gets the codigo servicio defecto.
	 *
	 * @return the codigo servicio defecto
	 */
	@Column(name = "CODIGO_SERVICIO_DEFECTO", nullable = false, length = 30)
	public String getCodigoServicioDefecto() {
		return this.codigoServicioDefecto;
	}

	/**
	 * Sets the codigo servicio defecto.
	 *
	 * @param codigoServicioDefecto the new codigo servicio defecto
	 */
	public void setCodigoServicioDefecto(String codigoServicioDefecto) {
		this.codigoServicioDefecto = codigoServicioDefecto;
	}

	/**
	 * Gets the codigo seccion defecto.
	 *
	 * @return the codigo seccion defecto
	 */
	@Column(name = "CODIGO_SECCION_DEFECTO", nullable = false, length = 30)
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
	 * Gets the num intentos fallidos.
	 *
	 * @return the num intentos fallidos
	 */
	@Column(name = "NUM_INTENTOS_FALLIDOS", nullable = false, precision = 22, scale = 0, updatable = false)
	public BigDecimal getNumIntentosFallidos() {
		return this.numIntentosFallidos;
	}

	/**
	 * Sets the num intentos fallidos.
	 *
	 * @param numIntentosFallidos the new num intentos fallidos
	 */
	public void setNumIntentosFallidos(BigDecimal numIntentosFallidos) {
		this.numIntentosFallidos = numIntentosFallidos;
	}

	/**
	 * Gets the fecha ultimo acceso.
	 *
	 * @return the fecha ultimo acceso
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "FECHA_ULTIMO_ACCESO", length = 7)
	public Date getFechaUltimoAcceso() {
		return this.fechaUltimoAcceso;
	}

	/**
	 * Sets the fecha ultimo acceso.
	 *
	 * @param fechaUltimoAcceso the new fecha ultimo acceso
	 */
	public void setFechaUltimoAcceso(Date fechaUltimoAcceso) {
		this.fechaUltimoAcceso = fechaUltimoAcceso;
	}

	/**
	 * Gets the visible.
	 *
	 * @return the visible
	 */
	@Column(name = "GCZ_PUBLICADO", nullable = false, length = 1)
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
	@Column(name = "GCZ_FECHAALTA", length = 7)
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
	@Column(name = "GCZ_FECHAPUB", length = 7)
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
	@Column(name = "GCZ_USUARIOPUB", length = 100)
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
	 * Gets the administrador.
	 *
	 * @return the administrador
	 */
	@Column(name = "ADMINISTRADOR", length = 1)
	public String getAdministrador() {
		return this.administrador == null ? "S" : this.administrador;
	}

	/**
	 * Sets the administrador.
	 *
	 * @param administrador the new administrador
	 */
	public void setAdministrador(String administrador) {
		this.administrador = administrador;
	}

	/**
	 * Gets the gcz servicios que audita.
	 *
	 * @return the gcz servicios que audita
	 */
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "gczUsuarios")
	public List<GczServicio> getGczServiciosQueAudita() {
		return this.gczServiciosQueAudita;
	}

	/**
	 * Sets the gcz servicios que audita.
	 *
	 * @param gczServicios the new gcz servicios que audita
	 */
	public void setGczServiciosQueAudita(List<GczServicio> gczServicios) {
		this.gczServiciosQueAudita = gczServicios;
	}

	/**
	 * Gets the gcz grupo usuarios.
	 *
	 * @return the gcz grupo usuarios
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_USUARIO_GRUPO_USUARIO", schema = ConfigAcceso.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_USUARIO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_GRUPO_USUARIO", nullable = false, updatable = false) })
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
	 * Gets the gcz perfils.
	 *
	 * @return the gcz perfils
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_USUARIO", schema = ConfigAcceso.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_USUARIO", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) })
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
	 * Gets the gcz propiedad usuarios.
	 *
	 * @return the gcz propiedad usuarios
	 */
	@OneToMany(/*orphanRemoval=true, */fetch = FetchType.EAGER, mappedBy = "gczUsuario", cascade = {CascadeType.ALL})
	public List<GczPropiedadUsuario> getGczPropiedadUsuarios() {
		return this.gczPropiedadUsuarios;
	}

	/**
	 * Sets the gcz propiedad usuarios.
	 *
	 * @param gczPropiedadUsuarios the new gcz propiedad usuarios
	 */
	public void setGczPropiedadUsuarios(
			List<GczPropiedadUsuario> gczPropiedadUsuarios) {
		this.gczPropiedadUsuarios = gczPropiedadUsuarios;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
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

	/**
	 * To usuario.
	 *
	 * @return usuario
	 */
	public Usuario toUsuario() {
		return new Usuario(this.getId(), this.getLogin(),
				this.getNombre(), this.getApellido1(), this.getApellido2(), this.getSecretKey(),
				this.getCorreoElectronico(), this.getCodigoServicioDefecto(),this.getCodigoSeccionDefecto(), 0, 
				new Date(), this.getVisible(), this.getBloqueado(), this.getAdministrador());
	}

}
