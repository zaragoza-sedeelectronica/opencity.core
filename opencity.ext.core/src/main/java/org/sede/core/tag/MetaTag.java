package org.sede.core.tag;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.sede.core.plantilla.LayoutInterceptor;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.utils.Funciones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class MetaTag extends AbstractElementTagProcessor {
	private static final Logger logger = LoggerFactory.getLogger(MetaTag.class);
	private static final String TAG_NAME = "meta";
    private static final int PRECEDENCE = 0;

    public MetaTag(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE); 
    }

    @Override
    protected void doProcess(
            final ITemplateContext context, final IProcessableElementTag tag,
            final IElementTagStructureHandler structureHandler) {

        final IModelFactory modelFactory = context.getModelFactory();

        final IModel  model = modelFactory.createModel();

		String title = tag.getAttributeValue("title");
		if (title.indexOf('$') >= 0) {
			// Si es una expresión obtenemos el valor
			final IEngineConfiguration configuration = context.getConfiguration();
			final IStandardExpressionParser parser =
			        StandardExpressions.getExpressionParser(configuration);
			
			final IStandardExpression expression = parser.parseExpression(context, title);
		 
			title = ((String) expression.execute(context));
		}
		
		String description = tag.getAttributeValue("description");
		if (description != null && description.indexOf('$') >= 0) {
			// Si es una expresión obtenemos el valor
			final IEngineConfiguration configuration = context.getConfiguration();
			final IStandardExpressionParser parser =
			        StandardExpressions.getExpressionParser(configuration);
			
			final IStandardExpression expression = parser.parseExpression(context, description);
		 
			description = ((String) expression.execute(context));
		}
		
		title = Funciones.removeHTMLEntity(title + ". Ayuntamiento de Zaragoza");
		model.add(modelFactory.createOpenElementTag("title"));
        model.add(modelFactory.createText(title));
        model.add(modelFactory.createCloseElementTag("title"));
		
        
        title = title.replaceAll("\"", "'");
        
		String image = "";
		if (!StringUtils.isEmpty(tag.getAttributeValue("summary_large_image"))) {
			image = tag.getAttributeValue("summary_large_image");
			if (image.indexOf('$') >= 0) {
				final IEngineConfiguration configuration = context.getConfiguration();
				final IStandardExpressionParser parser =
				        StandardExpressions.getExpressionParser(configuration);
				
				final IStandardExpression expression = parser.parseExpression(context, image);
			 
				image = ((String) expression.execute(context));
			}
		}
		Map<String, String> metaAttributes;
		if (StringUtils.isNotEmpty(image)) {
			
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "twitter:card");
			metaAttributes.put("content", "summary_large_image");
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));

	        metaAttributes = new HashMap<String, String>();
			metaAttributes.put("property", "og:image");
			metaAttributes.put("content", image);
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
			
	        metaAttributes = new HashMap<String, String>();
			metaAttributes.put("property", "twitter:image:alt");
			metaAttributes.put("content", image);
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
			

		} else {
			
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "twitter:card");
			metaAttributes.put("content", "summary");
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
			
		}
		metaAttributes = new HashMap<String, String>();
		metaAttributes.put("name", "og:type");
		metaAttributes.put("content", "website");
        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));

        metaAttributes = new HashMap<String, String>();
		metaAttributes.put("name", "twitter:title");
		metaAttributes.put("content", title);
        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
        
        metaAttributes = new HashMap<String, String>();
		metaAttributes.put("name", "og:title");
		metaAttributes.put("content", title);
        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
        
        String uri = Funciones.getFullUri();
        
        metaAttributes = new HashMap<String, String>();
		metaAttributes.put("name", "og:url");
		metaAttributes.put("content", Funciones.getFullUri());
        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
        
        if (uri.contains("/enlace/")) {
			try {
				String canonical = this.getCanonical(uri);
				metaAttributes = new HashMap<String, String>();
				metaAttributes.put("rel", "canonical");
				metaAttributes.put("href", canonical);
				model.add(modelFactory.createStandaloneElementTag("link", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
			} catch (Exception e) {
				;
			}
		}
        			
		if (!StringUtils.isEmpty(description)) {
			description = Funciones.removeHTMLEntity(description).trim().replaceAll("\"", "'");
			if (description.length() > 200) {
				description = description.substring(0, 200);
			}
			
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "description");
			metaAttributes.put("content", description);
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
	        
	        metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "og:description");
			metaAttributes.put("content", description);
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
	        
	        metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "twitter:description");
			metaAttributes.put("content", description);
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
	        
		}
		if (!StringUtils.isEmpty(tag.getAttributeValue("robots"))) {
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "robots");
			metaAttributes.put("content", tag.getAttributeValue("robots"));
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
	        
	
		}
		if (!StringUtils.isEmpty(tag.getAttributeValue("last-modified"))) {
			// Añadimos el atributo para poder ser utilizado en visualizacion
			structureHandler.setAttribute("metaTaglastModified", tag.getAttributeValue("last-modified"));
			
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "last-modified");
			metaAttributes.put("content", tag.getAttributeValue("last-modified"));
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
			
		}
		if (!StringUtils.isEmpty(tag.getAttributeValue("category"))) {
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "category");
			metaAttributes.put("content", tag.getAttributeValue("category"));
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
		}
		if (!StringUtils.isEmpty(tag.getAttributeValue("priority"))) {
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "priority");
			metaAttributes.put("content", tag.getAttributeValue("priority"));
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
		}
		if (!StringUtils.isEmpty(tag.getAttributeValue("audience"))) {
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "audience");
			metaAttributes.put("content", tag.getAttributeValue("audience"));
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
			
		}
		if (!StringUtils.isEmpty(tag.getAttributeValue("subject"))) {
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "subject");
			metaAttributes.put("content", tag.getAttributeValue("subject"));
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
		}
		if (!StringUtils.isEmpty(tag.getAttributeValue("keywords"))) {
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "keywords");
			metaAttributes.put("content", tag.getAttributeValue("keywords"));
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
		}
		
		if (StringUtils.isEmpty(tag.getAttributeValue("remove_viewport"))) {
			metaAttributes = new HashMap<String, String>();
			metaAttributes.put("name", "viewport");
			metaAttributes.put("content", "width=device-width,minimum-scale=1,initial-scale=1");
	        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
		}
		
		metaAttributes = new HashMap<String, String>();
		metaAttributes.put("name", "author");
		metaAttributes.put("content", StringUtils.isEmpty(tag.getAttributeValue("author")) ? "Oficina de Participación, Transparencia y Gobierno Abierto" :  tag.getAttributeValue("author"));
        model.add(modelFactory.createStandaloneElementTag("meta", metaAttributes, AttributeValueQuotes.DOUBLE, false, true));
        String plantilla = (String)context.getVariable(LayoutInterceptor.PLANTILLA_ATTR);
        try {
        	model.addModel(SedeDialect.computeFragment(context, plantilla + "::cssjs").getTemplateModel());
        } catch (Exception e) {
        	Funciones.getRequest().setAttribute(CheckeoParametros.TM_ERROR, e.getMessage());
//			logger.error(e.getMessage());
		}
        structureHandler.replaceWith(model, true);
    }
    
    
    private String getCanonical(String uri) {
		String pathEnlace = "/enlace/";
		return "https://www.zaragoza.es/sede/portal/" + uri.substring(uri.indexOf(pathEnlace) + pathEnlace.length(), uri.length());
	}
    
    
}
