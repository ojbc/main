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
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsentManagementRestController {
	
	private final Log log = LogFactory.getLog(ConsentManagementRestController.class);
	
	private static final String SAML_HEADER_NAME = "saml";
	public static final String DEMODATA_HEADER_NAME = "demodata-ok";
	
	@Value("${restBaseUrl}")
	private String restBaseUrl;
	
	@Value("${allowUpdatesWithoutSamlToken:false}")
	private Boolean allowUpdatesWithoutSamlToken;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@RequestMapping(value="/cm-api/findPendingInmates", method=RequestMethod.GET, produces="application/json")
	public String findPendingInmates(HttpServletRequest request) throws IOException {
		
		String ret = null;
		
		Map<String, String> samlHeaderInfo = getSamlHeaderInfo(request.getHeader(SAML_HEADER_NAME));
		String demodataHeaderValue = request.getHeader(DEMODATA_HEADER_NAME);
		
		if ("true".equals(demodataHeaderValue)) {
			ret = DemoConsentServiceImpl.getInstance().getDemoConsentRecords();
			
			return ret;
		}	
		
		if (!samlHeaderInfo.isEmpty() || allowUpdatesWithoutSamlToken) {
			ret = restTemplate.getForObject(restBaseUrl  + "/findPendingInmates", String.class);
			
		} else {
			// error?
			log.error("No SAML assertion in request, and not allowing demo data to be returned");
		}
		
		return ret;
		
	}
	
	@RequestMapping(value="/cm-api/recordConsentDecision", method=RequestMethod.POST, consumes="application/json", produces="application/json")
	public String recordConsentDecision(HttpServletRequest request) throws IOException {
		
		StringBuffer bodyBuf = new StringBuffer(1024);
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String line = null;
		
		while ((line = br.readLine()) != null) {
			bodyBuf.append(line).append("\n");
		}
		
		String body = bodyBuf.toString();
		
		Map<String, String> samlHeaderInfo = getSamlHeaderInfo(request.getHeader(SAML_HEADER_NAME));
		
		log.debug("Body before SAML: " + body);
		
		if (allowUpdatesWithoutSamlToken && samlHeaderInfo.isEmpty())
		{
			//Add fake SAML data
			body = addTestSamlData(body);
		}			
		
		log.info("Body after SAML: " + body);

		String demodataHeaderValue = request.getHeader(DEMODATA_HEADER_NAME);
		
		if ("true".equals(demodataHeaderValue)) {
			DemoConsentServiceImpl.getInstance().removeRecord(body);
		} else if (!samlHeaderInfo.isEmpty() || allowUpdatesWithoutSamlToken) {
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				HttpEntity<String> entity = new HttpEntity<String>(body, headers);

				restTemplate.postForLocation(restBaseUrl + "/recordConsentDecision", entity, body);
		} else {
				// error?
				log.error("No SAML assertion in request, and not allowing demo data to be returned");
		}
		
		return "{}";
		
	}
	
	private String addTestSamlData(String body) {

		body = StringUtils.replace(body, "\"consenterUserID\":null,\"consentUserFirstName\":null,\"consentUserLastName\":null",   
										 "\"consenterUserID\":\"testUser\",\"consentUserFirstName\":\"testFirstName\",\"consentUserLastName\":\"testLastName\"");
		return body;
	}

	private Map<String, String> getSamlHeaderInfo(String headerValue) {
		// todo: get it for real (using ShibbolethSamlAssertionRetriever), then parse the xml with xpath etc.
		// consider adding an enhancement to the ShibbolethSamlAssertionRetriever to do the parsing there to get the most common assertion info
		Map<String, String> samlHeaderInfo = new HashMap<String, String>();
		
		
		return samlHeaderInfo;
	}
	
}
