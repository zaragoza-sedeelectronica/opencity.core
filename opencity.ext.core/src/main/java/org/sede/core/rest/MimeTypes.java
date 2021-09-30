package org.sede.core.rest;

import java.util.HashMap;
import java.util.Map;

import org.sede.core.rest.view.TransformadorCsv;
import org.sede.core.rest.view.TransformadorKml;
import org.sede.core.rest.view.TransformadorGeoJson;
import org.sede.core.rest.view.TransformadorGeoRss;
import org.sede.core.rest.view.TransformadorICS;
import org.sede.core.rest.view.TransformadorJson;
import org.sede.core.rest.view.TransformadorJsonLd;
import org.sede.core.rest.view.TransformadorRdf;
import org.sede.core.rest.view.TransformadorRss;
import org.sede.core.rest.view.TransformadorXml;
import org.springframework.http.MediaType;

public class MimeTypes {
	private MimeTypes() {
		super();
	}
	public static final String JSONP_EXTENSION = "jsonp";
	public static final String JSON_EXTENSION = "json";
	public static final String JSONLD_EXTENSION = "jsonld";
	public static final String GEOJSON_EXTENSION = "geojson";
	public static final String XML_EXTENSION = "xml";
	public static final String XHTML_EXTENSION = "xhtml";
	public static final String HTML_EXTENSION = "html";
	public static final String RDF_EXTENSION = "rdf";
	public static final String TURTLE_EXTENSION = "ttl";
	public static final String RDF_N3_EXTENSION = "n3";
	public static final String SPARQL_XML_EXTENSION = "sparqlxml";
	public static final String SPARQL_JSON_EXTENSION = "sparqljson";
	
	public static final String SOLR_XML_EXTENSION = "solrxml";
	public static final String SOLR_JSON_EXTENSION = "solrjson";
	public static final String ICS_EXTENSION = "ics";
	
	public static final String GEORSS_EXTENSION = "georss";
	public static final String RSS_EXTENSION = "rss";
	
	public static final String CSV_EXTENSION = "csv";
	public static final String KML_EXTENSION = "kml";
	
	public static final String PDF_EXTENSION = "pdf";
	public static final String DOC_EXTENSION = "doc";
	public static final String XLS_EXTENSION = "xls";
	
	public static final String JSONP = "text/x+javascript";
	public static final String JSON = "application/json";
	public static final String JSONLD = "application/ld+json";
	public static final String GEOJSON = "application/geo+json";
	public static final MediaType GEOJSON_MEDIA = new MediaType("application", "geo+json");
	public static final String XML = "application/xml";
	public static final String XHTML = "application/xhtml+xml";
	public static final String HTML = "text/html";
	public static final String RDF = "application/rdf+xml";
	public static final String TURTLE = "application/x-turtle";
	public static final String RDF_N3 = "text/rdf+n3";
	public static final MediaType RDF_N3_MEDIA = new MediaType("text", "rdf+n3");
	public static final String SPARQL_XML = "application/sparql-results+xml";
	public static final String SPARQL_JSON = "application/sparql-results+json";
	public static final String ICS = "text/calendar";
	public static final MediaType ICS_MEDIA = new MediaType("text", "calendar");
	
	public static final String ZIP = "application/zip";
	
	public static final String PNG = "image/png";
	public static final String JPEG = "image/jpeg";
	public static final String GIF = "image/gif";
	
	public static final String SOLR_XML = "application/solr-results+xml";
	public static final String SOLR_JSON = "application/solr-results+json";
	public static final MediaType SOLR_JSON_MEDIA = new MediaType("application", "solr-results+json");
	public static final String GEORSS = "text/xml+georss";
	public static final String RSS = "application/rss+xml";

	public static final MediaType RSS_MEDIA = new MediaType("application", "rss+xml");
	public static final String CSV = "text/csv";
	public static final MediaType CSV_MEDIA = new MediaType("text", "csv");

	public static final String KML = "application/vnd.google-earth.kml+xml";
	public static final MediaType KML_MEDIA = new MediaType("application", "vnd.google-earth.kml+xml");
	
	public static final String PDF = "application/pdf";
	public static final MediaType PDF_MEDIA = new MediaType("application", "pdf");
	public static final String XLS = "application/vnd.ms-excel";
	public static final MediaType XLS_MEDIA = new MediaType("application", "vnd.ms-excel");
	
