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
package org.ojbc.intermediaries.sn.subscription;

import java.util.Optional;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.ojbc.audit.enhanced.processor.AbstractValidationRequestProcessor;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.intermediaries.sn.dao.ValidationDueDateStrategy;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class with handle Subscription Validation Requests.  If the subscription is found,
 * the lastValidatedDate column will be updated with the current date stamp.
 * 
 */
public class SubscriptionValidationMessageProcessor {

	private static final Log log = LogFactory.getLog( SubscriptionValidationMessageProcessor.class );
	
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;
	
	private FaultMessageProcessor faultMessageProcessor = new FaultMessageProcessor();
	
	private ValidationDueDateStrategy validationDueDateStrategy;
	
	private AbstractValidationRequestProcessor subscriptionValidationProcessor;  
	
	/**
	 * This method accepts the exchange, attempts to validate the message and creates the appropriate response
	 * 
	 * @param exchange
	 * @throws Exception
	 */
	public void validateSubscription(Exchange exchange, @Header("subscriptionId") Integer subscriptionId, 
			@Header("validationDueDateString") String validationDueDateString, @Header("validationType") String validationType) throws Exception 
	{
		int rowsUpdated = 0; 
		
		//Criminal subscriptions will use current date as the start date, update subscription accordingly
		if (validationType.equals("criminal"))
		{	
			rowsUpdated = getSubscriptionSearchQueryDAO().validateSubscriptionCriminal(validationDueDateString, subscriptionId);
		}

		//Civil subscriptions will use current date as the start date, update subscription accordingly
		if (validationType.equals("civil"))
		{	
			rowsUpdated = getSubscriptionSearchQueryDAO().validateSubscriptionCivil(validationDueDateString, subscriptionId, LocalDate.now().toString());
		}

		//Send a good response back if a row has been updated
		if (rowsUpdated == 1)
		{	
			Document returnDoc = createSubscriptionValidationResponseMessage(true);
			
			log.debug("Updated validation date for subscription id: " + subscriptionId);
			
			exchange.getIn().setBody(returnDoc);
			
			//Do auditing here
			subscriptionValidationProcessor.auditValidationRequest(exchange);
		}

		//Create an exception if no rows are updated
		if (rowsUpdated == 0)
		{	
			faultMessageProcessor.createFault(exchange);
		}

	}


	public String getValidationDueDateString(@Header("subscription") Subscription subscription, @Header("validationType") String validationType) {
		if (subscription == null) throw new IllegalArgumentException("Can't calculate the validation due date when subscription is not found.");
		
		if (StringUtils.isBlank(validationType))
		{
			throw new IllegalArgumentException("Validation type not specified. Unable to calculate validation date.");
		}	
		
		String validationDueDateString = "";
		
		if (validationType.equals("criminal"))
		{	
			//update here
			DateTime validationDueDate = validationDueDateStrategy.getValidationDueDate(subscription, subscription.getLastValidationDate().toLocalDate());
			validationDueDateString = Optional.ofNullable(validationDueDate).map(i->i.toString("yyyy-MM-dd")).orElse("");
		}

		if (validationType.equals("civil"))
		{	
			//update here
			DateTime validationDueDate = validationDueDateStrategy.getValidationDueDate(subscription, LocalDate.now());
			validationDueDateString = Optional.ofNullable(validationDueDate).map(i->i.toString("yyyy-MM-dd")).orElse("");
		}

		return validationDueDateString;
	}
    
	
    /**
     * This method create the validation response message.  It will set the validation indicator appropriately.
     * 
     * @param validationIndicator
     * @return
     * @throws Exception
     */
	Document createSubscriptionValidationResponseMessage(boolean validationIndicator) throws Exception
	{
        Document doc = null;

        DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
        docBuilderFact.setNamespaceAware(true);
        DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

        doc = docBuilder.newDocument();
        Element root = doc.createElementNS(OjbcNamespaceContext.NS_B2, "ValidateResponse");
        doc.appendChild(root);
        root.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);

        Element subscriptionValidationResponseMessageElement = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUB_VALID_MESSAGE, "SubscriptionValidationResponseMessage");
        
        Element subscriptionValidatedIndicatorElement = XmlUtils.appendElement(subscriptionValidationResponseMessageElement, OjbcNamespaceContext.NS_SUBSCRIPTION_RESPONSE_EXT, "SubscriptionValidatedIndicator");
        subscriptionValidatedIndicatorElement.setTextContent(String.valueOf(validationIndicator));
        
		return doc;
	}
	
	/**
	 * This method retrieves the subscription ID from the request message.  It will throw an exception if the subscription id is not set or invalid.
	 * 
	 * @param subscriptionValidationRequestMessage
	 * @return
	 * @throws Exception
	 */
	String returnSubscriptionIDFromSubscriptionValidationRequestMessage(Document subscriptionValidationRequestMessage) throws Exception
	{
		
		if (subscriptionValidationRequestMessage == null)
		{
			throw new IllegalStateException("Subscription Validation Request Message is not valid");
		}	
		
		String subscriptionId = XmlUtils.xPathStringSearch(subscriptionValidationRequestMessage, "/b-2:Validate/svm:SubscriptionValidationMessage/submsg-ext:SubscriptionIdentification/nc:IdentificationID");
		
		log.debug("Here is the subscription ID from the subscription validation message: " + subscriptionId);
		
		if (StringUtils.isBlank(subscriptionId))
		{
			throw new IllegalArgumentException("Unable to retrieve Subscription ID");
		}	
		
		return subscriptionId;
	}

	public FaultMessageProcessor getFaultMessageProcessor() {
		return faultMessageProcessor;
	}

	public void setFaultMessageProcessor(FaultMessageProcessor faultMessageProcessor) {
		this.faultMessageProcessor = faultMessageProcessor;
	}


	public ValidationDueDateStrategy getValidationDueDateStrategy() {
		return validationDueDateStrategy;
	}


	public void setValidationDueDateStrategy(
			ValidationDueDateStrategy validationDueDateStrategy) {
		this.validationDueDateStrategy = validationDueDateStrategy;
	}


	public SubscriptionSearchQueryDAO getSubscriptionSearchQueryDAO() {
		return subscriptionSearchQueryDAO;
	}


	public void setSubscriptionSearchQueryDAO(SubscriptionSearchQueryDAO subscriptionSearchQueryDAO) {
		this.subscriptionSearchQueryDAO = subscriptionSearchQueryDAO;
	}


	public AbstractValidationRequestProcessor getSubscriptionValidationProcessor() {
		return subscriptionValidationProcessor;
	}


	public void setSubscriptionValidationProcessor(
			AbstractValidationRequestProcessor subscriptionValidationProcessor) {
		this.subscriptionValidationProcessor = subscriptionValidationProcessor;
	}

}
