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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.custommonkey.xmlunit.DetailedDiff;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.bundles.adapters.fbi.ebts.application.FbiElectronicBiometricTransmissionSpecificationAdapterApplication;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


@UseAdviceWith
@CamelSpringBootTest
@SpringBootTest(classes=FbiElectronicBiometricTransmissionSpecificationAdapterApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
public class CamelContextTest {
	
	private static final Logger logger = (Logger) LogManager.getLogger(CamelContextTest.class);
	    
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate producerTemplate;
    
    @EndpointInject(uri = "mock:ngiUserServiceRequestEndpoint")
    protected MockEndpoint fbiEbtsSubscriptionManagerService;
    
    @BeforeEach
    public void setup() throws Exception{
    	
		XMLUnit.setIgnoreWhitespace(true);
    	XMLUnit.setIgnoreAttributeOrder(true);
    	XMLUnit.setIgnoreComments(true);
    	XMLUnit.setXSLTVersion("2.0");
    	
    	// replace'from' web service endpoint with a direct endpoint we call in our test
    	AdviceWith.adviceWith(context, "fbiEbtsInputWebServiceRoute", route -> {
    		route.replaceFromWith("direct:fbiEbtsInputEndpoint");
    	});
    	
    	AdviceWith.adviceWith(context, "fbiEbtsProcessingRoute", route -> {
    		route.mockEndpointsAndSkip("cxf:bean:ngiUserService*");
    	});
    	
    	AdviceWith.adviceWith(context, "ngiResponseServiceRoute", route -> {
    		route.mockEndpointsAndSkip("cxf:bean:ngiResponseService*");
    	});
    	
    	AdviceWith.adviceWith(context, "federalRapbackNotificationRoute", route -> {
    		route.mockEndpointsAndSkip("cxf:bean:arrestReportingService*"); 
    	});
    	
    	AdviceWith.adviceWith(context, "processOperationRoute", route -> {
    		route.interceptSendToEndpoint("https4:*").skipSendToOriginalEndpoint().to("mock:ngiUserServiceRequestEndpoint");
    	});
    	
    	context.start();	
    }
    
	@Test
	public void newCivilSubscriptionTest() throws Exception{
		
		fbiEbtsSubscriptionManagerService.reset();
		fbiEbtsSubscriptionManagerService.expectedMessageCount(1);
		
    	Exchange senderExchange = createExchange("src/test/resources/input/OJBC_Civil_Subscription_Request_Document.xml");
		
	    Exchange returnExchange = producerTemplate.send("direct:fbiEbtsInputEndpoint", senderExchange);
	    
		if (returnExchange.getException() != null) {
			throw new Exception(returnExchange.getException());
		}
								
		fbiEbtsSubscriptionManagerService.assertIsSatisfied();
		
		Document transformedReturnMessage = fbiEbtsSubscriptionManagerService.getExchanges().get(0).getIn().getBody(Document.class);
		
		XmlUtils.printNode(transformedReturnMessage);
		
		NodeList imageNodes = XmlUtils.xPathNodeListSearch(transformedReturnMessage, "//nistbio:NISTBiometricInformationExchangePackage/nistbio:PackageHighResolutionGrayscaleImageRecord");
		
		Assert.assertEquals(14, imageNodes.getLength());
		
		Element firstImage =  (Element) imageNodes.item(0);
		
		Assert.assertEquals("04",XmlUtils.xPathStringSearch(firstImage, "nbio:RecordCategoryCode"));
		Assert.assertEquals("1",XmlUtils.xPathStringSearch(firstImage, "nbio:ImageReferenceIdentification/nc:IdentificationID"));
		
		Assert.assertNotNull(XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nc:BinaryBase64Object"));
	
		Assert.assertEquals("1",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nbio:ImageCaptureDetail/nbio:CaptureResolutionCode"));
		Assert.assertEquals("1",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nbio:ImageCompressionAlgorithmCode"));
		
		Assert.assertEquals("800",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nbio:ImageHorizontalLineLengthPixelQuantity"));
		Assert.assertEquals("750",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nbio:ImageVerticalLineLengthPixelQuantity"));
		
		Assert.assertEquals("1",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nbio:FingerprintImagePosition/nbio:FingerPositionCode"));
		Assert.assertEquals("1",XmlUtils.xPathStringSearch(firstImage, "nbio:FingerprintImage/nbio:FingerprintImageImpressionCaptureCategoryCode"));
		
	}

	private Exchange createExchange(String fileName) throws IOException, Exception,
			ParserConfigurationException {
		Exchange senderExchange = new DefaultExchange(context);

	    File inputFile = new File(fileName);
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
		senderExchange.getIn().setHeader("operationName", "Subscribe");
		return senderExchange;
	}
	
	@Test
	public void newCriminalSubscriptionTest() throws Exception{
		
    	Exchange senderExchange = createExchange("src/test/resources/input/OJBC_Criminal_Subscription_Request_Document.xml");
		
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
