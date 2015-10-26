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
		
		Map<String, String> uriToErrorMessageMap = new HashMap<String, String>();
		uriToErrorMessageMap.put("http://ojbc.org/system1", "System one did not repond");
		
		Map<String, String> uriToErrorSystemNameMap= new HashMap<String, String>();
		uriToErrorSystemNameMap.put("http://ojbc.org/system1", "Records Management System One");
		
		fstp.setUriToErrorMessageMap(uriToErrorMessageMap);
		fstp.setUriToErrorSystemNameMap(uriToErrorSystemNameMap);
		
	    Document wrapperSearchResponseDocument = XmlUtils.parseFileToDocument(new File("src/test/resources/xml/WrappedPersonSearchResponse.xml"));
	    assertNotNull(wrapperSearchResponseDocument);

	    fstp.setParentElementName("PersonSearchResults");
	    fstp.setParentElementNamespace(OjbcNamespaceContext.NS_PERSON_SEARCH_RESULTS_DOC);
		
		Document updatedResponseDocument = fstp.createErrorMessagesForEndpointsThatTimedOut(wrapperSearchResponseDocument, endpointsThatDidNotRespond);
	
		assertNotNull(updatedResponseDocument);
		
		assertEquals("System one did not repond",XmlUtils.xPathStringSearch(updatedResponseDocument, "/OJBAggregateResponseWrapper/psres-doc:PersonSearchResults/srm:SearchResultsMetadata/srer:SearchRequestError/srer:ErrorText"));
		assertEquals("Records Management System One",XmlUtils.xPathStringSearch(updatedResponseDocument, "/OJBAggregateResponseWrapper/psres-doc:PersonSearchResults/srm:SearchResultsMetadata/srer:SearchRequestError/intel:SystemName"));
		
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
