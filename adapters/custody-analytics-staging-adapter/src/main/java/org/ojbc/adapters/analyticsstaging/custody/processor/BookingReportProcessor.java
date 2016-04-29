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
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyRelease;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
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
		NodeList chargeNodes = XmlUtils.xPathNodeListSearch(report, "/br-doc:BookingReport/jxdm51:Charge");
		
		List<BookingCharge> bookingCharges = new ArrayList<BookingCharge>();
		
		for (int i = 0; i < chargeNodes.getLength(); i++) {
			Node chargeNode = chargeNodes.item(i);
			
			BookingCharge bookingCharge = new BookingCharge();
			bookingCharge.setBookingId(bookingId);
			
			KeyValue chargeType = new KeyValue(); 
			chargeType.setValue( XmlUtils.xPathStringSearch(chargeNode, "jxdm51:ChargeCategoryDescriptionText"));
			chargeType.setKey(descriptionCodeLookupService.retrieveCode(CodeTable.ChargeType, chargeType.getValue()));
			bookingCharge.setChargeType(chargeType);
			
			bookingCharges.add(bookingCharge);
		}
		analyticalDatastoreDAO.saveBookingCharges(bookingCharges);
	}

	private void setBondInfo(Document report, Booking booking) throws Exception {
		
		String bondId = XmlUtils.xPathStringSearch(report, "/br-doc:BookingReport/"
				+ "jxdm51:BailBondChargeAssociation/jxdm51:BailBond/@s30:ref");
		
		if (StringUtils.isNotBlank(bondId)){
			Node bondNode = XmlUtils.xPathNodeSearch(report, 
					"/br-doc:BookingReport/jxdm51:BailBond[@s30:id = '"+ bondId +  "']");
			
			String bondType = XmlUtils.xPathStringSearch(bondNode, "nc30:ActivityCategoryText");
			Integer bondTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.BondType, bondType);
			KeyValue keyValue = new KeyValue(bondTypeId, bondType);
			booking.setBondType(keyValue);
			
			String bondAmount = XmlUtils.xPathStringSearch(bondNode, "jxdm51:BailBondAmount/nc30:Amount");
			if (StringUtils.isNotBlank(bondAmount)){
				booking.setBondAmount(new BigDecimal(bondAmount));
			}
		}
	}

	@Transactional
	private Integer processBookingReport(Document report, String personUniqueIdentifier) throws Exception {
		Booking booking = new Booking();
		
		Node personNode = XmlUtils.xPathNodeSearch(report, "/br-doc:BookingReport/nc30:Person");
        
        Integer bookingSubjectId = saveBookingSubject(personNode, personUniqueIdentifier);
        booking.setBookingSubjectId(bookingSubjectId);
        
        Node bookingReportNode = XmlUtils.xPathNodeSearch(report, "/br-doc:BookingReport");
        
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
        
        String pretrialStatus = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/nc30:SupervisionCustodyStatus/ac-bkg-codes:PreTrialCategoryCode");
        Integer pretrialStatusId = descriptionCodeLookupService.retrieveCode(CodeTable.PretrialStatus, pretrialStatus);
        booking.setPretrialStatusId(pretrialStatusId);
        
        String facility = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Booking/jxdm51:BookingDetentionFacility/nc30:FacilityIdentification/nc30:IdentificationID");
        Integer facilityId = descriptionCodeLookupService.retrieveCode(CodeTable.Facility, facility);
        booking.setFacilityId(facilityId);
        
        String bedType = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionBedIdentification/ac-bkg-codes:BedCategoryCode");
        Integer bedTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.BedType, bedType);
        booking.setBedTypeId(bedTypeId);
        
		String bookingNumber = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Booking/jxdm51:BookingSubject/jxdm51:SubjectIdentification/nc30:IdentificationID");
		booking.setBookingNumber(bookingNumber);
		

        String locationId = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Arrest/jxdm51:ArrestLocation/@s30:ref");
        if (StringUtils.isNotBlank(locationId)){
        	Node arrestLocation2DGeoCoordinateNode = XmlUtils.xPathNodeSearch(bookingReportNode, "nc30:Location[@s30:id='"+ locationId +"']/nc30:Location2DGeospatialCoordinate");
        	
        	if (arrestLocation2DGeoCoordinateNode != null){
        		String arrestLocationLongitude = XmlUtils.xPathStringSearch(arrestLocation2DGeoCoordinateNode, "nc30:GeographicCoordinateLongitude/nc30:LongitudeDegreeValue");
        		booking.setArrestLocationLongitude(new BigDecimal(arrestLocationLongitude));
        		
        		String arrestLocationLatitude = XmlUtils.xPathStringSearch(arrestLocation2DGeoCoordinateNode, "nc30:GeographicCoordinateLatitude/nc30:LatitudeDegreeValue");
        		booking.setArrestLocationLatitude(new BigDecimal(arrestLocationLatitude));
        	}
        }
        
		setBondInfo(report, booking);

        String supervisionReleaseDate = XmlUtils.xPathStringSearch(bookingReportNode, 
        		"jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionReleaseDate/nc30:DateTime");
        if (StringUtils.isNotBlank(supervisionReleaseDate)){
        	CustodyRelease custodyRelease = new CustodyRelease();
        	custodyRelease.setReleaseDate(LocalDateTime.parse(supervisionReleaseDate));
        	custodyRelease.setBookingNumber(bookingNumber);
        	custodyRelease.setReportDate(booking.getBookingReportDate());
        	analyticalDatastoreDAO.saveCustodyRelease(custodyRelease);
        }
        
        Integer bookingId = analyticalDatastoreDAO.saveBooking(booking);
		return bookingId;
	}

}
