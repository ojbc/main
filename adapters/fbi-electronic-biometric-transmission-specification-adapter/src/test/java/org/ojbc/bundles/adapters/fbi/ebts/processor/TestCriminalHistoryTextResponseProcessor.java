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

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.charset.StandardCharsets;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;


public class TestCriminalHistoryTextResponseProcessor {

	@Test
	public void testFbiEmailProcessor() throws Exception
	{
		CriminalHistoryTextResponseProcessor criminalHistoryTextResponseProcessor = new CriminalHistoryTextResponseProcessor();
		
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange ex = new DefaultExchange(ctx);
		
	    Document doc = XmlUtils.parseFileToDocument(new File("src/test/resources/input/Template(RBIHSR)RapBackIdentityHistorySummaryResponse.xml"));
	    ex.getIn().setBody(doc);

	    Document ret = criminalHistoryTextResponseProcessor.createCriminalHistoryTextResponse(ex);
	    
	    String rapSheet = XmlUtils.xPathStringSearch(ret, "//cht-doc:Base64BinaryObject");
	    
	    String str = new String(Base64.decodeBase64(rapSheet), StandardCharsets.UTF_8);
	    
	    assertEquals(str,"Subject's Rap Sheet goes here");
	    
	}
	
}
