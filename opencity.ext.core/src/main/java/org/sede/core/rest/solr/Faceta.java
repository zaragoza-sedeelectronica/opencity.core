package org.sede.core.rest.solr;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.codec.CharEncoding;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.tag.Utils;
import org.sede.core.utils.Funciones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@XmlRootElement(name = "faceta")
public class Faceta {
	private static final Logger logger = LoggerFactory
					.getLogger(Faceta.class);
	private String name;
	private String etiqueta;
	private Integer cantidad;

	private List<ValorFaceta> values;

	public String getEtiqueta() {
		return faceta.get(this.getName()) == null ? this.getName() : faceta.get(this.getName());
	}

	public void setEtiqueta(String etiqueta) {
		this.etiqueta = etiqueta;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCantidad() {
		return cantidad;
	}

	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}

	public List<ValorFaceta> getValues() {
		return values;
	}

	public void setValues(List<ValorFaceta> values) {
		this.values = values;
	}

	@Override
	public String toString() {
		return "Faceta [name=" + name + ", etiqueta=" + etiqueta
						+ ", cantidad=" + cantidad + ", values=" + values + "]";
	}

	public static final String FACET_CATEGORY = "category";

	public static final String FACET_PROGRAMA = "programa_smultiple";
	public static final String FACET_FORMATO = "formatoActividad_smultiple";

	public static final String FACET_TEMA = "temas_smultiple";

	public static final String FACET_SUBTEMA = "subtemas_smultiple";
	public static final String FACET_TIPO_ACTUALIZACION = "tipoactualizacion_txt";
	public static final String FACET_CONTENTTYPE = "content_type";
	public static final String FACET_KEYWORDS = "keywords_smultiple";

	public static final String FACET_RANGOEDAD = "rangoEdad_smultiple";

	public static final String FACET_DIRIGIDO = "dirigidoa_smultiple";
	public static final String FACET_FORMAS_TRAMITACION = "formasTramitacion_smultiple";

	public static final String FACET_FRANJA = "franja_smultiple";
	public static final String FACET_BARRIO = "barrio_smultiple";
	public static final String FACET_JUNTA = "junta_smultiple";
	public static final String FACET_DISTRITO = "distrito_smultiple";

	public static final String FACET_ACCESIBLE = "accesible_b";
	public static final String FACET_GRATUITA = "gratuita_b";

	public static final String FACET_FINICIO = "fechaInicio_dt";
	public static final String FACET_FFINAL = "fechaFinal_dt";
	
	public static final String FACET_ORGANIZA = "organiza_smultiple";
	public static final String FACET_FECHAINICIOINSCRIPCION = "fechaInicioInscripcion_dt";
	public static final String FACET_FECHAFINALINSCRIPCION = "fechaFinalInscripcion_dt";
	
	public static final String FACET_DIASHASTAFINALIZACION = "diasHastaFinalizacion_i";
	
	public static final String FACET_ESTILO = "estilos_smultiple";

	public static final String FACET_PUBLICO = "publico_b";
	public static final String FACET_TITULARIDAD = "titularidad_smultiple";

	public static final String FACET_CATEGORIA = "categoria_smultiple";

	public static final String FACET_GRADOPROTECCION = "proteccion_smultiple";

	public static final String FACET_FECHA_MODIFICADO = "last_modified";

	public static final String FACET_AUTOR = "autor_smultiple";
	public static final String FACET_MATERIAL = "material_smultiple";
	public static final String FACET_ICONOGRAFIA = "iconografia_smultiple";
	public static final String FACET_EPOCA = "epoca_smultiple";

	public static final String FACET_FECHA = "fecha_dt";

	public static final String FACET_ESTACION = "estacion_smultiple";

	public static final String FACET_TIPOCOCINA = "tipococina_smultiple";
	public static final String FACET_PLATAFORMA = "plataforma_smultiple";

	public static final String FACET_DISPOSITIVO = "dispositivo_smultiple";
	public static final String FACET_OS = "os_smultiple";

