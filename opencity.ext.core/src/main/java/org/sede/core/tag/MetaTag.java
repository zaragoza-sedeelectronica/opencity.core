package org.sede.core.tag;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.sede.core.plantilla.LayoutInterceptor;
import org.sede.core.utils.Funciones;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.Template;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;
import org.thymeleaf.fragment.ElementAndAttributeNameFragmentSpec;
import org.thymeleaf.fragment.IFragmentSpec;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.ElementNameProcessorMatcher;
import org.thymeleaf.processor.IProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.spring4.context.SpringWebContext;
import org.thymeleaf.standard.expression.IStandardExpression;
import org.thymeleaf.standard.expression.IStandardExpressionParser;
import org.thymeleaf.standard.expression.StandardExpressions;

public class MetaTag extends AbstractProcessor {

	@Override
	public int getPrecedence() {
		return 0;
	}

	public IProcessorMatcher<? extends Node> getMatcher() {
		return new ElementNameProcessorMatcher("meta");
	}

	@Override
	public ProcessorResult doProcess(Arguments arguments,
			ProcessorMatchingContext context, Node node) {
		try {
			Element parent = (Element) node;
			String plantilla = (String)((SpringWebContext)arguments.getContext()).getVariables().get(LayoutInterceptor.PLANTILLA_ATTR);
			
			String title = parent.getAttributeValue("title");
			if (title.indexOf('$') >= 0) {
				// Si es una expresión obtenemos el valor
				final Configuration configuration = arguments.getConfiguration();
				final IStandardExpressionParser parser =
				        StandardExpressions.getExpressionParser(configuration);
				
				final IStandardExpression expression =
				        parser.parseExpression(configuration, arguments, title);
			 
				title = ((String) expression.execute(configuration, arguments));
			}
			
			String description = parent.getAttributeValue("description");
			if (description != null && description.indexOf('$') >= 0) {
				// Si es una expresión obtenemos el valor
				final Configuration configuration = arguments.getConfiguration();
				final IStandardExpressionParser parser =
				        StandardExpressions.getExpressionParser(configuration);
				
				final IStandardExpression expression =
				        parser.parseExpression(configuration, arguments, description);
			 
				description = ((String) expression.execute(configuration, arguments));
			}
			
			title = title + ". Ayuntamiento de Zaragoza";
			Element meta = new Element("title");
			meta.setProcessable(true);
			meta.insertChild(0, new Text(title));
			node.getParent().insertBefore(node, meta);
			
			String image = "";
			if (!StringUtils.isEmpty(parent.getAttributeValue("summary_large_image"))) {
				image = parent.getAttributeValue("summary_large_image");
				if (image.indexOf('$') >= 0) {
					// Si es una expresión obtenemos el valor
					final Configuration configuration = arguments.getConfiguration();
					final IStandardExpressionParser parser =
					        StandardExpressions.getExpressionParser(configuration);
					
					final IStandardExpression expression =
					        parser.parseExpression(configuration, arguments, image);
				 
					image = ((String) expression.execute(configuration, arguments));
				}
			}
			if (StringUtils.isNotEmpty(image)) {
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "twitter:card");
				meta.setAttribute("content", "summary_large_image");
				node.getParent().insertBefore(node, meta);
				
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("property", "og:image");
				meta.setAttribute("content", image);
				node.getParent().insertBefore(node, meta);
				
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("property", "twitter:image:alt");
				meta.setAttribute("content", image);
				node.getParent().insertBefore(node, meta);
			} else {
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "twitter:card");
				meta.setAttribute("content", "summary");
				node.getParent().insertBefore(node, meta);
			}
			
			meta = new Element("meta");
			meta.setProcessable(true);
			meta.setAttribute("name", "og:type");
			meta.setAttribute("content", "website");
			node.getParent().insertBefore(node, meta);
			
			meta = new Element("meta");
			meta.setProcessable(true);
			meta.setAttribute("name", "twitter:title");
			meta.setAttribute("content", title);
			node.getParent().insertBefore(node, meta);
			
			meta = new Element("meta");
			meta.setProcessable(true);
			meta.setAttribute("name", "og:title");
			meta.setAttribute("content", title);
			node.getParent().insertBefore(node, meta);
			
