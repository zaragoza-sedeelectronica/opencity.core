package org.sede.servicio.acceso.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.DynamicUpdate;
import org.sede.core.anotaciones.InList;
import org.sede.servicio.acceso.Constants;

// TODO: Auto-generated Javadoc
/**
 * The Class GczGrupoOperaciones.
 * 
 * @autor Ayuntamiento de Zaragoza
 */
@Entity
@Table(name = "GCZ_GRUPO_OPERACIONES", schema = Constants.ESQUEMA, uniqueConstraints = @UniqueConstraint(columnNames = {
		"CODIGO_SERVICIO", "CODIGO_SECCION", "CODIGO_GRUPO_OPERACIONES" }))
@SequenceGenerator(name = "SECUENCIA_GCZ_GRUPO_OPERACIONES_SEQ", sequenceName = "GCZ_GRUPO_OPERACIONES_SEQ", allocationSize = 1)
@DynamicUpdate(value = true)
public class GczGrupoOperaciones implements java.io.Serializable {

	/** Constant serialVersionUID. */
	private static final long serialVersionUID = -69377272279402496L;

	/** id grupo operaciones. */
	private long idGrupoOperaciones;
	
	/** gcz seccion. */
	private GczSeccion gczSeccion;
	
	/** codigo grupo operaciones. */
	private String codigoGrupoOperaciones;
	
	/** nombre. */
	private String nombre;
	
	/** descripcion. */
	private String descripcion;
	
	/** visible. */
	private String visible;
	
	/** gcz fechaalta. */
	private Date gczFechaalta;
	
	/** gcz fechamod. */
	private Date gczFechamod;
	
	/** gcz fechapub. */
	private Date gczFechapub;
	
	/** gcz usuarioalta. */
	private String gczUsuarioalta;
	
	/** gcz usuariomod. */
	private String gczUsuariomod;
	
	/** gcz usuariopub. */
	private String gczUsuariopub;
	
	/** tipo. */
	private String tipo;
	
	/** gcz perfils. */
	private Set<GczPerfil> gczPerfils = new HashSet<GczPerfil>(0);

	/**
	 * Instantiates a new gcz grupo operaciones.
	 */
	public GczGrupoOperaciones() {
	}

	/**
	 * Instantiates a new gcz grupo operaciones.
	 *
	 * @param idGrupoOperaciones Id grupo operaciones
	 * @param gczSeccion Gcz seccion
	 * @param codigoGrupoOperaciones Codigo grupo operaciones
	 * @param gczPublicado Gcz publicado
	 * @param tipo Tipo
	 */
	public GczGrupoOperaciones(long idGrupoOperaciones, GczSeccion gczSeccion,
			String codigoGrupoOperaciones, String gczPublicado, String tipo) {
		this.idGrupoOperaciones = idGrupoOperaciones;
		this.gczSeccion = gczSeccion;
		this.codigoGrupoOperaciones = codigoGrupoOperaciones;
		this.visible = gczPublicado;
		this.tipo = tipo;
	}

