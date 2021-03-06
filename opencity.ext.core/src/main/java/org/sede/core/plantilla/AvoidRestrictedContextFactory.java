package org.sede.core.plantilla;
import java.util.List;
import java.util.Map;

import org.springframework.expression.BeanResolver;
import org.springframework.expression.ConstructorResolver;
import org.springframework.expression.MethodResolver;
import org.springframework.expression.OperatorOverloader;
import org.springframework.expression.PropertyAccessor;
import org.springframework.expression.TypeComparator;
import org.springframework.expression.TypeConverter;
import org.springframework.expression.TypeLocator;
import org.springframework.expression.TypedValue;
import org.thymeleaf.IEngineConfiguration;
import org.thymeleaf.context.IContext;
import org.thymeleaf.context.IEngineContext;
import org.thymeleaf.context.IEngineContextFactory;
import org.thymeleaf.context.StandardEngineContextFactory;
import org.thymeleaf.engine.TemplateData;
import org.thymeleaf.expression.IExpressionObjects;
import org.thymeleaf.spring5.expression.IThymeleafEvaluationContext;
//import org.thymeleaf.spring5.expression.IThymeleafEvaluationContext;
//import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext;
import org.thymeleaf.spring5.expression.ThymeleafEvaluationContext;

public class AvoidRestrictedContextFactory implements IEngineContextFactory {

	private final StandardEngineContextFactory factory = new StandardEngineContextFactory();

	@Override
	public IEngineContext createEngineContext(IEngineConfiguration configuration, TemplateData templateData,
			Map<String, Object> templateResolutionAttributes, IContext context) {
		IEngineContext engineCtx = factory.createEngineContext(configuration, templateData,
				templateResolutionAttributes, context);
		IThymeleafEvaluationContext evaluationContext;
		if (engineCtx.containsVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME)) {
			evaluationContext = (IThymeleafEvaluationContext) engineCtx
					.getVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME);
			engineCtx.setVariable(ThymeleafEvaluationContext.THYMELEAF_EVALUATION_CONTEXT_CONTEXT_VARIABLE_NAME,
					new AvoidRestrictedThymeleafEvaluationContext(evaluationContext));
		} else {
			throw new IllegalStateException();
		}
		return engineCtx;
	}
	
	
	private final class AvoidRestrictedThymeleafEvaluationContext implements IThymeleafEvaluationContext {

	      private final IThymeleafEvaluationContext evaluationContext;

	      public AvoidRestrictedThymeleafEvaluationContext(IThymeleafEvaluationContext context) {
	          super();
	          this.evaluationContext = context;
	      }

	      @Override
	      public TypedValue getRootObject() {
	          return evaluationContext.getRootObject();
	      }

	      @Override
	      public List<ConstructorResolver> getConstructorResolvers() {
	          return evaluationContext.getConstructorResolvers();
	      }

	      @Override
	      public List<MethodResolver> getMethodResolvers() {
	          return evaluationContext.getMethodResolvers();
	      }

	      @Override
	      public List<PropertyAccessor> getPropertyAccessors() {
	          return evaluationContext.getPropertyAccessors();
	      }

	      @Override
	      public TypeLocator getTypeLocator() {
	          return evaluationContext.getTypeLocator();
	      }

	      @Override
	      public TypeConverter getTypeConverter() {
	          return evaluationContext.getTypeConverter();
	      }

	      @Override
	      public TypeComparator getTypeComparator() {
	          return evaluationContext.getTypeComparator();
	      }

	      @Override
	      public OperatorOverloader getOperatorOverloader() {
	          return evaluationContext.getOperatorOverloader();
	      }

	      @Override
	      public BeanResolver getBeanResolver() {
	          return evaluationContext.getBeanResolver();
	      }

	      @Override
	      public void setVariable(String name, Object value) {
	          evaluationContext.setVariable(name, value);
	      }

	      @Override
	      public Object lookupVariable(String name) {
	          return evaluationContext.lookupVariable(name);
	      }

	      @Override
	      public final boolean isVariableAccessRestricted() {
	          // FUCK!!!
	          return false;
	      }

	      @Override
	      public void setVariableAccessRestricted(boolean restricted) {
	          // do nothing
	      }

	      @Override
	      public IExpressionObjects getExpressionObjects() {
	          return evaluationContext.getExpressionObjects();
	      }

	      @Override
	      public void setExpressionObjects(IExpressionObjects expressionObjects) {
	          evaluationContext.setExpressionObjects(expressionObjects);
	      }
	}

}
