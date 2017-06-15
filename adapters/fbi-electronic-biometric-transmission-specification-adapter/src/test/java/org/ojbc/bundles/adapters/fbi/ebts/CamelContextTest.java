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
package org.ojbc.bundles.adapters.fbi.ebts;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.log4j.Logger;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.camel.helper.OJBUtils;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/file-drop-routes.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",  
        "classpath:META-INF/spring/error-handlers.xml",  
        "classpath:META-INF/spring/properties-context.xml",
        })
public class CamelContextTest {
	
	private static final Logger logger = Logger.getLogger(CamelContextTest.class);
	    
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate producerTemplate;
    	
    @Before
    public void setup() throws Exception{
    	
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
    	
    	// replace'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("fbiEbtsInputWebServiceRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	
    	    	// bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:fbiEbtsInputEndpoint");    	    	
    	    }              
    	}); 
    	
    	context.getRouteDefinition("fbiEbtsProcessingRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {    	    	
    	    	mockEndpointsAndSkip("cxf:bean:ngiUserService*");
    	    }              
    	});
    	
    	context.getRouteDefinition("ngiResponseServiceRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {    	    	
    	    	mockEndpointsAndSkip("cxf:bean:ngiResponseService*");
    	    }              
    	});      
    	
    	context.getRouteDefinition("federalRapbackNotificationRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {    	    	
    	    	mockEndpointsAndSkip("cxf:bean:arrestReportingService*");    	    	
    	    }              
    	});       	    	
    	
    	context.start();	
    }
    
	@Test
	public void contextStartup() {
		Assert.assertTrue(true);
	}
	
	
	@Test
	public void newCriminalSubscriptionTest() throws Exception{
		
    	Exchange senderExchange = new DefaultExchange(context);

	    File inputFile = new File("src/test/resources/input/OJBC_Criminal_Subscription_Request_Document.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    Assert.assertNotNull(inputStr);
	    
	    logger.info(inputStr);
	    
	    senderExchange.getIn().setBody(inputStr);	    
	    	    
	    	    
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);				
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader("http://www.w3.org/2005/08/addressing", "MessageID", "12345"));		
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		
	    Exchange returnExchange = producerTemplate.send("direct:fbiEbtsInputEndpoint", senderExchange);
	    
		if (returnExchange.getException() != null) {
			throw new Exception(returnExchange.getException());
		}
								
		String transformedReturnMessage = returnExchange.getIn().getBody(String.class);
		
		logger.info("return message: \n" + transformedReturnMessage + "\n\n");		
		
		//assert the transformed xml against expected xml output doc				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/EBTS-RapBack-Criminal-Subscription-Request.xml"));
							
		Diff diff = XMLUnit.compareXML(expectedXmlString, transformedReturnMessage);		
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		logger.info("TODO, fix assertion on transaction date: \n\n" + detailedDiff);		
//		Assert.assertEquals(detailedDiff.toString(), 0, detailedDiff.getAllDifferences().size());
	    
	}
	
	@Test
	public void modifyCriminalSubscriptionTest() throws Exception{
		
    	Exchange senderExchange = new DefaultExchange(context);

	    File inputFile = new File("src/test/resources/input/OJBC_Subscription_Modify_Document.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    Assert.assertNotNull(inputStr);
	    
	    logger.info(inputStr);
	    
	    senderExchange.getIn().setBody(inputStr);	    
	    	    
	    	    
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);				
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader("http://www.w3.org/2005/08/addressing", "MessageID", "12345"));		
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		
	    Exchange returnExchange = producerTemplate.send("direct:fbiEbtsInputEndpoint", senderExchange);
	    
		if (returnExchange.getException() != null) {
			throw new Exception(returnExchange.getException());
		}
		
		String transformedReturnMessage = returnExchange.getIn().getBody(String.class);
		
		logger.info("return message: \n" + transformedReturnMessage);
		
		
		//assert the transformed xml against expected xml output doc				
		String expectedXmlString = FileUtils.readFileToString(
				new File("src/test/resources/output/EBTS-RapBack-Subscription-Maintenance-Replace-Request.xml"));
							
		Diff diff = XMLUnit.compareXML(expectedXmlString, transformedReturnMessage);		
		
		DetailedDiff detailedDiff = new DetailedDiff(diff);
		
		logger.info("TODO, fix assertion on transaction date: \n\n" + detailedDiff);
//		Assert.assertEquals(detailedDiff.toString(), 0, detailedDiff.getAllDifferences().size());
	    
	}
	
	private SoapHeader makeSoapHeader(String namespace, String localName, String value) throws ParserConfigurationException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		Document doc = dbf.newDocumentBuilder().newDocument();
		
		Element messageId = doc.createElementNS(namespace, localName);
		messageId.setTextContent(value);
		SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
		return soapHeader;
	}	

}
