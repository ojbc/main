package org.ojbc.adamscounty.xml.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.lang3.StringUtils;
import org.ojbc.booking.common.dao.model.Arrest;
import org.ojbc.util.xml.OjbcNamespaceContext;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class CustodyBookingXMLUtils {

	public static Document createCustodyReleaseReport(String operationName) throws Exception
	{
		Document doc = getNewDocument();
		
	    Element rootElement = getRootElement(doc, operationName);
	    doc.appendChild(rootElement);

	    createDocumentCreationDate(rootElement);
	    
	    createDocumentIdentification(rootElement,"identification ID");
	    
	    createIdentificationSourceSystem(rootElement,"identification ID","system name");
		
	    Element custody = createCustody(rootElement, OjbcNamespaceContext.NS_CUSTODY_RELEASE_REPORTING_EXT);
	    
	    createCase(custody, "Case_01", "", "T456789","DID45678");
	    
	    createBooking(custody, "2013-12-17T09:30:00", "","booking facility ID","booking number","booking subject number", null, "Correct", "CustodyRelease");
	    
	    createDetention(custody,OjbcNamespaceContext.NS_CUSTODY_RELEASE_REPORTING_EXT,"","","", "","", "", "", "", "","2012-01-01","","","","","","","");
	    
	    createRelease(custody, "2001-12-17T09:30:47.0Z", "activity description", "activity text", "Person_01", "Loc_01");
	    
	    createPerson(custody,"Person_01", "1968-12-17", "High School", "Occupation", "English","HD","", "", "", "", "first", "middle", "last", "A", "F", "", "DL239486","CO","FBI123", "","N");
	    
	    createPersonIdentity(custody, "first", "last", "M", "1970-01-02", "ALIAS001");
	    
	    createLocation(custody, "392", "Street Name", "city", "CO", "80601","Loc_01");
	    
	    List<String> aliasReferences = new ArrayList<String>();
	    aliasReferences.add("ALIAS001");
	    
	    createPersonAliasIdentityAssociation(custody, aliasReferences, "Person_01");
	    
	    createBehaviorHealthInformation(custody,OjbcNamespaceContext.NS_CUSTODY_RELEASE_REPORTING_EXT, true, "schizophrenia 295.10");
	    
	    
	    return doc;
	}

	public static Document createBookingReport(String operationName) throws Exception
	{
		Document doc = getNewDocument();
		
	    Element rootElement = getRootElement(doc, operationName);
	    doc.appendChild(rootElement);

	    createDocumentCreationDate(rootElement);
	    
	    createDocumentIdentification(rootElement,"identification ID");
	    
	    createIdentificationSourceSystem(rootElement,"identification ID","system name");
		
	    createCase(rootElement, "Case_01", "court name","","");
	    
	    createBooking(rootElement, "2013-12-17T09:30:00", "", "booking facility ID","booking number","booking subject number", null, "Correct", "Booking");
	    
	    createDetention(rootElement,OjbcNamespaceContext.NS_BOOKING_REPORTING_EXT,"2013-12-17T09:30:00","","pretrial", "probation officer", "primary","false", "true",
	    			"false", "true","","2014-12-17T10:30:00","Cell block A", "Bed 2", "Type 2", "Cell 7", "Supervision condition", "true");
	    
	    createBailBond(rootElement,"bond type","bond status","500.00", "bond_01");
	    
	    createCharge(rootElement, OjbcNamespaceContext.NS_BOOKING_REPORTING_EXT,"Charge_01", "Hold For Agency", "Felony", "Charge Description", "true", "sequence ID", "charge statute code ID", "disposition","charge jurisdiction court");
	    
	    createArrest(rootElement, "Arresting Agency", "Loc_01", "Arrest_01", null,"");
	
	    createPersonCriminalHistorySummary(rootElement, false);
	    
	    createNextCourtEvent(rootElement, "1968-12-17", "Court Name", "Event_01");
	    
	    createPerson(rootElement,"Person_01", "1968-12-17", "High School", "Occupation", "HD","English","Blue", "Black", "110", "4H", "first", "middle", "last", "A", "F", "132", "","","", "S123","N");
	    
	    createPersonOfficer(rootElement, "Officer Full Name");
	    
	    createPersonIdentity(rootElement, "first", "last", "M", "1970-01-02", "ALIAS001");
	    
	    createLocation(rootElement, "392", "Street Name", "city", "CO", "80601","Loc_01");
	    
	    createContactInformation(rootElement,"officer@agency.gov", "312-216-9923","Cell");
	    
	    List<String> activityReferences = new ArrayList<String>();
	    
	    activityReferences.add("Case_01");
	    activityReferences.add("Booking_01");
	    
	    createActivityChargeAssociation(rootElement, activityReferences, "Charge_01");

	    createBailBondChargeAssocation(rootElement, "Bond_01", "Charge_01");
	    
	    List<String> aliasReferences = new ArrayList<String>();
	    aliasReferences.add("ALIAS001");
	    
	    createPersonAliasIdentityAssociation(rootElement, aliasReferences, "Person_01");
	    
	    createPersonContactInformationAssociation(rootElement, "Officer_01", "CI_01");
	    
	    createBehaviorHealthInformation(rootElement,OjbcNamespaceContext.NS_BOOKING_REPORTING_EXT, true, "schizophrenia 295.10");
	    
		return doc;
	}
	
	public static Document createCustodyStatusChangeReport(String operationName) throws Exception
	{
		Document doc = getNewDocument();
		
	    Element rootElement = getRootElement(doc, operationName);
	    doc.appendChild(rootElement);

	    createDocumentCreationDate(rootElement);
	    
	    createDocumentIdentification(rootElement,"identification ID");
	    
	    createIdentificationSourceSystem(rootElement,"identification ID","system name");
		
	    Element custody = createCustody(rootElement, OjbcNamespaceContext.NS_CUSTODY_STATUS_CHANGE_EXT);
	    
	    createCase(custody, "Case_01", "court name","","");
	    
	    createBooking(custody, "2013-12-17T09:30:00", "", "booking facility ID","booking number","booking subject number", null, "Correct", "CustodyStatusChange");
	    
	    createDetention(custody,OjbcNamespaceContext.NS_CUSTODY_STATUS_CHANGE_EXT,"2013-12-17T09:30:00","","pretrial", "probation officer", "primary","false", 
	    		 "true", "false", "true","","2014-12-17T10:30:00","Cell block A", "Bed 2", "Type 2", "Cell 7", "Supervision condition","true");
	    
	    createRelease(custody, "2001-12-17T09:30:47.0Z","","","","");
	    
	    createBailBond(custody,"bond type","bond status","500.00","bond_01");
	    
	    createCharge(custody, OjbcNamespaceContext.NS_BOOKING_REPORTING_EXT, "Charge_01", "Hold for Agency", "Felony", "Charge Description", "true", "sequence ID", "charge statute code ID","disposition","charge jurisdiction court");
	    
	    createArrest(custody, "Arresting Agency", "Loc_01", "Arrest_01", null,"");
	    
	    createPersonCriminalHistorySummary(custody, false);
	    
	    createNextCourtEvent(custody, "1968-12-17", "Court Name", "Event_01");
	    
	    createPerson(custody,"Person_01", "1968-12-17", "High School", "Occupation", "English", "HD","Blue", "Black", "110", "4H", "first", "middle", "last", "A", "F", "132", "DL239486","CO","FBI123", "S123","N");
	    
	    createPersonOfficer(custody, "Officer Full Name");
	    
	    createPersonIdentity(custody, "first", "last", "M", "1970-01-02", "ALIAS001");
	    
	    createLocation(custody, "392", "Street Name", "city", "CO", "80601", "Loc_01");
	    
	    createContactInformation(custody,"officer@agency.gov", "312-216-9923","Cell");
	    
	    List<String> activityReferences = new ArrayList<String>();
	    
	    activityReferences.add("Case_01");
	    activityReferences.add("Booking_01");
	    
	    createActivityChargeAssociation(custody, activityReferences, "Charge_01");

	    createBailBondChargeAssocation(custody, "Bond_01", "Charge_01");
	    
	    List<String> aliasReferences = new ArrayList<String>();
	    aliasReferences.add("ALIAS001");
	    
	    createPersonAliasIdentityAssociation(custody, aliasReferences, "Person_01");
	    
	    createPersonContactInformationAssociation(custody, "Officer_01", "CI_01");
	    
	    createBehaviorHealthInformation(custody,OjbcNamespaceContext.NS_CUSTODY_STATUS_CHANGE_EXT, true, "schizophrenia 295.10");
	    
	    
	    return doc;
	}
	
	//<nc:Release structures:id="Release_01">
	//	<nc:ActivityDate>
	//		<nc:DateTime>2001-12-17T09:30:47.0Z</nc:DateTime>
	//	</nc:ActivityDate>
	//</nc:Release>
	public static void createRelease(Element rootElement, String releaseDateTime, String activityDescriptionTextString, String activityReasonTextString, String personRef, String locationRef) {
		Element release =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "nc:Release");	
		XmlUtils.addAttribute(release, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Release_01");	
		
		if (StringUtils.isNotBlank(releaseDateTime))
		{	
			Element activityDate = XmlUtils.appendElement(release, OjbcNamespaceContext.NS_NC_30, "nc:ActivityDate");
			
			Element dateTime = XmlUtils.appendElement(activityDate, OjbcNamespaceContext.NS_NC_30, "nc:DateTime");
			dateTime.setTextContent(releaseDateTime);
		}	
		
		if (StringUtils.isNotBlank(activityDescriptionTextString))
		{	
			Element activityDescriptionText = XmlUtils.appendElement(release, OjbcNamespaceContext.NS_NC_30, "nc:ActivityDescriptionText");
			activityDescriptionText.setTextContent(activityDescriptionTextString);
		}	

		if (StringUtils.isNotBlank(activityReasonTextString))
		{	
			Element activityReasonText = XmlUtils.appendElement(release, OjbcNamespaceContext.NS_NC_30, "nc:ActivityReasonText");
			activityReasonText.setTextContent(activityReasonTextString);

		}	

		if (StringUtils.isNotBlank(personRef))
		{	
			Element releasePerson =  XmlUtils.appendElement(release, OjbcNamespaceContext.NS_NC_30, "nc:ReleasePerson");			
			XmlUtils.addAttribute(releasePerson, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", personRef);		
		}	

		if (StringUtils.isNotBlank(locationRef))
		{	
			Element releaseLocation =  XmlUtils.appendElement(release, OjbcNamespaceContext.NS_NC_30, "nc:ReleaseToLocation");			
			XmlUtils.addAttribute(releaseLocation, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", locationRef);		
		}	

	}

	public static Element createCustody(Element rootElement, String extensionNamespace) {
		Element custody =  XmlUtils.appendElement(rootElement, extensionNamespace, "Custody");		
		return custody;
	}

	public static void createPerson(Element rootElement, String personID, String personDob, String personEducationLevelText, String occupation, String language, String militaryServiceStatusCode, String sEyeColor, String sHairColor, String sHeight, String sHeightUnits, 
			String personGivenName, String sPersonMiddleName, String personSurName, String sRaceCode, String sPersonSexCode,
			String sPersonWeight, String sDriversLicenseId, String driversLicenseSourceTxt, String sFbiId, String sPersonSid, String personEthnicity) {

		Element person =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "Person");			
		XmlUtils.addAttribute(person, OjbcNamespaceContext.NS_STRUCTURES_30, "id", personID);		
		
		if(StringUtils.isNotBlank(personDob)){
			Element personDobElement = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonBirthDate");
			
			Element personDobValueElement = XmlUtils.appendElement(personDobElement, OjbcNamespaceContext.NS_NC_30, "Date");		
			personDobValueElement.setTextContent(personDob);						
		}
		
		if(StringUtils.isNotBlank(personEducationLevelText)){
			Element personEducationElement = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonEducationLevelText");
			personEducationElement.setTextContent(personEducationLevelText);
		}
		
		if(StringUtils.isNotBlank(personEthnicity)){
			Element personEthnicityElement = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_JXDM_51, "PersonEthnicityCode");
			personEthnicityElement.setTextContent(personEthnicity);
		}
		
		if(StringUtils.isNotBlank(sEyeColor)){
			Element eyeColorElement = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonEyeColorText");
			eyeColorElement.setTextContent(sEyeColor);
		}
		
		if(StringUtils.isNotBlank(sHairColor)){
			Element hairColor = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonHairColorText");	
			hairColor.setTextContent(sHairColor);			
		}
		if(StringUtils.isNotBlank(sHeight)){
			Element personHeightEl = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonHeightMeasure");

			Element personHeightMeasureValTxtEl = XmlUtils.appendElement(personHeightEl, OjbcNamespaceContext.NS_NC_30, "MeasureValueText");
			personHeightMeasureValTxtEl.setTextContent(sHeight);	
						
			if(StringUtils.isNotBlank(sHeightUnits)){
				Element heightUnitTxtEl = XmlUtils.appendElement(personHeightEl, OjbcNamespaceContext.NS_NC_30, "MeasureUnitText");
				heightUnitTxtEl.setTextContent(sHeightUnits);				
			}									
		}
		
		if(StringUtils.isNotBlank(militaryServiceStatusCode)){
			Element personMilitarySummaryElement = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonMilitarySummary");
			
			Element militaryServiceStatusCodeElement = XmlUtils.appendElement(personMilitarySummaryElement, OjbcNamespaceContext.NS_ADAMS_CO_BOOKING_CODES_EXT, "MilitaryServiceStatusCode");		
			militaryServiceStatusCodeElement.setTextContent(militaryServiceStatusCode);						
		}
		
		boolean hasPersonGivenName = StringUtils.isNotBlank(personGivenName);
		boolean hasPersonMiddleName = StringUtils.isNotBlank(sPersonMiddleName);
		boolean hasPersonSurName = StringUtils.isNotBlank(personSurName);

		if(hasPersonGivenName || hasPersonMiddleName || hasPersonSurName){
			Element personNameElement = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonName");	

			if(hasPersonGivenName){
				Element personGivenNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonGivenName");
				personGivenNameElement.setTextContent(personGivenName);
			}
			
			if(hasPersonMiddleName){
				Element personMiddleNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonMiddleName");
				personMiddleNameElement.setTextContent(sPersonMiddleName);				
			}
			
			if(hasPersonSurName){
				Element personSurNameElement = XmlUtils.appendElement(personNameElement, OjbcNamespaceContext.NS_NC_30, "PersonSurName");		
				personSurNameElement.setTextContent(personSurName);					
			}			
		}
		
		if(StringUtils.isNotBlank(language)){
			Element personPrimaryLanguage = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonPrimaryLanguage");

			Element languageName = XmlUtils.appendElement(personPrimaryLanguage, OjbcNamespaceContext.NS_NC_30, "LanguageName");
			languageName.setTextContent(language);					
		}

		if(StringUtils.isNotBlank(sRaceCode)){
			Element personRaceCodeElement = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_ADAMS_CO_BOOKING_CODES_EXT, "PersonRaceCode");
			personRaceCodeElement.setTextContent(sRaceCode);			
		}
		
		if(StringUtils.isNotBlank(sPersonSexCode)){
			Element personSexCode = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_JXDM_51, "PersonSexCode");
			personSexCode.setTextContent(sPersonSexCode);			
		}
		
		if(StringUtils.isNotBlank(sPersonWeight)){
			Element personWeightMeasure = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "PersonWeightMeasure");

			Element measureValTxt = XmlUtils.appendElement(personWeightMeasure, OjbcNamespaceContext.NS_NC_30, "MeasureValueText");
			measureValTxt.setTextContent(sPersonWeight);					
		}
		
		
		Element personAugmentation = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_JXDM_51, "PersonAugmentation");
		
		if(StringUtils.isNotBlank(occupation)){
			Element occupationElement = XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_NC_30, "EmployeeOccupationCategoryText");
			occupationElement.setTextContent(occupation);
		}
		
		if(StringUtils.isNotBlank(sDriversLicenseId)){
			Element driverLicense = XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_JXDM_51, "DriverLicense");
			Element driverLicenseId = XmlUtils.appendElement(driverLicense, OjbcNamespaceContext.NS_JXDM_51, "DriverLicenseIdentification");

			Element driverLicenseIdVal = XmlUtils.appendElement(driverLicenseId, OjbcNamespaceContext.NS_NC_30, "IdentificationID"); 					
			driverLicenseIdVal.setTextContent(sDriversLicenseId);	
			
			if(StringUtils.isNotBlank(driversLicenseSourceTxt)){
				
				Element drivLicIdSrcTxtElement = XmlUtils.appendElement(driverLicenseId, OjbcNamespaceContext.NS_NC_30, "IdentificationSourceText");
				
				driversLicenseSourceTxt = driversLicenseSourceTxt.trim();
				
				drivLicIdSrcTxtElement.setTextContent(driversLicenseSourceTxt);								
			}
		}
		
		if(StringUtils.isNotBlank(sFbiId)){
			Element personFBIId = XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_JXDM_51, "PersonFBIIdentification");

			Element personFBIIdVal = XmlUtils.appendElement(personFBIId, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
			personFBIIdVal.setTextContent(sFbiId);			
		}
		
		if(StringUtils.isNotBlank(sPersonSid)){
			Element personSid = XmlUtils.appendElement(personAugmentation, OjbcNamespaceContext.NS_JXDM_51, "PersonStateFingerprintIdentification");

			Element personSidVal = XmlUtils.appendElement(personSid, OjbcNamespaceContext.NS_NC_30, "IdentificationID");
			personSidVal.setTextContent(sPersonSid);				
		}
		
	}

	//<br-ext:BehavioralHealthInformation>
	//	<br-ext:SeriousMentalIllnessIndicator>true</br-ext:SeriousMentalIllnessIndicator>
	//	<j:Evaluation>
	//		<j:EvaluationDiagnosisDescriptionText>schizophrenia 295.10</j:EvaluationDiagnosisDescriptionText>
	//	</j:Evaluation>
	//</br-ext:BehavioralHealthInformation>	

	public static void createBehaviorHealthInformation(Element rootElement,
			String extensionNamespace, Boolean smiIndicator, String evaluationDiagnosisDescriptionTextString) {

		Element behavioralHealthInformation =  XmlUtils.appendElement(rootElement,extensionNamespace, "BehavioralHealthInformation");
		
		if (smiIndicator != null)
		{	
			Element seriousMentalIllnessIndicator =  XmlUtils.appendElement(behavioralHealthInformation,extensionNamespace, "SeriousMentalIllnessIndicator");
			seriousMentalIllnessIndicator.setTextContent(String.valueOf(smiIndicator));
		}	
		
		if (StringUtils.isNotBlank(evaluationDiagnosisDescriptionTextString))
		{
			Element evaluation =  XmlUtils.appendElement(behavioralHealthInformation,OjbcNamespaceContext.NS_JXDM_51, "j:Evaluation");
			
			Element evaluationDiagnosisDescriptionText =  XmlUtils.appendElement(evaluation,OjbcNamespaceContext.NS_JXDM_51, "j:EvaluationDiagnosisDescriptionText");
			evaluationDiagnosisDescriptionText.setTextContent(evaluationDiagnosisDescriptionTextString);
		}	
		
	}

	//<nc:ContactInformationAssociation>
	//	<nc:ContactEntity structures:ref="Supervisor_01" />
	//	<nc:ContactInformation structures:ref="CI_01" />
	//</nc:ContactInformationAssociation>
	public static void createPersonContactInformationAssociation(
			Element rootElement, String supervisionReference, String contactInfoReference) {
    	Element contactInformationAssociation =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "nc:ContactInformationAssociation");
    	
    	Element contactEntity =  XmlUtils.appendElement(contactInformationAssociation, OjbcNamespaceContext.NS_NC_30, "nc:ContactEntity");
    	XmlUtils.addAttribute(contactEntity, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", supervisionReference);
    	
    	Element contactInformation =  XmlUtils.appendElement(contactInformationAssociation, OjbcNamespaceContext.NS_NC_30, "nc:ContactInformation");
    	XmlUtils.addAttribute(contactInformation, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", contactInfoReference);		
	}

	//<nc:PersonAliasIdentityAssociation>
	//	<nc:Person structures:ref="Person_01" />
	//	<nc:Identity structures:ref="ALIAS001" />
	//	<nc:Identity structures:ref="ALIAS002" />
	//	<nc:Identity structures:ref="ALIAS003" />
	//</nc:PersonAliasIdentityAssociation>	
	public static void createPersonAliasIdentityAssociation(
			Element rootElement, List<String> aliasReferences, String personReference) {
		
		Element personAliasIdentityAssociation =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "nc:PersonAliasIdentityAssociation");

		Element person =  XmlUtils.appendElement(personAliasIdentityAssociation, OjbcNamespaceContext.NS_NC_30, "nc:Person");
		XmlUtils.addAttribute(person, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", personReference);		
		
		for (String aliasReference : aliasReferences)
		{
			Element identity = XmlUtils.appendElement(personAliasIdentityAssociation,  OjbcNamespaceContext.NS_NC_30, "nc:Identity");
			XmlUtils.addAttribute(identity, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", aliasReference);
		}	
		
	}

	//<j:BailBondChargeAssociation>
//	<j:BailBond structures:ref="Bond_01" />
//	<j:Charge structures:ref="Charge_01" />
//</j:BailBondChargeAssociation>	
    public static void createBailBondChargeAssocation(Element rootElement, String bailBondReference, String chargeReference) {

    	Element bailBondChargeAssociation =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_JXDM_51, "j:BailBondChargeAssociation");
    	
    	Element bailBond =  XmlUtils.appendElement(bailBondChargeAssociation, OjbcNamespaceContext.NS_JXDM_51, "j:BailBond");
    	XmlUtils.addAttribute(bailBond, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", bailBondReference);
    	
    	Element charge =  XmlUtils.appendElement(bailBondChargeAssociation, OjbcNamespaceContext.NS_JXDM_51, "j:Charge");
    	XmlUtils.addAttribute(charge, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", chargeReference);
	}	
	
//<j:ActivityChargeAssociation>
//	<nc:Activity structures:ref="Case_01" />
//	<nc:Activity structures:ref="Booking_01" />
//	<nc:Activity structures:ref="Event_01" />
//	<nc:Activity structures:ref="Detention_01" />
//	<nc:Activity structures:ref="Arrest_01" />
//	<nc:Activity structures:ref="CHS_01" />
//	<j:Charge structures:ref="Charge_01" />
//</j:ActivityChargeAssociation>	

	public static void createActivityChargeAssociation(Element rootElement, List<String> activityReferences, String chargeReference) {
		Element activityChargeAssociation =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_JXDM_51, "j:ActivityChargeAssociation");

		for (String activityReference : activityReferences)
		{
			Element activity = XmlUtils.appendElement(activityChargeAssociation,  OjbcNamespaceContext.NS_NC_30, "nc:Activity");
			XmlUtils.addAttribute(activity, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", activityReference);
		}	
		
		Element charge =  XmlUtils.appendElement(activityChargeAssociation, OjbcNamespaceContext.NS_JXDM_51, "j:Charge");
		XmlUtils.addAttribute(charge, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", chargeReference);

	}
	
	//	<j:ActivityCaseAssociation>
	//		<nc:Activity structures:ref="Booking_01" />
	//		<nc:Activity structures:ref="Detention_01" />
	//		<nc:Activity structures:ref="Release_01" />
	//		<nc:Case structures:ref="Case_01" />
	//	</j:ActivityCaseAssociation>	
	public static void createActivityCaseAssociation(Element rootElement, List<String> activityReferences, String caseReference) {
		Element activityCaseAssociation =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_JXDM_51, "j:ActivityCaseAssociation");

		for (String activityReference : activityReferences)
		{
			Element activity = XmlUtils.appendElement(activityCaseAssociation,  OjbcNamespaceContext.NS_NC_30, "nc:Activity");
			XmlUtils.addAttribute(activity, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", activityReference);
		}	
		
		Element caseElement =  XmlUtils.appendElement(activityCaseAssociation, OjbcNamespaceContext.NS_NC_30, "nc:Case");
		XmlUtils.addAttribute(caseElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", caseReference);

	}	
	
	
	//<nc:ContactInformation structures:id="CI_01">
	//	<nc:ContactEmailID>officer@agency.gov</nc:ContactEmailID>
	//	<nc:ContactTelephoneNumber>
	//		<nc:FullTelephoneNumber>
	//			<nc:TelephoneNumberFullID>312-216-9923</nc:TelephoneNumberFullID>
	//		</nc:FullTelephoneNumber>
	//		<nc:TelephoneNumberCategoryText>Cell</nc:TelephoneNumberCategoryText>
	//	</nc:ContactTelephoneNumber>
	//</nc:ContactInformation>

	public static void createContactInformation(Element rootElement, String email, String telephoneNumber, String telephoneNumberCategoryTextString) {

	    Element contactInformation = XmlUtils.appendElement(rootElement, 
	            OjbcNamespaceContext.NS_NC_30, "ContactInformation");
	    XmlUtils.addAttribute(contactInformation, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "CI_01"); 
	    
	    Element contactEmailID = XmlUtils.appendElement(contactInformation, 
	            OjbcNamespaceContext.NS_NC_30, "ContactEmailID");
	    contactEmailID.setTextContent(email);
	    	
		Element contactTelephoneNumber = XmlUtils.appendElement(contactInformation, 
	            OjbcNamespaceContext.NS_NC_30, "ContactTelephoneNumber");
	    Element fullTelephoneNumber = XmlUtils.appendElement(contactTelephoneNumber, 
	            OjbcNamespaceContext.NS_NC_30, "FullTelephoneNumber");
	    Element telephoneNumberFullID = XmlUtils.appendElement(fullTelephoneNumber, 
	            OjbcNamespaceContext.NS_NC_30, "TelephoneNumberFullID");
	    telephoneNumberFullID.setTextContent(telephoneNumber);
	    
	    Element telephoneNumberCategoryText  = XmlUtils.appendElement(contactTelephoneNumber, 
	            OjbcNamespaceContext.NS_NC_30, "TelephoneNumberCategoryText");
	    telephoneNumberCategoryText.setTextContent(telephoneNumberCategoryTextString);
	}

    
	//<nc:Location structures:id="Loc_01">
	//	<nc:Address>
	//		<nc:AddressFullText>30 Main Street Denton, Colorada
	//		</nc:AddressFullText>
	//		<nc:LocationStreet>
	//			<nc:StreetNumberText>392</nc:StreetNumberText>
	//			<nc:StreetName>Woodlawn Ave
	//			</nc:StreetName>
	//		</nc:LocationStreet>
	//		<nc:LocationCityName>Burlington</nc:LocationCityName>
	//		<nc:LocationStateUSPostalServiceCode>NY
	//		</nc:LocationStateUSPostalServiceCode>
	//		<nc:LocationPostalCode>05408</nc:LocationPostalCode>
	//	</nc:Address>
	//	<nc:Location2DGeospatialCoordinate>
	//		<nc:GeographicCoordinateLatitude>
	//			<nc:LatitudeDegreeValue>56.1111</nc:LatitudeDegreeValue>
	//		</nc:GeographicCoordinateLatitude>
	//		<nc:GeographicCoordinateLongitude>
	//			<nc:LongitudeDegreeValue>32.1111</nc:LongitudeDegreeValue>
	//		</nc:GeographicCoordinateLongitude>
	//	</nc:Location2DGeospatialCoordinate>
	//</nc:Location>	
	
	
    public static void createLocation(Element rootElement, String streetNumberText, String streetNameText, String cityNameText, String stateNameText, String postalCodeText, String locationReference) {
        
            Element locationElement = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "Location");
    		XmlUtils.addAttribute(locationElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", locationReference); 
            
            Element address = XmlUtils.appendElement(locationElement, OjbcNamespaceContext.NS_NC_30, "Address");
            Element streetElement = XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationStreet");
            
            Element streetNumber = XmlUtils.appendElement(streetElement, OjbcNamespaceContext.NS_NC_30, "StreetNumberText");
            streetNumber.setTextContent(streetNumberText);
            
            Element streetName = XmlUtils.appendElement(streetElement, OjbcNamespaceContext.NS_NC_30, "StreetName");
            streetName.setTextContent(streetNameText);
            
            Element cityName = XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationCityName");
            cityName.setTextContent(cityNameText);
            
            Element state = XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationStateUSPostalServiceCode");
            state.setTextContent(stateNameText);
            
            Element postalCode = XmlUtils.appendElement(address, OjbcNamespaceContext.NS_NC_30, "LocationPostalCode");
            postalCode.setTextContent(postalCodeText);
		
	}

	public static void createPersonIdentity(Element rootElement, String firstName, String lastName, String personSexCodeString, String birthDate, String id) {

		Element personIdentity = XmlUtils.appendElement(rootElement, 
	            OjbcNamespaceContext.NS_NC_30, "Identity"); 

		XmlUtils.addAttribute(personIdentity, 
	            OjbcNamespaceContext.NS_STRUCTURES_30, "id", id); 
		
	    Element identityPersonRepresentation = XmlUtils.appendElement(personIdentity, 
	            OjbcNamespaceContext.NS_NC_30, "IdentityPersonRepresentation"); 
	    
		if (StringUtils.isNotBlank(birthDate))
		{	
			Element activityDate = XmlUtils.appendElement(identityPersonRepresentation, OjbcNamespaceContext.NS_NC_30, "nc:PersonBirthDate");
			
			Element dateTime = XmlUtils.appendElement(activityDate, OjbcNamespaceContext.NS_NC_30, "nc:Date");
			dateTime.setTextContent(birthDate);
		}		    
	    
	    Element personName = XmlUtils.appendElement(identityPersonRepresentation, 
	            OjbcNamespaceContext.NS_NC_30, "PersonName");
	    
	    Element personGivenName = XmlUtils.appendElement(personName, 
	            OjbcNamespaceContext.NS_NC_30, "PersonGivenName");
	    personGivenName.setTextContent(firstName);
	    
	    Element personSurName = XmlUtils.appendElement(personName, 
	            OjbcNamespaceContext.NS_NC_30, "PersonSurName");
	    personSurName.setTextContent(lastName);

	    Element personSexCode = XmlUtils.appendElement(identityPersonRepresentation, 
	            OjbcNamespaceContext.NS_JXDM_51, "j:PersonSexCode");
	    personSexCode.setTextContent(personSexCodeString);
	}
	
	
	//<nc:Person>
	//	<nc:PersonName structures:id="Officer_01">
	//		<nc:PersonFullName>Officer Full Name</nc:PersonFullName>
	//	</nc:PersonName>
	//</nc:Person>	
	public static void createPersonOfficer(Element rootElement, String officerFullName) {
		Element person = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "nc:Person");
		
		Element personName = XmlUtils.appendElement(person, OjbcNamespaceContext.NS_NC_30, "nc:PersonName");
		XmlUtils.addAttribute(personName, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Officer_01");
		
		Element personFullName = XmlUtils.appendElement(personName, OjbcNamespaceContext.NS_NC_30, "nc:PersonFullName");
		personFullName.setTextContent(officerFullName);
		
	}	
	
	
	//<cyfs:NextCourtEvent structures:id="Event_01">
	//	<nc:ActivityDate>
	//		<nc:Date>1968-12-17</nc:Date>
	//	</nc:ActivityDate>
	//	<j:CourtEventCourt>
	//		<j:CourtName>Court Name</j:CourtName>
	//	</j:CourtEventCourt>
	//</cyfs:NextCourtEvent>	
	public static void createNextCourtEvent(Element rootElement,
			String courtDate, String courtNameString, String eventID) {
		
		Element nextCourtEvent =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_CYFS_31, "cyfs:NextCourtEvent");
		XmlUtils.addAttribute(nextCourtEvent, OjbcNamespaceContext.NS_STRUCTURES_30, "id", eventID);
		
		if (StringUtils.isNotBlank(courtDate))
		{	
			Element activityDate = XmlUtils.appendElement(nextCourtEvent, OjbcNamespaceContext.NS_NC_30, "nc:ActivityDate");
			
			Element dateTime = XmlUtils.appendElement(activityDate, OjbcNamespaceContext.NS_NC_30, "nc:Date");
			dateTime.setTextContent(courtDate);
		}	

		Element courtEventCourt =  XmlUtils.appendElement(nextCourtEvent, OjbcNamespaceContext.NS_JXDM_51, "j:CourtEventCourt");

		Element courtName =  XmlUtils.appendElement(courtEventCourt, OjbcNamespaceContext.NS_JXDM_51, "j:CourtName");
		courtName.setTextContent(courtNameString);
		
	}

	//<j:PersonCriminalHistorySummary
	//	structures:id="CHS_01">
	//	<j:RegisteredSexualOffenderIndicator>false</j:RegisteredSexualOffenderIndicator>
	//</j:PersonCriminalHistorySummary>
	public static void createPersonCriminalHistorySummary(Element rootElement,
			Boolean sexOffenderIndicator) {
		
		Element personCriminalHistorySummary =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_JXDM_51, "j:PersonCriminalHistorySummary");
		XmlUtils.addAttribute(personCriminalHistorySummary, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "CHS_01");
		
		if (sexOffenderIndicator != null)
		{	
			Element registeredSexualOffenderIndicator =  XmlUtils.appendElement(personCriminalHistorySummary, OjbcNamespaceContext.NS_JXDM_51, "j:RegisteredSexualOffenderIndicator");
			registeredSexualOffenderIndicator.setTextContent(String.valueOf(sexOffenderIndicator));
		}	
		
		
	}
	
	
	
	//<j:Arrest structures:id="Arrest_01">
	//	<j:ArrestAgency>
	//		<nc:OrganizationName>Arrest Agency name</nc:OrganizationName>
	//	</j:ArrestAgency>
	//   <j:ArrestCharge structures:ref="Charge_01" />
	//	<j:ArrestLocation structures:ref="Loc_01" />
	//</j:Arrest>	
	public static void createArrest(Element rootElement, String arrestAgencyString, String arrestLocationReferenceString, String arrestReferenceString, Arrest arrest, String chargeReferenceBase) {
		
		Element arrestElement =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_JXDM_51, "j:Arrest");
		XmlUtils.addAttribute(arrestElement, OjbcNamespaceContext.NS_STRUCTURES_30, "id", arrestReferenceString);
		
		Element arrestAgency =  XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "j:ArrestAgency");

		Element organizationName = XmlUtils.appendElement(arrestAgency, OjbcNamespaceContext.NS_NC_30, "nc:OrganizationName");
		organizationName.setTextContent(arrestAgencyString);
		
		if (arrest != null && arrest.getChargeList().size() > 0)
		{	
			for (int i = 1;  i < arrest.getChargeList().size() + 1; i++)
			{
				Element chargeElement =  XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "j:ArrestCharge");
				XmlUtils.addAttribute(chargeElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", chargeReferenceBase + i);
			}	
		}	
		
		Element arrestLocation =  XmlUtils.appendElement(arrestElement, OjbcNamespaceContext.NS_JXDM_51, "j:ArrestLocation");
		XmlUtils.addAttribute(arrestLocation, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", arrestLocationReferenceString);

	}
	
//<j:Charge structures:id="Charge_01">
//	<j:ChargeSeverityText>Felony</j:ChargeSeverityText>
//	<j:ChargeDescriptionText>Charge Description</j:ChargeDescriptionText>
//	<j:ChargeDisposition>
//		<nc:DispositionText>Disposition</nc:DispositionText>
//	</j:ChargeDisposition>	
//	<j:ChargeHighestIndicator>true</j:ChargeHighestIndicator>
//	<j:ChargeSequenceID>Charge Sequence ID</j:ChargeSequenceID>
//	<j:ChargeStatute>
//		<j:StatuteCodeSectionIdentification>
//			<nc:IdentificationID>Charge Code ID</nc:IdentificationID>
//		</j:StatuteCodeSectionIdentification>
//	</j:ChargeStatute>
//  <br-ext:ChargeJurisdictionCourt>
//   	<j:CourtName>Smithville</j:CourtName>
//    </br-ext:ChargeJurisdictionCourt>	
//	<cscr-ext:HoldForAgency>
//		<nc:OrganizationName>Agency</nc:OrganizationName>
//	</cscr-ext:HoldForAgency>	
//</j:Charge>
	public static void createCharge(Element rootElement, String extensionNamespace, String chargeReference, String holdForAgency, String chargeCategoryDescriptionTextString, String chargeDescriptionText,  
			String chargeHighestIndicatorString, String chargeSequenceIDString, String chargeStatute, String chargeDisposition, String chargeJurisdictionCourt) {
		Element charge =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_JXDM_51, "j:Charge");
		XmlUtils.addAttribute(charge, OjbcNamespaceContext.NS_STRUCTURES_30, "id", chargeReference);

		if (StringUtils.isNotBlank(chargeDescriptionText))
		{
			Element chargeDescriptionTextElement =  XmlUtils.appendElement(charge, OjbcNamespaceContext.NS_JXDM_51, "j:ChargeDescriptionText");
			chargeDescriptionTextElement.setTextContent(chargeDescriptionText);
		}	

		if (StringUtils.isNotBlank(chargeDisposition))
		{
			Element chargeDispositionElement =  XmlUtils.appendElement(charge, OjbcNamespaceContext.NS_JXDM_51, "j:ChargeDisposition");
			
			Element dispositionTextElement =  XmlUtils.appendElement(chargeDispositionElement, OjbcNamespaceContext.NS_NC_30, "nc:DispositionText");
			dispositionTextElement.setTextContent(chargeDisposition);
		}	
		
		if (StringUtils.isNotBlank(chargeHighestIndicatorString))
		{
			Element chargeHighestIndicator =  XmlUtils.appendElement(charge, OjbcNamespaceContext.NS_JXDM_51, "j:ChargeHighestIndicator");
			chargeHighestIndicator.setTextContent(chargeHighestIndicatorString);
		}	

		if (StringUtils.isNotBlank(chargeSequenceIDString))
		{
			Element chargeSequenceID =  XmlUtils.appendElement(charge, OjbcNamespaceContext.NS_JXDM_51, "j:ChargeSequenceID");
			chargeSequenceID.setTextContent(chargeSequenceIDString);
		}	

		if (StringUtils.isNotBlank(chargeCategoryDescriptionTextString))
		{
			Element chargeSeverityText =  XmlUtils.appendElement(charge, OjbcNamespaceContext.NS_JXDM_51, "j:ChargeSeverityText");
			chargeSeverityText.setTextContent(chargeCategoryDescriptionTextString);
		}	

		
		if (StringUtils.isNotBlank(chargeStatute))
		{
			Element chargeStatuteElement =  XmlUtils.appendElement(charge, OjbcNamespaceContext.NS_JXDM_51, "j:ChargeStatute");
			Element statuteCodeSectionIdentification =  XmlUtils.appendElement(chargeStatuteElement, OjbcNamespaceContext.NS_JXDM_51, "j:StatuteCodeSectionIdentification");
			
			Element identificationID = XmlUtils.appendElement(statuteCodeSectionIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
			identificationID.setTextContent(chargeStatute);
			
		}	

		if (StringUtils.isNotBlank(chargeJurisdictionCourt))
		{
			Element holdForAgencyElement =  XmlUtils.appendElement(charge, extensionNamespace, "ChargeJurisdictionCourt");
			
			Element identificationID = XmlUtils.appendElement(holdForAgencyElement, OjbcNamespaceContext.NS_JXDM_51, "j:CourtName");
			identificationID.setTextContent(chargeJurisdictionCourt);
			
		}	

		if (StringUtils.isNotBlank(holdForAgency))
		{
			Element holdForAgencyElement =  XmlUtils.appendElement(charge, extensionNamespace, "HoldForAgency");
			
			Element identificationID = XmlUtils.appendElement(holdForAgencyElement, OjbcNamespaceContext.NS_NC_30, "nc:OrganizationName");
			identificationID.setTextContent(holdForAgency);
			
		}	

	}

	//<j:BailBond structures:id="Bond_01">
//	<nc:ActivityCategoryText>Bond Type</nc:ActivityCategoryText>
//	<nc:ActivityStatus>
//		<nc:StatusDescriptionText>Bond Status</nc:StatusDescriptionText>
//	</nc:ActivityStatus>
//	<j:BailBondAmount>
//		<nc:Amount>500.00</nc:Amount>
//	</j:BailBondAmount>
//</j:BailBond>
	public static void createBailBond(Element rootElement, String bondType, String bondStatus, String bondAmount, String bondReference) {
		Element bailBond =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_JXDM_51, "j:BailBond");
		XmlUtils.addAttribute(bailBond, OjbcNamespaceContext.NS_STRUCTURES_30, "id", bondReference);

		if (StringUtils.isNotBlank(bondType))
		{
			Element activityCategoryText = XmlUtils.appendElement(bailBond, OjbcNamespaceContext.NS_NC_30, "nc:ActivityCategoryText");
			activityCategoryText.setTextContent(bondType);
		}	
		
		if (StringUtils.isNotBlank(bondStatus))
		{
			if (!bondStatus.equals("`"))
			{	
				Element activityStatus = XmlUtils.appendElement(bailBond, OjbcNamespaceContext.NS_NC_30, "nc:ActivityStatus");
				
				Element statusDescriptionText = XmlUtils.appendElement(activityStatus, OjbcNamespaceContext.NS_NC_30, "nc:StatusDescriptionText");
				statusDescriptionText.setTextContent(bondStatus);
			}	
			
		}	

		if (StringUtils.isNotBlank(bondAmount))
		{
			Element bailBondAmount =  XmlUtils.appendElement(bailBond, OjbcNamespaceContext.NS_JXDM_51, "j:BailBondAmount");
			
			Element amount = XmlUtils.appendElement(bailBondAmount, OjbcNamespaceContext.NS_NC_30, "nc:Amount");
			amount.setTextContent(bondAmount);

		}	

	}	

	public static void createDetention(Element rootElement, String extensionNamespace, String detentionDateTime, String detentionDate, String custodyStatus, String supervisorCategoryTextString, 
			String supervisorStatusString, String detentiontImmigrationHoldIndicatorString, String inmateWorkReleaseIndicatorString, 
			String inmateWorkerIndicatorString, String allowAccountDepositIndicatorString, String supervisionReleaseEligibilityDateString,
			String supervisionReleaseDateString, String supervisionAreaIdentificationString, String bed, String bedCategory, String cell, String supervisionCondition, String inmateJailResidentIndicator) {
		Element detention =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_JXDM_51, "j:Detention");
		XmlUtils.addAttribute(detention, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Detention_01");

		Element activityDate = XmlUtils.appendElement(detention, OjbcNamespaceContext.NS_NC_30, "nc:ActivityDate");
		
		if (StringUtils.isNotBlank(detentionDateTime))
		{	
			Element dateTime = XmlUtils.appendElement(activityDate, OjbcNamespaceContext.NS_NC_30, "nc:DateTime");
			dateTime.setTextContent(detentionDateTime);
		}	

		if (StringUtils.isNotBlank(detentionDate))
		{	
			Element dateTime = XmlUtils.appendElement(activityDate, OjbcNamespaceContext.NS_NC_30, "nc:Date");
			dateTime.setTextContent(detentionDate);
		}	

		
		if (StringUtils.isNotBlank(custodyStatus))
		{	
			Element supervisionCustodyStatus = XmlUtils.appendElement(detention, OjbcNamespaceContext.NS_NC_30, "nc:SupervisionCustodyStatus");
			
			Element statusDescriptionText = XmlUtils.appendElement(supervisionCustodyStatus, OjbcNamespaceContext.NS_NC_30, "nc:StatusDescriptionText");
			statusDescriptionText.setTextContent(custodyStatus);
		}	

		if (StringUtils.isNotBlank(supervisorCategoryTextString) || StringUtils.isNotBlank(supervisorStatusString))
		{	
			Element supervisionSupervisor = XmlUtils.appendElement(detention, OjbcNamespaceContext.NS_NC_30, "nc:SupervisionSupervisor");
			
			Element entityPerson = XmlUtils.appendElement(supervisionSupervisor, OjbcNamespaceContext.NS_NC_30, "nc:EntityPerson");
			XmlUtils.addAttribute(entityPerson, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Officer_01");
			
			Element supervisorCategoryText = XmlUtils.appendElement(supervisionSupervisor, extensionNamespace, "SupervisorCategoryText");
			supervisorCategoryText.setTextContent(supervisorCategoryTextString);
			
			Element supervisorStatus = XmlUtils.appendElement(supervisionSupervisor, extensionNamespace, "SupervisorStatus");
			
			Element statusDescriptionText = XmlUtils.appendElement(supervisorStatus,  OjbcNamespaceContext.NS_NC_30, "nc:StatusDescriptionText");
			statusDescriptionText.setTextContent(supervisorStatusString);
		}
		
//		<br-ext:DetentiontImmigrationHoldIndicator>false</br-ext:DetentiontImmigrationHoldIndicator>
//		<br-ext:InmateWorkReleaseIndicator>true</br-ext:InmateWorkReleaseIndicator>
//		<br-ext:InmateWorkerIndicator>false</br-ext:InmateWorkerIndicator>
//		<br-ext:AllowAccountDepositIndicator>true</br-ext:AllowAccountDepositIndicator>
		
		if (StringUtils.isNotBlank(detentiontImmigrationHoldIndicatorString))
		{
			Element detentiontImmigrationHoldIndicator = XmlUtils.appendElement(detention, extensionNamespace, "DetentiontImmigrationHoldIndicator");
			detentiontImmigrationHoldIndicator.setTextContent(detentiontImmigrationHoldIndicatorString);
		}

		if (StringUtils.isNotBlank(inmateWorkReleaseIndicatorString))
		{
			Element inmateWorkReleaseIndicator = XmlUtils.appendElement(detention, extensionNamespace, "InmateWorkReleaseIndicator");
			inmateWorkReleaseIndicator.setTextContent(inmateWorkReleaseIndicatorString);
		}	

		if (StringUtils.isNotBlank(inmateWorkerIndicatorString))
		{
			Element inmateWorkerIndicator = XmlUtils.appendElement(detention, extensionNamespace, "InmateWorkerIndicator");
			inmateWorkerIndicator.setTextContent(inmateWorkerIndicatorString);
		}	

		if (StringUtils.isNotBlank(allowAccountDepositIndicatorString))
		{
			Element allowAccountDepositIndicator = XmlUtils.appendElement(detention, extensionNamespace, "AllowAccountDepositIndicator");
			allowAccountDepositIndicator.setTextContent(allowAccountDepositIndicatorString);
		}	

	//	<j:SupervisionAugmentation>
	//		<j:SupervisionReleaseEligibilityDate>
	//			<nc:Date>2001-12-17</nc:Date>
	//		</j:SupervisionReleaseEligibilityDate>
	//		<j:SupervisionReleaseDate>
	//		<nc:DateTime>2014-12-17T10:30:00</nc:DateTime>
	//	</j:SupervisionReleaseDate>
	//	<j:SupervisionAreaIdentification>
	//		<nc:IdentificationID>Cell Block A</nc:IdentificationID>
	//	</j:SupervisionAreaIdentification>
	//	<j:SupervisionBedIdentification>
	//		<nc:IdentificationID>Bed 2</nc:IdentificationID>
	//		<ac-bkg-codes:BedCategoryCode>Type 2</ac-bkg-codes:BedCategoryCode>
	//	</j:SupervisionBedIdentification>
	//	<j:SupervisionCellIdentification>
	//		<nc:IdentificationID>Cell 7</nc:IdentificationID>
	//	</j:SupervisionCellIdentification>
	//	<j:SupervisionCondition>
	//		<nc:ActivityDescriptionText>Supervision Condition
	//		</nc:ActivityDescriptionText>
	//	</j:SupervisionCondition>
		
//	</j:SupervisionAugmentation>
		//TODO add additional supervision augmentation data here
		if (StringUtils.isNotEmpty(supervisionReleaseEligibilityDateString) || StringUtils.isNotEmpty(supervisionReleaseDateString) || StringUtils.isNotEmpty(supervisionAreaIdentificationString)
				|| StringUtils.isNotEmpty(bed) || StringUtils.isNotEmpty(bedCategory) || StringUtils.isNotEmpty(supervisionCondition))
		{
			Element supervisionAugmentation =  XmlUtils.appendElement(detention, OjbcNamespaceContext.NS_JXDM_51, "j:SupervisionAugmentation");
			
			if (StringUtils.isNotEmpty(supervisionReleaseEligibilityDateString))
			{
				Element supervisionReleaseEligibiltyDate =  XmlUtils.appendElement(supervisionAugmentation, OjbcNamespaceContext.NS_JXDM_51, "j:SupervisionReleaseEligibilityDate");
				Element date = XmlUtils.appendElement(supervisionReleaseEligibiltyDate, OjbcNamespaceContext.NS_NC_30, "nc:Date");
				date.setTextContent(supervisionReleaseEligibilityDateString);
			}	
		
			if (StringUtils.isNotEmpty(supervisionReleaseDateString))
			{
				Element supervisionReleaseDate =  XmlUtils.appendElement(supervisionAugmentation, OjbcNamespaceContext.NS_JXDM_51, "j:SupervisionReleaseDate");
				Element date = XmlUtils.appendElement(supervisionReleaseDate, OjbcNamespaceContext.NS_NC_30, "nc:DateTime");
				date.setTextContent(supervisionReleaseDateString);
			}
			if (StringUtils.isNotEmpty(supervisionAreaIdentificationString))
			{
				Element supervisionAreaIdentification =  XmlUtils.appendElement(supervisionAugmentation, OjbcNamespaceContext.NS_JXDM_51, "j:SupervisionAreaIdentification");
				Element identificationID = XmlUtils.appendElement(supervisionAreaIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
				identificationID.setTextContent(supervisionAreaIdentificationString);
			}	
	
			if (StringUtils.isNotEmpty(bed) || StringUtils.isNotEmpty(bedCategory))
			{	
				Element supervisionBedIdentification =  XmlUtils.appendElement(supervisionAugmentation, OjbcNamespaceContext.NS_JXDM_51, "j:SupervisionBedIdentification");
				
				if (StringUtils.isNotEmpty(bedCategory))
				{
					Element identificationID = XmlUtils.appendElement(supervisionBedIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
					identificationID.setTextContent(supervisionAreaIdentificationString);
				}	
		
				if (StringUtils.isNotEmpty(cell))
				{					
					Element bedCategoryCode = XmlUtils.appendElement(supervisionBedIdentification, OjbcNamespaceContext.NS_ADAMS_CO_BOOKING_CODES_EXT, "ac-bkg-codes:BedCategoryCode");
					bedCategoryCode.setTextContent(cell);
				}
			}	
	
			if (StringUtils.isNotEmpty(supervisionCondition))
			{
				Element supervisionConditionElement =  XmlUtils.appendElement(supervisionAugmentation, OjbcNamespaceContext.NS_JXDM_51, "j:SupervisionCondition");
				Element activityDescriptionText =  XmlUtils.appendElement(supervisionConditionElement, OjbcNamespaceContext.NS_NC_30, "nc:ActivityDescriptionText");
				
				activityDescriptionText.setTextContent(supervisionCondition);
			}	
			
			if (StringUtils.isNotEmpty(inmateJailResidentIndicator))
			{
				Element inmateJailResidentIndicatorElement = XmlUtils.appendElement(detention, extensionNamespace, "InmateJailResidentIndicator");
				inmateJailResidentIndicatorElement.setTextContent(inmateJailResidentIndicator);

			}	
		}	
	}

	//<j:Booking structures:id="Booking_01">
	//	<nc:ActivityDate>
	//		<nc:DateTime>2013-12-17T09:30:00</nc:DateTime>
	//	</nc:ActivityDate>
	//  <j:BookingAgencyRecordIdentification>
	//    <nc:IdentificationID xmlns:nc="http://release.niem.gov/niem/niem-core/3.0/"
	//        >234567890</nc:IdentificationID>
	//  </j:BookingAgencyRecordIdentification>		
	//	<j:BookingDetentionFacility>
	//		<nc:FacilityIdentification>
	//			<nc:IdentificationID>Booking Facility ID</nc:IdentificationID>
	//		</nc:FacilityIdentification>
	//	</j:BookingDetentionFacility>
	//	<j:BookingSubject>
	//		<nc:RoleOfPerson structures:ref="Person_01" />
	//		<j:SubjectIdentification>
	//			<nc:IdentificationID>Booking Subject Number</nc:IdentificationID>
	//		</j:SubjectIdentification>
	//	</j:BookingSubject>
	//  <j:Arrest structures:ref="Arrest_01" />
	//  <j:Arrest structures:ref="Arrest_02" />	
	//  <br-ext:BookingStatusCode>Correct</br-ext:BookingStatusCode>
	//</j:Booking>	
	public static void createBooking(Element rootElement,  String bookingDateTime, String bookingDate,
			String bookingFacilityID, String bookingNumber, String bookingSubjectNumber, List<Arrest> arrests, String bookingStatusCode, String eventType) {
		
		Element booking =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_JXDM_51, "j:Booking");
		XmlUtils.addAttribute(booking, OjbcNamespaceContext.NS_STRUCTURES_30, "id", "Booking_01");

		Element activityDate = XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_NC_30, "nc:ActivityDate");
		
		if (StringUtils.isNotBlank(bookingDateTime))
		{	
			Element dateTime = XmlUtils.appendElement(activityDate, OjbcNamespaceContext.NS_NC_30, "nc:DateTime");
			dateTime.setTextContent(bookingDateTime);
		}	

		if (StringUtils.isNotBlank(bookingDate))
		{	
			Element dateTime = XmlUtils.appendElement(activityDate, OjbcNamespaceContext.NS_NC_30, "nc:Date");
			dateTime.setTextContent(bookingDate);
		}	
		
		Element bookingAgencyRecordIdentification =  XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_JXDM_51, "j:BookingAgencyRecordIdentification");
		
		Element identificationBookingID = XmlUtils.appendElement(bookingAgencyRecordIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
		identificationBookingID.setTextContent(bookingNumber);

		Element bookingDetentionFacility =  XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_JXDM_51, "j:BookingDetentionFacility");
		
		Element facilityIdentification = XmlUtils.appendElement(bookingDetentionFacility, OjbcNamespaceContext.NS_NC_30, "nc:FacilityIdentification");
		Element identificationID = XmlUtils.appendElement(facilityIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
		identificationID.setTextContent(bookingFacilityID);
		
		Element bookingSubject =  XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_JXDM_51, "j:BookingSubject");
		Element roleOfPersonRef = XmlUtils.appendElement(bookingSubject, OjbcNamespaceContext.NS_NC_30, "nc:RoleOfPerson");
		XmlUtils.addAttribute(roleOfPersonRef, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Person_01");
		
		Element subjectIdentification =  XmlUtils.appendElement(bookingSubject, OjbcNamespaceContext.NS_JXDM_51, "j:SubjectIdentification");
		
		Element identificationIDSubject = XmlUtils.appendElement(subjectIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
		identificationIDSubject.setTextContent(bookingSubjectNumber);
		
		if (arrests != null)
		{	
			for (int i = 1; i< arrests.size() + 1; i++)
			{
				Element arrestElement = XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_JXDM_51, "j:Arrest");
				XmlUtils.addAttribute(arrestElement, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", "Arrest_" + i);
				
			}	
		}	
		
		if (StringUtils.isNotBlank(bookingStatusCode))
		{	
			if (eventType.equals("Booking"))
			{	
				Element bookingStatusCodeElement = XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_BOOKING_REPORTING_EXT, "br-ext:BookingStatusCode");
				bookingStatusCodeElement.setTextContent(bookingStatusCode);
			}
			
			if (eventType.equals("CustodyRelease"))
			{	
				Element bookingStatusCodeElement = XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_CUSTODY_RELEASE_REPORTING_EXT, "crr-ext:BookingStatusCode");
				bookingStatusCodeElement.setTextContent(bookingStatusCode);
			}
			
			if (eventType.equals("CustodyStatusChange"))
			{	
				Element bookingStatusCodeElement = XmlUtils.appendElement(booking, OjbcNamespaceContext.NS_CUSTODY_STATUS_CHANGE_EXT, "cscr-ext:BookingStatusCode");
				bookingStatusCodeElement.setTextContent(bookingStatusCode);
			}
			
		}	
	}

	//<nc:Case structures:id="Case_01">
	//	<j:CaseAugmentation>
	//		<j:CaseCourt>
	//			<j:CourtName>Case Court</j:CourtName>
	//		</j:CaseCourt>
	//	</j:CaseAugmentation>
	//</nc:Case>	
	public static void createCase(Element rootElement, String caseAttributeID, String courtNameString, String caseTrackingIDString, String caseDocketIDString) {
		
		Element courtCase =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "nc:Case");
		XmlUtils.addAttribute(courtCase, OjbcNamespaceContext.NS_STRUCTURES_30, "id", caseAttributeID);
		
		if (StringUtils.isNotBlank(courtNameString))
		{	
			Element caseAugmentation =  XmlUtils.appendElement(courtCase, OjbcNamespaceContext.NS_JXDM_51, "j:CaseAugmentation");
			Element caseCourt =  XmlUtils.appendElement(caseAugmentation, OjbcNamespaceContext.NS_JXDM_51, "j:CaseCourt");
			Element courtName =  XmlUtils.appendElement(caseCourt, OjbcNamespaceContext.NS_JXDM_51, "j:CourtName");
			
			courtName.setTextContent(courtNameString);
		}	

		if (StringUtils.isNotBlank(caseTrackingIDString))
		{	
			Element caseTrackingID =  XmlUtils.appendElement(courtCase, OjbcNamespaceContext.NS_NC_30, "nc:CaseTrackingID");
			caseTrackingID.setTextContent(caseTrackingIDString);
		}
		
		if (StringUtils.isNotBlank(caseDocketIDString))
		{	
			Element caseDocketID =  XmlUtils.appendElement(courtCase, OjbcNamespaceContext.NS_NC_30, "nc:CaseDocketID");
			caseDocketID.setTextContent(caseDocketIDString);
		}	
		
	}

