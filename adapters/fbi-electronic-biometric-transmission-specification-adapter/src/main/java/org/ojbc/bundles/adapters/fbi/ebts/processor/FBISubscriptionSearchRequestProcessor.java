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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import org.apache.commons.lang.StringUtils;
import org.ojbc.bundles.adapters.fbi.ebts.FBISubscriptionSearchRequest;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class FBISubscriptionSearchRequestProcessor {
	
	
	public Document createFBISubscriptionSearchRequestDoc(FBISubscriptionSearchRequest fBISubscriptionSearchRequest) throws Exception{
		
		Document fbiSubReqDoc = OJBCXMLUtils.createDocument();
		
		String sid = fBISubscriptionSearchRequest.getPersonStateFingerprintId();
				
		Element rootElement = OJBCXMLUtils.createElement(fbiSubReqDoc, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST, 
				"SubscriptionSearchRequest");	
	
		rootElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_SUBSCRIPTION_SEARCH_REQUEST);
			
		fbiSubReqDoc.appendChild(rootElement);
		
		Element fbiSubElement = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "FBISubscription");
				
		if(StringUtils.isNotEmpty(sid)){
			
			Element subscriptionSubjectElement = XmlUtils.appendElement(fbiSubElement, 
					OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "SubscriptionSubject");
		
			Element personAugmentationElement = XmlUtils.appendElement(subscriptionSubjectElement, 
					OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
			
			Element personSidElement = XmlUtils.appendElement(personAugmentationElement, OjbcNamespaceContext.NS_JXDM_41, 
					"PersonStateFingerprintIdentification");
			
			Element sidValElement = XmlUtils.appendElement(personSidElement, OjbcNamespaceContext.NS_NC, "IdentificationID");	
			
			sidValElement.setTextContent(sid);
		}
				
		String reasonCode = fBISubscriptionSearchRequest.getCriminalSubscriptionReasonCode();
		
		if(StringUtils.isNotEmpty(reasonCode)){			
			Element reasonCodeElement = XmlUtils.appendElement(fbiSubElement, OjbcNamespaceContext.NS_SUBSCRIPTION_SEARCH_REQUEST_EXT, "CriminalSubscriptionReasonCode");			
			reasonCodeElement.setTextContent(reasonCode);
		}		
		
		OjbcNamespaceContext ojbNamespaceContext = new OjbcNamespaceContext();
		ojbNamespaceContext.populateRootNamespaceDeclarations(rootElement);
		
		return fbiSubReqDoc;
	}

}
