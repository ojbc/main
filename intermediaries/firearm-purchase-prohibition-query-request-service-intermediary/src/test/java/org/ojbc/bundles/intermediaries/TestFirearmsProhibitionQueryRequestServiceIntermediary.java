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
package org.ojbc.bundles.intermediaries;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.ojbc.intermediaires.firearmpurchase.FirearmPurchaseProhibitionQueryRequestServiceApplication;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.helper.OJBCXMLUtils;
import org.ojbc.util.xml.XmlUtils;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@CamelSpringBootTest
@SpringBootTest(classes=FirearmPurchaseProhibitionQueryRequestServiceApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@UseAdviceWith
public class TestFirearmsProhibitionQueryRequestServiceIntermediary {

	private static final Log log = LogFactory.getLog( TestFirearmsProhibitionQueryRequestServiceIntermediary.class );
	
	public static final String CXF_OPERATION_NAME = "SubmitFirearmPurchaseProhibitionQueryRequest";
	public static final String CXF_OPERATION_NAMESPACE = "http://ojbc.org/Services/WSDL/Firearm_Purchase_Prohibition_Query_Request_Service/1.0";
	
    @Resource
    private ModelCamelContext context;

    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(value = "mock:cxf:bean:firearmsPurchaseProhibitionQueryServiceAdapter")
    protected MockEndpoint firearmsPurchaseProhibitionQueryServiceMock;
    
	@BeforeEach
	public void setUp() throws Exception {
		
		AdviceWith.adviceWith(context, context.getRouteDefinition("firearmsPurchaseProhibitionQueryServiceRoute"), route -> {
			route.replaceFromWith("direct:firearmsPurchaseProhibitionQueryServiceEndpoint");
			route.mockEndpoints();
		});
    	//Advise the firearm search results endpoint and replace it with a mock endpoint.
    	//We then will test this mock endpoint to see if it gets the proper payload.

    	context.start();
	}
    
    @Test
    public void testFirearmSearch() throws Exception {
        	
    	//We should get one message
    	firearmsPurchaseProhibitionQueryServiceMock.expectedMessageCount(1);

    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
				
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = OJBCXMLUtils.createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", "https://localhost:18311/OJB/WebApp/FirearmsPurchaseProhibitionQueryResultsHandlerService"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		org.apache.cxf.message.Message message = new MessageImpl();

		//Add SAML token to request call
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(new SamlAssertionWrapper(samlToken));
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE);
	    
	    //Read the firearm prohibition query request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/FirearmPurchaseProhibitionQueryRequest.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:firearmsPurchaseProhibitionQueryServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		//Sleep while a response is generated
		Thread.sleep(3000);
		
		//Assert that the mock endpoint expectations are satisfied
		firearmsPurchaseProhibitionQueryServiceMock.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = firearmsPurchaseProhibitionQueryServiceMock.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("SubmitFirearmPurchaseProhibitionQueryRequest", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://ojbc.org/Services/WSDL/Firearm_Purchase_Prohibition_Query_Request_Service/1.0", opNamespace);
		
		//Get the actual response
		Document actualResponseDocument = (Document)ex.getIn().getBody();
		XmlUtils.printNode(actualResponseDocument);

		String actualResponse = OJBUtils.getStringFromDocument(actualResponseDocument);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xmlInstances/FirearmPurchaseProhibitionQueryAdapterRequest.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
			
		log.info("Expected Response: " + expectedResponseAsString);
		log.info("Actual Response: " + actualResponse);
	
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(expectedResponseAsString, actualResponse);
		assertTrue(myDiff.identical());
    	
    }

	private SoapHeader makeSoapHeader(Document doc, String namespace, String localName, String value) {
		Element messageId = doc.createElementNS(namespace, localName);
		messageId.setTextContent(value);
		SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
		return soapHeader;
	}
}
