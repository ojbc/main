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
package org.ojbc.bundles.intermediaries.warrantissuedreporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
		"classpath:META-INF/spring/camel-context.xml", 
		"classpath:META-INF/spring/cxf-endpoints.xml",
		"classpath:META-INF/spring/jetty-server.xml",
		"classpath:META-INF/spring/properties-context.xml"}) 
public class CamelContextTest {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
	public static final String CXF_OPERATION_NAME = "ReportWarrantIssued";
	
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/WarrantIssuedReportingService/1.0";
	
    @Resource
    private ModelCamelContext context;
	
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:warrantIssuedReportingAdapterService")
    protected MockEndpoint warrantIssuedReportingMockEndpoint;
    
    @EndpointInject(uri = "mock:log:org.ojbc.bundles.intermediaries.warrantissuedreporting")
    protected MockEndpoint loggingEndpoint;

	
	@Test
	public void contextStartup() {
		assertTrue(true);
	}
    
	@Test
	public void setUp() throws Exception {
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
		//We mock the 'log' endpoint to test against.
    	context.getRouteDefinition("warrantIssuedReporting_Route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:WarrantIssuedReportingService");
    	    	mockEndpoints("log:org.ojbc.bundles.intermediaries.warrantissuedreporting*");    	    	
    	    }              
    	});
    	
    	// mock the web service endpoints
    	context.getRouteDefinition("warrant_issued_route").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	
    	    	// mock the adapter endpoint
    	    	mockEndpointsAndSkip("cxf:bean:warrantIssuedReportingAdapterService*");    	    	
    	    }              
    	});    	
    	
		context.start();		
	}


	@Test
	public void testContextRoutes() throws Exception{
		
    	// will get one message
		warrantIssuedReportingMockEndpoint.expectedMessageCount(1);
		
		//logging endpoint will get one message from derived routes.
		loggingEndpoint.expectedMessageCount(1);
		
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
    	
    	//Test the entire web service route by sending through an warrant report
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the warrant report from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/warrantReporting/WarrantIssuedReport.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    senderExchange.getIn().setHeader("operationName", "ReportWarrantIssued");
	    senderExchange.getIn().setHeader("operationNamespace", "http://ojbc.org/Services/WSDL/WarrantIssuedReportingService/1.0");
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:WarrantIssuedReportingService", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(1000);

		//Assert that the mock endpoint expectations are satisfied
		warrantIssuedReportingMockEndpoint.assertIsSatisfied();
		loggingEndpoint.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = warrantIssuedReportingMockEndpoint.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("ReportWarrantIssued", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://ojbc.org/Services/WSDL/WarrantIssuedReportingService/1.0", opNamespace);

		Document returnDocWarrantAdapter = ex.getIn().getBody(Document.class);

		//Do some very basic assertions to assure the message is transformed.
		//The XSLT test does a more complete examination of the transformation.
		Node warrantRootNode = XmlUtils.xPathNodeSearch(returnDocWarrantAdapter, "/wir-doc:WarrantIssuedReport");
		
		assertNotNull(warrantRootNode);

		//Get the first exchange (the only one) to the logger
		//This is what would be sent to the derived bundle
		Exchange derivedBundleExchange = loggingEndpoint.getExchanges().get(0);

		Document returnDocDerivedBundle = derivedBundleExchange.getIn().getBody(Document.class);

		//Make sure the root node here is the message to the original exchange
		Node derivedBundleMsgRootNode = XmlUtils.xPathNodeSearch(returnDocDerivedBundle, "/wir-doc:WarrantIssuedReport");
		assertNotNull(derivedBundleMsgRootNode);
	
	}
	
	private SoapHeader makeSoapHeader(Document doc, String namespace, String localName, String value) {
		
		Element messageId = doc.createElementNS(namespace, localName);
		messageId.setTextContent(value);
		SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
		return soapHeader;
	}		
	
	public static Document createDocument() throws Exception{

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();

		return doc;
	}
	
}
