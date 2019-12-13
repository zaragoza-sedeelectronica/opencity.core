package org.sede.portal.dao;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Inheritance;
import javax.xml.bind.annotation.XmlRootElement;

import org.sede.core.anotaciones.Esquema;
import org.sede.core.utils.ConvertDate;
import org.sede.portal.HomeController;
//import org.sede.servicio.actividades.dao.ActoGenericDAO;
//import org.sede.servicio.actividades.entity.Acto;
//import org.sede.servicio.aviso.dao.AvisoGenericDAO;
//import org.sede.servicio.aviso.entity.Aviso;
//import org.sede.servicio.empleo.dao.PlazaGenericDAO;
//import org.sede.servicio.empleo.entity.Plaza;
//import org.sede.servicio.noticias.dao.NoticiaGenericDAO;
//import org.sede.servicio.noticias.entity.Noticia;
//import org.sede.servicio.perfilcontratante.dao.ContratoGenericDAO;
//import org.sede.servicio.perfilcontratante.entity.Contrato;
//import org.sede.servicio.tramite.dao.ProcedimientoGenericDAO;
//import org.sede.servicio.tramite.entity.Procedimiento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.Arguments;
import org.thymeleaf.Template;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.spring4.context.SpringWebContext;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;

@Inheritance
@Component("DatosHome")
@XmlRootElement(name = "home")
public class StaticData {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
//	@Autowired
//	private AvisoGenericDAO daoAviso;
//	
//	@Autowired
//	private ActoGenericDAO daoActo;
//	
//	@Autowired
//	private ProcedimientoGenericDAO daoTramite;
//	
//	@Autowired
//	private NoticiaGenericDAO daoNoticia;
//	
//	@Autowired
//	private ContratoGenericDAO daoContrato;
//	
//	@Autowired
//	private PlazaGenericDAO daoPlaza;
	
