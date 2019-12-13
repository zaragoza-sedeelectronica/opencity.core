package org.sede.core.rest;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "allowable-values")
public class AllowableValues {
	private String valueType = "LIST";
	private String[] values;
	public String getValueType() {
		return valueType;
	}
	public void setValueType(String valueType) {
		this.valueType = valueType;
	}
	public String[] getValues() {
		return values;
	}
	public void setValues(String[] values) {
		this.values = values;
	}
	
	
	
}