	/**
	 * Instantiates a new gcz grupo operaciones.
	 *
	 * @param idGrupoOperaciones Id grupo operaciones
	 * @param gczSeccion Gcz seccion
	 * @param codigoGrupoOperaciones Codigo grupo operaciones
	 * @param nombre Nombre
	 * @param descripcion Descripcion
	 * @param gczPublicado Gcz publicado
	 * @param gczFechaalta Gcz fechaalta
	 * @param gczFechamod Gcz fechamod
	 * @param gczFechapub Gcz fechapub
	 * @param gczUsuarioalta Gcz usuarioalta
	 * @param gczUsuariomod Gcz usuariomod
	 * @param gczUsuariopub Gcz usuariopub
	 * @param tipo Tipo
	 * @param gczPerfils Gcz perfils
	 */
	public GczGrupoOperaciones(long idGrupoOperaciones, GczSeccion gczSeccion,
			String codigoGrupoOperaciones, String nombre, String descripcion,
			String gczPublicado, Date gczFechaalta, Date gczFechamod,
			Date gczFechapub, String gczUsuarioalta, String gczUsuariomod,
			String gczUsuariopub, String tipo, Set<GczPerfil> gczPerfils) {
		this.idGrupoOperaciones = idGrupoOperaciones;
		this.gczSeccion = gczSeccion;
		this.codigoGrupoOperaciones = codigoGrupoOperaciones;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.visible = gczPublicado;
		this.gczFechaalta = gczFechaalta;
		this.gczFechamod = gczFechamod;
		this.gczFechapub = gczFechapub;
		this.gczUsuarioalta = gczUsuarioalta;
		this.gczUsuariomod = gczUsuariomod;
		this.gczUsuariopub = gczUsuariopub;
		this.tipo = tipo;
		this.gczPerfils = gczPerfils;
	}

	/**
	 * Instantiates a new gcz grupo operaciones.
	 *
	 * @param id Id
	 */
	public GczGrupoOperaciones(long id) {
		this.idGrupoOperaciones = id;
	}

	/**
	 * Gets the id grupo operaciones.
	 *
	 * @return the id grupo operaciones
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA_GCZ_GRUPO_OPERACIONES_SEQ")
	@Column(name = "ID_GRUPO_OPERACIONES", unique = true, nullable = false, precision = 14, scale = 0)
	public long getIdGrupoOperaciones() {
		return this.idGrupoOperaciones;
	}

	/**
	 * Sets the id grupo operaciones.
	 *
	 * @param idGrupoOperaciones the new id grupo operaciones
	 */
	public void setIdGrupoOperaciones(long idGrupoOperaciones) {
		this.idGrupoOperaciones = idGrupoOperaciones;
	}

	/**
	 * Gets the gcz seccion.
	 *
	 * @return the gcz seccion
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "CODIGO_SECCION", referencedColumnName = "CODIGO_SECCION", nullable = false),
			@JoinColumn(name = "CODIGO_SERVICIO", referencedColumnName = "CODIGO_SERVICIO", nullable = false) })
	public GczSeccion getGczSeccion() {
		return this.gczSeccion;
	}

	/**
	 * Sets the gcz seccion.
	 *
	 * @param gczSeccion the new gcz seccion
	 */
	public void setGczSeccion(GczSeccion gczSeccion) {
		this.gczSeccion = gczSeccion;
	}

	/**
	 * Gets the codigo grupo operaciones.
	 *
	 * @return the codigo grupo operaciones
	 */
	@Column(name = "CODIGO_GRUPO_OPERACIONES", nullable = false, length = 100)
	public String getCodigoGrupoOperaciones() {
		return this.codigoGrupoOperaciones;
	}

