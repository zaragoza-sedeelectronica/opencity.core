package org.sede.servicio.acceso.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.Valid;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.NotBlank;
import org.sede.core.anotaciones.InList;
import org.sede.core.dao.EntidadBase;
import org.sede.servicio.acceso.Constants;

// TODO: Auto-generated Javadoc
/**
 * The Class GczSeccion.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 * 
 */
@Entity
@Table(name = "GCZ_SECCION", schema = Constants.ESQUEMA)
@DynamicUpdate(value = true)
public class GczSeccion extends EntidadBase implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4495862564155147267L;

	/** id. */
	private GczSeccionId id;
	
	/** gcz servicio. */
	private GczServicio gczServicio;
	
	/** nombre. */
	private String nombre;
	
	/** descripcion. */
	private String descripcion;
	
	/** estado. */
	private String estado;
	
	/** www url. */
	private String wwwUrl;
	
	/** ini url. */
	private String iniUrl;
	
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
	
	/** gcz grupo operacioneses. */
	@Valid
	private List<GczGrupoOperaciones> gczGrupoOperacioneses = new ArrayList<GczGrupoOperaciones>(
			0);

	/**
	 * Instantiates a new gcz seccion.
	 */
	public GczSeccion() {
	}
	
	/**
	 * Instantiates a new gcz seccion.
	 *
	 * @param servicio Servicio
	 * @param seccion Seccion
	 */
	public GczSeccion(String servicio, String seccion) {
		this.id = new GczSeccionId(seccion, servicio);
	}
	
	/**
	 * Instantiates a new gcz seccion.
	 *
	 * @param id Id
	 * @param gczServicio Gcz servicio
	 */
	public GczSeccion(GczSeccionId id, GczServicio gczServicio) {
		this.id = id;
		this.gczServicio = gczServicio;
	}

	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@EmbeddedId
	@AttributeOverrides({
			@AttributeOverride(name = "codigoSeccion", column = @Column(name = "CODIGO_SECCION", nullable = false, length = 30)),
			@AttributeOverride(name = "codigoServicio", column = @Column(name = "CODIGO_SERVICIO", nullable = false, length = 30)) })
	public GczSeccionId getId() {
		return this.id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(GczSeccionId id) {
		this.id = id;
	}

	/**
	 * Gets the gcz servicio.
	 *
	 * @return the gcz servicio
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CODIGO_SERVICIO", nullable = false, insertable = false, updatable = false)
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
	 * Gets the nombre.
	 *
	 * @return the nombre
	 */
	@NotBlank
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
	 * Gets the www url.
	 *
	 * @return the www url
	 */
	@Column(name = "WWW_URL", length = 400)
	public String getWwwUrl() {
		return this.wwwUrl;
	}

	/**
	 * Sets the www url.
	 *
	 * @param wwwUrl the new www url
	 */
	public void setWwwUrl(String wwwUrl) {
		this.wwwUrl = wwwUrl;
	}

	/**
	 * Gets the ini url.
	 *
	 * @return the ini url
	 */
	@Column(name = "INI_URL", length = 400)
	public String getIniUrl() {
		return this.iniUrl;
	}

	/**
	 * Sets the ini url.
	 *
	 * @param iniUrl the new ini url
	 */
	public void setIniUrl(String iniUrl) {
		this.iniUrl = iniUrl;
	}
	
	/**
	 * Gets the visible.
	 *
	 * @return the visible
	 */
	@NotBlank
	@InList({"S","N"})
	@Column(name = "GCZ_PUBLICADO", length = 1)
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
	 * Gets the gcz grupo operacioneses.
	 *
	 * @return the gcz grupo operacioneses
	 */
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "gczSeccion", cascade = CascadeType.ALL)
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
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "GczSeccion [id=" + id + ", gczServicio=" + gczServicio
				+ ", nombre=" + nombre + ", descripcion=" + descripcion
				+ ", estado=" + estado + ", wwwUrl=" + wwwUrl + ", iniUrl="
				+ iniUrl + ", visible=" + visible
				+ ", creationDate="
				+ creationDate + ", lastUpdated=" + lastUpdated
				+ ", pubDate=" + pubDate + ", usuarioAlta="
				+ usuarioAlta + ", usuarioMod=" + usuarioMod
				+ ", usuarioPub=" + usuarioPub 
				+ "]";
	}

}
