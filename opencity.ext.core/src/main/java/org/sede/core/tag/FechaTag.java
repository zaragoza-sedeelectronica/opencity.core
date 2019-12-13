package org.sede.core.tag;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.sede.core.utils.ConvertDate;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractTextChildModifierAttrProcessor;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

public class FechaTag extends AbstractTextChildModifierAttrProcessor {

	
	public FechaTag() {
	    super("fecha");
	  }
	
	@Override
	protected String getText(final Arguments arguments, final Element element, final String attributeName) {
		final Configuration configuration = arguments.getConfiguration();
		 
	    final IStandardExpressionParser parser =
	        StandardExpressions.getExpressionParser(configuration);
	 
	    final String attributeValue = element.getAttributeValue(attributeName);
	 
	    final IStandardExpression expression =
	        parser.parseExpression(configuration, arguments, attributeValue);
	 
	    final String fecha = (String) expression.execute(configuration, arguments);
	    try {
	    	Date fechaDt = new SimpleDateFormat(ConvertDate.ISO8601_FORMAT).parse(fecha);
	    	return new SimpleDateFormat(ConvertDate.DATE_FORMAT).format(fechaDt);
	    } catch (ParseException p) {
	    	return "Fecha erronea";
	    }
	}

	@Override
	public int getPrecedence() {
		return 10000;
	}

}
