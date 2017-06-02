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
package org.ojbc.intermediaries.sn.topic.courtdispositionupdate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.camel.Message;
import org.ojbc.intermediaries.sn.SubscriptionNotificationConstants;
import org.ojbc.intermediaries.sn.notification.NotificationProcessor;
import org.ojbc.intermediaries.sn.notification.NotificationRequest;
import org.ojbc.intermediaries.sn.util.SubjectIdentifierUtils;

public class CourtDispositionUpdateNotificationProcessor extends NotificationProcessor {
	
	List<String> activeSubjectIdentifiers= new ArrayList<>(Arrays.asList(SubscriptionNotificationConstants.SID));
	
	List<List<String>> alternateConfiguredSubjectIdentifiers;
	
	@Override
	protected NotificationRequest makeNotificationRequestFromIncomingMessage(Message msg) throws Exception{
		
		CourtDispositionUpdateNotificationRequest courtDispositionUpdateNotificationRequest = new CourtDispositionUpdateNotificationRequest(msg); 
		
		Map<String, String> allowedSubjectIdentifiers = courtDispositionUpdateNotificationRequest.getSubjectIdentifiers();

		Map<String, String> finalSubjectIdentifiers = SubjectIdentifierUtils.returnFinalSubjectIdentifiers(allowedSubjectIdentifiers, activeSubjectIdentifiers);	
		courtDispositionUpdateNotificationRequest.setSubjectIdentifiers(finalSubjectIdentifiers);
		
		if (alternateConfiguredSubjectIdentifiers != null)
		{
			for (List<String> alternateConfiguredSubjectIdentifier : alternateConfiguredSubjectIdentifiers)
			{
				Map<String, String> finalAlternateSubjectIdentifiers = SubjectIdentifierUtils.returnFinalSubjectIdentifiers(allowedSubjectIdentifiers, alternateConfiguredSubjectIdentifier);
				courtDispositionUpdateNotificationRequest.getAlternateSubjectIdentifiers().add(finalAlternateSubjectIdentifiers);
			}	
		}	
		
		return courtDispositionUpdateNotificationRequest;
	}

	public List<String> getActiveSubjectIdentifiers() {
		return activeSubjectIdentifiers;
	}

	public void setActiveSubjectIdentifiers(List<String> activeSubjectIdentifiers) {
		this.activeSubjectIdentifiers = activeSubjectIdentifiers;
	}

	public List<List<String>> getAlternateConfiguredSubjectIdentifiers() {
		return alternateConfiguredSubjectIdentifiers;
	}

	public void setAlternateConfiguredSubjectIdentifiers(
			List<List<String>> alternateConfiguredSubjectIdentifiers) {
		this.alternateConfiguredSubjectIdentifiers = alternateConfiguredSubjectIdentifiers;
	}
	
}
