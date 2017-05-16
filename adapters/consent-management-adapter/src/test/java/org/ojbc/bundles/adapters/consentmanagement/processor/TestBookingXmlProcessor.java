package org.ojbc.bundles.adapters.consentmanagement.processor;

import static org.junit.Assert.*;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.ojbc.util.camel.helper.OJBUtils;
import org.w3c.dom.Document;

public class TestBookingXmlProcessor {

	@Test
	public void testProcessBookingReport() throws Exception
	{
		File inputFile = new File("src/test/resources/xmlInstances/bookingReport/BookingReportJail.xml");
	    String inputStr = FileUtils.readFileToString(inputFile);
		
		Document report = OJBUtils.loadXMLFromString(inputStr);
		assertNotNull(report);
		
		BookingXMLProcessor bookingXMLProcessor = new BookingXMLProcessor();
		
		Consent consent = bookingXMLProcessor.processBookingReport(report);
		
		assertEquals("Given", consent.getPersonFirstName());
		assertEquals("Middle", consent.getPersonMiddleName());
		assertEquals("Last", consent.getPersonLastName());
		
		assertEquals("111111", consent.getBookingNumber());
		assertEquals("222222", consent.getNameNumber());
		
		assertEquals("M", consent.getPersonGender());
		
		assertEquals("1994-01-02", consent.getPersonDOB().toString());
		
	}
	
}
