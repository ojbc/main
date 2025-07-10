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
package org.ojbc.intermediaries.sn.subscription;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.junit.jupiter.api.Test;
import org.apache.camel.test.spring.junit5.CamelSpringTest;
import org.apache.cxf.message.MessageImpl;
import org.apache.wss4j.common.principal.SAMLTokenPrincipal;
import org.apache.wss4j.common.principal.SAMLTokenPrincipalImpl;
import org.apache.wss4j.common.saml.SamlAssertionWrapper;
import org.junit.jupiter.api.BeforeEach;
import org.ojbc.intermediaries.sn.dao.SubscriptionSearchQueryDAO;
import org.ojbc.util.camel.security.saml.SAMLTokenUtils;
import org.ojbc.util.model.rapback.Subscription;
import org.ojbc.util.model.saml.SamlAttribute;
import org.ojbc.util.xml.XmlUtils;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@CamelSpringTest
@SpringJUnitConfig(locations = { "classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml", 
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml",
		"classpath:META-INF/spring/h2-mock-database-context-enhanced-auditlog.xml"
		})
@DirtiesContext
public class TestSubscriptionValidationMessageProcessor {

	@Resource
	private DataSource dataSource;

	@Autowired
	private SubscriptionValidationMessageProcessor subscriptionValidationMessageProcessor;

	@Autowired
	private SubscriptionSearchQueryDAO subscriptionSearchQueryDAO;	

    //This is used to update database to achieve desired state for test
	private JdbcTemplate jdbcTemplate;
	
	@BeforeEach
	public void setUp() throws Exception {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Test
	public void testValidateSubscription() throws Exception {
		File inputFile = new File("src/test/resources/xmlInstances/Validation_Request.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);

		CamelContext ctx = new DefaultCamelContext();
		Exchange ex = new DefaultExchange(ctx);
		
		org.apache.cxf.message.Message message = new MessageImpl();

		//Add SAML token to request call
		SAMLTokenPrincipal principal = createSAMLToken();
		message.put("wss4j.principal.result", principal);
		
		ex.getIn().setHeader(CxfConstants.CAMEL_CXF_MESSAGE, message);
		
		ex.getIn().setHeader("subscriptionId", "44");
		ex.getIn().setHeader("validationDueDateString", "2030-11-10");
		
		ex.getIn().setBody(document);

		int rowsUpdated = this.jdbcTemplate.update("update rapback_datastore.SUBSCRIPTION set SUBSCRIPTION_CATEGORY_CODE='CS' where ID ='62723'");
		assertEquals(1,rowsUpdated);
		
		Subscription subscription = subscriptionSearchQueryDAO.findSubscriptionWithFbiInfoBySubscriptionId("62723"); 
		String validationDueDateString = subscriptionValidationMessageProcessor.getValidationDueDateString(subscription, "criminal");
		
		subscriptionValidationMessageProcessor.validateSubscription(ex, subscription, validationDueDateString,"criminal");

		Document response = (Document) ex.getIn().getBody();
		validateAgainstWSNSpec(response);

		String indicator = XmlUtils.xPathStringSearch(response,
				"/b-2:ValidateResponse/svm:SubscriptionValidationResponseMessage/subresp-ext:SubscriptionValidatedIndicator");
		assertEquals("true", indicator);

	}

	private void validateAgainstWSNSpec(Document response) throws Exception {
		 String b2RootXsdPath = "ssp/Subscription_Notification/WSDL/wsn/b-2.xsd";
         String b2RootXsdDir =  "ssp/Subscription_Notification/WSDL/wsn";
         String wsdlDir = "ssp/Subscription_Notification/WSDL";
         String subNotDir = "ssp/Subscription_Notification";
         List<String> xsdDirPaths = Arrays.asList(b2RootXsdDir, wsdlDir, subNotDir);
         XmlUtils.validateInstanceWithAbsoluteClasspaths(b2RootXsdPath, xsdDirPaths, response);
	}

	@Test
	public void testCreateSubscriptionValidationResponseMessage() throws Exception {
		SubscriptionValidationMessageProcessor processor = new SubscriptionValidationMessageProcessor();

		Document doc = processor.createSubscriptionValidationResponseMessage(true);

		// ensure the response document is valid by using the xsd to validate it
		validateAgainstWSNSpec(doc);

		Node subscriptionValidatedIndicatorElement = XmlUtils.xPathNodeSearch(doc,
				"/b-2:ValidateResponse/svm:SubscriptionValidationResponseMessage/subresp-ext:SubscriptionValidatedIndicator");
		assertNotNull(subscriptionValidatedIndicatorElement);

	}

	@Test
	public void testReturnSubscriptionIDFromSubscriptionValidationRequestMessage() throws Exception {
		SubscriptionValidationMessageProcessor processor = new SubscriptionValidationMessageProcessor();

		File inputFile = new File("src/test/resources/xmlInstances/Validation_Request.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);

		assertEquals("62723", processor.returnSubscriptionIDFromSubscriptionValidationRequestMessage(document));

	}

	@Test
	public void testInvalidReturnSubscriptionIDFromSubscriptionValidationRequestMessage() throws Exception {
		SubscriptionValidationMessageProcessor processor = new SubscriptionValidationMessageProcessor();

		File inputFile = new File("src/test/resources/xmlInstances/Validation_Request_Invalid.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);

		
		assertThrows(IllegalArgumentException.class, () -> {
			processor.returnSubscriptionIDFromSubscriptionValidationRequestMessage(document);
		});
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
