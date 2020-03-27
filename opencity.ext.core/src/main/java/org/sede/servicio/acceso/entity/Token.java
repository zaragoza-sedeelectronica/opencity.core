package org.sede.servicio.acceso.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Token.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
@XmlRootElement(name = "token")

public class Token {
	
	/** access. */
	private String access;
	
	/** verifier. */
	private String verifier;
	
	/** expiration date. */
	private Date expiration_date;
	
	/**
	 * Gets the access.
	 *
	 * @return the access
	 */
	public String getAccess() {
		return access;
	}
	
	/**
	 * Sets the access.
	 *
	 * @param access the new access
	 */
	public void setAccess(String access) {
		this.access = access;
	}
	
	/**
	 * Gets the verifier.
	 *
	 * @return the verifier
	 */
	public String getVerifier() {
		return verifier;
	}
	
	/**
	 * Sets the verifier.
	 *
	 * @param verifier the new verifier
	 */
	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}
	
	/**
	 * Gets the expiration date.
	 *
	 * @return the expiration date
	 */
	public Date getExpiration_date() {
		return expiration_date;
	}
	
	/**
	 * Sets the expiration date.
	 *
	 * @param expiration_date the new expiration date
	 */
	public void setExpiration_date(Date expiration_date) {
		this.expiration_date = expiration_date;
	}
	
	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "Token [access=" + access + ", verifier=" + verifier
				+ ", expiration_date=" + expiration_date + "]";
	}
	
	
}
