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
