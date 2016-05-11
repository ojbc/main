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
package org.ojbc.intermediaries.sn.topic.courtdispositionupdate;

import java.util.HashMap;

import org.apache.camel.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.topic.arrest.ArrestNotificationRequest;
import org.ojbc.util.xml.XmlUtils;
import org.w3c.dom.Document;

public class CourtDispositionUpdateNotificationRequest extends NotificationRequest {
	
	private static final Log log = LogFactory.getLog( ArrestNotificationRequest.class );
	
	public CourtDispositionUpdateNotificationRequest(Message message) throws Exception{
	    this(message.getBody(Document.class));
	}

	public CourtDispositionUpdateNotificationRequest(Document document) throws Exception {
	    super(document);

		String sid = XmlUtils.xPathStringSearch(document,
				"/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/jxdm41:Person[@s:id=../nc:ActivityInvolvedPersonAssociation/nc:PersonReference/@s:ref]/jxdm41:PersonAugmentation/jxdm41:PersonStateFingerprintIdentification/nc:IdentificationID");

	    buildSubjectIdMap(sid);
	}
	
	private void buildSubjectIdMap(String sid) {
		subjectIdentifiers = new HashMap<String, String>();
		
		subjectIdentifiers.put(SubscriptionNotificationConstants.SID, sid);
		subjectIdentifiers.put(SubscriptionNotificationConstants.FIRST_NAME, personFirstName);
		subjectIdentifiers.put(SubscriptionNotificationConstants.LAST_NAME, personLastName);
		subjectIdentifiers.put(SubscriptionNotificationConstants.DATE_OF_BIRTH, personBirthDate);
	}

	@Override
	protected String getNotificationEventDateRootXpath() {

		return  "";
	}

	@Override
	protected String getNotifyingAgencyXpath() {

		return  "";
	}

	//TODO: Fix xpaths below
	@Override
	protected String getNotificationEventIdentifierXpath() {

		return  "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingCourtDispositionUpdate";
	}
	
	@Override
	protected String getNotificationAgencyPhoneNumberXpath() {
		// Add this to IEPD if required in notification message
		return  "";
	}

	@Override
	protected String getNotifyingSystemNameXPath() {
		// Add this to IEPD if required in notification message
		return  "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingCourtDispositionUpdate";
	}

	@Override
	public String getDescriptiveSubjectIdentifier() {
	    log.debug("Requesting descriptive subject identifier: " + getSubjectIdentifiers());
		return getSubjectIdentifiers().get(SubscriptionNotificationConstants.SID);
	}

	@Override
	protected String getOfficerNameReferenceXPath() {
		return  "";
	}

	@Override
	protected String getNotifyingAgencyOriXpath() {
		return  "/b-2:Notify/b-2:NotificationMessage/b-2:Message/notfm-exch:NotificationMessage/notfm-ext:NotifyingCourtDispositionUpdate";
	}
}