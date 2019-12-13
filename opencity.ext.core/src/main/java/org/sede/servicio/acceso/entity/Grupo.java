package org.sede.servicio.acceso.entity;

import java.io.Serializable;

public class Grupo implements Serializable {
	private String id;
	private String title;
	private String description;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public Grupo(String id, String title, String description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
	}

	@Override
	public String toString() {
		return "Grupo [id=" + id + ", title=" + title + ", description="
				+ description + "]";
	}
	
	
	
}
