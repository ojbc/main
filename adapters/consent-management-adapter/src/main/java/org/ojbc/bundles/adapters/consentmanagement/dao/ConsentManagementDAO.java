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
package org.ojbc.bundles.adapters.consentmanagement.dao;

import java.time.LocalDateTime;
import java.util.List;

import org.ojbc.bundles.adapters.consentmanagement.model.Consent;

public interface ConsentManagementDAO {

	public List<Consent> returnConsentRecordsFromLast24hours();
	
	public List<Consent> searchForConsentRecords();
	
	public void updateConsentRecordsWithNoInterview() throws Exception;
	
	public Integer saveConsentDecision(Consent consent);
	
	public void updateConsentDecision(Integer consentDecisionID, Integer consentDecisionTypeID, String consenterUserID, String consenterUserFirstName, String consenterUserLastName, String consentDocumentControlNumber, LocalDateTime consentDecisionTimestamp);

	public Integer retrieveConsentDecisionType(String consentDecision) throws Exception;
	
	public Consent returnConsentRecordfromId(Integer consentDecisionID);
	
	public void deleteAllConsentRecords();
	
	public String retrieveConsentDecisionText(Integer consentDecisionTypeID) throws Exception;
}
