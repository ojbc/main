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
package org.ojbc.bundles.shared;

import static org.apache.cxf.ws.addressing.JAXWSAConstants.CLIENT_ADDRESSING_PROPERTIES;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.ojbc.policydecision.application.OjbPolicyDecisionPointServiceApplication;
import org.ojbc.test.util.XmlTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;

@CamelSpringBootTest
@SpringBootTest(classes=OjbPolicyDecisionPointServiceApplication.class)
@ActiveProfiles("dev")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD) 
public class TestAccessControlRequestService {
    private static Log log = LogFactory.getLog(TestAccessControlRequestService.class);

    @Autowired
    private ModelCamelContext context;

    @Produce
    protected ProducerTemplate template;

    @EndpointInject(value = "mock:adapterRequest")
    protected MockEndpoint adapterRequestEndpoint;

    @EndpointInject(value = "mock:adapterResponse")
    protected MockEndpoint adapterResponseEndpoint;

    @Test
    public void testApplicationStartup() {
        assertTrue(true);
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Advise the Access Control Request Service endpoint and replace it
        // with a mock endpoint.We then will test this mock endpoint to see if 
        // it gets the proper payload.
    	AdviceWith.adviceWith(context, "accessControlRequestRoute", route -> {
    		route.replaceFromWith("direct:accessControlRequest");
    		route.interceptSendToEndpoint(
                    "accessControlRequestServicePolicyAcknowledgementEndpoint")
                    .to("mock:adapterRequest")
                    .log("Called Access Control Adapter Endpoint");
    	});
    	
    	 // Advise the Access Control Response Service endpoint and replace it
        // with a mock endpoint.We then will test this mock endpoint to see if 
        // it gets the proper payload.
    	AdviceWith.adviceWith(context, "processFederatedResponseRoute", route -> {

            route.interceptSendToEndpoint(
                    "accessControlResponseServicePolicyAcknowledgementEndpoint")
                    .to("mock:adapterResponse")
                    .log("Called Access Control Response Adapter Endpoint")
                    .stop();
    	});

        context.start();
    }

    @Test
    @DirtiesContext
    @Disabled
    public void testFederatedRequestToAdapter() throws Exception {
        // Read the access control request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/accessControl/IdentityBasedAccessControlRequest.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

        String expectedBody = FileUtils
                .readFileToString(new File(
                        "src/test/resources/xml/accessControl/IdentityBasedAccessControlRequest.xml"));
        int index = expectedBody.indexOf("<acr:IdentityBasedAccessControlRequest"); 
        adapterRequestEndpoint.expectedBodiesReceivedInAnyOrder(expectedBody.substring(index));

        Map<String, Object> headers = TestUtils.createHeaders();

        //Add operation name and namespace for service operation to invoke
        headers.put("operationName", "SubmitIdentityBasedAccessControlRequest");
        headers.put("operationNamespace", "http://ojbc.org/Services/WSDL/AccessControlRequestService/1.0");
                
        template.sendBodyAndHeaders("direct:accessControlRequest", requestBody, headers);

        adapterRequestEndpoint.assertIsSatisfied();
        
        Exchange exchange = adapterRequestEndpoint.getExchanges().get(0);
        String opName = (String)exchange.getIn().getHeader("operationName");
        assertEquals("SubmitIdentityBasedAccessControlRequest", opName);
        String opNamespace = (String)exchange.getIn().getHeader("operationNamespace");
        assertEquals("http://ojbc.org/Services/WSDL/AccessControlRequestService/1.0", opNamespace);
        
        @SuppressWarnings("unchecked")
        Map<String, Object> requestContext = (Map<String, Object>)exchange.getIn().getHeader(Client.REQUEST_CONTEXT);
        AddressingProperties maps = (AddressingProperties) requestContext.get(CLIENT_ADDRESSING_PROPERTIES);
        String messageId = maps.getMessageID().getValue(); 
        
        assertEquals("123456789", messageId);

        String expectedResponseBody = readResponseWithoutLicense(new File("src/test/resources/xml/accessControl/AccessControlResponseError.xml"));
        adapterResponseEndpoint.expectedBodiesReceivedInAnyOrder(expectedResponseBody);

        Thread.sleep(7000);

        adapterResponseEndpoint.expectedMessageCount(1);
        adapterResponseEndpoint.assertIsSatisfied();
    }

    /**
     * Works if the response file is single line plus the license text. 
     * @param file
     * @return
     * @throws IOException
     */
    private String readResponseWithoutLicense(File file) throws IOException {
        @SuppressWarnings("unchecked")
        List<String> stringList = FileUtils.readLines(file);
        
        if (stringList.size() > 19){
        	return StringUtils.join(stringList.subList(18, stringList.size()), "\n");
        }
        else { 
        	return stringList.get(18);
        }
    }

    @Test
    @DirtiesContext
    @Disabled
    public void testAccessControlHandledDirectlyByIntermediaryIdentityBased() throws Exception {
        // Read the access control request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/accessControl/IdentityBasedAccessControlRequestSubscriptions.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

		adapterResponseEndpoint.expectedMessageCount(1);
        
        Map<String, Object> headers = TestUtils.createHeaders();

        //Add operation name and namespace for service operation to invoke
        headers.put("operationName", "SubmitIdentityBasedAccessControlRequest");
        headers.put("operationNamespace", "http://ojbc.org/Services/WSDL/AccessControlRequestService/1.0");

        template.sendBodyAndHeaders("direct:accessControlRequest", requestBody, headers);

        Thread.sleep(2000);

        adapterResponseEndpoint.expectedMessageCount(1);
        adapterResponseEndpoint.assertIsSatisfied();
        String receivedReponse = (String) adapterResponseEndpoint.getReceivedExchanges().get(0).getIn().getBody();
        
        XmlTestUtils.compareDocs("src/test/resources/xml/accessControl/staticPermitIdentityBasedResponse.xml", receivedReponse);
        log.info("Received Response: \n " + receivedReponse);
    }

    @Test
    @DirtiesContext
    @Disabled
    public void testAccessControlHandledDirectlyByIntermediaryMessageBased() throws Exception {
    	
    	adapterResponseEndpoint.reset();
    	
        // Read the access control request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/accessControl/MessageBasedAccessControlRequest.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

		adapterResponseEndpoint.expectedMessageCount(1);
        
        Map<String, Object> headers = TestUtils.createHeaders();

        //Add operation name and namespace for service operation to invoke
        headers.put("operationName", "SubmitMessageBasedAccessControlRequest");
        headers.put("operationNamespace", "http://ojbc.org/Services/WSDL/AccessControlRequestService/1.0");

        template.sendBodyAndHeaders("direct:accessControlRequest", requestBody, headers);

        Thread.sleep(2000);

        adapterResponseEndpoint.expectedMessageCount(1);
        adapterResponseEndpoint.assertIsSatisfied();
        String receivedReponse = (String) adapterResponseEndpoint.getReceivedExchanges().get(0).getIn().getBody();
        XmlTestUtils.compareDocs("src/test/resources/xml/accessControl/staticPermitResponse.xml", receivedReponse);
    }

    
}

