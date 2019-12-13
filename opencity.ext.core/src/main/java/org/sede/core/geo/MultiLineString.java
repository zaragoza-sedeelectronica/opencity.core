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
public class MultiLineString extends Geometria {
	
	
	@RdfMultiple({@Rdf(uri = "@id"), @Rdf(contexto = Context.RDF, propiedad = "type")})
	private String type = Geometria.MULTILINESTRING;
	
//	@Rdf(contexto = Context.GEO, propiedad = "lat_lon")
	Double[][][] coordinates;
	
	@RdfMultiple({@Rdf(contexto = Context.GEOSPARQL, propiedad = "wktLiteral"), @Rdf(contexto = Context.GEOSPARQL, propiedad = "asWKT")})	
	private String asWKT = Geometria.MULTILINESTRING;
	
	@RdfMultiple({@Rdf(contexto = Context.GEOSPARQL, propiedad = "asGML"), @Rdf(contexto = Context.GEOSPARQL, propiedad = "asWKT")})	
	private String asGML = Geometria.MULTILINESTRING;
	
	public MultiLineString() {
		super();
	}
	public MultiLineString(Double[][][] coordinates) {
		super();
		this.coordinates = coordinates;
	}
	public MultiLineString(String type, Double[][][] coordinates) {
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
	public Double[][][] getCoordinates() {
		return coordinates;
	}
	public void setCoordinates(Double[][][] coordinates) {
		this.coordinates = coordinates;
	}
	@Override
	public String toString() {
		return "Geometry [type=" + type + ", coordinates=" + coordinates + "]";
	}
	
	@Override
	public String asJson(String sourceSrs, String srsnameRequested) {
		StringBuilder retorno = new StringBuilder();
		retorno.append("{\"type\":\"MultiLineString\",\"coordinates\":[");
		conCaracterYComas(sourceSrs, srsnameRequested, retorno, "[", "]");
		retorno.append("]}");
		return retorno.toString();
	}
	private void conCaracterYComas(String sourceSrs, String srsnameRequested, StringBuilder retorno, String caracterInicio, String caracterFin) {
//		if (srsnameRequested.equals(CheckeoParametros.SRSWGS84)) {
//			for(int k = 0; k < getCoordinates().length; k++) {
//				if (getCoordinates()[k] != null) {
//					if (k > 0) {
//						retorno.append(",");	
//					}
//					retorno.append("[");
//					for (int i = 0; i < getCoordinates()[k].length; i++) {
//						if (i > 0) {
//							retorno.append(",");
//						}
//						retorno.append(caracterInicio + Geometria.transformAsString(getCoordinates()[k][i][0], getCoordinates()[k][i][1], sourceSrs, srsnameRequested) + caracterFin);
//					}
//					retorno.append("]");
//				}
//			}
//		} else if (srsnameRequested.equals(CheckeoParametros.SRSETRS89)) {
			for(int k = 0; k < getCoordinates().length; k++) {
				if (getCoordinates()[k] != null) {
					if (k > 0) {
						retorno.append(",");	
					}
					retorno.append("[");
					for (int i = 0; i < getCoordinates()[k].length; i++) {
						if (i > 0) {
							retorno.append(",");
						}
						retorno.append(caracterInicio + Geometria.transformAsString(getCoordinates()[k][i][0], getCoordinates()[k][i][1], sourceSrs, srsnameRequested) + caracterFin);
					}
					retorno.append("]");
				}
			}
//			
//		} else {
//			for(int k = 0; k < getCoordinates().length; k++) {
//				if (getCoordinates()[k] != null) {
//					if (k > 0) {
//						retorno.append(",");	
//					}
//					retorno.append("[");
//					for (int i = 0; i < getCoordinates()[k].length; i++) {
//						if (i > 0) {
//							retorno.append(",");
//						}
//						retorno.append(caracterInicio + getCoordinates()[k][i][0] + "," + getCoordinates()[k][i][1] + caracterFin);
//					}
//					retorno.append("]");
//				}
//			}
//		}
	}
	
	private void sinCaracterYComas(String sourceSrs, String srsnameRequested, StringBuilder retorno) {
		for(int k = 0; k < getCoordinates().length; k++) {
			retorno.append("[");
			for (int i = 0; i < getCoordinates()[k].length; i++) {
				if (i > 0) {
					retorno.append(" ");
				}
				retorno.append(Geometria.transformAsString(getCoordinates()[k][i][0], getCoordinates()[k][i][1], sourceSrs, srsnameRequested));
			}
			retorno.append("]");
		}
			
	}
	
	@Override
	public String asJsonLD(String sourceSrs, String srsnameRequested) {
		StringBuilder retorno = new StringBuilder();
		if (srsnameRequested.equals(CheckeoParametros.SRSWGS84)) {
			retorno.append("{\"type\":\"http://www.opengis.net/ont/gml#MultiLineString\",\"asGml\":\"<gml:MultiLineString srsName='EPSG:4258' xmlns:gml='http://www.opengis.net/gml\'><gml:coordinates decimal='.' cs=',' ts=' '>");
			sinCaracterYComas(sourceSrs, srsnameRequested, retorno);
			retorno.append("</gml:coordinates></gml:MultiLineString>\"}");
		} else if (srsnameRequested.equals(CheckeoParametros.SRSETRS89)) {
			retorno.append("{\"type\":\"http://www.opengis.net/ont/sf#MultiLineString\",\"asWKT\":\"<http://www.opengis.net/def/crs/EPSG/0/25830>MultiLineString(");
			conCaracterYComas(sourceSrs, srsnameRequested, retorno, "(", ")");
			retorno.append(")\"}");
		} else{
			retorno.append("{\"type\":\"http://www.opengis.net/ont/sf#MultiLineString\",\"asWKT\":\"<http://www.opengis.net/def/crs/EPSG/0/23030>MultiLineString(");
			conCaracterYComas(sourceSrs, srsnameRequested, retorno, "(", ")");
			retorno.append(")\"}");
		}
		return retorno.toString();
	}
	
	@Override
	public String asXML(String sourceSrs, String srsnameRequested) {
		StringBuilder retorno = new StringBuilder();
		retorno.append("<type>MultiLineString</type><coordinates>");
		conCaracterYComas(sourceSrs, srsnameRequested, retorno, " ", "");
		retorno.append("</coordinates>");
		return retorno.toString();
	}
	
	@Override
	public String asGEORSS(String sourceSrs, String srsnameRequested) {
		StringBuilder retorno = new StringBuilder();
		retorno.append("<georss:multiLinestring>");
		conCaracterYComas(sourceSrs, srsnameRequested, retorno, " ", "");
		retorno.append("</georss:multiLinestring>");
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
		return (getCoordinates()[0][0][0] < 0);
	}
	
	
}