	private boolean refresh = false;
	
//	protected Aviso avisoDestacadoPrincipal;
//	protected SearchResult<Aviso> avisosDestacados;
//	protected SearchResult<Acto> actosDestacados;
//	protected SearchResult<Acto> actosDelCalendario;
//	protected List<Procedimiento> tramitesEnPlazo;
//	protected List<Procedimiento> tramitesDestacados;
//	protected Noticia noticiaDestacada;
//	protected SearchResult<Noticia> ultimasNoticias;
//	protected SearchResult<Plaza> ultimasOfertasEmpleo;
//	protected SearchResult<Contrato> ultimosContratos;
	protected Temperatura temperatura;

//	@Transactional(Esquema.TMNOTICIAS)
//	public Aviso getAvisoDestacadoPrincipal() {
//		if (avisoDestacadoPrincipal == null || refresh) {
//			setAvisoDestacadoPrincipal(setAvisoDestacadoPrincipal());
//		}
//		return avisoDestacadoPrincipal;
//	}
//	public void setAvisoDestacadoPrincipal(Aviso avisoDestacadoPrincipal) { this.avisoDestacadoPrincipal = avisoDestacadoPrincipal; }
//	private Aviso setAvisoDestacadoPrincipal() {
//		try {
//			Search busqueda = new Search().setMaxResults(1).setFirstResult(0);
//			busqueda.addSortAsc("order");
//			busqueda.addFilter(Filter.some("portal", Filter.equal("id", "225")));
//			busqueda.addFilterAnd(Filter.lessOrEqual("pubDate",new Date()), Filter.greaterOrEqual("expirationDate",new Date()));
//			busqueda.addFilter(Filter.equal("visible", "1"));
//			return (Aviso) daoAviso.searchAndCount(busqueda).getResult().get(0);
//		} catch (Exception e) {
//			return new Aviso();
//		}
//	}
//
//	@Transactional(Esquema.TMNOTICIAS)
//	public SearchResult<Aviso> getAvisosDestacados() {
//		if (avisosDestacados == null || refresh) {
//			setAvisosDestacados(setAvisosDestacados());
//		}
//		return avisosDestacados;
//	}
//	public void setAvisosDestacados(SearchResult<Aviso> avisosDestacados) {
//		this.avisosDestacados = avisosDestacados;
//	}
//	private SearchResult<Aviso> setAvisosDestacados() {
//		try {
//			Search busqueda = new Search().setMaxResults(4).setFirstResult(1);
//			busqueda.addSortAsc("order");
//			busqueda.addFilter(Filter.some("portal", Filter.equal("id", "225")));
//			busqueda.addFilterAnd(Filter.lessOrEqual("pubDate",new Date()), Filter.greaterOrEqual("expirationDate",new Date()));
//			busqueda.addFilter(Filter.equal("visible", "1"));
//			return daoAviso.searchAndCount(busqueda);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return null;
//		}
//	}
//
//	
//	public SearchResult<Acto> getActosDestacados() {
//		if (actosDestacados == null || refresh) {
//			setActosDestacados(setActosDestacados());
//		}
//		return actosDestacados;
//	}
//	public void setActosDestacados(SearchResult<Acto> actosDestacados) {
//		this.actosDestacados = actosDestacados;
//	}
//	
//	private SearchResult<Acto> setActosDestacados() {
//		try {
//			BigDecimal idPortalHome = new BigDecimal(15); // Portal primera página
//			return daoActo.getFeaturedEvents(idPortalHome);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return new SearchResult<Acto>();
//		}
//	}
//    public SearchResult<Acto> getActosDelCalendario(Date date) {
//    	if (actosDelCalendario == null || refresh) {
//			setActosDelCalendario(setActosDelCalendario(date));
//		}
//		return actosDelCalendario;
//	}
//	public void setActosDelCalendario(SearchResult<Acto> actosDelCalendario) {
//		this.actosDelCalendario = actosDelCalendario;
//	}
//
//	private SearchResult<Acto> setActosDelCalendario(Date date) {
//		try {
//			return daoActo.getCalendarEventListSql(date, null, null);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return new SearchResult<Acto>();
//		}
//    }
//
//	@Transactional(Esquema.TMINTRA)
//    public List<Procedimiento> getTramitesEnPlazo() {
//    	if (tramitesEnPlazo == null || refresh) {
//			setTramitesEnPlazo(setTramitesEnPlazo());
//		}
//		return tramitesEnPlazo;
//	}
//	public void setTramitesEnPlazo(List<Procedimiento> tramitesEnPlazo) {
//		this.tramitesEnPlazo = tramitesEnPlazo;
//	}
//	private List<Procedimiento> setTramitesEnPlazo() {
//    	try {
//	    	return daoTramite.getTramitesEnPlazo(null, 5);
//    	} catch (Exception e) {
//			logger.error(e.getMessage());
//			return new ArrayList<Procedimiento>(0);
//		}
//	}
//
//	@Transactional(Esquema.TMINTRA)
//	public List<Procedimiento> getTramitesDestacados() {
//		if (tramitesDestacados == null || refresh) {
//			setTramitesDestacados(setTramitesDestacados());
//		}
//		return tramitesDestacados;
//	}
//	public void setTramitesDestacados(List<Procedimiento> tramitesDestacados) {
//		this.tramitesDestacados = tramitesDestacados;
//	}
//	private List<Procedimiento> setTramitesDestacados() {
//		try {
//			return daoTramite.getTramitesDestacados(null,5);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return new ArrayList<Procedimiento>(0);
//		}
//	}
//
//	@Transactional(Esquema.TMNOTICIAS)
//	public Noticia getNoticiaDestacada() {
//		if (noticiaDestacada == null || refresh) {
//			setNoticiaDestacada(setNoticiaDestacada());
//		}
//		return noticiaDestacada;
//	}
//	public void setNoticiaDestacada(Noticia noticiaDestacada) {
//		this.noticiaDestacada = noticiaDestacada;
//	}
//	private Noticia setNoticiaDestacada() { 
//		try {
//			Search busqueda = new Search().setMaxResults(1).setFirstResult(0);
//			busqueda.addFilter(Filter.equal("authorCode", 1));
//			busqueda.addFilter(Filter.some("group", Filter.equal("id", "1")));
//			busqueda.addFilter(Filter.equal("visible", "S"));
//			busqueda.addFilter(Filter.equal("status", "s"));
//			busqueda.addFilter(Filter.equal("tipo", "1"));
//			busqueda.addFilter(Filter.isNotNull("pubDate"));
//			busqueda.addFilter(Filter.some("order", Filter.equal("num", "1")));
//			busqueda.addSortDesc("dateCreated");
//			return (Noticia) daoNoticia.searchAndCount(busqueda).getResult().get(0);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return new Noticia();
//		}
//	}

//	@Transactional(Esquema.TMNOTICIAS)
//	public SearchResult<Noticia> getUltimasNoticias() {
//		if (ultimasNoticias == null || refresh) {
//			setUltimasNoticias(setUltimasNoticias());
//		}
//		return ultimasNoticias;
//	}
//	public void setUltimasNoticias(SearchResult<Noticia> ultimasNoticias) {
//		this.ultimasNoticias = ultimasNoticias;
//	}
//	private SearchResult<Noticia> setUltimasNoticias() {
//		try {
//			Search busqueda = new Search().setMaxResults(3).setFirstResult(1);
//			busqueda.addFilter(Filter.equal("authorCode", 1));
//			busqueda.addFilter(Filter.some("group", Filter.equal("id", "1")));
//			busqueda.addFilter(Filter.equal("visible", "S"));
//			busqueda.addFilter(Filter.equal("status", "s"));
//			busqueda.addFilter(Filter.equal("tipo", "1"));
//			busqueda.addFilter(Filter.isNotNull("pubDate"));
//			busqueda.addSortAsc("pubDate");
//			return daoNoticia.searchAndCount(busqueda);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return new SearchResult<Noticia>();
//		}	
//	}
//
//	@Transactional(Esquema.TMOFERTA)
//	public SearchResult<Plaza> getUltimasOfertasEmpleo() {
//		if (ultimasOfertasEmpleo == null || refresh) {
//			setUltimasOfertasEmpleo(setUltimasOfertasEmpleo());
//		}
//		return ultimasOfertasEmpleo;
//	}
//	public void setUltimasOfertasEmpleo(SearchResult<Plaza> ultimasOfertasEmpleo) {
//		this.ultimasOfertasEmpleo = ultimasOfertasEmpleo;
//	}
//	private SearchResult<Plaza> setUltimasOfertasEmpleo() {
//		try {
//			Search busqueda = new Search().setMaxResults(5).setFirstResult(0);
//			//Lo añade el setFiltroOpenData
//			busqueda.addFilter(Filter.some("anuncios", Filter.equal("visible", "s")));
//			busqueda.addFilter(Filter.some("anuncios", Filter.greaterOrEqual("valid", new Date())));
//			busqueda.addFilter(Filter.some("anuncios", Filter.lessOrEqual("issued", new Date())));
//			busqueda.addSortDesc("anuncios.issued");
//			return daoPlaza.searchAndCount(busqueda);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return new SearchResult<Plaza>();
//		}
//	}

//	@Transactional(Esquema.TMPERFILCONTRATANTE)
//	public SearchResult<Contrato> getUltimosContratos() {
//		if (ultimosContratos == null || refresh) {
//			setUltimosContratos(setUltimosContratos());
//		}
//		return ultimosContratos;
//	}
//	public void setUltimosContratos(SearchResult<Contrato> ultimosContratos) {
//		this.ultimosContratos = ultimosContratos;
//	}
//	private SearchResult<Contrato> setUltimosContratos() {
//		try {
//			Search busqueda = new Search().setMaxResults(5).setFirstResult(0);
//			busqueda.addFilter(Filter.some("entity", Filter.equal("id", 1)));
//			busqueda.addFilter(Filter.equal("contratoMenor", "N"));
//			busqueda.addFilterOr(Filter.isNull("expiration"), Filter.greaterOrEqual("expiration", new Date()));
//			busqueda.addFilterAnd(Filter.equal("estado_int", "0"), Filter.greaterOrEqual("fecha_presentacion", new Date()));
//			busqueda.addSortDesc("fecha_presentacion");
//			return daoContrato.searchAndCount(busqueda);
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//			return new SearchResult<Contrato>();
//		}
//	}

