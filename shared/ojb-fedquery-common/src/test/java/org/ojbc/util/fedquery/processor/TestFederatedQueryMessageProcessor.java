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
package org.ojbc.util.fedquery.processor;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.ojbc.util.fedquery.FederatedQueryProfile;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class TestFederatedQueryMessageProcessor {

	@Test
	public void testProcessSystemName() throws Exception{
	    Map<String, List<FederatedQueryProfile>> federatedQueryManager  = new HashMap<String, List<FederatedQueryProfile>>();
	    
	    FederatedQueryMessageProcessor federatedQueryMessageProcessor = new FederatedQueryMessageProcessor();
	    federatedQueryMessageProcessor.setFederatedQueryManager(federatedQueryManager);
	    
	    Document searchDocument = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/PersonSearchRequest.xml"));
	    assertNotNull(searchDocument);
	    
	    NodeList sourceSystems = XmlUtils.xPathNodeListSearch(searchDocument, "//psr:SourceSystemNameText");
	    assertNotNull(sourceSystems);
	    
	    federatedQueryMessageProcessor.processSystemName("12345", sourceSystems);
	    
	    assertTrue(federatedQueryMessageProcessor.getFederatedQueryManager().containsKey("12345"));
	    
	    List<FederatedQueryProfile> queryProfile = (List<FederatedQueryProfile>)federatedQueryMessageProcessor.getFederatedQueryManager().get("12345");
	    
	    assertEquals("{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-RMS", queryProfile.get(0).getServiceName());
	    assertEquals("{http://ojbc.org/Services/WSDL/PersonSearchRequestService/1.0}SubmitPersonSearchRequest-Warrants", queryProfile.get(1).getServiceName());
	    
	}	
}
