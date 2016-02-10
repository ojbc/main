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
package org.ojbc.intermediaries.sn.testutil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;
import org.ojbc.intermediaries.sn.util.FaultMessageBuilderUtil;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.util.xml.IEPDResourceResolver;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class TestFaultMessageBuilderUtil {

	private static final Log log = LogFactory.getLog(TestFaultMessageBuilderUtil.class);

	@Test
	public void testCreateUnableToValidateSubscriptionFault() throws Exception {
		Document doc = FaultMessageBuilderUtil.createUnableToValidateSubscriptionFault("12345");

		String fault = OJBUtils.getStringFromDocument(doc);
		validateAgainstWSNSpec(parseString(fault));

		assertEquals(
				"12345",
				XmlUtils.xPathStringSearch(
						doc,
						"/b-2:UnableToValidateSubscriptionFault/subfltrsp-exch:SubscriptionValidationFaultResponseMessage/subfltrsp-ext:SubscriptionIdentification/nc:IdentificationID"));
		assertEquals(
				"Subscriptions",
				XmlUtils.xPathStringSearch(
						doc,
						"/b-2:UnableToValidateSubscriptionFault/subfltrsp-exch:SubscriptionValidationFaultResponseMessage/subfltrsp-ext:SubscriptionIdentification/nc:IdentificationSourceText"));

		assertNotNull(XmlUtils.xPathStringSearch(doc, "/b-2:UnableToValidateSubscriptionFault/bf2:Timestamp"));
	}

	@Test
	public void testCreateUnableToDestorySubscriptionFault() throws Exception {
		String fault = FaultMessageBuilderUtil.createUnableToDestorySubscriptionFault();
		validateAgainstWSNSpec(parseString(fault));
	}

	@Test
	public void testCreateSubscribeCreationFailedFaultErrorResponse() throws Exception {
		String fault = FaultMessageBuilderUtil.createSubscribeCreationFailedFaultErrorResponse("Some Error Text");
		validateAgainstWSNSpec(parseString(fault));
		validateFaultResponse(fault);
	}

	@Test
	public void testCreateSubscribeCreationFailedFaultInvalidEmailResponse() throws Exception {
		List<String> emailAddresses = new ArrayList<String>();
		emailAddresses.add("1@1.com");
		emailAddresses.add("2@2.com");

		String fault = FaultMessageBuilderUtil.createSubscribeCreationFailedFaultInvalidEmailResponse(emailAddresses);
		validateAgainstWSNSpec(parseString(fault));
		validateFaultResponse(fault);

	}

	@Test
	public void testCreateSubscribeCreationFailedFaultAccessDenialResponse() throws Exception {
		String fault = FaultMessageBuilderUtil
				.createSubscribeCreationFailedFaultAccessDenialResponse("The user is not privileged to create subscriptions");
		validateAgainstWSNSpec(parseString(fault));
		validateFaultResponse(fault);
	}

	@Test
	public void testCreateUnableToDestorySubscriptionFaultAccessDenialResponse() throws Exception {
		String fault = FaultMessageBuilderUtil
				.createUnableToDestorySubscriptionFaultAccessDenialResponse("The user is not privileged to create subscriptions");
		validateAgainstWSNSpec(parseString(fault));
		validateFaultResponse(fault);
	}

	private Document parseString(String s) throws Exception {
		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
		return docBuilder.parse(new ByteArrayInputStream(s.getBytes()));
	}

	private void validateFaultResponse(String fault) throws Exception {

		Node faultResponse = XmlUtils.xPathNodeSearch(OJBUtils.loadXMLFromString(fault), "//subfltrsp-exch:SubscriptionFaultResponseMessage");
		Assert.assertNotNull(faultResponse);

		DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
		docBuilderFact.setNamespaceAware(true);
		DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();

		Document document = docBuilder.newDocument();
		document.adoptNode(faultResponse);

		String iepdRootPath = "ssp/Subscription_Notification_Service/information_model/Subscription_Response-IEPD/xsd";
		XmlUtils.validateInstance(iepdRootPath, "Subscription_Fault_Response.xsd", document, new ResourceResolver(iepdRootPath));

	}

	private void validateAgainstWSNSpec(Document response) throws Exception {
		String b2RootXsdPath = "ssp/Subscription_Notification_Service/WSDL/wsn/b-2.xsd";
		String b2RootXsdDir = "ssp/Subscription_Notification_Service/WSDL/wsn";
		String wsdlDir = "ssp/Subscription_Notification_Service/WSDL";
		String subNotDir = "ssp/Subscription_Notification_Service";
		List<String> xsdDirPaths = Arrays.asList(b2RootXsdDir, wsdlDir, subNotDir);
		XmlUtils.validateInstanceWithAbsoluteClasspaths(b2RootXsdPath, xsdDirPaths, response);
	}

	private static final class ResourceResolver extends IEPDResourceResolver {

		public ResourceResolver(String iepdRootPath) {
			super("", iepdRootPath);
		}

		protected String reformatResourcePath(String systemId, String baseURI) {

			//log.info("schemaRootFolderName=" + schemaRootFolderName + ", iepdRootPath=" + iepdRootPath);
			//log.info("baseURI=" + baseURI + ", systemId=" + systemId);

			String fullPath = null;

			if (baseURI == null) {
				fullPath = iepdRootPath + "/" + systemId;
			} else if (systemId.contains("Subset")) {
				int subsetPosition = systemId.indexOf("Subset");
				String trimmedSystemId = systemId.substring(subsetPosition);
				fullPath = trimDirsFromPath(iepdRootPath, "/../..") + "/" + trimmedSystemId;
			} else if (systemId.startsWith("../../..")) {
				fullPath = trimDirsFromPath(iepdRootPath, "/../..") + "/Subset/niem/" + systemId.substring(9);
			} else if (systemId.startsWith("../..")) {
				fullPath = trimDirsFromPath(iepdRootPath, "/../..") + "/Subset/niem/" + systemId.substring(6);
			}

			//log.info("Returning fullPath=" + fullPath);
			return fullPath;

		}

	}

	@Test
	public void testTrimDirsFromPath() {
		assertEquals("a", trimDirsFromPath("a", ""));
		assertEquals("a", trimDirsFromPath("a/b", ".."));
		assertEquals("a/b", trimDirsFromPath("a/b/c", ".."));
		assertEquals("a/b", trimDirsFromPath("a/b/c/", ".."));
		assertEquals("a", trimDirsFromPath("a/b/c/", "../.."));
	}

	static String trimDirsFromPath(String path, String relativeRef) {
		Pattern p = Pattern.compile("\\.\\.");
		Matcher m = p.matcher(relativeRef);
		int backups = 0;
		while (m.find()) {
			backups++;
		}
		String trimmedPath = path;
		if (path.endsWith("/")) {
			trimmedPath = path.substring(0, path.length() - 1);
		}
		for (int i = 0; i < backups; i++) {
			trimmedPath = trimmedPath.substring(0, trimmedPath.lastIndexOf("/"));
		}
		return trimmedPath;
	}

}
