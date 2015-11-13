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
package org.ojbc.bundles.adapters.fbi.ebts.processor;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.activation.DataHandler;

import junit.framework.Assert;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

public class RapsheetProcessorTest {
		
	@Test
	public void attachMtomRapsheetTest() throws IOException{
		
		RapsheetMtomProcessor rapsheetMtomProcessor = new RapsheetMtomProcessor();
		
		CamelContext camelContext = new DefaultCamelContext();		
		Exchange exchange = new DefaultExchange(camelContext);
		
		exchange.getIn().setHeader("rapsheet", "<Fbi></Fbi>");		
		rapsheetMtomProcessor.attachMtomRapsheet(exchange);
				
		DataHandler rapsheetDataHandler = exchange.getIn().getAttachment("http://ojbc.org/arrest/document");
		InputStream rapsheetInStream = rapsheetDataHandler.getInputStream();
		
		String rapsheetAttached = IOUtils.toString(rapsheetInStream);		
		Assert.assertEquals("<Fbi></Fbi>", rapsheetAttached);		
	}
}


