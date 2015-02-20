package org.ojbc.bundles.prototypes.shared.spring;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Default implementation of the OjbcContext interface.
 *
 * This needs to be moved into the main OJBC codebase.
 *
 */
public class OjbcContextImpl implements OjbcContext {
	
	private List<Properties> propertiesList = new ArrayList<Properties>();
	private Map<String, Object> exposedBeans = new HashMap<String, Object>();

	@Override
	public List<Properties> getPropertiesList() {
		return Collections.unmodifiableList(propertiesList);
	}

	@Override
	public Map<String, Object> getExposedBeans() {
		return Collections.unmodifiableMap(exposedBeans);
	}
	
	public void setPropertiesList(List<Properties> propertiesList) {
		this.propertiesList = propertiesList;
	}
	
	public void setExposedBeans(Map<String, Object> exposedBeans) {
		this.exposedBeans = exposedBeans;
	}

}
