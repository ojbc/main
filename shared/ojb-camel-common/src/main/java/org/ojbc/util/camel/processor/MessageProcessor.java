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
package org.ojbc.util.camel.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.attachment.AttachmentMessage;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.support.DefaultExchange;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.ojbc.util.camel.helper.OJBUtils;

public class MessageProcessor {
	
	private static final Log log = LogFactory.getLog( MessageProcessor.class );
	
	private String operationName;
	private String operationNamespace;
	private String destinationEndpoint;
	
	private ProducerTemplate producerTemplate;
	/**
	 * By Default, the MessageProcessor will assume an asynchronous service
	 */
	private boolean callServiceSynchronous = false;
	
	/**
	 * This method will extract a message ID from the Soap header and set that as a GUID to correlate requests and responses
	 * It also creates a header that can be used to make a file name from the Message ID that works on all platforms.
	 * 
	 * @param exchange
	 * @throws Exception
	 */
	
	public void processRequestPayload(Exchange exchange) throws Exception{
		HashMap<String, String> wsAddressingHeadersMap = OJBUtils.returnWSAddressingHeadersFromCamelSoapHeaders(exchange);
		
		String requestID = wsAddressingHeadersMap.get("MessageID");	
		
		String replyTo = wsAddressingHeadersMap.get("ReplyTo");

		if (StringUtils.isNotBlank(replyTo)){
			exchange.getIn().setHeader("WSAddressingReplyTo", replyTo);
		}	
		
		if (StringUtils.isNotBlank(requestID)){
			String platformSafeFileName = requestID.replace(":", "");
			exchange.getIn().setHeader("federatedQueryRequestGUID", requestID);
			exchange.getIn().setHeader("platformSafeFileName", platformSafeFileName);
		}
		else{
			throw new Exception("Unable to find unique ID in Soap Header.  Was the message ID set in the Soap WS Addressing header?");
		}	

	}

	
	
	
	public void sendResponseMessage(CamelContext context, Exchange exchange) throws Exception
	{
		Exchange senderExchange = null;
	
		//If the synchronous header is set, change exchange pattern to InOut
		if (callServiceSynchronous)
		{
			senderExchange = new DefaultExchange(context, ExchangePattern.InOut);
		}	
		else
		{
			senderExchange = new DefaultExchange(context, ExchangePattern.InOnly);
		}	
		
		//This is used to propogate SAML tokens
		String tokenID = (String)exchange.getIn().getHeader("tokenID");

		if (StringUtils.isNotEmpty(tokenID))
		{	
			log.debug("Saml Token ID in Message Processor: " + tokenID);
			senderExchange.getIn().setHeader("tokenID", tokenID);
		}

				
		String requestID = (String)exchange.getIn().getHeader("federatedQueryRequestGUID");
		
    	//Create a new map with WS Addressing message properties that we want to override
		HashMap<String, String> wsAddressingMessageProperties = new HashMap<String, String>();
		wsAddressingMessageProperties.put("MessageID",requestID);

		String replyTo = (String)exchange.getIn().getHeader("WSAddressingReplyTo");

		if (StringUtils.isNotEmpty(replyTo))
		{	
			log.debug("WS Addressing Reply To Camel Header: " + replyTo);
			wsAddressingMessageProperties.put("ReplyTo",replyTo);
		}
		
		//Call method to create proper request context map
		Map<String, Object> requestContext = OJBUtils.setWSAddressingProperties(wsAddressingMessageProperties);
		
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		senderExchange.setProperty("tokenID", senderExchange.getProperty("tokenID"));
		
        senderExchange.getIn().setBody(exchange.getIn().getBody());
        
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, getOperationName());
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, getOperationNamespace());
	    
	    if (getProducerTemplate() == null) {
	        producerTemplate = context.createProducerTemplate();
	    }
		Exchange returnExchange = getProducerTemplate().send("cxf:bean:" + destinationEndpoint + "?dataFormat=PAYLOAD",
				senderExchange);
		
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
	}
	
	/**
	 * This method will use an existing exchange and set the 'out' message with the WS-Addressing message ID.  This removes all the headers from the 'in'
	 * message which tend to confuse Camel.
	 * 
	 * @param exchange
	 * @throws Exception
	 */
	public void prepareNewExchangeResponseMessage(Exchange exchange) throws Exception
	{
		String requestID = (String)exchange.getIn().getHeader("federatedQueryRequestGUID");
		
		if (StringUtils.isBlank(requestID))
		{
			requestID = (String)exchange.getProperty("federatedQueryRequestGUID");
		}	

		log.debug("Federeated Query Request ID: " + requestID);

    	//Create a new map with WS Addressing message properties that we want to override
		HashMap<String, String> wsAddressingMessageProperties = new HashMap<String, String>();
		
		if (StringUtils.isNotEmpty(requestID))
		{	
			wsAddressingMessageProperties.put("MessageID",requestID);
		}

		String replyTo = (String)exchange.getIn().getHeader("WSAddressingReplyTo");

		if (StringUtils.isNotEmpty(replyTo))
		{	
			log.debug("WS Addressing Reply To Camel Header: " + replyTo);
			wsAddressingMessageProperties.put("ReplyTo",replyTo);
		}

		String from = (String)exchange.getIn().getHeader("WSAddressingFrom");

		if (StringUtils.isNotEmpty(from))
		{	
			log.debug("WS Addressing from Camel Header: " + from);
			wsAddressingMessageProperties.put("From",from);
		}
		
		String action = (String)exchange.getIn().getHeader("WSAddressingAction");

		if (StringUtils.isNotEmpty(action))
		{	
			log.debug("WS Addressing Action Camel Header: " + action);
			wsAddressingMessageProperties.put("Action",action);
		}		
		
		exchange.getMessage().removeHeaders("*", Exchange.DESTINATION_OVERRIDE_URL, "recipientListReplyToEndpoint");

		//Call method to create proper request context map
		Map<String, Object> requestContext = OJBUtils.setWSAddressingProperties(wsAddressingMessageProperties);
		exchange.getMessage().setHeader(Client.REQUEST_CONTEXT , requestContext);

		//We do this so we can preserve the recipient list rather than losing it in the out message
		
		exchange.getMessage().setBody(exchange.getIn().getBody());
		
		AttachmentMessage attachmentMessageIn = exchange.getIn(AttachmentMessage.class);
		if (attachmentMessageIn.getAttachments() != null) {
			AttachmentMessage attachmentMessageOut = exchange.getMessage(AttachmentMessage.class);
			attachmentMessageOut.setAttachments(attachmentMessageIn.getAttachments());
		}
	}


	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getOperationNamespace() {
		return operationNamespace;
	}

	public void setOperationNamespace(String operationNamespace) {
		this.operationNamespace = operationNamespace;
	}

	public String getDestinationEndpoint() {
		return destinationEndpoint;
	}

	public void setDestinationEndpoint(String destinationEndpoint) {
		this.destinationEndpoint = destinationEndpoint;
	}

	public boolean isCallServiceSynchronous() {
		return callServiceSynchronous;
	}

	public void setCallServiceSynchronous(boolean callServiceSynchronous) {
		this.callServiceSynchronous = callServiceSynchronous;
	}
    public ProducerTemplate getProducerTemplate() {
        return producerTemplate;
    }
    public void setProducerTemplate(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

}
