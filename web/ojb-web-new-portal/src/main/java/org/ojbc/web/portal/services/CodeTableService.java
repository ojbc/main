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
package org.ojbc.web.portal.services;

import java.util.List;
import java.util.Map;

import org.ojbc.web.portal.audit.AuditUser;
import org.ojbc.web.portal.security.UserAttributes;

public interface CodeTableService {
	public Map<String, String> getMuniDispositionCodeMap();
	public Map<String, String> getMuniFiledChargeCodeMap();
	public Map<String, String> getMuniAmendedChargeCodeMap();
	public Map<String, String> getMuniAlternateSentenceMap();
	public Map<String, String> getMuniReasonsForDismissalMap();
	public Map<String, String> getDaDispositionCodeMap();
	public Map<String, String> getDaFiledChargeCodeMap();
	public Map<String, String> getDaAmendedChargeCodeMap();
	public Map<String, String> getDaAlternateSentenceMap();
	public Map<String, String> getDaReasonsForDismissalMap();
	public Map<String, String> getDaProvisions();
	public Map<String, String> getAgencies();
	public Map<String, String> getAuditActivityTypes();
	public List<AuditUser> getUsers();
	public UserAttributes auditUserLoginReturnUserAttributes(AuditUser auditUser);
	public List<CodeTableEntry> getCodeTableEntries(String uri);
	
}