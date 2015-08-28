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
package org.ojbc.adapters.analyticaldatastore.processor;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.adapters.analyticaldatastore.dao.IncidentCircumstance;
import org.ojbc.adapters.analyticaldatastore.dao.IncidentType;
import org.ojbc.adapters.analyticaldatastore.dao.model.Arrest;
import org.ojbc.adapters.analyticaldatastore.dao.model.Charge;
import org.ojbc.adapters.analyticaldatastore.dao.model.Incident;
import org.ojbc.adapters.analyticaldatastore.personid.IdentifierGenerationStrategy;
import org.ojbc.adapters.analyticaldatastore.util.AnalyticalDataStoreUtils;
import org.ojbc.util.xml.XmlUtils;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class IncidentReportProcessor extends AbstractReportRepositoryProcessor {

	private static final Log log = LogFactory.getLog( IncidentReportProcessor.class );
	
	private static final String PATH_TO_LEXS_DATA_ITEM_PACKAGE="//lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage";
	
	private static final String PATH_TO_LEXS_DIGEST= PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:Digest";
	
	private static final String INCIDENT_REPORT_ROOT_ELEMENT_NAME = "IncidentReport"; 
	
	private static final String INCIDENT_REPORT_UPDATE_ROOT_ELEMENT_NAME="IncidentReportUpdate"; 
	
	@Transactional
	public void processReport(Document incidentReport) throws Exception
	{
		Incident incident = new Incident();
		
		String rootElemntName = incidentReport.getDocumentElement().getLocalName(); 
		if (INCIDENT_REPORT_ROOT_ELEMENT_NAME.equals(rootElemntName)){
			incident.setRecordType('N');
		}
		else if (INCIDENT_REPORT_UPDATE_ROOT_ELEMENT_NAME.equals(rootElemntName)){
			incident.setRecordType('U');
		}
		
		String reportingAgencyName = XmlUtils.xPathStringSearch(incidentReport, 
				PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityOrganization/nc:Organization[@s:id= " + PATH_TO_LEXS_DIGEST + 
				" /lexsdigest:Associations/nc:ActivityReportingOrganizationAssociation[nc:ActivityReference/@s:ref=" + 
						PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityActivity/nc:Activity[nc:ActivityCategoryText='Incident']/@s:id]/nc:OrganizationReference/@s:ref]/nc:OrganizationName");
		log.debug("Agency Name: " + reportingAgencyName);

		String reportingAgencyORI = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:PackageMetadata/lexs:DataOwnerMetadata/lexs:DataOwnerIdentifier/lexs:ORI");
		log.debug("Agency ORI: " + reportingAgencyORI);

		Integer reportingAgencyId = null;
				
		if (StringUtils.isNotBlank(reportingAgencyORI))
		{
			reportingAgencyId = analyticalDatastoreDAO.searchForAgenyIDbyAgencyORI(reportingAgencyORI);
			
			if (reportingAgencyId == null)
			{
				throw new Exception("Valid Agency ORI required for incident.  Agency Name is: " + reportingAgencyName + ", Agency ORI is: " + reportingAgencyORI);
			}	
			
			incident.setReportingAgencyID(reportingAgencyId);
		}
		else{
			throw new Exception("Valid Agency ORI required for incident.  Agency Name is: " + StringUtils.trimToEmpty(reportingAgencyName) + 
					", Agency ORI is: " + StringUtils.trimToEmpty(reportingAgencyORI));
		}
		
		String incidentCaseNumber=XmlUtils.xPathStringSearch(incidentReport,  PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:PackageMetadata/lexs:DataItemID");
		log.debug("Incident Case Number: " + incidentCaseNumber);
		
		if (StringUtils.isNotBlank(incidentCaseNumber))
		{
			incident.setIncidentCaseNumber(incidentCaseNumber);
		}			
		
		//Check to see if incident(s) already exists
		List<Incident> incidents = analyticalDatastoreDAO.searchForIncidentsByIncidentNumberAndReportingAgencyID(incidentCaseNumber, reportingAgencyId);
		
		//if incidents exist, delete them prior to inserting a new one
		if (incidents.size() >1)
		{
			throw new IllegalStateException("Error condition. Duplicate records with same incident number and agency ID exists in database");
		}	
		
		Integer incidentIDToReplace = null;
				
		if (incidents.size() == 1)
		{
			incidentIDToReplace = incidents.get(0).getIncidentID();
			incident.setIncidentID(incidentIDToReplace);
			log.debug("Incident ID to replace: " + incidentIDToReplace);
			
			analyticalDatastoreDAO.deleteIncident(incidents.get(0).getIncidentID());
		}	
		
		
		String reportingSystem = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:PackageMetadata/lexs:DataOwnerMetadata/lexs:DataOwnerIdentifier/lexs:SystemID");
		incident.setReportingSystem(reportingSystem);
		
		//Look for either incident date/time or incident date field
		String incidentDateTimeAsString = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DIGEST+ "/lexsdigest:EntityActivity/nc:Activity/nc:ActivityDateRange/nc:StartDate/nc:DateTime");
		log.debug("Incident Date/Time: " + incidentDateTimeAsString);
		
		if (StringUtils.isNotEmpty(incidentDateTimeAsString))
		{	
			Calendar incidentDateTimeCal = DatatypeConverter.parseDateTime(incidentDateTimeAsString);
			Date incidentDateTime = incidentDateTimeCal.getTime();
	
			if (incidentDateTime != null)
			{
				incident.setIncidentDate(incidentDateTime);
			}	
			
			Time incidentTime =  new Time(incidentDateTime.getTime());
			log.debug("Incident Time: " + incidentTime.toString());
			
			if (incidentTime != null)
			{
				incident.setIncidentTime(incidentTime);
			}
		} else {
			String incidentDateAsString = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DIGEST+ "/lexsdigest:EntityActivity/nc:Activity/nc:ActivityDate/nc:Date");
			log.debug("Incident Date: " + incidentDateAsString);
			
			Calendar incidentDateCal = DatatypeConverter.parseDate(incidentDateAsString);
			Date incidentDate = incidentDateCal.getTime();
	
			if (incidentDate != null)
			{
				incident.setIncidentDate(incidentDate);
			}	


		}	
		
		String mapHorizontalCoordinateText =XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Location/nc:LocationMapLocation/nc:MapHorizontalCoordinateText");
		
		if (StringUtils.isNotBlank(mapHorizontalCoordinateText))
		{
			//TODO: put this into a strategy
			try
			{
				mapHorizontalCoordinateText = updatedCoordinate(mapHorizontalCoordinateText);
				log.debug("Map horizontal coordinate text: " + mapHorizontalCoordinateText);
	
				BigDecimal longitude = new BigDecimal(mapHorizontalCoordinateText);
				incident.setIncidentLocationLongitude(longitude);
			}
			catch (Exception ex)
			{
				log.warn("Unable to set map horizontal text coordinate");
			}
		}	
		
		String mapVerticalCoordinateText =XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Location/nc:LocationMapLocation/nc:MapVerticalCoordinateText");
		
		if (StringUtils.isNotBlank(mapVerticalCoordinateText))
		{	
			//TODO: put this into a strategy
			try
			{
				mapVerticalCoordinateText = updatedCoordinate(mapVerticalCoordinateText);
				log.debug("Map vertical coordinate text: " + mapVerticalCoordinateText);
	
				BigDecimal latitude = new BigDecimal(mapVerticalCoordinateText);
				incident.setIncidentLocationLatitude(latitude);
			}
			catch (Exception ex)
			{
				log.warn("Unable to set map vertical text coordinate");
			}
		}	
		
		String incidentLocationReference = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:Associations/lexsdigest:IncidentLocationAssociation/nc:LocationReference/@s:ref");
		
		if (StringUtils.isNotEmpty(incidentLocationReference))
		{
			Node locationNode = XmlUtils.xPathNodeSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityLocation/nc:Location[@s:id='" + incidentLocationReference + "']");
			
			String streetFullText = XmlUtils.xPathStringSearch(locationNode, "nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetFullText");
			log.debug("Street Full Text: " + streetFullText);

			if (StringUtils.isNotBlank(streetFullText))
			{
				incident.setIncidentLocationStreetAddress(streetFullText);
			}	
			else
			{
				String streetNumberText = XmlUtils.xPathStringSearch(locationNode, "nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetNumberText");
				log.debug("Street Number Text: " + streetNumberText);
				
				String streetName = XmlUtils.xPathStringSearch(locationNode, "nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetName");
				log.debug("Street Name Text: " + streetName);

				String streetCategoryText = XmlUtils.xPathStringSearch(locationNode, "nc:LocationAddress/nc:StructuredAddress/nc:LocationStreet/nc:StreetCategoryText");
				log.debug("Street Category Text: " + streetCategoryText);

				streetFullText = streetNumberText + streetName + streetFullText;
				log.debug("Street Full Text built from number, name and category: " + streetFullText);

				if (StringUtils.isNotBlank(streetFullText))
				{
					incident.setIncidentLocationStreetAddress(streetFullText);
				}	
	
			}	
			
			String cityTown = XmlUtils.xPathStringSearch(locationNode, "nc:LocationAddress/nc:StructuredAddress/nc:LocationCityName");
			log.debug("City/Town: " + cityTown);
			
			if (StringUtils.isNotBlank(cityTown))
			{
				incident.setIncidentLocationTown(cityTown);
			}	

		}	
		
		Integer incidentPk = analyticalDatastoreDAO.saveIncident(incident);

		//Add Incident Description Text
		processIncidentType(incidentReport, incidentPk);

		//Save circumstance codes
		processCircumstanceCodes(incidentReport, incidentPk);
		
		processArrests(incidentReport, incidentPk, reportingSystem);
			
	}

	private void processIncidentType(Document incidentReport, Integer incidentPk) throws Exception {
		NodeList incidentNatureTextNodes = XmlUtils.xPathNodeListSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Incident/inc-ext:IncidentNatureText");
		
	    if (incidentNatureTextNodes == null || incidentNatureTextNodes.getLength() == 0) 
	    {
			log.debug("No incident nature text nodes in document");
			return;
	    }
	    
		for (int i = 0; i < incidentNatureTextNodes.getLength(); i++) 
		{
			IncidentType incidentType = new IncidentType();
			
			incidentType.setIncidentID(incidentPk);
			incidentType.setIncidentDescriptionText(incidentNatureTextNodes.item(i).getTextContent());
			
			analyticalDatastoreDAO.saveIncidentType(incidentType);
			
		}	

	}

	private void processCircumstanceCodes(Document incidentReport,
			Integer incidentPk) throws Exception{
		
		NodeList circumstanceCodeNodes = XmlUtils.xPathNodeListSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Offense/inc-ext:OffenseModifierText");
		
	    if (circumstanceCodeNodes == null || circumstanceCodeNodes.getLength() == 0) 
	    {
			log.debug("No circumstance codes in document");
			return;
	    }
		
		for (int i = 0; i < circumstanceCodeNodes.getLength(); i++) 
		{
			IncidentCircumstance incidentCircumstance = new IncidentCircumstance();
			
			incidentCircumstance.setIncidentID(incidentPk);
			incidentCircumstance.setIncidentCircumstanceText(circumstanceCodeNodes.item(i).getTextContent());
			
			analyticalDatastoreDAO.saveIncidentCircumstance(incidentCircumstance);
			
		}	
		
	}

	protected void processArrests(Document incidentReport, Integer incidentPk, String reportingSystem)
			throws Exception {
		NodeList arrestSubjectAssocationNodes = XmlUtils.xPathNodeListSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:Associations/lexsdigest:ArrestSubjectAssociation");
		
	    if (arrestSubjectAssocationNodes == null || arrestSubjectAssocationNodes.getLength() == 0) 
	    {
			log.debug("No Arrest nodes in document");
			return;
	    }
		
		for (int i = 0; i < arrestSubjectAssocationNodes.getLength(); i++) 
		{
			Arrest arrest = new Arrest();
			
			arrest.setIncidentID(incidentPk);
			arrest.setReportingSystem(reportingSystem);
			
		    if (arrestSubjectAssocationNodes.item(i).getNodeType() == Node.ELEMENT_NODE)
		    {
		        String personId = XmlUtils.xPathStringSearch(arrestSubjectAssocationNodes.item(i), "nc:PersonReference/@s:ref");
		        log.debug("Arrestee Person ID: " + personId);

		        String arrestId = XmlUtils.xPathStringSearch(arrestSubjectAssocationNodes.item(i), "nc:ActivityReference/@s:ref");
		        log.debug("Arrest ID: " + arrestId);

		        Node personNode = XmlUtils.xPathNodeSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityPerson/lexsdigest:Person[@s:id='" + personId + "']");
		        
		        Map<String, Object> arrestee = AnalyticalDataStoreUtils.retrieveMapOfPersonAttributes(personNode);
		        String personIdentifierKey = identifierGenerationStrategy.generateIdentifier(arrestee);
		        log.debug("Arrestee person identifier keys: " + personIdentifierKey);

		        String personBirthDateAsString = (String)arrestee.get(IdentifierGenerationStrategy.BIRTHDATE_FIELD);
		        String personRace = (String)arrestee.get("personRace");
		        
		        if (StringUtils.isBlank(personRace))
		        {
		        	personRace = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Person/ndexia:PersonAugmentation[lexslib:SameAsDigestReference/@lexslib:ref='" + personId + "']/ndexia:PersonRaceCode");
		        	log.debug("Person race retrieved from ndex payload: " + personRace);
		        }	
		        
		        String personSex = (String)arrestee.get(IdentifierGenerationStrategy.SEX_FIELD);
		        
		        int personPk = savePerson(personBirthDateAsString, personSex, personRace, personIdentifierKey);	
		        arrest.setPersonID(personPk);
		        
		        
		        Date arrestDateTime = returnArrestDate(incidentReport,
		        		arrestSubjectAssocationNodes.item(i));
		        
		        arrest.setArrestDate(arrestDateTime);

				Time arrestTime =  new Time(arrestDateTime.getTime());
				log.debug("Arrest Time: " + arrestTime.toString());

				arrest.setArrestTime(arrestTime);
				
				String arrestingAgency = returnArrestingAgency(incidentReport);
				log.debug("Arresting Agency: " + arrestingAgency);

				if (StringUtils.isNotBlank(arrestingAgency))
				{
					arrest.setArrestingAgencyName(arrestingAgency);
				}	

				//Save arrest
		        int arrestPk = analyticalDatastoreDAO.saveArrest(arrest);
		        
		        //Save Charges
		        processArrestCharge(incidentReport, arrestPk, arrestId);
		    }
		}
	}


	private void processArrestCharge(Document incidentReport, int arrestPk, String arrestId) throws Exception {
		//get lexsdigest:ArrestOffenseAssociation nodes using arrest id
		NodeList arrestOffenseAssociationNodes = XmlUtils.xPathNodeListSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:Associations/lexsdigest:ArrestOffenseAssociation");

		//Get all offense IDs by looping through association
		for (int i = 0; i < arrestOffenseAssociationNodes.getLength(); i++) 
		{
			String offenseId = XmlUtils.xPathStringSearch(arrestOffenseAssociationNodes.item(i), "nc:ActivityReference[2]/@s:ref");
			log.debug("Arrest Node, Offense ID: " + offenseId);
			
			String offenseCategoryCodeText = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/inc-ext:IncidentReport/inc-ext:Offense[lexslib:SameAsDigestReference/@lexslib:ref='" + offenseId + "']/inc-ext:OffenseCategoryCodeText");
			
			if (StringUtils.isBlank(offenseCategoryCodeText))
			{
				offenseCategoryCodeText = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref='" + offenseId + "']/ndexia:OffenseCode");
			}	
			
			log.debug("Offense Category Code Text (Statute): " + offenseCategoryCodeText);
			
			String offenseDescriptionText1 = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref='" + offenseId + "']/jxdm40:Statute/jxdm40:StatuteCodeIdentification/nc:IdentificationID");

			if (StringUtils.isBlank(offenseDescriptionText1))
			{
				offenseDescriptionText1 = XmlUtils.xPathStringSearch(incidentReport, PATH_TO_LEXS_DATA_ITEM_PACKAGE + "/lexs:StructuredPayload/ndexia:IncidentReport/ndexia:Offense[ndexia:ActivityAugmentation/lexslib:SameAsDigestReference/@lexslib:ref='" + offenseId + "']/ndexia:OffenseText");
			}	
			
			log.debug("Offense Description Text 1 (Statute): " + offenseDescriptionText1);
			
			Charge charge = new Charge();
			charge.setArrestID(arrestPk);
			
			if (StringUtils.isNotBlank(offenseCategoryCodeText))
			{
				charge.setOffenseDescriptionText(offenseCategoryCodeText);
			}	
			
			if (StringUtils.isNotBlank(offenseDescriptionText1))
			{
				charge.setOffenseDescriptionText1(offenseDescriptionText1.trim());
			}	
			
			analyticalDatastoreDAO.saveCharge(charge);
		}
		
	}

	protected Date returnArrestDate(Document incidentReport,
			Node arrestSubjectAssocationNode) throws Exception {
		String arrestActivityReference = XmlUtils.xPathStringSearch(arrestSubjectAssocationNode, "nc:ActivityReference/@s:ref");
		log.debug("Arrest Activity Reference: " + arrestActivityReference);
		
		Node arrestNode = XmlUtils.xPathNodeSearch(incidentReport, PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityActivity/nc:Activity[@s:id='" + arrestActivityReference + "']");
		
		String arrestDateTimeAsString = XmlUtils.xPathStringSearch(arrestNode, "nc:ActivityDate/nc:DateTime");
		
		Calendar arrestDateTimeCal = DatatypeConverter.parseDateTime(arrestDateTimeAsString);
		Date arrestDateTime = arrestDateTimeCal.getTime();
		return arrestDateTime;
	}

	private String returnArrestingAgency(Node incidentReport) throws Exception
	{
		String arrestingOfficerRefernce = XmlUtils.xPathStringSearch(incidentReport, 
				PATH_TO_LEXS_DIGEST + "/lexsdigest:Associations/lexsdigest:ArrestOfficerAssociation/nc:PersonReference/@s:ref");
		log.debug("Arresting Officer Reference: " + arrestingOfficerRefernce);
		
		String arrestAgencyReference=XmlUtils.xPathStringSearch(incidentReport, 
				PATH_TO_LEXS_DIGEST + "/lexsdigest:Associations/nc:PersonAssignedUnitAssociation/nc:PersonReference"
						+ "[@s:ref='" + arrestingOfficerRefernce + "']/following-sibling::nc:OrganizationReference/@s:ref");
		log.debug("Arresting Agency Reference: " + arrestAgencyReference);
		
		String arrestingAgency = XmlUtils.xPathStringSearch(incidentReport, 
				PATH_TO_LEXS_DIGEST + "/lexsdigest:EntityOrganization/nc:Organization[@s:id='" + arrestAgencyReference + "']/nc:OrganizationName");
		log.debug("Arresting Agency: " + arrestingAgency);
		
		return arrestingAgency;
	}
	
	protected String updatedCoordinate(String coordinateText) {
		
		if (coordinateText.startsWith("-"))
		{
			coordinateText = new StringBuilder(coordinateText).insert(3, ".").toString();
		}	
		else
		{
			coordinateText = new StringBuilder(coordinateText).insert(2, ".").toString();	
		}	
		
		return coordinateText;
	}

}