//<intel:SystemIdentification>
//	<nc:IdentificationID>Source System ID</nc:IdentificationID>
//	<nc:SystemName>Source System Name</nc:SystemName>
//</intel:SystemIdentification>
	public static void createIdentificationSourceSystem(Element rootElement,
			String identificationIDString, String systemNameString) {
		
		Element systemIdentification = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_INTEL_31, "intel:SystemIdentification");
		
		Element identificationID = XmlUtils.appendElement(systemIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
		identificationID.setTextContent(identificationIDString);
		
		Element systemName = XmlUtils.appendElement(systemIdentification, OjbcNamespaceContext.NS_NC_30, "nc:SystemName");
		systemName.setTextContent(systemNameString);
		
		
	}

//<nc:DocumentIdentification>
//	<nc:IdentificationID>eDocumentID</nc:IdentificationID>
//</nc:DocumentIdentification>	
	public static void createDocumentIdentification(Element rootElement, String identificationIDString) {
		
		Element documentIdentification = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "nc:DocumentIdentification");
		
		Element identificationID = XmlUtils.appendElement(documentIdentification, OjbcNamespaceContext.NS_NC_30, "nc:IdentificationID");
		identificationID.setTextContent(identificationIDString);
	}

	//<nc:ActivityPersonAssociation>
	//	<nc:Activity structures:ref="CHS_01" />
	//	<nc:Person structures:ref="Person_01" />
	//</nc:ActivityPersonAssociation>
	public static void createActivityPersonAssociation(
			Element rootElement, String chReference, String personReference) {
    	
		Element activityPersonAssociation =  XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "nc:ActivityPersonAssociation");
    	
    	Element activity =  XmlUtils.appendElement(activityPersonAssociation, OjbcNamespaceContext.NS_NC_30, "nc:Activity");
    	XmlUtils.addAttribute(activity, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", chReference);
    	
    	Element person =  XmlUtils.appendElement(activityPersonAssociation, OjbcNamespaceContext.NS_NC_30, "nc:Person");
    	XmlUtils.addAttribute(person, OjbcNamespaceContext.NS_STRUCTURES_30, "ref", personReference);		
	}

	
