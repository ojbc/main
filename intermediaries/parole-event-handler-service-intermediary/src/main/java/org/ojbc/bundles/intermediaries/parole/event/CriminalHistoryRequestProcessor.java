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
package org.ojbc.bundles.intermediaries.parole.event;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.processor.MessageProcessor;
import org.springframework.stereotype.Service;

@Service
public class CriminalHistoryRequestProcessor extends RequestResponseProcessor implements CamelContextAware {

	/**
	 * Camel context needed to use producer template to send messages
	 */
	protected CamelContext camelContext;

	private MessageProcessor criminalHistoryMessageProcessor;
		
	private static final Log log = LogFactory.getLog(CriminalHistoryRequestProcessor.class );
	
	public String invokeRequest(DetailsRequest criminalHistoryRequest, String federatedQueryID) throws Exception{	
		
		//POJO to XML Request
		String criminalHistoryRequestPayload = CrimHistQueryFactory.createPersonQueryRequest(criminalHistoryRequest);
		
		//Create exchange
		Exchange senderExchange = new DefaultExchange(camelContext, ExchangePattern.InOnly);
		
		//Set exchange body to XML Request message
		senderExchange.getIn().setBody(criminalHistoryRequestPayload);
		
		//Set reply to and WS-Addressing message ID
		senderExchange.getIn().setHeader("federatedQueryRequestGUID", federatedQueryID);
		senderExchange.getIn().setHeader("WSAddressingReplyTo", this.getReplyToAddress());
		
		//Set the token header so that CXF can retrieve this on the outbound call
		String tokenID = senderExchange.getExchangeId();
		senderExchange.getIn().setHeader("tokenID", tokenID);
		
		criminalHistoryMessageProcessor.sendResponseMessage(camelContext, senderExchange);
		
		//Put message ID and "noResponse" place holder.  
		putRequestInMap(federatedQueryID);
		
		String response = pollMap(federatedQueryID);
		
		if (response.length() > 500){	
			log.debug("Here is the response (truncated): " + response.substring(0,500));
		}else{
			log.debug("Here is the response: " + response);
		}
		
		return response;
	}

	public CamelContext getCamelContext() {
		return camelContext;
	}

	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;

	}

	public MessageProcessor getCriminalHistoryMessageProcessor() {
		return criminalHistoryMessageProcessor;
	}

	public void setCriminalHistoryMessageProcessor(
			MessageProcessor criminalHistoryMessageProcessor) {
		this.criminalHistoryMessageProcessor = criminalHistoryMessageProcessor;
	}

}
