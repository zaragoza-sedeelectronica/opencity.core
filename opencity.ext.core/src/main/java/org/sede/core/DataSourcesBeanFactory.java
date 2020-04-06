package org.sede.core;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.sede.core.utils.Funciones;
import org.sede.core.utils.Propiedades;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewInterceptor;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.stereotype.Repository;
import org.thymeleaf.util.StringUtils;

@Repository
public class DataSourcesBeanFactory implements BeanDefinitionRegistryPostProcessor {
	
	private static final Logger logger = LoggerFactory.getLogger(DataSourcesBeanFactory.class);
	
	
	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) {
		
		ClassPathScanningCandidateComponentProvider scanner =
				new ClassPathScanningCandidateComponentProvider(false);

		scanner.addIncludeFilter(new AnnotationTypeFilter(Configuration.class));
		HashMap<String, DataSourceConfiguration> esquemas = new HashMap<>();
		for (BeanDefinition bd : scanner.findCandidateComponents("org.sede")) {
		    try {
		    	Object obj = Class.forName(bd.getBeanClassName()).newInstance();
		    	if (obj instanceof PropertyFileInterface) {
		    		final PropertyFileInterface pfi = (PropertyFileInterface) obj;
		    		DataSourceConfiguration dsc;
//		    		logger.info(pfi.getClass().getCanonicalName() + ":" + pfi.getSchema());
		    		if (esquemas.containsKey(pfi.getSchema())) {
		    			dsc = esquemas.get(pfi.getSchema());
		    		} else {
		    			dsc = new DataSourceConfiguration();
		    			dsc.setEschema(pfi.getSchema());
		    			dsc.setJndi(pfi.getJndi());
		    			
		    		}
		    		dsc.getClasses().add(pfi.getEntity());
		    		esquemas.put(pfi.getSchema(), dsc);
		    		
		    	}
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
				logger.error(Funciones.getStackTrace(e));
			}
		    
		}
		
		for (Map.Entry<String, DataSourceConfiguration> entry : esquemas.entrySet()) {
			String esquema = entry.getValue().getEschema();
			logger.info("Cargado esquema: {}", esquema);
			String entityManagerFactoryBean = esquema.toUpperCase();
			String capitalisedName = StringUtils.capitalize(esquema);
			
			String jndi = entry.getValue().getJndi();
			String[] clases = entry.getValue().getClasses().toArray(new String[entry.getValue().getClasses().size()]);
			for (String c : clases) {
				logger.info("----- {}", c);
			}
			registry.registerBeanDefinition(entityManagerFactoryBean, defineEntityManagerFactoryBean(esquema, "dataSource" + capitalisedName, clases).getBeanDefinition());
			registry.registerBeanDefinition("dataSource" + capitalisedName, defineDataSourceJndi(esquema, jndi).getBeanDefinition());
			registry.registerBeanDefinition("transactionManager" + capitalisedName, defineTransactionManager(entityManagerFactoryBean).getBeanDefinition());
			registry.registerBeanDefinition("os" + capitalisedName, defineOEIVI(entityManagerFactoryBean).getBeanDefinition());
		}
		BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(PersistenceExceptionTranslationPostProcessor.class);
		registry.registerBeanDefinition("persistenceExceptionTranslationPostProcessor", definitionBuilder.getBeanDefinition());
	}
	
	
	private BeanDefinitionBuilder defineEntityManagerFactoryBean(String persistenceUnitName, String dataSource, String[] packages) {
		BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(LocalContainerEntityManagerFactoryBean.class); 
	    
		definitionBuilder.addPropertyValue("persistenceUnitName", persistenceUnitName);
		definitionBuilder.addPropertyReference("dataSource", dataSource);
		definitionBuilder.addPropertyValue("packagesToScan", packages);
		BeanDefinitionBuilder vendorBuilder = BeanDefinitionBuilder.rootBeanDefinition(HibernateJpaVendorAdapter.class);
		vendorBuilder.getBeanDefinition().setBeanClass(HibernateJpaVendorAdapter.class);
		definitionBuilder.addPropertyValue("jpaVendorAdapter", vendorBuilder.getBeanDefinition());
		definitionBuilder.addPropertyValue("jpaProperties", additionalProperties());
		
	    return definitionBuilder;  
	}
	
	private BeanDefinitionBuilder defineDataSourceJndi(String esquema, String dataSourceName) {
		if (Propiedades.isDatasourceJdbc()) {
			logger.info(dataSourceName + ": Acceso a datos a través de jdbc");
			BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(DriverManagerDataSource.class); 
		    definitionBuilder.setScope("singleton");
		    definitionBuilder.setLazyInit(true);
		    if (!Propiedades.containsKey(esquema + ".url")) {
		    	logger.info("default jdbc: " + Propiedades.getString("jdbc.default.url"));
		    	definitionBuilder.addPropertyValue("url", Propiedades.getString("jdbc.default.url"));
		    } else {
		    	logger.info("specific jdbc: " + Propiedades.getString(esquema + ".url"));
		    	definitionBuilder.addPropertyValue("url", Propiedades.getString(esquema + ".url"));
		    }
		    if (Propiedades.containsKey(esquema + ".driver")) {
		    	definitionBuilder.addPropertyValue("driverClassName", Propiedades.getString(esquema + ".driver"));
		    }
		    definitionBuilder.addPropertyValue("username", Propiedades.getString(esquema + ".username"));
		    definitionBuilder.addPropertyValue("password", Propiedades.getString(esquema + ".password"));
		    return definitionBuilder;
		} else {
			logger.info(dataSourceName + ": Acceso a datos a través de jndi");
			BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(JndiObjectFactoryBean.class); 
		    definitionBuilder.setScope("singleton");
		    definitionBuilder.setLazyInit(true);
			definitionBuilder.addPropertyValue("jndiName", Propiedades.getDatasourcePrefix() + "jdbc/" + dataSourceName);
		    return definitionBuilder;
		}
	}
	private BeanDefinitionBuilder defineTransactionManager(String name) {
		BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(JpaTransactionManager.class); 
		definitionBuilder.addPropertyReference("entityManagerFactory", name);
	    return definitionBuilder; 
	}
	
	private BeanDefinitionBuilder defineOEIVI(String name) {
		BeanDefinitionBuilder definitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(OpenEntityManagerInViewInterceptor.class); 
		definitionBuilder.addPropertyReference("entityManagerFactory", name);
	    return definitionBuilder; 
	}
	private Properties additionalProperties() {
	    Properties properties = new Properties();
	    properties.setProperty("hibernate.dialect", "org.hibernate.dialect.Oracle10gDialect");
	    properties.setProperty("hibernate.cache.use_second_level_cache", "false");
	        
	    return properties;
	}
	@Override
	public void postProcessBeanFactory(
			ConfigurableListableBeanFactory beanFactory) throws BeansException {
		
	}

}
