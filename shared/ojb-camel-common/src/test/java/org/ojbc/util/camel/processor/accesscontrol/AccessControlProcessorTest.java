package org.ojbc.util.camel.processor.accesscontrol;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;

public class AccessControlProcessorTest {

	@Test
	public void testNoStrategyAccessControlAuthorization() throws Exception
	{
		AccessControlProcessor accessControlProcessor = new AccessControlProcessor();

		CamelContext ctx = new DefaultCamelContext(); 
		Exchange ex = new DefaultExchange(ctx);
		
		accessControlProcessor.authorize(ex);
		
		assertTrue((Boolean)ex.getIn().getHeader("accessControlIsAuthorized"));
		assertEquals("Null Object Strategy. This strategy will always authorize the exchange.", (String)ex.getIn().getHeader("accessControlResponseMessage"));
	}

	@Test
	public void testNullObjectStrategyAccessControlAuthorization() throws Exception
	{
		AccessControlProcessor accessControlProcessor = new AccessControlProcessor();
		NullObjectAccessControlStrategy nullObjectAccessControlStrategy = new NullObjectAccessControlStrategy();
		
		accessControlProcessor.setAccessControlStrategy(nullObjectAccessControlStrategy);
		
		CamelContext ctx = new DefaultCamelContext(); 
		Exchange ex = new DefaultExchange(ctx);
		
		accessControlProcessor.authorize(ex);
		
		assertTrue((Boolean)ex.getIn().getHeader("accessControlIsAuthorized"));
		assertEquals("Null Object Strategy. This strategy will always authorize the exchange.", (String)ex.getIn().getHeader("accessControlResponseMessage"));
		
	}

	
}
