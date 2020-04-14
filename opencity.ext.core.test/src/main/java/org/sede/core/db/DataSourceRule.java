package org.sede.core.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.sede.core.DataSourceConfiguration;
import org.sede.core.PropertyFileInterface;
import org.sede.core.PropiedadesTest;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

public class DataSourceRule extends SpringJUnit4ClassRunner {
	
	public DataSourceRule(Class<?> clazz) throws InitializationError {
		super(clazz);
		synchronized (DataSourceRule.class) {
			
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
					e.printStackTrace();
				}
			    
			}
			
			try {
				// Create initial context
				System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
				System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");
				
				for (Map.Entry<String, DataSourceConfiguration> entry : esquemas.entrySet()) {
					String esquema = entry.getValue().getEschema();
				
					String jndi = entry.getValue().getJndi();
					new InitialContext().rebind("java:/comp/env/jdbc/" + jndi, DataSource.crearDataSource(new DataSource("java:/comp/env/jdbc/" + jndi, esquema, PropiedadesTest.getString("db." + esquema + ".pass"))));
				}

			} catch (NamingException ex) {
				Logger.getLogger(Statement.class.getName()).log(Level.SEVERE, null, ex);
			} catch (SQLException e) {
				Logger.getLogger(Statement.class.getName()).log(Level.SEVERE, null, e);
			}
		}
	}
}