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
package org.ojbc.bundles.adapters.staticmock.samplegen;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.bundles.adapters.staticmock.JuvenileHistoryContainer;
import org.ojbc.bundles.adapters.staticmock.JuvenileHistoryContainerTestUtils;
import org.ojbc.bundles.adapters.staticmock.samplegen.AbstractSampleGenerator;
import org.ojbc.bundles.adapters.staticmock.samplegen.JuvenileHistorySampleGenerator;
import org.w3c.dom.Document;

public class TestJuvenileHistorySampleGenerator {

	private static final Log LOG = LogFactory.getLog(TestJuvenileHistorySampleGenerator.class);
	private static final int TEST_CASES = 10;

	private JuvenileHistorySampleGenerator juvenileHistorySampleGenerator;
	private AbstractSampleGenerator.PersonElementWrapper testKid;
	private DateTime baseDate;

	@Before
	public void setUp() throws Exception {
		juvenileHistorySampleGenerator = new JuvenileHistorySampleGenerator();
		testKid = juvenileHistorySampleGenerator.getRandomIdentity("WA");
		baseDate = new DateTime();
	}

	@Test
	public void testCreateJuvenileHistory() throws Exception {
		JuvenileHistorySampleGenerator.JuvenileHistory juvenileHistory = juvenileHistorySampleGenerator.createJuvenileHistory(testKid, baseDate, "WA");
		assertNotNull(juvenileHistory.court);
		assertNotNull(juvenileHistory.kid);
		assertTrue(Years.yearsBetween(juvenileHistory.kid.birthdate, baseDate).getYears() <= 17);
		assertTrue(juvenileHistory.mother != null || juvenileHistory.father != null);
		assertNotNull(juvenileHistory.kidResidence);
		assertTrue(juvenileHistory.fatherResidence != null || juvenileHistory.motherResidence != null);
		assertTrue(juvenileHistory.referrals.size() > 0);
		assertTrue(juvenileHistory.placements.size() > 0);
		assertTrue(juvenileHistory.offenseCharges.size() > 0);
		assertTrue(juvenileHistory.intakes.size() > 0);
		assertTrue(juvenileHistory.hearings.size() > 0);
	}

	@Test
	public void testBuildReferralDocument() throws Exception {
		for (int i = 0; i < TEST_CASES; i++) {
			Document d = juvenileHistorySampleGenerator.createJuvenileHistoryInstanceDocument(testKid, baseDate, "WA");
			//XmlUtils.printNode(d);
			JuvenileHistoryContainer juvenileHistoryContainer = new JuvenileHistoryContainer(d);
			Document dd = juvenileHistoryContainer.buildReferralHistoryDocument();
			JuvenileHistoryContainerTestUtils.validateReferralHistoryDocument(dd);
		}
	}

	@Test
	public void testBuildOffenseDocument() throws Exception {
		for (int i = 0; i < TEST_CASES; i++) {
			Document d = juvenileHistorySampleGenerator.createJuvenileHistoryInstanceDocument(testKid, baseDate, "WA");
			//XmlUtils.printNode(d);
			JuvenileHistoryContainer juvenileHistoryContainer = new JuvenileHistoryContainer(d);
			Document dd = juvenileHistoryContainer.buildOffenseHistoryDocument();
			JuvenileHistoryContainerTestUtils.validateOffenseHistoryDocument(dd);
		}
	}

	@Test
	public void testBuildCasePlanDocument() throws Exception {
		for (int i = 0; i < TEST_CASES; i++) {
			Document d = juvenileHistorySampleGenerator.createJuvenileHistoryInstanceDocument(testKid, baseDate, "WA");
			//XmlUtils.printNode(d);
			JuvenileHistoryContainer juvenileHistoryContainer = new JuvenileHistoryContainer(d);
			Document dd = juvenileHistoryContainer.buildCasePlanHistoryDocument();
			JuvenileHistoryContainerTestUtils.validateCasePlanHistoryDocument(dd);
		}
	}

	@Test
	public void testBuildPlacementDocument() throws Exception {
		for (int i = 0; i < TEST_CASES; i++) {
			Document d = juvenileHistorySampleGenerator.createJuvenileHistoryInstanceDocument(testKid, baseDate, "WA");
			//XmlUtils.printNode(d);
			JuvenileHistoryContainer juvenileHistoryContainer = new JuvenileHistoryContainer(d);
			Document dd = juvenileHistoryContainer.buildPlacementHistoryDocument();
			JuvenileHistoryContainerTestUtils.validatePlacementHistoryDocument(dd);
		}
	}

	@Test
	public void testBuildIntakeDocument() throws Exception {
		for (int i = 0; i < TEST_CASES; i++) {
			Document d = juvenileHistorySampleGenerator.createJuvenileHistoryInstanceDocument(testKid, baseDate, "WA");
			//XmlUtils.printNode(d);
			JuvenileHistoryContainer juvenileHistoryContainer = new JuvenileHistoryContainer(d);
			Document dd = juvenileHistoryContainer.buildIntakeHistoryDocument();
			JuvenileHistoryContainerTestUtils.validateIntakeHistoryDocument(dd);
		}
	}

	@Test
	public void testBuildHearingDocument() throws Exception {
		for (int i = 0; i < TEST_CASES; i++) {
			Document d = juvenileHistorySampleGenerator.createJuvenileHistoryInstanceDocument(testKid, baseDate, "WA");
			//XmlUtils.printNode(d);
			JuvenileHistoryContainer juvenileHistoryContainer = new JuvenileHistoryContainer(d);
			Document dd = juvenileHistoryContainer.buildHearingHistoryDocument();
			JuvenileHistoryContainerTestUtils.validateHearingHistoryDocument(dd);
		}
	}

}
