package org.sede.core;

import java.util.ArrayList;
import java.util.List;

public class DataSourceConfiguration {
	private String eschema;
	private String jndi;
	private List<String> classes = new ArrayList<>();
	public String getEschema() {
		return eschema;
	}
	public void setEschema(String eschema) {
		this.eschema = eschema;
	}
	public String getJndi() {
		return jndi;
	}
	public void setJndi(String jndi) {
		this.jndi = jndi;
	}
	public List<String> getClasses() {
		return classes;
	}
	public void setClasses(List<String> classes) {
		this.classes = classes;
	}
	
	
}
