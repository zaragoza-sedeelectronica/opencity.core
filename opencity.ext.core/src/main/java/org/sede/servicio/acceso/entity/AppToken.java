package org.sede.servicio.acceso.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "app-token")

public class AppToken {
	
	private String platform;
	private String token;
	
	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String toString() {
		return "AppToken [platform=" + platform + ", token=" + token + "]";
	}
	
	
}