//	<nc:DocumentCreationDate>
//	<nc:DateTime>2012-12-17T09:30:47.0Z</nc:DateTime>
//</nc:DocumentCreationDate>
	public static void createDocumentCreationDate(Element rootElement) {
		
		Element documentCreationDate = XmlUtils.appendElement(rootElement, OjbcNamespaceContext.NS_NC_30, "nc:DocumentCreationDate");
		
		Element dateTime = XmlUtils.appendElement(documentCreationDate, OjbcNamespaceContext.NS_NC_30, "nc:DateTime");
		
		String dateTimeString = "";
		
		LocalDateTime now = LocalDateTime.now();
		
		dateTimeString = now.format(DateTimeFormatter.ISO_DATE_TIME);
		
		dateTime.setTextContent(dateTimeString);
		
	}

	public static Element getRootElement(Document doc, String operationName) throws Exception{
		
		Element rootElement = null;
		
	    if(operationName.equals("booking")){        	
	    	
	    	rootElement = doc.createElementNS(OjbcNamespaceContext.NS_BOOKING_REPORTING, "br-doc:BookingReport");  
	    	
	    }else if(operationName.equals("custodyStatusChange")){        	
	    	
	    	rootElement = doc.createElementNS(OjbcNamespaceContext.NS_CUSTODY_STATUS_CHANGE_DOC, "cscr-doc:CustodyStatusChangeReport");  
	    } else if(operationName.equals("custodyRelease")){        	
	    	
	    	rootElement = doc.createElementNS(OjbcNamespaceContext.NS_CUSTODY_RELEASE_REPORTING, "crr-doc:CustodyReleaseReport");  
	    }
	    else {
	    	throw new Exception("Operation not supported");
	    }    	
	    
	    return rootElement;
	}
	
	public static Document getNewDocument() throws Exception{
		
	    DocumentBuilderFactory docBuilderFact = DocumentBuilderFactory.newInstance();
	    docBuilderFact.setNamespaceAware(true);
	    DocumentBuilder docBuilder = docBuilderFact.newDocumentBuilder();
	    Document doc = docBuilder.newDocument();
	    
	    return doc;
	}
}
