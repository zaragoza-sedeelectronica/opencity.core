package org.sede.core.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mobile.device.Device;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.engine.AttributeName;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractAttributeTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

public class ClassAttr extends AbstractAttributeTagProcessor {

	public static final Map<String, String> mobile = new HashMap<String, String>();
    public static final Map<String, String> other = new HashMap<String, String>();

    static {
        mobile.put("tab-content", "collapse-content");
        mobile.put("tab-pane active", "collapse-pane collapse in");
    }

    static {
    	other.put("tab-content", "tab-content");
    	other.put("tab-pane active", "tab-pane active");
    }
    
    
    public ClassAttr(final String dialectPrefix) {
        super(
        	TemplateMode.HTML, // This processor will apply only to HTML mode
            dialectPrefix,     // Prefix to be applied to name for matching
            null,              // No tag name: match any tag name
            false,             // No prefix to be applied to tag name
            "class",         // Name of the attribute that will be matched
            true,              // Apply dialect prefix to attribute name
            12000,        // Precedence (inside dialect's own precedence)
            true);             // Remove the matched attribute afterwards
    }


    @Override
    protected void doProcess(
            final ITemplateContext context, final IProcessableElementTag tag,
            final AttributeName attributeName, final String attributeValue,
            final IElementTagStructureHandler structureHandler) {


        final Device tipoDispositivo = (Device)context.getVariable("currentDevice");
        String currentClass = tag.getAttribute(this.getDialectPrefix() + ":class").getValue();
        if (mobile.containsKey(currentClass)) {
        	if (tipoDispositivo.isMobile()) {
        		structureHandler.setAttribute("class", mobile.get(currentClass));
        	} else {
        		structureHandler.setAttribute("class", other.get(currentClass));
        	}
        } else {
        	structureHandler.setAttribute("class", currentClass);
        }
        

    }

}
