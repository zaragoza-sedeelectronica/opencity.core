package org.sede.core.tag;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.sede.servicio.acceso.userrequirements.RequirementsInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.dialect.AbstractProcessorDialect;
import org.thymeleaf.dialect.IExpressionObjectDialect;
import org.thymeleaf.dialect.IProcessorDialect;
import org.thymeleaf.expression.IExpressionObjectFactory;
import org.thymeleaf.processor.IProcessor;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.standard.StandardDialect;
import org.thymeleaf.standard.expression.Fragment;
import org.thymeleaf.standard.expression.FragmentExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;
import org.thymeleaf.standard.processor.StandardXmlNsTagProcessor;
import org.thymeleaf.templatemode.TemplateMode;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.thymeleaf.context.IProcessingContext;
//import org.thymeleaf.dialect.AbstractDialect;
//import org.thymeleaf.dialect.IExpressionEnhancingDialect;
//import org.thymeleaf.dom.Element;
//import org.thymeleaf.processor.IProcessor;
//@Component
public class SedeDialect extends AbstractProcessorDialect implements IExpressionObjectDialect {
	private static final Logger logger = LoggerFactory.getLogger(SedeDialect.class);
	private static final String DIALECT_NAME = "Sede Dialect";

	@Autowired
	private ApplicationContext applicationContext;
	
	public SedeDialect() {
        // We will set this dialect the same "dialect processor" precedence as
        // the Standard Dialect, so that processor executions can interleave.
        super(DIALECT_NAME, "sede", StandardDialect.PROCESSOR_PRECEDENCE);
    }

	public Set<IProcessor> getProcessors(final String dialectPrefix) {
    	
    	final Set<IProcessor> processors = new HashSet<IProcessor>();
        processors.add(new ContentTag(dialectPrefix));
        processors.add(new MetaTag(dialectPrefix));
        processors.add(new RdfAttr(dialectPrefix));
        processors.add(new ClassAttr(dialectPrefix));
        processors.add(new BreadcrumbTag(dialectPrefix));
        processors.add(new SolrTag());
        processors.add(new RedirectTag());
        processors.add(new StandardXmlNsTagProcessor(TemplateMode.HTML, dialectPrefix));
    	
        String[] clases = applicationContext.getBeanNamesForType(AbstractElementTagProcessor.class);
        
    	for (String s: clases) {
    		logger.info("Tag added: {}", s);
    		processors.add((IProcessor) applicationContext.getBean(s));
    	}

        return processors;
    }
	
	@Override
	public IExpressionObjectFactory getExpressionObjectFactory() {
		return new IExpressionObjectFactory() {
			
			@Override
            public Set<String> getAllExpressionObjectNames() {
                return Collections.singleton("utils");
            }

            @Override
            public Object buildObject(IExpressionContext context,
                    String expressionObjectName) {
            	final Utils utils = new Utils();
                return utils;
            }

            @Override
            public boolean isCacheable(String expressionObjectName) {
                return true;
            }
        };
	}
	
	public static Fragment computeFragment(final ITemplateContext context, final String input) {
        final IStandardExpressionParser expressionParser = StandardExpressions.getExpressionParser(context.getConfiguration());
        final String trimmedInput = input.trim();
        final FragmentExpression fragmentExpression = (FragmentExpression) expressionParser.parseExpression(context, "~{" + trimmedInput + "}");
        final FragmentExpression.ExecutedFragmentExpression executedFragmentExpression = FragmentExpression.createExecutedFragmentExpression(context, fragmentExpression);
        final Fragment fragmento = FragmentExpression.resolveExecutedFragmentExpression(context, executedFragmentExpression, true);
        return fragmento;
    }

//	@Override
//	  public Set<IProcessor> getProcessors() {
//	    final Set<IProcessor> processors = new HashSet<IProcessor>();
//	    processors.add(new FechaTag());
//		processors.add(new SolrTag());
//	    processors.add(new RedirectTag());
//	    processors.add(new CollapseTag());
//	    return processors;
//	  }
//
//	public static Map<String, String> obtenerParametros(Element element, String attributeName) {
//		final String attributeValue = element.getAttributeValue(attributeName);
//		String[] parametros = attributeValue.split("#");
//		HashMap<String, String> retorno = new HashMap<String, String>();
//		for (String param:parametros) {
//			String[] valor = param.split("=");
//			retorno.put(valor[0].trim(), valor[1].trim());
//		}
//		return retorno;
//	}
//	@Override
//	public Map<String, Object> getAdditionalExpressionObjects(
//			IProcessingContext processingContext) {
//		 final Map<String, Object> objects = new HashMap<String, Object>();
//		 final Utils utils = new Utils();
//	     objects.put("utils", utils);
//	     return objects;
//	}
}
