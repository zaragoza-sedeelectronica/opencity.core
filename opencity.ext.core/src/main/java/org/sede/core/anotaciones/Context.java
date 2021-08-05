package org.sede.core.anotaciones;

import java.util.HashMap;
import java.util.Map;

public class Context {
	public static final String RDF = "rdf";
	public static final String RDFS = "rdfs";
	public static final String VCARD = "vcard";
	public static final String DCT = "dcterms";
	public static final String GR = "gr";
	public static final String GEO = "geo";
	public static final String GEONAMES = "geonames";
	public static final String FOAF = "foaf";
	public static final String ZAR = "zar";
	public static final String XSD= "xsd";
	public static final String ORG= "org";
	public static final String DCAT = "dcat";
	public static final String OWL = "owl";
	public static final String LOCN ="locn";
	public static final String SSN = "ssn";
	public static final String CPSV = "cpsv";

	public static final String ESCJR= "escjr";
	public static final String ESADM= "esadm";
	public static final String ESSUBV= "essubv";
	public static final String ESSUBVM= "essubvm";
	public static final String ESSRVC= "essrvc";
	public static final String ESPRESUP= "espresup";
	public static final String ESAGENDA= "esagenda";
	public static final String ESTURISMO= "esturismo";
	public static final String ESTUR = "estur";
	public static final String ESALOJ = "esaloj";
	public static final String ESMETEO = "esmeteo";
	public static final String ESCALIDADAIRE = "escalidadaire";
	public static final String ESACCID = "esaccid";
	public static final String ESEQUIP = "esequip";
	public static final String ESBICI = "esbici";
	public static final String ESAUTOB = "esautob";
	public static final String TMJOURNEY = "tmjourney";
	public static final String TMCOMMONS = "tmcommons";
	public static final String TMORG = "tmorg";
	public static final String ESNORM = "esnorm";
	public static final String ELI = "eli";
	
	public static final String CRUZAR = "cruzar";
	public static final String TURZAR = "turzar";
	public static final String MAPEOSEM = "mapeosem";
	public static final String OPEN311 = "o311";
	
	
	public static final String EVENT = "event";
	
	
	public static final String MY= "my";
	
	public static final String ORGES= "orges";
	
	public static final String GEOSPARQL= "geosparql";	
	public static final String GRAPH= "graph";
	
	public static final String SCHEMA= "schema";
	
	public static final String SKOS = "skos";
	
	public static final String EMP = "emp";
	
	public static final String ADMS = "adms";
	
	public static final String SOSA = "sosa";
	
	public static final String SF = "sf";
	
	
	
