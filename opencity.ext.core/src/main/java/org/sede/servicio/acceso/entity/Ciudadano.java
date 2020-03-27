package org.sede.servicio.acceso.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.validator.constraints.Email;
import org.sede.core.anotaciones.Interno;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.dao.BooleanConverter;
import org.sede.core.dao.EntidadBase;
import org.sede.servicio.acceso.ConfigCiudadano;
import org.sede.servicio.acceso.dao.CiudadanoGenericDAOImpl;

/**
 * The Class Ciudadano.
 * 
 * @autor Ayuntamiento de Zaragoza
 */
@XmlRootElement(name = "user")
@Entity
@DynamicUpdate
@Table(name = "USERS", schema = ConfigCiudadano.ESQUEMA)
@XmlAccessorType(XmlAccessType.FIELD)
@SequenceGenerator(name = "SECUENCIA_SEQ_USERS", sequenceName = "SEQ_USERS", allocationSize = 1)
public class Ciudadano extends EntidadBase {

    /** Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	//TODO: Sustituimos @Interno por Permisos.DET para poder acceder a los datos de un ciudadano desde inscripciones
    /** id. */
    /*@Interno */
    @Id
    @Permisos(Permisos.DET)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA_SEQ_USERS")
    @Column(name = "ID", nullable = false, unique = true)
    @Digits(integer = 22, fraction = 0)
    private Integer id;

    /** name. */
    @Column(name = "PERSON_NAME")
    @Size(max = 256)
    private String name;

    /** email. */
    @Column(name = "EMAIL", updatable = false)
    @Email
    @Size(max = 128)
    private String email;

    /** mobile. */
    @Column(name = "MOBILE", updatable = false)
    @Size(max = 128)
    private String mobile;

    /** password. */
    @Interno
    @Column(name = "PASSWORD", updatable = false)
    @Size(max = 32)
    private String password;

    /** is admin. */
    @Interno
    @Column(name = "IS_ADMIN", updatable = false)
    @Size(max = 1)
    private String isAdmin;

    /** account id. */
    @Column(name = "GUID", updatable = false)
    @Size(max = 256)
    private String account_id;

    /** is perfil. */
    @Interno
    @Column(name = "IS_PERFIL", updatable = false)
    @Size(max = 1)
    private String isPerfil;

    /** id facebook. */
    @Interno
    @Column(name = "IDFACEBOOK", updatable = false)
    @Size(max = 100)
    private String id_facebook;

    /** id twitter. */
    @Interno
    @Column(name = "IDTWITTER", updatable = false)
    @Digits(integer = 22, fraction = 0)
    private String id_twitter;
    
    /** screen name. */
    @Column(name = "SCREENNAME")

    @Size(max = 200)
    private String screen_name;

    /** tarjeta ciudadana. */
    @Column(name = "IDTARJETACIUDADANA", updatable = false)
    @Size(max = 200)
    private String tarjetaCiudadana;

    /** nif. */
    @Column(name = "NIF", updatable = false)
    @Size(max = 200)
    private String nif;

    /** birth year. */
    @Column(name = "ANIO_NACIMIENTO", updatable = false)
    private Integer birthYear;

    /** district. */
    @Column(name = "ID_DISTRITO", updatable = false)
    private Integer district;

    /** section. */
    @Column(name = "ID_SECCION", updatable = false)
    private Integer section;

    /** junta. */
    @Column(name = "JUNTA", updatable = false)
    @Size(max = 200)
    private String junta;

    /** junta user. */
    @Column(name = "JUNTA_USER", updatable = false)
    @Size(max = 200)
    private String juntaUser;

    /** empadronado. */
    @Column(name = "EMPADRONADO", updatable = false)
    @Size(max = 200)
    private String empadronado;

