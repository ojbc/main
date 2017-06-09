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
import org.w3c.dom.Element;

@RestController
public class ConsentManagementRestController {
	
	private final Log log = LogFactory.getLog(ConsentManagementRestController.class);
	
	public static final String DEMODATA_HEADER_NAME = "demodata-ok";
	
	@Value("${restBaseUrl}")
	private String restBaseUrl;
	
	@Value("${allowUpdatesWithoutSamlToken:false}")
	private Boolean allowUpdatesWithoutSamlToken;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private SamlServiceImpl samlService;
	
	@RequestMapping(value="/cm-api/findPendingInmates", method=RequestMethod.GET, produces="application/json")
	public String findPendingInmates(HttpServletRequest request) throws Exception {
		
		Map<String, String> samlHeaderInfo = returnSamlHeaderInfo(request);		
		
		log.info("Saml Header Info (find inmates): " + samlHeaderInfo.toString());
		
		String ret = null;
		
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
	public String recordConsentDecision(HttpServletRequest request, Map<String, String> model) throws Exception {
		
		Map<String, String> samlHeaderInfo = returnSamlHeaderInfo(request);	
		
		log.info("Saml Header Info (record): " + samlHeaderInfo.toString());
		
		StringBuffer bodyBuf = new StringBuffer(1024);
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String line = null;
		
		while ((line = br.readLine()) != null) {
			bodyBuf.append(line).append("\n");
		}
		
		String body = bodyBuf.toString();
		
		log.debug("Body before SAML: " + body);
		
		if (!samlHeaderInfo.isEmpty())
		{
			body = addSamlData(body, samlHeaderInfo.get("GivenName"), samlHeaderInfo.get("SurName"), samlHeaderInfo.get("FederationId"));
		}			
		
		if (allowUpdatesWithoutSamlToken && samlHeaderInfo.isEmpty())
		{
			//Add fake SAML data
			body = addSamlData(body,"testGiven", "testSurName","federationId");
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

	private Map<String, String> returnSamlHeaderInfo(
			HttpServletRequest request)
			throws Exception {
		
		log.info("Entering return SAML header info.  (allowUpdatesWithoutSamlToken)" + allowUpdatesWithoutSamlToken);
		
		Map<String, String> samlHeaderInfo = new HashMap<String, String>();
		
		if (!allowUpdatesWithoutSamlToken)
		{	
			
			log.info("Attempting to recieve SAML assertion");
			
			Element samlAssertion = (Element)request.getAttribute("samlAssertion");
			
			if (samlAssertion == null)
			{
				log.info("SAML assertion is null try to get it");
				
				samlAssertion = samlService.getSamlAssertion(request);
			}
			
			if (samlAssertion == null)
			{
				throw new Exception("Unable to retrieve SAML assertion.");
			}	
			else
			{
				samlHeaderInfo = samlService.getSamlHeaderInfo(samlAssertion);
			}	
		}
		return samlHeaderInfo;
	}
	
	private String addSamlData(String body, String givenName, String surName, String federationId) {

		body = StringUtils.replace(body, "\"consenterUserID\":null,\"consentUserFirstName\":null,\"consentUserLastName\":null",   
										 "\"consenterUserID\":\"" + federationId  + "\",\"consentUserFirstName\":\"" + givenName  + "\",\"consentUserLastName\":\"" + surName  + "\"");
		return body;
	}

}
