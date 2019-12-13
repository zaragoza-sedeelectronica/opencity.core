package org.sede.core.tag;

import java.util.HashMap;
import java.util.Map;

import org.springframework.mobile.device.Device;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.processor.attr.AbstractAttributeModifierAttrProcessor;
import org.thymeleaf.spring4.context.SpringWebContext;

public class ClassAttr extends AbstractAttributeModifierAttrProcessor {

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
	public ClassAttr() {
		super("class");
	}

	public int getPrecedence() {
		return 12000;
	}

	@Override
	protected Map<String, String> getModifiedAttributeValues(
			final Arguments arguments, final Element element,
			final String attributeName) {
		final String valor = element.getAttributeValue(attributeName);
		Device tipoDispositivo = (Device)((SpringWebContext)arguments.getContext()).getVariables().get("currentDevice");
		Map<String,String> valores = new HashMap<String, String>();
        if (tipoDispositivo.isMobile()) {
        	valores.put("class", mobile.get(valor));
        } else {
        	valores.put("class", other.get(valor));
        }
		return valores;
	}

	@Override
	protected ModificationType getModificationType(final Arguments arguments,
			final Element element, final String attributeName,
			final String newAttributeName) {
		return ModificationType.APPEND_WITH_SPACE;
	}

	@Override
	protected boolean removeAttributeIfEmpty(final Arguments arguments,
			final Element element, final String attributeName,
			final String newAttributeName) {
		return true;
	}

	@Override
	protected boolean recomputeProcessorsAfterExecution(
			final Arguments arguments, final Element element,
			final String attributeName) {
		return false;
	}
}
