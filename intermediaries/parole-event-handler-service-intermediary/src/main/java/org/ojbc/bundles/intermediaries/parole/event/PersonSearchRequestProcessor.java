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

import java.util.Arrays;

import org.apache.camel.CamelContext;
import org.apache.camel.CamelContextAware;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePattern;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.bundles.intermediaries.parole.event.utils.PersonSearchRequest;
import org.ojbc.util.camel.processor.MessageProcessor;
import org.ojbc.util.fedquery.error.MergeNotificationErrorProcessor;
import org.w3c.dom.Document;

public class PersonSearchRequestProcessor extends RequestResponseProcessor implements CamelContextAware{

	/**
	 * Camel context needed to use producer template to send messages
	 */
	protected CamelContext camelContext;
	
	private MessageProcessor personSearchMessageProcessor;
		
	private static final Log log = LogFactory.getLog( PersonSearchRequestProcessor.class );
	
	
	public void testInvokePersonSearchRequest() throws Exception{
	
		PersonSearchRequest psr = new PersonSearchRequest();
		
		psr.setPersonSID("a2588583");
		psr.setSourceSystems(Arrays.asList("{http://ojbc.org/Services/WSDL/Person_Search_Request_Service/Criminal_History/1.0}Submit-Person-Search---Criminal-History"));
		
		invokePersonSearchRequest(psr, "123");
	}
	
	
		
	public String invokePersonSearchRequest(PersonSearchRequest personSearchRequest, String federatedQueryID) throws Exception
	{
		String response = "";
		
		try{			
			PersonSearchRequestFactory personSearchReqFact = new PersonSearchRequestFactory();
			
			//POJO to XML Request
			Document personSearchRequestPayload = personSearchReqFact.createPersonSearchRequest(personSearchRequest);
						
			personSearchRequestPayload.normalizeDocument();
			
			//Create exchange
			Exchange senderExchange = new DefaultExchange(camelContext, ExchangePattern.InOnly);
			
			//Set exchange body to XML Request message
			senderExchange.getIn().setBody(personSearchRequestPayload);
			
			//Set reply to and WS-Addressing message ID
			senderExchange.getIn().setHeader("federatedQueryRequestGUID", federatedQueryID);
			senderExchange.getIn().setHeader("WSAddressingReplyTo", this.getReplyToAddress());
			
			//Set the token header so that CXF can retrieve this on the outbound call
			String tokenID = senderExchange.getExchangeId();
			senderExchange.getIn().setHeader("tokenID", tokenID);
		
			// call async. service(returns immediately even though response not there)
			personSearchMessageProcessor.sendResponseMessage(camelContext, senderExchange);
				
			//Put message ID and "noResponse" place holder.  
			putRequestInMap(federatedQueryID);
			
			response = pollMap(federatedQueryID);
			
			if (response.equals(NO_RESPONSE)) {
				log.debug("Endpoints timed out and no response recieved at web app, create error response");
				response = MergeNotificationErrorProcessor.returnMergeNotificationErrorMessage();
			}
			
			if (response.length() > 500){	
				log.debug("Here is the response (truncated): " + response.substring(0,500));
			}
			else{
				log.debug("Here is the response: " + response);
			}
			
		}
		catch (Exception ex){			
			ex.printStackTrace();
			throw(ex);
		}
		
		return response;		
	}

	public CamelContext getCamelContext() {
		return camelContext;
	}

	public void setCamelContext(CamelContext camelContext) {
		this.camelContext = camelContext;

	}

	public MessageProcessor getPersonSearchMessageProcessor() {
		return personSearchMessageProcessor;
	}

	public void setPersonSearchMessageProcessor(
			MessageProcessor personSearchMessageProcessor) {
		this.personSearchMessageProcessor = personSearchMessageProcessor;
	}
		
}
