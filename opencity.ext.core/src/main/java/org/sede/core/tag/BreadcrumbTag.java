package org.sede.core.tag;
import org.sede.core.rest.json.JSONArray;
import org.sede.core.rest.json.JSONObject;
import org.sede.core.utils.Funciones;
import org.springframework.mobile.device.Device;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Macro;
import org.thymeleaf.dom.Node;
import org.thymeleaf.dom.Text;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.ElementNameProcessorMatcher;
import org.thymeleaf.processor.IProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.spring4.context.SpringWebContext;

public class BreadcrumbTag extends AbstractProcessor {

	@Override
	public int getPrecedence() {
		return 0;
	}

	public IProcessorMatcher<? extends Node> getMatcher() {
		return new ElementNameProcessorMatcher("breadcrumb");
	}

	@Override
	public ProcessorResult doProcess(Arguments arguments,
			ProcessorMatchingContext context, Node node) {
		Element parent = (Element) node;
		JSONArray value = new JSONArray(parent.getAttributeValue("value"));
		boolean migasJson = false;
		String titleH1 = "";
		String enlaceH1 = "";
		if (parent.hasAttribute("title")) {
			migasJson = true;
			titleH1 = obtenerTituloSegunDispositivo(parent, arguments);
			Element h1 = new Element("h1");
			if (parent.hasAttribute("link")) {
				enlaceH1 = parent.getAttributeValue("link");
				Element linkH1 = new Element("a");
				linkH1.setAttribute("href", enlaceH1);
				linkH1.insertChild(0, new Text(titleH1));
				h1.addChild(linkH1);
			} else {
				h1.insertChild(0, new Text(titleH1));
			}
			
			node.getParent().insertBefore(node, h1);
		}
		
		Element script = new Element("script");
		script.setAttribute("type", "application/ld+json");
		
		StringBuilder json = new StringBuilder();
		json.append("{"
				+ " \"@context\": \"http://schema.org\","
				+ " \"@type\": \"BreadcrumbList\","
				+ " \"itemListElement\":"
				+ " [");
		
		Element nav = new Element("nav");
		Element ul = new Element("ul");
		ul.setAttribute("class", "breadcrumb small");
		if (!migasJson) {
			ul.setAttribute("vocab", "http://rdf.data-vocabulary.org/");
		}
		for (int i = 0; i < value.length(); i++) {
			JSONObject obj = value.getJSONObject(i);
			if (!Funciones.getPeticion().getUri().equals(obj.getString("link"))) {
				Element li = new Element("li");
				Element span = new Element("span");
				if (!migasJson) {
					span.setAttribute("typeof", "v:Breadcrumb");
				}
				Element a = new Element("a");
				a.setAttribute("href", obj.getString("link"));
				if (!migasJson) {
					a.setAttribute("rel", "v:url");
					a.setAttribute("property","v:title");
				}
				a.insertChild(0, new Text(obj.getString("text")));
				span.addChild(a);
				li.addChild(span);
				ul.addChild(li);
				if (i > 0) {
					json.append(",");
				}
				json.append("{"
						+ "\"@type\": \"ListItem\","
						+ "\"position\": "+ (i+1) +","
						+ "\"item\":"
							+ "{"
							+ "\"@id\": \"" + obj.getString("link") + "\","
							+ "\"name\": \"" + obj.getString("text") + "\""
							+ "}"
						+ "}");
			}
		}
		if (!Funciones.getPeticion().getUri().equals(enlaceH1)) {
			json.append(",{"
				+ "\"@type\": \"ListItem\","
				+ "\"position\": "+ (value.length() + 1) +","
				+ "\"item\":"
					+ "{"
						+ "\"@id\": \"" + enlaceH1 + "\","
						+ "\"name\": \"" + titleH1 + "\""
					+ "}"
				+ "}");
		}
		json.append(" ]"
				+ "}");
		nav.addChild(ul);
		if (migasJson) {
			script.insertChild(0, new Macro(json.toString()));
			nav.addChild(script);
		}
		node.getParent().insertBefore(node, nav);
		node.getParent().removeChild(node);
		return ProcessorResult.OK;
	}

	private String obtenerTituloSegunDispositivo(Element parent, Arguments arguments) {
		if (parent.hasAttribute("titlemobile")) {
			Device tipoDispositivo = (Device)((SpringWebContext)arguments.getContext()).getVariables().get("currentDevice");
			if (tipoDispositivo.isMobile()) {
				return parent.getAttributeValue("titlemobile");	
			} else {
				return parent.getAttributeValue("title");	
			}
		} else {
			return parent.getAttributeValue("title");
		}
		
	}

}
