package org.sede.core.plantilla;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.sede.core.anotaciones.PlantillaHTML;
import org.sede.core.rest.CheckeoParametros;
import org.sede.core.utils.Funciones;
import org.sede.servicio.ModelAttr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

public class LayoutInterceptor extends HandlerInterceptorAdapter {
	private static final Logger logger = LoggerFactory
			.getLogger(LayoutInterceptor.class);
	public static final String VIEW_ATTR = "view";
	public static final String COMPLETE_URI = "completeUri";
	public static final String PLANTILLA_ATTR = "plantilla";
	public static final String PLANTILLA_PORTAL = "plantillaPortal";
	public static final String PLANTILLA_SALIDA = "plantillaSalida";
	public static final String TEMPLATE_FILE_NAME = "_template";
	public static final String AMP_SUFFIX = "_amp";
	
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    	if (modelAndView == null || !modelAndView.hasView()) {
    		logger.info("No existe la vista");
            return;
        }
        String originalViewName = modelAndView.getViewName();
        if (modelAndView.getView() instanceof RedirectView 
        		|| isRedirectOrForward(originalViewName)) {
        	logger.info("Es una redirreccion: {}", originalViewName);
            return;
        }
        String template = LayoutInterceptor.TEMPLATE_FILE_NAME;
        if (modelAndView.getModelMap().containsKey(CheckeoParametros.AMP)) {
        	template = LayoutInterceptor.TEMPLATE_FILE_NAME + "_amp";
        }
        logger.info("peticion: {}", request.getRequestURL());
        logger.info("vista: {}", originalViewName);
        
        // TODO controlar error para el resto de atributos del modelo 
        if (modelAndView.getModel().containsKey(ModelAttr.REGISTRO) && modelAndView.getModel().get(ModelAttr.REGISTRO) instanceof ResponseEntity<?>) {
        	ResponseEntity<?> msg = (ResponseEntity<?>)modelAndView.getModel().get(ModelAttr.REGISTRO);
        	if (!msg.getStatusCode().is2xxSuccessful()) {
        		response.setStatus(msg.getStatusCode().value());
        	}
        }
        
        String plantilla;
        if (originalViewName.indexOf('/') >= 0) {
        	plantilla = originalViewName.substring(0, originalViewName.lastIndexOf('/')) + "/" + template;
        } else {
        	plantilla = "/" + template; 
        }
        HandlerMethod metodo = (HandlerMethod)handler;
        if (metodo.getMethod().getDeclaringClass().isAnnotationPresent(PlantillaHTML.class)) {
        	plantilla = metodo.getMethod().getDeclaringClass().getAnnotation(PlantillaHTML.class).value() + "/" + template;
        	
        }
        
        if (request.getAttribute(LayoutInterceptor.PLANTILLA_PORTAL) != null) {
        	plantilla = (String)request.getAttribute(LayoutInterceptor.PLANTILLA_PORTAL) + "/" + template;
        }
        if (request.getAttribute(LayoutInterceptor.PLANTILLA_SALIDA) != null) {
        	plantilla = (String)request.getAttribute(LayoutInterceptor.PLANTILLA_SALIDA) + "/" + template;
        }
        
        logger.info("{}: {}", LayoutInterceptor.PLANTILLA_ATTR, plantilla);
        
        modelAndView.addObject(LayoutInterceptor.PLANTILLA_ATTR, plantilla);
        modelAndView.addObject(LayoutInterceptor.VIEW_ATTR, originalViewName);
    }
	
		
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    	
    	if (ex != null) {
    		request.setAttribute(CheckeoParametros.TM_ERROR, ex.getMessage());
    		request.setAttribute(CheckeoParametros.TM_ERROR_STACK, Funciones.getStackTrace(ex));
    	}    	
    }
    private boolean isRedirectOrForward(String viewName) {
        return viewName.startsWith("redirect:") || viewName.startsWith("forward:");
    }
}
