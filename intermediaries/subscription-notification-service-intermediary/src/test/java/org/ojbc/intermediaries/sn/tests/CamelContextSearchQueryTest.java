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
package org.ojbc.intermediaries.sn.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
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
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.jdbc.core.JdbcTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class CamelContextSearchQueryTest extends AbstractSubscriptionNotificationTest {

	public static final String CXF_OPERATION_NAME_SEARCH = "SubmitSubscriptionSearchRequest";
	public static final String CXF_OPERATION_NAMESPACE_SEARCH = "http://ojbc.org/Services/WSDL/SubscriptionSearchRequestService/1.0";

	public static final String CXF_OPERATION_NAME_QUERY = "SubmitSubscriptionQueryRequest";
	public static final String CXF_OPERATION_NAMESPACE_QUERY = "http://ojbc.org/Services/WSDL/SubscriptionQueryRequestService/1.0";

	private static final Log log = LogFactory.getLog( CamelContextSearchQueryTest.class );
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
    @EndpointInject(uri = "mock:cxf:bean:subscriptionSearchResultsHandlerService")
    protected MockEndpoint subscriptionSearchResultsMock;

    @EndpointInject(uri = "mock:direct:processSubscriptionSearch")
    protected MockEndpoint subscriptionSAMLTokenProcessorSearchMock;
    
    @EndpointInject(uri = "mock:cxf:bean:subscriptionQueryResultsHandlerService")
    protected MockEndpoint subscriptionQueryResultsMock;

    @EndpointInject(uri = "mock:direct:processSubscriptionQuery")
    protected MockEndpoint subscriptionSAMLTokenProcessorQueryMock;    
    
	private JdbcTemplate jdbcTemplate;
	
	@Resource
	private DataSource dataSource;
	
	@Before
	public void setUp() throws Exception {
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("subscriptionSearchRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:subscriptionSearchServiceEndpoint");
    	
    	    	mockEndpoints("direct:processSubscriptionSearch*");
    	    	
    	    	//We mock the results service endpoint and intercept any submissions
    	    	mockEndpointsAndSkip("cxf:bean:subscriptionSearchResultsHandlerService*");
    
    	    }              
    	});
    	
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("subscriptionQueryRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:subscriptionQueryServiceEndpoint");
    	
    	    	mockEndpoints("direct:processSubscriptionQuery*");
    	    	
    	    	//We mock the results service endpoint and intercept any submissions
    	    	mockEndpointsAndSkip("cxf:bean:subscriptionQueryResultsHandlerService*");
    
    	    }              
    	});
    	
    	this.jdbcTemplate = new JdbcTemplate(dataSource);
    	
		context.start();		
		
	}
	
	@Test
	public void contextStartup() {
		assertTrue(true);
	}
	
    @Test
    public void testSubscriptionSearch() throws Exception {
    
    	NotifyBuilder notifySearch = new NotifyBuilder(context).whenReceivedSatisfied(subscriptionSearchResultsMock).create();
    	NotifyBuilder notifySaml = new NotifyBuilder(context).whenReceivedSatisfied(subscriptionSAMLTokenProcessorSearchMock).create();
    	
    	subscriptionSearchResultsMock.reset();
    	subscriptionSAMLTokenProcessorSearchMock.reset();
    	
    	subscriptionSearchResultsMock.expectedMessageCount(1);
    	subscriptionSAMLTokenProcessorSearchMock.expectedMessageCount(1);
    	
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
				
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", "https://localhost:18311/OJB/WebApp/FirearmSearchResultsHandlerService"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		org.apache.cxf.message.Message message = new MessageImpl();

		//Add SAML token to request call
		SAMLTokenPrincipal principal = createSAMLToken();
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_SEARCH);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_SEARCH);
    	
	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/SubscriptionSearchRequest.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionSearchServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		notifySearch.matches(10, TimeUnit.SECONDS);
		notifySaml.matches(10, TimeUnit.SECONDS);
		
		//Assert that the mock endpoint expectations are satisfied
		subscriptionSAMLTokenProcessorSearchMock.assertIsSatisfied();

		//Get the first exchange (the only one and confirm the SAML token header was set)
		Exchange exSamlToken = subscriptionSAMLTokenProcessorSearchMock.getExchanges().get(0);
		
		String federationId = (String)exSamlToken.getIn().getHeader("saml_FederationID");
		log.info("Federation ID pulled from SAML token is: " + federationId);
		assertEquals("OJBC:IDP:OJBC:USER:admin", federationId);
		
		subscriptionSearchResultsMock.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = subscriptionSearchResultsMock.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("ReportSubscriptionSearchResults", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://ojbc.org/Services/WSDL/SubscriptionSearchResultsService/1.0", opNamespace);

		Document returnDocument = ex.getIn().getBody(Document.class);
		
		//XmlUtils.printNode(returnDocument);
		
        Node searchResultNode = XmlUtils.xPathNodeSearch(returnDocument, "/ssr:SubscriptionSearchResults/ssr-ext:SubscriptionSearchResult"); 
        assertNotNull(searchResultNode);

    }

	private SAMLTokenPrincipal createSAMLToken() throws Exception {
		
		Map<SamlAttribute, String> customAttributes = new HashMap<SamlAttribute, String>();
		
		customAttributes.put(SamlAttribute.EmployerSubUnitName, "OJBC:IDP:OJBC");
		customAttributes.put(SamlAttribute.FederationId, "OJBC:IDP:OJBC:USER:admin");
		customAttributes.put(SamlAttribute.IdentityProviderId, "OJBC");
		
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth",
				SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, customAttributes);
		
		SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(new SamlAssertionWrapper(samlToken));
		return principal;
	}

    @Test
    public void testSubscriptionSearchWithoutSAMLToken() throws Exception {
    
    	NotifyBuilder notify = new NotifyBuilder(context).whenReceivedSatisfied(subscriptionSearchResultsMock).create();
    	
    	subscriptionSearchResultsMock.reset();
    	subscriptionSearchResultsMock.expectedMessageCount(1);
    	
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
				
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", "https://localhost:18311/OJB/WebApp/FirearmSearchResultsHandlerService"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_SEARCH);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_SEARCH);
    	
	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/SubscriptionSearchRequest.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionSearchServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		notify.matches(10, TimeUnit.SECONDS);
		
		subscriptionSearchResultsMock.assertIsSatisfied();
		
		Exchange ex = subscriptionSearchResultsMock.getReceivedExchanges().get(0);
		Document returnDocument = ex.getIn().getBody(Document.class);
		
		//XmlUtils.printNode(returnDocument);
		
        Node accessDeniedNode = XmlUtils.xPathNodeSearch(returnDocument, "/ssr:SubscriptionSearchResults/srm:SearchResultsMetadata/iad:InformationAccessDenial"); 
        assertNotNull(accessDeniedNode);
    }    
    
    
    @Test
    public void testSubscriptionSearchNullValidationDate() throws Exception {
    
    	NotifyBuilder notifySearch = new NotifyBuilder(context).whenReceivedSatisfied(subscriptionSearchResultsMock).create();
    	NotifyBuilder notifySaml = new NotifyBuilder(context).whenReceivedSatisfied(subscriptionSAMLTokenProcessorSearchMock).create();

    	subscriptionSearchResultsMock.reset();
    	subscriptionSAMLTokenProcessorSearchMock.reset();
    	
    	subscriptionSearchResultsMock.expectedMessageCount(1);
    	subscriptionSAMLTokenProcessorSearchMock.expectedMessageCount(1);
    	
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
				
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", "https://localhost:18311/OJB/WebApp/FirearmSearchResultsHandlerService"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		org.apache.cxf.message.Message message = new MessageImpl();

		//Add SAML token to request call
		SAMLTokenPrincipal principal = createSAMLToken();
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_SEARCH);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_SEARCH);
    	
	    //Update last validation to null to force an error
        this.jdbcTemplate.update("update subscription set lastValidationDate=null where id = 62720");
	    
	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/SubscriptionSearchRequest.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionSearchServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		notifySearch.matches(10, TimeUnit.SECONDS);
		notifySaml.matches(10, TimeUnit.SECONDS);
		
		//Assert that the mock endpoint expectations are satisfied
		subscriptionSAMLTokenProcessorSearchMock.assertIsSatisfied();

		//Get the first exchange (the only one and confirm the SAML token header was set)
		Exchange exSamlToken = subscriptionSAMLTokenProcessorSearchMock.getExchanges().get(0);
		
		String federationId = (String)exSamlToken.getIn().getHeader("saml_FederationID");
		log.info("Federation ID pulled from SAML token is: " + federationId);
		assertEquals("OJBC:IDP:OJBC:USER:admin", federationId);
		
		subscriptionSearchResultsMock.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = subscriptionSearchResultsMock.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("ReportSubscriptionSearchResults", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://ojbc.org/Services/WSDL/SubscriptionSearchResultsService/1.0", opNamespace);

		Document returnDocument = ex.getIn().getBody(Document.class);
		
		//XmlUtils.printNode(returnDocument);
		
        assertEquals("Last validation date can not be null", XmlUtils.xPathStringSearch(returnDocument, "/ssr:SubscriptionSearchResults/srm:SearchResultsMetadata/srer:SearchRequestError/srer:ErrorText")); 

        //Update last validation to null to it's original state.
        this.jdbcTemplate.update("update subscription set lastValidationDate='2011-10-19' where id = 62720");
    }
    
    @Test
    public void testSubscriptionQuery() throws Exception {
    	NotifyBuilder notifySearch = new NotifyBuilder(context).whenReceivedSatisfied(subscriptionSearchResultsMock).create();
    	NotifyBuilder notifySaml = new NotifyBuilder(context).whenReceivedSatisfied(subscriptionSAMLTokenProcessorSearchMock).create();
    	
    	subscriptionQueryResultsMock.reset();
    	subscriptionSAMLTokenProcessorQueryMock.reset();
    	
    	subscriptionQueryResultsMock.expectedMessageCount(1);
    	subscriptionSAMLTokenProcessorQueryMock.expectedMessageCount(1);
    	
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(context);
				
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "ReplyTo", "https://localhost:18311/OJB/WebApp/SubscriptionQueryResultsHandlerService"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		org.apache.cxf.message.Message message = new MessageImpl();
		
		//Add SAML token to request call
		SAMLTokenPrincipal principal = createSAMLToken();
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_QUERY);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_QUERY);
    	
	    //Read the firearm search request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/Subscription_Query_Request.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);

	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);
	    
	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:subscriptionQueryServiceEndpoint", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	

		notifySearch.matches(10, TimeUnit.SECONDS);
		notifySaml.matches(10, TimeUnit.SECONDS);

		//Assert that the mock endpoint expectations are satisfied
		subscriptionSAMLTokenProcessorQueryMock.assertIsSatisfied();

		//Get the first exchange (the only one and confirm the SAML token header was set)
		Exchange exSamlToken = subscriptionSAMLTokenProcessorQueryMock.getExchanges().get(0);
		
		String federationId = (String)exSamlToken.getIn().getHeader("saml_FederationID");
		log.info("Federation ID pulled from SAML token is: " + federationId);
		assertEquals("OJBC:IDP:OJBC:USER:admin", federationId);
		
		subscriptionQueryResultsMock.assertIsSatisfied();
		
		//Get the first exchange (the only one)
		Exchange ex = subscriptionQueryResultsMock.getExchanges().get(0);
		
		String opName = (String)ex.getIn().getHeader("operationName");
		assertEquals("ReportSubscriptionQueryResults", opName);
		
		String opNamespace = (String)ex.getIn().getHeader("operationNamespace");
		assertEquals("http://ojbc.org/Services/WSDL/SubscriptionQueryResultsService/1.0", opNamespace);

		Document returnDocument = ex.getIn().getBody(Document.class);
		
		XmlUtils.printNode(returnDocument);
		
        //Node searchResultNode = XmlUtils.xPathNodeSearch(returnDocument, "/ssr:SubscriptionSearchResults/ssr-ext:SubscriptionSearchResult"); 
        //assertNotNull(searchResultNode);

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
