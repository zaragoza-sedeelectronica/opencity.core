package org.sede.config;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import org.sede.core.PropertyFileInterface;
import org.sede.core.dao.AutowireHelper;
import org.sede.core.dao.SearchFiql;
import org.sede.core.filter.InterceptorPeticion;
import org.sede.core.plantilla.AvoidRestrictedContextFactory;
import org.sede.core.plantilla.LayoutInterceptor;
import org.sede.core.rest.view.Transformador;
import org.sede.core.tag.SedeDialect;
import org.sede.core.utils.Propiedades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.DeviceWebArgumentResolver;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.ServletWebArgumentResolverAdapter;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.thymeleaf.templateresolver.UrlTemplateResolver;
import org.thymeleaf.util.StringUtils;

import net.sf.ehcache.CacheManager;

@Configuration
@EnableTransactionManagement
@EnableWebMvc
@ComponentScan(basePackages = "org.sede", lazyInit = true)
public class WebConfig implements WebMvcConfigurer {
	
	@Autowired
	private ApplicationContext context;
	
	private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);
	/**
	 * Configuración thymeleaf
	 */
	
	@Inject
    private ResourceLoader resourceLoader;
	
	@Bean
	public AbstractConfigurableTemplateResolver templateResolver()
	{	
		String pathVistas = Propiedades.getThymeleafView();
		if (pathVistas.indexOf("http:") >= 0 || pathVistas.indexOf("https:") >= 0) {
			logger.info("UrlTemplateResolver: {}", pathVistas);
			UrlTemplateResolver templateResolver = new UrlTemplateResolver();
			templateResolver.setPrefix(pathVistas);
			templateResolver.setSuffix(".xml");
			templateResolver.setForceTemplateMode(true);
			templateResolver.setTemplateMode(TemplateMode.HTML);
			templateResolver.setCacheable(false);
			return templateResolver;
		} else {
			logger.info("FileTemplateResolver: {}", pathVistas);
			FileTemplateResolver templateResolver = new FileTemplateResolver();
			templateResolver.setPrefix(pathVistas);
			templateResolver.setSuffix(".xml");
			templateResolver.setForceTemplateMode(true);
			templateResolver.setTemplateMode(TemplateMode.HTML);
			templateResolver.setCacheable(false);
			return templateResolver;
		}
		
	}

	@Bean
	public AutowireHelper autowireHelper(){
	    return AutowireHelper.getInstance();
	}
	
	@Bean 
	public SedeDialect sedeDialect() {
		return new SedeDialect();
	}
	
	@Bean
	public SpringTemplateEngine templateEngine()
	{
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.addTemplateResolver(templateResolver());
		templateEngine.addDialect(sedeDialect());
		if (!Propiedades.isThymeleafStrictMode()) {
			templateEngine.setEngineContextFactory(new AvoidRestrictedContextFactory());
		}
		return templateEngine;
	}

	@Bean
	public ThymeleafViewResolver viewResolver()
	{
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		viewResolver.setContentType("text/html");
		return viewResolver;
	}
	
	/**
	 * Configuración i18n
	 */
	@Bean
	public ReloadableResourceBundleMessageSource messageSource()
	{
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("file:" + Propiedades.getPathi18n());
		messageSource.setCacheSeconds(0);
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	@Bean
	public CookieLocaleResolver localeResolver()
	{
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("es"));
		localeResolver.setCookieName("org_sede_locale");
		localeResolver.setCookieMaxAge(3600);
		return localeResolver;
	}

	 
	@Override
	public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
		configurer.favorPathExtension(true);
		configurer.ignoreAcceptHeader(false);
		configurer.defaultContentType(new MediaType("text", "html", Charset.forName("utf-8")));
		configurer.useRegisteredExtensionsOnly(false);
		configurer.defaultContentType(new MediaType("text", "html", Charset.forName("utf-8")));
		configurer.mediaType("json", new MediaType("application", "json", Charset.forName("utf-8")));
		configurer.mediaType("jsonld", new MediaType("application", "ld+json", Charset.forName("utf-8")));
		configurer.mediaType("xml", new MediaType("application", "xml", Charset.forName("utf-8")));
		configurer.mediaType("geojson", new MediaType("application", "geo+json", Charset.forName("utf-8")));
		configurer.mediaType("georss", new MediaType("text", "xml+georss", Charset.forName("utf-8")));
		configurer.mediaType("rss", new MediaType("application", "rss+xml", Charset.forName("utf-8")));
		configurer.mediaType("csv", new MediaType("text", "csv", Charset.forName("utf-8")));
		configurer.mediaType("kml", new MediaType("application", "vnd.google-earth.kml+xml", Charset.forName("utf-8")));
		configurer.mediaType("rdf", new MediaType("application", "rdf+xml", Charset.forName("utf-8")));
		configurer.mediaType("ttl", new MediaType("application", "x-turtle", Charset.forName("utf-8")));
		configurer.mediaType("n3", new MediaType("text", "rdf+n3", Charset.forName("utf-8")));
		configurer.mediaType("sparqlxml", new MediaType("application", "sparql-results+xml", Charset.forName("utf-8")));
		configurer.mediaType("sparqljson", new MediaType("application", "sparql-results+json", Charset.forName("utf-8")));
		configurer.mediaType("solrjson", new MediaType("application", "solr-results+json", Charset.forName("utf-8")));
		configurer.mediaType("solrxml", new MediaType("application", "solr-results+xml", Charset.forName("utf-8")));
		configurer.mediaType("ics", new MediaType("text", "calendar", Charset.forName("utf-8")));
//		super.configureContentNegotiation(configurer);
	}

	@Bean
	public CacheManager cacheManager(){
		return ehCacheCacheManager().getObject();
	}
	@Bean
	public EhCacheManagerFactoryBean ehCacheCacheManager() {
		EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
		cmfb.setConfigLocation(resourceLoader.getResource("classpath:ehcache.xml"));
		
		cmfb.setShared(true);
		return cmfb;
	}
	
	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
	    CommonsMultipartResolver resolver=new CommonsMultipartResolver();
	    resolver.setDefaultEncoding("utf-8");
	    // TODO Externalizar a fichero de propiedades??
	    resolver.setMaxUploadSize(20971520);
	    resolver.setMaxInMemorySize(1048576);
	    return resolver;
	}
	
	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public SearchFiql searchFiql() {
	    return new SearchFiql();
	}
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(new ServletWebArgumentResolverAdapter(new DeviceWebArgumentResolver()));
	}
	
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(new Transformador());
//		super.configureMessageConverters(converters);
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
	    LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
	    lci.setParamName("locale");
	    return lci;
	}
	
	@Bean
	public InterceptorPeticion interceptorPeticion() {
	    return new InterceptorPeticion();
	}
	
	@Bean
	public LayoutInterceptor layoutInterceptor() {
	    return new LayoutInterceptor();
	}
	
	@Bean
	public DeviceResolverHandlerInterceptor deviceResolverHandlerInterceptor() {
	    return new DeviceResolverHandlerInterceptor();
	}
	
	
	@Autowired
    private Map<String, PropertyFileInterface> propiedades;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(interceptorPeticion());
		registry.addInterceptor(deviceResolverHandlerInterceptor());
		registry.addInterceptor(layoutInterceptor());
		
		for (Map.Entry<String, PropertyFileInterface> entry : propiedades.entrySet()) {
			String esquema = entry.getValue().getSchema();
			registry.addWebRequestInterceptor((OpenEntityManagerInViewInterceptor)context.getBean("os" + StringUtils.capitalize(esquema)));		
		}
	}
	
	
}
