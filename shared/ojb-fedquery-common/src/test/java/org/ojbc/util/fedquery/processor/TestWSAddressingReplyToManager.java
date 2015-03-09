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
