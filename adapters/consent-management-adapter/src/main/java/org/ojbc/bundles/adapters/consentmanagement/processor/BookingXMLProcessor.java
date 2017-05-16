package org.ojbc.bundles.adapters.consentmanagement.processor;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.ojbc.bundles.adapters.consentmanagement.model.Consent;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

@Component
public class BookingXMLProcessor {
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	final 

	public Consent processBookingReport(Document doc) throws Exception
	{
		Consent consent = new Consent();
		
		String personFirstName = XmlUtils.xPathStringSearch(doc, "/br-doc:BookingReport/nc30:Person/nc30:PersonName/nc30:PersonGivenName");
		consent.setPersonFirstName(personFirstName);

		String personMiddleName = XmlUtils.xPathStringSearch(doc, "/br-doc:BookingReport/nc30:Person/nc30:PersonName/nc30:PersonMiddleName");
		consent.setPersonMiddleName(personMiddleName);

		String personLastName = XmlUtils.xPathStringSearch(doc, "/br-doc:BookingReport/nc30:Person/nc30:PersonName/nc30:PersonSurName");
		consent.setPersonLastName(personLastName);

		String personDob = XmlUtils.xPathStringSearch(doc, "/br-doc:BookingReport/nc30:Person/nc30:PersonBirthDate/nc30:Date");
		LocalDate localDate = LocalDate.parse(personDob, formatter);
		consent.setPersonDOB(localDate);
		
		String bookingNumber = XmlUtils.xPathStringSearch(doc, "/br-doc:BookingReport/jxdm51:Booking/jxdm51:BookingAgencyRecordIdentification/nc30:IdentificationID");
		consent.setBookingNumber(bookingNumber);

		String nameNumber = XmlUtils.xPathStringSearch(doc, "/br-doc:BookingReport/nc30:Person/nc30:PersonName/br-ext:PersonNameIdentification/nc30:IdentificationID");
		consent.setNameNumber(nameNumber);

		String personGender = XmlUtils.xPathStringSearch(doc, "/br-doc:BookingReport/nc30:Person/nc30:PersonSexText");
		consent.setPersonGender(personGender);
		
		return consent;
	}
	
}
