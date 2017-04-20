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
package org.ojbc.intermediaries.sn.topic.rapback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.camel.Message;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.notification.RapbackTriggeringEvent;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class RapbackNotificationRequest extends NotificationRequest {
	
	private static final Log log = LogFactory.getLog( RapbackNotificationRequest.class );
	
	private String SID;
	private String UCN;
	private List<RapbackTriggeringEvent> triggeringEvents;
	
	public RapbackNotificationRequest(Message message) throws Exception{
	    this(message.getBody(Document.class));
	}

	public RapbackNotificationRequest(Document document) throws Exception {
	    super(document);

	    SID = XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");
	    UCN = XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person/jxdm41:PersonAugmentation/jxdm41:PersonFBIIdentification/nc:IdentificationID");

	    triggeringEvents = new ArrayList<RapbackTriggeringEvent>();
	    
	    NodeList eventTypeNodeList = XmlUtils.xPathNodeListSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingFederalCriminalHistoryUpdate/notfm-ext:RelatedFBISubscription/notfm-ext:TriggeringEvent");
	
	    if (eventTypeNodeList != null)
	    {
			for(int i=0; i<eventTypeNodeList.getLength(); i++){
				Node childNode = eventTypeNodeList.item(i);
				
				String triggeringEventCode = XmlUtils.xPathStringSearch(childNode, "notfm-ext:FederalTriggeringEventCode");
				String triggeringEventText = XmlUtils.xPathStringSearch(childNode, "notfm-ext:RapBackEventText");
				String triggeringEventDate = XmlUtils.xPathStringSearch(childNode, "notfm-ext:RapBackEventDate/nc:Date");

				if (StringUtils.isNotBlank(triggeringEventDate))
				{
					DateTime triggeringEventDateTime = XmlUtils.parseXmlDate(triggeringEventDate);
					triggeringEventDate = triggeringEventDateTime.toString("MM-dd-yyyy");
				}
					
				RapbackTriggeringEvent triggeringEvent = new RapbackTriggeringEvent();
				
				triggeringEvent.setTriggeringEventCode(triggeringEventCode);
				triggeringEvent.setTriggeringEventDate(triggeringEventDate);
				triggeringEvent.setTriggeringEventText(triggeringEventText);
				
				triggeringEvents.add(triggeringEvent);
			}	
	    }	
	    subjectIdentifiers = new HashMap<String, String>();
	    subjectIdentifiers.put(SubscriptionNotificationConstants.SID, SID);
	}

    public String getEventDateTimeDisplay() {
        boolean isNotificationEventDateInclusiveOfTime = isNotificationEventDateInclusiveOfTime();
        DateTime eventDateTime = getNotificationEventDate();
        String eventDate = NotificationBrokerUtils.returnFormattedNotificationEventDate(eventDateTime, isNotificationEventDateInclusiveOfTime);
        return StringUtils.strip(eventDate);
    }
    
	@Override
	protected String getNotificationEventDateRootXpath() {
		//Notification date is per event and set by subclass
		return "";
	}

	@Override
	protected String getNotifyingAgencyXpath() {

		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingFederalCriminalHistoryUpdate/notfm-ext:NotifyingActivityReportingOrganization/nc:OrganizationName";
	}

	@Override
	protected String getNotificationEventIdentifierXpath() {

		return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingFederalCriminalHistoryUpdate/notfm-ext:RelatedFBISubscription/notfm-ext:RapBackActivityNotificationIdentification/nc:IdentificationID";
	}

	
	@Override
	protected String getNotificationAgencyPhoneNumberXpath() {
		// Add this to IEPD if required in notification message
		return "''";
	}

	@Override
	protected String getNotifyingSystemNameXPath() {
		// Add this to IEPD if required in notification message
		return  "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingFederalCriminalHistoryUpdate/notfm-ext:NotifyingActivityReportingSystemNameText";
	}

	@Override
	public String getDescriptiveSubjectIdentifier() {
	    log.debug("Requesting descriptive subject identifier: " + getSubjectIdentifiers());
		return getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID);
	}

	@Override
	protected String getOfficerNameReferenceXPath() {
		// Add this to IEPD if required in notification message
		return "";
	}

	@Override
	protected String getNotifyingAgencyOriXpath() {
		return null; //ArrestNotificationRequest does not have agency ORI element.
	}

	public String getSID() {
		return SID;
	}

	public void setSID(String sID) {
		SID = sID;
	}

	public String getUCN() {
		return UCN;
	}

	public void setUCN(String uCN) {
		UCN = uCN;
	}

	@Override
	public String toString() {
		return "RapbackNotificationRequest [SID=" + SID + ", UCN=" + UCN
				+ ", triggeringEvents=" + triggeringEvents + ", requestDocument="
				+ requestDocument + ", notificationEventDate="
				+ notificationEventDate
				+ ", isNotificationEventDateInclusiveOfTime="
				+ isNotificationEventDateInclusiveOfTime + ", personFirstName="
				+ personFirstName + ", personMiddleName=" + personMiddleName
				+ ", personLastName=" + personLastName + ", personNameSuffix="
				+ personNameSuffix + ", personBirthDate=" + personBirthDate
				+ ", personAge=" + personAge
				+ ", personActivityInvolvementText="
				+ personActivityInvolvementText + ", personTelephoneNumbers="
				+ personTelephoneNumbers + ", aliases=" + aliases
				+ ", notificationEventIdentifier="
				+ notificationEventIdentifier + ", notifyingAgencyName="
				+ notifyingAgencyName + ", notifyingAgencyPhoneNumber="
				+ notifyingAgencyPhoneNumber + ", notifyingSystemName="
				+ notifyingSystemName + ", officerNames=" + officerNames
				+ ", subjectIdentifiers=" + subjectIdentifiers
				+ ", alternateSubjectIdentifiers="
				+ alternateSubjectIdentifiers + ", topic=" + topic + "]";
	}

	public List<RapbackTriggeringEvent> getTriggeringEvents() {
		return triggeringEvents;
	}

	public void setTriggeringEvents(List<RapbackTriggeringEvent> triggeringEvents) {
		this.triggeringEvents = triggeringEvents;
	}



}
