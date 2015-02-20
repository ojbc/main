package org.ojbc.bundles.prototypes.shared.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A Spring Factory Bean that uses an OjbcContext to look up a "substitute" bean in the OSGi service.  If it finds one, it returns it; if it doesn't, it returns
 * the specified default.
 *
 * This needs to be moved into the main OJBC codebase.
 *
 */
public class SubstitutableObjectFactoryBean {
	
	private static final Log LOGGER = LogFactory.getLog(SubstitutableObjectFactoryBean.class);
	
	public static Object getObject(String beanName, OjbcContext ojbcContext, Object defaultObject) {
		Object ret = ojbcContext.getExposedBeans().get(beanName);
		if (ret != null) {
			LOGGER.info("Substituting for object " + beanName);
		}
		return ret == null ? defaultObject : ret;
	}
	
}
