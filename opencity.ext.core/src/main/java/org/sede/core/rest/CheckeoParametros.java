package org.sede.core.rest;

import java.util.HashMap;
import java.util.Map;

public class CheckeoParametros {
	private CheckeoParametros() {
		super();
	}
	public static final String PARAMID = "id";
	public static final String PARAMFECHA = "fecha";
	public static final String HEADERNUMFOUND = "totalCount";
	public static final String PARAMSHOWALL = "showAll";
	public static final String PARAMROWS = "rows";
	public static final String PARAMSTART = "start";
	public static final String PARAMSORT = "sort";
	
	public static final String PARAMRESULT = "result";
	
	public static final String PARAMFILTROCAMPOS = "fl";
	public static final String PARAMQUERY = "q";
	public static final String PARAMQUERYSOLR = "query";
	public static final String PARAMFQ = "fq";
	public static final String ROWS = "50";
	public static final Integer MAXROWS_RETURNED = 500;
	public static final String START = "0";
	
	
	public static final String HEADERPASSWORD = "X-password";
	public static final String HEADERCLIENTID = "clientId";
	public static final String HEADERHMAC = "HmacSHA1";
	
	public static final String HEADERLASTMODIFIED = "Last-Modified";
	
	public static final String ERROR = "error-msg";
	
	public static final String REFRESHPARAMETER = "refresh";
	
	public static final String PARAMSRS = "srsname";
	public static final String SRSWGS84 = "wgs84";
	public static final String SRSETRS89 = "utm30n_etrs89";
	public static final String SRSUTM30N = "utm30n";
	
	public static final String PARAMREMOVEPROPERTIES = "removeproperties";
	public static final String PARAMRESPUESTA = "rf";
	public static final String RESPUESTAHTML = "html";
	public static final String RESPUESTAMARKDOWN = "markdown";
	
	public static final String RESULTSONLY = "results_only";
	public static final String DEBUG = "debug";
	public static final String TM_ERROR = "txtMsgerrorPlantilla";
	public static final String TM_ERROR_STACK = "txtStackerrorPlantilla";
	public static final String TEST = "test";
	public static final String LOCALE = "locale";
	
	public static final String GEORSS_ICON = "georss_icon";

	public static final String PARAMPUNTOQUERYSOLR = "point";
	
	public static final String AMP = "amp_content";

	public static final String PARAMDISTANCEQUERYSOLR = "distance";
	public static final String DISTANCE = "500";
	public static final String ATTRPETICION = "attr_peticion";
	public static final String SESSIONCIUDADANO = "sess_ciudadano";
	public static final String SESSIONGCZ = "sess_risp";
	public static final String ACCEPTHEADER = "Accept";
	public static final String CONTENT_TYPE = "Content-Type";
	public static final String ACTUAL_CACHE = "actual_cache";
	public static final String NOCACHE = "nocache";
	public static final String REQUIREMENTESATTR = "userRequirements";
	
	public static final String USERAGENT = "User-Agent";
	public static final String REFERER = "Referer";
	
	public static int string2IntConValorPorDefecto(String valor, int valorPorDefecto) {
		if (valor == null) {
			return valorPorDefecto;
		} else {
			return Integer.parseInt(valor);
		}
		
	}
	public static final Map<String, Parameter> PARAMS = new HashMap<String, Parameter>() { {
		put(CheckeoParametros.PARAMID, new Parameter(CheckeoParametros.PARAMID, "Identificador del registro", "integer", "query"));
		put(CheckeoParametros.HEADERNUMFOUND, new Parameter(CheckeoParametros.HEADERNUMFOUND, "Numero de registros encontrados", "String", "query"));
		put(CheckeoParametros.PARAMROWS, new Parameter(CheckeoParametros.PARAMROWS, "Numero de filas que se obtendran", "integer", "query"));
		put(CheckeoParametros.PARAMSTART, new Parameter(CheckeoParametros.PARAMSTART, "Posicion del primer registro que se obtendra", "integer", "query"));
		put(CheckeoParametros.PARAMSORT, new Parameter(CheckeoParametros.PARAMSORT, "Ordenacion de los campos", "string", "query"));
		put(CheckeoParametros.PARAMFILTROCAMPOS, new Parameter(CheckeoParametros.PARAMFILTROCAMPOS, "Filtro de campos a obtener", "String", "query"));
		put(CheckeoParametros.PARAMQUERY, new Parameter(CheckeoParametros.PARAMQUERY, "Consulta con sintaxis <a href=\"http://tools.ietf.org/pdf/draft-nottingham-atompub-fiql-00.pdf\" target=\"_blank\">FIQL</a>", "string", "query"));
		put(CheckeoParametros.PARAMQUERYSOLR, new Parameter(CheckeoParametros.PARAMQUERYSOLR, "Consulta con sintaxis <a href=\"http://wiki.apache.org/solr/SolrQuerySyntax\" target=\"_blank\">SOLR</a>", "string", "query"));
		put(CheckeoParametros.PARAMPUNTOQUERYSOLR, new Parameter(CheckeoParametros.PARAMPUNTOQUERYSOLR, "Punto a partir del cual se desea obtener registros, ejemplo según sistema de referencia:<br/><strong>utm30n.</strong> 676840.375,4613966.0<br/><strong>wgs84.</strong> -0.8774209607549572,41.65599799116259", "string", "query"));
		put(CheckeoParametros.PARAMDISTANCEQUERYSOLR, new Parameter(CheckeoParametros.PARAMDISTANCEQUERYSOLR, "Distancia (en metros) a la que se encuentran los resultados", "string", "query"));
		put(CheckeoParametros.PARAMREMOVEPROPERTIES, new Parameter(CheckeoParametros.PARAMREMOVEPROPERTIES, "Eliminar propiedades obsoletas", "string", "query"));
		}};
	// Constantes ciudades abiertas
	public static final int defaultPage=1;
	public static final String PAGE = "page";
	public static final String PAGESIZE = "pageSize";
	public static final int defaultGroupByPageSize=100;
	public static final int maxGroupbyPageSize=200;
	public static final String SELF = "self";
	public static final String NEXT = "next";
	public static final String PREV = "prev";
	public static final String FIRST = "first";
	public static final String LAST = "last";
	public static final String PAGERECORDS = "pageRecords";
	public static final String TOTALRECORDS = "totalRecords";
	public static final String RESPONSEDATE = "responseDate";
	public static final String CONTENT_MD5= "Content-MD5";
	public static final String STATUS = "status";
	public static final String X = "X";
	public static final String Y = "Y";
	public static final String XETRS89 = "xETRS89";
	public static final String YETRS89 = "yETRS89";
	public static final String METERS = "meters";
	public static final String AJAX_PARAM = "_";
	public static final String FIELDS =  "fields";
	public static final String RSQL_Q =  "q";
	public static final String SORT =  "sort";
	public static final String SRID =  "srId";
}
