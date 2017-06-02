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
				props.load(applicationContext.getClassLoader().getResourceAsStream("config/ojb-web-portal.cfg"));
				activeProfiles=props.getProperty("spring.activeProfiles");
			} catch (IOException e) {
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
