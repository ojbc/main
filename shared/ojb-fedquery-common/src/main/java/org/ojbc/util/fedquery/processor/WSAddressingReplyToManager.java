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
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.util.camel.processor.WSAddressingEndpointProcessor;

/**
 * This class allows us to maintain a map of replyTo addresses from web service requests
 * so we can reply to the appropriate endpoint after calling a series of web services.
 * 
 * @author yogeshchawla
 *
 */
public class WSAddressingReplyToManager {

	private static final Log log = LogFactory.getLog(WSAddressingReplyToManager.class);
	
	/**
	 * This map contains the messageID as the key and the replyTo address as the value
	 */
	private Map<String, String> replyToMap = new HashMap<String, String>();
	
	/**
	 * This is injected into the class to see if the actual endpoint exists prior to adding it to the map
	 */
	private WSAddressingEndpointProcessor wsAddressingEndpointProcessor;
	
	/**
	 * This method will set the 'WSAddressingReplyTo' Camel header by using the messageID as the key.
	 * 
	 * @param exchange
	 * @param messageID
	 */
	public void getReplyToAddress(Exchange exchange, @Header(value = "federatedQueryRequestGUID")String messageID)
	{
		exchange.getIn().setHeader("WSAddressingReplyTo", replyToMap.get(messageID));
	}
	
	/**
	 * This method accepts a message ID and replyTo address.  If the replyTo address has an endpoint
	 * configured with it in the intermediary, then the replyTo address is entered in the map.
	 * 
	 * @param messageID
	 * @param replyToAddress
	 * @throws Exception
	 */
	public void putReplyToAddress(@Header(value = "federatedQueryRequestGUID")String messageID, @Header(value = "WSAddressingReplyTo") String replyToAddress) throws Exception
	{
		log.debug("Federated Query GUID (Message ID): " + messageID);
		
		if (wsAddressingEndpointProcessor.doesEndpointExist(replyToAddress) == false)
		{
			throw new Exception("Unable to find Reply To Address in Reply To Map: " + replyToAddress);
		}	
		
		replyToMap.put(messageID, replyToAddress);
	}
	
	/**
	 * This method will remove a reply to address enty from the map based on a message ID
	 * 
	 * @param messageID
	 */
	
	public void removeReplyToAddress(@Header(value = "federatedQueryRequestGUID")String messageID)
	{
		replyToMap.remove(messageID);
	}

	public WSAddressingEndpointProcessor getWsAddressingEndpointProcessor() {
		return wsAddressingEndpointProcessor;
	}

	public void setWsAddressingEndpointProcessor(
			WSAddressingEndpointProcessor wsAddressingEndpointProcessor) {
		this.wsAddressingEndpointProcessor = wsAddressingEndpointProcessor;
	}

}
