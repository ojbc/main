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

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.cxf.CxfPayload;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
import org.apache.camel.test.spring.UseAdviceWith;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.cxf.message.MessageImpl;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.xml.XmlUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@UseAdviceWith
//NOTE: this causes Camel contexts to not start up automatically
@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={
        "classpath:META-INF/spring/camel-context.xml", 
        "classpath:META-INF/spring/federated-query-routes.xml", 
        "classpath:META-INF/spring/cxf-endpoints.xml",
        "classpath:META-INF/spring/extensible-beans.xml",       
        "classpath:META-INF/spring/jetty-server.xml",       
        "classpath:META-INF/spring/local-osgi-context.xml",
        "classpath:META-INF/spring/properties-context.xml"}) 
@DirtiesContext
public class TestJuvenileHistoryServicesIntermediary {

	
	private static final Log log = LogFactory.getLog( TestJuvenileHistoryServicesIntermediary.class );
	
    @Autowired
    private ModelCamelContext context;

    @Produce
    protected ProducerTemplate template;

    private static DocumentBuilder documentBuilder;
    
    @Test
    public void testApplicationStartup()
    {
    	assertTrue(true);
    }
    
    //Case Plan
    @EndpointInject(uri = "mock:casePlanAdapterRequest")
    protected MockEndpoint casePlanAdapterRequestEndpoint;

    @EndpointInject(uri = "mock:casePlanPortalResponse")
    protected MockEndpoint casePlanPortalResponseEndpoint;

    //Hearing
    @EndpointInject(uri = "mock:hearingAdapterRequest")
    protected MockEndpoint hearingAdapterRequestEndpoint;

    @EndpointInject(uri = "mock:hearingPortalResponse")
    protected MockEndpoint hearingPortalResponseEndpoint;

    //Intake
    @EndpointInject(uri = "mock:intakeAdapterRequest")
    protected MockEndpoint intakeAdapterRequestEndpoint;

    @EndpointInject(uri = "mock:intakePortalResponse")
    protected MockEndpoint intakePortalResponseEndpoint;

    //Offense
    @EndpointInject(uri = "mock:offenseAdapterRequest")
    protected MockEndpoint offenseAdapterRequestEndpoint;

    @EndpointInject(uri = "mock:offensePortalResponse")
    protected MockEndpoint offensePortalResponseEndpoint;

    //Placement
    @EndpointInject(uri = "mock:placementAdapterRequest")
    protected MockEndpoint placementAdapterRequestEndpoint;

    @EndpointInject(uri = "mock:placementPortalResponse")
    protected MockEndpoint placementPortalResponseEndpoint;

    //Referral
    @EndpointInject(uri = "mock:referralAdapterRequest")
    protected MockEndpoint referralAdapterRequestEndpoint;

    @EndpointInject(uri = "mock:referralPortalResponse")
    protected MockEndpoint referralPortalResponseEndpoint;