	/**
	 * Sets the codigo grupo operaciones.
	 *
	 * @param codigoGrupoOperaciones the new codigo grupo operaciones
	 */
	public void setCodigoGrupoOperaciones(String codigoGrupoOperaciones) {
		this.codigoGrupoOperaciones = codigoGrupoOperaciones;
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
	 * Gets the descripcion.
	 *
	 * @return the descripcion
	 */
	@Column(name = "DESCRIPCION", length = 1000)
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
	 * Gets the gcz fechaalta.
	 *
	 * @return the gcz fechaalta
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAALTA", length = 7)
	public Date getGczFechaalta() {
		return this.gczFechaalta;
	}

	/**
	 * Sets the gcz fechaalta.
	 *
	 * @param gczFechaalta the new gcz fechaalta
	 */
	public void setGczFechaalta(Date gczFechaalta) {
		this.gczFechaalta = gczFechaalta;
	}

	/**
	 * Gets the gcz fechamod.
	 *
	 * @return the gcz fechamod
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAMOD", length = 7)
	public Date getGczFechamod() {
		return this.gczFechamod;
	}

	/**
	 * Sets the gcz fechamod.
	 *
	 * @param gczFechamod the new gcz fechamod
	 */
	public void setGczFechamod(Date gczFechamod) {
		this.gczFechamod = gczFechamod;
	}

	/**
	 * Gets the gcz fechapub.
	 *
	 * @return the gcz fechapub
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAPUB", length = 7)
	public Date getGczFechapub() {
		return this.gczFechapub;
	}

	/**
	 * Sets the gcz fechapub.
	 *
	 * @param gczFechapub the new gcz fechapub
	 */
	public void setGczFechapub(Date gczFechapub) {
		this.gczFechapub = gczFechapub;
	}

	/**
	 * Gets the gcz usuarioalta.
	 *
	 * @return the gcz usuarioalta
	 */
	@Column(name = "GCZ_USUARIOALTA", length = 100)
	public String getGczUsuarioalta() {
		return this.gczUsuarioalta;
	}

	/**
	 * Sets the gcz usuarioalta.
	 *
	 * @param gczUsuarioalta the new gcz usuarioalta
	 */
	public void setGczUsuarioalta(String gczUsuarioalta) {
		this.gczUsuarioalta = gczUsuarioalta;
	}

	/**
	 * Gets the gcz usuariomod.
	 *
	 * @return the gcz usuariomod
	 */
	@Column(name = "GCZ_USUARIOMOD", length = 100)
	public String getGczUsuariomod() {
		return this.gczUsuariomod;
	}

	/**
	 * Sets the gcz usuariomod.
	 *
	 * @param gczUsuariomod the new gcz usuariomod
	 */
	public void setGczUsuariomod(String gczUsuariomod) {
		this.gczUsuariomod = gczUsuariomod;
	}

	/**
	 * Gets the gcz usuariopub.
	 *
	 * @return the gcz usuariopub
	 */
	@Column(name = "GCZ_USUARIOPUB", length = 100)
	public String getGczUsuariopub() {
		return this.gczUsuariopub;
	}

	/**
	 * Sets the gcz usuariopub.
	 *
	 * @param gczUsuariopub the new gcz usuariopub
	 */
	public void setGczUsuariopub(String gczUsuariopub) {
		this.gczUsuariopub = gczUsuariopub;
	}

	/**
	 * Gets the tipo.
	 *
	 * @return the tipo
	 */
	@Column(name = "TIPO", nullable = false, length = 3)
	@InList({"PRE", "ESP"})
	public String getTipo() {
		return this.tipo;
	}

	/**
	 * Sets the tipo.
	 *
	 * @param tipo the new tipo
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * Gets the gcz perfils.
	 *
	 * @return the gcz perfils
	 */
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_GRUPO_OPERACIONES", schema = Constants.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_GRUPO_OPERACIONES", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) })
	public Set<GczPerfil> getGczPerfils() {
		return this.gczPerfils;
	}

	/**
	 * Sets the gcz perfils.
	 *
	 * @param gczPerfils the new gcz perfils
	 */
	public void setGczPerfils(Set<GczPerfil> gczPerfils) {
		this.gczPerfils = gczPerfils;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "GczGrupoOperaciones [idGrupoOperaciones=" + idGrupoOperaciones
				+ ", codigoGrupoOperaciones="
				+ codigoGrupoOperaciones + ", nombre=" + nombre
				+ ", descripcion=" + descripcion + ", visible="
				+ visible + ", gczFechaalta=" + gczFechaalta
				+ ", gczFechamod=" + gczFechamod + ", gczFechapub="
				+ gczFechapub + ", gczUsuarioalta=" + gczUsuarioalta
				+ ", gczUsuariomod=" + gczUsuariomod + ", gczUsuariopub="
				+ gczUsuariopub + ", tipo=" + tipo + ", gczPerfils="
				+ gczPerfils + "]";
	}

}