	public static final String DOC = "application/msword";
	public static final MediaType DOC_MEDIA = new MediaType("application", "msword");
	
	public static final MediaType APPLICATION_JSONLD = new MediaType("application", "ld+json");
	
	public static final Formato JSON_FORMATO = new Formato(JSON, new TransformadorJson(), JSON, JSON_EXTENSION, false);
	public static final Formato JSONLD_FORMATO = new Formato(JSONLD, new TransformadorJsonLd(), JSONLD, JSONLD_EXTENSION, false);
	public static final MediaType JSONLD_MEDIA = new MediaType("application", "ld+json");
	public static final Formato XML_FORMATO = new Formato(XML, new TransformadorXml(), XML, XML_EXTENSION, false);
	public static final Formato GEOJSON_FORMATO = new Formato(GEOJSON, new TransformadorGeoJson(), JSON, GEOJSON_EXTENSION, false);
	public static final Formato GEORSS_FORMATO = new Formato(GEORSS, new TransformadorGeoRss(), XML, GEORSS_EXTENSION, false);
	public static final Formato RSS_FORMATO = new Formato(RSS, new TransformadorRss(), RSS, RSS_EXTENSION, false);
	public static final Formato CSV_FORMATO = new Formato(CSV, new TransformadorCsv(), CSV, CSV_EXTENSION, false);
	public static final Formato KML_FORMATO = new Formato(KML, new TransformadorKml(), KML, KML_EXTENSION, false);
	public static final Formato RDF_FORMATO = new Formato(RDF, new TransformadorRdf(), RDF, RDF_EXTENSION, false);
	public static final Formato TURTLE_FORMATO = new Formato(TURTLE, new TransformadorRdf(), TURTLE, TURTLE_EXTENSION, false);
	public static final Formato N3_FORMATO = new Formato(RDF_N3, new TransformadorRdf(), RDF_N3, RDF_N3_EXTENSION, false);
	public static final Formato SPARQL_XML_FORMATO = new Formato(SPARQL_XML, new TransformadorRdf(), SPARQL_XML, SPARQL_XML_EXTENSION, false);
	public static final Formato SPARQL_JSON_FORMATO = new Formato(SPARQL_JSON, new TransformadorRdf(), SPARQL_JSON, SPARQL_JSON_EXTENSION, false);
	
	public static final Formato SOLR_XML_FORMATO = new Formato(SOLR_XML, new TransformadorXml(), SOLR_XML, SOLR_XML_EXTENSION, false);
	public static final Formato SOLR_JSON_FORMATO = new Formato(SOLR_JSON, new TransformadorJson(), SOLR_JSON, SOLR_JSON_EXTENSION, false);
	
	public static final Formato ICS_FORMATO = new Formato(ICS, new TransformadorICS(), ICS, ICS_EXTENSION, false);
	
	public static final Map<String, Formato> listado = new HashMap<String, Formato>();
    static {
        listado.put(JSONP, new Formato(JSONP, null, JSONP, JSONP_EXTENSION, false));
        listado.put(JSON, JSON_FORMATO);
        listado.put(JSONLD, JSONLD_FORMATO);
        listado.put(GEOJSON, GEOJSON_FORMATO);
        listado.put(CSV, CSV_FORMATO);
        listado.put(KML, KML_FORMATO);
        listado.put(XML, XML_FORMATO);
        listado.put(GEORSS, GEORSS_FORMATO);
        listado.put(RSS, RSS_FORMATO);
        listado.put(XHTML, new Formato(XHTML, null, XHTML, XHTML_EXTENSION, false));
        listado.put(HTML, new Formato(HTML, null, HTML, HTML_EXTENSION, false));
        listado.put(RDF, RDF_FORMATO);        
        listado.put(TURTLE, TURTLE_FORMATO);
        listado.put(RDF_N3, N3_FORMATO);
        listado.put(SPARQL_XML, SPARQL_XML_FORMATO);
        listado.put(SPARQL_JSON, SPARQL_JSON_FORMATO);
        listado.put(SOLR_XML, SOLR_XML_FORMATO);
        listado.put(SOLR_JSON, SOLR_JSON_FORMATO);
        listado.put(ICS, ICS_FORMATO);
        listado.put(PDF, new Formato(PDF, null, PDF, PDF_EXTENSION, true));
        listado.put(DOC, new Formato(DOC, null, DOC, DOC_EXTENSION, true));
        listado.put(XLS, new Formato(XLS, null, XLS, XLS_EXTENSION, true));
    }

