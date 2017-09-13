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
package org.ojbc.web.portal.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.transform.sax.SAXSource;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.web.portal.controllers.dto.PersonFilterCommand;
import org.ojbc.web.portal.controllers.dto.SubscriptionFilterCommand;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

@Service
public class SearchResultConverter implements ApplicationContextAware {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource
	XsltTransformerService xsltTransformerService;
	
	@Value("${personSearchResultXslLocation:classpath:xsl/personSearchResult.xsl}")
	org.springframework.core.io.Resource searchResultXsl;
	
	@Value("${showPersonSearchToSubscriptionButton:false}")
	Boolean showPersonSearchToSubscriptionButton;
	
	@Value("${vehicleSearchResultXslLocation:classpath:xsl/vehicleSearchResult.xsl}")
	org.springframework.core.io.Resource vehicleSearchResultXsl;
	
	@Value("classpath:xsl/incidentSearchResult.xsl")
	org.springframework.core.io.Resource incidentSearchResultXsl;
	
	@Value("classpath:xsl/firearmSearchResult.xsl")
	org.springframework.core.io.Resource firearmSearchResultXsl;

	@Value("classpath:xsl/personFilter.xsl")
	org.springframework.core.io.Resource personFilterXsl;
	
	@Value("classpath:xsl/subscriptionFilter.xsl")
	org.springframework.core.io.Resource subscriptionFilterXsl;

	@Value("classpath:xsl/personFilterCleanupMerged.xsl")
	org.springframework.core.io.Resource personFilterCleanupMergedXsl;	
	
	@Value("classpath:xsl/subscriptionSearchResult.xsl")
	org.springframework.core.io.Resource subscriptionSearchResultXsl;
	
	@Value("classpath:xsl/rapbackSearchResult.xsl")
	org.springframework.core.io.Resource rapbackSearchResultXsl;
	
	@Value("classpath:xsl/criminalIdentificationSearchResult.xsl")
	org.springframework.core.io.Resource criminalIdentificationSearchResultXsl;
	
    @Value("classpath:xsl/identityBasedAccessControlResult.xsl")
    org.springframework.core.io.Resource identityBasedAccessControlResultXsl;

	@Value("${rapbackValidationButtonShowingPeriod:30}")
	Integer rapbackValidationButtonShowingPeriod;

	@Value("${chDisplaySupervisionTroCustodyHeaders:true}")
	Boolean chDisplaySupervisionTroCustodyHeaders;

	@Resource
	Map<String,String> searchDetailToXsl;
	
	private ApplicationContext applicationContext;

	private Map<String, org.springframework.core.io.Resource > xsls = new HashMap<String, org.springframework.core.io.Resource>();
	
	public String convertPersonSearchResult(String searchContent, Map<String, Object> params){
		
		log.debug("Show subscription button?" + showPersonSearchToSubscriptionButton);
		
		if (showPersonSearchToSubscriptionButton != null)
		{	
			params.put("showPersonSearchToSubscriptionButton", showPersonSearchToSubscriptionButton);
		}	
		
		return convertXml(searchContent, searchResultXsl, params);
	}
	
	public String convertVehicleSearchResult(String searchContent, Map<String, Object> params){
		return convertXml(searchContent, vehicleSearchResultXsl, params);
	}
	
	public String convertIncidentSearchResult(String searchContent, Map<String, Object> params){
		return convertXml(searchContent, incidentSearchResultXsl, params);
	}
	
	public String convertFirearmSearchResult(String searchContent, Map<String, Object> params) {
		return convertXml(searchContent, firearmSearchResultXsl, params);
	}

    public String convertRapbackSearchResult(String searchContent) {
    	if (StringUtils.isBlank(searchContent)){
    		return "";
    	}
        Map<String, Object> params = new HashMap<String, Object>(); 
        params.put("rapbackValidationButtonShowingPeriod", rapbackValidationButtonShowingPeriod);
        return convertXml(searchContent, rapbackSearchResultXsl, params);
    }
    
