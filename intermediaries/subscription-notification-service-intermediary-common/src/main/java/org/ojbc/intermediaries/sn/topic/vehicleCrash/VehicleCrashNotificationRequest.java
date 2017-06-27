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
package org.ojbc.intermediaries.sn.topic.vehicleCrash;

import java.util.HashMap;
import java.util.Map;

import org.apache.camel.Message;
import org.apache.commons.lang.StringUtils;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.util.NotificationBrokerUtils;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class VehicleCrashNotificationRequest extends NotificationRequest {

	private String crashLocation;
	
    public VehicleCrashNotificationRequest(Message message) throws Exception {
        this(message.getBody(Document.class));
    }

    public VehicleCrashNotificationRequest(Document document) throws Exception {
        super(document);

        String dateOfBirth = XmlUtils
                .xPathStringSearch(requestDocument,
                        "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id=../nc:ActivityInvolvedPersonAssociation/nc:PersonReference/@s:ref]/nc:PersonBirthDate/nc:Date");

        buildSubjectIdMap(dateOfBirth);
        
        crashLocation = XmlUtils.xPathStringSearch(document, "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingVehicleCrash/vc-ext:VehicleCrash/nc:Location/nc:LocationHighway/nc:HighwayFullText");
    }

    public String getPersonNotificationSubjectName() {
        StringBuffer personNotificationSubjectName = new StringBuffer();

        personNotificationSubjectName.append(getPersonLastName());

        if (StringUtils.isNotEmpty(getPersonNameSuffix())) {
            personNotificationSubjectName.append(" " + getPersonNameSuffix());
        }

        personNotificationSubjectName.append(", " + getPersonFirstName());

        if (StringUtils.isNotEmpty(getPersonMiddleName())) {
            personNotificationSubjectName.append(" " + getPersonMiddleName());
        }
        return personNotificationSubjectName.toString();
    }

    private void buildSubjectIdMap(String dateOfBirth) {

        subjectIdentifiers = new HashMap<String, String>();

        subjectIdentifiers.put(SubscriptionNotificationConstants.FIRST_NAME, personFirstName);
        subjectIdentifiers.put(SubscriptionNotificationConstants.LAST_NAME, personLastName);
        subjectIdentifiers.put(SubscriptionNotificationConstants.DATE_OF_BIRTH, dateOfBirth);
    }

    @Override
    protected String getNotificationEventDateRootXpath() {

        return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingVehicleCrash/vc-ext:VehicleCrash/nc:ActivityDate";
    }

    @Override
    protected String getNotifyingAgencyXpath() {
        return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingVehicleCrash/vc-ext:VehicleCrash/vc-ext:CrashReportingAgency/nc:OrganizationName";
    }

    @Override
    protected String getNotificationEventIdentifierXpath() {
    		
        return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingVehicleCrash/vc-ext:VehicleCrash/jxdm41:Citation/nc:ActivityIdentification/nc:IdentificationID";
    }

    @Override
    protected String getNotificationAgencyPhoneNumberXpath() {

        return "''";

    }

    @Override
    protected String getNotifyingSystemNameXPath() {
        return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingVehicleCrash/notfm-ext:NotifyingActivityReportingSystemNameText";
    }

    @Override
    public String getDescriptiveSubjectIdentifier() {
        Map<String, String> sidMap = getSubjectIdentifiers();
        return sidMap.get(SubscriptionNotificationConstants.LAST_NAME) + "_" + sidMap.get(SubscriptionNotificationConstants.FIRST_NAME) + "_"
                + sidMap.get(SubscriptionNotificationConstants.DATE_OF_BIRTH);
    }

    @Override
    protected String getOfficerNameReferenceXPath() {
        return "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingVehicleCrash/vc-ext:VehicleCrash/jxdm41:IncidentAugmentation/jxdm41:IncidentReportingOfficial/nc:RoleOfPersonReference/@s:ref";
    }

    public String getCrashDateTimeDisplay() {
        return NotificationBrokerUtils.returnFormattedNotificationEventDate(getNotificationEventDate(), isNotificationEventDateInclusiveOfTime());
    }

	@Override
	protected String getNotifyingAgencyOriXpath() {
		return null;
	}

	public String getCrashLocation() {
		return crashLocation;
	}

	public void setCrashLocation(String crashLocation) {
		this.crashLocation = crashLocation;
	}

}
