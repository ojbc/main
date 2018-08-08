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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import com.google.common.base.CharMatcher;


@Service
public class SearchResultConverter implements ApplicationContextAware {
	private final Log log = LogFactory.getLog(this.getClass());

	@Resource
	XsltTransformerService xsltTransformerService;
	
	@Value("${dispositionSearchResultXslLocation:classpath:xsl/dispositionSearchResult.xsl}")
	org.springframework.core.io.Resource dispositionSearchResultXsl;

	@Autowired(required=false)
	Map<String,String> searchDetailToXsl;
	
	private ApplicationContext applicationContext;

	private Map<String, org.springframework.core.io.Resource > xsls = new HashMap<String, org.springframework.core.io.Resource>();
	
    public String convertDispositionSearchResult(String searchContent) {
    	if (StringUtils.isBlank(searchContent)){
    		return "";
    	}
        return convertXml(searchContent, dispositionSearchResultXsl, null);
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
		
			searchContent = CharMatcher.ascii().retainFrom(searchContent);
		
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
}




