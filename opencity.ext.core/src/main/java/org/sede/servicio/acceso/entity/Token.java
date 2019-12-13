package org.sede.servicio.acceso.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "token")

public class Token {
	private String access;
	private String verifier;
	private Date expiration_date;
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getVerifier() {
		return verifier;
	}
	public void setVerifier(String verifier) {
		this.verifier = verifier;
	}
	public Date getExpiration_date() {
		return expiration_date;
	}
	public void setExpiration_date(Date expiration_date) {
		this.expiration_date = expiration_date;
	}
	@Override
	public String toString() {
		return "Token [access=" + access + ", verifier=" + verifier
				+ ", expiration_date=" + expiration_date + "]";
	}
	
	
}
