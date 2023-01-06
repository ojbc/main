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
package org.ojbc.processor.arrest.search;

import static org.ojbc.util.helper.UniqueIdUtils.getFederatedQueryId;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.support.DefaultExchange;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.processor.MessageProcessor;
import org.ojbc.util.camel.processor.RequestResponseProcessor;
import org.ojbc.util.camel.security.saml.OJBSamlMap;
import org.ojbc.util.xml.XmlUtils;
import org.ojbc.web.OjbcWebConstants.ArrestType;
import org.ojbc.web.portal.arrest.ArrestSearchRequest;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@Configuration
@Profile("arrest-search")
public class ArrestSearchRequestProcessor extends RequestResponseProcessor implements CamelContextAware{
	private static final Log log = LogFactory.getLog( ArrestSearchRequestProcessor.class );
	/**
	 * Camel context needed to use producer template to send messages
	 */
	protected CamelContext camelContext;
	
	private MessageProcessor messageProcessor;
	
	private OJBSamlMap OJBSamlMap;
	
	public String invokeRequest(ArrestSearchRequest arrestSearchRequest, Element samlToken) throws Throwable {
		
		log.info("invoking the arrest search request service with the saml token ");
		if (samlToken == null)
		{
			throw new Exception("No SAML token provided. Unable to perform query.");
		}	
		
		//POJO to XML Request
		Document arrestSearchRequestPayload = RequestMessageBuilderUtilities.createArrestSearchRequest(arrestSearchRequest);
		XmlUtils.printNode(arrestSearchRequestPayload);
		if (ArrestType.DA == arrestSearchRequest.getArrestType()) {
			if (BooleanUtils.isNotTrue(arrestSearchRequest.getArrestWithDeferredDispositions())) {
				messageProcessor.setOperationName("SubmitDAChargesSearchRequest");
			}
			else {
				messageProcessor.setOperationName("SubmitDADeferredDispositionSearchRequest");
			}
		}
		else if (BooleanUtils.isNotTrue(arrestSearchRequest.getArrestWithDeferredDispositions())) {
			messageProcessor.setOperationName("SubmitMunicipalChargesSearchRequest");
		}
		else {
			messageProcessor.setOperationName("SubmitMunicipalDeferredDispositionSearchRequest");
		}
		
		//Create exchange
		Exchange senderExchange = new DefaultExchange(camelContext, ExchangePattern.InOnly);
		
		//Set exchange body to XML Request message
		senderExchange.getIn().setBody(arrestSearchRequestPayload);
		
		//Set reply to and WS-Addressing message ID
		String federatedQueryID = getFederatedQueryId();
		senderExchange.getIn().setHeader("federatedQueryRequestGUID", federatedQueryID);
		senderExchange.getIn().setHeader("WSAddressingReplyTo", this.getReplyToAddress());

		//Set the token header so that CXF can retrieve this on the outbound call
		String tokenID = senderExchange.getExchangeId();
		senderExchange.getIn().setHeader("tokenID", tokenID);

		OJBSamlMap.putToken(tokenID, samlToken);

		messageProcessor.sendResponseMessage(camelContext, senderExchange);
		
		//Put message ID and "noResponse" place holder.  
		putRequestInMap(federatedQueryID);
		
		String response = pollMap(federatedQueryID);
		
		if (response.length() > 500)
		{	
			log.debug("Here is the response (truncated): " + response.substring(0,500));
		}
		else
		{
			log.debug("Here is the response: " + response);
		}
		
		//return response here
		return response;
	}

	public CamelContext getCamelContext() {
		return camelContext;
	}

	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;

	}


	public MessageProcessor getMessageProcessor() {
		return messageProcessor;
	}


	public void setMessageProcessor(
			MessageProcessor messageProcessor) {
		this.messageProcessor = messageProcessor;
	}


	public OJBSamlMap getOJBSamlMap() {
		return OJBSamlMap;
	}


	public void setOJBSamlMap(OJBSamlMap oJBSamlMap) {
		OJBSamlMap = oJBSamlMap;
	}

}
