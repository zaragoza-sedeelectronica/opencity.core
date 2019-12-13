package org.sede.core.tag;

import java.util.LinkedHashMap;
import java.util.List;

import org.sede.core.dao.Solr;
import org.sede.core.rest.solr.Faceta;
import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.Template;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.ElementNameProcessorMatcher;
import org.thymeleaf.processor.IProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.spring4.context.SpringWebContext;
@Component
public class SolrTag extends AbstractProcessor {
	
	private Solr solr = Solr.getInstance();
	
	@Override
	public int getPrecedence() {
		return 0;
	}

	public IProcessorMatcher<? extends Node> getMatcher() {
		return new ElementNameProcessorMatcher("solr");
	}

	@Override
	public ProcessorResult doProcess(Arguments arguments,
			ProcessorMatchingContext context, Node node) {
		Element nodo = ((Element)node);
		LinkedHashMap<String, List<Faceta>> facetas = new LinkedHashMap<String, List<Faceta>>();
		String[] facetaParam = nodo.getAttributeValue("facetas").split(",");
		for (String s : facetaParam) {
			facetas.put(s, null);
		}
        
        String fragmento = nodo.getAttributeValue("fragment");
        String q = nodo.getAttributeValue("q");
        String fq = nodo.getAttributeValue("fq") == null ? "" : nodo.getAttributeValue("fq");
        String sort = nodo.getAttributeValue("sort") == null ? "" : nodo.getAttributeValue("sort");
        int rows = Integer.parseInt(nodo.getAttributeValue("rows") == null ? "20" : nodo.getAttributeValue("rows"));
        int start = Integer.parseInt(nodo.getAttributeValue("start") == null ? "0" : nodo.getAttributeValue("start"));
        
        Class<?> c;
		try {
			c = Class.forName(nodo.getAttributeValue("class"));
		} catch (ClassNotFoundException e) {
			c = null;
		}
        
		String categoria = nodo.getAttributeValue("categoria");
        String prefijo = nodo.getAttributeValue("prefijo");
		String facetSort = nodo.getAttributeValue("facetSort");
		((SpringWebContext) arguments.getContext()).getVariables().put("resultadoSolr", solr.query(categoria, prefijo, q, fq, facetSort, null, null, rows, start, null, facetas, sort, c, true));
        
        Template template = arguments.getTemplateRepository().getTemplate(new TemplateProcessingParameters(
				arguments.getConfiguration(), fragmento, arguments.getContext()));
		node.getParent().insertBefore(node, template.getDocument());
		node.getParent().removeChild(node);
        return ProcessorResult.OK;
	}
	
}
