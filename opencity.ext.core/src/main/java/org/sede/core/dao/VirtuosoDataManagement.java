package org.sede.core.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.rest.MimeTypes;
import org.sede.core.utils.Propiedades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonParseException;
import com.github.jsonldjava.core.JsonLdOptions;
import com.github.jsonldjava.core.JsonLdProcessor;
import com.github.jsonldjava.utils.JsonUtils;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoUpdateFactory;
import virtuoso.jena.driver.VirtuosoUpdateRequest;

public class VirtuosoDataManagement {
	private static final Logger logger = LoggerFactory.getLogger(VirtuosoDataManagement.class);
	private static final String USER = Propiedades.getString("virtuoso.user");
	private static final String PASSWORD = Propiedades.getString("virtuoso.pass");
	private static final String CONECTION = Propiedades.getString("virtuoso.connection");

	
	public static final int TOTAL_CARGA = 100000;
	public static final int ROWS_CARGA = 20;
	
	public static void main(String[] args) {
				
		
		VirtGraph set = new VirtGraph( "http://www.zaragoza.es/sector-publico/contrato",
				CONECTION, USER, PASSWORD);
		
//		String queryString = "PREFIX pproc: <http://contsem.unizar.es/def/sector-publico/pproc#>"
//			+ "DELETE FROM <http://www.zaragoza.es/sector-publico/contrato> {?x ?y ?z} WHERE {?x ?y ?z." 
//			+ "?x pproc:contractProcedureSpecifications ?contProcSpec."
//			+ "?contProcSpec pproc:procedureType pproc:Minor}";
		
//		SELECT distinct ?uri ?faceta
//				WHERE { ?uri a <http://www.w3.org/ns/org#Organization>. 	
//				?uri <http://purl.org/dc/terms/title>	?faceta.	
//
//				}
		
		String queryString = ""
				+ "DELETE FROM <http://www.zaragoza.es/sector-publico/contrato> {?uri ?y ?faceta} WHERE {?uri ?y ?faceta." 
				+ "?uri a <http://www.w3.org/ns/org#Organization>. ?uri <http://purl.org/dc/terms/title>	?faceta. FILTER (regex(str(?faceta),\"OFICINA UNICA DEL CONTRIBUYENTE\")).}";
		
//		String queryString = "DELETE FROM <http://www.zaragoza.es/sector-publico/contrato> {?uri ?faceta}  WHERE { ?uri a <http://www.w3.org/ns/org#Organization>. ?uri <http://purl.org/dc/terms/title>	?faceta. FILTER (regex(str(?faceta),\"ó\")).}";
		
		VirtuosoUpdateRequest vur2 = VirtuosoUpdateFactory.create(queryString, set);
		
		vur2.exec();
		
//		String[] ids = {"31116-14","31992-14","32975-14","33003-14","33056-14","33106-14","33128-14","33134-14","33139-14","33181-14","33251-14","33259-14","33338-14","33564-14","33649-14","33687-14","33692-14","33735-14","33818-14","33865-14","33954-14","34078-14","34129-14","34290-14","34291-14","34563-14","34654-14","34827-14","34831-14","34910-14","35474-14","35556-14","35770-14","35801-14","35953-14","36029-14","36214-14","36521-14","36525-14","36566-14","36605-14","36680-14","36895-14","36918-14","36934-14","36937-14","37014-14","37026-14","37307-14","37527-14","37574-14","37593-14","37850-14","37871-14","37878-14","38067-14","38134-14","38138-14","38143-14","38156-14","38633-14","38713-14","38720-14","38724-14","38770-14","38782-14","38817-14","38900-14","38904-14","38944-14","39175-14","39307-14","39592-14","39628-14","39785-14","39952-14","40005-14","40047-14","40088-14","40094-14","40140-14","40289-14","40310-14","40349-14","40680-14","40687-14","40689-14","40698-14","40830-14","40847-14","40874-14","40881-14","40895-14","40902-14","40904-14","40994-14","41001-14","41015-14","41111-14","41127-14","41162-14","41230-14","41355-14","41372-14","41382-14","41600-14","41792-14","41876-14","42012-14","42050-14","42053-14","42303-14","42366-14","42400-14","42591-14","42592-14","42625-14","42628-14","42896-14","42898-14","42929-14","42948-14","43131-14","43269-14","43655-14","43771-14","43841-14","44065-14","44188-14","44270-14","44338-14","44597-14","44656-14","44742-14","44749-14","44880-14","44881-14","44882-14","44883-14","44884-14","44885-14","44897-14","44939-14","44991-14","45037-14","45045-14","45056-14","45068-14","45085-14","45094-14","45106-14","45123-14","45124-14","45131-14","45187-14","45236-14","45244-14","45254-14","45298-14","45340-14","45511-14","45517-14","45551-14","45653-14","45657-14","45699-14","45812-14","45825-14","45874-14","45902-14","46005-14","46013-14","46397-14","46400-14","46403-14","46404-14","46442-14","46452-14","46470-14","46633-14","46635-14","46675-14","46739-14","46753-14","47484-14","47512-14","47561-14","47563-14","47577-14","47578-14","47593-14","47688-14","47715-14","47718-14","47722-14","47771-14","47826-14","47827-14","47838-14","48014-14","48139-14","48164-14","48201-14","48212-14","48316-14","48350-14","48516-14","48567-14","48738-14","48850-14"};
//		for (int i = 0; i < ids.length; i++) {
//			VirtuosoDataManagement.deleteElement("http://www.zaragoza.es/api/recurso/sector-publico/contrato/" + ids[i],Contrato.class.getAnnotation(Grafo.class).value());
//		}
		
//		addElement("http://www.zaragoza.es/api/recurso/ciencia-tecnologia/punto-wifi/79");
//		updateElement("http://www.zaragoza.es/api/recurso/ciencia-tecnologia/punto-wifi/79");
//		deleteElement("http://www.zaragoza.es/api/recurso/ciencia-tecnologia/punto-wifi/79");
//		deleteGrafo("http://www.zaragoza.es/sector-publico/contrato");
//		deleteGrafo("http://www.zaragoza.es/empleo/oferta-empleo/");
		
//		addElements("http://www.zaragoza.es/turismo/edificio-historico/", "<http://www.zaragoza.es/api/recurso/turismo/edificio-historico/119>"
//			      + " a       <http://vocab.linkeddata.es/datosabiertos/def/turismo/lugar#EdificioHistorico> ;"
//			      + " <http://www.w3.org/2000/01/rdf-schema#label>"
//			      + "  \" Mefisto , Fernando Soteras , 7\"^^<http://www.w3.org/2001/XMLSchema#string> ;"
//			      + "  <http://purl.org/dc/terms/identifier>"
//			      + "  \"119\" ;"
//			      + "  <http://purl.org/dc/terms/title>"
//			      + "  \" Mefisto , Fernando Soteras , 7\"^^<http://www.w3.org/2001/XMLSchema#string> ;"
//			      + "  <http://schema.org/name>"
//			      + "  \" Mefisto , Fernando Soteras , 7\"^^<http://www.w3.org/2001/XMLSchema#string> ;"
//			      + " <http://schema.org/url>"
//			      + "  \"http://www.zaragoza.es/pgou/edih/mefisto07.pdf\" ;"
//			      + "  <http://vocab.linkeddata.es/datosabiertos/def/turismo/lugar#elementosConservar>"
//			      + "  \"Fachada, zagu&#38;aacute;n, caja de escaleras, etc.\" ;"
//			      + "  <http://vocab.linkeddata.es/datosabiertos/def/turismo/lugar#gradoProteccion>"
//			      + "          \"Interes Ambiental\" ;"
//			      + "  <http://vocab.linkeddata.es/datosabiertos/def/turismo/lugar#intervencionesPermitidas>"
//			      + "        \"Rehabilitaci&#38;oacute;n\" ;"
//			      + "  <http://vocab.linkeddata.es/datosabiertos/def/turismo/lugar#notaHistorica>"
//			      + "        \" \" ;"
//			      + "  <http://vocab.linkeddata.es/datosabiertos/def/turismo/lugar#tipoEdificioHistorico>"
//			      + "  \"Edificio\" ;"
//			      + "  <http://www.geonames.org/ontology#name>"
//			       + "       \" Mefisto , Fernando Soteras , 7\"^^<http://www.w3.org/2001/XMLSchema#string> ;"
//			      + "  =       <http://www.zaragoza.es/api/recurso/urbanismo-infraestructuras/equipamiento/edificio/3851> .");
		
		
//		ejecutar("http://www.zaragoza.es/sector-publico/contrato", "PREFIX pproc: <http://contsem.unizar.es/def/sector-publico/pproc#> "
//				+ System.getProperty("line.separator") + "PREFIX pc: <http://purl.org/procurement/public-contracts#> "
//				+ System.getProperty("line.separator") + "DELETE { ?x pproc:supplier ?y } "
//				+ System.getProperty("line.separator") + "INSERT { ?x pc:supplier ?y } "
//				+ System.getProperty("line.separator") + "WHERE { ?x pproc:supplier ?y}");

		
//		ejecutar("PREFIX pproc: <http://contsem.unizar.es/def/sector-publico/pproc#> PREFIX pc: <http://purl.org/procurement/public-contracts#> DELETE { ?x pproc:duration ?y } INSERT { ?x pproc:contractTemporalConditions ?z . ?z pproc:estimatedDuration ?y} WHERE {  {SELECT ?x ?y (IRI(CONCAT(?x,\"/ContractTemporalConditions\")) AS ?z) WHERE{ ?x pproc:duration ?y }}}");
		
	}
	
