
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
import org.sede.servicio.acceso.dao.LiderListener;

@XmlRootElement(name = "lider")
@Entity(name = "Lider")
@DynamicUpdate
@SequenceGenerator(name = "SECUENCIA", sequenceName = "NOTICIAS.SEQ_LIDERES", allocationSize = 1)
@Table(name = "LIDERES", schema = "NOTICIAS")
@EntityListeners(LiderListener.class)
public class Lider extends EntidadBase {

    /*
     *  Una tabla que relacione el objeto (id_asociado, tipo_asociado) con el usuario responsable
     *  (id_responsable, tipo_usuario). Para discriminar el tipo del objeto se puede utilizar el nombre de la clase
     *  Java, para luego poder extraerlo mediante su DAO y su id.
     */    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SECUENCIA")
    @Column(name = "ID_LIDER", unique = true, nullable = false)
    @Digits(integer = 22, fraction = 0)
    private BigDecimal id;

    // identificador de la entidad asociada (grupo gcz, equipamiento, programa,etc..)
    @Column(name = "ID_ASOCIADO")
    @Digits(integer = 22, fraction = 0)
    private BigDecimal associatedId;

    // clase entidad grupo gcz, equipamiento, programa etc..
    @Column(name = "TIPO_ASOCIADO")
    @Size(max = 300)
    private String associatedType;

    // identificador del usuario líder
    @Column(name = "ID_USUARIO")
    @Digits(integer = 22, fraction = 0)
    private BigDecimal userId;
    
    // clase del tipo de usuario (gcz, ciudadano...)
    @Column(name = "TIPO_USUARIO")
    @Size(max = 300)
    private String userType;

    // tipo de usuario líder
    private transient Object userResource;
    
    // recurso sobre el que el usuario es líder
    private transient Object associatedResource;
    

    public BigDecimal getId() {
		return id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	public BigDecimal getAssociatedId() {
		return associatedId;
	}

	public void setAssociatedId(BigDecimal associatedId) {
		this.associatedId = associatedId;
	}

	public String getAssociatedType() {
		return associatedType;
	}

	public void setAssociatedType(String associatedType) {
		this.associatedType = associatedType;
	}

	public BigDecimal getUserId() {
		return userId;
	}

	public void setUserId(BigDecimal userId) {
		this.userId = userId;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public Object getUserResource() {
		return userResource;
	}

	public void setUserResource(Object userResource) {
		this.userResource = userResource;
	}

	public Object getAssociatedResource() {
		return associatedResource;
	}

	public void setAssociatedResource(Object associatedResource) {
		this.associatedResource = associatedResource;
	}

	@Override
    public String toString() {
        return "Lider [associatedId=" + associatedId + ", associatedType=" + associatedType +
        		", user_id=" + userId + ", userType=" + userType+ "]";
    }

	public Lider(String associatedType, BigDecimal associatedId, String userType, BigDecimal userId) {
		super();
		this.associatedId = associatedId;
		this.associatedType = associatedType;
		this.userId = userId;
		this.userType = userType;
	}
	public Lider() {
		super();
	}
}
