package org.sede.servicio.acceso.entity;

import javax.persistence.Transient;

public class MediaFile {

    @Transient
    private String media_name;
    @Transient
    private String media_description;
    @Transient
    private String media_body;
    @Transient
    private String media_type;

    public MediaFile() {
        super();
    }

    public String getMedia_name() {
        return media_name;
    }

    public void setMedia_name(String media_name) {
        this.media_name = media_name;
    }

    public String getMedia_description() {
        return media_description;
    }

    public void setMedia_description(String media_description) {
        this.media_description = media_description;
    }

    public String getMedia_body() {
        return media_body;
    }

    public void setMedia_body(String media_body) {
        this.media_body = media_body;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    @Override
    public String toString() {
        return "MediaFile [media_name=" + media_name
                + ", media_description=" + media_description
                + ", media_body=" + media_body + ", media_type="
                + media_type + "]";
    }
}