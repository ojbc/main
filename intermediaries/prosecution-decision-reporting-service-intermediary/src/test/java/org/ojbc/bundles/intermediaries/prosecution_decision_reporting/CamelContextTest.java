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
package org.ojbc.bundles.intermediaries.prosecution_decision_reporting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.headers.Header;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.intermediaries.prosecutionreporting.ProsecutionDecisionReportingIntermediaryApplication;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

@CamelSpringBootTest
@SpringBootTest(classes=ProsecutionDecisionReportingIntermediaryApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@UseAdviceWith
public class CamelContextTest {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
	public static final String CXF_OPERATION_NAME = "ReportProsecutionDecision";
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/ProsecutionDecisionReportingService/1.0";
	
    @Resource
    private ModelCamelContext context;
	
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:prosecutionDecisionReportingServiceAdapter")
    protected MockEndpoint prosecutionDecisionReportingServiceAdapterEndpoint;

    @EndpointInject(uri = "mock:cxf:bean:courtCaseFilingServiceAdapter")
    protected MockEndpoint courtCaseFilingServiceAdapterEndpoint;
      
    @EndpointInject(uri = "mock:log:org.ojbc.intermediaries.prosecution_decision_reporting")
    protected MockEndpoint loggingEndpoint;

    
	@BeforeEach
	public void setUp() throws Exception {
		
		
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
		//We mock the 'log' endpoint to test against.
		AdviceWith.adviceWith(context, "ProsecutionDecisionReportingServiceHandlerRoute", route -> {
			route.replaceFromWith("direct:prosecutionDecisionReportingServiceWebServiceEndpoint");
	    	route.mockEndpoints("log:org.ojbc.intermediaries.prosecution_decision_reporting*");
		});
		
		
		
    	//We mock the web service endpoints here
		AdviceWith.adviceWith(context, "callProsecutionDecisionFilingRoute", route -> {
			route.mockEndpointsAndSkip("cxf:bean:prosecutionDecisionReportingServiceAdapter*");
		});
		
    	//We mock the web service endpoints here
		AdviceWith.adviceWith(context, "callChargeFilingRoute", route -> {
			route.mockEndpointsAndSkip("cxf:bean:courtCaseFilingServiceAdapter*");
		});
		
		context.start();		
	}

	@Test
	public void testContextRoutes() throws Exception
	{
		
    	//Court Case Filing will get one message
		prosecutionDecisionReportingServiceAdapterEndpoint.expectedMessageCount(1);
		courtCaseFilingServiceAdapterEndpoint.expectedBodiesReceived(1);
		
		//logging endpoint will get two messages, one from content enricher and one from derived routes.
		loggingEndpoint.expectedMessageCount(2);
		
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
    	
    	//Test the entire web service route by sending through a Case Filing Decision Report
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the case filing decision report from the file system																							
	    File inputFile = new File("src/test/resources/xmlInstances/prosecutionDecisionReporting/ProsecutionDecision-Person.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:prosecutionDecisionReportingServiceWebServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(3000);

		//Assert that the mock endpoint expectations are satisfied
		prosecutionDecisionReportingServiceAdapterEndpoint.assertIsSatisfied();
		
		loggingEndpoint.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = prosecutionDecisionReportingServiceAdapterEndpoint.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals(CXF_OPERATION_NAME, opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals(CXF_OPERATION_NAMESPACE, opNamespace);

		//Get the first exchange (the only one) to the logger
		//This is what would be sent to the derived bundle
		Exchange derivedBundleExchange = loggingEndpoint.getExchanges().get(0);

		Document returnDocumentDerivedBundle = derivedBundleExchange.getIn().getBody(Document.class);
		
		XmlUtils.printNode(returnDocumentDerivedBundle);
				
		//Make sure the root node here is the message to the original exchange
		Node rootNode = XmlUtils.xPathNodeSearch(returnDocumentDerivedBundle, "/pd-doc:ProsecutionDecisionReport");
		assertNotNull(rootNode);
		
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
