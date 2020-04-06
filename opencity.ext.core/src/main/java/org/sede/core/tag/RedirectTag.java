package org.sede.core.tag;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.context.ITemplateContext;
import org.thymeleaf.model.IProcessableElementTag;
import org.thymeleaf.processor.element.AbstractElementTagProcessor;
import org.thymeleaf.processor.element.IElementTagStructureHandler;
import org.thymeleaf.templatemode.TemplateMode;
public class RedirectTag extends AbstractElementTagProcessor {
	
	private static final String TAG_NAME = "redirect";
    private static final int PRECEDENCE = 1000;

    public RedirectTag() {
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
    	String url = tag.getAttributeValue("url");
    	
    	((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse().setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
    	((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse().setHeader("Location", url);;
    	
    }
}
