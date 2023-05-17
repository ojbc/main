package org.ojbc.booking.common.processor;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class TestBookingPIIProcessor {

	@Test
	public void testRemovePII() throws Exception
	{
		File bookingFile = new File("src/test/resources/xmlInstances/BookingReport.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document originalDocument = dBuilder.parse(bookingFile);

		String personRef = XmlUtils.xPathStringSearch(originalDocument, "/br-doc:BookingReport/jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref");
		
		Node personNode = XmlUtils.xPathNodeSearch(originalDocument, "/br-doc:BookingReport/nc30:Person[@s30:id='" + personRef + "']");
		assertNotNull(personNode);
		
		assertNotNull(XmlUtils.xPathNodeSearch(personNode, "nc30:PersonBirthDate"));
		assertNotNull(XmlUtils.xPathNodeSearch(personNode, "nc30:PersonName"));
		assertNotNull(XmlUtils.xPathNodeSearch(personNode, "jxdm51:PersonAugmentation"));
		assertNotNull(XmlUtils.xPathNodeSearch(personNode, "nc30:PersonSSNIdentification"));
		assertNotNull(XmlUtils.xPathNodeSearch(originalDocument, "/br-doc:BookingReport/nc30:Identity"));
		assertNotNull(XmlUtils.xPathNodeSearch(originalDocument, "/br-doc:BookingReport/nc30:PersonAliasIdentityAssociation"));

		
		BookingPIIProcessor bookingPIIProcessor = new BookingPIIProcessor();
		
		Document alteredDocument = bookingPIIProcessor.removePII(originalDocument, "Booking");
		
		personRef = XmlUtils.xPathStringSearch(alteredDocument, "/br-doc:BookingReport/jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref");
		
		personNode = XmlUtils.xPathNodeSearch(alteredDocument, "/br-doc:BookingReport/nc30:Person[@s30:id='" + personRef + "']");
		assertNotNull(personNode);
		
		assertNotNull(XmlUtils.xPathNodeSearch(personNode, "nc30:PersonBirthDate"));
		assertNull(XmlUtils.xPathNodeSearch(personNode, "nc30:PersonName"));
		assertNull(XmlUtils.xPathNodeSearch(personNode, "jxdm51:PersonAugmentation/jxdm51:PersonStateFingerprintIdentification"));
		assertNull(XmlUtils.xPathNodeSearch(personNode, "nc30:PersonSSNIdentification"));
		assertNull(XmlUtils.xPathNodeSearch(alteredDocument, "/br-doc:BookingReport/nc30:Identity"));
		assertNull(XmlUtils.xPathNodeSearch(alteredDocument, "/br-doc:BookingReport/nc30:PersonAliasIdentityAssociation"));

		
	}
	
	
}
