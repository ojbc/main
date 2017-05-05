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
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Address;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChange;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChangeArrest;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyStatusChangeCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.util.helper.OJBCDateUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Component
public class CustodyStatusChangeReportProcessor extends AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( CustodyStatusChangeReportProcessor.class );
	
	@Transactional(rollbackFor=Exception.class)
	public void processReport(Document report) throws Exception
	{
		log.info("Processing custody status change report." );
		XmlUtils.printNode(report);
		
		Integer custodyStatusChangeId = processCustodyStatusChangeReport(report);
		processCustodyStatusChangeArrests(report, custodyStatusChangeId);
		
		log.info("Processed custody status change report successfully.");
		
	}

	private void processCustodyStatusChangeArrests(Document report, Integer custodyStatusChangeId) throws Exception {
		NodeList locationNodes = XmlUtils.xPathNodeListSearch(report, 
				"/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/nc30:Location[@s30:id = /cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody//jxdm51:Arrest/jxdm51:ArrestLocation/@s30:ref]");
		
		Map<String, Integer> addressMap = constructAddressMap(locationNodes);
		
		NodeList arrestNodes = XmlUtils.xPathNodeListSearch(report, 
				"/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/jxdm51:Arrest[@s30:id = preceding-sibling::jxdm51:Booking/jxdm51:Arrest/@s30:ref]");
		
		for (int i = 0; i < arrestNodes.getLength(); i++) {
			Node arrestNode = arrestNodes.item(i);
			Integer custodyStatusChangeArrestId = processCustodyStatusChangeArrest(arrestNode, custodyStatusChangeId, addressMap);
			processCustodyStatusChangeCharges(arrestNode, custodyStatusChangeArrestId);
		}
	}
	
	private Integer processCustodyStatusChangeArrest(Node arrestNode, Integer custodyStatusChangeId, Map<String, Integer> addressMap) throws Exception {
		CustodyStatusChangeArrest custodyStatusChangeArrest = new CustodyStatusChangeArrest();
		custodyStatusChangeArrest.setCustodyStatusChangeId(custodyStatusChangeId);;

		String locationRef = XmlUtils.xPathStringSearch(arrestNode, "jxdm51:ArrestLocation/@s30:ref");
		custodyStatusChangeArrest.setAddress(new Address(addressMap.get(locationRef)));
        
		String arrestAgency = XmlUtils.xPathStringSearch(arrestNode, "jxdm51:ArrestAgency/nc30:OrganizationName");
		custodyStatusChangeArrest.setArrestAgencyId(descriptionCodeLookupService.retrieveCode(CodeTable.Agency,arrestAgency));
		
        Integer custodyStatusChangeArrestId = analyticalDatastoreDAO.saveCustodyStatusChangeArrest(custodyStatusChangeArrest);
		return custodyStatusChangeArrestId;
	}

	
	private void processCustodyStatusChangeCharges(Node arrestNode, Integer custodyStatusChangeArrestId) throws Exception {
		
		NodeList chargeRefNodes = XmlUtils.xPathNodeListSearch(arrestNode, "jxdm51:ArrestCharge/@s30:ref");
		
		List<CustodyStatusChangeCharge> custodyStatusChangeCharges = new ArrayList<CustodyStatusChangeCharge>();
		
		for (int i = 0; i < chargeRefNodes.getLength(); i++) {
			Attr chargeRefNode = (Attr) chargeRefNodes.item(i);
			String chargeRef = chargeRefNode.getValue();
			
			Node chargeNode = XmlUtils.xPathNodeSearch(arrestNode,  
					"parent::cscr-ext:Custody/jxdm51:Charge[@s30:id = '" + chargeRef + "']");

			CustodyStatusChangeCharge custodyStatusChangeCharge = new CustodyStatusChangeCharge();
			custodyStatusChangeCharge.setCustodyStatusChangeArrestId(custodyStatusChangeArrestId);
		
	        String sendingAgency = XmlUtils.xPathStringSearch(chargeNode, "cscr-ext:HoldForAgency/nc30:OrganizationName");
	        custodyStatusChangeCharge.setAgencyId(descriptionCodeLookupService.retrieveCode(CodeTable.Agency,sendingAgency));
	        
	        custodyStatusChangeCharge.setChargeCode(XmlUtils.xPathStringSearch(chargeNode, 
	        		"jxdm51:ChargeStatute/jxdm51:StatuteCodeSectionIdentification/nc30:IdentificationID"));
	        custodyStatusChangeCharge.setChargeDisposition(XmlUtils.xPathStringSearch(chargeNode, "jxdm51:ChargeDisposition/nc30:DispositionText"));

	        String chargeClassType = XmlUtils.xPathStringSearch(chargeNode, "jxdm51:ChargeSeverityText");
	        custodyStatusChangeCharge.setChargeClassTypeId(descriptionCodeLookupService.retrieveCode(CodeTable.ChargeClassType, chargeClassType));
			
			String chargeJurisdictionType = XmlUtils.xPathStringSearch(chargeNode, "cscr-ext:ChargeJurisdictionCourt/jxdm51:CourtName");
			custodyStatusChangeCharge.setChargeJurisdictionTypeId((descriptionCodeLookupService.retrieveCode(CodeTable.JurisdictionType, chargeJurisdictionType)));
			
			setBondInfo(chargeNode, custodyStatusChangeCharge);

			custodyStatusChangeCharges.add(custodyStatusChangeCharge);
		}
		analyticalDatastoreDAO.saveCustodyStatusChangeCharges(custodyStatusChangeCharges);
	}

	private void setBondInfo(Node chargeNode, CustodyStatusChangeCharge custodyStatusChangeCharge) throws Exception {
		
		String chargeRef = XmlUtils.xPathStringSearch(chargeNode, "@s30:id");
		
		String bondId = XmlUtils.xPathStringSearch(chargeNode, "following-sibling::"
				+ "jxdm51:BailBondChargeAssociation[jxdm51:Charge/@s30:ref='"+ chargeRef + "']/jxdm51:BailBond/@s30:ref");
		
		if (StringUtils.isNotBlank(bondId)){
			Node bondNode = XmlUtils.xPathNodeSearch(chargeNode, 
					"preceding-sibling::jxdm51:BailBond[@s30:id = '"+ bondId +  "']");
			
			String bondType = XmlUtils.xPathStringSearch(bondNode, "nc30:ActivityCategoryText");
			Integer bondTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.BondType, bondType);
			KeyValue keyValue = new KeyValue(bondTypeId, bondType);
			custodyStatusChangeCharge.setBondType(keyValue);
			
			String bondAmount = XmlUtils.xPathStringSearch(bondNode, "jxdm51:BailBondAmount/nc30:Amount");
			if (StringUtils.isNotBlank(bondAmount)){
				custodyStatusChangeCharge.setBondAmount(new BigDecimal(bondAmount));
			}
			
			String bondStatusType = XmlUtils.xPathStringSearch(bondNode, "nc30:ActivityStatus/nc30:StatusDescriptionText");
			custodyStatusChangeCharge.setBondStatusTypeId(descriptionCodeLookupService.retrieveCode(CodeTable.BondStatusType, bondStatusType));
			
		}
	}

	@Transactional
	private Integer processCustodyStatusChangeReport(Document report) throws Exception {
		CustodyStatusChange custodyStatusChange = new CustodyStatusChange();
		
		Node personNode = XmlUtils.xPathNodeSearch(report, "/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody/nc30:Person");
        
        Node custodyNode = XmlUtils.xPathNodeSearch(report, "/cscr-doc:CustodyStatusChangeReport/cscr-ext:Custody");
		String bookingNumber = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Booking/jxdm51:BookingAgencyRecordIdentification/nc30:IdentificationID");
		custodyStatusChange.setBookingNumber(bookingNumber);
		
		Integer bookingId = getBookingIdByBookingNumber(bookingNumber);
		custodyStatusChange.setBookingId(bookingId);
		
        Integer personId = processPersonAndBehavioralHealthInfo(personNode, bookingNumber);
        custodyStatusChange.setPersonId(personId);
        
        String bookingDateTimeString = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Booking/nc30:ActivityDate/nc30:DateTime");
        LocalDateTime bookingDateTime = OJBCDateUtils.parseLocalDateTime(bookingDateTimeString);
        
        if (bookingDateTime != null){
        	custodyStatusChange.setBookingDate( bookingDateTime.toLocalDate());
        	custodyStatusChange.setBookingTime( bookingDateTime.toLocalTime());
        }
        else{
            String bookingDateString = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Booking/nc30:ActivityDate/nc30:Date");
        	custodyStatusChange.setBookingDate(OJBCDateUtils.parseLocalDate(bookingDateString));
        }

        String facility = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Booking/jxdm51:BookingDetentionFacility/nc30:FacilityIdentification/nc30:IdentificationID");
        Integer facilityId = descriptionCodeLookupService.retrieveCode(CodeTable.Facility, facility);
        custodyStatusChange.setFacilityId(facilityId);
        
        String supervisionUnitType = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionAreaIdentification/nc30:IdentificationID");
        Integer supervisionUnitTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.SupervisionUnitType, supervisionUnitType);
        custodyStatusChange.setSupervisionUnitTypeId(supervisionUnitTypeId);
        
		String supervisionReleaseEligibilityDate = XmlUtils.xPathStringSearch(custodyNode, 
        		"jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionReleaseEligibilityDate/nc30:Date");
		custodyStatusChange.setScheduledReleaseDate(OJBCDateUtils.parseLocalDate(supervisionReleaseEligibilityDate));
		
 		String inmateJailResidentIndicator = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Detention/cscr-ext:InmateJailResidentIndicator");
 		custodyStatusChange.setInmateJailResidentIndicator(BooleanUtils.toBooleanObject(inmateJailResidentIndicator));
 		
 		String inmateCurrentLocation = XmlUtils.xPathStringSearch(custodyNode, "jxdm51:Booking/jxdm51:BookingSubject/cscr-ext:SubjectLocationStatus/nc30:StatusDescriptionText");
 		custodyStatusChange.setInmateCurrentLocation(inmateCurrentLocation);
 		
        processCustodyReleaseInfo(custodyNode, bookingId, bookingNumber);

        Integer custodyStatusChangeId = analyticalDatastoreDAO.saveCustodyStatusChange(custodyStatusChange);
        
		return custodyStatusChangeId;
	}

	private Integer processPersonAndBehavioralHealthInfo(Node personNode, String bookingNumber) throws Exception {
		
		String personUniqueIdentifier = getPersonUniqueIdentifier(personNode, "cscr-ext:PersonPersistentIdentification/nc30:IdentificationID");
		
		Integer personId = savePerson(personNode, personUniqueIdentifier, "cscr-ext");
		
		processBehavioralHealthInfo(personNode, personId, "cscr-ext");

		return personId;
	}

}
