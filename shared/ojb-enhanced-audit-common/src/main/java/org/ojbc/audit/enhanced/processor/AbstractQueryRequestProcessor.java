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
package org.ojbc.audit.enhanced.processor;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.audit.enhanced.dao.model.QueryRequest;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public abstract class AbstractQueryRequestProcessor {

	private static final Log log = LogFactory.getLog(AbstractQueryRequestProcessor.class);
	
	public abstract void auditPersonQueryRequest(Exchange exchange, @Body Document document);
	
	QueryRequest processPersonQueryRequest(Document document) throws Exception
	{
		QueryRequest personQueryRequest = new QueryRequest();

        String identificationId = XmlUtils.xPathStringSearch(document, "//nc:IdentificationID");

        if (StringUtils.isBlank(identificationId))
        {
        	identificationId = XmlUtils.xPathStringSearch(document, "//nc30:IdentificationID");
        } 
        
        if (StringUtils.isBlank(identificationId))
		{
        	identificationId = XmlUtils.xPathStringSearch(document, "//nc40:IdentificationID");
		}		
        
        String identificationSourceText = XmlUtils.xPathStringSearch(document, "//nc:IdentificationSourceText");
        
        if (StringUtils.isBlank(identificationSourceText))
        {
        	identificationSourceText = XmlUtils.xPathStringSearch(document, "//nc30:SystemName");
        }	
        
        if (StringUtils.isBlank(identificationSourceText))
        {
        	identificationSourceText = XmlUtils.xPathStringSearch(document, "//nc30:IdentificationSourceText");
        }	
        
        if (StringUtils.isBlank(identificationSourceText))
        {
        	identificationSourceText = XmlUtils.xPathStringSearch(document, "//nc40:IdentificationSourceText");
        }	
        
        personQueryRequest.setIdentificationId(identificationId);
        personQueryRequest.setIdentificationSourceText(identificationSourceText);
        
        log.debug("Person Query Request: " + personQueryRequest.toString());

        return personQueryRequest;
	}

}
