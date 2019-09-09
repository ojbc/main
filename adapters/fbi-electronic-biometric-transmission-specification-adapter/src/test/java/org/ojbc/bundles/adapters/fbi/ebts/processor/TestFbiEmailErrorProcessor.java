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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class TestFbiEmailErrorProcessor {

	@Test
	public void testFbiEmailProcessor() throws Exception
	{
		FbiEmailErrorProcessor emailErrorProcessor = new FbiEmailErrorProcessor();
		
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange ex = new DefaultExchange(ctx);
		
	    Document doc = XmlUtils.parseFileToDocument(new File("src/test/resources/input/FBI_SUBSCRIPTION_RESPONSE_ERRA.xml"));
	    ex.getIn().setBody(doc);

	    ex.getIn().setHeader("trxCatCode", "Error code");
	    
		emailErrorProcessor.createErrorEmail(ex);
		
		System.out.println(ex.getIn().getBody(String.class));
		
		assertEquals(ex.getIn().getBody(String.class), "Error Code:Error code\nError Text:This is the transaction text: RB001: some error | This is the second transaction text\n\nThis subscription request that was sent to the FBI has failed.");
	}
	
}
