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
public class TestPersonSearchProcessor {

	private static final Log log = LogFactory.getLog(TestPersonSearchProcessor.class);
	
	@Resource
	private EnhancedAuditDAO enhancedAuditDao;
	
	@Test
	public void testProcessPersonSearchRequest() throws Exception
	{
		PersonSearchRequestSQLProcessor personSearchRequestProcessor = new PersonSearchRequestSQLProcessor();
		UserInfoSQLProcessor userInfoSQLProcessor = new UserInfoSQLProcessor();
		
		userInfoSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		personSearchRequestProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
		personSearchRequestProcessor.setUserInfoSQLProcessor(userInfoSQLProcessor);
		
        File inputFile = new File("src/test/resources/xmlInstances/personSearchRequest.xml");

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        dbf.setNamespaceAware(true);
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.parse(inputFile);
		
		PersonSearchRequest personSearchRequest = personSearchRequestProcessor.processPersonSearchRequest(document);
		
		log.info(personSearchRequest.toString());
		
		assertEquals("Frank", personSearchRequest.getFirstName());
		//assertEquals(new Integer(2), personSearchRequest.getFirstNameQualifier());
		assertEquals("f", personSearchRequest.getMiddleName());
		assertEquals("Smith", personSearchRequest.getLastName());
		//assertEquals(new Integer(2), personSearchRequest.getLastNameQualifier());
		assertEquals("123456789", personSearchRequest.getSsn());
		assertEquals("D123456789", personSearchRequest.getDriverLicenseId());
		assertEquals("WI", personSearchRequest.getDriverLiscenseIssuer());
		assertEquals("FBI12345", personSearchRequest.getFbiNumber());
		assertEquals("A123456789", personSearchRequest.getStateId());
		assertEquals("I", personSearchRequest.getRaceCode());
		assertEquals("M", personSearchRequest.getGenderCode());
		assertEquals("BLU", personSearchRequest.getEyeCode());
		assertEquals("BLK", personSearchRequest.getHairCode());
		assertEquals(2, personSearchRequest.getSystemsToSearch().size());
		assertEquals("{system1}URI", personSearchRequest.getSystemsToSearch().get(0));
		assertEquals("{system2}URI", personSearchRequest.getSystemsToSearch().get(1));
		assertEquals("On Behalf Of", personSearchRequest.getOnBehalfOf());
		assertEquals("This is the purpose", personSearchRequest.getPurpose());
		assertEquals(Integer.valueOf(50), personSearchRequest.getHeightMin());
		assertEquals(Integer.valueOf(75), personSearchRequest.getHeightMax());
		
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
		
		personSearchRequestProcessor.auditPersonSearchRequest(senderExchange, document);
		
		AuditSearchRequest personAuditSearchRequest = new AuditSearchRequest();
		
		personAuditSearchRequest.setStartTime(LocalDateTime.now().minusHours(1));
		personAuditSearchRequest.setEndTime(LocalDateTime.now().plusHours(1));
        
		List<PersonSearchRequest> personAuditSearchRequests = enhancedAuditDao.retrievePersonSearchRequest(personAuditSearchRequest);
		assertEquals(1, personAuditSearchRequests.size());
		
		assertEquals("Frank", personAuditSearchRequests.get(0).getFirstName());
		assertEquals("1990-05-30", personAuditSearchRequests.get(0).getDobFrom().toString());
		assertEquals("2000-05-30", personAuditSearchRequests.get(0).getDobTo().toString());
		assertEquals("D123456789", personAuditSearchRequests.get(0).getDriverLicenseId());
		assertEquals("BLU", personAuditSearchRequests.get(0).getEyeCode());
		assertEquals("FBI12345", personAuditSearchRequests.get(0).getFbiNumber());
		assertEquals("M", personAuditSearchRequests.get(0).getGenderCode());
		assertEquals("BLK", personAuditSearchRequests.get(0).getHairCode());
		assertEquals(new Integer(50), personAuditSearchRequests.get(0).getHeightMin());
		assertEquals(new Integer(75), personAuditSearchRequests.get(0).getHeightMax());
		assertEquals("Smith", personAuditSearchRequests.get(0).getLastName());
		assertEquals("123456", personAuditSearchRequests.get(0).getMessageId());
		assertEquals("f", personAuditSearchRequests.get(0).getMiddleName());
		assertEquals("On Behalf Of", personAuditSearchRequests.get(0).getOnBehalfOf());
		assertEquals("This is the purpose", personAuditSearchRequests.get(0).getPurpose());
		assertEquals("I", personAuditSearchRequests.get(0).getRaceCode());
		assertEquals("123456789", personAuditSearchRequests.get(0).getSsn());
		assertEquals("A123456789", personAuditSearchRequests.get(0).getStateId());
		assertEquals(2, personAuditSearchRequests.get(0).getSystemsToSearch().size());
		
		PersonSearchResponseSQLProcessor personSearchResponseSQLProcessor = new PersonSearchResponseSQLProcessor();
		
		personSearchResponseSQLProcessor.setEnhancedAuditDAO(enhancedAuditDao);
		
        inputFile = new File("src/test/resources/xmlInstances/personSearchResponse.xml");

        document = db.parse(inputFile);
		
        personSearchResponseSQLProcessor.auditPersonSearchResponse(document, "123456");

        inputFile = new File("src/test/resources/xmlInstances/ErrorMessage-PersonSearchResults.xml");

        document = db.parse(inputFile);
		
        personSearchResponseSQLProcessor.auditPersonSearchResponse(document, "123456");
        
        inputFile = new File("src/test/resources/xmlInstances/ErrorReported-PersonSearchResults.xml");

        document = db.parse(inputFile);
		
        personSearchResponseSQLProcessor.auditPersonSearchResponse(document, "123456");

	}
	
}