	public static final String FACET_RANGO = "rango_smultiple";

	public static final String FACET_GEO = "geo_b";

	private static final HashMap<String, String> faceta = new HashMap<String, String>();

	public static final String FACET_ANYO = "anyo_i";

	public static final String FACET_PUBLICACION = "publicacion_smultiple";
	public static final String FACET_ESTADO = "estado_smultiple";
	

	public static final String FACET_IMAGEN = "imagen_s";

	public static final String FACET_BANCO_AMBITO = "ambito_s";
	public static final String FACET_BANCO_DURACION = "duracion_s";
	public static final String FACET_BANCO_ENTIDAD = "entity_s";
	
	
	static {
		faceta.put(FACET_TEMA, "Temas");
		faceta.put(FACET_CATEGORY, "Portal");
		faceta.put(FACET_PROGRAMA, "Programa");
		faceta.put(FACET_FORMATO, "Tipo de actividad");
		faceta.put(FACET_SUBTEMA, "Subtemas");
		faceta.put(FACET_TIPO_ACTUALIZACION, "Tipo de actualización");
		faceta.put(FACET_CONTENTTYPE, "Formato");
		faceta.put(FACET_KEYWORDS, "Palabras clave");
		faceta.put(FACET_DIRIGIDO, "Dirigido a");
		faceta.put(FACET_FORMAS_TRAMITACION, "Formas de tramitación");
		faceta.put(FACET_ACCESIBLE, "Sin Barreras");
		faceta.put(FACET_GRATUITA, "Gratuita");

		faceta.put(FACET_FRANJA, "Franja Horaria");
		faceta.put(FACET_BARRIO, "Barrio");
		faceta.put(FACET_JUNTA, "Junta");
		faceta.put(FACET_RANGOEDAD, "Rango de Edad");
		faceta.put(FACET_DISTRITO, "Distrito");
		faceta.put(FACET_FINICIO, "Inicio");
		faceta.put(FACET_FFINAL, "Final");
		faceta.put(FACET_ESTILO, "Estilo");
		faceta.put(FACET_PUBLICO, "Público");

		faceta.put(FACET_TITULARIDAD, "Titularidad");

		faceta.put(FACET_CATEGORIA, "Categoría");
		faceta.put(FACET_FECHA_MODIFICADO, "Modificado el");

		faceta.put(FACET_AUTOR, "Autor");
		faceta.put(FACET_MATERIAL, "Material");
		faceta.put(FACET_ICONOGRAFIA, "Iconografía");
		faceta.put(FACET_EPOCA, "Época");

		faceta.put(FACET_GRADOPROTECCION, "Grado de protección");

		faceta.put(FACET_FECHA, "Fecha");
		faceta.put(FACET_TIPOCOCINA, "Tipo de Cocina");
		faceta.put(FACET_PLATAFORMA, "Plataforma");
		faceta.put(FACET_DISPOSITIVO, "Dispositivo");

		faceta.put(FACET_OS, "Sistema");

		faceta.put(FACET_ESTACION, "Estación");
		faceta.put(FACET_RANGO, "Rango");

		faceta.put(FACET_GEO, "Geográfico");

		faceta.put(FACET_PUBLICACION, "Publicación");
		faceta.put(FACET_ANYO, "Año");
		faceta.put(FACET_ESTADO, "Estado");

		faceta.put(FACET_BANCO_AMBITO, "Ámbitos");
		faceta.put(FACET_BANCO_DURACION, "Duración");
		faceta.put(FACET_BANCO_ENTIDAD, "Entidades");
		
	}

	public static String getEtiqueta(String clave) {
		return faceta.get(clave) == null ? clave : faceta.get(clave);
	}

	public String getCollapsedList(int show) {
		return getLista(Funciones.getRequest(), "", show);
	}
	// TODO Eliminar tras actualizar Thymeleaf
	public String getCollapsedList(HttpServletRequest request, int show) {
		return getLista(request, "", show);
	}

