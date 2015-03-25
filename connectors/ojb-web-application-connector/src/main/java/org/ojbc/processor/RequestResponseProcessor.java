package org.ojbc.processor;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.camel.Body;
import org.apache.camel.Header;
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
	private ConcurrentHashMap<String, String> requestResponseMap;
	
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
			log.debug("Here is the response (truncated): " + response.substring(0,500));
		}
		else
		{
			log.debug("Here is the response: " + response);
		}
		
		requestResponseMap.put(federatedQueryID, response);
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
			response = requestResponseMap.get(federatedQueryID);

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
	
	public String getReplyToAddress() {
		return replyToAddress;
	}

	public void setReplyToAddress(String replyToAddress) {
		this.replyToAddress = replyToAddress;
	}

	public void setRequestResponseMap(
			ConcurrentHashMap<String, String> requestResponseMap) {
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
