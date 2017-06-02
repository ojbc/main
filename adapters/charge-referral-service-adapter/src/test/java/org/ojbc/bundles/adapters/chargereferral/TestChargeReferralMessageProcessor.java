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
package org.ojbc.bundles.adapters.chargereferral;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.ojbc.bundles.adapters.chargereferral.ChargeReferralMessageProcessor;
import org.ojbc.bundles.adapters.chargereferral.model.ChargeReferral;
import org.ojbc.util.camel.helper.OJBUtils;
import org.w3c.dom.Document;

public class TestChargeReferralMessageProcessor {

	private Document chargeReferralDocument;
	ChargeReferralMessageProcessor chargeReferralMessageProcessor = new ChargeReferralMessageProcessor();
	
	@Test
	public void testProcessMessage() throws Exception
	{

		File inputFile = new File("src/test/resources/xml/Michigan_Charge_Referral.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
		
		chargeReferralDocument = OJBUtils.loadXMLFromString(inputStr);
		assertNotNull(chargeReferralDocument);	
		
		ChargeReferral chargeReferral = chargeReferralMessageProcessor.processMessage(chargeReferralDocument);
		
		assertEquals("Incident Reporting Agency", chargeReferral.getOrganizationName());
		
	}
	
}