	public String getLista() {
		int showAll = this.getValues().size();
		return getLista(Funciones.getRequest(), "", showAll);
	}
	
	// TODO Eliminar tras actualizar Thymeleaf
	public String getLista(HttpServletRequest request) {
		int showAll = this.getValues().size();
		return getLista(request, "", showAll);
	}
	public String getLista(String prefijoEnlace) {
		return getLista(Funciones.getRequest(), prefijoEnlace);
	}
	public String getLista(HttpServletRequest request, String prefijoEnlace) {
		int showAll = this.getValues().size();
		return getLista(request, prefijoEnlace, showAll);
	}
	public String getLista(String prefijoEnlace, int toShow) {
		return getLista(Funciones.getRequest(), prefijoEnlace, toShow);
	}
	public String getLista(HttpServletRequest request, String prefijoEnlace, int toShow) {
		StringBuilder xhtm = new StringBuilder();
		List<ValorFaceta> allFacetas = this.getValues();
		if (!allFacetas.isEmpty()) {
			boolean esBooleana = this.getName().contains("_b");
			xhtm.append("<ul class=\"" + (esBooleana ? "list-inline" : "list-group list-group-accordion") + "\">");
			if (esBooleana || toShow <= 0) {
				xhtm.append(valoresFacetaBlock(request, allFacetas, prefijoEnlace, esBooleana));
			} else {
				List<ValorFaceta> topNFacetas = allFacetas.subList(0, Math.min(toShow, allFacetas.size()));
				xhtm.append(valoresFacetaBlock(request, topNFacetas, prefijoEnlace, esBooleana));
				if (toShow < allFacetas.size()) {
					String collapsedId = String.format("%s-collapsed-%s", this.getName(), toShow);
					xhtm.append(verTodosBlock(collapsedId));
					xhtm.append("<li class=\"panel-collapse collapse\" id=\"" + collapsedId + "\" aria-expanded=\"false\" style=\"height: 0px;\">");
					xhtm.append("<ul class=\"list-group\">");
					xhtm.append(valoresFacetaBlock(request, allFacetas.subList(toShow, allFacetas.size()), prefijoEnlace, esBooleana));
					xhtm.append("</ul");
					xhtm.append("</li>");
				}
			}
			xhtm.append("</ul>");
		}

		return xhtm.toString();
	}

	private String verTodosBlock(String collapsedId) {
		return "<li class=\"panel-heading list-group-item\">\n" +
						String.format("<a class=\"accordion-toggle btn-block collapsed\" role=\"button\" aria-expanded=\"false\" aria-controls=\"collapseinc\" data-toggle=\"collapse\" data-parent=\"#accordion\" href=\"#%s\">\n", collapsedId) +
						"<span>Ver todos</span>\n" +
						"</a>\n" +
						"</li>";
	}

	private String facetaSeleccionadaBlock(HttpServletRequest request, String liClass, ValorFaceta valor) {
		return "<li class=\"" + liClass + "\">" + valor.getNameParsed() + " <span class=\"badge\">" + valor.getCount() + "</span><a href=\"" + this.enlaceEliminarFaceta(valor.getName(), request) + "\" class=\"unstyled\"><img src=\"//www.zaragoza.es/cont/gcz/img/dessselecionado_NP.gif\" alt=\"Eliminar valor\"/></a></li>";
	}

	private StringBuilder valoresFacetaBlock(HttpServletRequest request, List<ValorFaceta> facetas, String prefijoEnlace, boolean esBooleana) {
		StringBuilder xhtm = new StringBuilder();
		for (ValorFaceta v : facetas) {
			String liClass = esBooleana ? "" : "list-group-item";
			if (isFacetaSeleccionada(v, request)) {
				xhtm.append(facetaSeleccionadaBlock(request, liClass, v));
			} else {
				xhtm.append(facetaNoSeleccionadaBlock(request, prefijoEnlace, v, liClass));
			}
		}
		return xhtm;
	}

