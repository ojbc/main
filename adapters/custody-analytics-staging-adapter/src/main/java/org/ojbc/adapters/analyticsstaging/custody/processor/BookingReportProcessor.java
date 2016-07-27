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

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Booking;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingArrest;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingCharge;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyRelease;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PrescribedMedication;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Treatment;
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
	public void processReport(Document report) throws Exception
	{
		log.info("Processing booking report." );
		XmlUtils.printNode(report);
		
		Integer bookingId = processBookingReport(report);
		processBookingArrests(report, bookingId);
		processBookingCharges(report, bookingId);
		
		log.info("Processed booking report.");
		
	}

	private void processBookingArrests(Document report, Integer bookingId) throws Exception {
		NodeList arrestRefNodes = XmlUtils.xPathNodeListSearch(report, "/br-doc:BookingReport/jxdm51:Booking/jxdm51:Arrest");
		
		for (int i = 0; i < arrestRefNodes.getLength(); i++) {
			Node arrestRefNode = arrestRefNodes.item(i);
			String arrestRef = XmlUtils.xPathStringSearch(arrestRefNode, "@s30:ref");
			
			Node arrestNode = XmlUtils.xPathNodeSearch(report,  "/br-doc:BookingReport/jxdm51:Arrest[@s30:id = '" + arrestRef + "']");
			
			if (arrestNode != null){
				Integer bookingArrestId = processBookingArrest(arrestNode, bookingId);
				processBookingCharges(arrestNode, bookingArrestId);
			}
		}
	}

	private Integer processBookingArrest(Node arrestNode, Integer bookingId) throws Exception {
		BookingArrest bookingArrest = new BookingArrest();
		bookingArrest.setBookingId(bookingId);
		
		String locationRef = XmlUtils.xPathStringSearch(arrestNode, "jxdm51:ArrestLocation/@s30:ref");
		Node locationNode = XmlUtils.xPathNodeSearch(arrestNode, 
				"parent::br-doc:BookingReport/nc30:Location[@s30:id = '" + locationRef +"']");
		
		if (locationNode != null){
			String streetNumber = XmlUtils.xPathStringSearch(locationNode, "nc30:Address/nc30:LocationStreet/nc30:StreetNumberText");
			bookingArrest.setStreetNumber(streetNumber);
			
			String streetName = XmlUtils.xPathStringSearch(locationNode, "nc30:Address/nc30:LocationStreet/nc30:StreetName");
			bookingArrest.setStreetName(streetName);
			
			String city = XmlUtils.xPathStringSearch(locationNode, "nc30:Address/nc30:LocationCityName");
			bookingArrest.setCity(city);
			
			String state = XmlUtils.xPathStringSearch(locationNode, "nc30:Address/nc30:LocationStateUSPostalServiceCode");
			bookingArrest.setState(state);
			
			String postalcode = XmlUtils.xPathStringSearch(locationNode, "nc30:Address/nc30:LocationPostalCode");
			bookingArrest.setPostalcode(postalcode);
			
        	Node arrestLocation2DGeoCoordinateNode = XmlUtils.xPathNodeSearch(locationNode, "nc30:Location2DGeospatialCoordinate");
	        	
        	if (arrestLocation2DGeoCoordinateNode != null){
        		String arrestLocationLongitude = XmlUtils.xPathStringSearch(arrestLocation2DGeoCoordinateNode, "nc30:GeographicCoordinateLongitude/nc30:LongitudeDegreeValue");
        		bookingArrest.setArrestLocationLongitude(new BigDecimal(arrestLocationLongitude));
        		
        		String arrestLocationLatitude = XmlUtils.xPathStringSearch(arrestLocation2DGeoCoordinateNode, "nc30:GeographicCoordinateLatitude/nc30:LatitudeDegreeValue");
        		bookingArrest.setArrestLocationLatitude(new BigDecimal(arrestLocationLatitude));
	        }
	        
		}
		
        Integer bookingArrestId = analyticalDatastoreDAO.saveBookingArrest(bookingArrest);
		return bookingArrestId;
	}

	private void processBookingCharges(Node arrestNode, Integer bookingArrestId) throws Exception {
		NodeList chargeRefNodes = XmlUtils.xPathNodeListSearch(arrestNode, "jxdm51:ArrestCharge");
		
		List<BookingCharge> bookingCharges = new ArrayList<BookingCharge>();
		for (int i=0; i<chargeRefNodes.getLength(); i++){
			Node chargeRefNode = chargeRefNodes.item(i);
			
			String chargeRef = XmlUtils.xPathStringSearch(chargeRefNode, "@s30:ref");
			Node chargeNode = XmlUtils.xPathNodeSearch(arrestNode,  "parent::br-doc:BookingReport/jxdm51:Charge[@s30:id = '" + chargeRef + "']");
			
			if (chargeNode != null){
				BookingCharge bookingCharge = new BookingCharge();
				bookingCharge.setBookingArrestId(bookingArrestId);
				
		        String sendingAgency = XmlUtils.xPathStringSearch(chargeNode, "br-ext:HoldForAgency/nc30:OrganizationName");
		        bookingCharge.setAgencyId(descriptionCodeLookupService.retrieveCode(CodeTable.Agency,sendingAgency));
		        
				KeyValue chargeType = new KeyValue(); 
				chargeType.setValue( XmlUtils.xPathStringSearch(chargeNode, "jxdm51:ChargeCategoryDescriptionText"));
				chargeType.setKey(descriptionCodeLookupService.retrieveCode(CodeTable.ChargeType, chargeType.getValue()));
				bookingCharge.setChargeType(chargeType);
				
				setBondInfo(chargeNode, chargeRef, bookingCharge);
				
				String nextCourtEventRef = XmlUtils.xPathStringSearch(arrestNode, 
						"parent::br-doc:BookingReport/jxdm51:ActivityChargeAssociation[jxdm51:Charge/@s30:ref='" + chargeRef + "']/nc30:Activity/@s30:ref");
				
				String nextCourtDateString = XmlUtils.xPathStringSearch(arrestNode, 
						"parent::br-doc:BookingReport/cyfs31:NextCourtEvent[@s30:id='"+nextCourtEventRef + "']/nc30:ActivityDate/nc30:Date");
				bookingCharge.setNextCourtDate(StringUtils.isNotBlank(nextCourtDateString)? LocalDate.parse(nextCourtDateString):null);
				
				String nextCourtName = XmlUtils.xPathStringSearch(arrestNode, 
						"/br-doc:BookingReport/cyfs31:NextCourtEvent[@s30:id='"+nextCourtEventRef + "']/jxdm51:CourtEventCourt/jxdm51:CourtName");
				bookingCharge.setNextCourtName(nextCourtName);
				
				bookingCharges.add(bookingCharge);
			}
		}
		
		analyticalDatastoreDAO.saveBookingCharges(bookingCharges);
	}

	private void setBondInfo(Node chargeNode, String chargeRef, BookingCharge bookingCharge) throws Exception {
		
		String bondId = XmlUtils.xPathStringSearch(chargeNode, "parent::br-doc:BookingReport/"
				+ "jxdm51:BailBondChargeAssociation[jxdm51:Charge/@s30:ref='"+ chargeRef + "']/jxdm51:BailBond/@s30:ref");
		
		if (StringUtils.isNotBlank(bondId)){
			Node bondNode = XmlUtils.xPathNodeSearch(chargeNode, 
					"parent::br-doc:BookingReport/jxdm51:BailBond[@s30:id = '"+ bondId +  "']");
			
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
	private Integer processBookingReport(Document report) throws Exception {
		Booking booking = new Booking();
		
        Integer bookingSubjectId = processPersonAndBehavioralHealthInfo(report);
        booking.setBookingSubjectId(bookingSubjectId);
        
        Node bookingReportNode = XmlUtils.xPathNodeSearch(report, "/br-doc:BookingReport");
        
        String courtName = XmlUtils.xPathStringSearch(bookingReportNode, "nc30:Case/jxdm51:CaseAugmentation/jxdm51:CaseCourt/jxdm51:CourtName");
        booking.setJurisdictionId(descriptionCodeLookupService.retrieveCode(CodeTable.Jurisdiction, courtName));
        
        String bookingReportDate = XmlUtils.xPathStringSearch(bookingReportNode, "nc30:DocumentCreationDate/nc30:DateTime");
        if (StringUtils.isNotBlank(bookingReportDate)){
        	booking.setBookingReportDate(LocalDateTime.parse(StringUtils.substringBefore(bookingReportDate, ".")));
        }
        
        String bookingReportId = XmlUtils.xPathStringSearch(bookingReportNode, "nc30:DocumentIdentification/nc30:IdentificationID");
        booking.setBookingReportId(bookingReportId);
        
        String caseStatus = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/nc30:SupervisionCustodyStatus/nc30:StatusDescriptionText");
        Integer caseStatusId = descriptionCodeLookupService.retrieveCode(CodeTable.CaseStatus, caseStatus);
        booking.setCaseStatusId(caseStatusId);
        
        String bookingDate = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Booking/nc30:ActivityDate/nc30:DateTime");
        booking.setBookingDate(LocalDateTime.parse(bookingDate));

        String commitDate = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/nc30:ActivityDate/nc30:Date");
        booking.setCommitDate(LocalDate.parse(commitDate));
        
        String facility = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Booking/jxdm51:BookingDetentionFacility/nc30:FacilityIdentification/nc30:IdentificationID");
        Integer facilityId = descriptionCodeLookupService.retrieveCode(CodeTable.Facility, facility);
        booking.setFacilityId(facilityId);
        
        String bedType = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionBedIdentification/ac-bkg-codes:BedCategoryCode");
        Integer bedTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.BedType, bedType);
        booking.setBedTypeId(bedTypeId);
        
		String bookingNumber = XmlUtils.xPathStringSearch(bookingReportNode, "jxdm51:Booking/jxdm51:BookingAgencyRecordIdentification/nc30:IdentificationID");
		booking.setBookingNumber(bookingNumber);
		
        String supervisionReleaseEligibilityDate = XmlUtils.xPathStringSearch(bookingReportNode, 
        		"jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionReleaseEligibilityDate/nc30:Date");
        
        String supervisionReleaseDate = XmlUtils.xPathStringSearch(bookingReportNode, 
        		"jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionReleaseDate/nc30:DateTime");
        if (StringUtils.isNotBlank(supervisionReleaseDate) || StringUtils.isNotBlank(supervisionReleaseEligibilityDate)){
        	CustodyRelease custodyRelease = new CustodyRelease();
        	
        	if (StringUtils.isNotBlank(supervisionReleaseDate)){
        		custodyRelease.setReleaseDate(LocalDateTime.parse(supervisionReleaseDate));
        	}
        	
        	if (StringUtils.isNotBlank(supervisionReleaseEligibilityDate)){
        		custodyRelease.setScheduledReleaseDate(LocalDate.parse(supervisionReleaseEligibilityDate));
        	}
        	
        	custodyRelease.setBookingNumber(bookingNumber);
        	custodyRelease.setReportDate(booking.getBookingReportDate());
        	
        	booking.setCustodyRelease(custodyRelease);
        	analyticalDatastoreDAO.saveCustodyRelease(custodyRelease);
        }
        
        Integer bookingId = analyticalDatastoreDAO.saveBooking(booking);
		return bookingId;
	}
	
	private Integer processPersonAndBehavioralHealthInfo(Document report) throws Exception {
		
		Node personNode = XmlUtils.xPathNodeSearch(report, 
				"/br-doc:BookingReport/nc30:Person[@s30:id=/br-doc:BookingReport/jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref]");
        
		String personUniqueIdentifier = getPersonUniqueIdentifier(personNode, "br-ext:PersonPersistentIdentification/nc30:IdentificationID");
		
		BookingSubject bookingSubject = new BookingSubject();

		Integer personId = analyticalDatastoreDAO.getPersonIdByUniqueId(personUniqueIdentifier);

		if (personId != null){
			bookingSubject.setRecidivistIndicator(1);
		}
		else{
			personId = savePerson(personNode, personUniqueIdentifier);
		}
		
		processBehavioralHealthInfo(personNode, personId);
		
		return saveBookingSubject(personNode, bookingSubject, personId);
	}

	private void processBehavioralHealthInfo(Node personNode, Integer personId) throws Exception {
		
		String behavioralHealthInfoRef = XmlUtils.xPathStringSearch(personNode, "br-ext:PersonBehavioralHealthInformation/@s30:ref");
		String personCareEpisodeRef = XmlUtils.xPathStringSearch(personNode, "br-ext:PersonCareEpisode/@s30:ref");
		
		if (StringUtils.isNotBlank(behavioralHealthInfoRef) || StringUtils.isNotBlank(personCareEpisodeRef)){
			BehavioralHealthAssessment assessment = new BehavioralHealthAssessment();
			
			assessment.setPersonId(personId);
			
			Node behavioralHealthInfoNode = XmlUtils.xPathNodeSearch(personNode, 
					"/br-doc:BookingReport/br-ext:BehavioralHealthInformation['"+ behavioralHealthInfoRef + "']"); 
			
			if (behavioralHealthInfoNode != null){
				String seriousMentalIllnessIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, "br-ext:SeriousMentalIllnessIndicator");
				assessment.setSeriousMentalIllness(BooleanUtils.toBooleanObject(seriousMentalIllnessIndicator));
	
				String highRiskNeedsIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, "br-ext:HighRiskNeedsIndicator");
				assessment.setHighRiskNeeds(BooleanUtils.toBooleanObject(highRiskNeedsIndicator));
				
				String substanceAbuseIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, "br-ext:SubstanceAbuseIndicator");
				assessment.setSubstanceAbuse(BooleanUtils.toBooleanObject(substanceAbuseIndicator));
				
				String generalMentalHealthConditionIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, "br-ext:GeneralMentalHealthConditionIndicator");
				assessment.setGeneralMentalHealthCondition(BooleanUtils.toBooleanObject(generalMentalHealthConditionIndicator));
				
				String medicaidIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, "hs:MedicaidIndicator");
				assessment.setMedicaidIndicator(BooleanUtils.toBooleanObject(medicaidIndicator));
				
				String regionalAuthorityAssignmentText = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, "br-ext:RegionalBehavioralHealthAuthorityAssignmentText");
				assessment.setRegionalAuthorityAssignmentText(regionalAuthorityAssignmentText);
			}
			
			String careEpisodeStartDateString = XmlUtils.xPathStringSearch(personNode, 
					"/br-doc:BookingReport/br-ext:CareEpisode[@s30:id='" + personCareEpisodeRef + "']/nc30:ActivityDateRange/nc30:StartDate/nc30:Date");
			LocalDate careEpisodeStartDate = StringUtils.isNotBlank(careEpisodeStartDateString)?LocalDate.parse(careEpisodeStartDateString):null;
			assessment.setCareEpisodeStartDate(careEpisodeStartDate);
			
			String careEpisodeEndDateString = XmlUtils.xPathStringSearch(personNode, 
					"/br-doc:BookingReport/br-ext:CareEpisode[@s30:id='" + personCareEpisodeRef + "']/nc30:ActivityDateRange/nc30:EndDate/nc30:Date");
			LocalDate careEpisodeEndDate = StringUtils.isNotBlank(careEpisodeEndDateString)?LocalDate.parse(careEpisodeEndDateString):null;
			assessment.setCareEpisodeEndDate(careEpisodeEndDate);
			
			Integer assessmentId = analyticalDatastoreDAO.saveBehavioralHealthAssessment(assessment);
			
			
			processEvaluationNodes(assessment, behavioralHealthInfoNode, assessmentId);
			processTreatmentNodes(assessment, behavioralHealthInfoNode, assessmentId);
			processPrescribedMedications(assessment, behavioralHealthInfoNode, assessmentId);
		}
		
	}

	private void processPrescribedMedications(
			BehavioralHealthAssessment assessment,
			Node behavioralHealthInfoNode, Integer assessmentId) throws Exception {
		NodeList prescribedMedicationNodes = XmlUtils.xPathNodeListSearch(behavioralHealthInfoNode, "br-ext:PrescribedMedication");

		if (prescribedMedicationNodes.getLength() > 0){
			
			List<PrescribedMedication> prescribedMedications = new ArrayList<PrescribedMedication>();
			
			for (int i = 0; i < prescribedMedicationNodes.getLength(); i++) {
				Node prescribedMedicationNode = prescribedMedicationNodes.item(i);
				
				PrescribedMedication prescribedMedication = new PrescribedMedication();
				prescribedMedication.setBehavioralHealthAssessmentID(assessmentId);
				
				setMedicationId(prescribedMedicationNode, prescribedMedication);
				
				String medicationDispensingDate = XmlUtils.xPathStringSearch(prescribedMedicationNode, 
						"cyfs31:MedicationDispensingDate/nc30:Date");
				if (StringUtils.isNotBlank(medicationDispensingDate)){
					prescribedMedication.setMedicationDispensingDate(LocalDate.parse(medicationDispensingDate));
				}
				
				String medicationDoseMeasure = XmlUtils.xPathStringSearch(prescribedMedicationNode, 
						"cyfs31:MedicationDoseMeasure/nc30:MeasureValueText");
				prescribedMedication.setMedicationDoseMeasure(medicationDoseMeasure);
				
				prescribedMedications.add(prescribedMedication);
			}
			
			analyticalDatastoreDAO.savePrescribedMedications(prescribedMedications);
			assessment.setPrescribedMedications(prescribedMedications);
		}
		
	}

	private void setMedicationId(Node prescribedMedicationNode,
			PrescribedMedication prescribedMedication) throws Exception {
		String medicationItemName = XmlUtils.xPathStringSearch(prescribedMedicationNode,
				"cyfs31:Medication/nc30:ItemName");
		String medicationGeneralProdId = XmlUtils.xPathStringSearch(prescribedMedicationNode, 
				"cyfs31:Medication/br-ext:MedicationGeneralProductIdentification/nc30:IdentificationID");
		
		if (StringUtils.isNotBlank(medicationItemName)|| 
				StringUtils.isNotBlank(medicationGeneralProdId)){
			Integer medicationId = analyticalDatastoreDAO.getMedicationId(medicationGeneralProdId, medicationItemName);
			
			if (medicationId == null){
				medicationId = analyticalDatastoreDAO.saveMedication(medicationGeneralProdId, medicationItemName);
			}
			
			prescribedMedication.setMedicationId(medicationId);
		}
	}

	private void processTreatmentNodes(BehavioralHealthAssessment assessment,
			Node behavioralHealthInfoNode, Integer assessmentId) throws Exception {
		NodeList treatmentNodes = XmlUtils.xPathNodeListSearch(behavioralHealthInfoNode, "nc30:Treatment");

		if (treatmentNodes.getLength() > 0){
			
			List<Treatment> treatments = new ArrayList<Treatment>();
			
			for (int i = 0; i < treatmentNodes.getLength(); i++) {
				Node treatmentNode = treatmentNodes.item(i);
				
				Treatment treatment = new Treatment();
				treatment.setBehavioralHealthAssessmentID(assessmentId);
				
				String startDateString = XmlUtils.xPathStringSearch(treatmentNode, "nc30:ActivityDateRange/nc30:StartDate/nc30:Date");
				if (StringUtils.isNotBlank(startDateString)){
					treatment.setStartDate(LocalDate.parse(startDateString));
				}
				
				String endDateString = XmlUtils.xPathStringSearch(treatmentNode, "nc30:ActivityDateRange/nc30:EndDate/nc30:Date");
				if (StringUtils.isNotBlank(endDateString)){
					treatment.setEndDate(LocalDate.parse(endDateString));
				}
				
				String treatmentText = XmlUtils.xPathStringSearch(treatmentNode, "nc30:TreatmentText");
				treatment.setTreatmentText(treatmentText);
				
				String treatmentProvider = XmlUtils.xPathStringSearch(treatmentNode, "nc30:TreatmentProvider/nc30:EntityOrganization/nc30:OrganizationName");
				treatment.setTreatmentProvider(treatmentProvider);
				
				String treatmentCourtOrdered = XmlUtils.xPathStringSearch(treatmentNode, "br-ext:TreatmentCourtOrderedIndicator");
				treatment.setTreatmentCourtOrdered(BooleanUtils.toBooleanObject(treatmentCourtOrdered));
				
				String treatmentActive = XmlUtils.xPathStringSearch(treatmentNode, "br-ext:TreatmentActiveIndicator");
				treatment.setTreatmentActive(BooleanUtils.toBooleanObject(treatmentActive));
				
				treatments.add(treatment);
			}
			
			analyticalDatastoreDAO.saveTreatments(treatments);
			assessment.setTreatments(treatments);
		}
		
	}

	private void processEvaluationNodes(BehavioralHealthAssessment assessment,
			Node behavioralHealthInfoNode, Integer assessmentId)
			throws Exception {
		NodeList evaluationNodes = XmlUtils.xPathNodeListSearch(behavioralHealthInfoNode, "jxdm51:Evaluation");

		if (evaluationNodes.getLength() > 0){
			
			List<KeyValue> behavioralHealthTypes = new ArrayList<KeyValue>();
			
			for (int i = 0; i < evaluationNodes.getLength(); i++) {
				Node evaluationNode = evaluationNodes.item(i);
				String evaluationDiagnosisDescriptionText = StringUtils.trimToEmpty(XmlUtils.xPathStringSearch(evaluationNode, "jxdm51:EvaluationDiagnosisDescriptionText"));
				
				if (StringUtils.isNotBlank(evaluationDiagnosisDescriptionText)){
			        Integer behavioralHealthTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.BehavioralHealthType, evaluationDiagnosisDescriptionText);
					
			        if (behavioralHealthTypeId == null){
			        	behavioralHealthTypeId = analyticalDatastoreDAO.saveBehavioralHealthType(evaluationDiagnosisDescriptionText);
			        	descriptionCodeLookupService.addEntry(CodeTable.BehavioralHealthType, evaluationDiagnosisDescriptionText, behavioralHealthTypeId);
			        }
			        
			        KeyValue behavioralHealthType = new KeyValue(behavioralHealthTypeId, evaluationDiagnosisDescriptionText); 
			        behavioralHealthTypes.add(behavioralHealthType);
				}
			}
			
			analyticalDatastoreDAO.saveBehavioralHealthEvaluations(assessmentId, behavioralHealthTypes);
			assessment.setBehavioralHealthTypes(behavioralHealthTypes);
		}
	}


}
