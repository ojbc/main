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
package org.ojbc.intermediaries.sn;

public class SubscriptionNotificationConstants {
    
	// constants used for incident topic subscriptions/notifications
	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String DATE_OF_BIRTH = "dateOfBirth";
	
	// constants used for arrest topic subscriptions/notifications
	public static final String SID = "SID";
	public static final String FBI_ID = "FBI_ID";
	public static final String SUBSCRIPTION_QUALIFIER = "subscriptionQualifier";
	
	//Rapback constants
	public static final String FEDERAL_RAP_SHEET_DISCLOSURE_INDICATOR = "federalRapSheetDisclosureIndicator";
	public static final String FEDERAL_RAP_SHEET_DISCLOSURE_ATTENTION_DESIGNATION_TEXT = "federalRapSheetDisclosureAttentionDesignationText";
}
