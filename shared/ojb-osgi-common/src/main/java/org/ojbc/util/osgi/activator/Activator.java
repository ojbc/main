/*
 * Unless explicitly acquired and licensed from Licensor under another license, the contents of
 * this file are subject to the Reciprocal Public License ("RPL") Version 1.5, or subsequent
 * versions as allowed by the RPL, and You may not copy or use this file in either source code
 * or executable form, except in compliance with the terms and conditions of the RPL
 *
 * All software distributed under the RPL is provided strictly on an "AS IS" basis, WITHOUT
 * WARRANTY OF ANY KIND, EITHER EXPRESS OR IMPLIED, AND LICENSOR HEREBY DISCLAIMS ALL SUCH
 * WARRANTIES, INCLUDING WITHOUT LIMITATION, ANY WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE, QUIET ENJOYMENT, OR NON-INFRINGEMENT. See the RPL for specific language
 * governing rights and limitations under the RPL.
 *
 * http://opensource.org/licenses/RPL-1.5
 *
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.util.osgi.activator;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.osgi.OjbcContext;
import org.ojbc.util.osgi.OjbcContextImpl;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;

public class Activator implements BundleActivator {

	private static final Log log = LogFactory.getLog(Activator.class);
	
	private static final String SERVICE_NAMES_PROPERTY_NAME = "placeholder-service-names";

	private List<ServiceRegistration> serviceList = new ArrayList<ServiceRegistration>();

	@Override
	public void start(final BundleContext bundleContext) throws Exception {
		Hashtable<String, Object> properties = new Hashtable<String, Object>();
		properties.put(Constants.SERVICE_PID, "ojbc.context.services");
		bundleContext.registerService(ManagedService.class.getName(), new ManagedService(){
			@Override
			public void updated(Dictionary properties) throws ConfigurationException {
				if (properties != null) {
					String serviceNameString = (String) properties.get(SERVICE_NAMES_PROPERTY_NAME);
					if (serviceNameString != null) {
						unregsiterServices();
						String[] serviceNames = serviceNameString.split(",");
						for (String serviceName : serviceNames) {
							ServiceRegistration service = bundleContext.registerService(OjbcContext.class.getName(), new OjbcContextImpl(), null);
							Properties serviceProperties = new Properties();
							serviceProperties.setProperty("org.springframework.osgi.bean.name", serviceName);
							service.setProperties(serviceProperties);
							serviceList.add(service);
							log.info("Registered no-op OjbcContext service with name: " + serviceName);
						}
					}
				}
			}} , properties);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		unregsiterServices();
	}

	private void unregsiterServices() {
		for (ServiceRegistration registration : serviceList) {
			registration.unregister();
		}
	}

}
