package org.ojb.web.portal;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

public class WebPortalApplicationContextInitializer implements
		ApplicationContextInitializer<ConfigurableApplicationContext> {
	
	private static final Log LOG = LogFactory.getLog(WebPortalApplicationContextInitializer.class);

	@Override
	public void initialize(ConfigurableApplicationContext applicationContext) {
		ConfigurableEnvironment environment = applicationContext.getEnvironment();
		
		Properties props = new Properties();
		String activeProfiles="";
		
		try {
			props.load(applicationContext.getClassLoader().getResourceAsStream("config/ojb_web_external_resource.cfg"));
			activeProfiles=props.getProperty("spring.activeProfiles");
		} catch (IOException e) {
			LOG.info("Unable to load spring profiles from external resource, will load default from ojb_web_portal.cfg");
		}
		
		if (StringUtils.isBlank(activeProfiles))
		{	
			try {
				props.clear();
				props.load(applicationContext.getClassLoader().getResourceAsStream("ojb-web-portal.cfg"));
				activeProfiles=props.getProperty("spring.activeProfiles");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG.info("Unable to load spring profiles from ojb_web_portal.cfg");
			}
		}	
			
		if (StringUtils.isBlank(activeProfiles))
		{
			LOG.info("No spring profiles set, will run in 'standalone' mode");
			environment.setDefaultProfiles("standalone");
		}	
		else
		{
			LOG.info("Setting default profiles to:" + activeProfiles);
			environment.setDefaultProfiles(activeProfiles.split(","));
		}	
	}

	
}
