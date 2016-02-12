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

import java.util.Arrays;
import java.util.List;

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
		Document subscriptionResponse = SubscriptionResponseBuilderUtil.createSubscribeResponse();
		//XmlUtils.printNode(subscriptionResponse);
		validateAgainstWSNSpec(subscriptionResponse);
	}

	private void validateAgainstWSNSpec(Document response) throws Exception {
		String b2RootXsdPath = "ssp/Subscription_Notification/WSDL/wsn/b-2.xsd";
		String b2RootXsdDir = "ssp/Subscription_Notification/WSDL/wsn";
		String wsdlDir = "ssp/Subscription_Notification/WSDL";
		String subNotDir = "ssp/Subscription_Notification";
		List<String> xsdDirPaths = Arrays.asList(b2RootXsdDir, wsdlDir, subNotDir);
		XmlUtils.validateInstanceWithAbsoluteClasspaths(b2RootXsdPath, xsdDirPaths, response);
	}

}
