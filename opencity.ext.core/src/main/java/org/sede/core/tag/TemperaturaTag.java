package org.sede.core.tag;

import org.sede.portal.dao.StaticData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Node;
import org.thymeleaf.processor.AbstractProcessor;
import org.thymeleaf.processor.ElementNameProcessorMatcher;
import org.thymeleaf.processor.IProcessorMatcher;
import org.thymeleaf.processor.ProcessorMatchingContext;
import org.thymeleaf.processor.ProcessorResult;

@Component
public class TemperaturaTag extends AbstractProcessor {
	
	@Autowired
	private StaticData dao;
	
	@Override
	public int getPrecedence() {
		return 0;
	}

	public IProcessorMatcher<? extends Node> getMatcher() {
		return new ElementNameProcessorMatcher("temperatura");
	}

	@Override
	public ProcessorResult doProcess(Arguments arguments,
			ProcessorMatchingContext context, Node node) {
		
		return dao.getForTag(arguments, node);
	}
	
}
