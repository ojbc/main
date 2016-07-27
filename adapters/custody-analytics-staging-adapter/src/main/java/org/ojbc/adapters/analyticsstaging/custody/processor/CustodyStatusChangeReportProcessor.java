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
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChange;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChangeCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class CustodyStatusChangeReportProcessor extends AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( CustodyStatusChangeReportProcessor.class );
	
	@Transactional
	public void processReport(Document report) throws Exception
	{
		log.info("Processing custody status change report." );
		XmlUtils.printNode(report);
		
		Integer custodyStatusChangeId = processCustodyStatusChangeReport(report);
		processCustodyStatusChangeCharges(report, custodyStatusChangeId);
		
		log.info("Processed custody status change report successfully.");
		
	}

	private void processCustodyStatusChangeCharges(Document report, Integer custodyStatusChangeId) throws Exception {
		NodeList chargeNodes = XmlUtils.xPathNodeListSearch(report, "/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/jxdm51:Charge");
		
		List<CustodyStatusChangeCharge> custodyStatusChangeCharges = new ArrayList<CustodyStatusChangeCharge>();
		
		for (int i = 0; i < chargeNodes.getLength(); i++) {
			Node chargeNode = chargeNodes.item(i);
			
			CustodyStatusChangeCharge custodyStatusChangeCharge = new CustodyStatusChangeCharge();
			custodyStatusChangeCharge.setCustodyStatusChangeId(custodyStatusChangeId);
			
			KeyValue chargeType = new KeyValue(); 
			chargeType.setValue( StringUtils.trimToEmpty(XmlUtils.xPathStringSearch(chargeNode, "jxdm51:ChargeCategoryDescriptionText")));
			chargeType.setKey(descriptionCodeLookupService.retrieveCode(CodeTable.ChargeType, chargeType.getValue()));
			custodyStatusChangeCharge.setChargeType(chargeType);
			
			custodyStatusChangeCharges.add(custodyStatusChangeCharge);
		}
		analyticalDatastoreDAO.saveCustodyStatusChangeCharges(custodyStatusChangeCharges);
	}

	private void setBondInfo(Document report, CustodyStatusChange custodyStatusChange) throws Exception {
		
		String bondId = XmlUtils.xPathStringSearch(report, "/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/"
				+ "jxdm51:BailBondChargeAssociation/jxdm51:BailBond/@s30:ref");
		
		if (StringUtils.isNotBlank(bondId)){
			Node bondNode = XmlUtils.xPathNodeSearch(report, 
					"//cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/jxdm51:BailBond[@s30:id = '"+ bondId +  "']");
			
			String bondType = XmlUtils.xPathStringSearch(bondNode, "nc30:ActivityCategoryText");
			Integer bondTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.BondType, bondType);
			KeyValue keyValue = new KeyValue(bondTypeId, bondType);
			custodyStatusChange.setBondType(keyValue);
			
			String bondAmount = XmlUtils.xPathStringSearch(bondNode, "jxdm51:BailBondAmount/nc30:Amount");
			if (StringUtils.isNotBlank(bondAmount)){
				custodyStatusChange.setBondAmount(new BigDecimal(bondAmount));
			}
		}
	}

	@Transactional
	private Integer processCustodyStatusChangeReport(Document report) throws Exception {
		CustodyStatusChange custodyStatusChange = new CustodyStatusChange();
		
		Node personNode = XmlUtils.xPathNodeSearch(report, "/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/nc30:Person");
        
        Node custodyNode = XmlUtils.xPathNodeSearch(report, "/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody");
		String bookingNumber = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Booking/jxdm51:BookingSubject/jxdm51:BookingAgencyRecordIdentification/nc30:IdentificationID");
		custodyStatusChange.setBookingNumber(bookingNumber);
		
        Integer bookingSubjectId = processBookingSubjectAndBehavioralHealthInfo(personNode, bookingNumber);
        custodyStatusChange.setBookingSubjectId(bookingSubjectId);
        
        
        String courtName = XmlUtils.xPathStringSearch(custodyNode, "nc30:Case/jxdm51:CaseAugmentation/jxdm51:CaseCourt/jxdm51:CourtName");
        Integer courtId = descriptionCodeLookupService.retrieveCode(CodeTable.Jurisdiction, courtName);
        custodyStatusChange.setJurisdictionId(courtId);
        
        String reportDate = XmlUtils.xPathStringSearch(report, "/cscr-doc:CustodyStatusChangeReport/nc30:DocumentCreationDate/nc30:DateTime");
        custodyStatusChange.setReportDate(LocalDateTime.parse(reportDate));
        
        String reportId = XmlUtils.xPathStringSearch(report, "/cscr-doc:CustodyStatusChangeReport/nc30:DocumentIdentification/nc30:IdentificationID");
        custodyStatusChange.setReportId(reportId);
        
        String sendingAgency = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Detention/cscr-ext:HoldForAgency/nc30:OrganizationName");
        Integer sendingAgencyId = descriptionCodeLookupService.retrieveCode(CodeTable.Agency, sendingAgency);
        custodyStatusChange.setSendingAgencyId(sendingAgencyId);
        
        String caseStatus = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Detention/nc30:SupervisionCustodyStatus/nc30:StatusDescriptionText");
        Integer caseStatusId = descriptionCodeLookupService.retrieveCode(CodeTable.CaseStatus, caseStatus);
        custodyStatusChange.setCaseStatusId(caseStatusId);
        
        String bookingDate = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Booking/nc30:ActivityDate/nc30:DateTime");
        custodyStatusChange.setBookingDate(LocalDateTime.parse(bookingDate));

        String commitDate = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Detention/nc30:ActivityDate/nc30:Date");
        custodyStatusChange.setCommitDate(LocalDate.parse(commitDate));
        
        String pretrialStatus = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Detention/nc30:SupervisionCustodyStatus/ac-bkg-codes:PreTrialCategoryCode");
        Integer pretrialStatusId = descriptionCodeLookupService.retrieveCode(CodeTable.PretrialStatus, pretrialStatus);
        custodyStatusChange.setPretrialStatusId(pretrialStatusId);
        
        String facility = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Booking/jxdm51:BookingDetentionFacility/nc30:FacilityIdentification/nc30:IdentificationID");
        Integer facilityId = descriptionCodeLookupService.retrieveCode(CodeTable.Facility, facility);
        custodyStatusChange.setFacilityId(facilityId);
        
        String bedType = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionBedIdentification/ac-bkg-codes:BedCategoryCode");
        Integer bedTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.BedType, bedType);
        custodyStatusChange.setBedTypeId(bedTypeId);
        
        String locationId = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Arrest/jxdm51:ArrestLocation/@s30:ref");
        if (StringUtils.isNotBlank(locationId)){
        	Node arrestLocation2DGeoCoordinateNode = XmlUtils.xPathNodeSearch(custodyNode, "nc30:Location[@s30:id='"+ locationId +"']/nc30:Location2DGeospatialCoordinate");
        	
        	if (arrestLocation2DGeoCoordinateNode != null){
        		String arrestLocationLongitude = XmlUtils.xPathStringSearch(arrestLocation2DGeoCoordinateNode, "nc30:GeographicCoordinateLongitude/nc30:LongitudeDegreeValue");
        		custodyStatusChange.setArrestLocationLongitude(new BigDecimal(arrestLocationLongitude));
        		
        		String arrestLocationLatitude = XmlUtils.xPathStringSearch(arrestLocation2DGeoCoordinateNode, "nc30:GeographicCoordinateLatitude/nc30:LatitudeDegreeValue");
        		custodyStatusChange.setArrestLocationLatitude(new BigDecimal(arrestLocationLatitude));
        	}
        }
        
		setBondInfo(report, custodyStatusChange);

        Integer custodyStatusChangeId = analyticalDatastoreDAO.saveCustodyStatusChange(custodyStatusChange);
		return custodyStatusChangeId;
	}

	private Integer processBookingSubjectAndBehavioralHealthInfo(Node personNode, String bookingNumber) throws Exception {
		
		String personUniqueIdentifier = getPersonUniqueIdentifier(personNode, "cscr-ext:PersonPersistentIdentification/nc30:IdentificationID");
		
		BookingSubject bookingSubject = new BookingSubject();
		
		Integer personId = analyticalDatastoreDAO.getPersonIdByUniqueId(personUniqueIdentifier);

		if (personId != null){
			BookingSubject formerBookingSubject = analyticalDatastoreDAO.getBookingSubjectByBookingNumberAndPersonId(bookingNumber, personId); 
			
			if (formerBookingSubject != null){
				bookingSubject.setRecidivistIndicator(formerBookingSubject.getRecidivistIndicator());
			}
			else{
				bookingSubject.setRecidivistIndicator(1);
			}
		}
		else{
			personId = savePerson(personNode, personUniqueIdentifier);
		}
		
		return saveBookingSubject(personNode, bookingSubject, personId);
	}

}
