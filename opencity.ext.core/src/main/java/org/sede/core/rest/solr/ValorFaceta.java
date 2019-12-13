package org.sede.core.rest.solr;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="valor-faceta")
public class ValorFaceta {
	private String name;
	private Long count;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	@Override
	public String toString() {
		return "ValorFaceta [name=" + name + ", count=" + count + "]";
	}

	public String getNameParsed() {
		if ("true".equals(this.getName())) {
			return "Sí";
		} else if ("false".equals(this.getName())) {
			return "No";
		} else {
			return this.getName();
		}
	}
	
	
}
