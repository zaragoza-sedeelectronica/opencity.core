package org.sede.core.geo;

import java.util.StringTokenizer;

import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.DynamicUpdate;
import org.sede.core.anotaciones.Rdf;
import org.sede.core.anotaciones.RdfMultiple;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.anotaciones.Context;

@XmlRootElement(name="geometry")
@Entity
@DynamicUpdate
@XmlAccessorType(XmlAccessType.FIELD)
@Rdf
public class Punto extends Geometria {
	
	
	@RdfMultiple({@Rdf(uri = "@id"), @Rdf(contexto = Context.RDF, propiedad = "type")})
	private String type = Geometria.POINT;
	
	@Rdf(contexto = Context.GEO, propiedad = "lat_lon")
	Double[] coordinates;
	
	@RdfMultiple({@Rdf(contexto = Context.XSD, propiedad = "double"), @Rdf(contexto = Context.GEO, propiedad = "lat")})
	private String lat = Geometria.POINT;
	
//	@Rdf(contexto = Context.GEO, propiedad = "long")
	@RdfMultiple({@Rdf(contexto = Context.XSD, propiedad = "double"), @Rdf(contexto = Context.GEO, propiedad = "long")})	
	private String lon = Geometria.POINT;
	
	@RdfMultiple({@Rdf(contexto = Context.GEOSPARQL, propiedad = "wktLiteral"), @Rdf(contexto = Context.GEOSPARQL, propiedad = "asWKT")})	
	private String asWKT = Geometria.POINT;
	
