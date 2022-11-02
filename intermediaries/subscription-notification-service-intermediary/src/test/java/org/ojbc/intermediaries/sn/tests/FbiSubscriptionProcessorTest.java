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
package org.ojbc.intermediaries.sn.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;

import javax.annotation.Resource;

import org.apache.camel.Exchange;
import org.apache.camel.support.DefaultExchange;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.jupiter.api.Test;
import org.ojbc.intermediaries.sn.FbiSubscriptionProcessor;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class FbiSubscriptionProcessorTest extends AbstractSubscriptionNotificationIntegrationTest {
    
    @SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog(FbiSubscriptionIntegrationTest.class);
    
    @Resource
    FbiSubscriptionProcessor fbiSubscriptionProcessor;
    

    @Test
    public void testRouteToProcessFbiSubscriptionRoute() throws Exception
    {
    	
    	Document civilSubscriptionDocument = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/fbi/OJBC_Civil_Subscription_Request_Document.xml"));
    	
    	assertTrue(fbiSubscriptionProcessor.routeToProcessFbiSubscriptionRoute(civilSubscriptionDocument));
    	
    }
    
    @Test
    public void testProcessSubscription() throws Exception
    {
    	
    	Document civilSubscriptionDocument = XmlUtils.parseFileToDocument(new File("src/test/resources/xmlInstances/fbi/OJBC_Civil_Subscription_Request_Document.xml"));
    	Exchange exchange = new DefaultExchange(context);

    	Document document = fbiSubscriptionProcessor.processSubscription(exchange, null, civilSubscriptionDocument);
    	
    	String base64File = XmlUtils.xPathStringSearch(document, "//submsg-exch:SubscriptionMessage/submsg-ext:FingerprintDocument/nc:DocumentBinary/submsg-ext:Base64BinaryObject");
    	
    	assertNotNull(base64File);
    	
    	assertEquals("StateCivilFingerPrints",  new String(Base64.decodeBase64(base64File), StandardCharsets.UTF_8));

    }
    
}