			meta = new Element("meta");
			meta.setProcessable(true);
			meta.setAttribute("name", "og:url");
			meta.setAttribute("content", Funciones.getFullUri());
			node.getParent().insertBefore(node, meta);

			
			if (!StringUtils.isEmpty(description)) {
				description = Funciones.removeHTMLEntity(description).trim();
				if (description.length() > 200) {
					description = description.substring(0, 200);
				}
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "description");
				meta.setAttribute("content", description);
				node.getParent().insertBefore(node, meta);
				
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "og:description");
				meta.setAttribute("content", description);
				node.getParent().insertBefore(node, meta);
				
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "twitter:description");
				meta.setAttribute("content", description);
				node.getParent().insertBefore(node, meta);
				
			}
			if (!StringUtils.isEmpty(parent.getAttributeValue("robots"))) {
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "robots");
				meta.setAttribute("content", parent.getAttributeValue("robots"));
				node.getParent().insertBefore(node, meta);
			}
			if (!StringUtils.isEmpty(parent.getAttributeValue("last-modified"))) {
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "last-modified");
				meta.setAttribute("content", parent.getAttributeValue("last-modified"));
				node.getParent().insertBefore(node, meta);
			}
			if (!StringUtils.isEmpty(parent.getAttributeValue("category"))) {
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "category");
				meta.setAttribute("content", parent.getAttributeValue("category"));
				node.getParent().insertBefore(node, meta);
			}
			if (!StringUtils.isEmpty(parent.getAttributeValue("priority"))) {
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "priority");
				meta.setAttribute("content", parent.getAttributeValue("priority"));
				node.getParent().insertBefore(node, meta);
			}
			if (!StringUtils.isEmpty(parent.getAttributeValue("audience"))) {
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "audience");
				meta.setAttribute("content", parent.getAttributeValue("audience"));
				node.getParent().insertBefore(node, meta);
			}
			if (!StringUtils.isEmpty(parent.getAttributeValue("subject"))) {
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "subject");
				meta.setAttribute("content", parent.getAttributeValue("subject"));
				node.getParent().insertBefore(node, meta);
			}
			if (!StringUtils.isEmpty(parent.getAttributeValue("keywords"))) {
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "keywords");
				meta.setAttribute("content", parent.getAttributeValue("keywords"));
				node.getParent().insertBefore(node, meta);
			}
			meta = new Element("meta");
			meta.setProcessable(true);
			meta.setAttribute("name", "author");
			meta.setAttribute("content", StringUtils.isEmpty(parent.getAttributeValue("author")) ? "Oficina de Participación, Transparencia y Gobierno Abierto" :  parent.getAttributeValue("author"));
			node.getParent().insertBefore(node, meta);
			if (StringUtils.isEmpty(parent.getAttributeValue("remove_viewport"))) {
				meta = new Element("meta");
				meta.setProcessable(true);
				meta.setAttribute("name", "viewport");
				meta.setAttribute("content", "width=device-width,minimum-scale=1,initial-scale=1");
				node.getParent().insertBefore(node, meta);
			}
	
			
			final Configuration configuration = arguments.getConfiguration();
	
		    Template template = arguments.getTemplateRepository().getTemplate(new TemplateProcessingParameters(
			        arguments.getConfiguration(), plantilla, arguments.getContext()));
		    
		    Element general = new Element("div");
		    loadFragmento(arguments, general, configuration, template, "cssjs");
		    for(Node n : general.getChildren()) {
		    	node.getParent().insertBefore(node, n);
		    }
			
			node.getParent().removeChild(node);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ProcessorResult.OK;
	}
	private void loadFragmento(Arguments arguments, Node node,
			final Configuration configuration, Template template, String nombreFragmento) {
		IFragmentSpec fragmentSpec = new ElementAndAttributeNameFragmentSpec(null, "th:fragment", nombreFragmento, true);
		List<Node> fragmento = fragmentSpec.extractFragment(configuration, template.getDocument().getChildren());
		for (Node n: fragmento) {
			((Element)node).addChild(n);	
		}
	}
}
