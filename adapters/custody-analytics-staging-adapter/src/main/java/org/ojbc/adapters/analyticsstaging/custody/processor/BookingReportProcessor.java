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
 * Copyright 2012-2015 Open Justice Broker Consortium
 */
package org.ojbc.adapters.analyticsstaging.custody.processor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class BookingReportProcessor extends AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( BookingReportProcessor.class );
	
	@Transactional
	public void processReport(Document report, String personUniqueIdentifier) throws Exception
	{
		log.info("Processing booking report." );
		XmlUtils.printNode(report);
		
		Integer bookingId = processBookingReport(report, personUniqueIdentifier);
		processBookingCharges(report, bookingId);
		
		log.info("Processed booking report.");
		
	}

	private void processBookingCharges(Document report, Integer bookingId) throws Exception {
		NodeList chargeNodes = XmlUtils.xPathNodeListSearch(report, "/br-doc:BookingReports/br-ext:BookingReport/jxdm51:Charge");
		
		List<BookingCharge> bookingCharges = new ArrayList<BookingCharge>();
		
		for (int i = 0; i < chargeNodes.getLength(); i++) {
			Node chargeNode = chargeNodes.item(i);
			
			BookingCharge bookingCharge = new BookingCharge();
			bookingCharge.setBookingId(bookingId);
			
			KeyValue chargeType = new KeyValue(); 
			chargeType.setValue( XmlUtils.xPathStringSearch(chargeNode, "jxdm51:ChargeCategoryDescriptionText"));
			chargeType.setKey(descriptionCodeLookupService.retrieveCode(CodeTable.ChargeType, chargeType.getValue()));
			bookingCharge.setChargeType(chargeType);
			
			setBondInfo(report, chargeNode, bookingCharge);
			bookingCharges.add(bookingCharge);
		}
		analyticalDatastoreDAO.saveBookingCharges(bookingCharges);
	}

	private void setBondInfo(Document report, Node chargeNode,
			BookingCharge bookingCharge) throws Exception {
		String chargeId = XmlUtils.xPathStringSearch(chargeNode, "@s30:id");
		String bondId = XmlUtils.xPathStringSearch(report, "/br-doc:BookingReports/br-ext:BookingReport/"
				+ "jxdm51:BailBondChargeAssociation[jxdm51:Charge/@s30:ref='" + chargeId + "']/jxdm51:BailBond/@s30:ref");
		
		if (StringUtils.isNotBlank(bondId)){
			Node bondNode = XmlUtils.xPathNodeSearch(report, 
					"/br-doc:BookingReports/br-ext:BookingReport/jxdm51:BailBond[@s30:id = '"+ bondId +  "']");
			
			String bondType = XmlUtils.xPathStringSearch(bondNode, "nc30:ActivityCategoryText");
			Integer bondTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.BondType, bondType);
			KeyValue keyValue = new KeyValue(bondTypeId, bondType);
			bookingCharge.setBondType(keyValue);
			
			String bondAmount = XmlUtils.xPathStringSearch(bondNode, "jxdm51:BailBondAmount/nc30:Amount");
			if (StringUtils.isNotBlank(bondAmount)){
				bookingCharge.setBondAmount(new BigDecimal(bondAmount));
			}
		}
	}

	@Transactional
	private Integer processBookingReport(Document report, String personUniqueIdentifier) throws Exception {
		Booking booking = new Booking();
		
		Node personNode = XmlUtils.xPathNodeSearch(report, "/br-doc:BookingReports/br-ext:BookingReport/nc30:Person");
        
        Integer bookingSubjectId = saveBookingSubject(personNode, personUniqueIdentifier);
        booking.setBookingSubjectId(bookingSubjectId);
        
        Node bookingReportNode = XmlUtils.xPathNodeSearch(report, "/br-doc:BookingReports/br-ext:BookingReport");
        
        String courtName = XmlUtils.xPathStringSearch(bookingReportNode, "nc30:Case/jxdm51:CaseAugmentation/jxdm51:CaseCourt/jxdm51:CourtName");
        Integer courtId = descriptionCodeLookupService.retrieveCode(CodeTable.Jurisdiction, courtName);
        booking.setJurisdictionId(courtId);
        
        String bookingReportDate = XmlUtils.xPathStringSearch(bookingReportNode, "nc30:DocumentCreationDate/nc30:DateTime");
        booking.setBookingReportDate(LocalDateTime.parse(bookingReportDate));
        
        String bookingReportId = XmlUtils.xPathStringSearch(bookingReportNode, "nc30:DocumentIdentification/nc30:IdentificationID");
        booking.setBookingReportId(bookingReportId);
        
        String sendingAgency = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/br-ext:HoldForAgency/nc30:OrganizationName");
        Integer sendingAgencyId = descriptionCodeLookupService.retrieveCode(CodeTable.Agency, sendingAgency);
        booking.setSendingAgencyId(sendingAgencyId);
        
        String caseStatus = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/nc30:SupervisionCustodyStatus/nc30:StatusDescriptionText");
        Integer caseStatusId = descriptionCodeLookupService.retrieveCode(CodeTable.CaseStatus, caseStatus);
        booking.setCaseStatusId(caseStatusId);
        
        String bookingDate = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Booking/nc30:ActivityDate/nc30:DateTime");
        booking.setBookingDate(LocalDateTime.parse(bookingDate));

        String commitDate = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/nc30:ActivityDate/nc30:Date");
        booking.setCommitDate(LocalDate.parse(commitDate));
        
        String supervisionReleaseDate = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionReleaseDate/nc30:DateTime");
        if (StringUtils.isNotBlank(supervisionReleaseDate)){
        	booking.setSupervisionReleaseDate(LocalDateTime.parse(supervisionReleaseDate));
        }
        
        String pretrialStatus = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/nc30:SupervisionCustodyStatus/ac-bkg-codes:PreTrialCategoryCode");
        Integer pretrialStatusId = descriptionCodeLookupService.retrieveCode(CodeTable.PretrialStatus, pretrialStatus);
        booking.setPretrialStatusId(pretrialStatusId);
        
        String facility = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Booking/jxdm51:BookingDetentionFacility/nc30:FacilityIdentification/nc30:IdentificationID");
        Integer facilityId = descriptionCodeLookupService.retrieveCode(CodeTable.Facility, facility);
        booking.setFacilityId(facilityId);
        
        String bedType = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionBedIdentification/ac-bkg-codes:BedCategoryCode");
        Integer bedTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.BedType, bedType);
        booking.setBedTypeId(bedTypeId);
        
        Integer bookingId = analyticalDatastoreDAO.saveBooking(booking);
		return bookingId;
	}

	private Integer saveBookingSubject(Node personNode, String personUniqueIdentifier) throws Exception {
		BookingSubject bookingSubject = new BookingSubject();

		Integer personId = analyticalDatastoreDAO.getPersonIdByUniqueId(personUniqueIdentifier);

		if (personId != null){
			bookingSubject.setRecidivistIndicator(1);
		}
		else{
			personId = savePerson(personNode, personUniqueIdentifier);
		}
		
		bookingSubject.setPersonId(personId);
		String bookingNumber = XmlUtils.xPathStringSearch(personNode, "parent::br-ext:BookingReport/jxdm51:Booking/jxdm51:BookingSubject/jxdm51:SubjectIdentification/nc30:IdentificationID");
		bookingSubject.setBookingNumber(bookingNumber);
		
		String birthDateString = XmlUtils.xPathStringSearch(personNode,  "nc30:PersonBirthDate/nc30:Date");
	 	java.time.LocalDate birthDay = java.time.LocalDate.parse(birthDateString);
	 	long age = birthDay.until(java.time.LocalDate.now(), ChronoUnit.YEARS );
	 	bookingSubject.setPersonAge(Long.valueOf(age).intValue());
	 	
	 	String educationLevel = XmlUtils.xPathStringSearch(personNode, "nc30:PersonEducationLevelText");
	 	if(StringUtils.isNotBlank(educationLevel)){
		 	Integer educationLevelId = descriptionCodeLookupService.retrieveCode(CodeTable.EducationLevel, StringUtils.trim(educationLevel));
		 	bookingSubject.setEducationLevelId(educationLevelId);
	 	}
	 	
	 	String occupation = XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonAugmentation/nc30:EmployeeOccupationCategoryText");
	 	if (StringUtils.isNotBlank(occupation)){
	 		Integer occupationId = descriptionCodeLookupService.retrieveCode(CodeTable.Occupation, StringUtils.trim(occupation));
	 		bookingSubject.setOccupationId(occupationId);
	 	}
	 	
	 	String incomeLevel = XmlUtils.xPathStringSearch(personNode, "br-ext:PersonSocioEconomicStatusDescriptionText");
	 	if (StringUtils.isNotBlank(incomeLevel)){
	 		Integer incomeLevelId = descriptionCodeLookupService.retrieveCode(CodeTable.IncomeLevel, StringUtils.trim(incomeLevel));
	 		bookingSubject.setIncomeLevelId(incomeLevelId);
	 	}
	 	
	 	String housingStatus = XmlUtils.xPathStringSearch(personNode, "nc30:PersonResidentText");
	 	if(StringUtils.isNotBlank( housingStatus )){
	 		Integer housingStatusId = descriptionCodeLookupService.retrieveCode(CodeTable.HousingStatus, StringUtils.trim(housingStatus));
	 		bookingSubject.setHousingStatusId(housingStatusId);
	 	}
	 	
		Integer bookingSubjectId = analyticalDatastoreDAO.saveBookingSubject(bookingSubject);
		return bookingSubjectId;
	}

	private Integer savePerson(Node personNode, String personUniqueIdentifier) throws Exception {
		
		Person person = new Person();
		
		person.setPersonUniqueIdentifier(personUniqueIdentifier);
		
		String personRace=XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonRaceCode");
		person.setPersonRaceDescription(personRace);
		person.setPersonRaceID(descriptionCodeLookupService.retrieveCode(CodeTable.PersonRace, personRace));
		
		String personSex=XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonSexCode");
		person.setPersonSexDescription(personSex);
		person.setPersonSexID(descriptionCodeLookupService.retrieveCode(CodeTable.PersonSex, personSex));
		
		String personBirthDate = XmlUtils.xPathStringSearch(personNode, "nc30:PersonBirthDate/nc30:Date");
		person.setPersonBirthDate(LocalDate.parse(personBirthDate));
		
		String language = XmlUtils.xPathStringSearch(personNode, "nc30:PersonPrimaryLanguage/nc30:LanguageName");
		person.setLanguageId(descriptionCodeLookupService.retrieveCode(CodeTable.Language, language));
		
		Integer personId = analyticalDatastoreDAO.savePerson(person);
		
		return personId;
	}

}
