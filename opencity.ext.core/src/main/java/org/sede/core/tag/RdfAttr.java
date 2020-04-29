package org.sede.core.tag;

import java.util.List;
import java.util.Map;

import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.templatemode.TemplateMode;

public class RdfAttr extends AbstractAttributeModelProcessor  {
	
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


    protected void doProcess(
            final ITemplateContext context, final IModel model,
            final AttributeName attributeName, final String attributeValue,
            final IElementModelStructureHandler structureHandler) {

    	final IEngineConfiguration configuration = context.getConfiguration();

        final IStandardExpressionParser parser =
                StandardExpressions.getExpressionParser(configuration);
        
        final IStandardExpression expression = parser.parseExpression(context, attributeValue);

        
        final Map<String,String> valores = (Map<String,String>)expression.execute(context);
    	final IModelFactory modelFactory = context.getModelFactory();
    	if (valores.containsKey("text")) {
        	String valor = valores.get("text"); 
        	if (valor != null && !valor.trim().isEmpty()) {
        		model.add(modelFactory.createText(valor));
        	} else {
//        		element.getParent().removeChild(element);
        	}
            valores.remove("text");
        }
    	final List<IProcessableElementTag> elementStack = context.getElementStack();
    	final IProcessableElementTag container = elementStack.get(elementStack.size() - 2);
        if ((container instanceof IOpenElementTag)) {
            container.getAttributeMap().putAll(valores);
        } 
        
    }


	
//
//	public RdfAttr() {
//		super("rdf");
//	}
//
//	public int getPrecedence() {
//		return 12000;
//	}
//
//	@Override
//	protected Map<String, String> getModifiedAttributeValues(
//			final Arguments arguments, final Element element,
//			final String attributeName) {
//		final Configuration configuration = arguments.getConfiguration();
//        final String attributeValue = element.getAttributeValue(attributeName);
//        final IStandardExpressionParser parser =
//                StandardExpressions.getExpressionParser(configuration);
//        final IStandardExpression expression =
//                parser.parseExpression(configuration, arguments, attributeValue);
//        @SuppressWarnings("unchecked")
//		final Map<String,String> valores = (Map<String,String>) expression.execute(configuration, arguments);
//        if (valores.containsKey("text")) {
//        	String valor = valores.get("text"); 
//        	if (valor != null && !valor.trim().isEmpty()) {
//        		Macro text = new Macro(valor);
//        		element.addChild(text);
//        	} else {
//        		element.getParent().removeChild(element);
//        	}
//            valores.remove("text");
//        }
//		return valores;
//	}
//
//	@Override
//	protected ModificationType getModificationType(final Arguments arguments,
//			final Element element, final String attributeName,
//			final String newAttributeName) {
//		return ModificationType.APPEND_WITH_SPACE;
//	}
//
//	@Override
//	protected boolean removeAttributeIfEmpty(final Arguments arguments,
//			final Element element, final String attributeName,
//			final String newAttributeName) {
//		return true;
//	}
//
//	@Override
//	protected boolean recomputeProcessorsAfterExecution(
//			final Arguments arguments, final Element element,
//			final String attributeName) {
//		return false;
//	}
}