	private String facetaNoSeleccionadaBlock(HttpServletRequest request, String prefijoEnlace, ValorFaceta v, String liClass) {
		return "<li class=\"" + liClass + "\"><a href=\"" + trataEnlace(v, request, prefijoEnlace) + "\">" + v.getNameParsed() + " <span class=\"badge\">" + v.getCount() + "</span></a></li>";
	}
	public String enlaceEliminarFaceta(String valorFaceta) {
		return enlaceEliminarFaceta(valorFaceta, Funciones.getRequest());
	}
	public String enlaceEliminarFaceta(String valorFaceta, HttpServletRequest request) {
		try {
			
			StringBuilder queryString = generarQueryString(request);
			
			String facetaName = URLEncoder.encode(this.getName(), CharEncoding.UTF_8) + escaparEnlace(valorFaceta);
			String fq = URLEncoder.encode(request.getParameter(CheckeoParametros.PARAMFQ), CharEncoding.UTF_8);
			
			fq = fq.replace("+AND+" + facetaName, "");
			fq = fq.replace(facetaName + "+AND+", "");
			fq = fq.replace(facetaName, "");
			fq = CheckeoParametros.PARAMFQ + "=" + fq;
			return "?" + fq + (request.getParameter(CheckeoParametros.PARAMQUERYSOLR) == null ? "" : "&" + CheckeoParametros.PARAMQUERYSOLR + "=" + request.getParameter(CheckeoParametros.PARAMQUERYSOLR) + queryString);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	private boolean isFacetaSeleccionada(ValorFaceta v, HttpServletRequest request) {
		if (request.getParameter(CheckeoParametros.PARAMFQ) != null && request.getParameter(CheckeoParametros.PARAMFQ).trim().length() > 0) {
			String[] facetas = request.getParameterValues(CheckeoParametros.PARAMFQ);
			for (String facetaStr : facetas) {
				if (facetaStr.contains(this.getName()) && facetaStr.contains(Utils.toUnicode(v.getName()))) {
					return true;
				}
			}
		}
		return false;
	}

	private String trataEnlace(ValorFaceta v, HttpServletRequest request, String prefijoEnlace) {
		String fq;
		
		try {
			StringBuilder queryString = generarQueryString(request);
			
			if (request.getParameter(CheckeoParametros.PARAMFQ) != null && request.getParameter(CheckeoParametros.PARAMFQ).trim().length() > 0) {
				fq = CheckeoParametros.PARAMFQ + "=" + URLEncoder.encode(request.getParameter(CheckeoParametros.PARAMFQ), CharEncoding.UTF_8) + " AND " + this.getName() + escaparEnlace(v.getName());
			} else {
				fq = CheckeoParametros.PARAMFQ + "=" + this.getName() + escaparEnlace(v.getName());
			}
			return prefijoEnlace + "?" + fq + (request.getParameter(CheckeoParametros.PARAMQUERYSOLR) == null ? "" : "&" + CheckeoParametros.PARAMQUERYSOLR + "=" + request.getParameter(CheckeoParametros.PARAMQUERYSOLR) + queryString);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
			return "";
		}
	}

	public static String escaparEnlace(String valor) throws UnsupportedEncodingException {
		return URLEncoder.encode(":(\"" + Utils.toUnicode(valor) + "\")", CharEncoding.UTF_8);
	}

	private StringBuilder generarQueryString(HttpServletRequest request) {
		StringBuilder queryString = new StringBuilder();
		Enumeration<String> parameterNames = request.getParameterNames();
		while (parameterNames.hasMoreElements()) {
			String paramName = parameterNames.nextElement();
			if (!CheckeoParametros.PARAMFQ.equals(paramName) && !CheckeoParametros.PARAMQUERYSOLR.equals(paramName)) {
				for (String s : request.getParameterValues(paramName)) {
					queryString.append("&" + paramName + "=" + s);
				}
			}
		}
		return queryString;
	}
}
