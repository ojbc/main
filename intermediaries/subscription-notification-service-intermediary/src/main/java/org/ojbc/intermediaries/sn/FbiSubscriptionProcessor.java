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
package org.ojbc.intermediaries.sn;

import java.text.SimpleDateFormat;
import java.util.logging.Logger;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.fbi.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.fbi.rapback.FbiRapbackSubscription;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FbiSubscriptionProcessor {
	
	private static final Logger logger = Logger.getLogger(FbiSubscriptionProcessor.class.getName());
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Resource(name="rapbackDao")
	private FbiRapbackDao rapbackDao;
	
	
	public boolean hasStateSubscriptionsForFbiUcnId(Exchange exchange) throws Exception{
		
		logger.info("\n\n\n Process Unsubscribe... \n\n\n");
		
		Document unsubscribeDoc = exchange.getIn().getHeader("unsubscribeBody", Document.class);
		
		String personfbiUcnId = null; // TODO XmlUtils.xPathStringSearch(unsubscribeDoc, "TODO");
		
		int countStateSubscriptionsWithFbiUcnId = rapbackDao.countStateSubscriptionsHavingFbiUcnId(personfbiUcnId);
		
		// TODO enable when query works off fbi id or something else 
		boolean hasStateSubscriptionsForFbiUcnId = true; //countStateSubscriptionsWithFbiUcnId > 0;
		
		return hasStateSubscriptionsForFbiUcnId;
	}
	
	
	public Document processSubscription(Document subscriptionDoc) throws Exception{
		
		logger.info("\n\n processSubscription...\n\n");		
		
		String fbiIdUcn = XmlUtils.xPathStringSearch(subscriptionDoc,
				"/b-2:Subscribe/submsg-exch:SubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");								
		
		String subPurposeCategory = XmlUtils.xPathStringSearch(subscriptionDoc,
				"/b-2:Subscribe/submsg-exch:SubscriptionMessage/submsg-ext:CriminalSubscriptionReasonCode");
						
		Document rSubscriptionDoc = subscriptionDoc;
		
		if(StringUtils.isNotEmpty(fbiIdUcn) && StringUtils.isNotEmpty(subPurposeCategory)){
			
			FbiRapbackSubscription fbiRapbackSubscription = getFbiSubscriptionFromRapbackDataStore(fbiIdUcn, subPurposeCategory);
			
			//we're editing an existing fbi subscription, so add 'fbi data section' of elements at bottom of doc
			if(fbiRapbackSubscription != null){
				
				// add fbi data to subscription message so a new subscription gets created at the fbi								
				rSubscriptionDoc = appendFbiDataToSubscriptionDoc(subscriptionDoc, fbiRapbackSubscription);	
			}					
		}else{
			logger.warning("\n\n Not looking up rapback datastore. fbiIdUcn: " 
					+ fbiIdUcn + ", purposeCategory: " + subPurposeCategory + "\n\n");
			
		}
						
		return rSubscriptionDoc;
	}
	

	public Document appendFbiDataToSubscriptionDoc(Document subscriptionDoc, FbiRapbackSubscription fbiRapbackSubscription) throws Exception{
				
		logger.info("appendFbiDataToSubscriptionDoc...");
		
		Element relatedFBISubscriptionElement = subscriptionDoc.createElementNS(OjbcNamespaceContext.NS_SUB_MSG_EXT, "RelatedFBISubscription");
		relatedFBISubscriptionElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_SUB_MSG_EXT);						
		
		Node subMsgNode = XmlUtils.xPathNodeSearch(subscriptionDoc, "//submsg-exch:SubscriptionMessage");
				
		subMsgNode.appendChild(relatedFBISubscriptionElement);		
				
		Element dateRangeElement = XmlUtils.appendElement(relatedFBISubscriptionElement, OjbcNamespaceContext.NS_NC, "DateRange");				
		
		DateTime jtStartDate = fbiRapbackSubscription.getRapbackStartDate();
		
		if(jtStartDate != null){
			Element startDateElement = XmlUtils.appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC, "StartDate");		
			Element startDateValElement = XmlUtils.appendElement(startDateElement, OjbcNamespaceContext.NS_NC, "Date");																	
			String sStartDate = sdf.format(jtStartDate.toDate());			
			startDateValElement.setTextContent(sStartDate);			
		}
						
		DateTime jtEndDate = fbiRapbackSubscription.getRapbackExpirationDate();
		
		if(jtEndDate != null){
			Element endDateElement = XmlUtils.appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC, "EndDate");
			Element endDateValElement = XmlUtils.appendElement(endDateElement, OjbcNamespaceContext.NS_NC, "Date");								
			String sEndDate = sdf.format(jtEndDate.toDate());
			endDateValElement.setTextContent(sEndDate);			
		}
									
		String fbiId = fbiRapbackSubscription.getFbiSubscriptionId();
		
		if(StringUtils.isNotEmpty(fbiId)){
			Element subFbiIdElement = XmlUtils.appendElement(relatedFBISubscriptionElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionFBIIdentification");
			Element fbiIdValElement = XmlUtils.appendElement(subFbiIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
			fbiIdValElement.setTextContent(fbiId);			
		}		
				
		String reasonCode = fbiRapbackSubscription.getRapbackCategory();
		
		if(StringUtils.isNotEmpty(reasonCode)){
			Element reasonCodeElement = XmlUtils.appendElement(relatedFBISubscriptionElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "CriminalSubscriptionReasonCode");
			reasonCodeElement.setTextContent(reasonCode);			
		}
				
		String subTerm = fbiRapbackSubscription.getSubscriptionTerm();
		
		if(StringUtils.isNotEmpty(subTerm)){
			Element subscriptionTermElement = XmlUtils.appendElement(relatedFBISubscriptionElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, "SubscriptionTerm");			
			Element termDurationElement = XmlUtils.appendElement(subscriptionTermElement, OjbcNamespaceContext.NS_JXDM_41, "TermDuration");		
			termDurationElement.setTextContent(subTerm);			
		}		
								
		OjbcNamespaceContext ojbNsCtxt = new OjbcNamespaceContext();
		ojbNsCtxt.populateRootNamespaceDeclarations(subscriptionDoc.getDocumentElement());
						
		return subscriptionDoc;
	}
	
	
		
	private FbiRapbackSubscription getFbiSubscriptionFromRapbackDataStore(String fbiIdUcn, String category){				
		
		if(StringUtils.isEmpty(fbiIdUcn) || StringUtils.isEmpty(category)){
			return null;
		}
				
		FbiRapbackSubscription fbiRapbackSubscription = null;
				
		try{
			fbiRapbackSubscription = rapbackDao.getFbiRapbackSubscription(category, fbiIdUcn);
			
			logger.info("\n\n\n Received FbiRapbackSubscription: \n\n" + fbiRapbackSubscription + "\n\n\n");
			
		}catch(Exception e){
			
			logger.severe("\n\n Unable to get fbi rapback subscription in query! \n\n" + e.getMessage());			
		}
								
		return fbiRapbackSubscription;
	}
		
}

