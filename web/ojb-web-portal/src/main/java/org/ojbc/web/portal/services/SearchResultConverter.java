package org.ojbc.web.portal.services;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.transform.sax.SAXSource;

import org.apache.commons.codec.CharEncoding;
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

	@Resource
	XsltTransformerService xsltTransformerService;
	
	@Value("${personSearchResultXslLocation:classpath:xsl/personSearchResult.xsl}")
	org.springframework.core.io.Resource searchResultXsl;
	
	@Value("classpath:xsl/vehicleSearchResult.xsl")
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
	
    @Value("classpath:xsl/identityBasedAccessControlResult.xsl")
    org.springframework.core.io.Resource identityBasedAccessControlResultXsl;
    
	@Resource
	Map<String,String> searchDetailToXsl;
	
	private ApplicationContext applicationContext;

	private Map<String, org.springframework.core.io.Resource > xsls = new HashMap<String, org.springframework.core.io.Resource>();
	
	public String convertPersonSearchResult(String searchContent, Map<String, Object> params){
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

	public String convertDetailSearchResult(String searchContent, String systemName) {
		return convertXml(searchContent, getResource(systemName), null);
    }
	
	public String convertSubscriptionSearchResult(String searchContent,  Map<String, Object> params) {
		return convertXml(searchContent, subscriptionSearchResultXsl, params);
    }	

	public String convertIdentityBasedAccessControlResult(String searchContent,  Map<String, Object> params) {
	    return convertXml(searchContent, identityBasedAccessControlResultXsl, params);
	}	
	
	org.springframework.core.io.Resource getResource(String systemName){
		systemName = systemName.trim();
	
		org.springframework.core.io.Resource resource = xsls.get(systemName);
		if(resource == null){
			resource = applicationContext.getResource("classpath:xsl/"+ searchDetailToXsl.get(systemName));
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

}