	public String convertDetailSearchResult(String searchContent, String systemName, String activeAccordionId) throws UnsupportedEncodingException {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("chDisplaySupervisionTroCustodyHeaders", chDisplaySupervisionTroCustodyHeaders);
		
		if (StringUtils.isNotBlank(activeAccordionId)) {
	        params.put("activeAccordionId", activeAccordionId);
	        return convertXml(searchContent, getResource(systemName), params);
	    }
	    else {
	        return convertXml(searchContent, getResource(systemName), null);
	    }
		
    }
	
	public String convertSubscriptionSearchResult(String searchContent,  Map<String, Object> params) {
		return convertXml(searchContent, subscriptionSearchResultXsl, params);
    }	

	public String convertIdentityBasedAccessControlResult(String searchContent,  Map<String, Object> params) {
	    return convertXml(searchContent, identityBasedAccessControlResultXsl, params);
	}	
	
	org.springframework.core.io.Resource getResource(String systemName) throws UnsupportedEncodingException{
		systemName = URLDecoder.decode(StringUtils.trimToEmpty(systemName), "UTF-8");
		log.info("systemName: " + systemName);
		log.info("searchDetailToXsl: " + searchDetailToXsl);
		org.springframework.core.io.Resource resource = xsls.get(systemName);
		if(resource == null){
			resource = applicationContext.getResource("classpath:xsl/"+ searchDetailToXsl.get(systemName));
			log.info("xsl resource to use: " + StringUtils.trimToEmpty(searchDetailToXsl.get(systemName)));
			xsls.put(systemName, resource);
		}
		
		return resource;
	}

	
	private String convertXml(String searchContent, org.springframework.core.io.Resource resource, Map<String, Object> params) {
			return xsltTransformerService.transform(createSource(new ByteArrayInputStream(searchContent.getBytes())), createSourceAndSetSystemId(resource),params);
	}

	private SAXSource createSource(InputStream inputStream) {
		InputSource inputSource = new InputSource(inputStream);
		inputSource.setEncoding(CharEncoding.UTF_8);
		return new SAXSource(inputSource);
	}
	
	private SAXSource createSourceAndSetSystemId(org.springframework.core.io.Resource inputStream) {
		try {
			SAXSource inputSource;
			inputSource = createSource(inputStream.getInputStream());
			//need to setSystemId because xsl needs this set in order for to <import> to know where to look to load relative paths
			inputSource.setSystemId(inputStream.getURL().toExternalForm());
			return inputSource;
		} catch (Exception e) {
			throw new RuntimeException("Unable to read XML/XSL", e);
		}
	}


	@Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
    }


	public String filterXml(String xmlContent, PersonFilterCommand personFilterCommand) {
		String filterResult1 = convertXml(xmlContent, personFilterXsl, personFilterCommand.getParamsMap());
		return convertXml(filterResult1, personFilterCleanupMergedXsl, personFilterCommand.getParamsMap());		
	}
	
	public String filterXml(String xmlContent, SubscriptionFilterCommand subFilterCmd) {
		
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("currentDateTime", subFilterCmd.getCurrentDate());
		paramsMap.put("filterSubscriptionStatus", subFilterCmd.getSubscriptionStatus());
		paramsMap.put("validationDueWarningDays", subFilterCmd.getValidationDueWarningDays());				
		
		String filterResult = convertXml(xmlContent, subscriptionFilterXsl, paramsMap);
		
		return filterResult;		
	}

	public String convertCriminalIdentificationSearchResult(String searchContent) {
    	if (StringUtils.isBlank(searchContent)){
    		return "";
    	}
        return convertXml(searchContent, criminalIdentificationSearchResultXsl, null);
	}

	public String convertIdentificationResultsQueryResult(String searchContent,
			Object object) {
		// TODO Auto-generated method stub
		return null;
	}

}




