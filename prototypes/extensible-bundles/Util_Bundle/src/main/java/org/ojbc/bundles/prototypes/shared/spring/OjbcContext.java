package org.ojbc.bundles.prototypes.shared.spring;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Interface for OSGi services that allow one bundle to get beans and properties exposed by another bundle.
 * 
 * This needs to be moved into the main OJBC codebase.
 *
 */
public interface OjbcContext {
	
	/**
	 * Get the list of properties objects exposed by the service.
	 * @return the list of properties objects
	 */
	public List<Properties> getPropertiesList();
	
	/**
	 * Get the map of beans (key=bean name, value=bean reference) exposed by the service.
	 * @return the map of beans
	 */
	public Map<String, Object> getExposedBeans();

}
