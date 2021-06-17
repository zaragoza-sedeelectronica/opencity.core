package org.sede.core.tag;
import java.util.HashMap;

import org.sede.core.rest.json.JSONArray;
import org.sede.core.rest.json.JSONObject;
import org.sede.core.utils.Funciones;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mobile.device.Device;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class BreadcrumbTag extends AbstractElementTagProcessor {
	private static final Logger logger = LoggerFactory.getLogger(BreadcrumbTag.class);
	private static final String TAG_NAME = "breadcrumb";
    private static final int PRECEDENCE = 0;

    public BreadcrumbTag(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE); 
    }

    @Override
    protected void doProcess(
            final ITemplateContext context, final IProcessableElementTag tag,
            final IElementTagStructureHandler structureHandler) {

        final IModelFactory modelFactory = context.getModelFactory();
        final Device tipoDispositivo = (Device)context.getVariable("currentDevice");
        final IModel  model = modelFactory.createModel();

        try {
    		JSONArray value = new JSONArray(tag.getAttributeValue("value"));
    		boolean migasJson = false;
    		String titleH1 = "";
    		String enlaceH1 = "";
    		if (tag.hasAttribute("title")) {
    			migasJson = true;
    			titleH1 = obtenerTituloSegunDispositivo(tag, tipoDispositivo);
    			
    			
    			String etiqueta = "h1";
    			
    			if (!context.getTemplateData().getTemplate().endsWith("index")) {
    				etiqueta = tag.getAttributeValue("tag") == null ? "h1" : tag.getAttributeValue("tag");
    			}

    			model.add(modelFactory.createOpenElementTag(etiqueta, "class", "h1"));
    			
    			if (tag.hasAttribute("link")) {
    				enlaceH1 = tag.getAttributeValue("link");
    				
    				model.add(modelFactory.createOpenElementTag("a", "href", enlaceH1));
    				model.add(modelFactory.createText(titleH1));
    				
    				model.add(modelFactory.createCloseElementTag("a"));
    			} else {
    				model.add(modelFactory.createText(titleH1));
    			}
    			
    			model.add(modelFactory.createCloseElementTag(etiqueta));
    			
    			
    		}
    		
    		
    		StringBuilder json = new StringBuilder();
    		json.append("{"
    				+ " \"@context\": \"http://schema.org\","
    				+ " \"@type\": \"BreadcrumbList\","
    				+ " \"itemListElement\":"
    				+ " [");
    		
    		model.add(modelFactory.createOpenElementTag("nav"));
    		
    		HashMap<String, String> attrs = new HashMap<String, String>();
            attrs.put("class", "breadcrumb small");
            if (!migasJson) {
            	attrs.put("vocab", "http://rdf.data-vocabulary.org/");	
            }
            model.add(modelFactory.createOpenElementTag("ul", attrs, AttributeValueQuotes.DOUBLE, false));
    		
    		
    		
    		for (int i = 0; i < value.length(); i++) {
    			JSONObject obj = value.getJSONObject(i);
    			if (!Funciones.getPeticion().getUri().equals(obj.getString("link"))) {
    				model.add(modelFactory.createOpenElementTag("li"));
    				
    				attrs = new HashMap<String, String>();
    	            if (!migasJson) {
    	            	attrs.put("typeof", "v:Breadcrumb");	
    	            }
    	            model.add(modelFactory.createOpenElementTag("span", attrs, AttributeValueQuotes.DOUBLE, false));
    				
    				
    				
    	            attrs = new HashMap<String, String>();
    	            attrs.put("href", obj.getString("link"));
    	            if (!migasJson) {
    	            	attrs.put("rel", "v:url");
    	            	attrs.put("property", "v:title");
    	            }
    	            model.add(modelFactory.createOpenElementTag("a", attrs, AttributeValueQuotes.DOUBLE, false));
    	            model.add(modelFactory.createText(obj.getString("text")));
    	            model.add(modelFactory.createCloseElementTag("a"));
    	            model.add(modelFactory.createCloseElementTag("span"));
    	            model.add(modelFactory.createCloseElementTag("li"));
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
    		model.add(modelFactory.createCloseElementTag("ul"));
    		model.add(modelFactory.createCloseElementTag("nav"));
    		if (migasJson) {
    			model.add(modelFactory.createOpenElementTag("script", "type", "application/ld+json"));
    			model.add(modelFactory.createText(json.toString()));
    			model.add(modelFactory.createCloseElementTag("script"));
    		}
			
	        structureHandler.replaceWith(model, false);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
    }
    
	
//
//	@Override
//	public int getPrecedence() {
//		return 0;
//	}
//
//	public IProcessorMatcher<? extends Node> getMatcher() {
//		return new ElementNameProcessorMatcher("breadcrumb");
//	}
//
//	@Override
//	public ProcessorResult doProcess(Arguments arguments,
//			ProcessorMatchingContext context, Node node) {
//		Element parent = (Element) node;
//		JSONArray value = new JSONArray(parent.getAttributeValue("value"));
//		boolean migasJson = false;
//		String titleH1 = "";
//		String enlaceH1 = "";
//		if (parent.hasAttribute("title")) {
//			migasJson = true;
//			titleH1 = obtenerTituloSegunDispositivo(parent, arguments);
//			Element h1 = new Element("h1");
//			if (parent.hasAttribute("link")) {
//				enlaceH1 = parent.getAttributeValue("link");
//				Element linkH1 = new Element("a");
//				linkH1.setAttribute("href", enlaceH1);
//				linkH1.insertChild(0, new Text(titleH1));
//				h1.addChild(linkH1);
//			} else {
//				h1.insertChild(0, new Text(titleH1));
//			}
//			
//			node.getParent().insertBefore(node, h1);
//		}
//		
//		Element script = new Element("script");
//		script.setAttribute("type", "application/ld+json");
//		
//		StringBuilder json = new StringBuilder();
//		json.append("{"
//				+ " \"@context\": \"http://schema.org\","
//				+ " \"@type\": \"BreadcrumbList\","
//				+ " \"itemListElement\":"
//				+ " [");
//		
//		Element nav = new Element("nav");
//		Element ul = new Element("ul");
//		ul.setAttribute("class", "breadcrumb small");
//		if (!migasJson) {
//			ul.setAttribute("vocab", "http://rdf.data-vocabulary.org/");
//		}
//		for (int i = 0; i < value.length(); i++) {
//			JSONObject obj = value.getJSONObject(i);
//			if (!Funciones.getPeticion().getUri().equals(obj.getString("link"))) {
//				Element li = new Element("li");
//				Element span = new Element("span");
//				if (!migasJson) {
//					span.setAttribute("typeof", "v:Breadcrumb");
//				}
//				Element a = new Element("a");
//				a.setAttribute("href", obj.getString("link"));
//				if (!migasJson) {
//					a.setAttribute("rel", "v:url");
//					a.setAttribute("property","v:title");
//				}
//				a.insertChild(0, new Text(obj.getString("text")));
//				span.addChild(a);
//				li.addChild(span);
//				ul.addChild(li);
//				if (i > 0) {
//					json.append(",");
//				}
//				json.append("{"
//						+ "\"@type\": \"ListItem\","
//						+ "\"position\": "+ (i+1) +","
//						+ "\"item\":"
//							+ "{"
//							+ "\"@id\": \"" + obj.getString("link") + "\","
//							+ "\"name\": \"" + obj.getString("text") + "\""
//							+ "}"
//						+ "}");
//			}
//		}
//		if (!Funciones.getPeticion().getUri().equals(enlaceH1)) {
//			json.append(",{"
//				+ "\"@type\": \"ListItem\","
//				+ "\"position\": "+ (value.length() + 1) +","
//				+ "\"item\":"
//					+ "{"
//						+ "\"@id\": \"" + enlaceH1 + "\","
//						+ "\"name\": \"" + titleH1 + "\""
//					+ "}"
//				+ "}");
//		}
//		json.append(" ]"
//				+ "}");
//		nav.addChild(ul);
//		if (migasJson) {
//			script.insertChild(0, new Macro(json.toString()));
//			nav.addChild(script);
//		}
//		node.getParent().insertBefore(node, nav);
//		node.getParent().removeChild(node);
//		return ProcessorResult.OK;
//	}
//
	private String obtenerTituloSegunDispositivo(IProcessableElementTag tag, Device tipoDispositivo) {
		if (tag.hasAttribute("titlemobile")) {
			if (tipoDispositivo.isMobile()) {
				return tag.getAttributeValue("titlemobile");	
			} else {
				return tag.getAttributeValue("title");	
			}
		} else {
			return tag.getAttributeValue("title");
		}
		
	}

}
