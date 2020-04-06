package org.sede.core.tag;
import org.sede.portal.dao.StaticData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;

@Component
public class TemperaturaTag  extends AbstractElementTagProcessor {
	
	private static final String TAG_NAME = "temperatura";
    private static final int PRECEDENCE = 1000;

    @Autowired
	private StaticData dao;
    
    public TemperaturaTag() {
        super(
            TemplateMode.HTML, // This processor will apply only to HTML mode
            "sede",     // Prefix to be applied to name for matching
            TAG_NAME,          // Tag name: match specifically this tag
            true,              // Apply dialect prefix to tag name
            null,              // No attribute name: will match by tag name
            false,             // No prefix to be applied to attribute name
            PRECEDENCE);       // Precedence (inside dialect's own precedence)
    }


    @Override
    protected void doProcess(
            final ITemplateContext context, final IProcessableElementTag tag,
            final IElementTagStructureHandler structureHandler) {
        structureHandler.replaceWith(dao.getForTag(context, tag, structureHandler), true);
        
    }

}
