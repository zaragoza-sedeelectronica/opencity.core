package org.sede.core.tag;

import java.util.List;
//
//import org.springframework.mobile.device.Device;
//import org.thymeleaf.Arguments;
//import org.thymeleaf.Configuration;
//import org.thymeleaf.dom.Element;
//import org.thymeleaf.dom.Node;
//import org.thymeleaf.dom.Text;
//import org.thymeleaf.processor.AbstractProcessor;
//import org.thymeleaf.processor.ElementNameProcessorMatcher;
//import org.thymeleaf.processor.IProcessorMatcher;
//import org.thymeleaf.processor.ProcessorMatchingContext;
//import org.thymeleaf.processor.ProcessorResult;
//import org.thymeleaf.spring4.context.SpringWebContext;
//import org.thymeleaf.standard.expression.IStandardExpression;
//import org.thymeleaf.standard.expression.IStandardExpressionParser;
//import org.thymeleaf.standard.expression.StandardExpressions;

public class CollapseTag /*extends AbstractProcessor */{
//	@Override
//	public int getPrecedence() {
//		return 0;
//	}
//
//	public IProcessorMatcher<? extends Node> getMatcher() {
//		return new ElementNameProcessorMatcher("collapse");
//	}
//
//	@Override
//	public ProcessorResult doProcess(Arguments arguments,
//			ProcessorMatchingContext context, Node node) {
//		
//		final Configuration configuration = arguments.getConfiguration();
//		Device tipoDispositivo = (Device)((SpringWebContext)arguments.getContext()).getVariables().get("currentDevice");
//		Element nodo = ((Element)node);
//		final String attributeValue = nodo.getAttributeValue("text");
//        final IStandardExpressionParser parser =
//                StandardExpressions.getExpressionParser(configuration);
//        final IStandardExpression expression =
//                parser.parseExpression(configuration, arguments, attributeValue);
//        String text = (String) expression.execute(configuration, arguments);
//
//
//		Element content = new Element("div");
//		content.setProcessable(true);
//		if (tipoDispositivo.isMobile()) {
//			content.setAttribute("class", "sede-collapse-content");
//		}
//
//		String heading = nodo.getAttributeValue("heading");
//		
//		Element title = new Element(heading);
//		int x = 0;
//		if(nodo.getAttributeValue("icon") != null){
//			Element icon = new Element("i");
//			icon.setAttribute("class", "fa " + nodo.getAttributeValue("icon"));
//			icon.setAttribute("aria-hidden", "true");
//			title.insertChild(x, icon);
//			text = " " + text;
//			x++;
//		}
//		title.insertChild(x, new Text(text));
//		title.setProcessable(true);
//		String identificador = text.replaceAll("[^\\p{L}\\p{Nd}]+", "");
//		if (tipoDispositivo.isMobile()) {
//			title.setAttribute("data-toggle","collapse");
//			title.setAttribute("data-target","#" + identificador);
//			title.setAttribute("aria-expanded","false");
//			title.setAttribute("aria-controls","prueba");
//			title.setAttribute("role","button");
//		}
//		Element pane = new Element("div");
//		pane.setProcessable(true);
//		if (tipoDispositivo.isMobile()) {
//			pane.setAttribute("class","collapse sede-collapse-pane");
//			pane.setAttribute("id", identificador);
//		}
//		for (Node contenido: ((Element)node).getChildren()) {
//			contenido.setProcessable(true);
//			pane.addChild(contenido);
//			
//		}
//		// FIXME recursivamente asocia todos los nodos como procesables...
//		addProcessable(pane);
//		content.addChild(title);
//		content.addChild(pane);
//		node.getParent().insertBefore(node, content);
//		node.getParent().removeChild(node);
//		return ProcessorResult.OK;
//	}
//
//	private void addProcessable(final Element inputTag) {
//		final List<Node> children = inputTag.getChildren();
//		for (final Node child : children) {
//			if (child != null && child instanceof Element) {
//				final Element childTag = (Element) child;
//				final String childTagName = childTag.getNormalizedName();
//				childTag.setProcessable(true);
//				if(childTagName != null) {
//					addProcessable(childTag);
//				}
//			}
//		}
//	}
	
}
