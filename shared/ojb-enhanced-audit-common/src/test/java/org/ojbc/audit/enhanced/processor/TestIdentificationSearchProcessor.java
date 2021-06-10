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
import org.apache.camel.support.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.message.MessageImpl;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.ojbc.audit.enhanced.dao.EnhancedAuditDAO;
import org.ojbc.audit.enhanced.dao.model.IdentificationSearchRequest;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.w3c.dom.Document;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = {
	"classpath:META-INF/spring/spring-context.xml"})
@DirtiesContext
public class TestIdentificationSearchProcessor {

	private static final Log log = LogFactory.getLog(TestIdentificationSearchProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;

	@Test
	public void testProcessIdentificationSearchRequest() throws Exception
	{
		IdentificationSearchRequestSQLProcessor identificationSearchRequestSQLProcessor = new IdentificationSearchRequestSQLProcessor();
		UserInfoSQLProcessor userInfoSQLProcessor = new UserInfoSQLProcessor();
		
		userInfoSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		identificationSearchRequestSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
		identificationSearchRequestSQLProcessor.setUserInfoSQLProcessor(userInfoSQLProcessor);
		
        File inputFile = new File("src/test/resources/xmlInstances/OrganizationIdentificationResultsSearchRequest-Civil-detailed.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		IdentificationSearchRequest identificationSearchRequest = identificationSearchRequestSQLProcessor.processIdentificationSearchRequest(document);
		
		log.info(identificationSearchRequest.toString());
		
		assertEquals("Walter", identificationSearchRequest.getFirstName());
		assertEquals("Available for Subscription", identificationSearchRequest.getIdentificationResultsStatus());
		assertEquals("White", identificationSearchRequest.getLastName());
		assertEquals("12345678", identificationSearchRequest.getOtn());
		//assertEquals("Frank", identificationSearchRequest.getReasonCode());
		assertEquals("2011-01-01", identificationSearchRequest.getReportedFromDate().toString());
		assertEquals("2016-01-01", identificationSearchRequest.getReportedToDate().toString());

		
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
		
		identificationSearchRequestSQLProcessor.auditIdentificationSearchRequest(senderExchange, document);

		IdentificationSearchResponseSQLProcessor identificationSearchResponseSQLProcessor = new IdentificationSearchResponseSQLProcessor();
		
		identificationSearchResponseSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        inputFile = new File("src/test/resources/xmlInstances/OrganizationIdentificationResultsSearchResults-civil.xml");

        document = db.parse(inputFile);
		
        identificationSearchResponseSQLProcessor.auditIdentificationSearchResponse(document, "123456");

	}
	
}
