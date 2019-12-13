package org.sede.core.entity;

import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.RdfMultiple;
import org.sede.core.dao.EntidadBase;

@XmlRootElement(name="tel")
@Rdf(contexto = Context.VCARD, propiedad = "tel")
public class Tel extends EntidadBase {

	@RdfMultiple({@Rdf(contexto = Context.VCARD, propiedad = "Tel"), @Rdf(contexto = Context.SCHEMA, propiedad = "telephone")})
	private String tel;
	@Rdf(contexto = Context.VCARD, propiedad = "Fax")
	private String fax;
	@Rdf(contexto = Context.VCARD, propiedad = "Cell")
	private String cell;
	
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getCell() {
		return cell;
	}
	public void setCell(String cell) {
		this.cell = cell;
	}
	@Override
	public String toString() {
		return "Tel [tel=" + tel + ", fax=" + fax + ", cell=" + cell + "]";
	}
}