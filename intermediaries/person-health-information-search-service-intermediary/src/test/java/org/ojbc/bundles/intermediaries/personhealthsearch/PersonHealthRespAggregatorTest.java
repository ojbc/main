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
package org.ojbc.bundles.intermediaries.personhealthsearch;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Test;
import org.ojbc.bundles.intermediaries.personhealthsearch.aggregator.PersonHealthResponseAggregator;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;


public class PersonHealthRespAggregatorTest {

	static{		
		XMLUnit.setIgnoreAttributeOrder(true);
		XMLUnit.setIgnoreComments(true);
		XMLUnit.setIgnoreDiffBetweenTextAndCDATA(true);
		XMLUnit.setIgnoreWhitespace(true);
	}
	
	@Test
	public void aggregatorTest() throws Exception{
						
		PersonHealthResponseAggregator aggregator = new PersonHealthResponseAggregator();
		
		Exchange groupedExchange = getGroupedExchangeSample();
				
		aggregator.prepareResponseExchange(groupedExchange);
				
		String msgId = groupedExchange.getIn().getHeader("federatedQueryRequestGUID", String.class);
		Assert.assertEquals("123", msgId);
		
		Document groupedExchangeOutDoc = groupedExchange.getIn().getBody(Document.class);
		
		File healthResultsFile = new File(
				"src/test/resources/xmlInstances/PersonHealthInformationSearchResults-full-detail.xml");	
		Document healthResultsDoc = XmlUtils.parseFileToDocument(healthResultsFile);
		
		XMLUnit.compareXML(healthResultsDoc, groupedExchangeOutDoc);			
	}
	
	private Exchange getGroupedExchangeSample() throws Exception{
		
		CamelContext camelContext = new DefaultCamelContext();
		
		Exchange groupedExchange = new DefaultExchange(camelContext);
		
		Exchange timerExchange = new DefaultExchange(camelContext);		
		timerExchange.getIn().setHeader("federatedQueryRequestGUID", "123");		
		timerExchange.getIn().setBody("START_PERSON_HEALTH_TIMER");
		
		
		Exchange personHealthResultsExchange = new DefaultExchange(camelContext);		
		File healthResultsFile = new File(
				"src/test/resources/xmlInstances/PersonHealthInformationSearchResults-full-detail.xml");	
		Document healthResultsDoc = XmlUtils.parseFileToDocument(healthResultsFile);		
		personHealthResultsExchange.getIn().setBody(healthResultsDoc);
		

		List<Exchange> exchangeList = new ArrayList<Exchange>();
		exchangeList.add(timerExchange);
		exchangeList.add(personHealthResultsExchange);
		
		groupedExchange.setProperty(Exchange.GROUPED_EXCHANGE, exchangeList);
		
		return groupedExchange;
	}
	
}
