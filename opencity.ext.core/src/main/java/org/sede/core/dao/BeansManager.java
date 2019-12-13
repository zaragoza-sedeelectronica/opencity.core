package org.sede.core.dao;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.sede.servicio.acceso.userrequirements.RequirementsInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BeansManager {
	@Autowired
	private Map<String, RequirementsInterface> requirements;
	
//	@Autowired
//	private Set<Injectable> injectables = new HashSet<Injectable>();

	public Map<String, RequirementsInterface> getRequirements() {
		return requirements;
	}

	public void setRequirements(Map<String, RequirementsInterface> requirements) {
		this.requirements = requirements;
	}
	
//	@PostConstruct
//	private void inject() {
//	   for (Injectable injectableItem : injectables) {
//	       injectableItem.inject(this);
//	   }
//	}
	
}
