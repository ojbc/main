package org.ojbc.util.camel.ndex.util;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.NDexUtils;
import org.ojbc.util.ndex.util.UniqueIDGenerator;

public class TestNDexUtils {

	private NDexUtils ndexUtils;
	private UniqueIDGenerator uniqueIDGenerator;
	
	@Before
	public void setUp() throws Exception {
		ndexUtils = new NDexUtils();
		uniqueIDGenerator = new UniqueIDGenerator();
		
		ndexUtils.setUniqueIDGenerator(uniqueIDGenerator);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCreateNdexFormatFileName() throws Exception
	{
		//Expected file name: ME306000_20140422_1024_0001.xml
		String ndexFileName = ndexUtils.createNdexFormatFileName("ME306000");
		
		assertTrue(ndexFileName.startsWith("ME306000"));
		assertTrue(ndexFileName.endsWith("0001.xml"));
		
		String between = StringUtils.substringBetween(ndexFileName, "ME306000", "0001.xml");
		assertEquals(15, between.length());
		
		ndexFileName = ndexUtils.createNDexTestFileName(ndexFileName);
		
		assertTrue(ndexFileName.startsWith("TESTME306000"));
		assertTrue(ndexFileName.endsWith("0001.xml"));
		
		between = StringUtils.substringBetween(ndexFileName, "ME306000", "0001.xml");
		assertEquals(15, between.length());

	}

	
	@Test
	public void testGetAgencyORIFromFileName()
	{
		//Expected Camel File Name format INCIDENT_${in.headers.IncidentID}_DATE_${date:now:yyyyMMdd}_${date:now:HHmmssSS}.xml
		
		String camelFileName = "INCIDENT_INCIDENTID_DATE_20140422_23112233.xml";
		
		String incidentID = ndexUtils.getIncidentIDFromFileName(camelFileName);
		
		assertEquals(incidentID, "INCIDENTID");
		
		camelFileName = "INCIDENT__DATE_20140422_23112233.xml";
		
		incidentID = ndexUtils.getIncidentIDFromFileName(camelFileName);
		
		assertEquals(incidentID, "UnknownIncidentID");
		
	}
}
