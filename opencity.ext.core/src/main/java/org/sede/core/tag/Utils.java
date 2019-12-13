package org.sede.core.tag;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import com.google.common.collect.Ordering;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.sede.core.anotaciones.Permisos;
import org.sede.core.entity.CalendarDay;
import org.sede.core.geo.Punto;
import org.sede.core.utils.ConvertDate;
import org.sede.core.utils.Funciones;
import org.sede.core.utils.Propiedades;
import org.sede.servicio.acceso.dao.Autorizacion;
import org.sede.servicio.acceso.entity.Credenciales;
//import org.sede.servicio.actividades.entity.ActoLugarHorario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {
	
	private static final Logger logger = LoggerFactory.getLogger(Utils.class);
	public static String getDate(Date inicio) {
		return ConvertDate.date2String(inicio, ConvertDate.DATE_FORMAT);
	}

	public static Date getFecha(String fecha) {
		try {
			return ConvertDate.string2Date(fecha, ConvertDate.DATE_FORMAT);
		} catch (ParseException e) {
			return null;
		}
	}
	public static boolean desde(String fecha) {
		return desde(fecha, "00:01");
	}
	public static boolean desde(String fecha, String hora) {
		try {
			return desde(ConvertDate.string2Date(fecha + " " + hora, ConvertDate.DATETIME_FORMAT));
		} catch (ParseException e) {
			return false;
		}
	}
	public static boolean desde(Date fecha) {
		return (new Date().after(fecha));
	}
	
	public static boolean hasta(String fecha) {
		return hasta(fecha, "23:59");
	}
	public static boolean hasta(String fecha, String hora) {
		try {
			return hasta(ConvertDate.string2Date(fecha + " " + hora, ConvertDate.DATETIME_FORMAT));
		} catch (ParseException e) {
			return false;
		}
	}
	public static boolean hasta(Date fecha) {
		return (new Date().before(fecha));
	}
	public static String getLinkLoginUsuario(HttpServletRequest request) throws UnsupportedEncodingException {
		String q = "";
		if (request.getQueryString() != null) {
			q = "?" + request.getQueryString();
		}
		
		return Funciones.getPathSecure(request) + "/acceso?r=" + URLEncoder.encode(request.getRequestURI() + q, CharEncoding.UTF_8);
	}
	public static String getLinkCloseSession(HttpServletRequest request) throws UnsupportedEncodingException {
		return "/opencityext/acceso/salir?r=" + URLEncoder.encode(request.getRequestURI(), CharEncoding.UTF_8);
	}
	// Para incluir el día actual se debe marcar el día siguiente
	public static boolean enPlazo(Date inicio, Date fin) {
		if (inicio == null || fin == null) {
			return false;
		} else {
			Date ahora = new Date();
			if (fin.after(ahora) && inicio.before(ahora)) {
				return true;
			}
		}
		
		return false;
	}
	public static boolean enPlazoIncluyendoDiaFin(Date inicio, Date dFinal) {
		
		if (inicio == null || dFinal == null) {
			return false;
		} else {
			Date ahora = new Date();
			
			Calendar c = Calendar.getInstance();
			c.setTime(dFinal);
			c.set(Calendar.HOUR_OF_DAY, 23);
			c.set(Calendar.MINUTE, 59);
			Date fin = c.getTime();
			if (fin.after(ahora) && inicio.before(ahora)) {
				return true;
			}
		}
		
		return false;
	}
	public static boolean enPlazoSiNoNulo(Date inicio, Date fin) {
		if (inicio == null && fin == null) {
			return true;
		} else {
			Date ahora = new Date();
			if (fin == null && inicio.before(ahora)) {
				return true;
			} else if (inicio == null && fin.after(ahora)) {
				return true;
			} else if (inicio != null && fin != null && fin.after(ahora) && inicio.before(ahora)) {
				return true;
			}
		}
		
		return false;
	}
	public static boolean esFuturo(Date hoy, Date fecha) {
		if (fecha == null) {
			return false;
		}
		return hoy.before(fecha);
	}
	
	public static boolean esHoy(Date hoy, Date fecha) {
		if (fecha == null) {
			return false;
		}
		return DateUtils.isSameDay(hoy, fecha);
	}

	public static boolean esManyana(Date hoy, Date fecha) {
		Calendar c = Calendar.getInstance();
		c.setTime(hoy);
		c.add(Calendar.DATE, 1);
		return esHoy(c.getTime(), fecha);
	}
	public static String decode(String txt) {
		return StringEscapeUtils.unescapeJava(txt);
	}
	public static boolean esAnyoActual(Date fecha) {
		if (fecha == null) {
			return false;
		} else {
			int anyoActual = Calendar.getInstance().get(Calendar.YEAR);
			Calendar c = Calendar.getInstance();
			c.setTime(fecha);
			if (anyoActual == c.get(Calendar.YEAR)) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean esSemanaActual(Date fecha) {
		int semanaActual = Calendar.getInstance().get(Calendar.WEEK_OF_MONTH);
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		return (semanaActual == c.get(Calendar.WEEK_OF_MONTH) && esMesActual(fecha));
	}

	public static boolean esMesActual(Date fecha) {
		int mesActual = Calendar.getInstance().get(Calendar.MONTH);
		Calendar c = Calendar.getInstance();
		c.setTime(fecha);
		if (mesActual == c.get(Calendar.MONTH)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Calcula la diferencia de tiempo entre el momento actual y un instante concreto
	 * @return Cadena en lenguaje natural con una aproximación de la diferencia
	 */
	public static String diferenciaTiempo(Date instant) {
		Date now = new Date();
		
		long diffMillis = now.getTime() - instant.getTime();
		boolean past = true;
		if (diffMillis < 0) {
			past = false;
			diffMillis = -diffMillis;
		}
		
		long diffMinutes = diffMillis / (1000 * 60);
		long diffHours = diffMinutes / 60;
		long diffDays = diffHours / 24;
		long diffMonths = diffDays / 30;
		long diffYears = diffMonths / 12;
		
		String msg = past ? "hace " : "dentro de ";
		if (diffDays == 0) {
			if (diffHours == 0) {
				msg += diffMinutes + " minuto";
				msg += diffMinutes > 1 ? "s" : "";
			}
			else {
				msg += diffHours + " hora";
				msg += diffHours > 1 ? "s" : "";
			}
			return msg;
		}
		
		if (esManyana(now, instant)) {
			return "mañana";
		}
		if (esManyana(instant, now)) {
			return "ayer";
		}
		
		if (diffYears > 0) {
			msg += diffYears + " año";
			msg += diffYears > 1 ? "s" : "";
			return msg;
		}
		
		if (diffMonths > 0) {
			msg += diffMonths + " mes";
			msg += diffMonths > 1 ? "es" : "";
			return msg;
		}
		
		msg += diffDays + " día";
		msg += diffDays > 1 ? "s" : "";
		
		return msg;
	}

//	public static StringBuilder tratarHorarios(Set<ActoLugarHorario> horarios) {
//		StringBuilder xhtm = new StringBuilder();
//		List<ActoLugarHorario> horariosSortList = new ArrayList<ActoLugarHorario>(horarios);
//		Collections.sort(horariosSortList, new Comparator<ActoLugarHorario>() {
//			private final Ordering<String> diasOrdenados = Ordering.explicit("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo");
//			@Override
//			public int compare(final ActoLugarHorario object1, final ActoLugarHorario object2) {
//				int compare = diasOrdenados.compare(object1.getDayOfWeek(), object2.getDayOfWeek());
//				if(compare != 0){
//					return compare;
//				}
//				return object1.getStartTime().compareTo(object2.getStartTime());
//			}
//		});
//		LinkedHashMap<String,String> map = new LinkedHashMap<String, String>();
//		for (ActoLugarHorario h : horariosSortList){
//			String startTime = h.getStartTime() != null ? "<span property=\"startTime\">" + h.getStartTime() + "</span>" : null;
//			String endTime = h.getEndTime() != null ? "<span property=\"endTime\">" + h.getEndTime() + "</span>" : null;
//			if(map.containsKey(h.getDayOfWeek())){
//				String value = map.get(h.getDayOfWeek()) + (h.getEndTime() != null ? " y de " + startTime + " a " + endTime + " h." : " y a las " + startTime);
//				map.put(h.getDayOfWeek(), value);
//			} else {
//				map.put(h.getDayOfWeek(), (h.getEndTime() != null ? " de " + startTime + " a " + endTime + " h." : " a las " + startTime + " h."));
//			}
//		}
//		if(!map.isEmpty()) { xhtm.append("<ul>"); }
//		for (Map.Entry<String, String> entry : map.entrySet()) {
//			xhtm.append("<li>" + entry.getKey() + entry.getValue() + "</li>");
//		}
//		if(!map.isEmpty()) { xhtm.append("</ul>"); }
//		return xhtm;
//	}

	public static StringBuilder tratarFechas(Date inicio, Date fin) {
		if (inicio == null && fin == null) {
			return null;
		} else {
			Date hoy = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			try {
				Date today = dateFormat.parse(dateFormat.format(new Date()));
				if (inicio != null && inicio.before(today) && fin != null && fin.before(today)) {
					if (inicio.equals(fin)) {
						return new StringBuilder(fechaEnTexto(inicio));
					} else {
						StringBuilder xml = new StringBuilder();
						xml.append(ConvertDate.date2String(inicio, ConvertDate.DATE_FORMAT));
						xml.append(" al ");
						xml.append(ConvertDate.date2String(fin, ConvertDate.DATE_FORMAT));
						return xml;
					}
				}
			} catch (ParseException e) {
				;
			}
	
			StringBuilder xhtm = new StringBuilder();
			if (inicio != null && inicio.equals(fin)) {
				if (esHoy(hoy, inicio)) {
					xhtm.append("hoy");
				} else if (esManyana(hoy, fin)) {
					xhtm.append("mañana");
				} else {
					xhtm.append(fechaEnTexto(inicio));
				}
			} else if (inicio == null) {
				if (esManyana(hoy, fin)) {
					xhtm.append("hasta mañana");
				} else {
					if (esHoy(hoy, fin)) {
						xhtm.append("hasta hoy");
					} else {
						String ret = "hasta el " + fechaEnTexto(fin);
						xhtm.append(ret.replace("el  este", "el"));
					}
				}
			} else {
				if (inicio.before(hoy)) {
					if (esManyana(hoy, fin)) {
						xhtm.append("hasta mañana");
					} else {
						if (esHoy(hoy, fin)) {
							xhtm.append("hasta hoy");
						} else {
							String ret = "hasta el " + fechaEnTexto(fin);
							xhtm.append(ret.replace("el  este", "el"));
						}
					}
				} else {
					if (mismoMes(inicio,fin)) {
						xhtm.append(ConvertDate.date2String(inicio, ConvertDate.TEXTO_DIA_NUM));
						xhtm.append(" al");
						xhtm.append(ConvertDate.date2String(fin, ConvertDate.TEXTO_DIA_DE_MES));
					} else {
						String f = fechaEnTexto(fin);
						String i = fechaEnTexto(inicio);
						if (mismoMes(inicio,fin)) {
							i = i.replace("de " + ConvertDate.date2String(inicio, ConvertDate.TEXTO_MES) + " ", "");
						}
						xhtm.append(i);
						if (f.indexOf("este") >= 0) {
							xhtm.append(" a ");
						} else {
							xhtm.append(" al ");
						}
						xhtm.append(f);
					}
				}
			}
			return xhtm;
		}
	}
	private static boolean mismoMes(Date inicio, Date fin) {
		if (fin == null) {
			return false;
		}
		Calendar c = Calendar.getInstance();
		c.setTime(inicio);
		Calendar c1 = Calendar.getInstance();
		c1.setTime(fin);
		
		return (c.get(Calendar.MONTH) == c1.get(Calendar.MONTH) && c.get(Calendar.YEAR) == c1.get(Calendar.YEAR));
	}

	private static String fechaEnTexto(Date fin) {
		if (esAnyoActual(fin)) {
			if (esMesActual(fin)) {
				if (esSemanaActual(fin)) {
					return " este " + ConvertDate.date2String(fin, ConvertDate.TEXTO_DIA);
				} else {
					return ConvertDate.date2String(fin, ConvertDate.TEXTO);
				}
			} else {
				return ConvertDate.date2String(fin, ConvertDate.TEXTO_DIA_DE_MES);
			}
		} else {
			return ConvertDate.date2String(fin, ConvertDate.TEXTO_YEAR_SIN_DIA);
		}
	}

	public static String getCalendar(String fieldName, List<CalendarDay> dias, String mesStr, String anyStr, HttpServletRequest request) {
		return getCalendar(false, null, null, fieldName, dias, mesStr, anyStr, request);
	}

	public static String getCalendar(String queryParams, String fieldName, List<CalendarDay> dias, String mesStr, String anyStr, HttpServletRequest request) {
		return getCalendar(false, null, queryParams, fieldName, dias, mesStr, anyStr, request);
	}

	public static String getCalendar(boolean dayBox, String folder, String queryParams, String fieldName, List<CalendarDay> dias, String mesStr, String anyStr, HttpServletRequest request) {
		final String MONTH_PARAM = "month";
		try {
			StringBuilder queryString = new StringBuilder();
			@SuppressWarnings("unchecked")
			Enumeration<String> parameterNames = request.getParameterNames();
			while (parameterNames.hasMoreElements()) {
				String paramName = parameterNames.nextElement();
				if (!fieldName.equals(paramName) && !MONTH_PARAM.equals(paramName)) {
					String[] paramValues = request.getParameterValues(paramName);
					for (int i = 0; i < paramValues.length; i++) {
						queryString.append("&" + paramName + "=" + paramValues[i]);
					}
				}
			}
			// queryParams adicionales a la petición y al campo fecha de filtro
			if(queryParams != null){
				queryString.append(!StringUtils.isEmpty(queryString) ? "&" + queryParams : queryParams);
			}
			int anyAnterior;
			int mesAnterior;
			int anyPosterior;
			int mesPosterior;
			Date fecha = ConvertDate.string2Date("01-" + mesStr + "-" + anyStr, ConvertDate.DATE_FORMAT);
			int mes = Integer.parseInt(mesStr);
			int mesCalendar = mes - 1;
			int any = Integer.parseInt(anyStr);
			if (mes == 1) {
				mesAnterior = 12;
				anyAnterior = any - 1;
				mesPosterior = 2;
				anyPosterior = any;
			} else if (mes == 12) {
				mesAnterior = 11;
				anyAnterior = any;
				mesPosterior = 1;
				anyPosterior = any + 1;
			} else {
				mesAnterior = mes - 1;
				anyAnterior = any;
				mesPosterior = mes + 1;
				anyPosterior = any;
			}

			int diaAct = 1;
			Calendar a = Calendar.getInstance();
			int diaMes = a.get(Calendar.DAY_OF_MONTH);
			a.set(any, mesCalendar, diaAct);

			int diaSeleccionado = 0;
			Calendar b = Calendar.getInstance();
			if(request.getParameter(fieldName) != null && !"".equals(request.getParameter(fieldName))){
				Date fechaSelect = ConvertDate.string2Date(request.getParameter(fieldName), ConvertDate.DATE_FORMAT);
				b.setTime(fechaSelect);
				diaSeleccionado = b.get(Calendar.DAY_OF_MONTH);
			}

			StringBuilder xhtm = new StringBuilder();

			String cajaDia = "<div class=\"calendar-day\">"
					+ "<div class=\"header\">" + (request.getParameter(fieldName) != null ? "DÍA" : "HOY") + "</div>"
					+ "<div class=\"body\">" + (diaSeleccionado == 0 ? diaMes : diaSeleccionado) + "</div>"
					+ "<div class=\"footer\">" + ConvertDate.getMes(a.get(Calendar.MONTH)) + "</div>"
					+ "</div>";

			xhtm.append((dayBox ? cajaDia : "")
					+ "<table id=\"calendar\" class=\"table table-condensed table-calendar\">"
					+ "<caption class=\"text-center\">"
					+ "<a class=\"selector\" href=\"" + (folder == null ? "" : folder) + "?" + fieldName + "=01-" + mesAnterior + "-" + anyAnterior + queryString.toString() + "\" title=\"Mostrar mes anterior\">&#171;</a> "
					+ "<span class=\"mes\">" + StringUtils.capitalize(ConvertDate.date2String(fecha, ConvertDate.FORMATTEXTO)) + "</span> "
					+ "<a class=\"selector\" href=\"" + (folder == null ? "" : folder) + "?" + fieldName + "=01-" + mesPosterior + "-" + anyPosterior + queryString.toString() + "\" title=\"Mostrar mes siguiente\">&#187;</a>"
					+ "</caption>"
					+ "<tr><th id=\"c1\"><abbr title=\"lunes\">L</abbr></th><th id=\"c2\"><abbr title=\"martes\">M</abbr></th><th id=\"c3\"><abbr title=\"miércoles\">X</abbr></th><th id=\"c4\"><abbr title=\"jueves\">J</abbr></th><th id=\"c5\"><abbr title=\"viernes\">V</abbr></th><th id=\"c6\"><abbr title=\"sábado\">S</abbr></th><th id=\"c7\"><abbr title=\"domingo\">D</abbr></th></tr>");

			for (int j = 0; j < 7; j++) {
				xhtm.append("<tr>");
			  	int l;
				l = a.get(Calendar.DAY_OF_WEEK);
				l = (l - 1);
				if (l == 0) {
					l = 7;
				}
				for (int h = 1; h < l; h++) {
					xhtm.append("<td class=\"Vacio\" headers=\"c" + h + "\">&nbsp;</td>");
				}
				for (int i = l; i <= 7; i++) {
					if (Funciones.isDate(diaAct, mesCalendar, any)) {
						if (Funciones.tieneValorDia(diaAct, dias)) {
			    			xhtm.append("<td headers=\"c" + i + "\">"
			    					+ "<a href=\"" + (folder == null ? "" : folder) + "?" + fieldName + "=" + diaAct + "-" + mes + "-" + any + queryString.toString() + "\">"
									+ "<span class=\"day "
									+ (diaMes == diaAct && mesCalendar == Calendar.getInstance().get(Calendar.MONTH) ? "today " : "")
									+ (diaSeleccionado == diaAct ? "select" : "")
									+ "\">" + diaAct + "</span>"
									+ "</a></td>");
			    		} else {
			    			xhtm.append("<td headers=\"c" + i + "\">"
									+ "<span class=\"day "
									+ (diaMes == diaAct && mesCalendar == Calendar.getInstance().get(Calendar.MONTH)  ? "today " : "")
									+ (diaSeleccionado == diaAct ? "select" : "")
									+ "\">" + diaAct + "</span>"
									+ "</td>");
						}
					} else {
						xhtm.append("<td class=\"Vacio\" headers=\"c" + i + "\">&nbsp;</td>");
						j = 10;
					}	
					diaAct++;
					a.set(any, mesCalendar, diaAct);
				}
			 xhtm.append("</tr>");
			}
			xhtm.append("</table>");
			return xhtm.toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return "";
		}
	}	

	public static String editContent(Credenciales credenciales, String plantilla, String pagina) {
		if (credenciales.getUsuario().getPropiedades() != null 
				&& credenciales.getUsuario().getPropiedades().containsKey("carpeta")
				&& ("/".equals(credenciales.getUsuario().getPropiedades().get("carpeta"))
						|| pagina.contains("portal" + credenciales.getUsuario().getPropiedades().get("carpeta"))
						)) {
			StringBuilder xhtm = new StringBuilder();
			List<String> permisosPaginas = Autorizacion.obtenerPermisosSeccion(credenciales, "CONTENIDOS", "PAGINAS");
			List<String> permisosPlantilla = Autorizacion.obtenerPermisosSeccion(credenciales, "CONTENIDOS", "PLANTILLAS");
			xhtm.append("<link rel=\"stylesheet\" href=\"//www.zaragoza.es/cont/plantillas/js/jquery-ui-1.11.3.min.css\"/>"
			+ "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.24.2/codemirror.css\"/>"
			+ "<link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.24.2/addon/dialog/dialog.min.css\"/>"
			+ "<script src=\"https://cdn.ckeditor.com/4.6.2/full-all/ckeditor.js\"></script>"
			+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/ckeditor/4.6.2/plugins/lineutils/plugin.js\"></script>"
			
			+ "<script src=\"/cont/vistas/servicio/contenido/contenido.js\"></script>"
			
			+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.24.2/codemirror.js\"></script>"
			+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.24.2/mode/xml/xml.js\"></script>"
			+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.24.2/addon/search/search.js\"></script>"
			+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.24.2/addon/search/searchcursor.min.js\"></script>"
			+ "<script src=\"https://cdnjs.cloudflare.com/ajax/libs/codemirror/5.24.2/addon/dialog/dialog.min.js\"></script>");

			xhtm.append("<div class=\"editor-tool hidden-print\" style=\"background:#000;z-index:1010;position:fixed;right:0;left:0;margin-right:auto;margin-left:auto;min-height:3.5em;width:20em;text-align:center;border-radius:0px 0px 6px 6px;\">"
			+ "<div class=\"h3 nomargin fa-inverse\">Editor de página</div>");
			if (permisosPaginas.contains(Permisos.NEW)) {
				xhtm.append("<a data-toggle=\"modal\" data-target=\"#edicion\" href=\"/opencityext/servicio/contenido/\" title=\"Crear Página\" class=\"btn btn-xs\"><i class=\"fa fa-2x fa-inverse fa-plus-circle\" aria-hidden=\"true\"></i></a> ");
			}
			if (permisosPaginas.contains(Permisos.MOD)) {
				xhtm.append("<a data-toggle=\"modal\" data-target=\"#edicion\" href=\"/opencityext/servicio/contenido/?path=" + pagina + "\" title=\"Modificar Página\" class=\"btn btn-xs\"><i class=\"far fa-2x fa-inverse fa-edit\" aria-hidden=\"true\"></i></a> ");
			}
			if (permisosPaginas.contains(Permisos.DEL)) {
				xhtm.append("<a onclick=\"return confirm('¿Confirma que desea eliminar " + pagina + "?')\" href=\"/opencityext/servicio/contenido/remove/?path=" + pagina + "\" title=\"Eliminar Página\" class=\"btn btn-xs\"><i class=\"fa fa-2x fa-inverse fa-trash\" aria-hidden=\"true\"></i></a> ");
			}
			if (permisosPlantilla.contains(Permisos.MOD)) {
				xhtm.append("<a data-toggle=\"modal\" data-target=\"#edicion\" href=\"/opencityext/servicio/contenido/?path=" + plantilla + "\" title=\"Editar Plantilla\" class=\"btn btn-xs\"><i class=\"fas fa-2x fa-inverse fa-file-alt\" aria-hidden=\"true\"></i></a>");
			}
			if (permisosPlantilla.contains(Permisos.PUB)) {
				xhtm.append("<a data-toggle=\"modal\" data-target=\"#indizar\" title=\"Indizar\" class=\"btn btn-xs\" onclick=\"procesarPagina()\"><i class=\"fas fa-2x fa-inverse fa-cloud-upload-alt\" aria-hidden=\"true\"></i></a>");
			}
			xhtm.append("</div>");
			// Sobreescribe el z-index: 1040 al no estar el modal al final del body (fallo en los ancestros con position)
			xhtm.append("<style>.modal-backdrop{z-index:1000;}</style>");
			xhtm.append("<div class=\"modal\" id=\"edicion\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"
						    + "<div class=\"modal-dialog modal-lg\">"
					        	+ "<div class=\"modal-content\">"
					        	+ "</div>"
					        + "</div>"
					    + "</div>"
					    + "<div class=\"modal\" id=\"indizar\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">"
						    + "<div class=\"modal-dialog modal-lg\">"
					        	+ "<div class=\"modal-content\">"
					        		+ "<div class=\"modal-header\">"
					        		+ "<button type=\"button\" class=\"close\" data-dismiss=\"modal\" aria-hidden=\"true\">&times;</button>"
					                + "<h4 class=\"modal-title\">Indizar contenido</h4>"
					                + "<div id=\"respuesta\"></div>"
					        		+ "</div>"
				        			+ "<div class=\"modal-body\" id=\"indizarBody\">"
					        			+ "<form method=\"post\" id=\"indizarForm\" action=\"#\">"
						        			+ "<div class=\"form-group\">"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_uri\">URI</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"uri\" id=\"ind_uri\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_title\">title</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"title\" id=\"ind_title\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_description\">description</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"description\" id=\"ind_description\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_category\">category</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"category\" id=\"ind_category\" class=\"form-control\" />"
						        				+ "<p class=\"help-block\">Portal al que pertenece el contenido.</p>"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_subject\">subject</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"subject\" id=\"ind_subject\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_audience\">dirigidoa</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"audience\" id=\"ind_audience\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_author\">author</label>"
						        			+ "<div class=\"col-md-3\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"author\" id=\"ind_author\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_last-modified\">last_modified</label>"
						        			+ "<div class=\"col-md-3\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"last_modified\" id=\"ind_last-modified\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-1 control-label\" for=\"language\">Idioma</label>"
						        			+ "<div class=\"col-md-1\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"language\" id=\"ind_language\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_keywords\">Palabras clave</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"keywords\" id=\"ind_keywords\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_priority\">Prioritario</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"priority\" id=\"ind_priority\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_robots\">Robots</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"robots\" id=\"ind_robots\" class=\"form-control\" />"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_text\">Contenido</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<textarea readonly=\"readonly\" name=\"text\" id=\"ind_text\" class=\"form-control\"></textarea>"
						        			+ "</div>"
						        			+ "<label class=\"col-md-2 control-label\" for=\"ind_links\">Enlaces</label>"
						        			+ "<div class=\"col-md-10\">"
						        				+ "<input readonly=\"readonly\" type=\"text\" name=\"links\" id=\"ind_links\" rows=\"10\" class=\"form-control\" />"
						        			+ "</div>"
					        				+ "<div class=\"col-md-offset-2\">"
					        					+ "<button class=\"boton btn btn-primary\" onclick=\"indizar()\">Indizar</button>"
					        				+ "</div>"
						                + "</div>"
					        			+ "</form>"
				        			+ "</div>"
			        			+ "</div>"
			        		+ "</div>"
		        		+ "</div>");			
			return xhtm.toString();
		} else {
			return null;
		}
				
 	}
	
	public static boolean tienePermiso(Credenciales credenciales, String servicio, String seccion, String permiso) {
		List<String> permisosPaginas = Autorizacion.obtenerPermisosSeccion(credenciales, servicio, seccion);
		return (permisosPaginas.contains(permiso));
 	}
	
	
	public static String distance(Punto p1, Punto p2) {

		double distance = Math.sqrt(
				Math.pow(p1.getCoordinates()[0] - p2.getCoordinates()[0], 2)
				+
				Math.pow(p1.getCoordinates()[1] - p2.getCoordinates()[1], 2)
				);
		int distancia = (int) Math.round(distance);
		String retorno; 
		
		if (distancia < 100) {
			retorno = " a menos de 100 metros";
		} else if (distancia < 500) {
			retorno = " a menos de 500 metros";
		} else if (distancia < 1000) {
			retorno = " a menos de 1 Kilómetro";
		} else {
			retorno = " a más de 1 Kilómetro";
		}
		return retorno;
	}
	public static String toUnicode(String str) {
		StringBuilder retStr = new StringBuilder();
		for (int i = 0; i < str.length(); i++) {
			int cp = Character.codePointAt(str, i);
			int charCount = Character.charCount(cp);
			if (charCount > 1) {
				i += charCount - 1; // 2.
				if (i >= str.length()) {
					throw new IllegalArgumentException("truncated unexpectedly");
				}
			}

			if (cp < 128) {
				retStr.appendCodePoint(cp);
			} else {
				retStr.append(String.format("\\u00%x", cp));
			}
		}
		return retStr.toString();
	}

	public static String removeHTMLEntity(String text) {
		return StringEscapeUtils.unescapeHtml4(StringUtils.trimToEmpty(text).replaceAll("\\<.*?\\>", " "));
	}
	public static String removeCDATA(String text) {
		return text.replace("<![CDATA[", "").replace("]]>", "");
	}
	public static String htmlToTxt(String description) {
		String txt = description.replace("</p>", System.getProperty("line.separator"));
		txt = txt.replace("</div>", System.getProperty("line.separator"));
		txt = txt.replace("<li>", "- ");
		txt = txt.replace("</li>", System.getProperty("line.separator"));
		return removeHTMLEntity(txt).trim();
	}
	public static String getQr(String uri, String clase) {
		String apicode = "http://chart.apis.google.com/chart?cht=qr&amp;chs=315x315&amp;choe=UTF-8&amp;chld=H&amp;chl=";
		if (uri.indexOf("http") < 0) {
			uri = Propiedades.getPath() + uri;
		}
		if (clase != null && clase.length() > 0) {
			clase = "class=\"" + clase + "\" ";
		}
        return "<img title=\"QrCode\" width=\"110\" height=\"110\" " + clase + "alt=\"qr-code\" src=\"" + (apicode + uri) + "\" />";
		
	}
	public static String levelDownH(String txt) {
		String texto = txt.replace("<h5", "<h6");
		texto = texto.replace("</h5", "</h6");
		
		texto = texto.replace("<h4", "<h5");
		texto = texto.replace("</h4", "</h5");
		
		texto = texto.replace("<h3", "<h4");
		texto = texto.replace("</h3", "</h4");
		
		texto = texto.replace("<h2", "<h3");
		texto = texto.replace("</h2", "</h3");
		return texto;
	}
	public static String encode(String txt) {
		try {
			return URLEncoder.encode(txt, CharEncoding.UTF_8);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}
	
	public static Boolean showRecaptcha(HttpServletRequest request) {
		return !Propiedades.excludedFromRecaptcha(request);
	}
	public static String getFechaRFC850(Date fecha) {
		return ConvertDate.date2String(fecha, ConvertDate.PATTERN_RFC850, Locale.ENGLISH).replaceAll("00:00", "23:59");
	}
	public static String getFecha(String fecha, String sourceFormat, String resultFormat) {
		try {
			return ConvertDate.date2String(ConvertDate.string2Date(fecha, sourceFormat), resultFormat);
		} catch (ParseException e) {
			return null;
		}
	}

	public static String stripAccents(String input) {
		return StringUtils.stripAccents(input);
	}
}
