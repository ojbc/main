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
package org.ojbc.bundles.intermediaries.firearmsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.MessageImpl;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.custommonkey.xmlunit.Diff;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.XmlUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@UseAdviceWith	// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/extensible-beans.xml",	
		"classpath:META-INF/spring/jetty-server.xml",
		"classpath:META-INF/spring/local-osgi-context.xml",
		"classpath:META-INF/spring/properties-context.xml"}) 
public class TestFirearmSearchRequestServiceIntermediary {
	public static final String CXF_OPERATION_NAME = "SubmitFirearmSearchRequest";
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/FirearmSearchRequestService/1.0";
	
	private static final Log log = LogFactory.getLog( TestFirearmSearchRequestServiceIntermediary.class );
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:firearmSearchRequestServiceAdapter")
    protected MockEndpoint firearmSearchResultsMock;
	
    @EndpointInject(uri = "mock:maxRecordsProcessorMock")
    protected MockEndpoint maxRecordsProcessorMock;

    
    @Test
    public void testApplicationStartup() {
    	assertTrue(true);
    }	
    
	@Before
	public void setUp() throws Exception {
    	//Advise the firearm search results endpoint and replace it with a mock endpoint.
    	//We then will test this mock endpoint to see if it gets the proper payload.
    	context.getRouteDefinition("searchRequestFederatedServiceEndpointRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:searchRequestFederatedServiceEndpoint");
    	    	mockEndpoints("firearmSearchRequestServiceAdapterEndpoint");
    	    }              
    	});

    	context.getRouteDefinition("processFederatedResponseRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
				// The line below allows us to bypass CXF and send a message directly into the route
    	    	interceptSendToEndpoint("direct:sendMergeMessageResponse").to("mock:maxRecordsProcessorMock").stop();    	
    	    }              
    	});

    	
    	context.start();
	}
    
    @Test
    public void testFirearmSearchMaxRecords() throws Exception {
    	
    	maxRecordsProcessorMock.expectedMessageCount(1);

    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);

	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/firearmSearchResults/firearmSearchResultsLarge.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:processFederatedResponse", senderExchange);

    	
    	maxRecordsProcessorMock.assertIsSatisfied();
    	String errorMessage = (String)maxRecordsProcessorMock.getReceivedExchanges().get(0).getIn().getBody();
    	log.debug("Here is the error response: " + errorMessage);
    	assertEquals("<exc:EntityMergeResultMessage xmlns:exc=\"http://nij.gov/IEPD/Exchange/EntityMergeResultMessage/1.0\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:srer=\"http://ojbc.org/IEPD/Extensions/SearchRequestErrorReporting/1.0\" xmlns:intel=\"http://niem.gov/niem/domains/intelligence/2.1\" xmlns:srm=\"http://ojbc.org/IEPD/Extensions/SearchResultsMetadata/1.0\"> <exc:SearchResultsMetadataCollection> 	<srm:SearchResultsMetadata> 		<srer:SearchRequestError> 			<srer:ErrorText>The search results contained too many records, please refine your search.  The maximum combined number of records that will display is 200.<br />The Demostate search results contained 232 records.<br /></srer:ErrorText> 			<intel:SystemName>All Systems</intel:SystemName> 		</srer:SearchRequestError> 	</srm:SearchResultsMetadata> </exc:SearchResultsMetadataCollection></exc:EntityMergeResultMessage>", errorMessage);
    }	
    
    @Test
    public void testFirearmSearch() throws Exception {
        	
    	//We should get one message
    	firearmSearchResultsMock.expectedMessageCount(1);

    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
				
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = OJBCXMLUtils.createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", "https://localhost:18311/OJB/WebApp/FirearmSearchResultsHandlerService"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		org.apache.cxf.message.Message message = new MessageImpl();

		//Add SAML token to request call
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(new SamlAssertionWrapper(samlToken));
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE);
	    
	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/FirearmSearchRequest.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:searchRequestFederatedServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(3000);
		
		//Assert that the mock endpoint expectations are satisfied
		firearmSearchResultsMock.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = firearmSearchResultsMock.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("SubmitFirearmSearchRequest", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://ojbc.org/Services/WSDL/FirearmSearchRequestService/1.0", opNamespace);
		
		//Get the actual response
		Document actualResponseDocument = (Document)ex.getIn().getBody();
		XmlUtils.printNode(actualResponseDocument);

		String actualResponse = OJBUtils.getStringFromDocument(actualResponseDocument);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xmlInstances/FirearmSearchRequest.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
			
		log.debug("Expected Response: " + expectedResponseAsString);
	
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(expectedResponseAsString, actualResponse);
		assertTrue("XML should be identical " + myDiff.toString(),
		               myDiff.identical());
    	
    }

	private SoapHeader makeSoapHeader(Document doc, String namespace, String localName, String value) {
		Element messageId = doc.createElementNS(namespace, localName);
		messageId.setTextContent(value);
		SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
		return soapHeader;
	}	
}
