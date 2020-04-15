package org.sede.core.tag;

import java.util.HashMap;

import org.sede.core.plantilla.LayoutInterceptor;
import org.springframework.mobile.device.Device;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.AttributeValueQuotes;
import org.thymeleaf.model.IModel;
import org.thymeleaf.model.IModelFactory;
import org.thymeleaf.model.IOpenElementTag;
import org.thymeleaf.model.ITemplateEvent;
import org.thymeleaf.processor.element.AbstractElementModelProcessor;
import org.thymeleaf.processor.element.IElementModelStructureHandler;
import org.thymeleaf.standard.expression.Fragment;
import org.thymeleaf.templatemode.TemplateMode;


public class ContentTag extends AbstractElementModelProcessor {
	
	
	private static final String TAG_NAME = "content";
    private static final int PRECEDENCE = 0;

    public ContentTag(final String dialectPrefix) {
        super(TemplateMode.HTML, dialectPrefix, TAG_NAME, true, null, false, PRECEDENCE); 
    }

	@Override
	protected void doProcess(ITemplateContext context, IModel model,
			IElementModelStructureHandler structureHandler) {
		String plantilla = (String)context.getVariable(LayoutInterceptor.PLANTILLA_ATTR);
		final Device tipoDispositivo = (Device)context.getVariable("currentDevice");
		final boolean contenedor = model.get(0).toString().indexOf("container=\"") < 0;//tag.getAttributeValue("container") == null;	
		final IModelFactory modelFactory = context.getModelFactory();
		String estilo = "";
		boolean sidebar = false;
		Fragment fragmento = SedeDialect.computeFragment(context, plantilla);
		
		IModel modeloFragmento = fragmento.getTemplateModel(); 
		int n = modeloFragmento.size();
		while (n-- != 0) {
		    final ITemplateEvent event = modeloFragmento.get(n);		    
		    if (event instanceof IOpenElementTag){
		    	IOpenElementTag tag = (IOpenElementTag)event;
		    	if (tag.hasAttribute("estilo")) {
		    		estilo = tag.getAttributeValue("estilo");
		    	} else if (tag.hasAttribute("sidebar-wrapper")) {
		    		sidebar = true;
		    	}
		    }
		}
		
//      TODO revisar si se pueden obtener los fragmentos header, menu, footer del modeloFragmento 
//		creo que ahora por cada petición a computeFragment se hace una petición a disco o http... 
		
        model.replace(0, modelFactory.createOpenElementTag("div", "id", estilo));
        HashMap<String, String> attrs = new HashMap<String, String>();
        attrs.put("id", "wrapper");
        if (!tipoDispositivo.isMobile() && sidebar) {
        	attrs.put("class", "toggled");	
        }
        model.insert(1, modelFactory.createOpenElementTag("div", attrs, AttributeValueQuotes.DOUBLE, false));
        model.insert(2, modelFactory.createOpenElementTag("main", "id", "main"));
        attrs = new HashMap<String, String>();
        attrs.put("id", "rscont");
        if (contenedor) {
        	attrs.put("class", "container-fluid");	
        }
        model.insert(3, modelFactory.createOpenElementTag("div", attrs, AttributeValueQuotes.DOUBLE, false));
        model.insertModel(2, SedeDialect.computeFragment(context, plantilla + "::menu").getTemplateModel());
        
        
        model.insert(model.size() - 2, modelFactory.createCloseElementTag("div"));
        model.insert(model.size() - 1, modelFactory.createCloseElementTag("main"));
        model.insert(model.size() - 0, modelFactory.createCloseElementTag("div"));
        
        model.insertModel(model.size() - 2, SedeDialect.computeFragment(context, plantilla + "::footer").getTemplateModel());
        
        model.replace(model.size() - 2, modelFactory.createCloseElementTag("div"));
        
        model.insertModel(1,SedeDialect.computeFragment(context, plantilla + "::header").getTemplateModel());
           
	}	
}
