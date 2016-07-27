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
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.Body;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticsstaging.custody.dao.AnalyticalDatastoreDAO;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Address;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BehavioralHealthAssessment;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.BookingSubject;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CodeTable;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.CustodyRelease;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.KeyValue;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Person;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.PrescribedMedication;
import org.ojbc.adapters.analyticsstaging.custody.dao.model.Treatment;
import org.ojbc.adapters.analyticsstaging.custody.service.DescriptionCodeLookupService;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public abstract class AbstractReportRepositoryProcessor {
	private static final Log log = LogFactory.getLog( AbstractReportRepositoryProcessor.class );

	protected static final String FATAL_ERROR_PERSON_UNIQUE_ID_MISSING = "Fatal error: The person unique identifier is missing";

	@Autowired
	protected AnalyticalDatastoreDAO analyticalDatastoreDAO;
	
	@Autowired
	protected DescriptionCodeLookupService descriptionCodeLookupService; 
	
    @Transactional
	public abstract void processReport(@Body Document report) throws Exception;

	protected Integer savePerson(Node personNode, String personUniqueIdentifier) throws Exception {
		
		Person person = new Person();
		
		person.setPersonUniqueIdentifier(personUniqueIdentifier);
		
		String bookingSubjectNumber = XmlUtils.xPathStringSearch(personNode, 
				"parent::br-doc:BookingReport/jxdm51:Booking/jxdm51:BookingSubject/jxdm51:SubjectIdentification/nc30:IdentificationID");
		person.setBookingSubjectNumber(bookingSubjectNumber);
		
		String personSsn = XmlUtils.xPathStringSearch(personNode, "nc30:PersonSSNIdentification/nc30:IdentificationID");
		person.setPersonSsn(personSsn);
		
		String personSid = XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonAugmentation/jxdm51:PersonStateFingerprintIdentification/nc30:IdentificationID");
		person.setPersonSid(personSid);
		
		String personHairColor = XmlUtils.xPathStringSearch(personNode, "nc30:PersonHairColorText");
		person.setPersonHairColor(personHairColor);
		
		String personEyeColor = XmlUtils.xPathStringSearch(personNode, "nc30:PersonEyeColorText");
		person.setPersonEyeColor(personEyeColor);
		
		String personHeight = XmlUtils.xPathStringSearch(personNode,  "nc30:PersonHeightMeasure/nc30:MeasureValueText");
		person.setPersonHeight(personHeight);
		
		String personHeightMeasureUnit = XmlUtils.xPathStringSearch(personNode,  "nc30:PersonHeightMeasure/nc30:MeasureUnitText");
		person.setPersonHeightMeasureUnit(personHeightMeasureUnit);
		
		String personWeight = XmlUtils.xPathStringSearch(personNode,  "nc30:PersonWeightMeasure/nc30:MeasureValueText");
		person.setPersonWeight(personWeight);
		
		String personWeightMeasureUnit = XmlUtils.xPathStringSearch(personNode,  "nc30:PersonWeightMeasure/nc30:MeasureUnitText");
		person.setPersonWeightMeasureUnit(personWeightMeasureUnit);
		
		String personRace=XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonRaceCode");
		person.setPersonRaceId(descriptionCodeLookupService.retrieveCode(CodeTable.PersonRace, personRace));
		
		String personSex=XmlUtils.xPathStringSearch(personNode, "jxdm51:PersonSexCode");
		person.setPersonSexCode(StringUtils.trimToNull(personSex));
		person.setPersonSexId(descriptionCodeLookupService.retrieveCode(CodeTable.PersonSex, StringUtils.trimToNull(personSex)));

		String personBirthDate = XmlUtils.xPathStringSearch(personNode, "nc30:PersonBirthDate/nc30:Date");
		person.setPersonBirthDate(LocalDate.parse(personBirthDate));
		
		String language = XmlUtils.xPathStringSearch(personNode, "nc30:PersonPrimaryLanguage/nc30:LanguageName");
		person.setLanguage(language);
		person.setLanguageId(descriptionCodeLookupService.retrieveCode(CodeTable.Language, language));
		
		String personCriminalHistorySummaryRef = 
				XmlUtils.xPathStringSearch(personNode, "parent::br-doc:BookingReport/nc30:ActivityPersonAssociation"
						+ "[nc30:Person/@s30:ref=/br-doc:BookingReport/jxdm51:Booking/jxdm51:BookingSubject/nc30:RoleOfPerson/@s30:ref]/nc30:Activity/@s30:ref");
		String registeredSexOffender = XmlUtils.xPathStringSearch(personNode, 
				"/br-doc:BookingReport/jxdm51:PersonCriminalHistorySummary[@s30:id='"+ personCriminalHistorySummaryRef + "']/jxdm51:RegisteredSexualOffenderIndicator");
		person.setRegisteredSexOffender(BooleanUtils.toBooleanObject(registeredSexOffender));
		
		Integer personId = analyticalDatastoreDAO.savePerson(person);
		
		return personId;
	}

	protected Integer saveBookingSubject(Node personNode, BookingSubject bookingSubject,
			Integer personId) throws Exception {
		bookingSubject.setPersonId(personId);
		
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
	 	
	 	String militaryServiceStatusCode = XmlUtils.xPathStringSearch(personNode, "nc30:PersonMilitarySummary/ac-bkg-codes:MilitaryServiceStatusCode");
	 	bookingSubject.setMilitaryServiceStatusCode(militaryServiceStatusCode);
	 	
		Integer bookingSubjectId = analyticalDatastoreDAO.saveBookingSubject(bookingSubject);
		return bookingSubjectId;
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
	
				String highRiskNeedsIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, extPrefix + ":HighRiskNeedsIndicator");
				assessment.setHighRiskNeeds(BooleanUtils.toBooleanObject(highRiskNeedsIndicator));
				
				String substanceAbuseIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, extPrefix + ":SubstanceAbuseIndicator");
				assessment.setSubstanceAbuse(BooleanUtils.toBooleanObject(substanceAbuseIndicator));
				
				String generalMentalHealthConditionIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, extPrefix + ":GeneralMentalHealthConditionIndicator");
				assessment.setGeneralMentalHealthCondition(BooleanUtils.toBooleanObject(generalMentalHealthConditionIndicator));
				
				String medicaidIndicator = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, "hs:MedicaidIndicator");
				assessment.setMedicaidIndicator(BooleanUtils.toBooleanObject(medicaidIndicator));
				
				String regionalAuthorityAssignmentText = XmlUtils.xPathStringSearch(behavioralHealthInfoNode, extPrefix + ":RegionalBehavioralHealthAuthorityAssignmentText");
				assessment.setRegionalAuthorityAssignmentText(regionalAuthorityAssignmentText);
			}
			
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
				
				String treatmentCourtOrdered = XmlUtils.xPathStringSearch(treatmentNode, extPrefix + ":TreatmentCourtOrderedIndicator");
				treatment.setTreatmentCourtOrdered(BooleanUtils.toBooleanObject(treatmentCourtOrdered));
				
				String treatmentActive = XmlUtils.xPathStringSearch(treatmentNode, extPrefix + ":TreatmentActiveIndicator");
				treatment.setTreatmentActive(BooleanUtils.toBooleanObject(treatmentActive));
				
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
			
			analyticalDatastoreDAO.saveBehavioralHealthEvaluations(assessment.getBehavioralHealthAssessmentId(), behavioralHealthTypes);
			assessment.setBehavioralHealthTypes(behavioralHealthTypes);
		}
	}

	protected Address getArrestInfo(Node arrestNode) throws Exception {
		Address address = new Address();
		
		String locationRef = XmlUtils.xPathStringSearch(arrestNode, "jxdm51:ArrestLocation/@s30:ref");
		Node locationNode = XmlUtils.xPathNodeSearch(arrestNode, 
				"following-sibling::nc30:Location[@s30:id = '" + locationRef +"']");
		
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
        		address.setArrestLocationLongitude(new BigDecimal(arrestLocationLongitude));
        		
        		String arrestLocationLatitude = XmlUtils.xPathStringSearch(arrestLocation2DGeoCoordinateNode, "nc30:GeographicCoordinateLatitude/nc30:LatitudeDegreeValue");
        		address.setArrestLocationLatitude(new BigDecimal(arrestLocationLatitude));
	        }
	        
		}
		return address;
	}

	protected LocalDateTime parseLocalDateTime(String dateTimeString) {
		
		try{
			if (StringUtils.isNotBlank(dateTimeString)){
				return LocalDateTime.parse(StringUtils.substringBefore(dateTimeString, "."));
			}
			else{
				log.error("The dateTimeString can not be blank");
			}
		}
		catch (DateTimeParseException e){
			log.error("Failed to parse dateTimeString " + dateTimeString, e);
		}
		
		return null;
	}
	
	protected LocalDate parseLocalDate(String dateString) {
		
		try{
			if (StringUtils.isNotBlank(dateString)){
				return LocalDate.parse(dateString);
			}
			else{
				log.error("The dateString can not be blank");
			}
		}
		catch (DateTimeParseException e){
			log.error("Failed to parse dateTimeString " + dateString, e);
		}
		
		return null;
	}

	protected CustodyRelease processCustodyReleaseInfo(LocalDateTime reportDate, Node parentNode,
			String bookingNumber) throws Exception {
		String supervisionReleaseEligibilityDate = XmlUtils.xPathStringSearch(parentNode, 
        		"jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionReleaseEligibilityDate/nc30:Date");
        
        String supervisionReleaseDate = XmlUtils.xPathStringSearch(parentNode, 
        		"jxdm51:Detention/jxdm51:SupervisionAugmentation/jxdm51:SupervisionReleaseDate/nc30:DateTime");
        CustodyRelease custodyRelease = new CustodyRelease();
        
        if (StringUtils.isNotBlank(supervisionReleaseDate) || StringUtils.isNotBlank(supervisionReleaseEligibilityDate)){
        	
        	if (StringUtils.isNotBlank(supervisionReleaseDate)){
        		custodyRelease.setReleaseDate(LocalDateTime.parse(supervisionReleaseDate));
        	}
        	
        	if (StringUtils.isNotBlank(supervisionReleaseEligibilityDate)){
        		custodyRelease.setScheduledReleaseDate(LocalDate.parse(supervisionReleaseEligibilityDate));
        	}
        	
        	custodyRelease.setBookingNumber(bookingNumber);
        	custodyRelease.setReportDate(reportDate);
        	
        	if (StringUtils.isNotBlank(supervisionReleaseDate)){
        		analyticalDatastoreDAO.saveCustodyRelease(custodyRelease);
        	}
        }
		return custodyRelease;
	}
	

}
