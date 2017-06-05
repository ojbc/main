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
package org.ojbc.intermediaries.sn.util;

import javax.xml.parsers.DocumentBuilderFactory;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Utility class for building subscription and unsubscription response messages.
 *
 */
public class SubscriptionResponseBuilderUtil {
	
	private static Document BASE_UNSUBSCRIPTION_RESPONSE = null;
	private static Document BASE_SUBSCRIPTION_RESPONSE = null;
	private static Node SUBSCRIPTION_CURRENT_TIME_NODE = null;
	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
	
	static {
		
		try {
			
			// we do this in a static block once, at class loading, to avoid any performance bottlenecks with rebuilding the DOM structure
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			dbf.setNamespaceAware(true);
			
			BASE_UNSUBSCRIPTION_RESPONSE = dbf.newDocumentBuilder().newDocument();
			Element root = BASE_UNSUBSCRIPTION_RESPONSE.createElementNS("http://docs.oasis-open.org/wsn/b-2", "UnsubscribeResponse");
			root.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);
			BASE_UNSUBSCRIPTION_RESPONSE.appendChild(root);

			BASE_SUBSCRIPTION_RESPONSE = dbf.newDocumentBuilder().newDocument();
			
			root = BASE_SUBSCRIPTION_RESPONSE.createElementNS("http://docs.oasis-open.org/wsn/b-2", "SubscribeResponse");
			root.setPrefix(OjbcNamespaceContext.NS_PREFIX_B2);
			BASE_SUBSCRIPTION_RESPONSE.appendChild(root);
			
			Element e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_B2, "SubscriptionReference");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_ADD, "Address").setTextContent("http://www.ojbc.org/SubcribeNotify/");
			SUBSCRIPTION_CURRENT_TIME_NODE = XmlUtils.appendElement(e, OjbcNamespaceContext.NS_B2, "CurrentTime");
			
			e = XmlUtils.appendElement(root, OjbcNamespaceContext.NS_SUBSCRIPTION_RESPONSE_EXCH, "SubscriptionResponseMessage");
			XmlUtils.appendElement(e, OjbcNamespaceContext.NS_SUBSCRIPTION_RESPONSE_EXT, "SubscriptionCreatedIndicator").setTextContent("true");
			
			new OjbcNamespaceContext().populateRootNamespaceDeclarations(root);
			
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static Document createSubscribeResponse() throws Exception {
		return createSubscribeResponse(new DateTime());
	}
	
	static Document createSubscribeResponse(DateTime creationDate) throws Exception {
		SUBSCRIPTION_CURRENT_TIME_NODE.setTextContent(DATE_FORMATTER.print(creationDate));
		return BASE_SUBSCRIPTION_RESPONSE;
	}

	public static Document createUnsubscribeResponse() {
		return BASE_UNSUBSCRIPTION_RESPONSE;
	}
	
}