	public Punto() {
		super();
	}
	public Punto(Double[] coordinates) {
		super();
		this.coordinates = coordinates;
	}
	public Punto(String type, Double[] coordinates) {
		super();
		this.type = type;
		this.coordinates = coordinates;
	}
	@Override
	public String getType() {
		return type;
	}
	@Override
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public Double[] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Double[] coordinates) {
		this.coordinates = coordinates;
	}
	@Override
	public String toString() {
		return "Geometry [type=" + type + ", coordinates=" + coordinates + "]";
	}
	
	@Override
	public String asJson(String sourceSrs, String srsnameRequested) {
		if (sourceSrs.equals(srsnameRequested)) {
			return "{\"type\":\"Point\",\"coordinates\":[" + getCoordinates()[0] + "," + getCoordinates()[1] + "]}";
		} else {
			return "{\"type\":\"Point\",\"coordinates\":[" + Geometria.transformAsString(getCoordinates()[0], getCoordinates()[1], sourceSrs, srsnameRequested) + "]}";
		}
	}
	@Override
	public String asJsonLD(String sourceSrs, String srsnameRequested) {
		if (srsnameRequested.equals(CheckeoParametros.SRSWGS84)) {
			String wgs84 = Geometria.transformAsString(getCoordinates()[0], getCoordinates()[1], sourceSrs, srsnameRequested);
			StringTokenizer st = new StringTokenizer(wgs84, ",");
			return "{\"type\":\"http://www.w3.org/2003/01/geo/wgs84_pos#Point\",\"long\": " + st.nextToken() + ", \"lat\": " + st.nextToken() + "}";
		} else if (srsnameRequested.equals(CheckeoParametros.SRSETRS89)) {
			String etrs89 = Geometria.transformAsString(getCoordinates()[0], getCoordinates()[1], sourceSrs, srsnameRequested);
			StringTokenizer st = new StringTokenizer(etrs89, ",");
			return "{\"type\":\"http://www.opengis.net/ont/sf#Point\",\"asWKT\":\"<http://www.opengis.net/def/crs/EPSG/0/25830>Point("+ st.nextToken()+" "+ st.nextToken()+")"+"\"}";
		} else {
			double[] coords = Geometria.transform(getCoordinates()[0], getCoordinates()[1], sourceSrs, srsnameRequested);
			return "{\"type\":\"http://www.opengis.net/ont/sf#Point\",\"asWKT\":\"<http://www.opengis.net/def/crs/EPSG/0/23030>Point("+ coords[0]+" "+ coords[1]+")"+"\"}";
		}
	}
	
	@Override
	public String asXML(String sourceSrs, String srsnameRequested) {
		if (sourceSrs.equals(srsnameRequested)) {
			return "<type>Point</type><coordinates>" + getCoordinates()[0] + "," + getCoordinates()[1] + "</coordinates>";
		} else {
			return "<type>Point</type><coordinates>" + Geometria.transformAsString(getCoordinates()[0], getCoordinates()[1], sourceSrs, srsnameRequested) + "</coordinates>";
		}
	}
	
	@Override
	public String asGEORSS(String sourceSrs, String srsnameRequested) {
		
		if (sourceSrs.equals(srsnameRequested)) {
			return "<georss:point>" + getCoordinates()[0] + " " + getCoordinates()[1] + "</georss:point>";
		} else {
			return "<georss:point>" + Geometria.transformAsString(getCoordinates()[0], getCoordinates()[1], sourceSrs, srsnameRequested) + "</georss:point>";
		}
	}
	@Override
	public String asCSV(String sourceSrs, String srsnameRequested) {
		if (sourceSrs.equals(srsnameRequested)) {
			return "" + getCoordinates()[0] + " " + getCoordinates()[1] + "";
		} else {
			return "" + Geometria.transformAsString(getCoordinates()[0], getCoordinates()[1], sourceSrs, srsnameRequested) + "";
		}		
	}
	@Override
	public String asHtml(String sourceSrs, String srsnameRequested) {
		if (sourceSrs.equals(srsnameRequested)) {
			return "" + getCoordinates()[0] + " " + getCoordinates()[1] + "";
		} else {
			return "" + Geometria.transformAsString(getCoordinates()[0], getCoordinates()[1], sourceSrs, srsnameRequested) + "";
		}			
	}
	
	public boolean formatoWgs84() {
		return (getCoordinates()[0] < 0);
	}

	public String getRdf() {
		return getRdf("true");
	}
	
	public Double[] getWgs84() {
		
		double[] coord = (double[])Geometria.transform(getCoordinates()[0], getCoordinates()[1], CheckeoParametros.SRSUTM30N, CheckeoParametros.SRSWGS84);	
		return new Double[]{coord[0], coord[1]};
	}
	public String getMeta() {
		// FIXME revisar si @SourceSRS es distinto de SRSUTM30B 
		if (!formatoWgs84()) {
			double[] coord;
			if (CheckeoParametros.SRSETRS89.equals(this.getType())) {
				coord = (double[])Geometria.transform(getCoordinates()[0], getCoordinates()[1], CheckeoParametros.SRSETRS89, CheckeoParametros.SRSWGS84);
			} else {
				coord = (double[])Geometria.transform(getCoordinates()[0], getCoordinates()[1], CheckeoParametros.SRSUTM30N, CheckeoParametros.SRSWGS84);	
			}
			
			this.setCoordinates(new Double[]{coord[0], coord[1]});
		}
		return "<meta property=\"geo.position\" content=\"" + getCoordinates()[1] + ";" + getCoordinates()[0] + "\"/>";
	}
	public String getRdf(String property) {
		// FIXME revisar si @SourceSRS es distinto de SRSUTM30B 
		
		if (!formatoWgs84()) {
			double[] coord;
			if (CheckeoParametros.SRSETRS89.equals(this.getType())) {
				coord = (double[])Geometria.transform(getCoordinates()[0], getCoordinates()[1], CheckeoParametros.SRSETRS89, CheckeoParametros.SRSWGS84);
			} else {
				coord = (double[])Geometria.transform(getCoordinates()[0], getCoordinates()[1], CheckeoParametros.SRSUTM30N, CheckeoParametros.SRSWGS84);	
			}
			
			this.setCoordinates(new Double[]{coord[0], coord[1]});
		}
		return "<div" + ("true".equals(property) ? " property=\"geo\"" : "") + " typeof=\"GeoCoordinates\">"
                + "<meta property=\"latitude\" content=\"" + getCoordinates()[1] + "\"/>"
                + "<meta property=\"longitude\" content=\"" + getCoordinates()[0] + "\"/>"
            + "</div>";
	}
}
