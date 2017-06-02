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
package org.ojbc.adapters.rapbackdatastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.Exchange;
import org.apache.camel.Produce;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultExchange;
import org.apache.camel.model.ModelCamelContext;
import org.apache.camel.test.spring.CamelSpringJUnit4ClassRunner;
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
import org.junit.runner.RunWith;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.saml.SamlAttribute;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

@RunWith(CamelSpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/camel-context.xml",
        "classpath:META-INF/spring/spring-context.xml",
        "classpath:META-INF/spring/cxf-endpoints.xml",      
        "classpath:META-INF/spring/properties-context.xml",
        "classpath:META-INF/spring/dao.xml",
        "classpath:META-INF/spring/h2-mock-database-application-context.xml",
        "classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml"
      })
@DirtiesContext
public class TestCriminalHistoryConsolidationService {
    private static final String COUNT_SID_A123457 = "select count(*) as rowcount from identification_subject "
    		+ "where civil_sid = 'A123457' or criminal_Sid = 'A123457'";

	private static final String COUNT_SID_A123458 = "select count(*) as rowcount from identification_subject "
    		+ "where civil_sid = 'A123458' or criminal_Sid = 'A123458'";
	
	private static final Log log = LogFactory.getLog( TestCriminalHistoryConsolidationService.class );

	private static final Object CXF_OPERATION_NAME_NOTIFICATION = "ReportCriminalHistoryConsolidation";
	private static final Object CXF_OPERATION_NAMESPACE_NOTIFICATION = "http://ojbc.org/Services/WSDL/CriminalHistoryConsolidationReportingService/1.0";
    @Resource  
    private DataSource dataSource;  
	
    @Resource
    private ModelCamelContext context;
    
    @Produce
    protected ProducerTemplate template;
    
	@Before
	public void setUp() throws Exception {
		
    	//We replace the 'from' web service endpoint with a direct endpoint we call in our test
    	context.getRouteDefinition("criminalHistoryConsolidationRequestRoute").adviceWith(context, new AdviceWithRouteBuilder() {
    	    @Override
    	    public void configure() throws Exception {
    	    	// The line below allows us to bypass CXF and send a message directly into the route
    	    	replaceFromWith("direct:criminalHistoryConsolidationRequest");
    	    }              
    	});

    	context.start();
	}	
	
	@Test
	public void contextStartup() {
		assertTrue(true);
	}

	@Test
	public void testCriminalConsolidationService() throws Exception
	{
		Connection conn = dataSource.getConnection();
		ResultSet rs = conn.createStatement().executeQuery(COUNT_SID_A123458);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(COUNT_SID_A123457);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		
		String countSubjectUcn9222201 = "select count(*) as rowcount from identification_subject where ucn = '9222201'";
		rs = conn.createStatement().executeQuery(countSubjectUcn9222201);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		
		String countSubjectUcn9222202 = "select count(*) as rowcount from identification_subject where ucn = '9222202'";
		rs = conn.createStatement().executeQuery(countSubjectUcn9222202);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));

		String countFbiSubscriptionUcn9222201 = "select count(*) as rowcount from fbi_rap_back_subscription where ucn = '9222201'";
		rs = conn.createStatement().executeQuery(countFbiSubscriptionUcn9222201);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));
		String countFbiSubscriptionUcn9222202 = "select count(*) as rowcount from fbi_rap_back_subscription where ucn = '9222202'";
		rs = conn.createStatement().executeQuery(countFbiSubscriptionUcn9222202);
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		

    	//Create a new exchange
    	Exchange senderExchange = createSenderExchangeNotification();
    	
	    //Read the notification with attachment request file from the file system
	    File inputFile = new File("src/test/resources/xmlInstances/criminalHistoryConsolidation/criminal_history_consolidation_report.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
	    
	    assertNotNull(inputStr);
	    
	    log.debug(inputStr);
	    
	    //Set it as the message message body
	    senderExchange.getIn().setBody(inputStr);

	    //Send the one-way exchange.  Using template.send will send an one way message
		Exchange returnExchange = template.send("direct:criminalHistoryConsolidationRequest", senderExchange);
		
		//Use getException to see if we received an exception
		if (returnExchange.getException() != null)
		{	
			throw new Exception(returnExchange.getException());
		}	
		
		rs = conn.createStatement().executeQuery(COUNT_SID_A123457);
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(COUNT_SID_A123458);
		assertTrue(rs.next());
		assertEquals(2,rs.getInt("rowcount"));

		rs = conn.createStatement().executeQuery(countSubjectUcn9222201);
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(countSubjectUcn9222202);
		assertTrue(rs.next());
		assertEquals(2,rs.getInt("rowcount"));

		rs = conn.createStatement().executeQuery(countFbiSubscriptionUcn9222201);
		assertTrue(rs.next());
		assertEquals(0,rs.getInt("rowcount"));
		rs = conn.createStatement().executeQuery(countFbiSubscriptionUcn9222202);
		assertTrue(rs.next());
		assertEquals(1,rs.getInt("rowcount"));

	}
	
	private Exchange createSenderExchangeNotification() throws Exception {
		Exchange senderExchange = new DefaultExchange(context);
				
    	//Set the WS-Address Message ID
		Map<String, Object> requestContext = OJBUtils.setWSAddressingMessageID("123456789");
		
		//Set the operation name and operation namespace for the CXF exchange
		senderExchange.getIn().setHeader(Client.REQUEST_CONTEXT , requestContext);
		
		Document doc = createDocument();
		List<SoapHeader> soapHeaders = new ArrayList<SoapHeader>();
		soapHeaders.add(makeSoapHeader(doc, "http://www.w3.org/2005/08/addressing", "MessageID", "12345"));
		senderExchange.getIn().setHeader(Header.HEADER_LIST , soapHeaders);
		
		org.apache.cxf.message.Message message = new MessageImpl();
		
		//Add SAML token to request call
		SAMLTokenPrincipal principal = createSAMLToken();
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
 	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAME, CXF_OPERATION_NAME_NOTIFICATION);
	    senderExchange.getIn().setHeader(CxfConstants.OPERATION_NAMESPACE, CXF_OPERATION_NAMESPACE_NOTIFICATION);
	    senderExchange.getIn().setHeader("notificationTopic", "arrest");
		return senderExchange;
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

}
