package org.sede.core.geo;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.GeoJson;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.Context;


@GeoJson(title = "", description = "", icon = "", link = "")
@XmlRootElement(name = "item")
@Rdf
public class Item {
	private String link;
	private String about;
	private String title;
	private String description;
	private String pubDate;
	private String icon;
	private String category;
	private String author;
	private String duration;
	@Rdf(contexto = Context.GEO, propiedad = "geometry")
	private Geometria geometry;
	private String lugar;
	private String priority;
	private Map<String, Object> propiedades;
	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getAbout() {
		return about;
	}
	public void setAbout(String about) {
		this.about = about;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPubDate() {
		return pubDate;
	}
	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String imagen) {
		this.icon = imagen;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public Geometria getGeometry() {
		return geometry;
	}
	public void setGeometry(Geometria geometry) {
		this.geometry = geometry;
	}
	public String getLugar() {
		return lugar;
	}
	public void setLugar(String lugar) {
		this.lugar = lugar;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public Map<String, Object> getPropiedades() {
		return propiedades;
	}
	public void setPropiedades(Map<String, Object> propiedades) {
		this.propiedades = propiedades;
	}
	
	public void setGeo(String geo) {
		String[] dat = geo.split(" ");
		this.geometry = new Punto(Punto.POINT, new Double[]{Double.parseDouble(dat[1]),Double.parseDouble(dat[0])});
	}
	public void setImagen(String dato) {
		this.icon = dato;
	}
	@Override
	public String toString() {
		return "Item [link=" + link + ", about=" + about + ", title=" + title
				+ ", description=" + description + ", pubDate=" + pubDate
				+ ", icon=" + icon + ", category=" + category + ", author="
				+ author + ", duration=" + duration + ", geometry=" + geometry
				+ ", lugar=" + lugar + ", priority=" + priority
				+ ", propiedades=" + propiedades + "]";
	}
	
}
