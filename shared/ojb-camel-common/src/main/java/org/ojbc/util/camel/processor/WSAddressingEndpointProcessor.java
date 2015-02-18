package org.ojbc.util.camel.processor;

import java.util.HashMap;

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;

/**
 * This class uses a Camel header named "WSAddressingReplyTo" to lookup an endpoint to send
 * a response to in the 'endpointReplyToMap' hashmap 
 * 
 * @author yogeshchawla
 *
 */
public class WSAddressingEndpointProcessor {

	private HashMap<String, String> endpointReplyToMap;
	
	
	/**
	 * This method looks in the Camel Exchange for the 'WSAddressingReplyTo' header.
	 * Once it finds this header, it will look up a reply to endpoint in a map and
	 * create a recipient list header to send the response to.  
	 * 
	 * @param exchange
	 */
	public void processReplyToAddress(Exchange exchange)
	{
		String replyTo = (String)exchange.getIn().getHeader("WSAddressingReplyTo");
		
		String endpointNameFromReplyToAddress = endpointReplyToMap.get(replyTo);
		
		if (StringUtils.isNotEmpty(endpointNameFromReplyToAddress))
		{
			exchange.getIn().setHeader("recipientListReplyToEndpoint", endpointNameFromReplyToAddress);
		}	

	}
	
	/**
	 * This method checks to see if a 'replyTo' address send by a WSC has a correponding 
	 * replyTo endpoint in the map this class maintains.
	 * 
	 * @param replyToAddress
	 * @return
	 */
	public boolean doesEndpointExist(String replyToAddress)
	{
		String endpointNameFromReplyToAddress = endpointReplyToMap.get(replyToAddress);
		
		if (StringUtils.isNotBlank(endpointNameFromReplyToAddress))
		{	
			return true;
		}
		else
		{
			return false;
		}	
	}

	public HashMap<String, String> getEndpointReplyToMap() {
		return endpointReplyToMap;
	}

	public void setEndpointReplyToMap(HashMap<String, String> endpointReplyToMap) {
		this.endpointReplyToMap = endpointReplyToMap;
	}



	
}
