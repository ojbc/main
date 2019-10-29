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
package org.ojbc.audit.enhanced.processor;

import static org.junit.Assert.assertEquals;

import java.io.File;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.MessageImpl;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.SubscriptionSearchResult;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.sn.SubscriptionSearchRequest;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.xml.signature.SignatureConstants;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class TestSubscriptionSearchProcessor {

	private static final Log log = LogFactory.getLog(TestSubscriptionSearchProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessSubscriptionSearchRequest() throws Exception
	{
		SubscriptionSearchRequestSQLProcessor subscriptionSearchRequestSQLProcessor = new SubscriptionSearchRequestSQLProcessor();
		UserInfoSQLProcessor userInfoSQLProcessor = new UserInfoSQLProcessor();
		
		userInfoSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		subscriptionSearchRequestSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
		subscriptionSearchRequestSQLProcessor.setUserInfoSQLProcessor(userInfoSQLProcessor);
		
        File inputFile = new File("src/test/resources/xmlInstances/SubscriptionSearchRequest.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		SubscriptionSearchRequest subscriptionSearchRequest = subscriptionSearchRequestSQLProcessor.processSubscriptionSearchRequest(document);
		
		log.info(subscriptionSearchRequest.toString());
		
		assertEquals("first", subscriptionSearchRequest.getSubjectFirstName());
		assertEquals("last", subscriptionSearchRequest.getSubjectLastName());
		assertEquals("UCN", subscriptionSearchRequest.getUcn());
		assertEquals("sid", subscriptionSearchRequest.getSid());
		assertEquals("CS", subscriptionSearchRequest.getSubscriptionCategories().get(0));
		assertEquals(Boolean.FALSE, subscriptionSearchRequest.getAdminSearch());
		assertEquals(Boolean.TRUE, subscriptionSearchRequest.getActive());
		assertEquals("{http://ojbc.org/OJB_Portal/Subscriptions/1.0}OJB", subscriptionSearchRequest.getSubscribingSystemIdentifier());
		

		SubscriptionSearchResponseSQLProcessor subscriptionSearchResponseSQLProcessor = new SubscriptionSearchResponseSQLProcessor();
		
		inputFile = new File("src/test/resources/xmlInstances/SubscriptionSearchResults.xml");
        document = db.parse(inputFile);
        SubscriptionSearchResult subscriptionSearchResult = subscriptionSearchResponseSQLProcessor.processSubscriptionSearchResponse(document);
        
        assertEquals(new Integer(6), subscriptionSearchResult.getSearchResultsCount());
        assertEquals("The search displayed 5 of 6 total results.", subscriptionSearchResult.getSearchResultsErrorText());
        assertEquals(true, subscriptionSearchResult.getSearchResultsErrorIndicator());
        
        inputFile = new File("src/test/resources/xmlInstances/AccessDenial_SubscriptionSearchResults.xml");
        document = db.parse(inputFile);
        subscriptionSearchResult = subscriptionSearchResponseSQLProcessor.processSubscriptionSearchResponse(document);
        
        assertEquals(true, subscriptionSearchResult.getSearchResultsAccessDeniedIndicator());
        assertEquals("improper privileges", subscriptionSearchResult.getSearchResultsErrorText());
        
        inputFile = new File("src/test/resources/xmlInstances/Error_SubscriptionSearchResults.xml");
        document = db.parse(inputFile);
        subscriptionSearchResult = subscriptionSearchResponseSQLProcessor.processSubscriptionSearchResponse(document);
        
        assertEquals("Error Description", subscriptionSearchResult.getSearchResultsErrorText());
        assertEquals(true, subscriptionSearchResult.getSearchResultsErrorIndicator());
		
		CamelContext ctx = new DefaultCamelContext(); 
		    
    	//Create a new exchange
    	Exchange senderExchange = new DefaultExchange(ctx);
		
		org.apache.cxf.message.Message message = new MessageImpl();

		//Add SAML token to request call
		Assertion samlToken = SAMLTokenUtils.createStaticAssertionWithCustomAttributes("https://idp.ojbc-local.org:9443/idp/shibboleth", SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS, SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1, true, true, null);
		SAMLTokenPrincipal principal = new SAMLTokenPrincipalImpl(new SamlAssertionWrapper(samlToken));
		message.put("wss4j.principal.result", principal);
		
		senderExchange.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
		senderExchange.getIn().setHeader("federatedQueryRequestGUID", "123456");
		
		subscriptionSearchRequestSQLProcessor.auditSubscriptionSearchRequest(senderExchange, document);
		
		subscriptionSearchResponseSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        inputFile = new File("src/test/resources/xmlInstances/SubscriptionSearchResults.xml");

        document = db.parse(inputFile);
		
        subscriptionSearchResponseSQLProcessor.auditSubscriptionSearchResponse(document, "123456");

        inputFile = new File("src/test/resources/xmlInstances/AccessDenial_SubscriptionSearchResults.xml");

        document = db.parse(inputFile);
		
        subscriptionSearchResponseSQLProcessor.auditSubscriptionSearchResponse(document, "123456");
        
        inputFile = new File("src/test/resources/xmlInstances/Error_SubscriptionSearchResults.xml");

        document = db.parse(inputFile);
		
        subscriptionSearchResponseSQLProcessor.auditSubscriptionSearchResponse(document, "123456");

	}
	
}
