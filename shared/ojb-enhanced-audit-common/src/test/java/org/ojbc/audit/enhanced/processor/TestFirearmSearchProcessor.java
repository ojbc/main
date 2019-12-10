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
import java.time.LocalDateTime;
import java.util.List;

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
import org.ojbc.audit.enhanced.dao.model.FirearmsSearchRequest;
import org.ojbc.audit.enhanced.dao.model.PersonSearchRequest;
import org.ojbc.audit.enhanced.dao.model.auditsearch.AuditSearchRequest;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
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
public class TestFirearmSearchProcessor {

	private static final Log log = LogFactory.getLog(TestFirearmSearchProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessFirearmSearchRequest() throws Exception
	{
		FirearmsSearchRequestSQLProcessor firearmSearchRequestSQLProcessor = new FirearmsSearchRequestSQLProcessor();
		UserInfoSQLProcessor userInfoSQLProcessor = new UserInfoSQLProcessor();
		
		userInfoSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		firearmSearchRequestSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
		firearmSearchRequestSQLProcessor.setUserInfoSQLProcessor(userInfoSQLProcessor);
		
        File inputFile = new File("src/test/resources/xmlInstances/FirearmSearchRequest.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		FirearmsSearchRequest firearmsSearchRequest = firearmSearchRequestSQLProcessor.processFirearmsSearchRequest(document);
		
		log.info(firearmsSearchRequest.toString());
		
		assertEquals(3, firearmsSearchRequest.getSystemsToSearch().size());
		assertEquals("{system1}URI", firearmsSearchRequest.getSystemsToSearch().get(0));
		assertEquals("{system2}URI", firearmsSearchRequest.getSystemsToSearch().get(1));
		assertEquals("{system3}URI", firearmsSearchRequest.getSystemsToSearch().get(2));
		assertEquals("John Doe", firearmsSearchRequest.getOnBehalfOf());
		assertEquals("Criminal Justice", firearmsSearchRequest.getPurpose());
		assertEquals("Remington", firearmsSearchRequest.getMake());
		assertEquals("R", firearmsSearchRequest.getFirearmsType());
		assertEquals("Model", firearmsSearchRequest.getModel());
		assertEquals("Registration Number", firearmsSearchRequest.getRegistrationNumber());
		assertEquals("123476576", firearmsSearchRequest.getSerialNumber());
		assertEquals("exact", firearmsSearchRequest.getSerialNumberQualifierCode());
		
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
		
		firearmSearchRequestSQLProcessor.auditFirearmsSearchRequest(senderExchange, document);
		
		FirearmsSearchResponseSQLProcessor firearmsSearchResponseSQLProcessor = new FirearmsSearchResponseSQLProcessor();
		
		firearmsSearchResponseSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        inputFile = new File("src/test/resources/xmlInstances/FirearmSearchResults-single.xml");

        document = db.parse(inputFile);
		
        firearmsSearchResponseSQLProcessor.auditFirearmSearchResponse(document, "123456");
        
		AuditSearchRequest firearmAuditSearchRequest = new AuditSearchRequest();
		
		firearmAuditSearchRequest.setStartTime(LocalDateTime.now().minusHours(1));
		firearmAuditSearchRequest.setEndTime(LocalDateTime.now().plusHours(1));
        
		List<FirearmsSearchRequest> firearmSearchRequests = enhancedAuditDao.retrieveFirearmSearchRequest(firearmAuditSearchRequest);
		assertEquals(1, firearmSearchRequests.size());
		
		//Add assertions here
		assertEquals("R", firearmSearchRequests.get(0).getFirearmsType());
		assertEquals("Remington", firearmSearchRequests.get(0).getMake());
		assertEquals("Model", firearmSearchRequests.get(0).getModel());
		assertEquals("John Doe", firearmSearchRequests.get(0).getOnBehalfOf());
		assertEquals("Criminal Justice", firearmSearchRequests.get(0).getPurpose());
		assertEquals("Registration Number", firearmSearchRequests.get(0).getRegistrationNumber());
		assertEquals("123476576", firearmSearchRequests.get(0).getSerialNumber());
		assertEquals(3, firearmSearchRequests.get(0).getSystemsToSearch().size());
		//TODO: Search Qualifier Code
		
        inputFile = new File("src/test/resources/xmlInstances/Error-FirearmSearchResults.xml");

        document = db.parse(inputFile);
		
        firearmsSearchResponseSQLProcessor.auditFirearmSearchResponse(document, "123456");
        
        inputFile = new File("src/test/resources/xmlInstances/Access-Denial-FirearmSearchResults.xml");

        document = db.parse(inputFile);
		
        firearmsSearchResponseSQLProcessor.auditFirearmSearchResponse(document, "123456");
        
	}
	
}