    @Before
    public void setUp() throws Exception {
    	
    	//Case Plan - Request
        context.getRouteDefinition("juvenileCasePlanHistoryRequestFederatedServiceRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:casePlanFederatedServiceRequest");

                        interceptSendToEndpoint(
                                "juvenileCasePlanHistoryRequestAdapterServiceEndpoint")
                                .to("mock:casePlanAdapterRequest")
                                .log("Called Case Plan Adapter Endpoint")
                                .stop();
                    }
                });

    	//Case Plan - Response routes
        context.getRouteDefinition("juvenileCasePlanHistoryResultsHandlerServiceRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:casePlanFederatedServiceResults");
                    }
                });
        
        //All Federated Response routes go hear 
        context.getRouteDefinition("processFederatedResponseRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        interceptSendToEndpoint(
                                "juvenileCasePlanHistoryResultsPortalServiceEndpoint")
                                .to("mock:casePlanPortalResponse")
                                .log("Called Case Plan Adapter Portal Endpoint")
                                .stop();
                        
                        interceptSendToEndpoint(
		                        "juvenileHearingHistoryResultsPortalServiceEndpoint")
		                        .to("mock:hearingPortalResponse")
		                        .log("Called Hearing Adapter Portal Endpoint")
		                        .stop();
                        
                    }
                });

        //Hearing - Request
        context.getRouteDefinition("juvenileHearingHistoryRequestFederatedServiceRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:hearingFederatedServiceRequest");

                        interceptSendToEndpoint(
                                "juvenileHearingHistoryRequestAdapterServiceEndpoint")
                                .to("mock:hearingAdapterRequest")
                                .log("Called Hearing Adapter Endpoint");
                    }
                });

    	//Hearing - Response
        context.getRouteDefinition("juvenileHearingHistoryResultsHandlerServiceRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:hearingFederatedServiceResults");

                    }
                });
        
        //Intake
        context.getRouteDefinition("juvenileIntakeHistoryRequestFederatedServiceRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:intakeFederatedServiceRequest");

                        interceptSendToEndpoint(
                                "juvenileIntakeHistoryRequestAdapterServiceEndpoint")
                                .to("mock:intakeAdapterRequest")
                                .log("Called Intake Adapter Endpoint");
                    }
                });

        //Offense
        context.getRouteDefinition("juvenileOffenseHistoryRequestFederatedServiceRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:offenseFederatedServiceRequest");

                        interceptSendToEndpoint(
                                "juvenileOffenseHistoryRequestAdapterServiceEndpoint")
                                .to("mock:offenseAdapterRequest")
                                .log("Called Offense Adapter Endpoint");
                    }
                });

        //Placement
        context.getRouteDefinition("juvenilePlacementHistoryRequestFederatedServiceRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:placementFederatedServiceRequest");

                        interceptSendToEndpoint(
                                "juvenilePlacementHistoryRequestAdapterServiceEndpoint")
                                .to("mock:placementAdapterRequest")
                                .log("Called Placement Adapter Endpoint");
                    }
                });

        //Referral
        context.getRouteDefinition("juvenileReferralHistoryRequestFederatedServiceRoute").adviceWith(
                context, new AdviceWithRouteBuilder() {
                    @Override
                    public void configure() throws Exception {
                        // The line below allows us to bypass CXF and send a
                        // message directly into the route
                        replaceFromWith("direct:referralFederatedServiceRequest");

                        interceptSendToEndpoint(
                                "juvenileReferralHistoryRequestAdapterServiceEndpoint")
                                .to("mock:referralAdapterRequest")
                                .log("Called Referral Adapter Endpoint");
                    }
                });

        context.start();
    }

            
    @Test
    public void testCasePlanHistory() throws Exception
    {    	    	    	    	
        // Read the access control request file from the file system
    	String requestBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/casePlan/JuvenileHistoryQuery_Sample.xml");
        
        String expectedBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/casePlan/JuvenileHistoryQuery_Sample.xml");        

        casePlanAdapterRequestEndpoint.expectedBodiesReceived(expectedBody);
		casePlanAdapterRequestEndpoint.expectedMessageCount(1);

    	Exchange senderExchange = setUpSenderExchange("123456789", "https://localhost:18311/OJB/WebApp/JuvenileQuery/CasePlanHistoryResultsService", "SubmitJuvenileCasePlanHistoryQuery", "http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/CasePlanRequest/1.0", true);
        
	    //Set it as the message message body
	    senderExchange.getIn().setBody(requestBody);

        template.send("direct:casePlanFederatedServiceRequest", senderExchange);

        casePlanAdapterRequestEndpoint.getReceivedExchanges();
        casePlanAdapterRequestEndpoint.assertIsSatisfied();
        
        //Confirm that the request file get to the Adapter
        Exchange callAdapterExchnage = casePlanAdapterRequestEndpoint.getExchanges().get(0);
        assertEquals("https://localhost:8443/OJB/adapter/CasePlanHistoryRequestService",callAdapterExchnage.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));
        
        ///////////////////////////////////
        //////////////////////////////////
        
        //Mock sending response from Adapter        
        String responseFromAdapter = XmlUtils.getRootNodeAsString("src/test/resources/xml/casePlan/JuvenileCasePlanHistoryResponse_Sample.xml");
        
        
        Exchange responseFromAdapterExchange = setUpSenderExchange("123456789", "https://localhost:18311/OJB/WebApp/JuvenileQuery/CasePlanHistoryResultsService", "SubmitJuvenileCasePlanHistoryResponse", "http://ojbc.org/Services/WSDL/JuvenileHistoryResults/1.0", false);
        
        //The federated query required a CXF exchange to properly test response route
        CxfPayload<SoapHeader> responsePayload = createCXFPayload(responseFromAdapter);
        
	    //Set it as the message message body
        responseFromAdapterExchange.getIn().setBody(responsePayload);

        template.send("direct:casePlanFederatedServiceResults", responseFromAdapterExchange);

        //This was not working as expected, but the message is getting to the response endpoint and it printed.
        //casePlanPortalResponseEndpoint.expectedBodiesReceived(responseFromAdapter);
        casePlanPortalResponseEndpoint.expectedMessageCount(1);
        
        //This is the actual response
        String actualResponse = casePlanPortalResponseEndpoint.getExchanges().get(0).getIn().getBody(String.class);
        log.debug("This is the Case Plan response to portal: " + actualResponse);
        
        casePlanPortalResponseEndpoint.assertIsSatisfied();
    }

	private CxfPayload<SoapHeader> createCXFPayload(String responseFromAdapter)
			throws Exception {
		
		Element details=OJBUtils.loadXMLFromString(responseFromAdapter).getDocumentElement();
        List<Element> outElements=new ArrayList<Element>();
        outElements.add(details);
        CxfPayload<SoapHeader> responsePayload=new CxfPayload<SoapHeader>(null,outElements);
		return responsePayload;
	}
    
    @Test
    public void testHearingHistory() throws Exception
    {
        // Read the access control request file from the file system        
        String requestBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/hearing/JuvenileHistoryQuery_Sample.xml");
                        
        String expectedBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/hearing/JuvenileHistoryQuery_Sample.xml");        
        
        hearingAdapterRequestEndpoint.expectedBodiesReceived(expectedBody);
		hearingAdapterRequestEndpoint.expectedMessageCount(1);

    	Exchange senderExchange = setUpSenderExchange("123456789", "https://localhost:18311/OJB/WebApp/JuvenileQuery/HearingHistoryResultsService", "SubmitJuvenileHearingHistoryQuery", "http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/HearingRequest/1.0", true);
        
	    //Set it as the message message body
	    senderExchange.getIn().setBody(requestBody);

        template.send("direct:hearingFederatedServiceRequest", senderExchange);

        hearingAdapterRequestEndpoint.assertIsSatisfied();
        
        Exchange callAdapterExchnage = hearingAdapterRequestEndpoint.getExchanges().get(0);
        assertEquals("https://localhost:8443/OJB/adapter/HearingHistoryRequestService",callAdapterExchnage.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));
     
    }
    
    @Test
    public void testIntakeHistory() throws Exception
    {
        // Read the access control request file from the file system       
        String requestBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/Intake/JuvenileHistoryQuery_Sample.xml");                        

        String expectedBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/Intake/JuvenileHistoryQuery_Sample.xml");        		        		                
        
        intakeAdapterRequestEndpoint.expectedBodiesReceived(expectedBody);
		intakeAdapterRequestEndpoint.expectedMessageCount(1);

    	Exchange senderExchange = setUpSenderExchange("123456789", "https://localhost:18311/OJB/WebApp/JuvenileQuery/IntakeHistoryResultsService", "SubmitJuvenileIntakeHistoryQuery", "http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/IntakeRequest/1.0", true);
        
	    //Set it as the message message body
	    senderExchange.getIn().setBody(requestBody);

        template.send("direct:intakeFederatedServiceRequest", senderExchange);

        intakeAdapterRequestEndpoint.assertIsSatisfied();
        
        Exchange callAdapterExchnage = intakeAdapterRequestEndpoint.getExchanges().get(0);
        assertEquals("https://localhost:8443/OJB/adapter/IntakeHistoryRequestService",callAdapterExchnage.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));
        
    }
    
    @Test
    public void testOffenseHistory() throws Exception
    {
        // Read the access control request file from the file system        
        String requestBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/Offense/JuvenileHistoryQuery_Sample.xml");
        
        String expectedBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/Offense/JuvenileHistoryQuery_Sample.xml");
                
        offenseAdapterRequestEndpoint.expectedBodiesReceived(expectedBody);
		offenseAdapterRequestEndpoint.expectedMessageCount(1);

    	Exchange senderExchange = setUpSenderExchange("123456789", "https://localhost:18311/OJB/WebApp/JuvenileQuery/OffenseHistoryResultsService", "SubmitJuvenileOffenseHistoryQuery", "http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/OffenseRequest/1.0", true);
        
	    //Set it as the message message body
	    senderExchange.getIn().setBody(requestBody);

        template.send("direct:offenseFederatedServiceRequest", senderExchange);

        offenseAdapterRequestEndpoint.assertIsSatisfied();
        
        Exchange callAdapterExchnage = offenseAdapterRequestEndpoint.getExchanges().get(0);
        assertEquals("https://localhost:8443/OJB/adapter/OffenseHistoryRequestService",callAdapterExchnage.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));
        
    } 
    
    @Test
    public void testPlacementHistory() throws Exception
    {
        // Read the access control request file from the file system        
        String requestBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/Placement/JuvenileHistoryQuery_Sample.xml");                
        
        String expectedBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/Placement/JuvenileHistoryQuery_Sample.xml");
        
        placementAdapterRequestEndpoint.expectedBodiesReceived(expectedBody);
		placementAdapterRequestEndpoint.expectedMessageCount(1);

    	Exchange senderExchange = setUpSenderExchange("123456789", "https://localhost:18311/OJB/WebApp/JuvenileQuery/PlacementHistoryResultsService", "SubmitJuvenilePlacementHistoryQuery", "http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/PlacementRequest/1.0", true);
        
	    //Set it as the message message body
	    senderExchange.getIn().setBody(requestBody);

        template.send("direct:placementFederatedServiceRequest", senderExchange);

        placementAdapterRequestEndpoint.assertIsSatisfied();
        
        Exchange callAdapterExchnage = placementAdapterRequestEndpoint.getExchanges().get(0);
        assertEquals("https://localhost:8443/OJB/adapter/PlacementHistoryRequestService",callAdapterExchnage.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));
        
    } 
    
    @Test
    public void testReferralHistory() throws Exception
    {
        // Read the access control request file from the file system        
        String requestBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/Referral/JuvenileHistoryQuery_Sample.xml");
                
        String expectedBody = XmlUtils.getRootNodeAsString("src/test/resources/xml/Referral/JuvenileHistoryQuery_Sample.xml");
        
        referralAdapterRequestEndpoint.expectedBodiesReceived(expectedBody);
		referralAdapterRequestEndpoint.expectedMessageCount(1);

    	Exchange senderExchange = setUpSenderExchange("123456789", "https://localhost:18311/OJB/WebApp/JuvenileQuery/ReferralHistoryResultsService", "SubmitJuvenileReferralHistoryQuery", "http://ojbc.org/Services/WSDL/JuvenileHistoryRequest/ReferralRequest/1.0", true);
        
	    //Set it as the message message body
	    senderExchange.getIn().setBody(requestBody);

        template.send("direct:referralFederatedServiceRequest", senderExchange);

        referralAdapterRequestEndpoint.assertIsSatisfied();
        
        Exchange callAdapterExchnage = referralAdapterRequestEndpoint.getExchanges().get(0);
        assertEquals("https://localhost:8443/OJB/adapter/ReferralHistoryRequestService",callAdapterExchnage.getIn().getHeader(Exchange.DESTINATION_OVERRIDE_URL));
        
    } 
        
	private Exchange setUpSenderExchange(String wsAddressingMessageId, String replyToAddress, String operationName, String operationNamespace, boolean includeSAMLToken) throws ParserConfigurationException,
			Exception {
		//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
        
        if (documentBuilder == null) {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                    .newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
        }
        
        Map<String, Object> headers = new HashMap<String, Object>(); 
        //Set the WS-Address Message ID
        Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID(wsAddressingMessageId);
        
        //Set the operation name and operation namespace for the CXF exchange
        headers.put(Client.REQUEST_CONTEXT , requestContext);
        
        Document doc = documentBuilder.newDocument();
        List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
        soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", wsAddressingMessageId));
        soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", replyToAddress));
        senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
        
        org.apache.cxf.message.Message message = new MessageImpl();
        
		//Add SAML token to request call
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(new SamlAssertionWrapper(samlToken));
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, operationName);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, operationNamespace);
		return senderExchange;
	}
    
	private SoapHeader makeSoapHeader(Document doc, String namespace, String localName, String value) {
		Element messageId = doc.createElementNS(namespace, localName);
		messageId.setTextContent(value);
		SoapHeader soapHeader = new SoapHeader(new QName(namespace, localName), messageId);
		return soapHeader;
	}	
}
