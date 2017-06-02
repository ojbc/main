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

import org.apache.camel.Exchange;
import org.apache.commons.lang.StringUtils;

/**
 * This class uses a Camel header named "WSAddressingReplyTo" to lookup an endpoint to send
 * a response to in the 'endpointReplyToMap' hashmap 
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
    		exchange.getIn().setHeader(Exchange.DESTINATION_OVERRIDE_URL,replyTo);
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
