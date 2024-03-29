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
package org.ojbc.intermediaries.sn.topic.statewarrant;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class StateWarrantFileNotificationRequest extends NotificationRequest{
	
	private static final Log logger = LogFactory.getLog(StateWarrantFileNotificationRequest.class);
	
    public StateWarrantFileNotificationRequest(Message message) throws Exception{
        this(message.getBody(Document.class));
    }

    public StateWarrantFileNotificationRequest(Document document) throws Exception {
        super(document);
        notificationEventDate = DateTime.now();
        buildSubjectIdMap();        
    }
    
    public String getPersonFullName() {
        String personFirstName = StringUtils.strip(getPersonFirstName());
        String personLastName = StringUtils.strip(getPersonLastName());
        return personFirstName + " " + personLastName;
    }
    
    public String getEventDateTimeDisplay() {
        boolean isNotificationEventDateInclusiveOfTime = isNotificationEventDateInclusiveOfTime();
        DateTime eventDateTime = getNotificationEventDate();
        String eventDate = NotificationBrokerUtils.returnFormattedNotificationEventDate(eventDateTime, isNotificationEventDateInclusiveOfTime);
        return StringUtils.strip(eventDate);
    }
    
    @Override
	protected String getNotificationEventDateRootXpath() {
		
		// note doesn't return nc:Date or nc:DateTime but rather returns their parent node,
		// because inherited logic conditionally uses one the the two child values
		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingCriminalHistoryUpdate/chu:CycleTrackingIdentifierAssignment/nc:ActivityDate";
	}

	@Override
	protected String getNotifyingAgencyXpath() {
		
		String notifyingAgencyXpath = null;		
		
		try {
			String origOrganizerRef = getOrigOrganizationRef();
			
			notifyingAgencyXpath = "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Organization[@s:id='"+origOrganizerRef+"']/nc:OrganizationName";	
		} catch (Exception e) {			
			e.printStackTrace();
		}

		return notifyingAgencyXpath;
	}

	private String getOrigOrganizationRef() throws Exception {
		String origOrganizerRef = XmlUtils.xPathStringSearch(requestDocument, 
				"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingWarrant/notfm-ext:NotifyingOrganizationReference/@s:ref");
		
		logger.info("Notifying Agency ref: " + origOrganizerRef);
		return origOrganizerRef;
	}

	@Override
	protected String getNotificationEventIdentifierXpath() {

		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingCriminalHistoryUpdate/chu:CycleTrackingIdentifierAssignment/nc:Case/nc:ActivityIdentification/nc:IdentificationID";
	}
		
	
	@Override
	protected String getNotifyingSystemNameXPath(){
		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingActivityReportingSystemNameText";
	}
	
	@Override
	public String getDescriptiveSubjectIdentifier() {
		
		Map<String, String> sidMap = getSubjectIdentifiers();
		
		String lastName = sidMap.get(SubscriptionNotificationConstants.LAST_NAME);
		
		String firstName = sidMap.get(SubscriptionNotificationConstants.FIRST_NAME);
		
		String dateOfBirth = sidMap.get(SubscriptionNotificationConstants.DATE_OF_BIRTH);
				
		return lastName + "_" + firstName + "_" + dateOfBirth;
	}
	
	@Override
	protected String getNotificationAgencyPhoneNumberXpath() {
		return "''";	
	}

	@Override
	protected String getOfficerNameReferenceXPath() {		
		return null;
	}

	private void buildSubjectIdMap() throws Exception {
		
		subjectIdentifiers = new HashMap<String, String>();
		
		String firstName = XmlUtils.xPathStringSearch(requestDocument, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-exch:Person[@s:id=../notfm-ext:NotifyingWarrant/jxdm41:Warrant/jxdm41:CourtOrderDesignatedSubject/nc:RoleOfPersonReference/@s:ref]/nc:PersonName/nc:PersonGivenName");
		subjectIdentifiers.put(SubscriptionNotificationConstants.FIRST_NAME, firstName);
		this.personFirstName = firstName;
		
		String lastName = XmlUtils.xPathStringSearch(requestDocument, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-exch:Person[@s:id=../notfm-ext:NotifyingWarrant/jxdm41:Warrant/jxdm41:CourtOrderDesignatedSubject/nc:RoleOfPersonReference/@s:ref]/nc:PersonName/nc:PersonSurName");
		subjectIdentifiers.put(SubscriptionNotificationConstants.LAST_NAME, lastName);
		this.personLastName = lastName;
		
		String dateOfBirth = XmlUtils.xPathStringSearch(requestDocument,
				"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-exch:Person[@s:id=../notfm-ext:NotifyingWarrant/jxdm41:Warrant/jxdm41:CourtOrderDesignatedSubject/nc:RoleOfPersonReference/@s:ref]/nc:PersonBirthDate/nc:Date");
		
		logger.debug("Notification message DOB: " + dateOfBirth);
		
		this.personBirthDate = dateOfBirth;
		
		subjectIdentifiers.put(SubscriptionNotificationConstants.DATE_OF_BIRTH, dateOfBirth);
	}

	@Override
	protected String getNotifyingAgencyOriXpath() {
		try {
			String origOrganizerRef = getOrigOrganizationRef();
			return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Organization[@s:id='"
					+origOrganizerRef+"']/jxdm41:OrganizationAugmentation/jxdm41:OrganizationORIIdentification/nc:IdentificationID";			
		} catch (Exception e) {			
			e.printStackTrace();
			return null;
		}

	}
}
