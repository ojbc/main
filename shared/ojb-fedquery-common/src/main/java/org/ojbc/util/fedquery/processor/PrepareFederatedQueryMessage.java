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
package org.ojbc.util.fedquery.processor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.ojbc.util.camel.helper.OJBUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * This processor is used by the Splitter to prepare the message for processing
 * 
 * It will set the following headers: 
 * 	webServiceEndpointToCall.  This indicates what web service endpoint to call
 *  Exchange.DESTINATION_OVERRIDE_URL.  This indicates the address of the endpoint to call.
 *  
 *  It also preserves the WS Addressing message ID and replyTo values.
 *  
 *  Prior to calling the adapter, it will updated the source systems node to include the adapter that is being called.
 *
 */
public class PrepareFederatedQueryMessage  implements Processor{

	private static final Log log = LogFactory.getLog( PrepareFederatedQueryMessage.class );
	
	private ExchangeDestinationLookupStrategy exchangeDestinationLookupStrategy;
	
	public void process(Exchange exchange) throws Exception {
		
		//Get System Name from the split exchange
		Element systemNameElement = (Element)exchange.getIn().getBody();
		String systemName = systemNameElement.getTextContent();    			
		log.debug("System name in prepare ref for Splitter: " + systemName);
		
		//Provide the system name that we are calling as a convenience to ExchangeDestinationLookupStrategy
		exchange.getIn().setHeader("systemNameURI", systemName);
		
		//Remove system names from message
		//Retrieve original request message that is a Camel Header
		Document requestMessage = (Document)exchange.getIn().getHeader("requestMessageBody");
		
    	//Set body to request message with only source system name of adapter being called.
    	Document requestMessageWithSystemNameForAdapter = removeSystemNamesNotIntendedForAdapter(requestMessage, systemName);
    	exchange.getIn().setBody(requestMessageWithSystemNameForAdapter);
    	
		//Set Header to call web service using recipient list in camel route
		String endpointName = exchangeDestinationLookupStrategy.getCXFEndpointName(exchange);
		exchange.getIn().setHeader("webServiceEndpointToCall", endpointName);
		
    	//Set the Destination Override URL map entry so we can set the adapter address
    	String cxfEndpointAddress = exchangeDestinationLookupStrategy.getCXFEndpointAddress(exchange);
    	
    	if (StringUtils.isNotBlank(cxfEndpointAddress))
    	{	
    		log.debug("Setting CXF endpoint address to: " + cxfEndpointAddress);
    		exchange.getIn().setHeader(Exchange.DESTINATION_OVERRIDE_URL,cxfEndpointAddress);
    	}	
    	
    	//Get message ID
    	Map<String, Object> requestContext = prepareWSAddressingParamters(exchange);
    	log.debug("Federated Query ID: " + exchange.getIn().getHeader("federatedQueryRequestGUID"));
    	
    	//Set WS-Addressing message properties header
    	exchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		//Remove obsolete headers to create clean exchange, the first argument '*' removes all headers, and the arguments that follows are headers that are preserved
		exchange.getIn().removeHeaders("*", "federatedQueryRequestGUID", "operationName", "operationNamespace", "tokenID", "webServiceEndpointToCall", Client.REQUEST_CONTEXT, Exchange.DESTINATION_OVERRIDE_URL);
	}

	private Map<String, Object> prepareWSAddressingParamters(Exchange exchange)
			throws Exception {
		String requestID = (String)exchange.getIn().getHeader("federatedQueryRequestGUID");
    	
    	//Create a new map with WS Addressing message properties that we want to override
    	//We want to preserve the Message ID as well
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
		return requestContext;
	}
	
	Document removeSystemNamesNotIntendedForAdapter(Document requestMessage,
			String endpointUriValue) {

		//We need to clone the node because the recipient list message is mutable
    	Document clonedRequestDocument = (Document) requestMessage.cloneNode(true);
    	NodeList sourceSystems = clonedRequestDocument.getElementsByTagName("SourceSystemNameText");
    	
    	Set<Node> targetElementsToRemove = new HashSet<Node>();
    	
    	//Loop through the source systems and remove any source systems that don't match the system we are calling
    	//We can't remove them from the live list so we put them in a set and remove later
    	//http://stackoverflow.com/questions/1374088/removing-dom-nodes-when-traversing-a-nodelist
    	 for(int s=0; s<sourceSystems.getLength(); s++){
    		
    		 Node sourceSystemNode = sourceSystems.item(s);
    		 	    		 
             if(sourceSystemNode.getNodeType() == Node.ELEMENT_NODE){
            	 
            	 String currentSourceSystemNode = sourceSystemNode.getTextContent();
            	 
            	 if (!currentSourceSystemNode.equals(endpointUriValue))
            	 {
            		 targetElementsToRemove.add(sourceSystemNode);
            	 }	 
             }	 
            	 
    	 }
    	 
    	 for (Node e: targetElementsToRemove) {
    		  e.getParentNode().removeChild(e);
    	 }
    	
    	 //Set the message body of the modified message
    	 return clonedRequestDocument;
	}

	public ExchangeDestinationLookupStrategy getExchangeDestinationLookupStrategy() {
		return exchangeDestinationLookupStrategy;
	}

	public void setExchangeDestinationLookupStrategy(
			ExchangeDestinationLookupStrategy exchangeDestinationLookupStrategy) {
		this.exchangeDestinationLookupStrategy = exchangeDestinationLookupStrategy;
	}



}
