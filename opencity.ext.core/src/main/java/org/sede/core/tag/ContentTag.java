package org.sede.core.tag;

import java.util.List;

import org.sede.core.plantilla.LayoutInterceptor;
import org.springframework.mobile.device.Device;
import org.thymeleaf.Arguments;
import org.thymeleaf.Configuration;
import org.thymeleaf.Template;
import org.thymeleaf.TemplateProcessingParameters;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.Node;
import org.thymeleaf.fragment.ElementAndAttributeNameFragmentSpec;
import org.thymeleaf.fragment.IFragmentSpec;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.ElementNameProcessorMatcher;
import org.thymeleaf.processor.IProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.spring4.context.SpringWebContext;

public class ContentTag extends AbstractProcessor {
//	private static final Logger logger = LoggerFactory.getLogger(ContentTag.class);
	@Override
	public int getPrecedence() {
		return 0;
	}

	public IProcessorMatcher<? extends Node> getMatcher() {
		return new ElementNameProcessorMatcher("content");
	}

	@Override
	public ProcessorResult doProcess(Arguments arguments,
			ProcessorMatchingContext context, Node node) {
		String plantilla = (String)((SpringWebContext)arguments.getContext()).getVariables().get(LayoutInterceptor.PLANTILLA_ATTR);

		final Configuration configuration = arguments.getConfiguration();

		Template template = arguments.getTemplateRepository().getTemplate(new TemplateProcessingParameters(
		        arguments.getConfiguration(), plantilla, arguments.getContext()));

		final Device tipoDispositivo = (Device)((SpringWebContext)arguments.getContext()).getVariables().get("currentDevice");
		final boolean contenedor = ((Element) node).getAttributeValue("container") == null;

//		<th:block th:include="${plantilla} :: header" />
//	    <div class="rscont container">
//	 +       <div class="row row-offcanvas row-offcanvas-left">
//	 +           <div class="col-md-12 bloque-contenido">
//
//	 +               <th:block th:include="${plantilla} :: menu" />
//	 +               <th:block th:include="${plantilla} :: breadcrumb" />
//
//	 +               <main id="main">
//	 +				<th:block th:include="fragmentos/readspeaker" />                    
//	 +                   <div class="clearfix"></div>
//	 +                   <div id="rscont">
		
		Element general = new Element("div");
		general.setAttribute("id", obtenerIdDeTemplate(template));
		general.setProcessable(true);
		loadFragmento(arguments, general, configuration, template, "header");
		
		
		Element rsCont = new Element("div");
		rsCont.setProcessable(true);
		rsCont.setAttribute("id", "rscont");
		if(contenedor) {
			rsCont.setAttribute("class", "container-fluid");
		}

		//Copiamos el contenido de la pagina en rscont
		for (Node contenido: ((Element)node).getChildren()) {
			contenido.setProcessable(true);
			rsCont.addChild(contenido);
			
		}
		// FIXME recursivamente asocia todos los nodos como procesables...
		addProcessable(rsCont);
		
		Element main = new Element("main");
		main.setAttribute("id", "main");

		main.addChild(rsCont);

		Element wrapper = new Element("div");
		wrapper.setAttribute("id", "wrapper");
		
		loadFragmentoMenu(tipoDispositivo, arguments, wrapper, configuration, template, "menu");
		loadFragmento(arguments, wrapper, configuration, template, "breadcrumb");

		wrapper.addChild(main);

		general.addChild(wrapper);
		node.getParent().insertBefore(node, general);
		
		loadFragmento(arguments, general, configuration, template, "footer");
		
		node.getParent().removeChild(node);
		
//		</div>
//        </main>
//        </div>
//    </div>
//</div>
//<th:block th:include="${plantilla} :: footer" />
		
		return ProcessorResult.OK;
	}
	
	private String obtenerIdDeTemplate(Template template) {
		return ((Element)template.getDocument().getFirstChild()).getAttributeValue("estilo");
	}

	
	private void loadFragmentoMenu(Device tipoDispositivo, Arguments arguments, Node node,
			final Configuration configuration, Template template, String nombreFragmento) {
		
		
		IFragmentSpec fragmentSpec = new ElementAndAttributeNameFragmentSpec(null, "th:fragment", nombreFragmento, true);
		List<Node> fragmento = fragmentSpec.extractFragment(configuration, template.getDocument().getChildren());
		
		for (Node n: fragmento) {
			if (n instanceof Element) {
				// Si el dispositivo es distinto de móvil y existe menú lateral (#sidebar-wrapper), aparece abierto por defecto.
				if (!tipoDispositivo.isMobile() && "sidebar-wrapper".equals(((Element)n).getAttributeValue("id"))) {
					((Element)node).setAttribute("class","toggled");
				}
			}
			((Element)node).addChild(n);	
		}
	}
	
	private void loadFragmento(Arguments arguments, Node node,
			final Configuration configuration, Template template, String nombreFragmento) {
		IFragmentSpec fragmentSpec = new ElementAndAttributeNameFragmentSpec(null, "th:fragment", nombreFragmento, true);
		List<Node> fragmento = fragmentSpec.extractFragment(configuration, template.getDocument().getChildren());
		for (Node n: fragmento) {
			((Element)node).addChild(n);	
		}
	}
	
	private void addProcessable(final Element inputTag) {
		final List<Node> children = inputTag.getChildren();
		for (final Node child : children) {
			if (child != null && child instanceof Element) {
				final Element childTag = (Element) child;
				final String childTagName = childTag.getNormalizedName();
				childTag.setProcessable(true);
				if(childTagName != null) {
					addProcessable(childTag);
				}
			}
		}
	}
	
}
