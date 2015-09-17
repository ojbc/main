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
package org.ojbc.bundles.intermediaries.parole.event;

import java.io.File;

import junit.framework.Assert;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;


public class PersonSearchAggregatorTest {
	
	private PersonSearchEntityResolutionResponseHandlerAggregator personSearchAggregator;

	@Before
	public void init() {

		personSearchAggregator = new PersonSearchEntityResolutionResponseHandlerAggregator();
	}
	
	@Test
	public void setFbiHeaderTest() throws Exception{
		
		CamelContext camelContext = new DefaultCamelContext();		
		Exchange exchange = new DefaultExchange(camelContext);
				
		Document personSearchResponseDoc = XmlUtils.parseFileToDocument(new File("src/main/resources/mock/PersonSearchResults_single.xml"));;
		
		String fbiId = personSearchAggregator.populateGroupExchFbiIdHeaderFromPersonSearchResponse(personSearchResponseDoc, exchange);		
		Assert.assertEquals("12345", fbiId);
		
		String fbiIdHeader = (String)exchange.getIn().getHeader("fbiId");		
		Assert.assertEquals("12345", fbiIdHeader);
	}
	
	@Test
	public void copyCaseInitExchHeadersTest(){
	
		CamelContext camelContext = new DefaultCamelContext();
		
		Exchange exchange = new DefaultExchange(camelContext);
		exchange.getIn().setHeader("federatedQueryRequestGUID", "123");
		exchange.getIn().setHeader("topicExpression", "arrest");
				
		Exchange groupExchange = new DefaultExchange(camelContext);
		
		personSearchAggregator.copyCaseInitExchHeadersToGroupedExchange(groupExchange, exchange);
		
		String groupFeqQueryId = (String)groupExchange.getIn().getHeader("federatedQueryRequestGUID");
		String groupTopic = (String)groupExchange.getIn().getHeader("topicExpression");
		
		Assert.assertEquals("123", groupFeqQueryId);
		Assert.assertEquals("arrest", groupTopic);
	}
	
}
