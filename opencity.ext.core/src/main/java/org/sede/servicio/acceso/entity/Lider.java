
package org.sede.servicio.acceso.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.sede.core.dao.EntidadBase;
import org.sede.servicio.acceso.ConfigCiudadano;
import org.sede.servicio.acceso.dao.LiderListener;

// TODO: Auto-generated Javadoc
/**
 * The Class Lider.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
@XmlRootElement(name = "lider")
@Entity(name = "Lider")
@DynamicUpdate
@SequenceGenerator(name = "SECUENCIA", sequenceName = "GENERAL.SEQ_LIDERES", allocationSize = 1)
@Table(name = "LIDERES", schema = ConfigCiudadano.ESQUEMA)
@EntityListeners(LiderListener.class)
public class Lider extends EntidadBase {

    /*
     *  Una tabla que relacione el objeto (id_asociado, tipo_asociado) con el usuario responsable
     *  (id_responsable, tipo_usuario). Para discriminar el tipo del objeto se puede utilizar el nombre de la clase
     *  Java, para luego poder extraerlo mediante su DAO y su id.
     */    
    
    /** Constant serialVersionUID. */
	private static final long serialVersionUID = -6493446799144351128L;

	/** id. */
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA")
    @Column(name = "ID_LIDER", unique = true, nullable = false)
    @Digits(integer = 22, fraction = 0)
    private BigDecimal id;

    /** associated id. */
    // identificador de la entidad asociada (grupo gcz, equipamiento, programa,etc..)
    @Column(name = "ID_ASOCIADO")
    @Digits(integer = 22, fraction = 0)
    private BigDecimal associatedId;

    /** associated type. */
    // clase entidad grupo gcz, equipamiento, programa etc..
    @Column(name = "TIPO_ASOCIADO")
    @Size(max = 300)
    private String associatedType;

    /** user id. */
    // identificador del usuario líder
    @Column(name = "ID_USUARIO")
    @Digits(integer = 22, fraction = 0)
    private BigDecimal userId;
    
    /** user type. */
    // clase del tipo de usuario (gcz, ciudadano...)
    @Column(name = "TIPO_USUARIO")
    @Size(max = 300)
    private String userType;

    /** user resource. */
    // tipo de usuario líder
    private transient Object userResource;
    
    /** associated resource. */
    // recurso sobre el que el usuario es líder
    private transient Object associatedResource;
    

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
	 * Gets the associated id.
	 *
	 * @return the associated id
	 */
	public BigDecimal getAssociatedId() {
		return associatedId;
	}

	/**
	 * Sets the associated id.
	 *
	 * @param associatedId the new associated id
	 */
	public void setAssociatedId(BigDecimal associatedId) {
		this.associatedId = associatedId;
	}

	/**
	 * Gets the associated type.
	 *
	 * @return the associated type
	 */
	public String getAssociatedType() {
		return associatedType;
	}

	/**
	 * Sets the associated type.
	 *
	 * @param associatedType the new associated type
	 */
	public void setAssociatedType(String associatedType) {
		this.associatedType = associatedType;
	}

	/**
	 * Gets the user id.
	 *
	 * @return the user id
	 */
	public BigDecimal getUserId() {
		return userId;
	}

	/**
	 * Sets the user id.
	 *
	 * @param userId the new user id
	 */
	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}

	/**
	 * Gets the user type.
	 *
	 * @return the user type
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * Sets the user type.
	 *
	 * @param userType the new user type
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * Gets the user resource.
	 *
	 * @return the user resource
	 */
	public Object getUserResource() {
		return userResource;
	}

	/**
	 * Sets the user resource.
	 *
	 * @param userResource the new user resource
	 */
	public void setUserResource(Object userResource) {
		this.userResource = userResource;
	}

	/**
	 * Gets the associated resource.
	 *
	 * @return the associated resource
	 */
	public Object getAssociatedResource() {
		return associatedResource;
	}

	/**
	 * Sets the associated resource.
	 *
	 * @param associatedResource the new associated resource
	 */
	public void setAssociatedResource(Object associatedResource) {
		this.associatedResource = associatedResource;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
    public String toString() {
        return "Lider [associatedId=" + associatedId + ", associatedType=" + associatedType +
        		", user_id=" + userId + ", userType=" + userType+ "]";
    }

	/**
	 * Instantiates a new lider.
	 *
	 * @param associatedType Associated type
	 * @param associatedId Associated id
	 * @param userType User type
	 * @param userId User id
	 */
	public Lider(String associatedType, BigDecimal associatedId, String userType, BigDecimal userId) {
		super();
		this.associatedId = associatedId;
		this.associatedType = associatedType;
		this.userId = userId;
		this.userType = userType;
	}
	
	/**
	 * Instantiates a new lider.
	 */
	public Lider() {
		super();
	}
}
