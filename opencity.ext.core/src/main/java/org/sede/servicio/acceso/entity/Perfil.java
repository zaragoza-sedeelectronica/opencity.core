
package org.sede.servicio.acceso.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.sede.core.anotaciones.Description;
import org.sede.core.anotaciones.InList;
import org.sede.core.anotaciones.PathId;
import org.sede.core.dao.EntidadBase;
import org.sede.servicio.acceso.ConfigCiudadano;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * The Class Perfil.
 */
@XmlRootElement(name = "perfilUsuario")
@Entity
@Table(name = "PERFILES_USUARIOS", schema = ConfigCiudadano.ESQUEMA)
@PathId("/servicio/comunidad")
@DynamicUpdate
@Description("Perfil del ciudadano para la interacción en la Comunidad")
@XmlAccessorType(XmlAccessType.FIELD)
public class Perfil extends EntidadBase {

    /** Constant serialVersionUID. */
	private static final long serialVersionUID = 9024222274194881204L;


	/** Las preferencias sobre categorías de actividades preferidas del usuario y otras preferencias del usuario se modelarían como una nueva entidad relacionada con Ciudadano. */

    @Id
    @Column(name = "ID_USUARIO_ADENTRA")
    @Digits(integer = 22, fraction = 0)
    private BigDecimal id;
    
    
    //Definición preferencias en relación a actividades
    /** temas. */
    //  - tipología de actividades que son de su interés
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USERS_TEMA", schema = ConfigCiudadano.ESQUEMA,
    		joinColumns = { @JoinColumn(name = "ID_USUARIO_ADENTRA", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "COD_TEMA", nullable = false, updatable = false) })
    @Access(AccessType.FIELD)
    @BatchSize(size = 50)
    @Fetch(FetchMode.JOIN)
    @Valid
    private Set<Tema> temas = new HashSet<Tema>(0);
    
    /** valores. */
    //  - valores que promueven las actividades),
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = ConfigCiudadano.ESQUEMA, name = "USERS_VALOR",
    		joinColumns = { @JoinColumn(name = "ID_USUARIO_ADENTRA", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "COD_VALOR", nullable = false, updatable = false) })
    @Access(AccessType.FIELD)
    @BatchSize(size = 50)
    @Fetch(FetchMode.JOIN)
    @Valid
    private Set<Valor> valores = new HashSet<Valor>(0);
    
    
    /** fecha nacimiento. */
    @Column(name = "FECHA_NACIMIENTO", length = 7)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    
    
    /** telefono contacto. */
    @Column(name = "telefonocontacto")
    private String telefonoContacto;
    
    
    /** tema ids. */
    // Listas de ids de los temas y valores, para el formulario de perfil
    @Transient
    private List<BigDecimal> temaIds = new ArrayList<BigDecimal>();
    
    /** valor ids. */
    @Transient
    private List<BigDecimal> valorIds = new ArrayList<BigDecimal>();
    

    /** visible. */
    //  configuración de visibilidad: si menor, no saldrá en los listados de asistentes, etc. sólo saldrá el número de inscritos (no ‘ZAC Junior’)
    @Column(name = "VISIBLE")
    @Size(max = 1)
    @InList({ "S", "N" })
    private String visible;

    //Datos para definir el perfil que necesitan para sacar estadísticas:
    //edad (según rangos), la fecha de nacimiento está en Ciudadano
    
    /** genero. */
    // género
    @Column(name = "GENERO")
    @Size(max = 1)
    @InList({ "F", "M", "O" })
    private String genero;
    
    /** pais. */
    // país
    @Column(name = "PAIS")
    @Size(max = 100)
    private String pais;
    
    /** estudios. */
    // nivel estudios terminados
    @Column(name = "ESTUDIOS")
    @Size(max = 150)
    private String estudios;
    
    /** estudios actuales. */
    // Estudios actuales (ZGZ16)
    @Column(name = "ESTUDIOS_ACTUALES")
    @Size(max = 150)
    private String estudiosActuales;
    
    /** ocupacion. */
    // ocupación
    @Column(name = "OCUPACION")
    @Size(max = 150)
    private String ocupacion;
    
    /** independiente. */
    // ¿vives independiente?
    @Column(name = "INDEPENDIENTE")
    @Size(max = 1)
    @InList({ "S", "N" })
    private String independiente;

    /** necesidades especiales. */
    //  campo de texto libre ‘Necesidades especiales’.
    @Column(name = "NECESIDADES_ESPECIALES")
    @Size(max = 2000)
    private String necesidadesEspeciales;

    /** formato. */
    //  ‘Formato’
    @Column(name = "FORMATO")
    @Size(max = 150)
    private String formato;
    
    // Distrito de residencia. Está en Ciudadano
    /** distrito. */
    // Preguntamos por "Barrio en el que vives"
    @Transient
    private String distrito;
    

    /**
     * Gets the id.
     *
     * @return the id
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }   
   
    /**
     * Gets the temas.
     *
     * @return the temas
     */
    public Set<Tema> getTemas() {
        return temas;
    }

    /**
     * Sets the temas.
     *
     * @param temas the new temas
     */
    public void setTemas(Set<Tema> temas) {
        this.temas = temas;
    }

    /**
     * Gets the valores.
     *
     * @return the valores
     */
    public Set<Valor> getValores() {
		return valores;
	}

	/**
	 * Sets the valores.
	 *
	 * @param valores the new valores
	 */
	public void setValores(Set<Valor> valores) {
		this.valores = valores;
	}

    /**
     * Gets the visible.
     *
     * @return the visible
     */
    public String getVisible() {
        return visible;
    }

    /**
     * Sets the visible.
     *
     * @param visible the new visible
     */
    public void setVisible(String visible) {
        this.visible = visible;
    }

    /**
     * Gets the genero.
     *
     * @return the genero
     */
    public String getGenero() {
		return genero;
	}

	/**
	 * Sets the genero.
	 *
	 * @param genero the new genero
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	 * Gets the pais.
	 *
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}

	/**
	 * Sets the pais.
	 *
	 * @param pais the new pais
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}

	/**
	 * Gets the estudios.
	 *
	 * @return the estudios
	 */
	public String getEstudios() {
		return estudios;
	}

	/**
	 * Sets the estudios.
	 *
	 * @param estudios the new estudios
	 */
	public void setEstudios(String estudios) {
		this.estudios = estudios;
	}

	/**
	 * Gets the estudios actuales.
	 *
	 * @return the estudios actuales
	 */
	public String getEstudiosActuales() {
		return estudiosActuales;
	}

	/**
	 * Sets the estudios actuales.
	 *
	 * @param estudiosActuales the new estudios actuales
	 */
	public void setEstudiosActuales(String estudiosActuales) {
		this.estudiosActuales = estudiosActuales;
	}

	/**
	 * Gets the ocupacion.
	 *
	 * @return the ocupacion
	 */
	public String getOcupacion() {
		return ocupacion;
	}

	/**
	 * Sets the ocupacion.
	 *
	 * @param ocupacion the new ocupacion
	 */
	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	/**
	 * Gets the formato.
	 *
	 * @return the formato
	 */
	public String getFormato() {
		return formato;
	}

	/**
	 * Sets the formato.
	 *
	 * @param formato the new formato
	 */
	public void setFormato(String formato) {
		this.formato = formato;
	}

	/**
	 * Gets the independiente.
	 *
	 * @return the independiente
	 */
	public String getIndependiente() {
		return independiente;
	}

	/**
	 * Sets the independiente.
	 *
	 * @param independiente the new independiente
	 */
	public void setIndependiente(String independiente) {
		this.independiente = independiente;
	}

	/**
	 * Gets the necesidades especiales.
	 *
	 * @return the necesidades especiales
	 */
	public String getNecesidadesEspeciales() {
		return necesidadesEspeciales;
	}

	/**
	 * Sets the necesidades especiales.
	 *
	 * @param necesidadesEspeciales the new necesidades especiales
	 */
	public void setNecesidadesEspeciales(String necesidadesEspeciales) {
		this.necesidadesEspeciales = necesidadesEspeciales;
	}

	
	/**
	 * Gets the tema ids.
	 *
	 * @return the tema ids
	 */
	public List<BigDecimal> getTemaIds() {
		return temaIds;
	}

	/**
	 * Sets the tema ids.
	 *
	 * @param temaIds the new tema ids
	 */
	public void setTemaIds(List<BigDecimal> temaIds) {
		this.temaIds = temaIds;
	}

	/**
	 * Gets the valor ids.
	 *
	 * @return the valor ids
	 */
	public List<BigDecimal> getValorIds() {
		return valorIds;
	}

	/**
	 * Sets the valor ids.
	 *
	 * @param valorIds the new valor ids
	 */
	public void setValorIds(List<BigDecimal> valorIds) {
		this.valorIds = valorIds;
	}

	/**
	 * Gets the fecha nacimiento.
	 *
	 * @return the fecha nacimiento
	 */
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * Sets the fecha nacimiento.
	 *
	 * @param fechaNacimiento the new fecha nacimiento
	 */
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * Gets the distrito.
	 *
	 * @return the distrito
	 */
	public String getDistrito() {
		return distrito;
	}

	/**
	 * Sets the distrito.
	 *
	 * @param distrito the new distrito
	 */
	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}
	
    /**
     * Gets the telefono contacto.
     *
     * @return the telefono contacto
     */
    public String getTelefonoContacto() {
		return telefonoContacto;
	}

	/**
	 * Sets the telefono contacto.
	 *
	 * @param telefonoContacto the new telefono contacto
	 */
	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}

	/**
	 * Gets the edad.
	 *
	 * @return the edad
	 */
	public Integer getEdad() {
    	if (this.fechaNacimiento == null) {
    		return null;
    	}
    	if (this.fechaNacimiento.after(new Date())) {
    		// Aún no ha nacido, no debería pasar...
    		return null;
    	}
    	
    	Calendar ahora = Calendar.getInstance();
    	Calendar nacimiento = Calendar.getInstance();
    	nacimiento.setTime(this.fechaNacimiento);
    	if (nacimiento.after(ahora)) {
    		return -1;
    	}
    	int anio1 = ahora.get(Calendar.YEAR);
    	int anio2 = nacimiento.get(Calendar.YEAR);
    	int edad = anio1 - anio2;
    	int mes1 = ahora.get(Calendar.MONTH);
    	int mes2 = nacimiento.get(Calendar.MONTH);
    	if (mes2 > mes1) {
    		edad--;
    	} else if (mes1 == mes2) {
    		int dia1 = ahora.get(Calendar.DAY_OF_MONTH);
    		int dia2 = nacimiento.get(Calendar.DAY_OF_MONTH);
    		if (dia2 > dia1) {
    			edad--;
    		}
    	}
    	return edad;
    }

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
    public String toString() {
        return "Perfil [id=" + id + ", visible=" + visible + ", genero=" + genero + ", pais=" + pais
        		+ ", fechaNacimiento=" + fechaNacimiento +
        		", estudios=" + estudios + ", independiente=" + independiente + "]";
    }
}
