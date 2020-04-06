package org.sede.core.tag;


import java.util.LinkedHashMap;
import java.util.List;

import org.sede.core.dao.Solr;
import org.sede.core.rest.solr.Faceta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class SolrTag extends AbstractElementTagProcessor {
	
	private static final String TAG_NAME = "solr";
    private static final int PRECEDENCE = 1000;

    private static final Logger logger = LoggerFactory.getLogger(SolrTag.class);
    private Solr solr = Solr.getInstance();
    
    public SolrTag() {
        super(
            TemplateMode.HTML, // This processor will apply only to HTML mode
            "sede",     // Prefix to be applied to name for matching
            TAG_NAME,          // Tag name: match specifically this tag
            true,              // Apply dialect prefix to tag name
            null,              // No attribute name: will match by tag name
            false,             // No prefix to be applied to attribute name
            PRECEDENCE);       // Precedence (inside dialect's own precedence)
    }


    @Override
    protected void doProcess(
            final ITemplateContext context, final IProcessableElementTag tag,
            final IElementTagStructureHandler structureHandler) {
        structureHandler.replaceWith(getForTag(context, tag, structureHandler), true);
        
    }
    private IModel getForTag(ITemplateContext context,
			IProcessableElementTag tag,
			IElementTagStructureHandler structureHandler) {
		try {
			
			LinkedHashMap<String, List<Faceta>> facetas = new LinkedHashMap<String, List<Faceta>>();
			String[] facetaParam = tag.getAttributeValue("facetas").split(",");
			for (String s : facetaParam) {
				facetas.put(s, null);
			}
	        
	        String fragmento = tag.getAttributeValue("fragment");
	        String q = tag.getAttributeValue("q");
	        String fq = tag.getAttributeValue("fq") == null ? "" : tag.getAttributeValue("fq");
	        String sort = tag.getAttributeValue("sort") == null ? "" : tag.getAttributeValue("sort");
	        int rows = Integer.parseInt(tag.getAttributeValue("rows") == null ? "20" : tag.getAttributeValue("rows"));
	        int start = Integer.parseInt(tag.getAttributeValue("start") == null ? "0" : tag.getAttributeValue("start"));
	        
	        Class<?> c;
			try {
				c = Class.forName(tag.getAttributeValue("class"));
			} catch (ClassNotFoundException e) {
				c = null;
			}
	        
			String categoria = tag.getAttributeValue("categoria");
	        String prefijo = tag.getAttributeValue("prefijo");
			String facetSort = tag.getAttributeValue("facetSort");
			structureHandler.setLocalVariable("resultadoSolr", solr.query(categoria, prefijo, q, fq, facetSort, null, null, rows, start, null, facetas, sort, c, true));
	       
			final IModelFactory modelFactory = context.getModelFactory();
			
			final IModel  model = modelFactory.createModel();
			model.addModel(SedeDialect.computeFragment(context, fragmento).getTemplateModel());
			return model;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}	
}
