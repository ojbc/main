package org.ojbc.adamscounty.xml.util;

import static org.junit.Assert.*;

import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class TestCustodyBookingXMLUtils {

	@Test
	public void testCreateCustodyStatusChangeReport() throws Exception
	{
		Document custodyStatusChangeReport = CustodyBookingXMLUtils.createCustodyStatusChangeReport("custodyStatusChange");
		
		XmlUtils.printNode(custodyStatusChangeReport);
		
		assertEquals("Correct", XmlUtils.xPathStringSearch(custodyStatusChangeReport, "/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/jxdm51:Booking/cscr-ext:BookingStatusCode"));
	}
	
	@Test
	public void testcreateBookingReport() throws Exception
	{
		Document bookingReport = CustodyBookingXMLUtils.createBookingReport("booking");
		
		XmlUtils.printNode(bookingReport);
		
		assertEquals("Correct", XmlUtils.xPathStringSearch(bookingReport, "/br-doc:BookingReport/jxdm51:Booking/br-ext:BookingStatusCode"));
	}

	@Test
	public void testCreateReleaseReport() throws Exception
	{
		Document custodyReleaseChangeReport = CustodyBookingXMLUtils.createCustodyReleaseReport("custodyRelease");
		
		XmlUtils.printNode(custodyReleaseChangeReport);
		
		assertEquals("Correct", XmlUtils.xPathStringSearch(custodyReleaseChangeReport, "/crr-exc:CustodyReleaseReport/crr-ext:Custody/jxdm51:Booking/crr-ext:BookingStatusCode"));
		
	}

}
