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
package org.ojbc.web.consentmanagement.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsentManagementRestController {
	
	private final Log log = LogFactory.getLog(ConsentManagementRestController.class);
	
	private static final String SAML_HEADER_NAME = "saml";
	public static final String DEMODATA_HEADER_NAME = "demodata-ok";

	@RequestMapping(value="/cm-api/search", method=RequestMethod.GET, produces="application/json")
	public String search(HttpServletRequest request) throws IOException {
		
		String ret = null;
		
		Map<String, String> samlHeaderInfo = getSamlHeaderInfo(request.getHeader(SAML_HEADER_NAME));
		String demodataHeaderValue = request.getHeader(DEMODATA_HEADER_NAME);
		
		if (!samlHeaderInfo.isEmpty()) {
			
			// todo: hit adapter
			
		} else if ("true".equals(demodataHeaderValue)) {
			
			ServletContext context = request.getServletContext();
			InputStream resourceContent = context.getResourceAsStream("/WEB-INF/demodata.json");
			BufferedReader br = new BufferedReader(new InputStreamReader(resourceContent));
			StringBuffer sb = new StringBuffer(1024);
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			br.close();
			ret = sb.toString();
			
			
		} else {
			// error?
			log.error("No SAML assertion in request, and not allowing demo data to be returned");
		}
		
		return ret;
		
	}
	
	private Map<String, String> getSamlHeaderInfo(String headerValue) {
		// todo: get it for real (using ShibbolethSamlAssertionRetriever), then parse the xml with xpath etc.
		// consider adding an enhancement to the ShibbolethSamlAssertionRetriever to do the parsing there to get the most common assertion info
		return new HashMap<>();
	}

}
