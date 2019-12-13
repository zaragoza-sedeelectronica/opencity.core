
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
import org.springframework.format.annotation.DateTimeFormat;

@XmlRootElement(name = "perfilUsuario")
@Entity
@Table(name = "PERFILES_USUARIOS", schema = "NOTICIAS")
@PathId("/servicio/comunidad")
@DynamicUpdate
@Description("Perfil del ciudadano para la interacción en la Comunidad")
@XmlAccessorType(XmlAccessType.FIELD)
//@EntityListeners(ComunidadPersistenceContextListener.class)
public class Perfil extends EntidadBase {

    /**
     * Las preferencias sobre categorías de actividades preferidas del usuario y
     * otras preferencias del usuario se modelarían como una nueva entidad
     * relacionada con Ciudadano
     */

    @Id
    @Column(name = "ID_USUARIO_ADENTRA")
    @Digits(integer = 22, fraction = 0)
    private BigDecimal id;
    
    
    //Definición preferencias en relación a actividades
    //  - tipología de actividades que son de su interés
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USERS_TEMA", schema = "NOTICIAS",
    		joinColumns = { @JoinColumn(name = "ID_USUARIO_ADENTRA", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "COD_TEMA", nullable = false, updatable = false) })
    @Access(AccessType.FIELD)
    @BatchSize(size = 50)
    @Fetch(FetchMode.JOIN)
    @Valid
//    @NotEmpty
    private Set<Tema> temas = new HashSet<Tema>(0);
    
    //  - valores que promueven las actividades),
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(schema = "NOTICIAS", name = "USERS_VALOR",
    		joinColumns = { @JoinColumn(name = "ID_USUARIO_ADENTRA", nullable = false, updatable = false) },
            inverseJoinColumns = { @JoinColumn(name = "COD_VALOR", nullable = false, updatable = false) })
    @Access(AccessType.FIELD)
    @BatchSize(size = 50)
    @Fetch(FetchMode.JOIN)
    @Valid
//    @NotEmpty
    private Set<Valor> valores = new HashSet<Valor>(0);
    
    
    @Column(name = "FECHA_NACIMIENTO", length = 7)
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    @Temporal(TemporalType.DATE)
    private Date fechaNacimiento;
    
    
    @Column(name = "telefonocontacto")
    private String telefonoContacto;
    
    
    // Listas de ids de los temas y valores, para el formulario de perfil
    @Transient
    private List<BigDecimal> temaIds = new ArrayList<BigDecimal>();
    
    @Transient
    private List<BigDecimal> valorIds = new ArrayList<BigDecimal>();
    

    // consentimiento recepción info, está en Ciudadano  

    /*
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "USERS_SECTOR", joinColumns = { @JoinColumn(name = "ID_USUARIO_ADDENTRA", nullable = false, updatable = false) }, 
            inverseJoinColumns = { @JoinColumn(name = "COD_SECTOR", nullable = false, updatable = false), })
    @Access(AccessType.FIELD)
    @BatchSize(size = 50)
    @Fetch(FetchMode.JOIN)
    private Set<SectorPoblacion> population = new HashSet<SectorPoblacion>(0);
    */

    //  configuración de visibilidad: si menor, no saldrá en los listados de asistentes, etc. sólo saldrá el número de inscritos (no ‘ZAC Junior’)
    @Column(name = "VISIBLE")
    @Size(max = 1)
    @InList({ "S", "N" })
    private String visible;

    //Datos para definir el perfil que necesitan para sacar estadísticas:
    //edad (según rangos), la fecha de nacimiento está en Ciudadano
    
    // género
    @Column(name = "GENERO")
    @Size(max = 1)
    @InList({ "F", "M", "O" })
    private String genero;
    
    // país
    @Column(name = "PAIS")
    @Size(max = 100)
    private String pais;
    
    // nivel estudios terminados
    @Column(name = "ESTUDIOS")
    @Size(max = 150)
    private String estudios;
    
    // Estudios actuales (ZGZ16)
    @Column(name = "ESTUDIOS_ACTUALES")
    @Size(max = 150)
    private String estudiosActuales;
    
    // ocupación
    @Column(name = "OCUPACION")
    @Size(max = 150)
    private String ocupacion;
    
    // ¿vives independiente?
    @Column(name = "INDEPENDIENTE")
    @Size(max = 1)
    @InList({ "S", "N" })
    private String independiente;

    //  campo de texto libre ‘Necesidades especiales’.
    @Column(name = "NECESIDADES_ESPECIALES")
    @Size(max = 2000)
    private String necesidadesEspeciales;

    //  ‘Formato’
    @Column(name = "FORMATO")
    @Size(max = 150)
    private String formato;
    
    // Distrito de residencia. Está en Ciudadano
    // Preguntamos por "Barrio en el que vives"
    @Transient
    private String distrito;
    

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }   
   
    public Set<Tema> getTemas() {
        return temas;
    }

    public void setTemas(Set<Tema> temas) {
        this.temas = temas;
    }

    public Set<Valor> getValores() {
		return valores;
	}

	public void setValores(Set<Valor> valores) {
		this.valores = valores;
	}

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getPais() {
		return pais;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getEstudios() {
		return estudios;
	}

	public void setEstudios(String estudios) {
		this.estudios = estudios;
	}

	public String getEstudiosActuales() {
		return estudiosActuales;
	}

	public void setEstudiosActuales(String estudiosActuales) {
		this.estudiosActuales = estudiosActuales;
	}

	public String getOcupacion() {
		return ocupacion;
	}

	public void setOcupacion(String ocupacion) {
		this.ocupacion = ocupacion;
	}

	public String getFormato() {
		return formato;
	}

	public void setFormato(String formato) {
		this.formato = formato;
	}

	public String getIndependiente() {
		return independiente;
	}

	public void setIndependiente(String independiente) {
		this.independiente = independiente;
	}

	public String getNecesidadesEspeciales() {
		return necesidadesEspeciales;
	}

	public void setNecesidadesEspeciales(String necesidadesEspeciales) {
		this.necesidadesEspeciales = necesidadesEspeciales;
	}

	
	public List<BigDecimal> getTemaIds() {
		return temaIds;
	}

	public void setTemaIds(List<BigDecimal> temaIds) {
		this.temaIds = temaIds;
	}

	public List<BigDecimal> getValorIds() {
		return valorIds;
	}

	public void setValorIds(List<BigDecimal> valorIds) {
		this.valorIds = valorIds;
	}

	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}
	
    public String getTelefonoContacto() {
		return telefonoContacto;
	}

	public void setTelefonoContacto(String telefonoContacto) {
		this.telefonoContacto = telefonoContacto;
	}

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

	@Override
    public String toString() {
        return "Perfil [id=" + id + ", visible=" + visible + ", genero=" + genero + ", pais=" + pais
        		+ ", fechaNacimiento=" + fechaNacimiento +
        		", estudios=" + estudios + ", independiente=" + independiente + "]";
    }
}
