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

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class with handle Subscription Validation Requests.  If the subscription is found,
 * the lastValidatedDate column will be updated with the current date stamp.
 * 
 */
public class SubscriptionValidationMessageProcessor {

	private static final Log log = LogFactory.getLog( SubscriptionValidationMessageProcessor.class );
	
	private JdbcTemplate jdbcTemplate;
	
	private FaultMessageProcessor faultMessageProcessor;
	
	private static final String SUBSCRIPTION_VALIDATION_QUERY = "update subscription set lastValidationDate = curdate() where id = ?";
	
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }
    

	/**
	 * This method accepts the exchange, attempts to validate the message and creates the appropriate response
	 * 
	 * @param exchange
	 * @throws Exception
	 */
	public void validateSubscription(Exchange exchange) throws Exception 
	{
		Document subscriptionValidationRequestMessage = exchange.getIn().getBody(Document.class);
		
		String subscriptionID="";
		
		//Try to get the subscription ID.  If not found or not set, create a fault
		try {
			subscriptionID = returnSubscriptionIDFromSubscriptionValidationRequestMessage(subscriptionValidationRequestMessage);
		} catch (Exception e) {
			
			exchange.getIn().setHeader("subscriptionID", subscriptionID);
			
			faultMessageProcessor.createFault(exchange);
			return;
		}
		
		int rowsUpdated = this.jdbcTemplate.update(SUBSCRIPTION_VALIDATION_QUERY,new Object[] {subscriptionID.trim()});
		
		//Send a good response back if a row has been updated
		if (rowsUpdated == 1)
		{	
			Document returnDoc = createSubscriptionValidationResponseMessage(true);
			
			log.debug("Updated validation date for subscription id: " + subscriptionID);
			
			exchange.getIn().setBody(returnDoc);
		}

		//Create an exception if no rows are updated
		if (rowsUpdated == 0)
		{	
			exchange.getIn().setHeader("subscriptionID", subscriptionID);
			faultMessageProcessor.createFault(exchange);
		}

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
}
