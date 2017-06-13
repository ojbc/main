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
package org.ojbc.intermediaries.sn;

import static org.ojbc.util.xml.OjbcNamespaceContext.NS_JXDM_41;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_NC;
import static org.ojbc.util.xml.OjbcNamespaceContext.NS_SUB_MSG_EXT;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.dao.Subscription;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackSubscription;
import org.ojbc.intermediaries.sn.dao.rapback.FbiSubModDocBuilder;
import org.ojbc.intermediaries.sn.subscription.SubscriptionMessageProcessor;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestSubscriptionRequest;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Value;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FbiSubscriptionProcessor extends SubscriptionMessageProcessor {
	
    private static final Log log = LogFactory.getLog(FbiSubscriptionProcessor.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Resource(name="rapbackDao")
	private FbiRapbackDao rapbackDao;	
	
	@Resource(name="subscriptionSearchQueryDAO")
	private SubscriptionSearchQueryDAO subscriptionDAO;	
	
    @Value("${publishSubscribe.fbiSubscriptionMember:false}")
    private Boolean fbiSubscriptionMember;
    
	FbiSubModDocBuilder fbiSubModDocBuilder; 
	
    public FbiSubscriptionProcessor(){
    	super();
    	fbiSubModDocBuilder = new FbiSubModDocBuilder();
    }

	public void prepareUnsubscribeMessageForFbiEbts(@Body Document document) throws Exception{
				
		String subscriptionId = XmlUtils.xPathStringSearch(document, 
				"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:SubscriptionIdentification/nc:IdentificationID");
				
		Subscription subscription = subscriptionDAO.findSubscriptionWithFbiInfoBySubscriptionId(subscriptionId);
		log.info("Prepare to unsubscribe Subscription:" + subscription);
		
		appendFbiSubscriptionInfoToUnsubscribeMessage(document, subscription);
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(document.getDocumentElement());
	}
	

	private void appendFbiSubscriptionInfoToUnsubscribeMessage(
			Document document, Subscription subscription) throws Exception {
		Element  unsubscriptionMessage = 
				(Element)XmlUtils.xPathNodeSearch(document, "/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage");
		Element subject = XmlUtils.appendElement(unsubscriptionMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "Subject");
		appendPersonInfo(subject, subscription);
		appendSubscribingOriToUnsubscribeMessage(unsubscriptionMessage, subscription.getOri());
		appendFbiSubscriptionId(unsubscriptionMessage, subscription.getFbiRapbackSubscription().getFbiSubscriptionId());
	}


	private void appendSubscribingOriToUnsubscribeMessage(Element parent, String ori) throws Exception {
		Element subscribingOrganization = XmlUtils.appendElement(parent, NS_SUB_MSG_EXT, "SubscribingOrganization");
		Element organizationAugmentation = XmlUtils.appendElement(subscribingOrganization, NS_JXDM_41, "OrganizationAugmentation");
		Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, NS_JXDM_41, "OrganizationORIIdentification");
		Element identificationID = XmlUtils.appendElement(organizationORIIdentification, NS_NC, "IdentificationID");
		identificationID.setTextContent(ori);
	}


	
	public String getReasonCodeFromUnsubscribeDoc(Document unsubscribeDoc) throws Exception{

		String categoryReasonCode = XmlUtils.xPathStringSearch(unsubscribeDoc, 
				"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:CriminalSubscriptionReasonCode|"
				+ "/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:CivilSubscriptionReasonCode");		
		
		return categoryReasonCode;
	}
	
	
	public String getPersonFbiUcnIdFromUnsubscribeDoc(Document unsubscribeDoc) throws Exception{
		
		String personFbiUcnId = null;
		
		try{		
			personFbiUcnId = XmlUtils.xPathStringSearch(unsubscribeDoc, 
					"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
		
		}catch(Exception e){
			log.warn("\n\n\n Exception: " + e.getMessage() + "\n\n from doc: \n\n ");
			
			XmlUtils.printNode(unsubscribeDoc);
		}				
		return personFbiUcnId;
	}
	
	
	public SubscriptionRequest getSubReqFromSubDoc(Document subDoc) throws Exception{
		
		// create a sub-optimal Message wrapper so the doc-creation constructor can be reused below 
		Message msg = new DefaultMessage();		
		msg.setBody(subDoc);
		
		SubscriptionRequest subRequest = new ArrestSubscriptionRequest(msg, null);
				
		return subRequest;
	}
	
	public Date getSubReqEndDate(Document subscriptionDoc) throws Exception{
		
		SubscriptionRequest subReq = getSubReqFromSubDoc(subscriptionDoc);
		
		String stateSubEndDate = subReq.getEndDateString();
		
		Date subEndDate = null;
		
		try{
			subEndDate = sdf.parse(stateSubEndDate);
			
		}catch(Exception e){
			
			log.warn("Could not parse end date format! for: " + stateSubEndDate + " -> " + e.getMessage());
		}		
		return subEndDate;
	}
	
	// design: multiple returns were cleaner than nested conditionals
	//
	public Document processSubscription(Exchange exchange, @Header("subscriptionId") Integer subscriptionId, 
			Document subscriptionDoc) throws Exception{
		
		log.info("\n\n processSubscription...\n\n");		
		
		FbiRapbackSubscription fbiRapbackSubscription = null;
		
		if( subscriptionId != null && subscriptionId > 0){
			fbiRapbackSubscription = rapbackDao.getFbiRapbackSubscription(subscriptionId);
			
		}else{								
			log.warn(" Can't lookup with invalid subscription Id " + subscriptionId == null? "null": subscriptionId);
			return subscriptionDoc;
		}
				
		if(fbiRapbackSubscription == null){
			// negative coupling, but lets the caller know what endpoint to send to
			exchange.getIn().setHeader("operationName", "Subscribe");
			
			appendSubscriptionId(subscriptionDoc, subscriptionId);
			return subscriptionDoc;			
		}
					
		Document subscriptionModifyDoc = fbiSubModDocBuilder.buildFbiSubModDoc(fbiRapbackSubscription, subscriptionDoc);
		exchange.getIn().setHeader("operationName", "Modify");
		
		return subscriptionModifyDoc;
				
	}
	

	private void appendSubscriptionId(Document subscriptionDoc,
			Integer subscriptionId) throws Exception {
		
		Element parentNode = (Element) XmlUtils.xPathNodeSearch(subscriptionDoc,
				"/b-2:Subscribe/submsg-exch:SubscriptionMessage");
		Element reasonCodeNode = (Element) XmlUtils.xPathNodeSearch(parentNode,
				"submsg-ext:CriminalSubscriptionReasonCode | submsg-ext:CivilSubscriptionReasonCode  ");								

	
		Element subscriptionIdentification = 
				XmlUtils.insertElementBefore(parentNode, reasonCodeNode, NS_SUB_MSG_EXT, "SubscriptionIdentification");
		
		XmlUtils.appendTextElement(subscriptionIdentification, NS_NC, "IdentificationID", subscriptionId.toString());
	}

	/**
	 * Anticipated to be received from the portal manual subscriptions (not from intermediary auto subscriptions)
	 */
	public Document appendFbiUcnIdToUnsubscribeDoc(Document unsubscribeDoc, String fbiUcnId) throws Exception{
		
		log.info("\n\n\n appendFbiUcnIdToUnsubscribeDoc... \n\n\n");
																
		Node  unsubMsgElement = XmlUtils.xPathNodeSearch(unsubscribeDoc, "/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage");
						
		Node subjNode = XmlUtils.xPathNodeSearch(unsubMsgElement, "submsg-ext:Subject");
		if(subjNode != null){
			throw new Exception("Unexpected existance of Subject node.  Appending fbi data would have created duplicate subject node"
					+ "(corrupting doc). Requirements must have changed to arrive here.");
		}
															
		if(StringUtils.isNotEmpty(fbiUcnId)){
			
			Element subjectElement = unsubscribeDoc.createElementNS(OjbcNamespaceContext.NS_SUB_MSG_EXT, "Subject");
			subjectElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_SUB_MSG_EXT);
			unsubMsgElement.appendChild(subjectElement);			
		
			Element personAugmentElement = XmlUtils.appendElement(subjectElement, OjbcNamespaceContext.NS_JXDM_41, "PersonAugmentation");
			
			Element personFbiIdElement = XmlUtils.appendElement(personAugmentElement, OjbcNamespaceContext.NS_JXDM_41, "PersonFBIIdentification");
			
			Element personFbiIdValElement = XmlUtils.appendElement(personFbiIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
		
			personFbiIdValElement.setTextContent(fbiUcnId);
		}		
		
		OjbcNamespaceContext ojbNsCtxt = new OjbcNamespaceContext();
		ojbNsCtxt.populateRootNamespaceDeclarations(unsubscribeDoc.getDocumentElement());
						
		return unsubscribeDoc;
	}	
	
	
	public void appendFbiSubscriptionId(Element parent, String fbiSubId) throws Exception{
		if(StringUtils.isNotEmpty(fbiSubId)){
			Element relatedFBISubscription = XmlUtils.appendElement(parent, NS_SUB_MSG_EXT, "RelatedFBISubscription");
			
			Element subscriptionFBIIdentification = XmlUtils.appendElement(relatedFBISubscription, NS_SUB_MSG_EXT, 
					"SubscriptionFBIIdentification");
			
			XmlUtils.appendTextElement(subscriptionFBIIdentification, OjbcNamespaceContext.NS_NC, "IdentificationID", fbiSubId);
		}		
	}	
	
	
	public Document appendFbiDataToSubscriptionDoc(Document subscriptionDoc, FbiRapbackSubscription fbiRapbackSubscription) throws Exception{
				
		log.info("appendFbiDataToSubscriptionDoc...");
		
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
	
	
		
	public Boolean routeToProcessFbiUnsubscribeRoute(@Body Document document) throws Exception{
		Boolean hasFbiSubscription = false; 
		
		if (BooleanUtils.isTrue(fbiSubscriptionMember)){
			String subscriptionIdString = XmlUtils.xPathStringSearch(document, 
					"//unsubmsg-exch:UnsubscriptionMessage/submsg-ext:SubscriptionIdentification/nc:IdentificationID");
			
			if (StringUtils.isNotBlank(subscriptionIdString)){
				hasFbiSubscription = rapbackDao.hasFbiSubscription(new Integer(subscriptionIdString));
			}
		}
		
		return hasFbiSubscription;
	}
	
	/**
	 * Decide whether to send to EBTS adapter.  
	 * @param document
	 * @return true 
	 * if <pre> 
	 * 	 1. fbiSubscriptionMember is true && UCN is not empty && is criminal subscription 
	 * 	 2. Or  ( fbiSubscriptionMember is true && UCN is not empty && is civil subscription && owner ORI is FBI subscripiton qualified.) 
	 * </pre> 
	 * @throws Exception
	 */
	public Boolean routeToProcessFbiSubscriptionRoute(@Body Document document) throws Exception{
		
		String ucn = XmlUtils.xPathStringSearch(document, 
				"//submsg-exch:SubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");
		
		if (BooleanUtils.isTrue(fbiSubscriptionMember) && StringUtils.isNotBlank(ucn)){
			
			String criminalSubscriptionReasonCode = XmlUtils.xPathStringSearch(document, 
					"//submsg-exch:SubscriptionMessage/submsg-ext:CriminalSubscriptionReasonCode");
			if (StringUtils.isNotBlank(criminalSubscriptionReasonCode)){
				return true;
			}
			
			String civilSubscriptionReasonCode = XmlUtils.xPathStringSearch(document, 
					"//submsg-exch:SubscriptionMessage/submsg-ext:CivilSubscriptionReasonCode");

			if (StringUtils.isNotBlank(civilSubscriptionReasonCode)){
				String transactionNumber = XmlUtils.xPathStringSearch(document, 
						"//submsg-exch:SubscriptionMessage/submsg-ext:SubscriptionRelatedCaseIdentification/nc:IdentificationID");
				Boolean fbiSubscriptionQualification = rapbackDao.getfbiSubscriptionQualification(transactionNumber);
				
				return BooleanUtils.isTrue(fbiSubscriptionQualification);
			}
		}
		
		return false;
	}
}

