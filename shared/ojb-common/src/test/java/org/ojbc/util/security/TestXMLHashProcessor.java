package org.ojbc.util.security;

import static org.junit.Assert.*;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Test;
import org.ojbc.util.helper.Hash;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class TestXMLHashProcessor {

	@Test
	public void testHashandSaltXMLElement() throws Exception
	{
		//Read file from file system
		File fXmlFile = new File("src/test/resources/xml/BookingReport-Adams.xml");
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		dbFactory.setNamespaceAware(true);
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		Document xmlDocument = dBuilder.parse(fXmlFile);
		
		assertNotNull(xmlDocument);
		//XmlUtils.printNode(xmlDocument);

		//Retrieve booking number from XML document
		String xPath = "/br-doc:BookingReport/jxdm51:Booking/jxdm51:BookingAgencyRecordIdentification/nc30:IdentificationID";
		String bookingNumber = XmlUtils.xPathStringSearch(xmlDocument, xPath);
		assertEquals("Booking Number", bookingNumber);

		//Hash the value in the XML document
		XmlHashProcessor hashProcessor = new XmlHashProcessor();
		hashProcessor.setSalt("salty");
		hashProcessor.hashXMLandSaltElement(xmlDocument, xPath,"SHA-256");
		
		//Get the hashed value using Xpath
		bookingNumber = XmlUtils.xPathStringSearch(xmlDocument, xPath);
		assertEquals("52e575df62f6b86d8c2c897ed34a4aa18d706938487a3bf7866b0fe3079fc31d", bookingNumber);

		//Hash the booking number directly without using Xpaths and confirm the values are the same.
		String hashedValeuDirect = Hash.sha256WithSalt("Booking Number", "salty");
		assertEquals(hashedValeuDirect, bookingNumber);
	}
	
}