    /** fecha nacimiento. */
    @Column(name = "FECHA_NACIMIENTO", updatable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha_nacimiento;

    /** activado. */
    @Interno
    @Column(name = "ACTIVADO", updatable = false)
    @Digits(integer = 22, fraction = 0)
    private BigDecimal activado;

    /** creation date. */
    @Interno
    @Column(name = "FECHA_ALTA", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    /** image. */
    @Column(name = "IMAGE", updatable = false)
    @Size(max = 200)
    private String image;

	/** acepta mail. */
	@Permisos(Permisos.DET)
    @Column(name = "ACEPTAMAIL", updatable = false)
    @Convert(converter = BooleanConverter.class)
    private Boolean aceptaMail;
	
    /** documento identificativo. */
    @Column(name = "DOCUMENTO_IDENTIFICATIVO", updatable = false)
    @Size(max = 200)
    private String documentoIdentificativo;

	/** perfil. */
	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name = "ID", referencedColumnName = "ID_USUARIO_ADENTRA", insertable = false, updatable = false)
    private Perfil perfil;
	
	/**
	 * Gets the visible.
	 *
	 * @return the visible
	 */
	@Transient
	public boolean getVisible() {
		if (perfil == null) {
			return true;
		}
		
		String visible = perfil.getVisible();
		if (visible == null) {
			return true;
		}
		else {
			return "S".equalsIgnoreCase(visible);
		}
	}
	
    /**
     * Gets the id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets the id.
     *
     * @param id the new id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email.
     *
     * @param email the new email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the mobile.
     *
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Sets the mobile.
     *
     * @param mobile the new mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * Gets the password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the password.
     *
     * @param password the new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets the checks if is admin.
     *
     * @return the checks if is admin
     */
    public String getIsAdmin() {
        return isAdmin;
    }

    /**
     * Sets the checks if is admin.
     *
     * @param isAdmin the new checks if is admin
     */
    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Gets the account id.
     *
     * @return the account id
     */
    public String getAccount_id() {
        return account_id;
    }

    /**
     * Sets the account id.
     *
     * @param account_id the new account id
     */
    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    /**
     * Gets the checks if is perfil.
     *
     * @return the checks if is perfil
     */
    public String getIsPerfil() {
        return isPerfil;
    }

    /**
     * Sets the checks if is perfil.
     *
     * @param isPerfil the new checks if is perfil
     */
    public void setIsPerfil(String isPerfil) {
        this.isPerfil = isPerfil;
    }

    /**
     * Gets the id facebook.
     *
     * @return the id facebook
     */
    public String getId_facebook() {
        return id_facebook;
    }

    /**
     * Sets the id facebook.
     *
     * @param id_facebook the new id facebook
     */
    public void setId_facebook(String id_facebook) {
        this.id_facebook = id_facebook;
    }

    /**
     * Gets the id twitter.
     *
     * @return the id twitter
     */
    public String getId_twitter() {
        return id_twitter;
    }

    /**
     * Sets the id twitter.
     *
     * @param id_twitter the new id twitter
     */
    public void setId_twitter(String id_twitter) {
        this.id_twitter = id_twitter;
    }

    /**
     * Gets the screen name.
     *
     * @return the screen name
     */
    public String getScreen_name() {
        return screen_name;
    }

    /**
     * Sets the screen name.
     *
     * @param screen_name the new screen name
     */
    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    /**
     * Gets the nif.
     *
     * @return the nif
     */
    public String getNif() {
        return nif;
    }

    /**
     * Sets the nif.
     *
     * @param nif the new nif
     */
    public void setNif(String nif) {
        this.nif = nif;
    }

    /**
     * Gets the fecha nacimiento.
     *
     * @return the fecha nacimiento
     */
    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    /**
     * Sets the fecha nacimiento.
     *
     * @param fecha_nacimiento the new fecha nacimiento
     */
    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    /**
     * Gets the activado.
     *
     * @return the activado
     */
    public BigDecimal getActivado() {
        return activado;
    }

    /**
     * Sets the activado.
     *
     * @param activado the new activado
     */
    public void setActivado(BigDecimal activado) {
        this.activado = activado;
    }

    /**
     * Gets the creation date.
     *
     * @return the creation date
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * Sets the creation date.
     *
     * @param creationDate the new creation date
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * Gets the tarjeta ciudadana.
     *
     * @return the tarjeta ciudadana
     */
    public String getTarjetaCiudadana() {
        return tarjetaCiudadana;
    }

    /**
     * Sets the tarjeta ciudadana.
     *
     * @param tarjetaCiudadana the new tarjeta ciudadana
     */
    public void setTarjetaCiudadana(String tarjetaCiudadana) {
        this.tarjetaCiudadana = tarjetaCiudadana;
    }

    /**
     * Gets the birth year.
     *
     * @return the birth year
     */
    public Integer getBirthYear() {
        return birthYear;
    }

    /**
     * Sets the birth year.
     *
     * @param birthYear the new birth year
     */
    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    /**
     * Gets the empadronado.
     *
     * @return the empadronado
     */
    public String getEmpadronado() {
        return empadronado;
    }

    /**
     * Sets the empadronado.
     *
     * @param empadronado the new empadronado
     */
    public void setEmpadronado(String empadronado) {
        this.empadronado = empadronado;
    }

    /**
     * Gets the district.
     *
     * @return the district
     */
    public Integer getDistrict() {
        return district;
    }

    /**
     * Sets the district.
     *
     * @param district the new district
     */
    public void setDistrict(Integer district) {
        this.district = district;
    }

    /**
     * Gets the section.
     *
     * @return the section
     */
    public Integer getSection() {
        return section;
    }

    /**
     * Sets the section.
     *
     * @param section the new section
     */
    public void setSection(Integer section) {
        this.section = section;
    }

    /**
     * Gets the junta.
     *
     * @return the junta
     */
    public String getJunta() {
        return junta;
    }

    /**
     * Sets the junta.
     *
     * @param junta the new junta
     */
    public void setJunta(String junta) {
        this.junta = junta;
    }

    /**
     * Gets the junta user.
     *
     * @return the junta user
     */
    public String getJuntaUser() {
        return juntaUser;
    }

    /**
     * Sets the junta user.
     *
     * @param juntaUser the new junta user
     */
    public void setJuntaUser(String juntaUser) {
        this.juntaUser = juntaUser;
    }

    /**
     * Gets the image.
     *
     * @return the image
     */
    public String getImage() {
        return image == null ? null : CiudadanoGenericDAOImpl.IMG_HTTP_PATH + image;
    }

    /**
     * Gets the image name.
     *
     * @return the image name
     */
    public String getImageName() {
        return image;
    }

    /**
     * Sets the image.
     *
     * @param image the new image
     */
    public void setImage(String image) {
        this.image = image;
    }

    /**
     * Gets the acepta mail.
     *
     * @return the acepta mail
     */
    public Boolean getAceptaMail() {
        return aceptaMail;
    }

    /**
     * Sets the acepta mail.
     *
     * @param aceptaMail the new acepta mail
     */
    public void setAceptaMail(Boolean aceptaMail) {
        this.aceptaMail = aceptaMail;
    }

    /**
     * Gets the documento identificativo.
     *
     * @return the documento identificativo
     */
    public String getDocumentoIdentificativo() {
		return documentoIdentificativo;
	}

	/**
	 * Sets the documento identificativo.
	 *
	 * @param documentoIdentificativo the new documento identificativo
	 */
	public void setDocumentoIdentificativo(String documentoIdentificativo) {
		this.documentoIdentificativo = documentoIdentificativo;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
    public String toString() {
        return "UserAddentra [id=" + id + ", name=" + name + ", email=" + email
                + ", password=" + password + ", isAdmin=" + isAdmin
                + ", account_id=" + account_id + ", isPerfil=" + isPerfil
                + ", id_facebook=" + id_facebook + ", id_twitter=" + id_twitter
                + ", screen_name=" + screen_name + ", nif=" + nif
                + ", junta=" + junta
                + ", aceptaMail=" + aceptaMail
                + ", image=" + image
                + ", mobile=" + mobile
                + ", birthYear=" + birthYear + ", empadronado=" + empadronado
                + ", fecha_nacimiento=" + fecha_nacimiento + ", activado="
                + activado + ", creationDate=" + creationDate + "]";
    }

    
	/**
	 * Gets the perfil.
	 *
	 * @return the perfil
	 */
	public Perfil getPerfil() {
		return perfil == null ? new Perfil() : perfil;
	}
	
	/**
	 * Sets the perfil.
	 *
	 * @param perfil the new perfil
	 */
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	
	/**
	 * Gets the junta actual.
	 *
	 * @return the junta actual
	 */
	public String getJuntaActual() {
//		return StringUtils.isEmpty(this.juntaUser) ? this.junta : this.juntaUser;
        return this.junta;
    }

    /** solo nombre junta. */
    @Transient
    private String soloNombreJunta;

    /**
     * Gets the solo nombre junta.
     *
     * @return the solo nombre junta
     */
    public String getSoloNombreJunta() {
        return getJuntaActual() == null ? getJuntaActual() : getJuntaActual().replace("Junta", "").replace("JV de ", "").trim();
    }

}
