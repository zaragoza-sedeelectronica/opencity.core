package org.sede.servicio.acceso.entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.persistence.Transient;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.codec.CharEncoding;
import org.sede.core.utils.Funciones;

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
    
    public byte[] getBytes() throws UnsupportedEncodingException {
    	String body = Funciones.removeB64Prefix(URLDecoder.decode(getMedia_body(), CharEncoding.UTF_8));
		return DatatypeConverter.parseBase64Binary(body);
		
    }
    
    public InputStream getInputStream() throws UnsupportedEncodingException {
    	return new ByteArrayInputStream(getBytes());
    }

    @Override
    public String toString() {
        return "MediaFile [media_name=" + media_name
                + ", media_description=" + media_description
                + ", media_body=" + media_body + ", media_type="
                + media_type + "]";
    }
}