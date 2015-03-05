package org.search.ojb.samplegen;

import static org.junit.Assert.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class TestJuvenileHistorySampleGenerator {
	
	private static final Log LOG = LogFactory.getLog(TestJuvenileHistorySampleGenerator.class);
	
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
	public void testBuildContainerDocument() throws Exception {
		Document d = juvenileHistorySampleGenerator.createJuvenileHistoryInstanceDocument(testKid, baseDate, "WA");
		XmlUtils.printNode(d);
	}
	
}
