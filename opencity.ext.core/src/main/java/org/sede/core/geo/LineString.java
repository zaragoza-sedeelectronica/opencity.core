package org.sede.core.geo;

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
public class LineString extends Geometria {
	
	
	@RdfMultiple({@Rdf(uri = "@id"), @Rdf(contexto = Context.RDF, propiedad = "type")})
	private String type = Geometria.LINESTRING;
	
//	@Rdf(contexto = Context.GEO, propiedad = "lat_lon")
	Double[][] coordinates;
	
	@RdfMultiple({@Rdf(contexto = Context.GEOSPARQL, propiedad = "wktLiteral"), @Rdf(contexto = Context.GEOSPARQL, propiedad = "asWKT")})	
	private String asWKT = Geometria.LINESTRING;
	
	@RdfMultiple({@Rdf(contexto = Context.GEOSPARQL, propiedad = "asGML"), @Rdf(contexto = Context.GEOSPARQL, propiedad = "asWKT")})	
	private String asGML = Geometria.LINESTRING;
	
	public LineString() {
		super();
	}
	public LineString(Double[][] coordinates) {
		super();
		this.coordinates = coordinates;
	}
	public LineString(String type, Double[][] coordinates) {
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
	public Double[][] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Double[][] coordinates) {
		this.coordinates = coordinates;
	}
	@Override
	public String toString() {
		return "Geometry [type=" + type + ", coordinates=" + coordinates + "]";
	}
	
	@Override
	public String asJson(String sourceSrs, String srsnameRequested) {
		StringBuilder retorno = new StringBuilder();
		retorno.append("{\"type\":\"LineString\",\"coordinates\":[");
		conCaracterYComas(sourceSrs, srsnameRequested, retorno, "[", "]");
		retorno.append("]}");
		return retorno.toString();
	}
	private void conCaracterYComas(String sourceSrs, String srsname, StringBuilder retorno, String caracterInicio, String caracterFin) {
//		if (srsname.equals(CheckeoParametros.SRSWGS84)) {
//			for(int i = 0; i < getCoordinates().length; i++) {
//				if (i > 0) {
//					retorno.append(",");
//				}
//				retorno.append(caracterInicio + Geometria.transformAsString(getCoordinates()[i][0], getCoordinates()[i][1], sourceSrs, srsname) + caracterFin);
//			}
//		} else if (srsname.equals(CheckeoParametros.SRSETRS89)) {
			for(int i = 0; i < getCoordinates().length; i++) {
				if (i > 0) {
					retorno.append(",");
				}
				retorno.append(caracterInicio + Geometria.transformAsString(getCoordinates()[i][0], getCoordinates()[i][1], sourceSrs, srsname) + caracterFin);
			}
//		} else {
//			for(int i = 0; i < getCoordinates().length; i++) {
//				if (i > 0) {
//					retorno.append(",");
//				}
//				retorno.append(caracterInicio + getCoordinates()[i][0] + "," + getCoordinates()[i][1] + caracterFin);
//			}
//		}
	}
	
	private void sinCaracterYComas(String sourceSrs, String srsnameRequested, StringBuilder retorno) {
		
		for(int i = 0; i < getCoordinates().length; i++) {
			if (i > 0) {
				retorno.append(" ");
			}
			retorno.append(Geometria.transformAsString(getCoordinates()[i][0], getCoordinates()[i][1], sourceSrs, srsnameRequested));
		}
			
	}
	
	@Override
	public String asJsonLD(String sourceSrs, String srsnameRequested) {
		StringBuilder retorno = new StringBuilder();
		if (srsnameRequested.equals(CheckeoParametros.SRSWGS84)) {
			retorno.append("{\"type\":\"http://www.opengis.net/ont/gml#LineString\",\"asGml\":\"<gml:LineString srsName='EPSG:4258' xmlns:gml='http://www.opengis.net/gml\'><gml:coordinates decimal='.' cs=',' ts=' '>");
			sinCaracterYComas(sourceSrs, srsnameRequested, retorno);
			retorno.append("</gml:coordinates></gml:LineString>\"}");
		} else if (srsnameRequested.equals(CheckeoParametros.SRSETRS89)) {
			retorno.append("{\"type\":\"http://www.opengis.net/ont/sf#LineString\",\"asWKT\":\"<http://www.opengis.net/def/crs/EPSG/0/25830>LineString(");
			conCaracterYComas(sourceSrs, srsnameRequested, retorno, "(", ")");
			retorno.append(")\"}");
		} else{
			retorno.append("{\"type\":\"http://www.opengis.net/ont/sf#LineString\",\"asWKT\":\"<http://www.opengis.net/def/crs/EPSG/0/23030>LineString(");
			conCaracterYComas(sourceSrs, srsnameRequested, retorno, "(", ")");
			retorno.append(")\"}");
		}
		return retorno.toString();
	}
	
	@Override
	public String asXML(String sourceSrs, String srsnameRequested) {
		StringBuilder retorno = new StringBuilder();
		retorno.append("<type>LineString</type><coordinates>");
		conCaracterYComas(sourceSrs, srsnameRequested, retorno, " ", "");
		retorno.append("</coordinates>");
		return retorno.toString();
	}
	
	@Override
	public String asGEORSS(String sourceSrs, String srsnameRequested) {
		StringBuilder retorno = new StringBuilder();
		retorno.append("<georss:linestring>");
		conCaracterYComas(sourceSrs, srsnameRequested, retorno, " ", "");
		retorno.append("</georss:linestring>");
		return retorno.toString();

	}
	@Override
	public String asCSV(String sourceSrs, String srsnameRequested) {
		StringBuilder retorno = new StringBuilder();
		conCaracterYComas(sourceSrs, srsnameRequested, retorno, "[", "]");
		return retorno.toString();
	}
	@Override
	public String asHtml(String sourceSrs, String srsnameRequested) {
		return asCSV(sourceSrs, srsnameRequested);
		
	}
	
	public boolean formatoWgs84() {
		return (getCoordinates()[0][0] < 0);
	}
	
	
}
