package org.sede.servicio.acceso.entity;

import java.io.Serializable;

/**
 * The Class Grupo.
 * 
 * @autor Ayuntamiento de Zaragoza
 * 
 */
public class Grupo implements Serializable {
	
	/** Constant serialVersionUID. */
	private static final long serialVersionUID = 33762736470924742L;
	
	/** id. */
	private String id;
	
	/** title. */
	private String title;
	
	/** description. */
	private String description;
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the id.
	 *
	 * @param id the new id
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Gets the description.
	 *
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets the description.
	 *
	 * @param description the new description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Instantiates a new grupo.
	 *
	 * @param id Id
	 * @param title Title
	 * @param description Description
	 */
	public Grupo(String id, String title, String description) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
	}

	/**
	 * To string.
	 *
	 * @return string
	 */
	@Override
	public String toString() {
		return "Grupo [id=" + id + ", title=" + title + ", description="
				+ description + "]";
	}
	
	
	
}
