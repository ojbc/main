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
package org.ojbc.bundles.connectors;

import static org.junit.Assert.assertEquals;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ${package}.ConnectorFileProcessor;

/**
 * Simple test demonstrates using a camel runner 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@DirtiesContext
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml" })
public class ConnectorTest {
    
	@Autowired
    private ConnectorFileProcessor connectorFileProcessor;
	
	/**
	 * Simple test example displays sending data through the 
	 * camel exchange and asserting its resulting value is the 
	 * same.
	 */
    @Test
    public void testProcessMessage() throws Exception {
           	            	
        Exchange exchange = new DefaultExchange((CamelContext) null);
        
        Message inMessage = exchange.getIn();
        
        inMessage.setBody("testMessage");

        connectorFileProcessor.processMessage(exchange);
               
        Object receivedBody = exchange.getOut().getBody();

        assertEquals("testMessage", receivedBody);        
    }
        
}


