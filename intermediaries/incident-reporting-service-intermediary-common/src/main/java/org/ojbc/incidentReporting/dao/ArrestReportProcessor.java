package org.ojbc.incidentReporting.dao;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.incidentReporting.dao.model.PersonInvolvement;
import org.ojbc.util.helper.Hash;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

/**
 * This processor will extract the appropriate information from an arrest report message
 * to create the appropriate person hashes.  This class interacts with the incident reporting DAO
 * to see if person involved hashes exist and will persist information about new person involvements. 
 * 
 */
public class ArrestReportProcessor {

	public static final String PERSON_INVOLVMENT_ACTIVITY_ARREST="personInvolvmentActivityArrest";
	private static final Log log = LogFactory.getLog(ArrestReportProcessor.class);
	
	private IncidentReportingDao incidentReportingDao;
	
	public boolean hasThisPersonArrestBeenProcessedBefore(Document arrestDocument) throws Exception
	{
		String personFirstName="";
		String personLastName="";
		String personDateOfBirth ="";
		
		//TODO: Update xpaths to remove //
		
        String personReference = XmlUtils.xPathStringSearch(arrestDocument,
        			"//lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:Associations/lexsdigest:ArrestSubjectAssociation/nc:PersonReference/@s:ref");
        
		if (StringUtils.isNotBlank(personReference)) {
		    personFirstName = XmlUtils.xPathStringSearch(arrestDocument, "//lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id='" + personReference
		            + "']/nc:PersonName/nc:PersonGivenName");
		    
		    personLastName = XmlUtils.xPathStringSearch(arrestDocument, "//lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id='" + personReference
		            + "']/nc:PersonName/nc:PersonSurName");

		    personDateOfBirth = XmlUtils.xPathStringSearch(arrestDocument,
	                "//lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:Digest/lexsdigest:EntityPerson/lexsdigest:Person[@s:id='" + personReference
	                + "']/nc:PersonBirthDate/nc:Date");
		} else {
		    log.error("Unable to find person reference. Unable to XQuery for person name and will return false.");
		    return false;
		}

		String personStringToHash = personLastName + "_" + personFirstName + "_" + personDateOfBirth;
		
		log.debug("Person String to Hash: " + personStringToHash);
		
		String personInvolvementHash = Hash.sha256(personStringToHash);
		
		log.debug("Person Involvement Hash: " + personInvolvementHash);
		
		String incidentId=XmlUtils.xPathStringSearch(arrestDocument,"//lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataItemPackage/lexs:PackageMetadata/lexs:DataItemID");
		String incidentOriginatingSystemUri=XmlUtils.xPathStringSearch(arrestDocument, "//lexspd:doPublish/lexs:PublishMessageContainer/lexs:PublishMessage/lexs:DataSubmitterMetadata/lexs:SystemIdentifier/lexs:SystemID");
		
		XmlUtils.xPathStringSearch(arrestDocument,"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/notfm-ext:NotifyingActivityReportingSystemNameText");
		
		PersonInvolvement personInvolvement = incidentReportingDao.isThereAPriorPersonInvolvment(incidentId, incidentOriginatingSystemUri, personInvolvementHash, PERSON_INVOLVMENT_ACTIVITY_ARREST);
		
		if (personInvolvement == null)
		{
			incidentReportingDao.addPersonInvolvement(incidentId, incidentOriginatingSystemUri, personInvolvementHash, PERSON_INVOLVMENT_ACTIVITY_ARREST);
			return false;
		}	
		else
		{
			return true;
		}	
	}

	public IncidentReportingDao getIncidentReportingDao() {
		return incidentReportingDao;
	}

	public void setIncidentReportingDao(IncidentReportingDao incidentReportingDao) {
		this.incidentReportingDao = incidentReportingDao;
	}
	
}
