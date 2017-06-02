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
