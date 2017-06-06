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

import junit.framework.Assert;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.processor.WSAddressingEndpointProcessor;

public class TestWSAddressingReplyToManager {

	private WSAddressingReplyToManager wsAddressingReplyToManager = new WSAddressingReplyToManager();
	private WSAddressingEndpointProcessor wsAddressingEndpointProcessor = new WSAddressingEndpointProcessor();
	
	@Before
	public void setUp()
	{
		HashMap<String, String> endpointReplyToMap = new HashMap<String, String>();
		endpointReplyToMap.put("http://replyToAddress.com", "presentMergeNotificationServiceResultsEndpoint");
		wsAddressingEndpointProcessor.setEndpointReplyToMap(endpointReplyToMap );

		wsAddressingReplyToManager.setWsAddressingEndpointProcessor(wsAddressingEndpointProcessor);

	}
	
	@Test
	public void testWSAddressingReplyToManager() throws Exception
	{
		wsAddressingReplyToManager.putReplyToAddress("12345", "http://replyToAddress.com");
		
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange exchange = new DefaultExchange(ctx);
	    
		wsAddressingReplyToManager.getReplyToAddress(exchange, "12345");
		Assert.assertEquals("http://replyToAddress.com", exchange.getIn().getHeader("WSAddressingReplyTo"));
		
		wsAddressingReplyToManager.removeReplyToAddress("12345");
		
	}

	@Test(expected=Exception.class)
	public void testWSAddressingReplyToManagerInvalidReplyToAddress() throws Exception
	{
		wsAddressingReplyToManager.putReplyToAddress("12345", "http://replyToAddressBad.com");
	}

}
