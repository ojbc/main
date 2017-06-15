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
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.w3c.dom.Document;

public class TestRapbackEventTextProcessor {

	@Test
	public void processRapbackEventTextConsolidation() throws Exception
	{
	    //Read the criminal history update file from the file system
	    File inputFile = new File("src/test/resources/input/FBI_RBN_UCN_Consolidation.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

		Document fbiRBNucnConsolidation = OJBUtils.loadXMLFromString(inputStr);
		
		RapbackEventTextProcessor rapbackEventTextProcessor = new RapbackEventTextProcessor();
		
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange ex = new DefaultExchange(ctx);
		
		ex = rapbackEventTextProcessor.processRapbackEventText(fbiRBNucnConsolidation, ex);
		
		assertEquals("YYYYYYYYYY",(String) ex.getIn().getHeader("retainedIdentity"));
		assertEquals("XXXXXXXXX", (String)ex.getIn().getHeader("deletedIdentity"));
		
	}
	
	@Test
	public void processRapbackEventTextRestoration() throws Exception
	{
	    //Read the criminal history update file from the file system
	    File inputFile = new File("src/test/resources/input/FBI_RBN_UCN_Restoration.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

		Document fbiRBNucnConsolidation = OJBUtils.loadXMLFromString(inputStr);
		
		RapbackEventTextProcessor rapbackEventTextProcessor = new RapbackEventTextProcessor();
		
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange ex = new DefaultExchange(ctx);
		
		ex = rapbackEventTextProcessor.processRapbackEventText(fbiRBNucnConsolidation, ex);
		
		assertEquals("XXXXXXXXX",(String) ex.getIn().getHeader("restoredIdentity"));
		assertEquals("yyyyyyy", (String)ex.getIn().getHeader("deletedSubscription"));
		
	}

	@Test
	public void processRapbackEventTextDeletion() throws Exception
	{
	    //Read the criminal history update file from the file system
	    File inputFile = new File("src/test/resources/input/FBI_RBN_UCN_Deletion.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

		Document fbiRBNucnConsolidation = OJBUtils.loadXMLFromString(inputStr);
		
		RapbackEventTextProcessor rapbackEventTextProcessor = new RapbackEventTextProcessor();
		
	    CamelContext ctx = new DefaultCamelContext(); 
	    Exchange ex = new DefaultExchange(ctx);
		
		ex = rapbackEventTextProcessor.processRapbackEventText(fbiRBNucnConsolidation, ex);

		assertEquals("9999999", (String)ex.getIn().getHeader("deletedIdentity"));
		assertEquals("8888888",(String) ex.getIn().getHeader("deletedSubscription"));
		
	}

}
