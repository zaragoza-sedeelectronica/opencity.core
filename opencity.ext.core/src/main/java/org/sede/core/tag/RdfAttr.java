package org.sede.core.tag;

import java.util.Map;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class RdfAttr extends AbstractAttributeTagProcessor  {
	
	private static final String ATTR_NAME = "rdf";
    private static final int PRECEDENCE = 12000;


    public RdfAttr(final String dialectPrefix) {
        super(
            TemplateMode.HTML, // This processor will apply only to HTML mode
            dialectPrefix,     // Prefix to be applied to name for matching
            null,              // No tag name: match any tag name
            false,             // No prefix to be applied to tag name
            ATTR_NAME,         // Name of the attribute that will be matched
            true,              // Apply dialect prefix to attribute name
            PRECEDENCE,        // Precedence (inside dialect's own precedence)
            true);             // Remove the matched attribute afterwards
    }


    @Override
    protected void doProcess(ITemplateContext context,
                             IProcessableElementTag iProcessableElementTag,
                             AttributeName attributeName,
                             String attributeValue,
                             IElementTagStructureHandler iElementTagStructureHandler) {

    	final IEngineConfiguration configuration = context.getConfiguration();

        final IStandardExpressionParser parser = StandardExpressions.getExpressionParser(configuration);
        
        final IStandardExpression expression = parser.parseExpression(context, attributeValue);
       
        final Map<String,String> valores = (Map<String,String>)expression.execute(context);
        final IModelFactory modelFactory = context.getModelFactory();
    	if (valores.containsKey("text")) {
        	String valor = valores.get("text"); 
        	if (valor != null && !valor.trim().isEmpty()) {
                iElementTagStructureHandler.setBody(modelFactory.createText(modelFactory.createText(valor)), false);
        	} 
            valores.remove("text");
        }
    	for (Map.Entry<String, String> entry : valores.entrySet()) {
            iElementTagStructureHandler.setAttribute(entry.getKey(), entry.getValue());
        }
    }

}