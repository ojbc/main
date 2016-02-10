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
package org.ojbc.intermediaries.sn.subscription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml", 
		"classpath:META-INF/spring/h2-mock-database-context-rapback-datastore.xml", })
@DirtiesContext
public class TestSubscriptionValidationMessageProcessor {

	@Resource
	private DataSource dataSource;

	@Autowired
	private SubscriptionValidationMessageProcessor subscriptionValidationMessageProcessor;

	@Before
	public void setUp() throws Exception {
		subscriptionValidationMessageProcessor = new SubscriptionValidationMessageProcessor();
		subscriptionValidationMessageProcessor.setDataSource(dataSource);
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

		ex.getIn().setBody(document);

		subscriptionValidationMessageProcessor.validateSubscription(ex);

		Document response = (Document) ex.getIn().getBody();
		validateAgainstWSNSpec(response);

		String indicator = XmlUtils.xPathStringSearch(response,
				"/b-2:ValidateResponse/svm:SubscriptionValidationResponseMessage/subresp-ext:SubscriptionValidatedIndicator");
		assertEquals("true", indicator);

	}

	private void validateAgainstWSNSpec(Document response) throws Exception {
		 String b2RootXsdPath = "ssp/Subscription_Notification_Service/WSDL/wsn/b-2.xsd";
         String b2RootXsdDir =  "ssp/Subscription_Notification_Service/WSDL/wsn";
         String wsdlDir = "ssp/Subscription_Notification_Service/WSDL";
         String subNotDir = "ssp/Subscription_Notification_Service";
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

		assertEquals("62720", processor.returnSubscriptionIDFromSubscriptionValidationRequestMessage(document));

	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidReturnSubscriptionIDFromSubscriptionValidationRequestMessage() throws Exception {
		SubscriptionValidationMessageProcessor processor = new SubscriptionValidationMessageProcessor();

		File inputFile = new File("src/test/resources/xmlInstances/Validation_Request_Invalid.xml");

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document document = docBuilder.parse(inputFile);

		processor.returnSubscriptionIDFromSubscriptionValidationRequestMessage(document);

	}

}
