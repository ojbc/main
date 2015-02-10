package org.ojbc.intermediaries.sn.testutil;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.ojbc.intermediaries.sn.subscription.SubscriptionProcessor;
import org.ojbc.intermediaries.sn.util.SubscriptionResponseBuilderUtil;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class TestSubscriptionBuilderUtil {

	private static final Log log = LogFactory.getLog(SubscriptionProcessor.class);

	@Test
	public void createSubscribeResponse() throws Exception {
		String subscriptionResponse = SubscriptionResponseBuilderUtil.createSubscribeResponse();
		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		Document response = docBuilder.parse(new ByteArrayInputStream(subscriptionResponse.getBytes()));
		validateAgainstWSNSpec(response);
	}

	private void validateAgainstWSNSpec(Document response) throws Exception {
		String b2RootXsdPath = "service-specifications/Subscription_Notification_Service/WSDL/wsn/b-2.xsd";
		String b2RootXsdDir = "service-specifications/Subscription_Notification_Service/WSDL/wsn";
		String wsdlDir = "service-specifications/Subscription_Notification_Service/WSDL";
		String subNotDir = "service-specifications/Subscription_Notification_Service";
		List<String> xsdDirPaths = Arrays.asList(b2RootXsdDir, wsdlDir, subNotDir);
		XmlUtils.validateInstanceWithAbsoluteClasspaths(b2RootXsdPath, xsdDirPaths, response);
	}

}
