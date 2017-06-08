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
import java.util.List;

import javax.annotation.Resource;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
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
import org.ojbc.intermediaries.sn.dao.rapback.FbiSubscriptionModification;
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
    
    public FbiSubscriptionProcessor(){
    	super();
    }

	public Document prepareSubscriptionModificationFromUnsubscribe(Exchange unsubscribeExchange) throws Exception{
		
		Document unsubscribeDoc = unsubscribeExchange.getIn().getBody(Document.class);
		
		String personFbiUcnId = XmlUtils.xPathStringSearch(unsubscribeDoc, 
				"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");				
				
		if(StringUtils.isEmpty(personFbiUcnId)){
			
			personFbiUcnId = lookupFbiUcnId(unsubscribeDoc);						
		}
						
		if(StringUtils.isEmpty(personFbiUcnId)){
			throw new Exception("Could not lookup personFbiUcnId. Can't constuct Modify message.");
		}
		
					
		String categoryPurposeReason = XmlUtils.xPathStringSearch(unsubscribeDoc, 
				"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:CriminalSubscriptionReasonCode");		
		
		if(StringUtils.isEmpty(categoryPurposeReason)){
			throw new Exception("Couldn't lookup categoryPurposeReason.  Can't construct Modify message.");
		}
		
					
		FbiRapbackSubscription fbiRapbackSubscription = lookupFbiSubscriptionFromRapbackDataStore(personFbiUcnId, categoryPurposeReason);			
		
		if(fbiRapbackSubscription == null){

			XmlUtils.printNode(unsubscribeDoc);
			
			throw new Exception("Was not able to lookup FbiRapbackSubscription to get related fbi data to add to Modify message");				
		}						

		FbiSubscriptionModification fbiSubMod = new FbiSubscriptionModification();
		
		fbiSubMod.setPersonFbiUcnId(personFbiUcnId);
		fbiSubMod.setReasonCode(categoryPurposeReason);
		fbiSubMod.setSubModEndDate(fbiRapbackSubscription.getRapbackTermDate().toDate());
		fbiSubMod.setSubscriptionFbiId(fbiRapbackSubscription.getFbiSubscriptionId());	
		
		Document subModDoc = prepareSubscriptionMaintenanceMessage(fbiSubMod);
	
		return subModDoc;
	}
	
	
	public Document prepareSubscriptionMaintenanceMessage(FbiSubscriptionModification fbiSubMod) throws Exception{
									
		FbiSubModDocBuilder fbiSubModDocBuilder = new FbiSubModDocBuilder();
		
		Document subscribeMaintenanceDoc = fbiSubModDocBuilder.buildFbiSubModDoc(fbiSubMod);		 				
		
		return subscribeMaintenanceDoc;
	}
	

    public void determineStateSubsEndDateLessThanFbiSubEndDate(Exchange exchange) throws Exception{
    	
    	Document unsubscribeDoc = exchange.getIn().getBody(Document.class);
    	
    	String fbiUcnId = getPersonFbiUcnIdFromUnsubscribeDoc(unsubscribeDoc);
    	
    	String reasonCode = getReasonCodeFromUnsubscribeDoc(unsubscribeDoc);
    	
    	// -------------------------------------------
    	
    	List<Subscription> subscriptionList = rapbackDao.getStateSubscriptions(fbiUcnId, reasonCode);
    	
    	DateTime greatestStateSubscriptionEndDate = getGreatestEndDate(subscriptionList);
    	
    	//------------------------------------
    	
    	FbiRapbackSubscription fbiRapbackSubscription = rapbackDao.getFbiRapbackSubscription(reasonCode, fbiUcnId);
    	
    	DateTime fbiRapBackExpDate = fbiRapbackSubscription.getRapbackExpirationDate();
    	
    	//---------------------------------------------
    	
    	boolean stateSubsEndDateLessThanFbiSubEndDate = greatestStateSubscriptionEndDate.isBefore(fbiRapBackExpDate);
    	
    	exchange.getIn().setHeader("stateSubsEndDateLessThanFbiSubEndDate", stateSubsEndDateLessThanFbiSubEndDate);    	
    }
	
    /**
     * @Deprecated no longer needed  
     * @param subscriptionList
     * @return
     */
    public DateTime getGreatestEndDate(List<Subscription> subscriptionList){
    	
    	if(subscriptionList == null || subscriptionList.isEmpty()){
    		throw new IllegalArgumentException("subscriptionList was null");
    	}
    	
    	DateTime greatestEndDate = subscriptionList.get(0).getEndDate();
    		
		for(Subscription iSubscription : subscriptionList){
		
			DateTime iSubDate = iSubscription.getEndDate();
			
			if(iSubDate != null && iSubDate.isAfter(greatestEndDate)){
			
				greatestEndDate = iSubDate;
			}    		
		}    		    		    	    	    		
    	return greatestEndDate;
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

	/**
	 * TODO not used any more.  Will check later to see if it can be moved elsewhere to be used, remove if not. -hw 
	 * @param parent
	 * @param fbiRapbackSubscription
	 * @throws Exception
	 */
	private void appendRapbackInfo(Element parent,
			FbiRapbackSubscription fbiRapbackSubscription) throws Exception {
		Element rapBackActivityNotificationFormatCode = 
				XmlUtils.appendElement(parent, NS_SUB_MSG_EXT, "RapBackActivityNotificationFormatCode");
		rapBackActivityNotificationFormatCode.setTextContent(fbiRapbackSubscription.getRapbackActivityNotificationFormat());
		
		Element rapBackInStateOptOutIndicator = 
				XmlUtils.appendElement(parent, NS_SUB_MSG_EXT, "RapBackInStateOptOutIndicator");
		rapBackInStateOptOutIndicator.setTextContent(BooleanUtils.toStringTrueFalse(fbiRapbackSubscription.getRapbackOptOutInState()));
		

	}

	private void appendSubscribingOriToUnsubscribeMessage(Element parent, String ori) throws Exception {
		Element subscribingOrganization = XmlUtils.appendElement(parent, NS_SUB_MSG_EXT, "SubscribingOrganization");
		Element organizationAugmentation = XmlUtils.appendElement(subscribingOrganization, NS_JXDM_41, "OrganizationAugmentation");
		Element organizationORIIdentification = XmlUtils.appendElement(organizationAugmentation, NS_JXDM_41, "OrganizationORIIdentification");
		Element identificationID = XmlUtils.appendElement(organizationORIIdentification, NS_NC, "IdentificationID");
		identificationID.setTextContent(ori);
	}


	/**
	 * @deprecated
	 * It is not needed any more since now the state subscription has a one to up-to-one relationship to FBI subscription. 
	 * Will confirm and remove the method once the 6.0 tasks are done.  -hw 
	 * @param exchange
	 * @return
	 * @throws Exception
	 */
	public boolean shouldDeleteFbiSubscription(Exchange exchange) throws Exception{
		
		log.info("\n\n\n Process Unsubscribe... \n\n\n");
		
		Document unsubscribeDoc = exchange.getIn().getBody(Document.class);
		
		String reasonCode = getReasonCodeFromUnsubscribeDoc(unsubscribeDoc);
		
		if(StringUtils.isEmpty(reasonCode)){
			throw new Exception("Reason Code not specified. Can't determine if shouldDeleteFbiSubscription.");
		}
		
		String personFbiUcnId = getPersonFbiUcnIdFromUnsubscribeDoc(unsubscribeDoc);
				
		if(StringUtils.isEmpty(personFbiUcnId)){
		
			log.info("\n\n\n Person FBI UCN ID not provided(probably a manual subscription).  Looking it up now...  \n\n\n");
						
			personFbiUcnId = lookupFbiUcnId(unsubscribeDoc);					
		}
		
		boolean shouldDeleteFbiSubscription = false;
		
		if(StringUtils.isNotEmpty(personFbiUcnId)){
			
			log.info("\n\n\n Using FBI Id: " + personFbiUcnId + "\n\n\n");
		
			int countStateSubscriptionsWithFbiUcnId = rapbackDao.countStateSubscriptions(personFbiUcnId, reasonCode);
			
			log.info("\n\n\n State Subscription Count: " + countStateSubscriptionsWithFbiUcnId + " \n\n\n");
			
			shouldDeleteFbiSubscription = countStateSubscriptionsWithFbiUcnId == 0;
		}else{
			
			log.warn("\n\n\n FbiUcn Id unavailable. \n\n\n");
		}
		
		return shouldDeleteFbiSubscription;
	}
	
	
	
	public String getReasonCodeFromUnsubscribeDoc(Document unsubscribeDoc) throws Exception{

		String categoryReasonCode = XmlUtils.xPathStringSearch(unsubscribeDoc, 
				"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:CriminalSubscriptionReasonCode|"
				+ "/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:CivilSubscriptionReasonCode");		
		
		return categoryReasonCode;
	}
	
	
	private String lookupFbiUcnId(Document unsubscribeDoc) throws Exception{
		
		String unsubscribeSubId = XmlUtils.xPathStringSearch(unsubscribeDoc, 
				"/b-2:Unsubscribe/unsubmsg-exch:UnsubscriptionMessage/submsg-ext:SubscriptionIdentification/nc:IdentificationID");								

		String categoryReasonCode = getReasonCodeFromUnsubscribeDoc(unsubscribeDoc);
		
		String personFbiUcnId = null;
		
		if(StringUtils.isNotEmpty(unsubscribeSubId) && StringUtils.isNotEmpty(categoryReasonCode)){
			
			log.info("\n\n\n Calling fbi rapback dao to get person fbi ucn id for sub. id: " + unsubscribeSubId + 
					" and categoryReasonCode: " + categoryReasonCode + " \n\n\n");
			
			personFbiUcnId = rapbackDao.getFbiUcnIdFromSubIdAndReasonCode(unsubscribeSubId, categoryReasonCode);
			
			log.info("\n\n\n Using personFbiUcnId: " + personFbiUcnId + "\n\n\n");
			
		}else{
			log.warn("\n\n\n\n Don't have both sub. id and reason code.  Not looking up fbi ucn id! \n\n\n");
		}
		
		return personFbiUcnId;		
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
	
	private Date getFbiSubReqEndDate(FbiRapbackSubscription fbiRapbackSubscription) throws Exception{
		
		DateTime jtFbiSubEndDate = fbiRapbackSubscription.getRapbackExpirationDate();	
		
		Date fbiSubEndDate = jtFbiSubEndDate == null ? null : jtFbiSubEndDate.toDate();
		
		return fbiSubEndDate;		
	}
	
	
	// design: multiple returns were cleaner than nested conditionals
	//
	public Document processSubscription(Exchange exchange, Document subscriptionDoc) throws Exception{
		
		log.info("\n\n processSubscription...\n\n");		
		
		String fbiIdUcn = XmlUtils.xPathStringSearch(subscriptionDoc,
				"/b-2:Subscribe/submsg-exch:SubscriptionMessage/submsg-ext:Subject/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");								
		
		String subPurposeCategory = XmlUtils.xPathStringSearch(subscriptionDoc,
				"/b-2:Subscribe/submsg-exch:SubscriptionMessage/submsg-ext:CriminalSubscriptionReasonCode|/b-2:Subscribe/submsg-exch:SubscriptionMessage/submsg-ext:CivilSubscriptionReasonCode");
						
		
		FbiRapbackSubscription fbiRapbackSubscription = null;
		
		if(StringUtils.isNotEmpty(fbiIdUcn) && StringUtils.isNotEmpty(subPurposeCategory)){
			
			fbiRapbackSubscription = lookupFbiSubscriptionFromRapbackDataStore(fbiIdUcn, subPurposeCategory);							
		}else{								
			log.warn("\n\n\n Can't lookup FbiRapbackSubscription because don't have both: fbiIdUcn and subPurposeCategory. Not sending to FBI EBTS!  \n\n\n");
			
			exchange.getIn().setHeader("sendSubToFbiEbtsAdapter", false);
			
			return subscriptionDoc;
		}

				
		if(fbiRapbackSubscription == null){
			
			log.warn("\n\n\n Rapback Datastore returned Nothing for fbi Id: " + fbiIdUcn + " and category: " + subPurposeCategory 
					+ ".  So - Interpreting this as an Add (new FBI subscription) \n\n\n");	
						
			exchange.getIn().setHeader("sendSubToFbiEbtsAdapter", true);
			
			// negative coupling, but lets the caller know what endpoint to send to
			exchange.getIn().setHeader("operationName", "Subscribe");
			
			return subscriptionDoc;			
		}

					
		log.info("\n\n\n Looked up/found existing fbiRapbackSubscription, Handling... \n\n\n");				
		
		Date fbiSubscriptionEndDate = getFbiSubReqEndDate(fbiRapbackSubscription);
		
		Date stateSubscriptionEndDate = getSubReqEndDate(subscriptionDoc);		
																
		if(fbiSubscriptionEndDate != null && stateSubscriptionEndDate != null && stateSubscriptionEndDate.after(fbiSubscriptionEndDate)){		
			
			log.info("\n\n\n State Subscription End Date Greater than FBI Sub. End Date. Preparing MODIFY message... \n\n\n");
			
			FbiSubscriptionModification fbiSubMod = new FbiSubscriptionModification();	
			
			fbiSubMod.setPersonFbiUcnId(fbiIdUcn);
			fbiSubMod.setReasonCode(subPurposeCategory);			
			fbiSubMod.setSubModEndDate(stateSubscriptionEndDate);
			fbiSubMod.setSubscriptionFbiId(fbiRapbackSubscription.getFbiSubscriptionId());					
			
			Document subscriptionModifyDoc = prepareSubscriptionMaintenanceMessage(fbiSubMod);
			
			exchange.getIn().setHeader("sendSubToFbiEbtsAdapter", true);			
			exchange.getIn().setHeader("operationName", "Modify");
			
			return subscriptionModifyDoc;
			
		}else{			
			log.warn("\n\n\n FBI subscription end date is greater than end date on new state subscription, or It just can't be determined."
					+ "  Not sending to EBTS adapter! \n\n\n");
			
			exchange.getIn().setHeader("sendSubToFbiEbtsAdapter", false);
		}
				
		return subscriptionDoc;
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
	
	
	/**
	 * @deprecated
	 * @param unsubscribeDoc
	 * @param fbiSubId
	 * @return
	 * @throws Exception
	 */
	public Document appendFbiSubscriptionIdToUnsubscribeDoc(Document unsubscribeDoc, String fbiSubId) throws Exception{
		
		log.info("\n\n\n appendFbiDataToFbiUnSubscribeDoc... \n\n\n");
		
		Element relatedFBISubscriptionElement = unsubscribeDoc.createElementNS(OjbcNamespaceContext.NS_SUB_MSG_EXT, "RelatedFBISubscription");
		relatedFBISubscriptionElement.setPrefix(OjbcNamespaceContext.NS_PREFIX_SUB_MSG_EXT);						
						
		Node unsubMsgNode = XmlUtils.xPathNodeSearch(unsubscribeDoc, "//unsubmsg-exch:UnsubscriptionMessage");		
		unsubMsgNode.appendChild(relatedFBISubscriptionElement);								
									
		
		if(StringUtils.isNotEmpty(fbiSubId)){
			
			Element subFbiIdElement = XmlUtils.appendElement(relatedFBISubscriptionElement, OjbcNamespaceContext.NS_SUB_MSG_EXT, 
					"SubscriptionFBIIdentification");
			
			Element fbiIdValElement = XmlUtils.appendElement(subFbiIdElement, OjbcNamespaceContext.NS_NC, "IdentificationID");
			
			fbiIdValElement.setTextContent(fbiSubId);			
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
	
	
		
	private FbiRapbackSubscription lookupFbiSubscriptionFromRapbackDataStore(String fbiIdUcn, String category){				
		
		if(StringUtils.isEmpty(fbiIdUcn) || StringUtils.isEmpty(category)){
			log.warn("\n\n\n Not looking up fbi subscription from rapback datastore.  Don't have both fbiUcnId and categoryReason \n\n\n");
			return null;
		}
				
		FbiRapbackSubscription fbiRapbackSubscription = null;
				
		try{
			fbiRapbackSubscription = rapbackDao.getFbiRapbackSubscription(category, fbiIdUcn);
			
			log.info("\n\n\n Received FbiRapbackSubscription: \n\n" + fbiRapbackSubscription + "\n\n\n");
			
		}catch(Exception e){
			
			log.warn("\n\n Unable to get fbi rapback subscription in query! \n\n" + e.getMessage());			
		}
								
		return fbiRapbackSubscription;
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
	
	public Boolean routeToProcessFbiSubscriptionRoute(@Body Document document) throws Exception{
		
		if (BooleanUtils.isTrue(fbiSubscriptionMember)){
			String transactionNumber = XmlUtils.xPathStringSearch(document, 
					"//submsg-exch:SubscriptionMessage/submsg-ext:SubscriptionRelatedCaseIdentification/nc:IdentificationID");
			Boolean fbiSubscriptionQualification = rapbackDao.getfbiSubscriptionQualification(transactionNumber);
			
			if (BooleanUtils.isTrue(fbiSubscriptionQualification)){
				return true;
			}
		}
		
		return false;
	}
}

