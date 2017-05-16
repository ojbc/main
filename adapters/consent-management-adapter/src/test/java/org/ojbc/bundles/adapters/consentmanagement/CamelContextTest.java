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
package org.ojbc.bundles.adapters.consentmanagement;

import static org.junit.Assert.*;

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
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.bundles.adapters.consentmanagement.dao.ConsentManagementDAOImpl;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:META-INF/spring/properties-context.xml",
		"classpath:META-INF/spring/dao.xml",
		"classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-consent-management-datastore.xml",
		"classpath:META-INF/spring/cxf-endpoints.xml"
		})
@DirtiesContext
public class CamelContextTest {

	@SuppressWarnings("unused")
	private static final Log log = LogFactory.getLog( CamelContextTest.class );
	
	public static final String CXF_OPERATION_NAME = "ReportBooking";
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/BookingReportingService/1.0";
	
    @Resource
    private ModelCamelContext context;
	
    @Produce
    protected ProducerTemplate template;
    
	@Autowired
	private ConsentManagementDAOImpl consentManagementDAOImpl;
    
    @EndpointInject(uri = "mock:direct:processBooking")
    protected MockEndpoint directEndpoint;

	@Before
	public void setUp() throws Exception {
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
		//We mock the 'direct' endpoint to test against.
    	context.getRouteDefinition("bookingReportingServiceHandlerRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:BookingReportingServiceEndpoint");
    	    	mockEndpoints("direct:processBooking*");
    	    	
    	    }              
    	});
    	
		context.start();		
	}

	@Test
	public void testContextRoutes() throws Exception
	{
		//direct endpoint will get one message
		directEndpoint.expectedMessageCount(1);
		
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
    	
    	//Test the entire web service route by sending through a Booking Report
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);

	    //Read the booking report from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/bookingReport/BookingReportJail.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:BookingReportingServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(3000);

		directEndpoint.assertIsSatisfied();
		
		Consent consentRecord = consentManagementDAOImpl.returnConsentRecordfromId(1);
		
		assertNotNull(consentRecord);
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
