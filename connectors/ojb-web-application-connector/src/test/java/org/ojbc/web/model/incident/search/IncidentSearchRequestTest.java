package org.ojbc.web.model.incident.search;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.custommonkey.xmlunit.Diff;
import org.custommonkey.xmlunit.XMLUnit;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.ojbc.util.camel.helper.OJBUtils;
import org.ojbc.web.util.RequestMessageBuilderUtilities;
import org.w3c.dom.Document;

public class IncidentSearchRequestTest {

	private static final Log log = LogFactory.getLog( IncidentSearchRequestTest.class );
	
	@Before
	public void setUp() throws Exception {
		//Tell XML Unit to ignore whitespace between elements and within elements
		XMLUnit.setIgnoreWhitespace(true);
		XMLUnit.setNormalizeWhitespace(true);
		XMLUnit.setIgnoreComments(true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIncidentSearchRequest() throws Exception {
		IncidentSearchRequest isr = IncidentSearchRequestTestUtils.createIncidentSearchRequestModel();
		
		Assert.assertNotNull(isr);
		
		Document doc = RequestMessageBuilderUtilities.createIncidentSearchRequest(isr, "http://ojbc.org/IEPD/Extensions/DemostateLocationCodes/1.0", "LocationCityTownCode");
		doc.normalizeDocument();
		
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/incidentSearchRequest/incidentSearchRequest.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
		
		//System.out.println(OJBUtils.getStringFromDocument(doc));
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(expectedResponseAsString, OJBUtils.getStringFromDocument(doc));
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());
	}

	@Test
	public void testIncidentSearchRequestSameDateRange() throws Exception {
		IncidentSearchRequest isr = IncidentSearchRequestTestUtils.createIncidentSearchRequestModelSameDate();
		
		Assert.assertNotNull(isr);
		
        Document doc = RequestMessageBuilderUtilities.createIncidentSearchRequest(isr, "http://ojbc.org/IEPD/Extensions/DemostateLocationCodes/1.0", "LocationCityTownCode");
		doc.normalizeDocument();
        
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/incidentSearchRequest/incidentSearchRequestSameDateRange.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
		
		//System.out.println(OJBUtils.getStringFromDocument(doc));
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(expectedResponseAsString, OJBUtils.getStringFromDocument(doc));
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());
	}

	@Test
	public void testIncidentSearchRequestOnlyStartDate() throws Exception {
		IncidentSearchRequest isr = IncidentSearchRequestTestUtils.createIncidentSearchRequestModelOnlyStartDate();
		
		Assert.assertNotNull(isr);
		
        Document doc = RequestMessageBuilderUtilities.createIncidentSearchRequest(isr, "http://ojbc.org/IEPD/Extensions/DemostateLocationCodes/1.0", "LocationCityTownCode");
		doc.normalizeDocument();
        
		log.debug(OJBUtils.getStringFromDocument(doc));
		
		Assert.assertNotNull(doc);
		
	    //Read the expected response into a string
		File expectedReponseFile = new File("src/test/resources/xml/incidentSearchRequest/incidentSearchRequestOnlyStartDate.xml");
		String expectedResponseAsString = FileUtils.readFileToString(expectedReponseFile);
		
		//System.out.println(OJBUtils.getStringFromDocument(doc));
		
		//Use XML Unit to compare these files
		Diff myDiff = new Diff(expectedResponseAsString, OJBUtils.getStringFromDocument(doc));
		Assert.assertTrue("XML identical " + myDiff.toString(),
		               myDiff.identical());
	}
}
