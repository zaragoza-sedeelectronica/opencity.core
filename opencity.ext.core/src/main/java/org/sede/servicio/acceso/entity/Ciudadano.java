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

@XmlRootElement(name = "user")
@Entity
@DynamicUpdate
@Table(name = "USERS", schema = ConfigCiudadano.ESQUEMA)
@XmlAccessorType(XmlAccessType.FIELD)
@SequenceGenerator(name = "SECUENCIA_SEQ_USERS", sequenceName = "SEQ_USERS", allocationSize = 1)
public class Ciudadano extends EntidadBase {

    //TODO: Sustituimos @Interno por Permisos.DET para poder acceder a los datos de un ciudadano desde inscripciones
    /*@Interno */
    @Id
    @Permisos(Permisos.DET)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA_SEQ_USERS")
    @Column(name = "ID", nullable = false, unique = true)
    @Digits(integer = 22, fraction = 0)
    private Integer id;

    @Column(name = "PERSON_NAME")
    @Size(max = 256)
    private String name;

    @Column(name = "EMAIL", updatable = false)
    @Email
    @Size(max = 128)
    private String email;

    @Column(name = "MOBILE", updatable = false)
    @Size(max = 128)
    private String mobile;

    @Interno
    @Column(name = "PASSWORD", updatable = false)
    @Size(max = 32)
    private String password;

    @Interno
    @Column(name = "IS_ADMIN", updatable = false)
    @Size(max = 1)
    private String isAdmin;

    @Column(name = "GUID", updatable = false)
    @Size(max = 256)
    private String account_id;

    @Interno
    @Column(name = "IS_PERFIL", updatable = false)
    @Size(max = 1)
    private String isPerfil;

    @Interno
    @Column(name = "IDFACEBOOK", updatable = false)
    @Size(max = 100)
    private String id_facebook;

    @Interno
    @Column(name = "IDTWITTER", updatable = false)
    @Digits(integer = 22, fraction = 0)
    private String id_twitter;
    @Column(name = "SCREENNAME")

    @Size(max = 200)
    private String screen_name;

    @Column(name = "IDTARJETACIUDADANA", updatable = false)
    @Size(max = 200)
    private String tarjetaCiudadana;

    @Column(name = "NIF", updatable = false)
    @Size(max = 200)
    private String nif;

    @Column(name = "ANIO_NACIMIENTO", updatable = false)
    private Integer birthYear;

    @Column(name = "ID_DISTRITO", updatable = false)
    private Integer district;

    @Column(name = "ID_SECCION", updatable = false)
    private Integer section;

    @Column(name = "JUNTA", updatable = false)
    @Size(max = 200)
    private String junta;

    @Column(name = "JUNTA_USER", updatable = false)
    @Size(max = 200)
    private String juntaUser;

    @Column(name = "EMPADRONADO", updatable = false)
    @Size(max = 200)
    private String empadronado;

    @Column(name = "FECHA_NACIMIENTO", updatable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha_nacimiento;

    @Interno
    @Column(name = "ACTIVADO", updatable = false)
    @Digits(integer = 22, fraction = 0)
    private BigDecimal activado;

    @Interno
    @Column(name = "FECHA_ALTA", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "IMAGE", updatable = false)
    @Size(max = 200)
    private String image;

	@Permisos(Permisos.DET)
    @Column(name = "ACEPTAMAIL", updatable = false)
    @Convert(converter = BooleanConverter.class)
    private Boolean aceptaMail;
	
    @Column(name = "DOCUMENTO_IDENTIFICATIVO", updatable = false)
    @Size(max = 200)
    private String documentoIdentificativo;

	@ManyToOne(fetch = FetchType.EAGER)
	@NotFound(action=NotFoundAction.IGNORE)
    @JoinColumn(name = "ID", referencedColumnName = "ID_USUARIO_ADENTRA", insertable = false, updatable = false)
    private Perfil perfil;
	
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
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getIsPerfil() {
        return isPerfil;
    }

    public void setIsPerfil(String isPerfil) {
        this.isPerfil = isPerfil;
    }

    public String getId_facebook() {
        return id_facebook;
    }

    public void setId_facebook(String id_facebook) {
        this.id_facebook = id_facebook;
    }

    public String getId_twitter() {
        return id_twitter;
    }

    public void setId_twitter(String id_twitter) {
        this.id_twitter = id_twitter;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Date getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(Date fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public BigDecimal getActivado() {
        return activado;
    }

    public void setActivado(BigDecimal activado) {
        this.activado = activado;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getTarjetaCiudadana() {
        return tarjetaCiudadana;
    }

    public void setTarjetaCiudadana(String tarjetaCiudadana) {
        this.tarjetaCiudadana = tarjetaCiudadana;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public String getEmpadronado() {
        return empadronado;
    }

    public void setEmpadronado(String empadronado) {
        this.empadronado = empadronado;
    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Integer getSection() {
        return section;
    }

    public void setSection(Integer section) {
        this.section = section;
    }

    public String getJunta() {
        return junta;
    }

    public void setJunta(String junta) {
        this.junta = junta;
    }

    public String getJuntaUser() {
        return juntaUser;
    }

    public void setJuntaUser(String juntaUser) {
        this.juntaUser = juntaUser;
    }

    public String getImage() {
        return image == null ? null : CiudadanoGenericDAOImpl.IMG_HTTP_PATH + image;
    }

    public String getImageName() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getAceptaMail() {
        return aceptaMail;
    }

    public void setAceptaMail(Boolean aceptaMail) {
        this.aceptaMail = aceptaMail;
    }

    public String getDocumentoIdentificativo() {
		return documentoIdentificativo;
	}

	public void setDocumentoIdentificativo(String documentoIdentificativo) {
		this.documentoIdentificativo = documentoIdentificativo;
	}

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

    
	public Perfil getPerfil() {
		return perfil == null ? new Perfil() : perfil;
	}
	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}
	public String getJuntaActual() {
//		return StringUtils.isEmpty(this.juntaUser) ? this.junta : this.juntaUser;
        return this.junta;
    }

    @Transient
    private String soloNombreJunta;

    public String getSoloNombreJunta() {
        return getJuntaActual() == null ? getJuntaActual() : getJuntaActual().replace("Junta", "").replace("JV de ", "").trim();
    }

}
