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
package org.ojbc.intermediaries.sn.dao.rapback;

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_41;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_PREFIX_SUB_MSG_EXT;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_SUB_MSG_EXT;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FbiSubModDocBuilder {
	
	
	private static final String YYYY_MM_DD = "yyyy-MM-dd";

	private static final Logger logger = Logger.getLogger(FbiSubModDocBuilder.class);
	
	private static final OjbcNamespaceContext CTXT = new OjbcNamespaceContext();
	
	
	public Document buildFbiSubModDoc( FbiRapbackSubscription fbiRapbackSubscription, Document subscripitonDoc ) throws Exception{
				
		Document doc = OJBCXMLUtils.createDocument();
		
		Element modifyElement = doc.createElementNS(OjbcNamespaceContext.NS_B2, "Modify");
		modifyElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);	
		
		modifyElement.setAttribute("xmlns:jxdm41", NS_JXDM_41 );
		modifyElement.setAttribute("xmlns:" + NS_PREFIX_SUB_MSG_EXT, NS_SUB_MSG_EXT );
		modifyElement.setAttribute("xmlns:nc", NS_NC);

		doc.appendChild(modifyElement);
				
		Element subModMsgElement = XmlUtils.appendElement(modifyElement, OjbcNamespaceContext.NS_SUB_MODIFY_MESSAGE, "SubscriptionModificationMessage");
		
		Element subscriptionMessage = (Element) XmlUtils.xPathNodeSearch(subscripitonDoc, "/b-2:Subscribe/submsg-exch:SubscriptionMessage");
		Node subscriptionRelatedCaseIdentification = XmlUtils.xPathNodeSearch(subscriptionMessage, 
				"submsg-ext:SubscriptionRelatedCaseIdentification"); 
		subModMsgElement.appendChild(doc.importNode(subscriptionRelatedCaseIdentification, true));
		
		Node subscribingOrganization = XmlUtils.xPathNodeSearch(subscriptionMessage, "submsg-ext:SubscribingOrganization");
		if (subscribingOrganization != null){
			subModMsgElement.appendChild(doc.importNode(subscribingOrganization,true));
		}
		
		Node subject = XmlUtils.xPathNodeSearch(subscriptionMessage, "submsg-ext:Subject");
		if (subject != null){
			subModMsgElement.appendChild(doc.importNode(subject, true));
		}
				
		Node subscriptionQualifierIdentification = XmlUtils.xPathNodeSearch(subscriptionMessage, "submsg-ext:SubscriptionQualifierIdentification");
		if (subscriptionQualifierIdentification != null){
			subModMsgElement.appendChild(doc.importNode(subscriptionQualifierIdentification, true));
		}
		
		Element subscriptionIdentification = 
				XmlUtils.appendElement(subModMsgElement, NS_SUB_MSG_EXT, "SubscriptionIdentification");
		XmlUtils.appendTextElement(
				subscriptionIdentification, NS_NC, "IdentificationID", fbiRapbackSubscription.getStateSubscriptionId().toString());

		Node subscriptionReasonCode = XmlUtils.xPathNodeSearch(subscriptionMessage, 
				"submsg-ext:CriminalSubscriptionReasonCode | submsg-ext:CivilSubscriptionReasonCode");
		if (subscriptionReasonCode != null){
			subModMsgElement.appendChild(doc.importNode(subscriptionReasonCode, true));
		}
		
		Node triggeringEvents = XmlUtils.xPathNodeSearch(subscriptionMessage, "submsg-ext:TriggeringEvents");
		if (triggeringEvents != null){
			subModMsgElement.appendChild(doc.importNode(triggeringEvents, true));
		}
		
		Node federalRapSheetDisclosure = XmlUtils.xPathNodeSearch(subscriptionMessage, "submsg-ext:FederalRapSheetDisclosure");
		if (federalRapSheetDisclosure != null){
			subModMsgElement.appendChild(doc.importNode(federalRapSheetDisclosure, true));
		}
		
		Element fbiSubscription = buildFBISubscriptionElement(subModMsgElement, fbiRapbackSubscription);
		if (subscriptionReasonCode != null){
			fbiSubscription.appendChild(doc.importNode(subscriptionReasonCode, true));
		}
		
		buildSubModEndDateElement(subModMsgElement, fbiRapbackSubscription);				
		
		CTXT.populateRootNamespaceDeclarations(doc.getDocumentElement());
		
		return doc;
	}


	private Element buildFBISubscriptionElement(Element parentElement, FbiRapbackSubscription fbiRapbackSubscription){
	
		Element fbiSubscriptionElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "FBISubscription");
			
		Element subscriptionFBIIdentification = 
				XmlUtils.appendElement(fbiSubscriptionElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionFBIIdentification");
		XmlUtils.appendTextElement(subscriptionFBIIdentification, OjbcNamespaceContext.NS_NC, 
				"IdentificationID", fbiRapbackSubscription.getFbiSubscriptionId());
		
		return fbiSubscriptionElement;
	}
	
	
	private void buildSubModEndDateElement(Element parentElement, FbiRapbackSubscription fbiRapbackSubscription){
		
		DateTime subModEndDate = fbiRapbackSubscription.getRapbackExpirationDate();
						
		if(subModEndDate != null){
			
			Element subModElement = XmlUtils.appendElement(parentElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionModification");
			
			Element dateRangeElement = XmlUtils.appendElement(subModElement, OjbcNamespaceContext.NS_NC, "DateRange");
			
			Element endDateElement = XmlUtils.appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC, "EndDate");
			
			Element endDateValElement = XmlUtils.appendElement(endDateElement, OjbcNamespaceContext.NS_NC, "Date");
			
			try{
				String sSubModEndDate = subModEndDate.toString(YYYY_MM_DD);
				
				endDateValElement.setTextContent(sSubModEndDate);
				
			}catch(Exception e){
				logger.error("Cannot format date");
			}		
		}				
	}

}