	public static final Map<String, Context> listado = new HashMap<String, Context>();
	
	
    static { 
    	listado.put(RDF, new Context("http://www.w3.org/1999/02/22-rdf-syntax-ns#", RDF));
        listado.put(RDFS, new Context("http://www.w3.org/2000/01/rdf-schema#", RDFS));        
        listado.put(VCARD, new Context("http://www.w3.org/2006/vcard/ns#", VCARD));
        listado.put(DCT, new Context("http://purl.org/dc/terms/", DCT));
        listado.put(GR, new Context("http://purl.org/goodrelations/v1#", GR));
        listado.put(GEO, new Context("http://www.w3.org/2003/01/geo/wgs84_pos#", GEO));
        listado.put(GEONAMES, new Context("http://www.geonames.org/ontology#", GEONAMES));
        listado.put(FOAF, new Context("http://xmlns.com/foaf/0.1/", FOAF));
        listado.put(ZAR, new Context("http://www.zaragoza.es/sede/portal/skos/vocab/", ZAR));        
        listado.put(XSD, new Context("http://www.w3.org/2001/XMLSchema#", XSD));
        listado.put(ORG, new Context("http://www.w3.org/ns/org#", ORG));
        listado.put(ADMS, new Context("https://www.w3.org/TR/vocab-adms/#", ADMS));
        listado.put(MY, new Context("http://www.zaragoza.es/example/my#", MY));
        listado.put(GEOSPARQL, new Context("http://www.opengis.net/ont/geosparql#", GEOSPARQL));
        listado.put(GRAPH, new Context("http://www.zaragoza.es/graph/default/", GRAPH));
        listado.put(ESCJR, new Context("http://vocab.linkeddata.es/datosabiertos/def/urbanismo-infraestructuras/callejero#",ESCJR));
        listado.put(ESADM, new Context("http://vocab.linkeddata.es/datosabiertos/def/sector-publico/territorio#",ESADM));
        listado.put(ESSUBVM, new Context("http://vocab.linkeddata.es/datosabiertos/def/sector-publico/subvencion#",ESSUBVM));
        listado.put(ESSRVC, new Context("http://vocab.linkeddata.es/datosabiertos/def/sector-publico/servicio#",ESSRVC));
        listado.put(ESPRESUP, new Context("http://vocab.linkeddata.es/datosabiertos/def/hacienda/presupuesto#",ESPRESUP));
        listado.put(ESAGENDA, new Context("http://vocab.linkeddata.es/datosabiertos/def/cultura-ocio/agenda#",ESAGENDA));        
        listado.put(ESTURISMO, new Context("http://vocab.linkeddata.es/datosabiertos/def/turismo/lugar#",ESTURISMO));
        listado.put(CRUZAR, new Context("http://idi.fundacionctic.org/cruzar/turismo#",CRUZAR));
        listado.put(TURZAR, new Context("https://www.zaragoza.es/sede/portal/def/turismo/lugar#",TURZAR));
        listado.put(ESTUR, new Context("http://vocab.linkeddata.es/datosabiertos/def/turismo/lugar#",ESTUR));
        listado.put(ESALOJ,new Context("http://vocab.linkeddata.es/datosabiertos/def/turismo/alojamiento#", ESALOJ));
        listado.put(ESMETEO,new Context("http://vocab.linkeddata.es/datosabiertos/def/medio-ambiente/meteorologia#", ESMETEO));
        listado.put(ESCALIDADAIRE,new Context("http://vocab.linkeddata.es/datosabiertos/def/medio-ambiente/calidad-aire#", ESCALIDADAIRE));
        listado.put(ESACCID,new Context("http://vocab.linkeddata.es/datosabiertos/def/transporte/accidentalidad-trafico#", ESACCID));
        listado.put(MAPEOSEM,new Context("https://www.zaragoza.es/sede/portal/def/urbanismo-infraestructuras/mapeo-semantico-calle#", MAPEOSEM));
        listado.put(ORGES, new Context("http://datos.gob.es/voc/sector-publico/org#",ORGES));
        listado.put(SCHEMA, new Context("http://schema.org/",SCHEMA));        
        listado.put(SKOS, new Context("http://www.w3.org/2004/02/skos/core#",SKOS));
        listado.put(EVENT, new Context("http://purl.org/NET/c4dm/event.owl#",EVENT));        
        listado.put(LOCN, new Context("http://www.w3.org/ns/locn#",LOCN));
        listado.put(OWL, new Context("http://www.w3.org/2002/07/owl#",OWL));
        listado.put(EMP, new Context("http://purl.org/ctic/empleo/oferta#",EMP));
        listado.put(SSN, new Context("http://purl.oclc.org/NET/ssnx/ssn#",SSN));
        listado.put(DCAT, new Context("http://www.w3.org/ns/dcat#",DCAT));
        listado.put(ESEQUIP, new Context("http://vocab.linkeddata.es/datosabiertos/def/urbanismo-infraestructuras/equipamiento#",ESEQUIP));
        listado.put(ESBICI, new Context("http://vocab.ciudadesabiertas.es/def/transporte/bicicleta-publica#",ESBICI));
        listado.put(ESAUTOB, new Context("http://vocab.ciudadesabiertas.es/def/transporte/autobus#",ESAUTOB));
        listado.put(TMJOURNEY, new Context("http://w3id.org/transmodel/journeys#",TMJOURNEY));
        listado.put(TMCOMMONS, new Context("http://w3id.org/transmodel/commons#",TMCOMMONS));
        listado.put(TMORG, new Context("http://w3id.org/transmodel/organisations#",TMORG));
        listado.put(ESNORM, new Context("http://vocab.linkeddata.es/datosabiertos/def/sector-publico/normativa#",ESNORM));
        listado.put(ELI, new Context("http://data.europa.eu/eli/ontology#",ELI));
        listado.put(OPEN311, new Context("http://ontology.eil.utoronto.ca/o311o#",OPEN311));
        listado.put(CPSV, new Context("http://purl.org/vocab/cpsv#",CPSV));
        listado.put(SOSA, new Context("http://www.w3.org/ns/sosa/",SOSA));
        listado.put(SF, new Context("http://www.opengis.net/ont/sf#",SF));
    }
	
	private String uri;
	private String prefijo;
	
	public Context(String uri, String prefijo) {
		super();
		this.uri = uri;
		this.prefijo = prefijo;
	}
	
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}
	public String getPrefijo() {
		return prefijo;
	}
	public void setPrefijo(String prefijo) {
		this.prefijo = prefijo;
	}
}
