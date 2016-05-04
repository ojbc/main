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
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.io.FileUtils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ws.addressing.AddressingProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

@UseAdviceWith
// NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:META-INF/spring/camel-context.xml", 
        "classpath:META-INF/spring/cxf-endpoints.xml",
        "classpath:META-INF/spring/extensible-beans.xml",       
        "classpath:META-INF/spring/local-osgi-context.xml",
        "classpath:META-INF/spring/properties-context.xml"}) 
@DirtiesContext
public class TestAccessControlRequestService {

    @Autowired
    private ModelCamelContext context;

    @Produce
    protected ProducerTemplate template;

    @EndpointInject(uri = "mock:adapterRequest")
    protected MockEndpoint adapterRequestEndpoint;

    @EndpointInject(uri = "mock:adapterResponse")
    protected MockEndpoint adapterResponseEndpoint;

    @Test
    public void testApplicationStartup() {
        assertTrue(true);
    }

    @Before
    public void setUp() throws Exception {
        // Advise the Access Control Request Service endpoint and replace it
        // with a mock endpoint.We then will test this mock endpoint to see if 
        // it gets the proper payload.
        context.getRouteDefinition("accessControlRequestRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:accessControlRequest");

                        interceptSendToEndpoint(
                                "accessControlRequestServicePolicyAcknowledgementEndpoint")
                                .to("mock:adapterRequest")
                                .log("Called Access Control Adapter Endpoint");
                    }
                });

        // Advise the Access Control Response Service endpoint and replace it
        // with a mock endpoint.We then will test this mock endpoint to see if 
        // it gets the proper payload.
        context.getRouteDefinition("processFederatedResponseRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {

                        interceptSendToEndpoint(
                                "accessControlResponseServicePolicyAcknowledgementEndpoint")
                                .to("mock:adapterResponse")
                                .log("Called Access Control Response Adapter Endpoint")
                                .stop();
                    }
                });

        
        context.start();
    }

    @Test
    @DirtiesContext
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

        String expectedResponseBody = readSingleLineResponseWithoutLicense(new File("src/test/resources/xml/accessControl/AccessControlResponseError.xml"));
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
    private String readSingleLineResponseWithoutLicense(File file) throws IOException {
        @SuppressWarnings("unchecked")
        List<String> stringList = FileUtils.readLines(file);
        String expectedResponseBody=stringList.get(18);
        return expectedResponseBody;
    }

    @Test
    @DirtiesContext
    public void testAccessControlHandledDirectlyByIntermediaryIdentityBased() throws Exception {
        // Read the access control request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/accessControl/IdentityBasedAccessControlRequestSubscriptions.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

        String expectedResponseBody = readSingleLineResponseWithoutLicense(new File(
        "src/test/resources/xml/accessControl/staticPermitIdentityBasedResponse.xml"));

		adapterResponseEndpoint.expectedBodiesReceived(expectedResponseBody);
		adapterResponseEndpoint.expectedMessageCount(1);
        
        Map<String, Object> headers = TestUtils.createHeaders();

        //Add operation name and namespace for service operation to invoke
        headers.put("operationName", "SubmitIdentityBasedAccessControlRequest");
        headers.put("operationNamespace", "http://ojbc.org/Services/WSDL/AccessControlRequestService/1.0");

        template.sendBodyAndHeaders("direct:accessControlRequest", requestBody, headers);

        Thread.sleep(2000);

        adapterResponseEndpoint.expectedMessageCount(1);
        adapterResponseEndpoint.assertIsSatisfied();
    }

    @Test
    @DirtiesContext
    public void testAccessControlHandledDirectlyByIntermediaryMessageBased() throws Exception {
    	
    	adapterResponseEndpoint.reset();
    	
        // Read the access control request file from the file system
        File inputFile = new File(
                "src/test/resources/xml/accessControl/MessageBasedAccessControlRequest.xml");
        String requestBody = FileUtils.readFileToString(inputFile);

        String expectedResponseBody = readSingleLineResponseWithoutLicense(new File(
        "src/test/resources/xml/accessControl/staticPermitResponse.xml"));

		adapterResponseEndpoint.expectedBodiesReceivedInAnyOrder(expectedResponseBody);
		adapterResponseEndpoint.expectedMessageCount(1);
        
        Map<String, Object> headers = TestUtils.createHeaders();

        //Add operation name and namespace for service operation to invoke
        headers.put("operationName", "SubmitMessageBasedAccessControlRequest");
        headers.put("operationNamespace", "http://ojbc.org/Services/WSDL/AccessControlRequestService/1.0");

        template.sendBodyAndHeaders("direct:accessControlRequest", requestBody, headers);

        Thread.sleep(2000);

        adapterResponseEndpoint.expectedMessageCount(1);
        adapterResponseEndpoint.assertIsSatisfied();
    }

    
}
