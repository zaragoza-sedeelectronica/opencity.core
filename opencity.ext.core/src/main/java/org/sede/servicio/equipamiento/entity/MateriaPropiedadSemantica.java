package org.sede.servicio.equipamiento.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.Context;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RequiredSinValidacion;
import org.sede.core.dao.EntidadBase;

@XmlRootElement(name = "materia-propiedad")
@Entity(name="MateriaPropiedadSemantica")
@Table(name = "CENTRO_PROPIEDAD_TEMA", schema = "INTRA")
//@PathId("/recurso/dataset/materia-inspire/")
@Rdf(contexto = Context.DCAT, propiedad = "theme")
public class MateriaPropiedadSemantica extends EntidadBase {
    @Id
    @Column(name = "ID_tema", unique = true, nullable = false)  @Digits(integer = 22, fraction = 0)
    @Rdf(contexto = Context.DCT, propiedad = "identifier")
    private BigDecimal id;
    @Column(name = "NOMBRE") @Size(max = 100)
    @Rdf(contexto = Context.DCT, propiedad = "title")
    @RequiredSinValidacion
    private String title;
    @Column(name = "TYPE") @Size(max = 100)
    @RequiredSinValidacion
    private String type;

    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal idMateria) {
        this.id = idMateria;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String nombre) {
        this.title = nombre;
    }

    @Override
    public String toString() {
        return "Materia [id=" + id + ", title=" + title + ", type=" + type +"]";
    }


}
