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

@Entity
@Table(name = "GCZ_GRUPO_OPERACIONES", schema = Constants.ESQUEMA, uniqueConstraints = @UniqueConstraint(columnNames = {
		"CODIGO_SERVICIO", "CODIGO_SECCION", "CODIGO_GRUPO_OPERACIONES" }))
@SequenceGenerator(name = "SECUENCIA_GCZ_GRUPO_OPERACIONES_SEQ", sequenceName = "GCZ_GRUPO_OPERACIONES_SEQ", allocationSize = 1)
@DynamicUpdate(value = true)
public class GczGrupoOperaciones implements java.io.Serializable {

	private long idGrupoOperaciones;
	private GczSeccion gczSeccion;
	private String codigoGrupoOperaciones;
	private String nombre;
	private String descripcion;
	private String visible;
	private Date gczFechaalta;
	private Date gczFechamod;
	private Date gczFechapub;
	private String gczUsuarioalta;
	private String gczUsuariomod;
	private String gczUsuariopub;
	private String tipo;
	private Set<GczPerfil> gczPerfils = new HashSet<GczPerfil>(0);

	public GczGrupoOperaciones() {
	}

	public GczGrupoOperaciones(long idGrupoOperaciones, GczSeccion gczSeccion,
			String codigoGrupoOperaciones, String gczPublicado, String tipo) {
		this.idGrupoOperaciones = idGrupoOperaciones;
		this.gczSeccion = gczSeccion;
		this.codigoGrupoOperaciones = codigoGrupoOperaciones;
		this.visible = gczPublicado;
		this.tipo = tipo;
	}

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

	public GczGrupoOperaciones(long id) {
		this.idGrupoOperaciones = id;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA_GCZ_GRUPO_OPERACIONES_SEQ")
	@Column(name = "ID_GRUPO_OPERACIONES", unique = true, nullable = false, precision = 14, scale = 0)
	public long getIdGrupoOperaciones() {
		return this.idGrupoOperaciones;
	}

	public void setIdGrupoOperaciones(long idGrupoOperaciones) {
		this.idGrupoOperaciones = idGrupoOperaciones;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "CODIGO_SECCION", referencedColumnName = "CODIGO_SECCION", nullable = false),
			@JoinColumn(name = "CODIGO_SERVICIO", referencedColumnName = "CODIGO_SERVICIO", nullable = false) })
	public GczSeccion getGczSeccion() {
		return this.gczSeccion;
	}

	public void setGczSeccion(GczSeccion gczSeccion) {
		this.gczSeccion = gczSeccion;
	}

	@Column(name = "CODIGO_GRUPO_OPERACIONES", nullable = false, length = 100)
	public String getCodigoGrupoOperaciones() {
		return this.codigoGrupoOperaciones;
	}

	public void setCodigoGrupoOperaciones(String codigoGrupoOperaciones) {
		this.codigoGrupoOperaciones = codigoGrupoOperaciones;
	}

	@Column(name = "NOMBRE", length = 100)
	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	@Column(name = "DESCRIPCION", length = 1000)
	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Column(name = "GCZ_PUBLICADO", nullable = false, length = 1)
	@InList({"S", "N"})
	public String getVisible() {
		return this.visible;
	}

	public void setVisible(String gczPublicado) {
		this.visible = gczPublicado;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAALTA", length = 7)
	public Date getGczFechaalta() {
		return this.gczFechaalta;
	}

	public void setGczFechaalta(Date gczFechaalta) {
		this.gczFechaalta = gczFechaalta;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAMOD", length = 7)
	public Date getGczFechamod() {
		return this.gczFechamod;
	}

	public void setGczFechamod(Date gczFechamod) {
		this.gczFechamod = gczFechamod;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "GCZ_FECHAPUB", length = 7)
	public Date getGczFechapub() {
		return this.gczFechapub;
	}

	public void setGczFechapub(Date gczFechapub) {
		this.gczFechapub = gczFechapub;
	}

	@Column(name = "GCZ_USUARIOALTA", length = 100)
	public String getGczUsuarioalta() {
		return this.gczUsuarioalta;
	}

	public void setGczUsuarioalta(String gczUsuarioalta) {
		this.gczUsuarioalta = gczUsuarioalta;
	}

	@Column(name = "GCZ_USUARIOMOD", length = 100)
	public String getGczUsuariomod() {
		return this.gczUsuariomod;
	}

	public void setGczUsuariomod(String gczUsuariomod) {
		this.gczUsuariomod = gczUsuariomod;
	}

	@Column(name = "GCZ_USUARIOPUB", length = 100)
	public String getGczUsuariopub() {
		return this.gczUsuariopub;
	}

	public void setGczUsuariopub(String gczUsuariopub) {
		this.gczUsuariopub = gczUsuariopub;
	}

	@Column(name = "TIPO", nullable = false, length = 3)
	@InList({"PRE", "ESP"})
	public String getTipo() {
		return this.tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "GCZ_PERFIL_GRUPO_OPERACIONES", schema = Constants.ESQUEMA, joinColumns = { @JoinColumn(name = "ID_GRUPO_OPERACIONES", nullable = false, updatable = false) }, inverseJoinColumns = { @JoinColumn(name = "ID_PERFIL", nullable = false, updatable = false) })
	public Set<GczPerfil> getGczPerfils() {
		return this.gczPerfils;
	}

	public void setGczPerfils(Set<GczPerfil> gczPerfils) {
		this.gczPerfils = gczPerfils;
	}

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
