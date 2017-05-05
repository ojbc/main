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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.camel.Body;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.AnalyticalDatastoreDAO;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Address;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyRelease;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PrescribedMedication;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Treatment;
import org.ojbc.adapters.analyticsstaging.custody.service.DescriptionCodeLookupFromExcelService;
import org.ojbc.util.helper.OJBCDateUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractReportRepositoryProcessor {
	private static final String ASSESSMENT_CATEGORY_GENERAL_MENTAL_HEALTH = "General Mental Health";
	private static final String ASSESSMENT_CATEGORY_SUBSTANCE_ABUSE = "Substance Abuse";

	private static final Log log = LogFactory.getLog( AbstractReportRepositoryProcessor.class );

	protected static final String FATAL_ERROR_PERSON_UNIQUE_ID_MISSING = "Fatal error: The person unique identifier is missing";
	protected static final String BOOKING_NUMBER_IS_MISSING_IN_THE_REPORT = "Booking Number is missing in the report";

	@Autowired
	protected AnalyticalDatastoreDAO analyticalDatastoreDAO;
	
	@Autowired
	protected DescriptionCodeLookupFromExcelService descriptionCodeLookupService; 
	
    @Transactional
	public abstract void processReport(@Body Document report) throws Exception;

	protected Integer savePerson(Node personNode, String personUniqueIdentifier, String extPrefix) throws Exception {
		
		Person person = new Person();
		
		person.setPersonUniqueIdentifier(personUniqueIdentifier);
		
		person.setPersonUniqueIdentifier2(XmlUtils.xPathStringSearch(personNode, "preceding-sibling::jxdm51:Booking/jxdm51:BookingSubject/jxdm51:SubjectIdentification/nc30:IdentificationID"));;
		
		String personRaceCode=XmlUtils.xPathStringSearch(personNode, "ac-bkg-codes:PersonRaceCode");
		if (StringUtils.isBlank(personRaceCode)){
			personRaceCode=XmlUtils.xPathStringSearch(personNode, "pc-bkg-codes:PersonRaceCode");
		}
		person.setPersonRaceCode(personRaceCode);
		person.setPersonRaceId(descriptionCodeLookupService.retrieveCode(CodeTable.PersonRaceType, personRaceCode));
		
		String personSex=XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonSexCode");
		person.setPersonSexCode(StringUtils.trimToNull(personSex));
		person.setPersonSexId(descriptionCodeLookupService.retrieveCode(CodeTable.PersonSexType, personSex));
		
		String personEthnicityType=XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonEthnicityCode");
		person.setPersonEthnicityTypeDescription(StringUtils.trimToNull(personEthnicityType));
		person.setPersonEthnicityTypeId(descriptionCodeLookupService.retrieveCode(CodeTable.PersonEthnicityType, StringUtils.trimToNull(personEthnicityType)));

		String personBirthDate = XmlUtils.xPathStringSearch(personNode, "nc30:PersonBirthDate/nc30:Date");
		person.setPersonBirthDate(LocalDate.parse(personBirthDate));
		
		String language = XmlUtils.xPathStringSearch(personNode, "nc30:PersonPrimaryLanguage/nc30:LanguageName");
		person.setLanguage(language);
		person.setLanguageId(descriptionCodeLookupService.retrieveCode(CodeTable.LanguageType, language));

		String personCriminalHistorySummaryRef = 
				XmlUtils.xPathStringSearch(personNode, "following-sibling::nc30:ActivityPersonAssociation"
						+ "[nc30:Person/@s30:ref= preceding-sibling::jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref]"
						+ "/nc30:Activity/@s30:ref");
		String registeredSexOffender = XmlUtils.xPathStringSearch(personNode, 
				"preceding-sibling::jxdm51:PersonCriminalHistorySummary[@s30:id='"+ personCriminalHistorySummaryRef + "']/jxdm51:RegisteredSexualOffenderIndicator");
		Boolean registeredSexOffenderBoolean = BooleanUtils.toBooleanObject(registeredSexOffender);
		String sexOffenderStatus = BooleanUtils.toString(registeredSexOffenderBoolean, "registered", "not registered", null); 
		person.setSexOffenderStatusTypeId(descriptionCodeLookupService.retrieveCode(CodeTable.SexOffenderStatusType, sexOffenderStatus));
		
	 	String educationLevel = XmlUtils.xPathStringSearch(personNode, "nc30:PersonEducationLevelText");
	 	person.setEducationLevel(educationLevel);
	 	
	 	String occupation = XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonAugmentation/nc30:EmployeeOccupationCategoryText");
 		person.setOccupation(occupation);
	 	
 		Boolean homelessIndicator = BooleanUtils.toBooleanObject(XmlUtils.xPathStringSearch(personNode, extPrefix + ":PersonHomelessIndicator"));
 		String domicileStatusType = BooleanUtils.toString(homelessIndicator, "homeless", "not homeless", null);
 		person.setDomicileStatusTypeId(descriptionCodeLookupService.retrieveCode(CodeTable.DomicileStatusType, domicileStatusType));

 		Boolean personVeteranBenefitsEligibilityIndicator = BooleanUtils.toBooleanObject(XmlUtils.xPathStringSearch(personNode, extPrefix + ":PersonVeteranBenefitsEligibilityIndicator"));
 		String programEligibilityType = BooleanUtils.toString(personVeteranBenefitsEligibilityIndicator, "Veteran Services", "none", null); 
 		person.setProgramEligibilityTypeId(descriptionCodeLookupService.retrieveCode(CodeTable.ProgramEligibilityType, programEligibilityType));
 		
 		Boolean inmateWorkReleaseIndicator = BooleanUtils.toBooleanObject(XmlUtils.xPathStringSearch(personNode, "preceding-sibling::jxdm51:Detention/" + extPrefix + ":InmateWorkReleaseIndicator"));
 		String workReleaseStatusType = BooleanUtils.toString(inmateWorkReleaseIndicator, "assigned", "not assigned", null);
 		person.setWorkReleaseStatusTypeId(descriptionCodeLookupService.retrieveCode(CodeTable.WorkReleaseStatusType, workReleaseStatusType));;
 		
	 	String militaryServiceStatusCode = XmlUtils.xPathStringSearch(personNode, "nc30:PersonMilitarySummary/ac-bkg-codes:MilitaryServiceStatusCode");
 		Integer militaryServiceStatusTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.MilitaryServiceStatusType, militaryServiceStatusCode);
	 	person.setMilitaryServiceStatusType(new KeyValue(militaryServiceStatusTypeId, militaryServiceStatusCode));
		Integer personId = analyticalDatastoreDAO.savePerson(person);
		
		return personId;
	}


	protected String getPersonUniqueIdentifier(Node personNode, String xPath) throws Exception {
		String personUniqueIdentifier = StringUtils.trimToNull(XmlUtils.xPathStringSearch(personNode, 
				xPath));
		if (StringUtils.isBlank(personUniqueIdentifier)){
			log.fatal(FATAL_ERROR_PERSON_UNIQUE_ID_MISSING);
			throw new Exception(FATAL_ERROR_PERSON_UNIQUE_ID_MISSING);
		}
		return personUniqueIdentifier;
	}

	protected void processBehavioralHealthInfo(Node personNode, Integer personId, String extPrefix) throws Exception {
		
		String behavioralHealthInfoRef = XmlUtils.xPathStringSearch(personNode, extPrefix + ":PersonBehavioralHealthInformation/@s30:ref");
		String personCareEpisodeRef = XmlUtils.xPathStringSearch(personNode, extPrefix + ":PersonCareEpisode/@s30:ref");
		
		if (StringUtils.isNotBlank(behavioralHealthInfoRef) || StringUtils.isNotBlank(personCareEpisodeRef)){
			BehavioralHealthAssessment assessment = new BehavioralHealthAssessment();
			
			assessment.setPersonId(personId);
			
			Node behavioralHealthInfoNode = XmlUtils.xPathNodeSearch(personNode, 
					"following-sibling::"+ extPrefix + ":BehavioralHealthInformation['"+ behavioralHealthInfoRef + "']"); 
			
			if (behavioralHealthInfoNode != null){
				String seriousMentalIllnessIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, extPrefix + ":SeriousMentalIllnessIndicator");
				assessment.setSeriousMentalIllness(BooleanUtils.toBooleanObject(seriousMentalIllnessIndicator));
				
				String medicaidIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, "hs:MedicaidIndicator");
				Boolean medicaidIndicatorBoolean = BooleanUtils.toBooleanObject(medicaidIndicator);
				String medicaidStatusType = BooleanUtils.toString(medicaidIndicatorBoolean, "eligible", "not eligible", null);
				assessment.setMedicaidStatusTypeId(descriptionCodeLookupService.retrieveCode(CodeTable.MedicaidStatusType, medicaidStatusType));
				
				String regionalAuthorityAssignmentText = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, extPrefix + ":RegionalBehavioralHealthAuthorityAssignmentText");
				assessment.setEnrolledProviderName(regionalAuthorityAssignmentText);
				
				Set<Integer> assessmentCategoryTypeIds = new HashSet<Integer>();
				Boolean substanceAbuseIndicator = BooleanUtils.toBooleanObject(
						XmlUtils.xPathStringSearch(behavioralHealthInfoNode, extPrefix + ":SubstanceAbuseIndicator"));
				if (BooleanUtils.isTrue(substanceAbuseIndicator)){
					Integer assessmentCategoryTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.AssessmentCategoryType, ASSESSMENT_CATEGORY_SUBSTANCE_ABUSE);
					addAssessmentCategoryToAssessment(assessment, assessmentCategoryTypeIds,
							assessmentCategoryTypeId);
				}

				Boolean generalMentalHealthConditionIndicator = BooleanUtils.toBooleanObject(
						XmlUtils.xPathStringSearch(behavioralHealthInfoNode, extPrefix + ":GeneralMentalHealthConditionIndicator"));
				if (BooleanUtils.isTrue(generalMentalHealthConditionIndicator)){
					Integer assessmentCategoryTypeId = descriptionCodeLookupService.retrieveCode(CodeTable.AssessmentCategoryType, ASSESSMENT_CATEGORY_GENERAL_MENTAL_HEALTH);
					addAssessmentCategoryToAssessment(assessment, assessmentCategoryTypeIds,
							assessmentCategoryTypeId);
				}
				
				processTreatmentTextNodes(assessment, behavioralHealthInfoNode,
						assessmentCategoryTypeIds);
				
				String careEpisodeStartDateString = XmlUtils.xPathStringSearch(personNode, 
						"following-sibling::"+ extPrefix + ":CareEpisode[@s30:id='" + personCareEpisodeRef + "']/nc30:ActivityDateRange/nc30:StartDate/nc30:Date");
				LocalDate careEpisodeStartDate = StringUtils.isNotBlank(careEpisodeStartDateString)?LocalDate.parse(careEpisodeStartDateString):null;
				assessment.setCareEpisodeStartDate(careEpisodeStartDate);
				
				String careEpisodeEndDateString = XmlUtils.xPathStringSearch(personNode, 
						"following-sibling::"+ extPrefix + ":CareEpisode[@s30:id='" + personCareEpisodeRef + "']/nc30:ActivityDateRange/nc30:EndDate/nc30:Date");
				LocalDate careEpisodeEndDate = StringUtils.isNotBlank(careEpisodeEndDateString)?LocalDate.parse(careEpisodeEndDateString):null;
				assessment.setCareEpisodeEndDate(careEpisodeEndDate);
				
				Integer assessmentId = analyticalDatastoreDAO.saveBehavioralHealthAssessment(assessment);
				
				assessment.setBehavioralHealthAssessmentId(assessmentId);
				processEvaluationNodes(assessment, behavioralHealthInfoNode, extPrefix);
				processTreatmentNodes(assessment, behavioralHealthInfoNode, extPrefix);
				processPrescribedMedications(assessment, behavioralHealthInfoNode, extPrefix);
			}
		}
		
	}

	private void processTreatmentTextNodes(BehavioralHealthAssessment assessment,
			Node behavioralHealthInfoNode,
			Set<Integer> assessmentCategoryTypeIds) throws Exception {
		NodeList treatmentTextNodes = XmlUtils.xPathNodeListSearch(behavioralHealthInfoNode, "nc30:Treatment/nc30:TreatmentText");
		if (treatmentTextNodes.getLength() > 0){
			for (int i= 0; i < treatmentTextNodes.getLength(); i++){
				String treatmentText = treatmentTextNodes.item(i).getTextContent(); 
				String[] treatmentTextValues = StringUtils.split(treatmentText, ',');
				
				for (String treatmentTextValue : treatmentTextValues){
					Integer assessmentCategoryTypeId = 
							descriptionCodeLookupService.retrieveCode(CodeTable.AssessmentCategoryType, treatmentTextValue);
					addAssessmentCategoryToAssessment(assessment, assessmentCategoryTypeIds,
							assessmentCategoryTypeId);
				}
			}
		}
	}

	private void addAssessmentCategoryToAssessment(BehavioralHealthAssessment assessment,
			Set<Integer> assessmentCategoryTypeIds,
			Integer assessmentCategoryTypeId) {
		if (assessmentCategoryTypeId != null && !assessmentCategoryTypeIds.contains(assessmentCategoryTypeId)){
			assessment.getAssessmentCategories().add(new KeyValue(assessmentCategoryTypeId, ASSESSMENT_CATEGORY_SUBSTANCE_ABUSE));
			assessmentCategoryTypeIds.add(assessmentCategoryTypeId);
		}
	}

	private void processPrescribedMedications(
			BehavioralHealthAssessment assessment,
			Node behavioralHealthInfoNode, String extPrefix) throws Exception {
		NodeList prescribedMedicationNodes = XmlUtils.xPathNodeListSearch(behavioralHealthInfoNode, extPrefix + ":PrescribedMedication");

		if (prescribedMedicationNodes.getLength() > 0){
			
			List<PrescribedMedication> prescribedMedications = new ArrayList<PrescribedMedication>();
			
			for (int i = 0; i < prescribedMedicationNodes.getLength(); i++) {
				Node prescribedMedicationNode = prescribedMedicationNodes.item(i);
				
				PrescribedMedication prescribedMedication = new PrescribedMedication();
				prescribedMedication.setBehavioralHealthAssessmentID(assessment.getBehavioralHealthAssessmentId());
				String medicationItemName = XmlUtils.xPathStringSearch(prescribedMedicationNode,
						"cyfs31:Medication/nc30:ItemName");
				prescribedMedication.setMedicationDescription(medicationItemName);
				
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

	private void processTreatmentNodes(BehavioralHealthAssessment assessment,
			Node behavioralHealthInfoNode, String extPrefix) throws Exception {
		NodeList treatmentNodes = XmlUtils.xPathNodeListSearch(behavioralHealthInfoNode, "nc30:Treatment");

		if (treatmentNodes.getLength() > 0){
			
			List<Treatment> treatments = new ArrayList<Treatment>();
			
			for (int i = 0; i < treatmentNodes.getLength(); i++) {
				Node treatmentNode = treatmentNodes.item(i);
				
				Treatment treatment = new Treatment();
				treatment.setBehavioralHealthAssessmentID(assessment.getBehavioralHealthAssessmentId());
				
				String startDateString = XmlUtils.xPathStringSearch(treatmentNode, "nc30:ActivityDateRange/nc30:StartDate/nc30:Date");
				if (StringUtils.isNotBlank(startDateString)){
					treatment.setTreatmentStartDate(LocalDate.parse(startDateString));
				}
				
				String treatmentProvider = XmlUtils.xPathStringSearch(treatmentNode, "nc30:TreatmentProvider/nc30:EntityOrganization/nc30:OrganizationName");
				treatment.setTreatmentProviderName(treatmentProvider);
				
				Boolean treatmentCourtOrdered = BooleanUtils.toBooleanObject(XmlUtils.xPathStringSearch(treatmentNode, extPrefix + ":TreatmentCourtOrderedIndicator"));
				String treatmentAdmissionReason = BooleanUtils.toString(treatmentCourtOrdered, "Court-Ordered Treatment", "Other", null); 
				treatment.setTreatmentAdmissionReasonTypeId(descriptionCodeLookupService.retrieveCode(CodeTable.TreatmentAdmissionReasonType, treatmentAdmissionReason));
				
				Boolean treatmentActive = BooleanUtils.toBooleanObject(XmlUtils.xPathStringSearch(treatmentNode, extPrefix + ":TreatmentActiveIndicator"));
				String treamentStatusType = BooleanUtils.toString(treatmentActive, "active", "inactive", null);
				treatment.setTreatmentStatusTypeId(descriptionCodeLookupService.retrieveCode(CodeTable.TreatmentStatusType, treamentStatusType));
				
				treatments.add(treatment);
			}
			
			analyticalDatastoreDAO.saveTreatments(treatments);
			assessment.setTreatments(treatments);
		}
		
	}

	private void processEvaluationNodes(BehavioralHealthAssessment assessment,
			Node behavioralHealthInfoNode, String extPrefix)
			throws Exception {
		NodeList evaluationNodes = XmlUtils.xPathNodeListSearch(behavioralHealthInfoNode, "jxdm51:Evaluation");

		if (evaluationNodes.getLength() > 0){
			
			List<String> behavioralHealthDiagnoses = new ArrayList<String>();
			
			for (int i = 0; i < evaluationNodes.getLength(); i++) {
				Node evaluationNode = evaluationNodes.item(i);
				String evaluationDiagnosisDescriptionText = XmlUtils.xPathStringSearch(evaluationNode, "jxdm51:EvaluationDiagnosisDescriptionText");
				
				if (StringUtils.isNotBlank(evaluationDiagnosisDescriptionText)){
					behavioralHealthDiagnoses.add(evaluationDiagnosisDescriptionText);
				}
			}
			
			analyticalDatastoreDAO.saveBehavioralHealthEvaluations(assessment.getBehavioralHealthAssessmentId(), behavioralHealthDiagnoses);
			assessment.setBehavioralHealthDiagnoses(behavioralHealthDiagnoses);
		}
	}

	protected Map<String, Integer> constructAddressMap(NodeList locationNodes)
			throws Exception {
		Map<String, Integer> addressMap = new HashMap<String, Integer>(); 
		for (int i = 0; i < locationNodes.getLength(); i++) {
			Node locationNode = locationNodes.item(i);
			
			String locationId = XmlUtils.xPathStringSearch(locationNode, "@s30:id");
			Address address = getAddress(locationNode);
			Integer addressId = analyticalDatastoreDAO.saveAddress(address);
			
			addressMap.put(locationId, addressId);
		}
		return addressMap;
	}


	protected Address getAddress(Node locationNode) throws Exception {
		Address address = new Address(null);
		
		if (locationNode != null){
			
			String streetNumber = XmlUtils.xPathStringSearch(locationNode, "nc30:Address/nc30:LocationStreet/nc30:StreetNumberText");
			address.setStreetNumber(streetNumber);
			
			String streetName = XmlUtils.xPathStringSearch(locationNode, "nc30:Address/nc30:LocationStreet/nc30:StreetName");
			address.setStreetName(streetName);
			
			String city = XmlUtils.xPathStringSearch(locationNode, "nc30:Address/nc30:LocationCityName");
			address.setCity(city);
			
			String state = XmlUtils.xPathStringSearch(locationNode, "nc30:Address/nc30:LocationStateUSPostalServiceCode");
			address.setState(state);
			
			String postalcode = XmlUtils.xPathStringSearch(locationNode, "nc30:Address/nc30:LocationPostalCode");
			address.setPostalcode(postalcode);
			
        	Node arrestLocation2DGeoCoordinateNode = XmlUtils.xPathNodeSearch(locationNode, "nc30:Location2DGeospatialCoordinate");
	        	
        	if (arrestLocation2DGeoCoordinateNode != null){
        		String arrestLocationLongitude = XmlUtils.xPathStringSearch(arrestLocation2DGeoCoordinateNode, "nc30:GeographicCoordinateLongitude/nc30:LongitudeDegreeValue");
        		address.setLocationLongitude(new BigDecimal(arrestLocationLongitude));
        		
        		String arrestLocationLatitude = XmlUtils.xPathStringSearch(arrestLocation2DGeoCoordinateNode, "nc30:GeographicCoordinateLatitude/nc30:LatitudeDegreeValue");
        		address.setLocationLatitude(new BigDecimal(arrestLocationLatitude));
	        }
		}
		
		return address;
	}

	protected CustodyRelease processCustodyReleaseInfo(Node parentNode,
			Integer bookingId, String bookingNumber) throws Exception {
		
        String supervisionReleaseDateTimeString = XmlUtils.xPathStringSearch(parentNode, 
        		"jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionReleaseDate/nc30:DateTime");
        LocalDateTime supervisionReleaseDateTime = OJBCDateUtils.parseLocalDateTime(supervisionReleaseDateTimeString); 
        CustodyRelease custodyRelease = new CustodyRelease();
        
        if (supervisionReleaseDateTime != null){
    		custodyRelease.setReleaseDate(supervisionReleaseDateTime.toLocalDate());
    		custodyRelease.setReleaseTime(supervisionReleaseDateTime.toLocalTime());
        }
        else{
        	String releaseDateString = XmlUtils.xPathStringSearch(parentNode, 
            		"jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionReleaseDate/nc30:Date");
        	custodyRelease.setReleaseDate(OJBCDateUtils.parseLocalDate(releaseDateString));
        }
        
        if (custodyRelease.getReleaseDate() != null){
	        custodyRelease.setBookingId(bookingId);
	        custodyRelease.setBookingNumber(bookingNumber);
	        analyticalDatastoreDAO.saveCustodyRelease(custodyRelease);
        }
        
		return custodyRelease;
	}
	
	/**
	 * Returns a Single BookingId for the given bookingNumber
	 * @param bookingNumber
	 * @return bookingId matching the bookingNumber
	 * @throws Exception if bookingNumber is empty of no BookingID found for the bookingNumber. 
	 */
	protected Integer getBookingIdByBookingNumber(String bookingNumber) throws Exception {
		
		if (StringUtils.isBlank(bookingNumber)){
			log.fatal(BOOKING_NUMBER_IS_MISSING_IN_THE_REPORT);
			throw new Exception(BOOKING_NUMBER_IS_MISSING_IN_THE_REPORT);
		}
		
		Integer bookingId = analyticalDatastoreDAO.getBookingIdByBookingNumber(bookingNumber);
		return bookingId;
	}

}
