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
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultMessage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.dao.rapback.FbiRapbackDao;
import org.ojbc.intermediaries.sn.dao.rapback.FbiSubModDocBuilder;
import org.ojbc.intermediaries.sn.subscription.SubscriptionMessageProcessor;
import org.ojbc.intermediaries.sn.subscription.SubscriptionRequest;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestSubscriptionRequest;
import org.ojbc.util.model.rapback.FbiRapbackSubscription;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class FbiSubscriptionProcessor extends SubscriptionMessageProcessor {
	
    private static final Log log = LogFactory.getLog(FbiSubscriptionProcessor.class);
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private FbiRapbackDao rapbackDao;	
	
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;	
	
    private Boolean fbiSubscriptionMember;
    
    private List<String> nonFbiSubscriptionReasonCodes;
    
	FbiSubModDocBuilder fbiSubModDocBuilder; 
	
    public FbiSubscriptionProcessor(){
    	super();
    	fbiSubModDocBuilder = new FbiSubModDocBuilder();
    }

	public Document prepareUnsubscribeMessageForFbiEbts(@Body Document document) throws Exception{
				
		String subscriptionId = XmlUtils.xPathStringSearch(document, 
				"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:SubscriptionIdentification/nc:IdentificationID");
				
		Subscription subscription = subscriptionSearchQueryDAO.findSubscriptionWithFbiInfoBySubscriptionId(subscriptionId);
		log.info("Prepare to unsubscribe Subscription:" + subscription);
		
		appendFbiSubscriptionInfoToUnsubscribeMessage(document, subscription);
		
		ojbcNamespaceContext.populateRootNamespaceDeclarations(document.getDocumentElement());
		return document;
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
			
			log.info("No FBI Subscription, update FBI subscription status to pending for: " + subscriptionId);
			
			subscriptionSearchQueryDAO.updateFbiSubscriptionStatus(subscriptionId, "PENDING");
			
			return subscriptionDoc;			
		}
					
		Document subscriptionModifyDoc = fbiSubModDocBuilder.buildFbiSubModDoc(fbiRapbackSubscription, subscriptionDoc);
		exchange.getIn().setHeader("operationName", "Modify");
		
		return subscriptionModifyDoc;
				
	}
	
	public Document buildValidationModifyMessage(@Header("subscriptionId") Integer subscriptionId, 
			@Header("subscription") Subscription subscription,
			@Header("validationDueDateString") String validationDueDateString,
			@Body Document validationDoc) throws Exception{
		
		log.info("\n\n processSubscription...\n\n");	
		
		if (subscriptionId == null || subscriptionId <= 0 || subscription == null || subscription.getFbiRapbackSubscription() == null) {
			log.warn("Can't find subscription with FBI subscripiton for  " + subscriptionId == null? "null": subscriptionId);
			return validationDoc;
		}
		
		Document subscriptionModifyDoc = fbiSubModDocBuilder.buildModifyMessageWithSubscripiton(subscription, validationDueDateString);
		
		return subscriptionModifyDoc;
		
	}
	

	private void appendSubscriptionId(Document subscriptionDoc,
			Integer subscriptionId) throws Exception {
		
		Element parentNode = (Element) XmlUtils.xPathNodeSearch(subscriptionDoc,
				"/b-2:Subscribe/submsg-exch:SubscriptionMessage");
		Element reasonCodeNode = (Element) XmlUtils.xPathNodeSearch(parentNode,
				"submsg-ext:CriminalSubscriptionReasonCode | submsg-ext:CivilSubscriptionReasonCode  ");								

	
		String subscriptionIdFromMessage = 
				XmlUtils.xPathStringSearch(parentNode, "submsg-ext:SubscriptionIdentification/nc:IdentificationID");
		
		if (StringUtils.isBlank(subscriptionIdFromMessage)){
			Element subscriptionIdentification = 
					XmlUtils.insertElementBefore(parentNode, reasonCodeNode, NS_SUB_MSG_EXT, "SubscriptionIdentification");
			
			XmlUtils.appendTextElement(subscriptionIdentification, NS_NC, "IdentificationID", subscriptionId.toString());
		}
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
		
		LocalDate jtStartDate = fbiRapbackSubscription.getRapbackStartDate();
		
		if(jtStartDate != null){
			Element startDateElement = XmlUtils.appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC, "StartDate");		
			Element startDateValElement = XmlUtils.appendElement(startDateElement, OjbcNamespaceContext.NS_NC, "Date");																	
			startDateValElement.setTextContent(jtStartDate.toString());			
		}
						
		LocalDate jtEndDate = fbiRapbackSubscription.getRapbackExpirationDate();
		
		if(jtEndDate != null){
			Element endDateElement = XmlUtils.appendElement(dateRangeElement, OjbcNamespaceContext.NS_NC, "EndDate");
			Element endDateValElement = XmlUtils.appendElement(endDateElement, OjbcNamespaceContext.NS_NC, "Date");								
			endDateValElement.setTextContent(jtEndDate.toString());			
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
	
	
		
	public Boolean hasFbiSubscription( @Header("subscriptionId") Integer subscriptionId) throws Exception{
		Boolean hasFbiSubscription = false; 
		
		if (BooleanUtils.isTrue(fbiSubscriptionMember)){
			hasFbiSubscription = rapbackDao.hasFbiSubscription(subscriptionId);
		}
		
		return hasFbiSubscription;
	}
	
	public void retrieveFingerprintToFile(Exchange exchange, @Header("transactionNumber") String transactionNumber, @Header("encoded") Boolean encoded) throws Exception {
		
		log.info("Retrieving finger prints for transaction number: " + transactionNumber!=null?transactionNumber: "");
		log.info("The finger prints file will contain base 64 encoded content: " + BooleanUtils.isTrue(encoded));
		
		byte[] fingerprintByteArray = rapbackDao.getCivilFingerPrints(transactionNumber);
		
		if (BooleanUtils.isTrue(encoded)) {
			exchange.getIn().setBody(Base64.encodeBase64String(fingerprintByteArray), String.class);
		}
		else {
			exchange.getIn().setBody(fingerprintByteArray, byte[].class);
		}
		
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
		
		log.info("UCN: " + ucn + ", FBI subscription member: " + fbiSubscriptionMember);
		
		if (BooleanUtils.isTrue(fbiSubscriptionMember) && StringUtils.isNotBlank(ucn)){
			
			String criminalSubscriptionReasonCode = XmlUtils.xPathStringSearch(document, 
					"//submsg-exch:SubscriptionMessage/submsg-ext:CriminalSubscriptionReasonCode");
			
			log.info("Criminal Subscription Reason Code: " + criminalSubscriptionReasonCode + ", nonFbiSubscriptionReasonCodes" + nonFbiSubscriptionReasonCodes);
			
			if (StringUtils.isNotBlank(criminalSubscriptionReasonCode) && 
					!nonFbiSubscriptionReasonCodes.contains(criminalSubscriptionReasonCode)){
				return true;
			}
			
			String civilSubscriptionReasonCode = XmlUtils.xPathStringSearch(document, 
					"//submsg-exch:SubscriptionMessage/submsg-ext:CivilSubscriptionReasonCode");
			log.info("Civil Subscription Reason Code: " + civilSubscriptionReasonCode );
			
			if (StringUtils.isNotBlank(civilSubscriptionReasonCode) 
					&& !nonFbiSubscriptionReasonCodes.contains(civilSubscriptionReasonCode)){
				String transactionNumber = XmlUtils.xPathStringSearch(document, 
						"//submsg-exch:SubscriptionMessage/submsg-ext:FingerprintIdentificationTransactionIdentification/nc:IdentificationID");
				Boolean fbiSubscriptionQualification = rapbackDao.getfbiSubscriptionQualification(transactionNumber);
				
				log.info("FBI Subscription Qualification: " + fbiSubscriptionQualification );
				
				//Add civil fingerprints here
	 			byte[] civilFingerPrint = rapbackDao.getCivilFingerPrints(transactionNumber);
				
				if (civilFingerPrint != null)
				{
					Element subscriptionMessage = (Element) XmlUtils.xPathNodeSearch(document, "//submsg-exch:SubscriptionMessage");
					
					Element fingerPrintDocument = XmlUtils.appendElement(subscriptionMessage, OjbcNamespaceContext.NS_SUB_MSG_EXT, "submsg-ext:FingerprintDocument");
					
					Element documentBinary = XmlUtils.appendElement(fingerPrintDocument, OjbcNamespaceContext.NS_NC, "nc:DocumentBinary");
					
					Element base64BinaryObject = XmlUtils.appendElement(documentBinary, OjbcNamespaceContext.NS_SUB_MSG_EXT, "submsg-ext:Base64BinaryObject");
					
					base64BinaryObject.setTextContent(Base64.encodeBase64String(civilFingerPrint));
					
				}	
				
				
				return BooleanUtils.isTrue(fbiSubscriptionQualification);
			}
		}
		
		return false;
	}

	public FbiRapbackDao getRapbackDao() {
		return rapbackDao;
	}

	public void setRapbackDao(FbiRapbackDao rapbackDao) {
		this.rapbackDao = rapbackDao;
	}

	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}

	public void setSubscriptionSearchQueryDAO(
			SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}

	public Boolean getFbiSubscriptionMember() {
		return fbiSubscriptionMember;
	}

	public void setFbiSubscriptionMember(Boolean fbiSubscriptionMember) {
		this.fbiSubscriptionMember = fbiSubscriptionMember;
	}

	public List<String> getNonFbiSubscriptionReasonCodes() {
		return nonFbiSubscriptionReasonCodes;
	}

	public void setNonFbiSubscriptionReasonCodes(
			List<String> nonFbiSubscriptionReasonCodes) {
		this.nonFbiSubscriptionReasonCodes = nonFbiSubscriptionReasonCodes;
	}
}

