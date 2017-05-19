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
package org.ojbc.util.fedquery.processor;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ws.addressing.AddressingProperties;

/**
 * This class accesses a reply to map and will set web service destination messages
 * based on the key / value pairs.  The key is the reply to manager in the soap message
 * and the value is where we want to send the response.
 * 
 * We preserve the reply to address from the original connector call so we can not re-use
 * that from the adapter to intermediary call.  
 *
 */
public class HashMapReplyToProcessor {

	private Map <String, String> replyToMap = new HashMap<String, String>();
	
	private static final Log log = LogFactory.getLog( HashMapReplyToProcessor.class );
	
	@SuppressWarnings("unchecked")
	public void overrideCXFAddress(Exchange exchange)
	{
		Map<String, Object> requestContext = (Map<String, Object>) exchange.getIn().getHeader(Client.REQUEST_CONTEXT);
		
		log.debug("CXF Request Context: " + requestContext);
		
		if (requestContext != null)
		{
			AddressingProperties addressingProperties = (AddressingProperties)requestContext.get("javax.xml.ws.addressing.context");
			
			if (addressingProperties.getReplyTo().getAddress().getValue() != null)
			{
				String replyTo = addressingProperties.getReplyTo().getAddress().getValue();
				
				log.debug("Reply to address in CXF Request Context: " + replyTo);
				
				if (replyToMap.containsKey(replyTo))
				{
					log.debug("Map contains key: " + replyTo +  ", setting destination override to: " + replyToMap.get(replyTo));
					
					exchange.getIn().setHeader(Exchange.DESTINATION_OVERRIDE_URL, replyToMap.get(replyTo));
				}	
				
			}	
		}	
	}

	public Map<String, String> getReplyToMap() {
		return replyToMap;
	}

	public void setReplyToMap(Map<String, String> replyToMap) {
		this.replyToMap = replyToMap;
	}
	
}
