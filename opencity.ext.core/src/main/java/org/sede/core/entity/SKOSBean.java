package org.sede.core.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.utils.Funciones;

@Entity
@XmlRootElement(name = "skos")
@Rdf(contexto= Context.SKOS, propiedad="Concept")
//added by fpriyatna in 2016-06
public class SKOSBean implements Serializable {
	@Rdf(contexto = Context.SKOS, propiedad = "inScheme")
	String scheme;

	@Rdf(contexto = Context.DCT, propiedad = "identifier")
	String uri;

	@Rdf(contexto = Context.SKOS, propiedad = "notation")
	String notation;

	@Rdf(contexto = Context.SKOS, propiedad = "prefLabel")
	String prefLabel;

	public String getScheme() {
		return scheme;
	}

	public void setScheme(String scheme) {
		this.scheme = scheme;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getNotation() {
		return notation;
	}

	public void setNotation(String notation) {
		this.notation = notation;
	}

	public String getPrefLabel() {
		return prefLabel;
	}

	public void setPrefLabel(String prefLabel) {
		this.prefLabel = prefLabel;
	}

	public SKOSBean(String conceptName, String scheme, boolean encodeConceptName) {
		this.scheme = scheme;
		this.uri = SKOSBean.generateURI(conceptName, scheme, encodeConceptName);
		this.notation = conceptName;
		this.prefLabel = conceptName + "@es";
	}

	public SKOSBean(String uri, String conceptName, String scheme) {
		this.scheme = scheme;
		this.uri = uri;
		this.notation = conceptName;
		this.prefLabel = conceptName + "@es";
	}
	
	@Override
	public String toString() {
		return "SKOSBean [uri=" + uri + "\n, notation=" + notation
				+ "\n, prefLabel=" + prefLabel + "\n, scheme=" + scheme + "]";
	}

	public static String generateURI(String conceptName, String namespace,
			boolean encodeConceptName) {
		// 1. Normalize accented characters
		// 2. Replace spaces with underscore
		// 3. Convert to lowercase
		if (conceptName == null) {
			return null;
		} else {
			String conceptURI = conceptName;
			try {
				if (encodeConceptName) {
					conceptURI = Funciones.normalizar(conceptName);
				}
			} catch (Exception e) {
				;
			}
			conceptURI = conceptURI.replaceAll(" ", "_").toLowerCase();
			return namespace + conceptURI;
		}
	}

}