    public static final Map<String, Formato> extensiones = new HashMap<String, Formato>();
	
    static {
    	extensiones.put(JSONP_EXTENSION, new Formato(JSONP, null, JSONP, JSONP_EXTENSION, false));
    	extensiones.put(JSON_EXTENSION, JSON_FORMATO);
    	extensiones.put(CSV_EXTENSION, CSV_FORMATO);
    	extensiones.put(KML_EXTENSION, KML_FORMATO);
    	extensiones.put(JSONLD_EXTENSION, JSONLD_FORMATO);
    	extensiones.put(GEOJSON_EXTENSION, GEOJSON_FORMATO);
    	extensiones.put(XML_EXTENSION, XML_FORMATO);
    	extensiones.put(GEORSS_EXTENSION, GEORSS_FORMATO);
    	extensiones.put(RSS_EXTENSION, RSS_FORMATO);
    	extensiones.put(XHTML_EXTENSION, new Formato(XHTML, null, XHTML, XHTML_EXTENSION, false));
    	extensiones.put(HTML_EXTENSION, new Formato(HTML, null, HTML, HTML_EXTENSION, false));
    	extensiones.put(RDF_EXTENSION, RDF_FORMATO);
    	extensiones.put(TURTLE_EXTENSION, TURTLE_FORMATO);
    	extensiones.put(RDF_N3_EXTENSION, N3_FORMATO);
    	extensiones.put(SPARQL_XML_EXTENSION, SPARQL_XML_FORMATO);
    	extensiones.put(SPARQL_JSON_EXTENSION, SPARQL_JSON_FORMATO);
    	
    	extensiones.put(SOLR_XML_EXTENSION, SOLR_XML_FORMATO);
    	extensiones.put(SOLR_JSON_EXTENSION, SOLR_JSON_FORMATO);
    	
    	extensiones.put(ICS_EXTENSION, ICS_FORMATO);
    	
    	extensiones.put(PDF_EXTENSION, new Formato(PDF, null, PDF, PDF_EXTENSION, true));
    	extensiones.put(DOC_EXTENSION, new Formato(DOC, null, DOC, DOC_EXTENSION, true));
    	extensiones.put(XLS_EXTENSION, new Formato(XLS, null, XLS, XLS_EXTENSION, true));
    	
    }

    
    public static String getFileExtension(String sFileName) {
		String sExtension;
		int iPos;
		iPos = sFileName.lastIndexOf('.');
		if (iPos > 0) {
			sExtension = sFileName.substring(iPos + 1);
		} else {
			sExtension = sFileName.substring(sFileName.length() - 3);
		}
		return sExtension.toLowerCase();
	}


	public static MediaType getContentTypeFromFileName(String fileName) {
		String sMimeType = getFileExtension(fileName);
	    MediaType finalContentType;
        // Controlled formats: Excel, Word, PowerPoint, Adobe.pdf, zip, text/html
        if (sMimeType.equals("htm") || sMimeType.equals("html")) {
        	finalContentType = MediaType.parseMediaType("text/html");
        } else if (sMimeType.equals("txt")) {
        	finalContentType = MediaType.parseMediaType("text/plain");
        } else if (sMimeType.equals("xml")) {
        	finalContentType = MediaType.parseMediaType("text/xml");
        } else if (sMimeType.equals("xls")) {
        	finalContentType = XLS_MEDIA;
        } else if (sMimeType.equals("doc") || sMimeType.equals("dot")) {
        	finalContentType = MediaType.parseMediaType(DOC);
        } else if (sMimeType.equals("pdf")) {
        	finalContentType = PDF_MEDIA;
        } else if (sMimeType.equals("ppt")) {
        	finalContentType = MediaType.parseMediaType("application/vnd.ms-powerpoint");
        } else if (sMimeType.equals("odt")) {
        	finalContentType = MediaType.parseMediaType("application/vnd.oasis.opendocument.text");
        } else if (sMimeType.equals("ods")) {
        	finalContentType = MediaType.parseMediaType("application/vnd.oasis.opendocument.spreadsheet");
        } else if (sMimeType.equals("zip")) {
        	finalContentType = MediaType.parseMediaType("application/x-zip-compressed");
        } else {
        	finalContentType = MediaType.parseMediaType("application");
        }
        return finalContentType;
	}
	
}
