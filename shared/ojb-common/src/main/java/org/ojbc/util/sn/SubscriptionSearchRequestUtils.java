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
package org.ojbc.util.sn;

import java.util.Objects;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class SubscriptionSearchRequestUtils {
	
	private static final Log log = LogFactory.getLog(SubscriptionSearchRequestUtils.class);

    public static SubscriptionSearchRequest parseSubscriptionSearchRequest(
			Document request) throws Exception {
    	SubscriptionSearchRequest subscriptionSearchRequest = new SubscriptionSearchRequest();
    	String adminSearchIndicator = XmlUtils.xPathStringSearch(request, "/ssreq:SubscriptionSearchRequest/ssreq-ext:AdminSearchRequestIndicator");
    	subscriptionSearchRequest.setAdminSearch(BooleanUtils.toBooleanObject(adminSearchIndicator));
    	
    	String requestActiveSubscriptionsIndicator = XmlUtils.xPathStringSearch(request, "/ssreq:SubscriptionSearchRequest/ssreq-ext:RequestActiveSubscriptionsIndicator");
    	String requestInactiveSubscriptionsIndicator = XmlUtils.xPathStringSearch(request, "/ssreq:SubscriptionSearchRequest/ssreq-ext:RequestInactiveSubscriptionsIndicator");
    	
    	if (!Objects.equals(BooleanUtils.toBoolean(requestActiveSubscriptionsIndicator), 
    			BooleanUtils.toBoolean(requestInactiveSubscriptionsIndicator))){
    		
    		if(BooleanUtils.toBoolean(requestActiveSubscriptionsIndicator)){
    			subscriptionSearchRequest.setActive(true);
    		}
    		
    		if(BooleanUtils.toBoolean(requestInactiveSubscriptionsIndicator)){
    			subscriptionSearchRequest.setActive(false);
    		}
    	}
    	
    	if (BooleanUtils.isNotTrue(subscriptionSearchRequest.getActive())){
    		subscriptionSearchRequest.setIncludeExpiredSubscriptions(true);
    	}
    	
    	String ownerFirstName = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:SubscribedEntity/nc:EntityPerson/nc:PersonName/nc:PersonGivenName");
    	subscriptionSearchRequest.setOwnerFirstName(ownerFirstName);
    	
    	String ownerLastName = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:SubscribedEntity/nc:EntityPerson/nc:PersonName/nc:PersonSurName");
    	subscriptionSearchRequest.setOwnerLastName(ownerLastName);
    	
    	String ownerFederatedId = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:SubscribedEntity/ssreq-ext:SubscribedEntityFederatedIdentification/nc:IdentificationID");
    	subscriptionSearchRequest.setOwnerFederatedId(ownerFederatedId);
    	
    	String ownerOri = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/jxdm41:Organization/jxdm41:OrganizationAugmentation/jxdm41:OrganizationORIIdentification/nc:IdentificationID");
    	subscriptionSearchRequest.setOwnerOri(ownerOri);
    	
    	String subjectFirstName = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:FBISubscription/ssreq-ext:SubscriptionSubject/nc:PersonName/nc:PersonGivenName");
    	subscriptionSearchRequest.setSubjectFirstName(subjectFirstName);
    	if (StringUtils.isNotBlank(subjectFirstName)){
    		subscriptionSearchRequest.getSubjectIdentifiers().put("firstName", subjectFirstName);
    	}
    	
    	String subjectLastName = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:FBISubscription/ssreq-ext:SubscriptionSubject/nc:PersonName/nc:PersonSurName");
    	subscriptionSearchRequest.setSubjectLastName(subjectLastName);
    	if (StringUtils.isNotBlank(subjectLastName)){
    		subscriptionSearchRequest.getSubjectIdentifiers().put("lastName", subjectLastName);
    	}
    	
    	String ucn = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:FBISubscription/ssreq-ext:SubscriptionSubject/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
    	subscriptionSearchRequest.setUcn(ucn);
    	
    	if (StringUtils.isNotBlank(ucn)){
    		subscriptionSearchRequest.getSubjectIdentifiers().put("FBI_ID", ucn);
    	}
    	
    	String sid = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:FBISubscription/ssreq-ext:SubscriptionSubject/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
    	subscriptionSearchRequest.setSid(sid);
    	if(StringUtils.isNotBlank(sid)){
    		subscriptionSearchRequest.getSubjectIdentifiers().put("SID", sid);
    	}
    	
    	NodeList reasonCodeNodeList = XmlUtils.xPathNodeListSearch(request, "/ssreq:SubscriptionSearchRequest/ssreq-ext:FBISubscription/ssreq-ext:CriminalSubscriptionReasonCode");
		if (reasonCodeNodeList != null && reasonCodeNodeList.getLength() > 0){
			for (int i = 0; i < reasonCodeNodeList.getLength(); i++) {
                Element reasonCodeElement = (Element) reasonCodeNodeList.item(i);
				if (StringUtils.isNotBlank(reasonCodeElement.getTextContent())){
					subscriptionSearchRequest.getSubscriptionCategories().add(reasonCodeElement.getTextContent());
				}
	        }
		}
		
		String subscribingSystemIdentifier = XmlUtils.xPathStringSearch(request, 
    			"/ssreq:SubscriptionSearchRequest/ssreq-ext:SubscribedEntity/nc:IdentificationID");
    	subscriptionSearchRequest.setSubscribingSystemIdentifier(subscribingSystemIdentifier);

    	log.info("Parsed subscriptionSearchRequest " + subscriptionSearchRequest);
		return subscriptionSearchRequest;
	}
}
