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
package org.ojbc.util.fedquery.processor;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class TestDefaultFederatedSearchTimeoutProcessor {

	/**
	 * This test will confirm that an error block will be created for the endpoint that did not respond
	 * using the provided hashmaps with the error text and system name mappings.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testCreateErrorMessagesForEndpointsThatTimedOut() throws Exception
	{
		DefaultFederatedSearchTimeoutProcessor fstp = new DefaultFederatedSearchTimeoutProcessor();
		
		List<String> endpointsThatDidNotRespond = new ArrayList<String>();
		endpointsThatDidNotRespond.add("http://ojbc.org/system1");
		endpointsThatDidNotRespond.add("http://ojbc.org/system2");
		
		Map<String, String> uriToErrorMessageMap = new HashMap<String, String>();
		uriToErrorMessageMap.put("http://ojbc.org/system1", "System one did not repond");
		uriToErrorMessageMap.put("http://ojbc.org/system2", "System two did not repond");
		
		Map<String, String> uriToErrorSystemNameMap= new HashMap<String, String>();
		uriToErrorSystemNameMap.put("http://ojbc.org/system1", "Records Management System One");
		uriToErrorSystemNameMap.put("http://ojbc.org/system2", "Records Management System Two");
		
		fstp.setUriToErrorMessageMap(uriToErrorMessageMap);
		fstp.setUriToErrorSystemNameMap(uriToErrorSystemNameMap);
		
	    Document wrapperSearchResponseDocument = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/WrappedPersonSearchResponse.xml"));
	    assertNotNull(wrapperSearchResponseDocument);

	    fstp.setParentElementName("PersonSearchResults");
	    fstp.setParentElementNamespace(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_DOC);
		
		Document updatedResponseDocument = fstp.createErrorMessagesForEndpointsThatTimedOut(wrapperSearchResponseDocument, endpointsThatDidNotRespond);
	
		assertNotNull(updatedResponseDocument);
		
		assertEquals("System one did not repond",XmlUtils.xPathStringSearch(updatedResponseDocument, "/OJBAggregateResponseWrapper/psres-doc:PersonSearchResults/srm:SearchResultsMetadata/srer:SearchRequestError[1]/srer:ErrorText"));
		assertEquals("Records Management System One",XmlUtils.xPathStringSearch(updatedResponseDocument, "/OJBAggregateResponseWrapper/psres-doc:PersonSearchResults/srm:SearchResultsMetadata/srer:SearchRequestError[1]/intel:SystemName"));

		assertEquals("System two did not repond",XmlUtils.xPathStringSearch(updatedResponseDocument, "/OJBAggregateResponseWrapper/psres-doc:PersonSearchResults/srm:SearchResultsMetadata/srer:SearchRequestError[2]/srer:ErrorText"));
		assertEquals("Records Management System Two",XmlUtils.xPathStringSearch(updatedResponseDocument, "/OJBAggregateResponseWrapper/psres-doc:PersonSearchResults/srm:SearchResultsMetadata/srer:SearchRequestError[2]/intel:SystemName"));

	}
	
	/**
	 * This test will confirm that if no endpoints time out that the message will not have a SearchResultsMetadata container added
	 * 
	 * @throws Exception
	 */

	@Test
	public void testNoEndpointsTimedOut() throws Exception
	{
		DefaultFederatedSearchTimeoutProcessor fstp = new DefaultFederatedSearchTimeoutProcessor();
		
		List<String> endpointsThatDidNotRespond = new ArrayList<String>();
		
	    Document wrapperSearchResponseDocument = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/WrappedPersonSearchResponse.xml"));
	    assertNotNull(wrapperSearchResponseDocument);

	    fstp.setParentElementName("PersonSearchResults");
	    fstp.setParentElementNamespace(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_DOC);
		
		Document updatedResponseDocument = fstp.createErrorMessagesForEndpointsThatTimedOut(wrapperSearchResponseDocument, endpointsThatDidNotRespond);
	
		assertNotNull(updatedResponseDocument);
		
		NodeList searchResultsMetadata = XmlUtils.xPathNodeListSearch(updatedResponseDocument, "/OJBAggregateResponseWrapper/psres-doc:PersonSearchResults/srm:SearchResultsMetadata");
		
		assertEquals(0,searchResultsMetadata.getLength());
		
	}
	
}
