package org.sede.core.geo;

import java.util.ArrayList;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RdfMultiple;
import org.sede.core.anotaciones.Context;


@XmlRootElement(name="geometries")
@Entity
@DynamicUpdate
@XmlAccessorType(XmlAccessType.FIELD)
@org.sede.core.anotaciones.Rdf
public class GeometryCollection extends Geometria {
	
	
	@RdfMultiple({@Rdf(uri = "@id"), @Rdf(contexto = Context.RDF, propiedad = "type")})
	private String type = Geometria.GEOMETRYCOLLECTION;

	Geometria[] geometries;
	
	public GeometryCollection() {
		super();
	}
	public GeometryCollection(Geometria[] geometries) {
		super();
		this.geometries = geometries;
	}
	public GeometryCollection(String type, Geometria[] geometries) {
		super();
		this.type = type;
		this.geometries = geometries;
	}
	@Override
	public String getType() {
		return type;
	}
	@Override
	public void setType(String type) {
		this.type = type;
	}
	
	public Geometria[] getGeometries() {
		return geometries;
	}
	public void setGeometries(Geometria[] geometries) {
		this.geometries = geometries;
	}
	@Override
	public String toString() {
		return "Geometry [type=" + type + ", geometries=" + geometries + "]";
	}
	
	@Override
	public String asJson(String sourceSrs, String srsnameRequested) {
		StringBuilder retorno = new StringBuilder();
		retorno.append("{\"type\":\"GeometryCollection\",\"geometries\":[");
		conCaracterYComas(sourceSrs, srsnameRequested, retorno);
		retorno.append("]}");
		return retorno.toString();
	}
	private void conCaracterYComas(String sourceSrs, String srsnameRequested, StringBuilder retorno) {
		boolean primero = true;
		for (Geometria g : this.geometries) {
			if (!primero) {
				retorno.append(",");
			}
			primero = false;
			if (Geometria.POINT.equals(g.getType())) {
				if (g.getCoordinates() instanceof ArrayList) {
					ArrayList<?> a = (ArrayList<?>) g.getCoordinates();
					Punto p = new Punto(new Double[]{(Double)a.get(0), (Double)a.get(1)});
					retorno.append(p.asJson(sourceSrs, srsnameRequested));
				}
			} else if (Geometria.LINESTRING.equals(g.getType())) {
				if (g.getCoordinates() instanceof ArrayList) {
					ArrayList<?> a = (ArrayList<?>) g.getCoordinates();
					
					Double[][] d = new Double[a.size()][2];
					for (int i = 0; i < d.length; i ++) {
						ArrayList<?> coord = (ArrayList<?>)a.get(i);
						d[i] = new Double[]{(Double)coord.get(0), (Double)coord.get(1)};
					}
					LineString l = new LineString(d);
					retorno.append(l.asJson(sourceSrs, srsnameRequested));
				}
			} else if (Geometria.POLIGON.equals(g.getType()) 
					&& (g.getCoordinates() instanceof ArrayList)) {
				ArrayList<?> a = (ArrayList<?>) g.getCoordinates();
				
				Double[][] d = new Double[a.size()][2];
				for (int i = 0; i < d.length; i ++) {
					ArrayList<?> coord = (ArrayList<?>)a.get(i);
					d[i] = new Double[]{(Double)coord.get(0), (Double)coord.get(1)};
				}
				Polygon l = new Polygon(d);
				retorno.append(l.asJson(sourceSrs, srsnameRequested));
			}  
		}
	}

	
}
