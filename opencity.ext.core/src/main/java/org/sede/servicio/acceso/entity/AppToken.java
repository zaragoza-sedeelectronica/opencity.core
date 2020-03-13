package org.sede.servicio.acceso.entity;

import javax.xml.bind.annotation.XmlRootElement;

// TODO: Auto-generated Javadoc
/**
 * The Class AppToken.
 * 
 * @autor Ayuntamiento de Zaragoza
 */
@XmlRootElement(name = "app-token")

public class AppToken {
	
	/** platform. */
	private String platform;
	
	/** token. */
	private String token;
	
	/**
	 * Gets the platform.
	 *
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * Sets the platform.
	 *
	 * @param platform the new platform
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}

	/**
	 * Gets the token.
	 *
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * Sets the token.
	 *
	 * @param token the new token
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "AppToken [platform=" + platform + ", token=" + token + "]";
	}
	
	
}