	public Temperatura getTemperatura() {
		if (temperatura == null || refresh) {
			setTemperatura(setTemperatura());
		}
		return temperatura;
	}
	public void setTemperatura(Temperatura temperatura) {
		this.temperatura = temperatura;
	}
	private Temperatura setTemperatura() {
		try {
			String query = "select maxima,minima,icono,fecha from noticias.temperatura";
			@SuppressWarnings("unchecked")
//			List<Object> listado = daoNoticia.em().createNativeQuery(query).getResultList();
	        Temperatura t = new Temperatura();
//			for (Iterator<Object> iterador = listado.iterator(); iterador.hasNext();) {
//				Object[] row = (Object[])iterador.next();
//				t.setMax(Integer.parseInt(row[0].toString()));
//				t.setMin(Integer.parseInt(row[1].toString()));
//				t.setIcon(Integer.parseInt(row[2].toString()));
//	            try {
//	                t.setFecha(ConvertDate.string2Date(row[3].toString(), ConvertDate.DATE_FORMAT));
//	            } catch (ParseException e) {
//	            	logger.error(e.getMessage());
//	            }
//	        }
			return t;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return new Temperatura();
		}
	}

	public boolean isRefresh() {
		return refresh;
	}
	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	public ProcessorResult getForTag(Arguments arguments, Node node) {
		try {
			Element nodo = ((Element)node);
			String fragmento = nodo.getAttributeValue("fragment") == null ? "fragmentos/temperatura" : nodo.getAttributeValue("fragment");
			((SpringWebContext)arguments.getContext()).getVariables().put("temperatura", this.getTemperatura());
			Template template = arguments.getTemplateRepository().getTemplate(new TemplateProcessingParameters(
					arguments.getConfiguration(), fragmento, arguments.getContext()));
			node.getParent().insertBefore(node, template.getDocument());
			node.getParent().removeChild(node);
			return ProcessorResult.OK;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ProcessorResult.OK;
		}
	}
}
