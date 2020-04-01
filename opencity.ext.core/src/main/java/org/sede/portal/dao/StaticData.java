package org.sede.portal.dao;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.SearchResult;
import org.sede.core.anotaciones.Esquema;
import org.sede.core.tag.SedeDialect;
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
//import org.thymeleaf.Arguments;
//import org.thymeleaf.Template;
//import org.thymeleaf.TemplateProcessingParameters;
//import org.thymeleaf.dom.Element;
//import org.thymeleaf.dom.Node;
//import org.thymeleaf.processor.ProcessorResult;
//import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.IElementTagStructureHandler;

import javax.persistence.Inheritance;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Inheritance
@Component("DatosHome")
@XmlRootElement(name = "home")
public class StaticData {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private boolean refresh = false;
	protected Temperatura temperatura;


	

	public boolean isRefresh() {
		return refresh;
	}
	public void setRefresh(boolean refresh) {
		this.refresh = refresh;
	}

	
	public IModel getForTag(ITemplateContext context,
			IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		try {
			
			String fragmento = tag.getAttributeValue("fragment") == null ? "fragmentos/temperatura" : tag.getAttributeValue("fragment");
//			structureHandler.setLocalVariable("temperatura", this.getTemperatura());
						
			final IModelFactory modelFactory = context.getModelFactory();
			
			final IModel  model = modelFactory.createModel();
			model.addModel(SedeDialect.computeFragment(context, fragmento).getTemplateModel());
			return model;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return context.getModelFactory().createModel();
		}
	}

}
