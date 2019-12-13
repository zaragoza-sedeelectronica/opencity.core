package org.sede.core.dao;

import java.lang.annotation.ElementType;

import javax.validation.Path;
import javax.validation.Path.Node;
import javax.validation.TraversableResolver;

// Soluciona error HV000041: Call to TraversableResolver.isReachable() threw an exception.
// Caused by: javax.persistence.PersistenceException: 
// java.lang.ClassNotFoundException: org.apache.openjpa.persistence.PersistenceProviderImpl
// https://forum.hibernate.org/viewtopic.php?f=9&t=1011745&start=15
public class JPAIgnoreTraversableResolver implements TraversableResolver {

	@Override
	public boolean isReachable(Object traversableObject,
			Node traversableProperty, Class<?> rootBeanType,
			Path pathToTraversableObject, ElementType elementType) {
		return true;
	}

	@Override
	public boolean isCascadable(Object traversableObject,
			Node traversableProperty, Class<?> rootBeanType,
			Path pathToTraversableObject, ElementType elementType) {
		return true;
	}
}
