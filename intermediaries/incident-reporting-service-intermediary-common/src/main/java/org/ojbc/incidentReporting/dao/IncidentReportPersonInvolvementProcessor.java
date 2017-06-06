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
 * Copyright 2012-2017 Open Justice Broker Consortium
 */
package org.ojbc.incidentReporting.dao;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.incidentReporting.dao.model.PersonInvolvement;
import org.ojbc.util.helper.Hash;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

/**
 * This processor will extract the appropriate information from an person/incident notification message
 * to create the appropriate person hashes.  This class interacts with the incident reporting DAO
 * to see if person involved hashes exist and will persist information about new person involvements. 
 * 
 */
public class IncidentReportPersonInvolvementProcessor {

	public static final String PERSON_INVOLVMENT_ACTIVITY_INCIDENT="personInvolvmentActivityIncident";
	private static final Log log = LogFactory.getLog(IncidentReportPersonInvolvementProcessor.class);
	
	private IncidentReportingDao incidentReportingDao;
	
	public boolean hasThisPersonInvolvmentBeenProcessedBefore(Document notificationDocument) throws Exception
	{
		String personFirstName="";
		String personLastName="";
		String personDateOfBirth ="";
				
        String personReference = XmlUtils.xPathStringSearch(notificationDocument,
        "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/nc:ActivityInvolvedPersonAssociation/nc:PersonReference/@s:ref");

		if (StringUtils.isNotBlank(personReference)) {
		    personFirstName = XmlUtils.xPathStringSearch(notificationDocument, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id='" + personReference
		            + "']/nc:PersonName/nc:PersonGivenName");
		    personLastName = XmlUtils.xPathStringSearch(notificationDocument, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id='" + personReference
		            + "']/nc:PersonName/nc:PersonSurName");

		    personDateOfBirth = XmlUtils.xPathStringSearch(notificationDocument,
	                "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id=../nc:ActivityInvolvedPersonAssociation/nc:PersonReference/@s:ref]/nc:PersonBirthDate/nc:Date");
		} else {
		    log.error("Unable to find person reference. Unable to XQuery for person name and will return false.");
		    return false;
		}

		String personStringToHash = personLastName + "_" + personFirstName + "_" + personDateOfBirth;
		
		log.debug("Person String to Hash: " + personStringToHash);
		
		String personInvolvementHash = Hash.sha256(personStringToHash);
		
		log.debug("Person Involvement Hash: " + personInvolvementHash);
		
		String incidentId=XmlUtils.xPathStringSearch(notificationDocument,"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/jxdm41:Incident/nc:ActivityIdentification/nc:IdentificationID");
		String incidentOriginatingSystemUri=XmlUtils.xPathStringSearch(notificationDocument,"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingIncident/notfm-ext:NotifyingActivityReportingSystemNameText");
		
		PersonInvolvement personInvolvement = incidentReportingDao.isThereAPriorPersonInvolvment(incidentId, incidentOriginatingSystemUri, personInvolvementHash, PERSON_INVOLVMENT_ACTIVITY_INCIDENT);
		
		if (personInvolvement == null)
		{
			incidentReportingDao.addPersonInvolvement(incidentId, incidentOriginatingSystemUri, personInvolvementHash, PERSON_INVOLVMENT_ACTIVITY_INCIDENT);
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
