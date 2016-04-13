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
package org.ojbc.adapters.identifier;

import org.springframework.stereotype.Service;

@Service
public class IdentifierServiceAdapterConstants {
    public static final String TOPIC_DIALECT = "http://docs.oasis-open.org/wsn/t-1/TopicExpression/Concrete";

    public static final String SYSTEM_NAME = "RapbackDataStore";

    public static final String SOURCE_SYSTEM_NAME_TEXT = 
    		"http://ojbc.org/Services/WSDL/Organization_Identification_Results_Search_Request_Service/Subscriptions/1.0}RapbackDatastore";

    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    
    public static final String REPORT_FEDERAL_SUBSCRIPTION_UPDATE = "ReportFederalSubscriptionUpdate";

    public static final String REPORT_FEDERAL_SUBSCRIPTION_CREATION = "ReportFederalSubscriptionCreation";
}