	public static String query(String query, String graph, String format) throws Exception {
		logger.info("Consulta Virtuoso: " + query);
		String consulta = Propiedades.getSparqlEndPoint() + "?default-graph-uri=" + graph 
				+ "&query=" + URLEncoder.encode(query, CharEncoding.UTF_8) + "&format=" + URLEncoder.encode(format, CharEncoding.UTF_8) + "&timeout=0&debug=on";
		return makeHttpGet(consulta);
	} 
	public static String queryExterno(String externo,String query, String graph) throws Exception {
		String consulta = externo + "/sparql?default-graph-uri=" + graph 
				+ "&query=" + URLEncoder.encode(query, CharEncoding.UTF_8) + "&format=" + URLEncoder.encode(MimeTypes.SPARQL_JSON, CharEncoding.UTF_8) + "&timeout=0&debug=on";
		return makeHttpGet(consulta);
	}
	public static void addElement(String uri){
		if (USER.length() > 0) {
			try {
				String response = makeHttpRequest(uri+".n3");
			
				VirtGraph set = new VirtGraph (CONECTION, USER, PASSWORD);
		
				String graph = getGraph(uri);
		        String str = "INSERT INTO GRAPH <"+graph+"> { "+ response +" }";
		        VirtuosoUpdateRequest  vur = VirtuosoUpdateFactory.create(str, set);
		        vur.exec();  
		        
		        set.close();
	        
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("No se ha especificado la configuración de virtuoso");
		}
	}
	
	public static void addElements(String graph, String triples) throws Exception {
		if (USER.length() > 0) {

			try { 
				
				VirtGraph set = new VirtGraph (CONECTION, USER, PASSWORD);
		        String str = "INSERT INTO GRAPH <"+graph+"> { "+ triples +" }";
		        
		        VirtuosoUpdateRequest  vur = VirtuosoUpdateFactory.create(str, set);
		        vur.exec();  
		        set.close();
			} catch (Exception e) {
				throw e;
			}
		} else {
			logger.error(triples);
			logger.error("No se ha especificado la configuracion de virtuoso");
		}
	}
	
	public static void addElementTriple(String uri, String triples){
		if (USER.length() > 0) {
			try {
//				String response = makeHttpRequest(uri+".n3");
			
				VirtGraph set = new VirtGraph (CONECTION, USER, PASSWORD);
				String graph = getGraph(uri);
		        String str = "INSERT INTO GRAPH <"+graph+"> { "+ triples +" }";
		        VirtuosoUpdateRequest  vur = VirtuosoUpdateFactory.create(str, set);
		        vur.exec();  
		        
		        set.close();
	        
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("No se ha especificado la configuración de virtuoso");
		}
	}
	
	public static void addElementTriple(String uri, String triples, String graph){
		if (USER.length() > 0) {
			try {
				String response = makeHttpRequest(uri+".n3");
			
				VirtGraph set = new VirtGraph (CONECTION, USER, PASSWORD);
		
		        String str = "INSERT INTO GRAPH <"+graph+"> { "+ response +" }";
		        VirtuosoUpdateRequest  vur = VirtuosoUpdateFactory.create(str, set);
		        vur.exec();  
		        
		        set.close();
	        
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		} else {
			logger.error("No se ha especificado la configuración de virtuoso");
		}
	}
	
	public static void deleteGrafo(String graph) {
		if (USER.length() > 0) {
			VirtGraph set = new VirtGraph(graph, CONECTION, USER, PASSWORD);
			set.clear();
			set.close();
		} else {
			logger.error("No se ha especificado la configuracion de virtuoso");
		}

	}
	
	public static void deleteElement(String uri){
		if (USER.length() > 0) {
			VirtGraph set = new VirtGraph (CONECTION, USER, PASSWORD);
			
			String graph = getGraph(uri);
			
	        String sparql = "DELETE " 
	        		+ " FROM GRAPH <"+graph+"> {?x ?y ?z}" 
	        		+ " WHERE {select distinct ?x ?y ?z " 
	        		+ " WHERE {?x ?y ?z. " 
	        		+ " FILTER (?x=<"+uri+">).} }";
			
	        VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create (sparql, set);
	
	        vur.exec();
			
			set.close();
		} else {
			logger.error("No se ha especificado la configuración de virtuoso");
		}
		
	}
	
	public static void deleteElement(String uri, String graph){
		if (USER.length() > 0) {
			VirtGraph set = new VirtGraph (CONECTION, USER, PASSWORD);
	        String sparql = "DELETE FROM GRAPH <"+graph+"> {?x ?y ?z}" +
					" WHERE {select distinct ?x ?y ?z " +
					" WHERE {?x ?y ?z. " +
					" FILTER (?x=<"+uri+">).} }";
			
//			String sparql = "DELETE FROM GRAPH <"+graph+"> {?x ?y ?z}" 
//				+ "WHERE {?x ?y ?z. "
//					+ "FILTER "
//					+ "(regex(str(?x),\"" + uri + "\")). "
//					+ "}";
			VirtuosoUpdateRequest vur = VirtuosoUpdateFactory.create (sparql, set);
	
	        vur.exec();
			
			set.close();
		} else {
			logger.error("No se ha especificado la configuración de virtuoso");
		}
		
	}
	
	public static void updateElement(String uri){
		deleteElement(uri);
		addElement(uri);
	}
	
	public static void updateElementTriple(String uri, String triples){
		deleteElement(uri);
		addElementTriple(uri, triples);
	}
	
	public static void updateElementTriple(String uri, String triples, String graph){
		deleteElement(uri, graph);
		addElementTriple(uri, triples, graph);
	}
	
	private static String makeHttpRequest(String url) throws IOException {
		 
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
 
		// optional default is GET
		con.setRequestMethod("GET");
		con.setRequestProperty("Accept-Charset", CharEncoding.UTF_8);
 
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), CharEncoding.UTF_8));

		String inputLine;
		StringBuilder response = new StringBuilder();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine+"\n");
		}
		in.close();
		
		return response.toString();
	}
	
	private static String makeHttpGet(String url) throws Exception{
		 
		HttpClient client = new HttpClient();
		
		GetMethod method = new GetMethod(url);
		if (Propiedades.getProxyHost().length() >0) {
			client.getHostConfiguration().setProxy(Propiedades.getProxyHost(), Integer.parseInt(Propiedades.getProxyPort()));
		}
		method.addRequestHeader(CheckeoParametros.USERAGENT, "ayto-zaragoza");
		method.addRequestHeader(CheckeoParametros.REFERER, "http://www.zaragoza.es/");
		
		String retorno = "";
		try {
			int status = client.executeMethod(method);
			if (status == 200) {
				retorno = new String(method.getResponseBody(), CharEncoding.UTF_8);
			} else {
				retorno = null;
			}
		} catch (HttpException e) {
			logger.error(e.getMessage());
			retorno = null;
		} catch (IOException e) {
			logger.error(e.getMessage());
			retorno = null;
		} finally {
			method.releaseConnection();
		}
		return retorno;
	}
	
	private static String getGraph(String uri){
		String graphUri = "";
		
		int i = uri.lastIndexOf('/');
		graphUri = uri.substring(0, i);
		
		return graphUri;
	}

	public static void loadJsonLd(String jsonld, String graph) throws Exception {
		if (USER.length() > 0) {
			VirtGraph set = null;
		    try {
		    	String obj = transformarN3(jsonld);
		    	
		    	if (obj == null || "".equals(obj.trim())) {
		    		throw new Exception("Error al tratar el fichero jsonld");
		    	} else {
			    	set = new VirtGraph (CONECTION, USER, PASSWORD);
			        String str = "INSERT INTO GRAPH <"+graph+"> { " + obj + " }";
			        VirtuosoUpdateRequest  vur = VirtuosoUpdateFactory.create(str, set);
			        vur.exec();
		    	}
			} finally {
		    	if (set != null) {
		    		set.close();
		    	}
		    }
		} else {
			logger.error("No se ha especificado la configuración de virtuoso");
		}

	}

	public static String transformarN3(String jsonld) throws JsonParseException, IOException {
		JsonLdOptions jsonLdOptions = new JsonLdOptions();
		jsonLdOptions.format = "application/n-quads";
		Object jsonObject = JsonUtils.fromString(jsonld);
		return JsonLdProcessor.toRDF(jsonObject, jsonLdOptions).toString();
	}
	
	
	public static void ejecutar(String str, String grafo) {
		VirtGraph set = new VirtGraph (grafo, CONECTION, USER, PASSWORD);
		
        VirtuosoUpdateRequest  vur = VirtuosoUpdateFactory.create(str, set);
        vur.exec();  
        
        set.close();
	}

}
