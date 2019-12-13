package org.sede.core.tag;

import java.util.Map;

import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

public class RdfAttr extends AbstractAttributeModifierAttrProcessor {

	public RdfAttr() {
		super("rdf");
	}

	public int getPrecedence() {
		return 12000;
	}

	@Override
	protected Map<String, String> getModifiedAttributeValues(
			final Arguments arguments, final Element element,
			final String attributeName) {
		final Configuration configuration = arguments.getConfiguration();
        final String attributeValue = element.getAttributeValue(attributeName);
        final IStandardExpressionParser parser =
                StandardExpressions.getExpressionParser(configuration);
        final IStandardExpression expression =
                parser.parseExpression(configuration, arguments, attributeValue);
        @SuppressWarnings("unchecked")
		final Map<String,String> valores = (Map<String,String>) expression.execute(configuration, arguments);
        if (valores.containsKey("text")) {
        	String valor = valores.get("text"); 
        	if (valor != null && !valor.trim().isEmpty()) {
        		Macro text = new Macro(valor);
        		element.addChild(text);
        	} else {
        		element.getParent().removeChild(element);
        	}
            valores.remove("text");
        }
		return valores;
	}

	@Override
	protected ModificationType getModificationType(final Arguments arguments,
			final Element element, final String attributeName,
			final String newAttributeName) {
		return ModificationType.APPEND_WITH_SPACE;
	}

	@Override
	protected boolean removeAttributeIfEmpty(final Arguments arguments,
			final Element element, final String attributeName,
			final String newAttributeName) {
		return true;
	}

	@Override
	protected boolean recomputeProcessorsAfterExecution(
			final Arguments arguments, final Element element,
			final String attributeName) {
		return false;
	}
}
