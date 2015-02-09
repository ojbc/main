package org.ojbc.intermediaries.sn.subscription;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.ojbc.util.OJBUtils;
import org.ojbc.util.xml.IEPDResourceResolver;
import org.ojbc.util.xml.XmlUtils;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.DefaultExchange;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:META-INF/spring/test-application-context.xml",
		"classpath:META-INF/spring/h2-mock-database-application-context.xml", "classpath:META-INF/spring/h2-mock-database-context-subscription.xml", })
@DirtiesContext
public class TestSubscriptionValidationMessageProcessor {
	
	private static final Log log = LogFactory.getLog(TestSubscriptionValidationMessageProcessor.class);

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
		XmlUtils.validateInstance("service-specifications/Subscription_Notification_Service/WSDL/wsn", "b-2.xsd", response, new IEPDResourceResolver(
				"..", "service-specifications/Subscription_Notification_Service/WSDL/wsn"));
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
