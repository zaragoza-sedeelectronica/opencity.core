package org.sede.core.geo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.sede.core.rest.CheckeoParametros;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.geoslab.coordinatetransformation.Transformer;

public class Geometria implements Serializable {
	private static final Logger logger = LoggerFactory.getLogger(Geometria.class);
	
	public static final String POINT = "Point";
	public static final String LINESTRING = "LineString";
	public static final String MULTILINESTRING = "MultiLineString";
	public static final String POLIGON = "Polygon";
	public static final String GEOMETRYCOLLECTION = "GeometryCollection";
	
	
	private static final String UTM30N_ED50 = "23030";
	private static final String UTM30N_ETRS89 = "25830";
	private static final String WGS84 = "4326";	
	
	private static final Map<String, String> SRS = new HashMap<String, String>();
    static {
        SRS.put(CheckeoParametros.SRSUTM30N, UTM30N_ED50);
        SRS.put(CheckeoParametros.SRSETRS89, UTM30N_ETRS89);
        SRS.put(CheckeoParametros.SRSWGS84, WGS84);
        
    }
	
	
	private String type;
	private Object coordinates;
	
	public String asJson(String sourceSrs, String srsnameRequested) {
		return "";
	}
	
	public String asXML(String sourceSrs, String srsnameRequested) {
		return "";
	}
	
	public String asGEORSS(String sourceSrs, String srsnameRequested) {
		return "";
	}
	
	public String asCSV(String sourceSrs, String srsnameRequested) {
		return "";
	}
	
	public String asHtml(String sourceSrs, String srsnameRequested) {
		return "";
	}
	
	public String asJsonLD(String sourceSrs, String srsnameRequested) {
		return "";
	}
	
	
	public static String transformAsString(Double x, Double y, String source, String target) {
		try {
	        double[] wsgGP = Geometria.transform(x, y, source, target);
	        double lax = wsgGP[1]; // x
	        double lay = wsgGP[0]; // y
	        return "" + lay + "," + lax;
		} catch (Exception e) {
			return "";
		}
	}
	
	public static double[] transform(Double x, Double y, String source, String target) {
		logger.info("Transformar coordenadas: origen " + source + ", destino " + target);
		return Transformer.transform(x,y, SRS.get(source), SRS.get(target));
	}

	public static Punto pasarAWgs84ConPunto(Double x, Double y) {
		try {
			String source = UTM30N_ED50;
	        String target = WGS84;
	        double[] coords = Transformer.transform(x,y, source,target);
	        return new Punto(Punto.POINT, new Double[]{coords[1],coords[0]});
		} catch (Exception e) {
			return new Punto();
		}
	}
	
	
	public static Double[] pasarAUTM30(Double lon, Double lat) {
		try {
			String source = WGS84;
			String target = UTM30N_ED50;
			double[] coords = Transformer.transform(lon,lat, source,target);
			
			return new Double[]{new Double(coords[0]), new Double(coords[1])};
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	public static Double[] pasarETRS89AUTM30(Double lon, Double lat) {
		try {
			String source = UTM30N_ETRS89;
			String target = UTM30N_ED50;
			double[] coords = Transformer.transform(lon,lat, source,target);
			
			return new Double[]{new Double(coords[0]), new Double(coords[1])};
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new IllegalArgumentException(e.getMessage());
		}
	}
	
	public static String invertirCoordenadas(String p) {
		String[] coors = p.split(",");
		return "" + coors[1] + "," + coors[0];
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Object getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(Object coordinates) {
		this.coordinates = coordinates;
	}

	
	
	
}
