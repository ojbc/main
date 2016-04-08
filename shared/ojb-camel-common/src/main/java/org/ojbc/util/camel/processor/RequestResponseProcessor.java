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
package org.ojbc.util.camel.processor;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.Body;
import org.apache.camel.Exchange;
import org.apache.camel.Header;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class RequestResponseProcessor {
	/**
	 * Reply to Address for the reply service handler
	 */
	private String replyToAddress;
	
	//Details on Concurrent Hash Map
	//http://stackoverflow.com/questions/2836267/concurrenthashmap-in-java
	/**
	 * This is the map that holds the requests and responses
	 */
	private ConcurrentHashMap<String, Object> requestResponseMap;
	
	/**
	 * Maximum polls for a response before timing out
	 */
	private int maxPolls;
	
	/**
	 * Polling interval, the thread will sleep for this many milliseconds before checking map for response
	 */
	private int pollingIntervalInMillis;
	
	public static final String NO_RESPONSE = "noResponse";
	
	private static final Log log = LogFactory.getLog( RequestResponseProcessor.class );

	/**
	 * This method is called by the camel response listener route to update the map with the reply 
	 * 
	 * @param response
	 * @param federatedQueryID
	 */
	public final void updateMapWithResponse(@Body String response, @Header("federatedQueryRequestGUID") String federatedQueryID)
	{
		log.debug("Messaage ID: " + federatedQueryID);
		
		if (response.length() > 500)
		{	
			log.debug("Here is the response (truncated): " + StringUtils.abbreviate(response, 503));
		}
		else
		{
			log.debug("Here is the response: " + response);
		}
		
		requestResponseMap.put(federatedQueryID, response);
	}
	
	/**
	 * This method is called by the camel response listener route to update the map with the reply 
	 * 
	 * @param response
	 * @param federatedQueryID
	 */
	public final void updateMapWithResponseExchange(Exchange exchange, @Body String response,  @Header("federatedQueryRequestGUID") String federatedQueryID)
	{
		log.debug("Messaage ID: " + federatedQueryID);
		
		if (response.length() > 500)
		{	
			log.debug("Here is the response (truncated): " + StringUtils.abbreviate(response, 503));
		}
		else
		{
			log.debug("Here is the response: " + response);
		}
		
		requestResponseMap.put(federatedQueryID, exchange);
	}
	
	public final void putRequestInMap(String federatedQueryID) 
	{
		//concurrent map does not allow null entries
		//http://stackoverflow.com/questions/698638/why-does-concurrenthashmap-prevent-null-keys-and-values
		requestResponseMap.put(federatedQueryID, NO_RESPONSE);
	}
	
	public final String pollMap(String federatedQueryID) throws Exception
	{
		boolean haveResponse = false;
		int counter = 0;
		String response = "";
		
		//Polling until we have a response or we get to max polls
		while(!haveResponse && counter < this.getMaxPolls())
		{
			//Get response from map
			response = (String) requestResponseMap.get(federatedQueryID);

			//This means that the key does not exist, this is an error condition
			if (response == null)
			{
				response = "";
				break;
			}	
			
			//if we are getting 'noResponse', update the counter and continue
			if (response.equals(NO_RESPONSE))
			{
				counter++;
				Thread.sleep(this.getPollingIntervalInMillis());
				log.debug("Sleeping and waiting for response for federated ID: " + federatedQueryID );
			}
			else
			{
				//We have a response, remove from the key from the map and break
				requestResponseMap.remove(federatedQueryID);	
				break;
			}	
		}	
		
		return response;
	}
	
	public final Exchange pollMapForResponseExchange(String federatedQueryID) throws Exception
	{
		boolean haveResponse = false;
		int counter = 0;
		
		//Polling until we have a response or we get to max polls
		while(!haveResponse && counter < this.getMaxPolls())
		{
			//Get response from map
			Object response = requestResponseMap.get(federatedQueryID);
			
			//This means that the key does not exist, this is an error condition
			if (response == null)
			{
				return null;
			}	
			
			//if we are getting 'noResponse', update the counter and continue
			if (response.equals(NO_RESPONSE))
			{
				counter++;
				Thread.sleep(this.getPollingIntervalInMillis());
				log.debug("Sleeping and waiting for response for federated ID: " + federatedQueryID );
			}
			else
			{
				//We have a response, remove from the key from the map and break
				requestResponseMap.remove(federatedQueryID);	
				return (Exchange)response;
			}	
		}
		return null;	
		
	}
	
	public String getReplyToAddress() {
		return replyToAddress;
	}

	public void setReplyToAddress(String replyToAddress) {
		this.replyToAddress = replyToAddress;
	}

	public void setRequestResponseMap(
			ConcurrentHashMap<String, Object> requestResponseMap) {
		this.requestResponseMap = requestResponseMap;
	}

	public int getMaxPolls() {
		return maxPolls;
	}

	public void setMaxPolls(int maxPolls) {
		this.maxPolls = maxPolls;
	}

	public int getPollingIntervalInMillis() {
		return pollingIntervalInMillis;
	}

	public void setPollingIntervalInMillis(int pollingIntervalInMillis) {
		this.pollingIntervalInMillis = pollingIntervalInMillis;
	}

}
